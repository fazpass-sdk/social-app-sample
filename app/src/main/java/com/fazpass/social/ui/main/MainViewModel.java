package com.fazpass.social.ui.main;

import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.fazpass.social.R;
import com.fazpass.social.data.SampleFeed;
import com.fazpass.social.helper.Storage;
import com.fazpass.social.object.User;
import com.fazpass.trusted_device.Fazpass;

public class MainViewModel extends ViewModel {
    private MainFragment fragment;
    private User user;
    private SampleFeed sampleFeed;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public SampleFeed getSampleFeed() {
        return sampleFeed;
    }

    public void setSampleFeed(SampleFeed sampleFeed) {
        this.sampleFeed = sampleFeed;
    }

    public void toNotification() {
        Navigation.findNavController(fragment.requireView())
                .navigate(R.id.action_mainFragment_to_notificationFragment);
    }

    public void initialize(MainFragment fragment) {
        this.fragment = fragment;
    }

    public void logout() {
        Fazpass.removeDevice(fragment.requireActivity().getApplicationContext());
        Storage.logout(fragment.requireContext());

        NavHostFragment.findNavController(fragment)
                .navigate(R.id.action_mainFragment_to_loginFragment);
    }
}