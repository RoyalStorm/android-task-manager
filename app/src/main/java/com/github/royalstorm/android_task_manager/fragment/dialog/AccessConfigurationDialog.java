package com.github.royalstorm.android_task_manager.fragment.dialog;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import com.github.royalstorm.android_task_manager.R;
import com.github.royalstorm.android_task_manager.dao.Action;
import com.github.royalstorm.android_task_manager.dao.EntityType;
import com.github.royalstorm.android_task_manager.dao.EventInstance;
import com.github.royalstorm.android_task_manager.dao.PermissionRequest;
import com.github.royalstorm.android_task_manager.service.PermissionService;
import com.google.firebase.auth.FirebaseAuth;

public class AccessConfigurationDialog extends AppCompatDialogFragment
        implements PermissionService.RequestPermissionCallback {

    private FirebaseAuth firebaseAuth;
    private String userToken;
    private PermissionService permissionService;

    private Intent shareIntent;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.access_configuration_dialog, null);

        firebaseAuth = FirebaseAuth.getInstance();
        userToken = firebaseAuth.getCurrentUser().getIdToken(false).getResult().getToken();
        permissionService = new PermissionService(this);

        builder.setView(view)
                .setTitle("Выберите уровни привилегий")
                .setNegativeButton("Отмена", (dialogInterface, i) -> {
                })
                .setPositiveButton("Создать токен", (dialogInterface, i) -> {
                    Bundle accessConfigurationBundle = this.getArguments();
                    EventInstance eventInstance = (EventInstance) accessConfigurationBundle.getSerializable(EventInstance.class.getSimpleName());

                    PermissionRequest permissionRequest = new PermissionRequest();
                    permissionRequest.setAction(Action.READ.name());
                    permissionRequest.setEntityType(EntityType.EVENT.name());
                    permissionRequest.setEntityId(eventInstance.getEventId());

                    if (userToken == null) {
                        firebaseAuth.getCurrentUser().getIdToken(true).addOnCompleteListener(task -> {
                            userToken = task.getResult().getToken();
                            permissionService.generateSharingLink(new PermissionRequest[]{permissionRequest}, userToken);
                        });
                    } else {
                        userToken = firebaseAuth.getCurrentUser().getIdToken(false).getResult().getToken();
                        permissionService.generateSharingLink(new PermissionRequest[]{permissionRequest}, userToken);
                    }
                });

        return builder.create();
    }

    @Override
    public void requestPermissionSuccess(boolean success, String sharingToken) {
        shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, sharingToken);
        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent, "Выберите пользователя"));
    }
}
