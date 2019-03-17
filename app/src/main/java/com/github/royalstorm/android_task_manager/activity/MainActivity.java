package com.github.royalstorm.android_task_manager.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.github.royalstorm.android_task_manager.R;
import com.github.royalstorm.android_task_manager.fragment.DayFragment;
import com.github.royalstorm.android_task_manager.fragment.MonthFragment;
import com.github.royalstorm.android_task_manager.fragment.WeekFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;

    DayFragment dayFragment;
    WeekFragment weekFragment;
    MonthFragment monthFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        initFragments();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.calendarContainer, monthFragment)
                    .commit();
            navigationView.setCheckedItem(R.id.nav_month);
        }

        /*CalendarView calendar = findViewById(R.id.calendar);
        calendar.setOnDateChangeListener(
                new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                        Intent intent = new Intent(MainActivity.this, DayActivity.class);

                        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                        Date date = new Date(year, month, dayOfMonth - 1);
                        String dayOfWeek = sdf.format(date);

                        switch (dayOfWeek) {
                            case "Sunday":
                                dayOfWeek = "Воскресенье";
                                break;

                            case "Monday":
                                dayOfWeek = "Понедельник";
                                break;

                            case "Tuesday":
                                dayOfWeek = "Вторник";
                                break;

                            case "Wednesday":
                                dayOfWeek = "Среда";
                                break;

                            case "Thursday":
                                dayOfWeek = "Четверг";
                                break;

                            case "Friday":
                                dayOfWeek = "Пятница";
                                break;

                            case "Saturday":
                                dayOfWeek = "Суббота";
                                break;
                        }

                        intent.putExtra("currentDay", dayOfWeek);

                        String currentDate = dayOfMonth + "/" + (month + 1) + "/" + year;

                        intent.putExtra("currentDate", currentDate);

                        startActivity(intent);
                    }
                }
        );*/
    }

    private void initFragments() {
        dayFragment = new DayFragment();
        weekFragment = new WeekFragment();
        monthFragment = new MonthFragment();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_day:
                getSupportFragmentManager().beginTransaction().replace(R.id.calendarContainer,
                        new DayFragment()).commit();
                break;

            case R.id.nav_week:
                getSupportFragmentManager().beginTransaction().replace(R.id.calendarContainer,
                        new WeekFragment()).commit();
                break;

            case R.id.nav_month:
                getSupportFragmentManager().beginTransaction().replace(R.id.calendarContainer,
                        new MonthFragment()).commit();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}
