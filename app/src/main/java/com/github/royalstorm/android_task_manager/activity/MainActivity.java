package com.github.royalstorm.android_task_manager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.royalstorm.android_task_manager.R;
import com.github.royalstorm.android_task_manager.fragment.DayFragment;
import com.github.royalstorm.android_task_manager.fragment.MonthFragment;
import com.github.royalstorm.android_task_manager.fragment.WeekFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;

    @BindView(R.id.account_image)
    ImageView accountImage;
    @BindView(R.id.account_name)
    TextView accountName;
    @BindView(R.id.account_phone)
    TextView accountPhone;

    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build();

    GoogleSignInClient mGoogleSignInClient;

    private int RC_SIGN_IN = 0;

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null)
            updateUI(account);
    }

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

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.calendarContainer, new MonthFragment())
                    .commit();
            navigationView.setCheckedItem(R.id.nav_month);
        }

        /*View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header);

        headerLayout.findViewById(R.id.sign_in_button).setOnClickListener(v -> {
            signIn();
        });*/
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
        }
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

            case R.id.dark:
                SwitchCompat darkTheme = findViewById(R.id.dark_theme);
                darkTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked)
                        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    else
                        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                });
                break;
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("FAILED_SIGNED_IN", "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    private void updateUI(GoogleSignInAccount account) {
        Picasso.with(this).load(account.getPhotoUrl()).into(accountImage, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {

            }
        });

        accountName.setText(account.getDisplayName());
        accountPhone.setText(account.getEmail());
    }
}
