package com.github.royalstorm.android_task_manager.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.royalstorm.android_task_manager.R;
import com.github.royalstorm.android_task_manager.adapter.MyPermissionsRecyclerViewAdapter;
import com.github.royalstorm.android_task_manager.dao.EntityType;
import com.github.royalstorm.android_task_manager.dao.Permission;
import com.github.royalstorm.android_task_manager.dto.PermissionResponse;
import com.github.royalstorm.android_task_manager.fragment.dialog.DeletePermissionAlert;
import com.github.royalstorm.android_task_manager.shared.RetrofitClient;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RulesManagementFragment extends Fragment implements
        MyPermissionsRecyclerViewAdapter.OnMyPermissionListener {

    private RetrofitClient retrofitClient;

    private FirebaseAuth firebaseAuth;
    private String userToken;

    private RecyclerView rvMyPermissions;
    private RecyclerView rvOtherPermissions;

    private List<Permission> permissions;

    private MyPermissionsRecyclerViewAdapter myPermissionsRecyclerViewAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rules_management, container, false);

        retrofitClient = RetrofitClient.getInstance();

        firebaseAuth = FirebaseAuth.getInstance();
        userToken = firebaseAuth.getCurrentUser().getIdToken(false).getResult().getToken();

        rvMyPermissions = view.findViewById(R.id.rv_my_permissions);
        rvOtherPermissions = view.findViewById(R.id.rv_other_permissions);

        rvMyPermissions.setLayoutManager(new LinearLayoutManager(getContext()));
        rvOtherPermissions.setLayoutManager(new LinearLayoutManager(getContext()));

        permissions = new ArrayList<>();

        myPermissionsRecyclerViewAdapter = new MyPermissionsRecyclerViewAdapter(new ArrayList<>(), this);

        updateToken();
        getAllMyPermissionsRequest(true, userToken);

        return view;
    }

    private void getAllMyPermissionsRequest(boolean mine, String userToken) {
        retrofitClient.getPermissionRepository().getPermissions(EntityType.EVENT.name(), mine, userToken).enqueue(new Callback<PermissionResponse>() {
            @Override
            public void onResponse(Call<PermissionResponse> call, Response<PermissionResponse> response) {
                if (response.isSuccessful()) {
                    permissions = Arrays.asList(response.body().getData());
                    myPermissionsRecyclerViewAdapter.setItems(permissions);
                    rvMyPermissions.setAdapter(myPermissionsRecyclerViewAdapter);
                }
            }

            @Override
            public void onFailure(Call<PermissionResponse> call, Throwable t) {

            }
        });
    }

    private void updateToken() {
        if (userToken == null)
            firebaseAuth.getCurrentUser().getIdToken(true).addOnCompleteListener(task -> {
                userToken = task.getResult().getToken();
            });
        else
            userToken = firebaseAuth.getCurrentUser().getIdToken(false).getResult().getToken();
    }

    @Override
    public void onMyPermissionClick(int position) {
        DeletePermissionAlert deletePermissionAlert = new DeletePermissionAlert();

        Bundle bundle = new Bundle();
        Permission permission = permissions.get(position);
        bundle.putSerializable(Permission.class.getSimpleName(), permission);

        deletePermissionAlert.setArguments(bundle);
        deletePermissionAlert.show(getFragmentManager(), "Delete permission");
    }
}
