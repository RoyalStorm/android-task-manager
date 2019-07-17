package com.github.royalstorm.android_task_manager.fragment.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;

import com.github.royalstorm.android_task_manager.dao.Permission;
import com.github.royalstorm.android_task_manager.service.PermissionService;
import com.google.firebase.auth.FirebaseAuth;

public class DeletePermissionAlert extends AppCompatDialogFragment implements PermissionService.RequestPermissionCallback {

    private PermissionService permissionService;

    private FirebaseAuth firebaseAuth;
    private String userToken;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        permissionService = new PermissionService(this);

        firebaseAuth = FirebaseAuth.getInstance();

        Bundle bundle = this.getArguments();
        Permission permission = (Permission) bundle.getSerializable(Permission.class.getSimpleName());

        builder.setMessage("Один из уровней привилегий будет удален")
                .setPositiveButton("Ок", (dialog, id) -> {
                    updateUserToken();
                    permissionService.delete(permission.getId(), userToken);
                })
                .setNegativeButton("Отмена", (dialog, id) -> {
                });

        return builder.create();
    }

    @Override
    public void requestPermissionSuccess(boolean success, String sharingToken) {

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
