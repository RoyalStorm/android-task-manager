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
    private RadioButton everyDay;
    private RadioButton everyWeek;
    private RadioButton everyMonth;
    private RadioButton everyYear;
    private RadioButton other;

    private View.OnClickListener modeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.never:
                    listener.applyMode("Не повторяется");
                    eventPattern.setRrule(null);
                    break;
                case R.id.every_day:
                    listener.applyMode("Каждый день");
                    eventPattern.setRrule("FREQ=DAILY;INTERVAL=1");
                    break;
                case R.id.every_week:
                    listener.applyMode("Каждую неделю");
                    eventPattern.setRrule("FREQ=WEEKLY;INTERVAL=1");
                    break;
                case R.id.every_month:
                    listener.applyMode("Каждый месяц");
                    eventPattern.setRrule("FREQ=MONTHLY;INTERVAL=1");
                    break;
                case R.id.every_year:
                    listener.applyMode("Каждый год");
                    eventPattern.setRrule("FREQ=YEARLY;INTERVAL =1");
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
        everyDay = view.findViewById(R.id.every_day);
        everyWeek = view.findViewById(R.id.every_week);
        everyMonth = view.findViewById(R.id.every_month);
        everyYear = view.findViewById(R.id.every_year);
        other = view.findViewById(R.id.other);

        never.setOnClickListener(modeListener);
        everyDay.setOnClickListener(modeListener);
        everyWeek.setOnClickListener(modeListener);
        everyMonth.setOnClickListener(modeListener);
        everyYear.setOnClickListener(modeListener);
        other.setOnClickListener(modeListener);

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
}
