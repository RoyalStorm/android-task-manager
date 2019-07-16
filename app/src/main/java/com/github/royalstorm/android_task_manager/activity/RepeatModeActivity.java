package com.github.royalstorm.android_task_manager.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.royalstorm.android_task_manager.R;
import com.github.royalstorm.android_task_manager.dao.EventPattern;
import com.github.royalstorm.android_task_manager.filter.InputFilterMinMax;
import com.github.royalstorm.android_task_manager.fragment.picker.DatePickerFragment;
import com.google.ical.values.Frequency;
import com.google.ical.values.RRule;
import com.google.ical.values.Weekday;
import com.google.ical.values.WeekdayNum;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RepeatModeActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    @BindView(R.id.interval)
    EditText interval;
    @BindView(R.id.frequency)
    Spinner frequency;

    @BindView(R.id.MO)
    CheckBox MO;
    @BindView(R.id.TU)
    CheckBox TU;
    @BindView(R.id.WE)
    CheckBox WE;
    @BindView(R.id.TH)
    CheckBox TH;
    @BindView(R.id.FR)
    CheckBox FR;
    @BindView(R.id.SA)
    CheckBox SA;
    @BindView(R.id.SU)
    CheckBox SU;

    @BindView(R.id.ending_case)
    RadioGroup endingCase;
    @BindView(R.id.rbNever)
    RadioButton never;
    @BindView(R.id.rbUntil)
    RadioButton rbUntil;
    @BindView(R.id.until)
    TextView until;
    @BindView(R.id.rbCount)
    RadioButton times;
    @BindView(R.id.count)
    EditText count;

    private EventPattern eventPattern;

    private List<String> byDayList;

    private Long endedAt;
    private SimpleDateFormat sdf = new SimpleDateFormat("d MMMM yyyy (E)", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_repeat_mode);
        setTitle("Повтор события");

        ButterKnife.bind(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                new String[]{"День", "Неделя", "Месяц", "Год"}
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        frequency.setAdapter(adapter);

        byDayList = new ArrayList<>();

        initActivity();
        setListeners();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_event_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save_event) {
            creteRRule();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setListeners() {
        until.setOnClickListener(v -> {
            DialogFragment picker = new DatePickerFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(GregorianCalendar.class.getSimpleName(), new GregorianCalendar());
            picker.setArguments(bundle);
            picker.show(getSupportFragmentManager(), "Until date");
        });

        interval.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2), new InputFilterMinMax(1, 99)});
        count.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2), new InputFilterMinMax(1, 99)});
    }

    private void initActivity() {
        RRule rRule = initRRule();

        interval.setText(Integer.toString(rRule.getInterval()));

        switch (rRule.getFreq()) {
            case DAILY:
                frequency.setSelection(0);
                break;
            case WEEKLY:
                frequency.setSelection(1);
                for (WeekdayNum day : rRule.getByDay()) {
                    switch (day.wday) {
                        case SU:
                            SU.toggle();
                            break;
                        case MO:
                            MO.toggle();
                            break;
                        case TU:
                            TU.toggle();
                            break;
                        case WE:
                            WE.toggle();
                            break;
                        case TH:
                            TH.toggle();
                            break;
                        case FR:
                            FR.toggle();
                            break;
                        case SA:
                            SA.toggle();
                            break;
                    }
                }
                break;
            case MONTHLY:
                frequency.setSelection(2);
                break;
            case YEARLY:
                frequency.setSelection(3);
                break;
        }

        if (rRule.getCount() == 0) {
            ((RadioButton) endingCase.getChildAt(0)).setChecked(true);
            never.setSelected(true);
        } else if (rRule.getUntil() != null) {
            ((RadioButton) endingCase.getChildAt(1)).setChecked(true);
            rbUntil.setSelected(true);
            until.setText(sdf.format(eventPattern.getEndedAt()));
        } else {
            ((RadioButton) endingCase.getChildAt(3)).setChecked(true);
            times.setSelected(true);
            count.setText(Integer.toString(rRule.getCount()));
        }

        //Special condition for radio buttons init (count = 1, until = null)
        if (eventPattern.getId() == null || eventPattern.getRrule() == null) {
            ((RadioButton) endingCase.getChildAt(0)).setChecked(true);
            never.setSelected(true);

            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            gregorianCalendar.setTimeInMillis(eventPattern.getEndedAt());
            gregorianCalendar.add(Calendar.MONTH, 1);
            until.setText(sdf.format(gregorianCalendar.getTimeInMillis()));
        }
    }

    private RRule initRRule() {
        Bundle bundle = getIntent().getExtras();
        eventPattern = (EventPattern) bundle.getSerializable(EventPattern.class.getSimpleName());
        endedAt = eventPattern.getEndedAt();

        RRule rRule = new RRule();

        //If event is being created
        if (eventNotCreatedOrNotRepeated(eventPattern)) {
            rRule.setInterval(1);
            rRule.setFreq(Frequency.WEEKLY);

            GregorianCalendar start = new GregorianCalendar();
            start.setTimeInMillis(eventPattern.getStartedAt());
            int weekDay = start.get(Calendar.DAY_OF_WEEK);

            HashMap<Integer, Weekday> weekdays = new HashMap<Integer, Weekday>() {{
                put(1, Weekday.SU);
                put(2, Weekday.MO);
                put(3, Weekday.TU);
                put(4, Weekday.WE);
                put(5, Weekday.TH);
                put(6, Weekday.FR);
                put(7, Weekday.SA);
            }};

            byDayList.add(weekdays.get(weekDay).name());

            rRule.setByDay(new ArrayList<WeekdayNum>() {{
                add(new WeekdayNum(weekDay, weekdays.get(weekDay)));
            }});
            rRule.setUntil(null);
            rRule.setCount(1);
        } else { //If event was created
            try {
                rRule = new RRule("RRULE:" + eventPattern.getRrule());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return rRule;
    }

    private void creteRRule() {
        RRule rRule = new RRule();
        rRule.setInterval(getInterval());
        rRule.setFreq(getFrequency());
        setSelectedDays();
        rRule.setCount(getCount());

        StringBuilder byDay = new StringBuilder(";BYDAY=");
        for (String day : byDayList)
            byDay.append(day).append(",");

        eventPattern.setRrule(rRule.toIcal().substring(6) + byDay.substring(0, byDay.length() - 1));

        if (byDayList.isEmpty()) {
            Snackbar.make(getWindow().getDecorView().
                    getRootView(), "Не выбраны дни повторения", Snackbar.LENGTH_LONG).show();
            return;
        }

        if (endingCase.getCheckedRadioButtonId() == R.id.rbNever)
            eventPattern.setEndedAt(Long.MAX_VALUE - 1);
        else
            eventPattern.setEndedAt(endedAt);

        Log.d("_____________", eventPattern.toString());

        Intent intent = new Intent();
        intent.putExtra(EventPattern.class.getSimpleName(), eventPattern);
        setResult(RESULT_OK, intent);
        finish();
    }

    private boolean eventNotCreatedOrNotRepeated(EventPattern eventPattern) {
        return eventPattern.getId() == null || eventPattern.getRrule() == null;
    }

    private int getInterval() {
        return interval.getText().toString().isEmpty() ?
                1 : Integer.parseInt(interval.getText().toString());
    }

    private Frequency getFrequency() {
        switch (frequency.getSelectedItemPosition()) {
            case 0:
                return Frequency.DAILY;
            case 1:
                return Frequency.WEEKLY;
            case 2:
                return Frequency.MONTHLY;
            case 3:
                return Frequency.YEARLY;
            default:
                return null;
        }
    }

    private void setSelectedDays() {
        byDayList.clear();

        if (MO.isChecked())
            byDayList.add("MO");
        if (TU.isChecked())
            byDayList.add("TU");
        if (WE.isChecked())
            byDayList.add("WE");
        if (TH.isChecked())
            byDayList.add("TH");
        if (FR.isChecked())
            byDayList.add("FR");
        if (SA.isChecked())
            byDayList.add("SA");
        if (SU.isChecked())
            byDayList.add("SU");
    }

    /*private GregorianCalendar getUntil() {
        if (endingCase.getCheckedRadioButtonId() == R.id.rbUntil)
            return
    }*/

    private int getCount() {
        if (endingCase.getCheckedRadioButtonId() == R.id.rbCount)
            return count.getText().toString().isEmpty() ?
                    1 : Integer.parseInt(count.getText().toString());

        return 0;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        until.setText(sdf.format(new GregorianCalendar(year, month, dayOfMonth).getTimeInMillis()));
        rbUntil.toggle();
    }
}
