package com.fazpass.social.ui.splash;

import android.os.Bundle;
import android.os.Handler;

import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.fazpass.social.R;
import com.fazpass.social.helper.Storage;
import com.fazpass.trusted_device.Fazpass;
import com.fazpass.trusted_device.FazpassTd;
import com.fazpass.trusted_device.TrustedDeviceListener;
import com.fazpass.trusted_device.User;

public class SplashViewModel extends ViewModel {

    public void startCountdown(SplashFragment fragment) {
        new Handler().postDelayed(afterCountdown(fragment), 3000L);
    }

    private Runnable afterCountdown(SplashFragment fragment) {
        return () -> {
            final User user = Storage.getUser(fragment.getContext());

            if (user != null) {
                Fazpass.check(fragment.requireContext(), user.getEmail(), user.getPhone(), new TrustedDeviceListener<FazpassTd>() {
                    @Override
                    public void onSuccess(FazpassTd o) {
                        Navigation.findNavController(fragment.requireView())
                                .navigate(R.id.action_splashFragment_to_mainFragment);
                    }

                    @Override
                    public void onFailure(Throwable err) {
                        Storage.logout(fragment.getContext());

                        final Bundle args = new Bundle();
                        args.putString("error_message", "Session has expired");
                        Navigation.findNavController(fragment.requireView())
                                .navigate(R.id.action_splashFragment_to_loginFragment, args);
                    }
                });
            }
            else {
                Navigation.findNavController(fragment.requireView())
                        .navigate(R.id.action_splashFragment_to_loginFragment);
            }
        };
    }
}