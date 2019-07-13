package com.github.royalstorm.android_task_manager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.github.royalstorm.android_task_manager.R;
import com.github.royalstorm.android_task_manager.dao.EventPattern;
import com.github.royalstorm.android_task_manager.filter.InputFilterMinMax;
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

public class RepeatModeActivity extends AppCompatActivity {

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

    @BindView(R.id.never)
    RadioButton never;

    /*@BindView(R.id.selected_date)
    RadioButton selectedDate;
    @BindView(R.id.date)
    TextView date;*/

    @BindView(R.id.times)
    RadioButton times;
    @BindView(R.id.count)
    EditText count;

    private EventPattern eventPattern;

    private List<String> byDay;

    private Long endedAt;

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

        byDay = new ArrayList<>();

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
        interval.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2), new InputFilterMinMax(1, 99)});
        count.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2), new InputFilterMinMax(1, 99)});

        SU.setOnClickListener(v -> {
            if (SU.isChecked())
                byDay.add("SU");
            else
                byDay.remove("SU");
        });
        MO.setOnClickListener(v -> {
            if (MO.isChecked())
                byDay.add("MO");
            else
                byDay.remove("MO");
        });
        TU.setOnClickListener(v -> {
            if (TU.isChecked())
                byDay.add("TU");
            else
                byDay.remove("TU");
        });
        WE.setOnClickListener(v -> {
            if (WE.isChecked())
                byDay.add("WE");
            else
                byDay.remove("WE");
        });
        TH.setOnClickListener(v -> {
            if (TH.isChecked())
                byDay.add("TH");
            else
                byDay.remove("TH");
        });
        FR.setOnClickListener(v -> {
            if (FR.isChecked())
                byDay.add("FR");
            else
                byDay.remove("FR");
        });
        SA.setOnClickListener(v -> {
            if (SA.isChecked())
                byDay.add("SA");
            else
                byDay.remove("SA");
        });
    }

    private void initActivity() {
        SimpleDateFormat sdf = new SimpleDateFormat("d MMMM yyyy (E)", Locale.getDefault());

        RRule rRule = initRRule();

        //Setup repeat per time (first EditText)
        interval.setText(Integer.toString(rRule.getInterval()));

        //Setup values for spinner and checkboxes
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

        //Setup radio buttons
        if (rRule.getCount() == 0 && rRule.getUntil() == null) {
            ((RadioButton) endingCase.getChildAt(0)).setChecked(true);
            never.setSelected(true);
        } /*else if (rRule.getUntil() != null) {
            ((RadioButton) endingCase.getChildAt(1)).setChecked(true);
            selectedDate.setSelected(true);
            date.setText(sdf.format(eventPattern.getEndedAt()));
        }*/ else {
            ((RadioButton) endingCase.getChildAt(1)).setChecked(true);
            times.setSelected(true);
            count.setText(Integer.toString(rRule.getCount()));
        }

        //Special condition for radio buttons init (count = 1, until = null)
        if (eventPattern.getId() == null || eventPattern.getRrule() == null) {
            ((RadioButton) endingCase.getChildAt(0)).setChecked(true);
            never.setSelected(true);

            /*GregorianCalendar gregorianCalendar = new GregorianCalendar();
            gregorianCalendar.setTimeInMillis(eventPattern.getEndedAt());
            gregorianCalendar.add(Calendar.MONTH, 1);
            selectedDate.setText(sdf.format(gregorianCalendar.getTimeInMillis()));*/
        }
    }

    private RRule initRRule() {
        Bundle bundle = getIntent().getExtras();
        eventPattern = (EventPattern) bundle.getSerializable(EventPattern.class.getSimpleName());
        endedAt = eventPattern.getEndedAt();

        RRule rRule = new RRule();

        //If event is being created
        if (eventPattern.getId() == null || eventPattern.getRrule() == null) {
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

            byDay.add(weekdays.get(weekDay).name());

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
        rRule.setFreq(getFrequency());
        rRule.setCount(getCount());
        rRule.setInterval(getInterval());

        StringBuilder byDay = new StringBuilder(";BYDAY=");
        for (String s : this.byDay)
            byDay.append(s).append(",");

        eventPattern.setRrule(rRule.toIcal().substring(6) + byDay.substring(0, byDay.length() - 1));

        if (this.byDay.isEmpty()) {
            Snackbar.make(getWindow().getDecorView().
                    getRootView(), "Не выбраны дни повторения", Snackbar.LENGTH_LONG).show();
            return;
        }

        if (((RadioButton) endingCase.getChildAt(0)).isChecked())
            eventPattern.setEndedAt(Long.MAX_VALUE - 1);
        else
            eventPattern.setEndedAt(endedAt);

        Intent intent = new Intent();
        intent.putExtra(EventPattern.class.getSimpleName(), eventPattern);
        setResult(RESULT_OK, intent);
        finish();
    }

    private int getInterval() {
        return interval.getText().toString().isEmpty() ?
                1 : interval.getText().toString().length() > 2 ?
                99 : Integer.parseInt(interval.getText().toString());
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

    private int getCount() {
        if (((RadioButton) endingCase.getChildAt(1)).isChecked())
            return count.getText().toString().isEmpty() ?
                    1 : count.getText().toString().length() > 2 ?
                    99 : Integer.parseInt(count.getText().toString());

        return 0;
    }
}
