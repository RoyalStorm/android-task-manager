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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.annimon.stream.Stream;
import com.github.royalstorm.android_task_manager.R;
import com.github.royalstorm.android_task_manager.dao.Action;
import com.github.royalstorm.android_task_manager.dao.EntityType;
import com.github.royalstorm.android_task_manager.dao.PermissionRequest;
import com.github.royalstorm.android_task_manager.fragment.ActivationTokenFragment;
import com.github.royalstorm.android_task_manager.fragment.DayFragment;
import com.github.royalstorm.android_task_manager.fragment.MonthFragment;
import com.github.royalstorm.android_task_manager.fragment.RulesManagementFragment;
import com.github.royalstorm.android_task_manager.fragment.WeekFragment;
import com.github.royalstorm.android_task_manager.service.PermissionService;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        PermissionService.RequestPermissionCallback {

    private RetrofitClient retrofitClient = RetrofitClient.getInstance();

    private NavigationView navigationView;

    private DrawerLayout drawer;

    private de.hdodenhof.circleimageview.CircleImageView accountImage;
    private TextView accountName;
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
        accountName = navigationView.getHeaderView(0).findViewById(R.id.account_name);
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
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else
            drawer.openDrawer(GravityCompat.START);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.export_from_server:
                exportToICal(userToken);
                break;
            case R.id.share_all_events:
                shareAllCalendar();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

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
            case R.id.nav_sharing:
                getSupportFragmentManager().beginTransaction().replace(R.id.calendarContainer,
                        new RulesManagementFragment()).commit();
                break;
            case R.id.nav_activate_token:
                getSupportFragmentManager().beginTransaction().replace(R.id.calendarContainer,
                        new ActivationTokenFragment()).commit();
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

        signInButton.setVisibility(View.VISIBLE);
        signOutButton.setVisibility(View.GONE);

        drawer.closeDrawer(GravityCompat.START);

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
        } else
            return true;
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

            accountName.setText(account.getDisplayName());

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
                userToken = firebaseAuth.getCurrentUser().getIdToken(false).getResult().getToken();

                navigationView.setNavigationItemSelectedListener(MainActivity.this);
            } else
                userToken = null;
        });
    }

    private void shareAllCalendar() {
        PermissionService permissionService = new PermissionService(this);
        updateUserToken();

        List<PermissionRequest> permissionRequests = new ArrayList<>();

        PermissionRequest readEventPermissionRequest = new PermissionRequest();
        readEventPermissionRequest.setAction(Action.READ.name());
        readEventPermissionRequest.setEntityType(EntityType.EVENT.name());
        readEventPermissionRequest.setEntityId(null);

        permissionRequests.add(readEventPermissionRequest);

        PermissionRequest updateEventPermissionRequest = new PermissionRequest();
        updateEventPermissionRequest.setAction(Action.UPDATE.name());
        updateEventPermissionRequest.setEntityType(EntityType.EVENT.name());
        updateEventPermissionRequest.setEntityId(null);

        PermissionRequest updateEventPatternPermissionRequest = new PermissionRequest();
        updateEventPatternPermissionRequest.setAction(Action.UPDATE.name());
        updateEventPatternPermissionRequest.setEntityType(EntityType.PATTERN.name());
        updateEventPatternPermissionRequest.setEntityId(null);

        permissionRequests.add(updateEventPermissionRequest);
        permissionRequests.add(updateEventPatternPermissionRequest);

        PermissionRequest deleteEventPermissionRequest = new PermissionRequest();
        deleteEventPermissionRequest.setAction(Action.DELETE.name());
        deleteEventPermissionRequest.setEntityType(EntityType.EVENT.name());
        deleteEventPermissionRequest.setEntityId(null);

        PermissionRequest deleteEventPatternPermissionRequest = new PermissionRequest();
        deleteEventPatternPermissionRequest.setAction(Action.DELETE.name());
        deleteEventPatternPermissionRequest.setEntityType(EntityType.PATTERN.name());
        deleteEventPatternPermissionRequest.setEntityId(null);

        permissionRequests.add(deleteEventPermissionRequest);
        permissionRequests.add(deleteEventPatternPermissionRequest);

        permissionService.generateSharingLink(Stream.of(permissionRequests).toArray(PermissionRequest[]::new), userToken);
    }

    @Override
    public void requestPermissionSuccess(boolean success, String sharingToken) {
        Intent shareWithFriendsIntent = new Intent();
        shareWithFriendsIntent.setAction(Intent.ACTION_SEND);
        shareWithFriendsIntent.putExtra(Intent.EXTRA_TEXT, sharingToken);
        shareWithFriendsIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareWithFriendsIntent, "Выберите пользователя"));
    }

    private void updateUserToken() {
        if (userToken == null)
            firebaseAuth.getCurrentUser().getIdToken(true).addOnCompleteListener(task -> {
                userToken = task.getResult().getToken();
            });
        else
            userToken = firebaseAuth.getCurrentUser().getIdToken(false).getResult().getToken();

    }
}
