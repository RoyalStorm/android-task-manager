package com.github.royalstorm.android_task_manager.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.github.royalstorm.android_task_manager.R;
import com.github.royalstorm.android_task_manager.shared.RetrofitClient;
import com.google.firebase.auth.FirebaseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivationTokenFragment extends Fragment {

    private RetrofitClient retrofitClient;

    private FirebaseAuth firebaseAuth;
    private String userToken;

    private EditText tokenField;
    private ImageButton confirm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activation_token, container, false);

        retrofitClient = RetrofitClient.getInstance();

        firebaseAuth = FirebaseAuth.getInstance();
        userToken = firebaseAuth.getCurrentUser().getIdToken(false).getResult().getToken();

        tokenField = view.findViewById(R.id.token_field);
        confirm = view.findViewById(R.id.confirm);

        confirm.setOnClickListener(v -> {
            String activationToken = tokenField.getText().toString().trim();

            if (activationToken.isEmpty()) {
                showSnackbar("Поле для активации не может быть пустым");
                return;
            }

            if (userToken == null) {
                firebaseAuth.getCurrentUser().getIdToken(true).addOnCompleteListener(task -> {
                    userToken = task.getResult().getToken();
                    activateToken(activationToken, userToken);
                });
            } else {
                userToken = firebaseAuth.getCurrentUser().getIdToken(false).getResult().getToken();
                activateToken(activationToken, userToken);
            }
        });

        return view;
    }

    private void activateToken(String activationToken, String userToken) {
        retrofitClient.getPermissionRepository().activateShareLink(activationToken, userToken).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful())
                    showSnackbar("Событие было успешно добавлено в Ваш календарь");
                else
                    showSnackbar("Активация не удалась, код ошибки " + response.code());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                showSnackbar("Не удалось активировать токен, повторите попытку");
            }
        });
    }

    private void showSnackbar(String message) {
        Snackbar.make(getActivity().findViewById(R.id.calendarContainer),
                message, Snackbar.LENGTH_LONG).show();
    }
}
