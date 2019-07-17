package com.github.royalstorm.android_task_manager.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.royalstorm.android_task_manager.R;
import com.github.royalstorm.android_task_manager.dao.Action;
import com.github.royalstorm.android_task_manager.dao.EntityType;
import com.github.royalstorm.android_task_manager.dao.Permission;
import com.github.royalstorm.android_task_manager.dto.EventResponse;
import com.github.royalstorm.android_task_manager.service.EventService;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Collection;
import java.util.List;

public class MyPermissionsRecyclerViewAdapter extends RecyclerView.Adapter<MyPermissionsRecyclerViewAdapter.MyPermissionViewHolder> {

    private FirebaseAuth firebaseAuth;
    private String userToken;

    private List<Permission> permissions;

    private OnMyPermissionListener onMyPermissionListener;

    private View view;

    private EventService eventService;

    public MyPermissionsRecyclerViewAdapter(List<Permission> permissions, MyPermissionsRecyclerViewAdapter.OnMyPermissionListener onMyPermissionListener) {
        this.permissions = permissions;
        this.onMyPermissionListener = onMyPermissionListener;
    }

    class MyPermissionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            EventService.RequestEventCallback {

        TextView eventName;
        TextView eventDetails;
        TextView entityType;

        OnMyPermissionListener onMyPermissionListener;

        MyPermissionViewHolder(View itemView, MyPermissionsRecyclerViewAdapter.OnMyPermissionListener onMyPermissionListener) {
            super(itemView);

            eventName = itemView.findViewById(R.id.event_name);
            eventDetails = itemView.findViewById(R.id.event_details);
            entityType = itemView.findViewById(R.id.entity_type);

            firebaseAuth = FirebaseAuth.getInstance();
            userToken = firebaseAuth.getCurrentUser().getIdToken(false).getResult().getToken();

            this.onMyPermissionListener = onMyPermissionListener;

            eventService = new EventService(this);

            itemView.setOnClickListener(this);
        }

        void bind(Permission permission) {
            updateToken();

            String access = permission.getName().split("_")[0];
            String entity = permission.getName().split("_")[1];

            if (access.equals(Action.READ.name())) {
                if (entity.equals(EntityType.EVENT.name()))
                    entityType.setText("Событие (чтение)");
                if (entity.equals(EntityType.PATTERN.name()))
                    entityType.setText("Паттерн (чтение)");
            } else if (access.equals(Action.UPDATE.name())) {
                if (entity.equals(EntityType.EVENT.name()))
                    entityType.setText("Событие (обновление)");
                if (entity.equals(EntityType.PATTERN.name()))
                    entityType.setText("Паттерн (обновление)");
            } else {
                if (entity.equals(EntityType.EVENT.name()))
                    entityType.setText("Событие (удаление)");
                if (entity.equals(EntityType.PATTERN.name()))
                    entityType.setText("Паттерн (удаление)");
            }

            eventService.getEventsById(new Long[]{Long.parseLong(permission.getEntityId())}, userToken);
        }

        @Override
        public void onClick(View view) {
            onMyPermissionListener.onMyPermissionClick(getAdapterPosition());
        }

        void updateToken() {
            if (userToken == null)
                firebaseAuth.getCurrentUser().getIdToken(true).addOnCompleteListener(task -> {
                    userToken = task.getResult().getToken();
                });
            else
                userToken = firebaseAuth.getCurrentUser().getIdToken(false).getResult().getToken();
        }

        @Override
        public void requestEventSuccess(boolean success, EventResponse eventResponse) {
            eventName.setText(eventResponse.getData()[0].getName());
            eventDetails.setText(eventResponse.getData()[0].getDetails());
        }
    }

    public interface OnMyPermissionListener {
        void onMyPermissionClick(int position);
    }

    @NonNull
    @Override
    public MyPermissionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.permission_item, viewGroup, false);
        return new MyPermissionsRecyclerViewAdapter.MyPermissionViewHolder(view, onMyPermissionListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPermissionViewHolder myPermissionViewHolder, int i) {
        myPermissionViewHolder.bind(permissions.get(i));
    }

    @Override
    public int getItemCount() {
        return permissions.size();
    }

    public void setItems(Collection<Permission> permissions) {
        clearItems();
        this.permissions.addAll(permissions);
        notifyDataSetChanged();
    }

    private void clearItems() {
        permissions.clear();
    }
}
