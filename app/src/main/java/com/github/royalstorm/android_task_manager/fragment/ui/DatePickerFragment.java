package com.github.royalstorm.android_task_manager.fragment.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.github.royalstorm.android_task_manager.dao.Task;

public class DatePickerFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        Task task = (Task) bundle.getSerializable(Task.class.getSimpleName());
        boolean IS_BEGIN_DATE = bundle.getBoolean("IS_BEGIN_DATE");

        if (IS_BEGIN_DATE)
            return new DatePickerDialog(getActivity(),
                    (DatePickerDialog.OnDateSetListener) getActivity(),
                    task.getBeginYear(), task.getBeginMonth(), task.getBeginDay());
        else
            return new DatePickerDialog(getActivity(),
                    (DatePickerDialog.OnDateSetListener) getActivity(),
                    task.getEndYear(), task.getEndMonth(), task.getEndDay());
    }
}
