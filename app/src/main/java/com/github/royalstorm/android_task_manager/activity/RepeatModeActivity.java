package com.github.royalstorm.android_task_manager.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.royalstorm.android_task_manager.R;
import com.github.royalstorm.android_task_manager.dao.EventPattern;
import com.google.ical.values.Frequency;
import com.google.ical.values.RRule;
import com.google.ical.values.Weekday;
import com.google.ical.values.WeekdayNum;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RepeatModeActivity extends AppCompatActivity {

    @BindView(R.id.repeat_per_time)
    EditText repeatPerTime;
    @BindView(R.id.repeat_period)
    Spinner repeatPeriod;

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

    @BindView(R.id.selected_date)
    RadioButton selectedDate;
    @BindView(R.id.date)
    TextView date;

    @BindView(R.id.times)
    RadioButton times;
    @BindView(R.id.repeat_times)
    EditText repeatTimes;

    private String[] periods = {"День", "Неделя", "Месяц", "Год"};

    private EventPattern eventPattern;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_repeat_mode);
        setTitle("Повтор события");

        ButterKnife.bind(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, periods);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        repeatPeriod.setAdapter(adapter);

        initActivity();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_task_menu, menu);

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

    public List<Weekday> onCheckboxClicked(View view) {
        CheckBox day = (CheckBox) view;
        List<Weekday> weekdays = new ArrayList<>();
        switch (view.getId()) {
            case R.id.MO:
                if (day.isChecked())
                    weekdays.add(Weekday.MO);
                break;
            case R.id.TU:
                if (day.isChecked())
                    weekdays.add(Weekday.TU);
                break;
            case R.id.WE:
                if (day.isChecked())
                    weekdays.add(Weekday.WE);
                break;
            case R.id.TH:
                if (day.isChecked())
                    weekdays.add(Weekday.TH);
                break;
            case R.id.FR:
                if (day.isChecked())
                    weekdays.add(Weekday.FR);
                break;
            case R.id.SA:
                if (day.isChecked())
                    weekdays.add(Weekday.SA);
                break;
            case R.id.SU:
                if (day.isChecked())
                    weekdays.add(Weekday.SU);
                break;
        }
        return weekdays;
    }

    private void initActivity() {
        SimpleDateFormat sdf = new SimpleDateFormat("d MMMM yyyy (E)", Locale.getDefault());

        RRule rRule = initRRule();

        //Setup repeat per time (first EditText)
        repeatPerTime.setText(Integer.toString(rRule.getInterval()));

        //Setup values for spinner and checkboxes
        switch (rRule.getFreq()) {
            case DAILY:
                repeatPeriod.setSelection(0);
                break;
            case WEEKLY:
                repeatPeriod.setSelection(1);

                for (WeekdayNum day : rRule.getByDay()) {
                    switch (day.num) {
                        case 0:
                            MO.toggle();
                            break;
                        case 1:
                            TU.toggle();
                            break;
                        case 2:
                            WE.toggle();
                            break;
                        case 3:
                            TH.toggle();
                            break;
                        case 4:
                            FR.toggle();
                            break;
                        case 5:
                            SA.toggle();
                            break;
                        case 6:
                            SU.toggle();
                            break;
                    }
                }
                break;
            case MONTHLY:
                repeatPeriod.setSelection(2);
                break;
            case YEARLY:
                repeatPeriod.setSelection(3);
                break;
        }

        //Setup radio buttons
        if (rRule.getCount() == 0 && rRule.getUntil() == null) {
            ((RadioButton) endingCase.getChildAt(0)).setChecked(true);
            never.setSelected(true);
        } else if (rRule.getUntil() != null) {
            ((RadioButton) endingCase.getChildAt(1)).setChecked(true);
            selectedDate.setSelected(true);
            date.setText(sdf.format(eventPattern.getEndedAt()));
        } else {
            ((RadioButton) endingCase.getChildAt(3)).setChecked(true);
            times.setSelected(true);
            repeatTimes.setText(Integer.toString(rRule.getCount()));
        }
    }

    private RRule initRRule() {
        Bundle bundle = getIntent().getExtras();
        eventPattern = (EventPattern) bundle.getSerializable(EventPattern.class.getSimpleName());

        RRule rRule = new RRule();

        //If event is being created
        if (eventPattern.getId() == null) {
            rRule.setInterval(1);
            rRule.setFreq(Frequency.WEEKLY);
            rRule.setUntil(null);
            rRule.setCount(1);

            /*GregorianCalendar start = new GregorianCalendar();
            start.setTimeInMillis(eventPattern.getStartedAt());
            int weekDay = start.get(Calendar.DAY_OF_WEEK);


            List<WeekdayNum> weekdayNums = new ArrayList<>();
            weekdayNums.add(new WeekdayNum(5, Weekday.FR));
            rRule.setByDay(weekdayNums);*/
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
        //rRule.set

        eventPattern.setRrule(rRule.toString());
    }

    private Frequency getFrequency() {
        switch (repeatPeriod.getSelectedItemPosition()) {
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
        return Integer.parseInt(repeatTimes.getText().toString());
    }

    private int getInterval() {
        return Integer.parseInt(repeatPerTime.getText().toString());
    }
}
