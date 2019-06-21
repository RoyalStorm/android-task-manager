package com.github.royalstorm.android_task_manager.fragment.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.github.royalstorm.android_task_manager.dao.EventInstance;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DatePickerFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        //Task task = (Task) bundle.getSerializable(Task.class.getSimpleName());
        EventInstance eventInstance = (EventInstance) bundle.getSerializable(EventInstance.class.getSimpleName());
        boolean IS_BEGIN_DATE = bundle.getBoolean("IS_BEGIN_DATE");

        if (IS_BEGIN_DATE) {
            GregorianCalendar start = timestampToGregorian(eventInstance.getStartedAt());

            return new DatePickerDialog(getActivity(),
                    (DatePickerDialog.OnDateSetListener) getActivity(),
                    start.get(Calendar.YEAR), start.get(Calendar.MONTH), start.get(Calendar.DAY_OF_MONTH));
        } else {
            GregorianCalendar end = timestampToGregorian(eventInstance.getEndedAt());

            return new DatePickerDialog(getActivity(),
                    (DatePickerDialog.OnDateSetListener) getActivity(),
                    end.get(Calendar.YEAR), end.get(Calendar.MONTH), end.get(Calendar.DAY_OF_MONTH));
        }
    }

    private GregorianCalendar timestampToGregorian(Long millis) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTimeInMillis(millis);

        return gregorianCalendar;
    }
}
