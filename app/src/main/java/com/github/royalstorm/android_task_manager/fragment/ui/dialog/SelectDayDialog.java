package com.github.royalstorm.android_task_manager.fragment.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.github.royalstorm.android_task_manager.R;

public class SelectDayDialog extends AppCompatDialogFragment {
    private TextView currentDay;
    private DatePicker datePicker;

    private SelectDayDialogListener selectDayDialogListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.select_date_dialog, null);

        builder.setView(view)
                .setTitle("Выбрать дату")
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int day = datePicker.getDayOfMonth();
                        int month = datePicker.getMonth();
                        int year = datePicker.getYear();

                        Bundle bundle = new Bundle();
                        bundle.putInt("day", day);
                        bundle.putInt("month", month);
                        bundle.putInt("year", year);

                        selectDayDialogListener.setDay(year, month, day);
                    }
                });

        datePicker = view.findViewById(R.id.date_picker);

        Bundle bundle = this.getArguments();

        if (bundle != null) {
            int day = bundle.getInt("day");
            int month = bundle.getInt("month");
            int year = bundle.getInt("year");

            datePicker.init(year, month, day, null);
        }

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            selectDayDialogListener = (SelectDayDialogListener) getTargetFragment();
        }catch (ClassCastException e){
            Log.e("______________:", "onAttach: ClassCastException : " + e.getMessage() );
        }
    }

    public interface SelectDayDialogListener {
        void setDay(int year, int month, int day);
    }
}
