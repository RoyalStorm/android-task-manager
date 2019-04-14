package com.github.royalstorm.android_task_manager.fragment.ui;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;

import com.github.royalstorm.android_task_manager.dao.Task;

public class TimePickerFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        Task task = (Task) bundle.getSerializable(Task.class.getSimpleName());
        boolean IS_BEGIN_TIME = bundle.getBoolean("IS_BEGIN_TIME");

        if (IS_BEGIN_TIME)
            return new TimePickerDialog(getActivity(),
                    (TimePickerDialog.OnTimeSetListener) getActivity(),
                    task.getBeginHour(), task.getBeginMinute(),
                    DateFormat.is24HourFormat(getActivity()));
        else
            return new TimePickerDialog(getActivity(),
                    (TimePickerDialog.OnTimeSetListener) getActivity(),
                    task.getEndHour(), task.getEndMinute(),
                    DateFormat.is24HourFormat(getActivity()));
    }
}
