package com.fazpass.social.ui.notification;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fazpass.social.R;
import com.fazpass.social.data.SampleFeed;

public class NotificationFragment extends Fragment {

    private NotificationViewModel mViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(NotificationViewModel.class);
        mViewModel.setSampleFeed(new SampleFeed(requireContext()));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_notification, container, false);

        ImageButton backBtn = v.findViewById(R.id.back_btn2);
        backBtn.setOnClickListener((view) -> mViewModel.onBack());

        RecyclerView notifications = v.findViewById(R.id.notification_rv);
        notifications.setLayoutManager(new LinearLayoutManager(requireContext()));
        NotificationRVAdapter notificationRVAdapter = new NotificationRVAdapter(mViewModel.getSampleFeed().getFeed1());
        notifications.setAdapter(notificationRVAdapter);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        mViewModel.initialize(this);
    }
}