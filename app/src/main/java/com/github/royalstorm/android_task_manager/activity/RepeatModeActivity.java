package com.github.royalstorm.android_task_manager.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.google.ical.values.WeekdayNum;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RepeatModeActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    @BindView(R.id.et_interval)
    EditText etInterval;
    @BindView(R.id.spinner_frequency)
    Spinner spinnerFrequency;

    @BindView(R.id.cb_mo)
    CheckBox cbMo;
    @BindView(R.id.cb_tu)
    CheckBox cbTu;
    @BindView(R.id.cb_we)
    CheckBox cbWe;
    @BindView(R.id.cb_th)
    CheckBox cbTh;
    @BindView(R.id.cb_fr)
    CheckBox cbFr;
    @BindView(R.id.cb_sa)
    CheckBox cbSa;
    @BindView(R.id.cb_su)
    CheckBox cbSu;

    @BindView(R.id.rg_ending_mode)
    RadioGroup rgEndingMode;
    @BindView(R.id.rb_never)
    RadioButton rbNever;
    @BindView(R.id.rb_until)
    RadioButton rbUntil;
    @BindView(R.id.tv_until)
    TextView tvUntil;
    @BindView(R.id.rb_count)
    RadioButton rbCount;
    @BindView(R.id.et_count)
    EditText etCount;

    private SimpleDateFormat untilSimpleDateFormat;

    private EventPattern eventPattern;

    private GregorianCalendar untilGregorianCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_repeat_mode);
        setTitle("Повтор события");

        ButterKnife.bind(this);

        untilSimpleDateFormat = new SimpleDateFormat("d MMMM yyyy (E)", Locale.getDefault());

        eventPattern = (EventPattern) getIntent().getExtras().getSerializable(EventPattern.class.getSimpleName());

        untilGregorianCalendar = new GregorianCalendar();
        untilGregorianCalendar.setTimeInMillis(eventPattern.getStartedAt() + eventPattern.getDuration());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                new String[]{"День", "Неделя", "Месяц", "Год"}
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFrequency.setAdapter(adapter);

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
            createRRule();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        rbUntil.toggle();
        untilGregorianCalendar.set(year, month, dayOfMonth);
        tvUntil.setText(untilSimpleDateFormat.format(untilGregorianCalendar.getTimeInMillis()));
    }

    private void setListeners() {
        tvUntil.setOnClickListener(v -> {
            DialogFragment picker = new DatePickerFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(GregorianCalendar.class.getSimpleName(), untilGregorianCalendar);
            picker.setArguments(bundle);
            picker.show(getSupportFragmentManager(), "Until date");
        });

        etInterval.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2), new InputFilterMinMax(1, 99)});
        etCount.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2), new InputFilterMinMax(1, 99)});
    }

    private void initActivity() {
        RRule rRule = initRRule();

        etInterval.setText(Integer.toString(rRule.getInterval()));

        switch (rRule.getFreq()) {
            case DAILY:
                spinnerFrequency.setSelection(0);
                break;
            case WEEKLY:
                spinnerFrequency.setSelection(1);
                for (WeekdayNum day : rRule.getByDay()) {
                    switch (day.wday) {
                        case MO:
                            cbMo.toggle();
                            break;
                        case TU:
                            cbTu.toggle();
                            break;
                        case WE:
                            cbWe.toggle();
                            break;
                        case TH:
                            cbTh.toggle();
                            break;
                        case FR:
                            cbFr.toggle();
                            break;
                        case SA:
                            cbSa.toggle();
                            break;
                        case SU:
                            cbSu.toggle();
                            break;
                    }
                }
                break;
            case MONTHLY:
                spinnerFrequency.setSelection(2);
                break;
            case YEARLY:
                spinnerFrequency.setSelection(3);
                break;
        }

        if (rRule.getCount() == 0 && rRule.getUntil() == null)
            rbNever.toggle();
        else if (rRule.getUntil() != null)
            rbUntil.toggle();
        else {
            rbCount.toggle();
            etCount.setText(Integer.toString(rRule.getCount()));
        }

        untilGregorianCalendar.add(Calendar.MONTH, 1);
        tvUntil.setText(untilSimpleDateFormat.format(untilGregorianCalendar.getTimeInMillis()));
        etCount.setText("1");
    }

    private RRule initRRule() {
        RRule rRule = new RRule();

        if (isEventHasEmptyRRule(eventPattern)) {
            rRule.setInterval(1);
            rRule.setFreq(Frequency.WEEKLY);

            rRule.setUntil(null);
        } else
            try {
                rRule = new RRule("RRULE:" + eventPattern.getRrule());
            } catch (ParseException e) {
                e.printStackTrace();
            }

        return rRule;
    }

    private boolean isEventHasEmptyRRule(EventPattern eventPattern) {
        return eventPattern.getRrule() == null;
    }

    private void createRRule() {
        RRule rRule = new RRule();
        rRule.setInterval(getInterval());
        rRule.setFreq(getFrequency());

        List<String> byDay = getByDay();
        if (byDay.isEmpty()) {
            Snackbar.make(getWindow().getDecorView().
                    getRootView(), "Не выбраны дни повторения", Snackbar.LENGTH_LONG).show();
            return;
        }

        StringBuilder byDayPart = new StringBuilder(";BYDAY=");
        for (String day : byDay)
            byDayPart.append(day).append(",");

        eventPattern.setEndedAt(getInfinitePeriod(eventPattern));
        eventPattern.setEndedAt(getUntil(eventPattern));
        eventPattern.setRrule(rRule.toIcal().substring(6) + byDayPart.substring(0, byDayPart.length() - 1));

        Intent intent = new Intent();
        intent.putExtra(EventPattern.class.getSimpleName(), eventPattern);
        setResult(RESULT_OK, intent);
        finish();
    }

    private int getInterval() {
        return etInterval.getText().toString().isEmpty() ?
                1 : Integer.parseInt(etInterval.getText().toString());
    }

    private Frequency getFrequency() {
        switch (spinnerFrequency.getSelectedItemPosition()) {
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

    private List<String> getByDay() {
        List<String> byDay = new ArrayList<>();

        if (cbMo.isChecked())
            byDay.add("MO");
        if (cbTu.isChecked())
            byDay.add("TU");
        if (cbWe.isChecked())
            byDay.add("WE");
        if (cbTh.isChecked())
            byDay.add("TH");
        if (cbFr.isChecked())
            byDay.add("FR");
        if (cbSa.isChecked())
            byDay.add("SA");
        if (cbSu.isChecked())
            byDay.add("SU");

        return byDay;
    }

    //If selected "never"
    private Long getInfinitePeriod(EventPattern eventPattern) {
        if (rgEndingMode.getCheckedRadioButtonId() == R.id.rb_never)
            return Long.MAX_VALUE - 1;

        return eventPattern.getEndedAt();
    }

    private Long getUntil(EventPattern eventPattern) {
        if (rgEndingMode.getCheckedRadioButtonId() == R.id.rb_until)
            return untilGregorianCalendar.getTimeInMillis();

        return eventPattern.getEndedAt();
    }

    /*private Long getCount() {
        if (rgEndingMode.getCheckedRadioButtonId() == R.id.rb_count) {
            return etCount.getText().toString().isEmpty() ?
                    1 : Integer.parseInt(etCount.getText().toString());

            //RecurrenceRule ruleHelper = new RecurrenceRule(stRule);
            //RecurrenceRuleIterator it = ruleHelper.iterator(new DateTime(eventPattern.getStartedAt()));
        }

        return 0;
    }*/
}
