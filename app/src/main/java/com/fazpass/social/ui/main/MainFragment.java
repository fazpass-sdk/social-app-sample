package com.fazpass.social.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fazpass.social.R;
import com.fazpass.social.data.SampleFeed;
import com.fazpass.social.helper.Storage;
import com.fazpass.social.object.User;

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mViewModel.setUser(Storage.getUser(requireContext()));
        mViewModel.setSampleFeed(new SampleFeed(requireContext()));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        TextView userName = v.findViewById(R.id.profile_name);
        TextView userEmail = v.findViewById(R.id.profile_email);
        TextView userPhone = v.findViewById(R.id.profile_phone);
        User user = mViewModel.getUser();
        userName.setText(user.getName());
        userEmail.setText(user.getEmail());
        userPhone.setText(user.getPhone());

        ImageButton notifBtn = v.findViewById(R.id.notification_btn);
        notifBtn.setOnClickListener((view) -> mViewModel.toNotification());

        ImageButton menuBtn = v.findViewById(R.id.menu_btn);
        PopupMenu menuPopup = new PopupMenu(v.getContext(), menuBtn);
        menuPopup.setOnMenuItemClickListener(this::onMenuItemClick);
        menuPopup.inflate(R.menu.main_menu);
        menuBtn.setOnClickListener(view -> menuPopup.show());

        RecyclerView feeds = v.findViewById(R.id.feed_rv);
        feeds.setLayoutManager(new LinearLayoutManager(requireContext()));
        FeedRVAdapter feedRVAdapter = new FeedRVAdapter(mViewModel.getSampleFeed().getFeed1());
        feeds.setAdapter(feedRVAdapter);

        return v;
    }

    private boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.profile_menu_logout) {
            mViewModel.logout();
            return true;
        }
        return false;
    }

    @Override
    public void onStart() {
        super.onStart();

        mViewModel.initialize(this);
    }
}