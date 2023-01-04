package com.fazpass.social.ui.login;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.fazpass.social.R;
import com.fazpass.social.component.DialogInputNumber;
import com.fazpass.social.helper.LOGIN_TYPE;
import com.fazpass.social.helper.Storage;
import com.fazpass.trusted_device.CROSS_DEVICE;
import com.fazpass.trusted_device.EnrollStatus;
import com.fazpass.trusted_device.Fazpass;
import com.fazpass.trusted_device.FazpassTd;
import com.fazpass.trusted_device.TRUSTED_DEVICE;
import com.fazpass.trusted_device.TrustedDeviceListener;
import com.fazpass.trusted_device.User;

import java.util.ArrayList;

public class LoginViewModel extends ViewModel {

    private LoginFragment fragment;
    private AlertDialog dialog;
    private LOGIN_TYPE loginType;

    public void initialize(LoginFragment fragment) {
        this.fragment = fragment;

        AlertDialog.Builder builder = new AlertDialog.Builder(fragment.requireContext());
        builder.setTitle("Logging in")
                .setMessage("\nPlease wait while we try to log you in...\n")
                .setCancelable(false);
        dialog = builder.create();
    }

    public void login(String input) {
        if (input.isEmpty()) {
            failedLogin("Please input your email or phone number.");
            return;
        }

        loginType = null;
        try {
            Double.parseDouble(input);
            loginType = LOGIN_TYPE.phone;
        } catch (NumberFormatException ignored) {}
        if (loginType==null && input.contains("@")) {
            loginType = LOGIN_TYPE.email;
        }
        if (loginType==null) {
            failedLogin("Input is neither email nor phone number.");
            return;
        }

        User u;
        if (loginType==LOGIN_TYPE.email) {
            u = new User(input, "", input.split("@")[0],"","");
        }
        else {
            u = new User("", input, "","","");
        }
        User.setIsUseFinger(false);

        dialog.show();
        Fazpass.check(fragment.getContext(), u.getEmail(), u.getPhone(), new TrustedDeviceListener<FazpassTd>() {
            @Override
            public void onSuccess(FazpassTd o) {
                dialog.dismiss();
                if (o.td_status == TRUSTED_DEVICE.TRUSTED) {
                    requestEnroll(u, o);
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

    private void requestEnroll(User u, FazpassTd o) {
        String title = "Input PIN";
        String message = "Input new PIN for your application. (at least 4 digits)";
        AlertDialog inputPinDialog = new DialogInputNumber(
                fragment, title, message, 4,
                (alertDialog, input) -> {
                    alertDialog.dismiss();
                    enroll(u, o, input);
                    return null;
                },
                alertDialog -> {
                    alertDialog.dismiss();
                    return null;
                }).getInstance();
        inputPinDialog.show();
    }

    private void enroll(User u, FazpassTd o, String pin) {
        AlertDialog.Builder builder = new AlertDialog.Builder(fragment.requireContext());
        builder.setTitle("Finishing Login")
                .setMessage(fragment.getString(R.string.login_finish))
                .setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();

        o.enrollDeviceByPin(fragment.requireContext(), u, pin, new TrustedDeviceListener<EnrollStatus>() {
            @Override
            public void onSuccess(EnrollStatus o) {
                dialog.dismiss();

                if (o.getStatus()) {
                    successLogin(u);
                } else {
                    fragment.showErrorMessage(o.getMessage());
                }
            }

            @Override
            public void onFailure(Throwable err) {
                dialog.dismiss();
                Toast.makeText(fragment.requireContext(), "Failed to enroll. Check your internet and try again.", Toast.LENGTH_SHORT).show();
                requestEnroll(u, o);
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

        Bundle args = new Bundle();
        args.putStringArrayList("ARGS_USER", list);
        args.putBoolean("ARGS_CD_IS_AVAILABLE", cd_status.equals(CROSS_DEVICE.AVAILABLE));
        args.putString("ARGS_LOGIN_TYPE", loginType.toString());
        Navigation.findNavController(fragment.requireView())
                .navigate(R.id.action_loginFragment_to_confirmLoginFragment, args);
    }

    private void failedLogin(String errorMessage) {
        fragment.showErrorMessage(errorMessage);
    }
}