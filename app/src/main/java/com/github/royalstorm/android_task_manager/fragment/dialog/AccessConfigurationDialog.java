package com.github.royalstorm.android_task_manager.fragment.dialog;

import android.app.Dialog;
import android.content.Context;
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

    private ApplyAccessConfiguration applyAccessConfiguration;

    PermissionRequest defaultPermission = new PermissionRequest();

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.access_configuration_dialog, null);

        firebaseAuth = FirebaseAuth.getInstance();
        userToken = firebaseAuth.getCurrentUser().getIdToken(false).getResult().getToken();
        permissionService = new PermissionService(this);

        Bundle accessConfigurationBundle = this.getArguments();
        EventInstance eventInstance = (EventInstance) accessConfigurationBundle.getSerializable(EventInstance.class.getSimpleName());

        defaultPermission.setAction(Action.READ.name());
        defaultPermission.setEntityType(EntityType.EVENT.name());
        defaultPermission.setEntityId(eventInstance.getEventId());

        builder.setView(view)
                .setTitle("Выберите уровни привилегий")
                .setNegativeButton("Отмена", (dialogInterface, i) -> {
                })
                .setPositiveButton("Создать токен", (dialogInterface, i) -> {
                    if (userToken == null) {
                        firebaseAuth.getCurrentUser().getIdToken(true).addOnCompleteListener(task -> {
                            userToken = task.getResult().getToken();
                            permissionService.generateSharingLink(new PermissionRequest[]{defaultPermission}, userToken);
                        });
                    } else {
                        userToken = firebaseAuth.getCurrentUser().getIdToken(false).getResult().getToken();
                        permissionService.generateSharingLink(new PermissionRequest[]{defaultPermission}, userToken);
                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            applyAccessConfiguration = (AccessConfigurationDialog.ApplyAccessConfiguration) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "Must implement ApplyAccessConfiguration");
        }
    }

    @Override
    public void requestPermissionSuccess(boolean success, String sharingToken) {
        shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, sharingToken);
        shareIntent.setType("text/plain");
        applyAccessConfiguration.applySharingLink(shareIntent);
    }

    public interface ApplyAccessConfiguration {
        void applySharingLink(Intent sharingIntent);
    }
}
