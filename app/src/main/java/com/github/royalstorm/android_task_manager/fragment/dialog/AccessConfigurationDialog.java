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
import android.widget.CheckBox;

import com.annimon.stream.Stream;
import com.github.royalstorm.android_task_manager.R;
import com.github.royalstorm.android_task_manager.dao.Action;
import com.github.royalstorm.android_task_manager.dao.EntityType;
import com.github.royalstorm.android_task_manager.dao.EventInstance;
import com.github.royalstorm.android_task_manager.dao.PermissionRequest;
import com.github.royalstorm.android_task_manager.service.PermissionService;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class AccessConfigurationDialog extends AppCompatDialogFragment
        implements PermissionService.RequestPermissionCallback {

    private CheckBox cbRead;
    private CheckBox cbUpdate;
    private CheckBox cbDelete;

    private FirebaseAuth firebaseAuth;
    private String userToken;
    private PermissionService permissionService;

    private ApplyAccessConfiguration applyAccessConfiguration;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.access_configuration_dialog, null);

        cbRead = view.findViewById(R.id.cbRead);
        cbUpdate = view.findViewById(R.id.cbUpdate);
        cbDelete = view.findViewById(R.id.cbDelete);

        firebaseAuth = FirebaseAuth.getInstance();
        userToken = firebaseAuth.getCurrentUser().getIdToken(false).getResult().getToken();
        permissionService = new PermissionService(this);

        Bundle accessConfigurationBundle = this.getArguments();
        EventInstance eventInstance = (EventInstance) accessConfigurationBundle.getSerializable(EventInstance.class.getSimpleName());

        builder.setView(view)
                .setTitle("Выберите уровни привилегий")
                .setNegativeButton("Отмена", (dialogInterface, i) -> {
                })
                .setPositiveButton("Создать токен", (dialogInterface, i) -> {
                    updateUserToken();
                    permissionService.generateSharingLink(getSelectedPermissions(eventInstance), userToken);
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
        Intent shareWithFriendsIntent = new Intent();
        shareWithFriendsIntent.setAction(Intent.ACTION_SEND);
        shareWithFriendsIntent.putExtra(Intent.EXTRA_TEXT, sharingToken);
        shareWithFriendsIntent.setType("text/plain");
        applyAccessConfiguration.applySharingLink(shareWithFriendsIntent);
    }

    public interface ApplyAccessConfiguration {
        void applySharingLink(Intent sharingIntent);
    }

    private PermissionRequest[] getSelectedPermissions(EventInstance eventInstance) {
        List<PermissionRequest> permissionRequests = new ArrayList<>();

        if (cbRead.isChecked()) {
            PermissionRequest readEventPermissionRequest = new PermissionRequest();
            readEventPermissionRequest.setAction(Action.READ.name());
            readEventPermissionRequest.setEntityType(EntityType.EVENT.name());
            readEventPermissionRequest.setEntityId(eventInstance.getEventId());

            permissionRequests.add(readEventPermissionRequest);
        }

        if (cbUpdate.isChecked()) {
            PermissionRequest updateEventPermissionRequest = new PermissionRequest();
            updateEventPermissionRequest.setAction(Action.UPDATE.name());
            updateEventPermissionRequest.setEntityType(EntityType.EVENT.name());
            updateEventPermissionRequest.setEntityId(eventInstance.getEventId());

            PermissionRequest updateEventPatternPermissionRequest = new PermissionRequest();
            updateEventPatternPermissionRequest.setAction(Action.UPDATE.name());
            updateEventPatternPermissionRequest.setEntityType(EntityType.PATTERN.name());
            updateEventPatternPermissionRequest.setEntityId(eventInstance.getPatternId());

            permissionRequests.add(updateEventPermissionRequest);
            permissionRequests.add(updateEventPatternPermissionRequest);
        }

        if (cbDelete.isChecked()) {
            PermissionRequest deleteEventPermissionRequest = new PermissionRequest();
            deleteEventPermissionRequest.setAction(Action.DELETE.name());
            deleteEventPermissionRequest.setEntityType(EntityType.EVENT.name());
            deleteEventPermissionRequest.setEntityId(eventInstance.getEventId());

            PermissionRequest deleteEventPatternPermissionRequest = new PermissionRequest();
            deleteEventPatternPermissionRequest.setAction(Action.DELETE.name());
            deleteEventPatternPermissionRequest.setEntityType(EntityType.PATTERN.name());
            deleteEventPatternPermissionRequest.setEntityId(eventInstance.getPatternId());

            permissionRequests.add(deleteEventPermissionRequest);
            permissionRequests.add(deleteEventPatternPermissionRequest);
        }

        return Stream.of(permissionRequests).toArray(PermissionRequest[]::new);
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
