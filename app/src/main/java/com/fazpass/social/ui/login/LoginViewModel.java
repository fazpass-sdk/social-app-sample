package com.fazpass.social.ui.login;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.fazpass.social.R;
import com.fazpass.social.helper.Storage;
import com.fazpass.social.object.User;
import com.fazpass.trusted_device.CROSS_DEVICE;
import com.fazpass.trusted_device.Fazpass;
import com.fazpass.trusted_device.FazpassTd;
import com.fazpass.trusted_device.TRUSTED_DEVICE;
import com.fazpass.trusted_device.TrustedDeviceListener;

import java.util.ArrayList;

public class LoginViewModel extends ViewModel {

    private LoginFragment fragment;
    private AlertDialog dialog;

    public void initialize(LoginFragment fragment) {
        this.fragment = fragment;

        AlertDialog.Builder builder = new AlertDialog.Builder(fragment.requireContext());
        builder.setTitle("Logging in")
                .setMessage("\nPlease wait while we try to log you in...\n")
                .setCancelable(false);
        dialog = builder.create();
    }

    public void login(String email, String phone, String pin) {
        if (email.isEmpty() || phone.isEmpty() || pin.isEmpty()) {
            failedLogin("Please fill in all of this form.");
            return;
        }

        dialog.show();

        User u = new User(email, phone, email.split("@")[0],"","", pin);
        User.setIsUseFinger(false);
        Fazpass.check(fragment.getContext(), email, phone, pin, new TrustedDeviceListener<FazpassTd>() {
            @Override
            public void onSuccess(FazpassTd o) {
                dialog.dismiss();
                if (o.td_status == TRUSTED_DEVICE.TRUSTED) {
                    successLogin(u);
                }
                else {
                    toConfirmOptions(u, o.cd_status);
                }
            }

            @Override
            public void onFailure(Throwable err) {
                dialog.dismiss();
                err.printStackTrace();
                failedLogin("Failed to initialize login. Check your internet and try again.");
            }
        });
    }

    private void successLogin(User u) {
        Storage.saveUser(fragment.requireContext(), u);
        Navigation.findNavController(fragment.requireView())
                .navigate(R.id.action_loginFragment_to_mainFragment);
    }

    private void toConfirmOptions(User u, CROSS_DEVICE cd_status) {
        ArrayList<String> list = new ArrayList<>();
        list.add(u.getEmail());
        list.add(u.getPhone());
        list.add(u.getName());
        list.add(u.getIdCard());
        list.add(u.getAddress());
        list.add(u.getPin());

        Bundle args = new Bundle();
        args.putStringArrayList("ARGS_USER", list);
        args.putBoolean("ARGS_CD_IS_AVAILABLE", cd_status.equals(CROSS_DEVICE.AVAILABLE));
        Navigation.findNavController(fragment.requireView())
                .navigate(R.id.action_loginFragment_to_confirmLoginFragment, args);
    }

    private void failedLogin(String errorMessage) {
        fragment.showErrorMessage(errorMessage);
    }
}