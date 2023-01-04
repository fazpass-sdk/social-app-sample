package com.fazpass.social.ui.main;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.fazpass.social.R;
import com.fazpass.social.component.DialogInputNumber;
import com.fazpass.social.data.SampleFeed;
import com.fazpass.social.helper.Storage;
import com.fazpass.trusted_device.Fazpass;
import com.fazpass.trusted_device.TrustedDeviceListener;
import com.fazpass.trusted_device.User;

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

    public void requestLogout() {
        new DialogInputNumber(
                fragment,
                "Input PIN",
                "Input your pin to logout.",
                4,
                (alertDialog, s) -> {
                    alertDialog.dismiss();
                    logout(s);
                    return null;
                },
                alertDialog -> {
                    alertDialog.dismiss();
                    return null;
                }
        ).getInstance().show();
    }

    public void logout(String pin) {
        Fazpass.removeDevice(fragment.requireContext(), pin, new TrustedDeviceListener<Boolean>() {
            @Override
            public void onSuccess(Boolean o) {
                Log.e("remove device", ""+o);
                Storage.logout(fragment.requireContext());

                NavHostFragment.findNavController(fragment)
                        .navigate(R.id.action_mainFragment_to_loginFragment);
            }

            @Override
            public void onFailure(Throwable err) {
                Toast.makeText(fragment.requireContext(), err.getMessage(), Toast.LENGTH_SHORT).show();
                requestLogout();
            }
        });
    }
}