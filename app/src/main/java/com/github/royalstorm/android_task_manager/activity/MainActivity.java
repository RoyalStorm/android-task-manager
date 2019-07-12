package com.github.royalstorm.android_task_manager.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.royalstorm.android_task_manager.R;
import com.github.royalstorm.android_task_manager.fragment.DayFragment;
import com.github.royalstorm.android_task_manager.fragment.MonthFragment;
import com.github.royalstorm.android_task_manager.fragment.WeekFragment;
import com.github.royalstorm.android_task_manager.shared.RetrofitClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RetrofitClient retrofitClient = RetrofitClient.getInstance();

    private NavigationView navigationView;

    private DrawerLayout drawer;

    private ImageView accountImage;
    private SignInButton signInButton;
    private Button signOutButton;

    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInOptions gso;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private String userToken = null;

    private static int RC_SIGN_IN = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(null);

        accountImage = navigationView.getHeaderView(0).findViewById(R.id.account_image);
        signInButton = navigationView.getHeaderView(0).findViewById(R.id.sign_in_button);
        signOutButton = navigationView.getHeaderView(0).findViewById(R.id.sign_out_button);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.client_secret))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        firebaseAuth = FirebaseAuth.getInstance();

        if (savedInstanceState == null && firebaseAuth.getCurrentUser() != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.calendarContainer, new MonthFragment())
                    .commit();
            navigationView.setCheckedItem(R.id.nav_month);
        }

        navigationView.getHeaderView(0).findViewById(R.id.sign_in_button).setOnClickListener(v -> {
            signIn();
        });
        navigationView.getHeaderView(0).findViewById(R.id.sign_out_button).setOnClickListener(v -> {
            signOut();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if (account != null) {
            updateUI(account);
            firebaseAuth.getCurrentUser().getIdToken(true).addOnCompleteListener(task -> {
                userToken = task.getResult().getToken();
                navigationView.setNavigationItemSelectedListener(MainActivity.this);
            });
        } else {
            userToken = null;
            signInButton.setVisibility(View.VISIBLE);
            signOutButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.export_from_server) {
            exportToICal(userToken);
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

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        firebaseAuth.signOut();
        userToken = null;
        mGoogleSignInClient.signOut().addOnCompleteListener(this, task -> {
            signInButton.setVisibility(View.VISIBLE);
            signOutButton.setVisibility(View.GONE);
        });

        if (navigationView.getCheckedItem() != null)
            navigationView.getCheckedItem().setChecked(true);

        navigationView.setNavigationItemSelectedListener(null);

        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragments)
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

    private void exportToICal(String userToken) {
        retrofitClient.getCalendarRepository().exportFromServer(userToken).enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String iCal = response.body().string();

                    isStoragePermissionGranted();

                    Calendar calendar = new GregorianCalendar();
                    SimpleDateFormat creationDate = new SimpleDateFormat("dd-MM-yyyy_HH:mm");
                    try {
                        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath() + "/Weekler_" + creationDate.format(calendar.getTime()) + ".ics");
                        file.createNewFile();
                        FileWriter fileWriter = new FileWriter(file);
                        fileWriter.write(iCal);
                        fileWriter.flush();
                        fileWriter.close();
                        Toast.makeText(getApplicationContext(), "Экспортирование прошло успешно", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            updateUI(account);
        } catch (ApiException e) {
            Log.w("FAILED_SIGNED_IN", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void updateUI(GoogleSignInAccount account) {
        if (account != null) {
            Picasso.with(this).load(account.getPhotoUrl()).into(accountImage, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {

                }
            });

            signInButton.setVisibility(View.GONE);
            signOutButton.setVisibility(View.VISIBLE);
            firebaseAuthWithGoogle(account);
        } else {
            signInButton.setVisibility(View.VISIBLE);
            signOutButton.setVisibility(View.GONE);
            userToken = null;
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount googleSignInAccount) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);

        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                Log.d("AUTH DONE", "SUCCESS");

                firebaseUser = firebaseAuth.getCurrentUser();
                userToken = firebaseUser.getIdToken(false).getResult().getToken();

                navigationView.setNavigationItemSelectedListener(MainActivity.this);
            } else {
                Log.d("AUTH FAIL", task.getException().toString());
                userToken = null;
            }
        });
    }
}
