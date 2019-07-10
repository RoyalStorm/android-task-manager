package com.github.royalstorm.android_task_manager.fragment.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;

import com.github.royalstorm.android_task_manager.R;
import com.github.royalstorm.android_task_manager.activity.RepeatModeActivity;
import com.github.royalstorm.android_task_manager.dao.EventPattern;

import static com.github.royalstorm.android_task_manager.shared.Frequency.DAILY;
import static com.github.royalstorm.android_task_manager.shared.Frequency.MONTHLY;
import static com.github.royalstorm.android_task_manager.shared.Frequency.NEVER;
import static com.github.royalstorm.android_task_manager.shared.Frequency.WEEKLY;
import static com.github.royalstorm.android_task_manager.shared.Frequency.YEARLY;

public class SelectRepeatModeDialog extends AppCompatDialogFragment {

    private SelectRepeatModeDialogListener listener;

    private EventPattern eventPattern;

    private RadioButton never;
    private RadioButton daily;
    private RadioButton weekly;
    private RadioButton monthly;
    private RadioButton yearly;
    private RadioButton other;

    private int SELECT_REPEAT_MODE_ACTIVITY = 1;

    private View.OnClickListener repeatModeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.never:
                    listener.applyMode("Не повторяется", NEVER, eventPattern.getEndedAt());
                    break;
                case R.id.daily:
                    listener.applyMode("Каждый день", DAILY, Long.MAX_VALUE - 1);
                    break;
                case R.id.weekly:
                    listener.applyMode("Каждую неделю", WEEKLY, Long.MAX_VALUE - 1);
                    break;
                case R.id.monthly:
                    listener.applyMode("Каждый месяц", MONTHLY, Long.MAX_VALUE - 1);
                    break;
                case R.id.yearly:
                    listener.applyMode("Каждый год", YEARLY, Long.MAX_VALUE - 1);
                    break;
                case R.id.other:
                    Intent intent = new Intent(getContext(), RepeatModeActivity.class);
                    intent.putExtra(EventPattern.class.getSimpleName(), eventPattern);
                    startActivityForResult(intent, SELECT_REPEAT_MODE_ACTIVITY);
                    break;
            }
            dismiss();
        }
    };

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.select_repeat_mode_dialog, null);

        builder.setView(view);

        eventPattern = (EventPattern) this.getArguments().getSerializable(EventPattern.class.getSimpleName());

        never = view.findViewById(R.id.never);
        daily = view.findViewById(R.id.daily);
        weekly = view.findViewById(R.id.weekly);
        monthly = view.findViewById(R.id.monthly);
        yearly = view.findViewById(R.id.yearly);
        other = view.findViewById(R.id.other);

        never.setOnClickListener(repeatModeListener);
        daily.setOnClickListener(repeatModeListener);
        weekly.setOnClickListener(repeatModeListener);
        monthly.setOnClickListener(repeatModeListener);
        yearly.setOnClickListener(repeatModeListener);
        other.setOnClickListener(repeatModeListener);

        initRepeatMode(eventPattern);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (SelectRepeatModeDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "Must implement SelectRepeatModeDialogListener");
        }
    }

    public interface SelectRepeatModeDialogListener {
        void applyMode(String mode, String rRule, Long endedAt);
    }

    private void initRepeatMode(EventPattern eventPattern) {
        if (eventPattern.getRrule() == NEVER)
            never.setChecked(true);
        else
            switch (eventPattern.getRrule()) {
                case DAILY:
                    daily.setChecked(true);
                    break;
                case WEEKLY:
                    weekly.setChecked(true);
                    break;
                case MONTHLY:
                    monthly.setChecked(true);
                    break;
                case YEARLY:
                    yearly.setChecked(true);
                    break;
                default:
                    other.setChecked(true);
                    break;
            }
    }
}
