package com.github.royalstorm.android_task_manager.fragment.ui;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;

import com.github.royalstorm.android_task_manager.dao.EventInstance;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class TimePickerFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        EventInstance eventInstance = (EventInstance) bundle.getSerializable(EventInstance.class.getSimpleName());
        boolean IS_BEGIN_TIME = bundle.getBoolean("IS_BEGIN_TIME");

        if (IS_BEGIN_TIME) {
            GregorianCalendar start = timestampToGregorian(eventInstance.getStartedAt());

            return new TimePickerDialog(getActivity(),
                    (TimePickerDialog.OnTimeSetListener) getActivity(),
                    start.get(Calendar.HOUR_OF_DAY), start.get(Calendar.MINUTE),
                    DateFormat.is24HourFormat(getActivity()));
        } else {
            GregorianCalendar end = timestampToGregorian(eventInstance.getEndedAt());

            return new TimePickerDialog(getActivity(),
                    (TimePickerDialog.OnTimeSetListener) getActivity(),
                    end.get(Calendar.HOUR_OF_DAY), end.get(Calendar.MINUTE),
                    DateFormat.is24HourFormat(getActivity()));
        }
    }

    private GregorianCalendar timestampToGregorian(Long millis) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTimeInMillis(millis);

        return gregorianCalendar;
    }
}
