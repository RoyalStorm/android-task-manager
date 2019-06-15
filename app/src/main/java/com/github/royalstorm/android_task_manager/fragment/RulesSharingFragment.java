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
import com.github.royalstorm.android_task_manager.adapter.EventAdapter;
import com.github.royalstorm.android_task_manager.dto.EventInstanceResponse;
import com.github.royalstorm.android_task_manager.shared.RetrofitClient;

import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RulesSharingFragment extends Fragment {

    private RetrofitClient retrofitClient = RetrofitClient.getInstance();

    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.events_fragment, container, false);

        super.onCreate(savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        eventAdapter = new EventAdapter();

        GregorianCalendar begin = new GregorianCalendar(2019, Calendar.JUNE, 15, 0, 0);
        GregorianCalendar end = new GregorianCalendar(2019, Calendar.JUNE, 15, 23, 59);

        retrofitClient.getEventRepository().getInstancesByInterval(begin.getTimeInMillis(), end.getTimeInMillis()).enqueue(new Callback<EventInstanceResponse>() {
            @Override
            public void onResponse(Call<EventInstanceResponse> call, Response<EventInstanceResponse> response) {
                if (response.isSuccessful()) {
                    eventAdapter.setItems(Arrays.asList(response.body()));

                    recyclerView.setAdapter(eventAdapter);
                }
            }

            @Override
            public void onFailure(Call<EventInstanceResponse> call, Throwable t) {

            }
        });

        return view;
    }
}
