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

public class SelectRepeatModeDialog extends AppCompatDialogFragment {

    private SelectRepeatModeDialogListener listener;

    private EventPattern eventPattern;

    private RadioButton never;
    private RadioButton daily;
    private RadioButton weekly;
    private RadioButton monthly;
    private RadioButton yearly;
    private RadioButton other;

    private static final String NEVER = null;
    private static final String DAILY = "FREQ=DAILY;INTERVAL=1";
    private static final String WEEKLY = "FREQ=WEEKLY;INTERVAL=1";
    private static final String MONTHLY = "FREQ=MONTHLY;INTERVAL=1";
    private static final String YEARLY = "FREQ=YEARLY;INTERVAL=1";

    private View.OnClickListener modeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.never:
                    listener.applyMode("Не повторяется");
                    eventPattern.setRrule(NEVER);
                    break;
                case R.id.daily:
                    listener.applyMode("Каждый день");
                    eventPattern.setRrule(DAILY);
                    break;
                case R.id.weekly:
                    listener.applyMode("Каждую неделю");
                    eventPattern.setRrule(WEEKLY);
                    break;
                case R.id.monthly:
                    listener.applyMode("Каждый месяц");
                    eventPattern.setRrule(MONTHLY);
                    break;
                case R.id.yearly:
                    listener.applyMode("Каждый год");
                    eventPattern.setRrule(YEARLY);
                    break;
                case R.id.other:
                    Intent intent = new Intent(getContext(), RepeatModeActivity.class);
                    intent.putExtra(EventPattern.class.getSimpleName(), eventPattern);
                    startActivity(intent);
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

        never.setOnClickListener(modeListener);
        daily.setOnClickListener(modeListener);
        weekly.setOnClickListener(modeListener);
        monthly.setOnClickListener(modeListener);
        yearly.setOnClickListener(modeListener);
        other.setOnClickListener(modeListener);

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
        void applyMode(String mode);
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
