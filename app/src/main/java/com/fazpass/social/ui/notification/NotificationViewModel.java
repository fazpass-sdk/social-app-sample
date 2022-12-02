package com.fazpass.social.ui.notification;

import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.fazpass.social.data.SampleFeed;

public class NotificationViewModel extends ViewModel {
    private NotificationFragment fragment;
    private SampleFeed sampleFeed;

    public SampleFeed getSampleFeed() {
        return sampleFeed;
    }

    public void setSampleFeed(SampleFeed sampleFeed) {
        this.sampleFeed = sampleFeed;
    }

    public void onBack() {
        Navigation.findNavController(fragment.requireView())
                .navigateUp();
    }

    public void initialize(NotificationFragment fragment) {
        this.fragment = fragment;
    }
}