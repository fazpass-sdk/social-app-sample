package com.fazpass.social.ui.confirm_login;

import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.fazpass.social.R;
import com.fazpass.social.component.DialogInputNumber;
import com.fazpass.social.helper.LOGIN_TYPE;
import com.fazpass.social.helper.OTP_TYPE;
import com.fazpass.social.helper.Storage;
import com.fazpass.trusted_device.CrossDeviceListener;
import com.fazpass.trusted_device.EnrollStatus;
import com.fazpass.trusted_device.Fazpass;
import com.fazpass.trusted_device.FazpassTd;
import com.fazpass.trusted_device.HeaderEnrichment;
import com.fazpass.trusted_device.Otp;
import com.fazpass.trusted_device.OtpResponse;
import com.fazpass.trusted_device.TrustedDeviceListener;
import com.fazpass.trusted_device.User;

import java.util.ArrayList;
import java.util.Iterator;

public class ConfirmLoginViewModel extends ViewModel {

    private static final int otpLength = 4;
    private User user;
    private boolean isCDAvailable;
    private ConfirmLoginFragment fragment;
    private LOGIN_TYPE loginType;

    private String otpId;
    private AlertDialog inputOtpDialog;
    
    public void initialize(ConfirmLoginFragment fragment) {
        this.fragment = fragment;
    }

    public boolean isCDAvailable() {
        return isCDAvailable;
    }

    public void setCDAvailable(boolean CDAvailable) {
        isCDAvailable = CDAvailable;
    }

    public LOGIN_TYPE getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = LOGIN_TYPE.valueOf(loginType);
    }

    public User getUser() {
        return user;
    }

    public void setUser(@Nullable ArrayList<String> argsUser) {
        if (argsUser != null) {
            Iterator<String> iterator = argsUser.iterator();
            this.user = new User(iterator.next(), iterator.next(), iterator.next(), iterator.next(), iterator.next());
        }
    }

    public void navigateUp(View view) {
        Navigation.findNavController(view)
                .navigateUp();
    }

    public void crossDeviceNotificationOption(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(fragment.requireContext());
        builder.setTitle("Cross Device Notification")
                .setMessage(R.string.cd_notification_message)
                .setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.show();

        Fazpass.check(fragment.requireContext(), user.getEmail(), user.getPhone(), new TrustedDeviceListener<FazpassTd>() {
            @Override
            public void onSuccess(FazpassTd o) {
                o.validateCrossDevice(fragment.requireContext(), 60, new CrossDeviceListener() {
                    @Override
                    public void onResponse(String device, boolean status) {
                        dialog.dismiss();

                        if (status) {
                            requestEnroll(o);
                        } else {
                            fragment.showErrorMessage("Cross device validation has been declined.");
                        }
                    }

                    @Override
                    public void onExpired() {
                        dialog.dismiss();
                        fragment.showErrorMessage("Cross device validation is expired. Ready your device and try again.");
                    }
                });
            }

            @Override
            public void onFailure(Throwable err) {
                dialog.dismiss();
                failedLogin(fragment.getString(R.string.fazpass_init_failed_message, "cross device notification"));
            }
        });
    }

    public void smsVerificationOption(View view) {
        String message = fragment.getString(R.string.sms_verification_message);
        doOtp(OTP_TYPE.sms, R.string.otp_sms_gateway_key, message);
    }

    public void miscallVerificationOption(View view) {
        String message = fragment.getString(R.string.phone_verification_message, otpLength);
        doOtp(OTP_TYPE.miscall, R.string.otp_miscall_gateway_key, message);
    }

    public void emailVerificationOption(View view) {
        String message = fragment.getString(R.string.email_verification_message);
        doOtp(OTP_TYPE.email, R.string.otp_email_gateway_key, message);
    }

    public void waVerificationOption(View view) {
        String message = fragment.getString(R.string.wa_verification_message);
        doOtp(OTP_TYPE.wa, R.string.otp_wa_gateway_key, message);
    }

    public void waLongVerificationOption(View view) {
        String message = fragment.getString(R.string.wa_verification_message);
        doOtp(OTP_TYPE.waLong, R.string.otp_wa_long_gateway_key, message);
    }

    public void heVerificationOption(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(fragment.requireContext());
        builder.setTitle("Header Enrichment")
                .setMessage(R.string.he_verification_message)
                .setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();

        Fazpass.heValidation(
                view.getContext(),
                user.getPhone(),
                fragment.getString(R.string.otp_he_gateway_key),
                new HeaderEnrichment.Request() {
                    @Override
                    public void onComplete(boolean status) {
                        if (status) {
                            Fazpass.check(
                                    view.getContext(),
                                    user.getEmail(),
                                    user.getPhone(),
                                    new TrustedDeviceListener<FazpassTd>() {
                                        @Override
                                        public void onSuccess(FazpassTd o) {
                                            dialog.dismiss();
                                            requestEnroll(o);
                                        }

                                        @Override
                                        public void onFailure(Throwable err) {
                                            dialog.dismiss();
                                            failedLogin(err.getMessage());
                                        }
                                    });
                        } else {
                            dialog.dismiss();
                            failedLogin("Header enrichment request rejected.");
                        }
                    }

                    @Override
                    public void onError(Throwable err) {
                        dialog.dismiss();
                        failedLogin(err.getMessage());
                    }
                }
        );
    }

    private void doOtp(OTP_TYPE otpType, int gatewayKeyId, String verMessage) {
        otpId = null;
        inputOtpDialog = null;

        String title;
        switch (otpType) {
            case sms:
            default:
                title = "SMS Verification";
                break;
            case miscall:
                title = "Miscall Verification";
                break;
            case email:
                title = "Email Verification";
                break;
            case wa:
                title = "Whatsapp Verification";
                break;
            case waLong:
                title = "Whatsapp Long Number Verification";
                break;
        }

        inputOtpDialog = new DialogInputNumber(
                fragment, title, verMessage, otpLength,
                (alertDialog, input) -> {
                    if (otpId != null) {
                        alertDialog.dismiss();
                        confirmOtp(input, otpId);
                    } else {
                        Toast.makeText(fragment.getContext(), fragment.getString(R.string.otp_not_ready_message), Toast.LENGTH_LONG).show();
                    }
                    return null;
                },
                alertDialog -> {
                    alertDialog.dismiss();
                    return null;
                }).getInstance();
        inputOtpDialog.show();

        String gatewayKey = fragment.getString(gatewayKeyId);
        if (otpType.equals(OTP_TYPE.email)) {
            Fazpass.generateOtpByEmail(fragment.requireContext(), user.getEmail(), gatewayKey, new OtpReq());
        }
        else {
            Fazpass.generateOtpByPhone(fragment.requireContext(), user.getPhone(), gatewayKey, new OtpReq());
        }
    }

    private void confirmOtp(String inputtedOtp, String otpId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(fragment.requireContext());
        builder.setTitle("Confirming OTP")
                .setMessage(R.string.otp_confirming_message)
                .setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();

        Fazpass.validateOtp(fragment.requireContext(), otpId, inputtedOtp, new Otp.Validate() {
            @Override
            public void onComplete(boolean isSuccess) {

                if (isSuccess) {
                    Fazpass.check(fragment.requireContext(), user.getEmail(), user.getPhone(), new TrustedDeviceListener<FazpassTd>() {
                        @Override
                        public void onSuccess(FazpassTd fazpassTd) {
                            dialog.dismiss();
                            requestEnroll(fazpassTd);
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            dialog.dismiss();
                            failedLogin("Failed to check device. Please try again.");
                        }
                    });
                } else {
                    dialog.dismiss();
                    failedLogin("Wrong OTP code.");
                }
            }

            @Override
            public void onError(Throwable throwable) {
                dialog.dismiss();
                failedLogin("Failed to confirm otp. Please try again later.");
            }
        });
    }

    private void requestEnroll(FazpassTd o) {
        String title = "Input PIN";
        String message = "Input new PIN for your application. (at least 4 digits)";
        AlertDialog inputPinDialog = new DialogInputNumber(
                fragment, title, message, 4,
                (alertDialog, input) -> {
                    alertDialog.dismiss();
                    enroll(o, input);
                    return null;
                },
                alertDialog -> {
                    alertDialog.dismiss();
                    return null;
                }).getInstance();
        inputPinDialog.show();
    }

    private void enroll(FazpassTd o, String pin) {
        AlertDialog.Builder builder = new AlertDialog.Builder(fragment.requireContext());
        builder.setTitle("Finishing Login")
                .setMessage(fragment.getString(R.string.login_finish))
                .setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();

        o.enrollDeviceByPin(fragment.requireContext(), user, pin, new TrustedDeviceListener<EnrollStatus>() {
            @Override
            public void onSuccess(EnrollStatus o) {
                dialog.dismiss();

                if (o.getStatus()) {
                    successLogin();
                } else {
                    fragment.showErrorMessage(o.getMessage());
                }
            }

            @Override
            public void onFailure(Throwable err) {
                dialog.dismiss();
                Toast.makeText(fragment.requireContext(), "Failed to enroll. Check your internet and try again.", Toast.LENGTH_SHORT).show();
                requestEnroll(o);
            }
        });
    }

    private void successLogin() {
        Storage.saveUser(fragment.requireContext(), user);

        Navigation.findNavController(fragment.requireView())
                .navigate(R.id.action_confirmLoginFragment_to_mainFragment);
    }

    private void failedLogin(String errorMessage) {
        fragment.showErrorMessage(errorMessage);
    }

    private class OtpReq implements Otp.Request {

        @Override
        public void onComplete(OtpResponse response) {
            otpId = response.getOtpId();
        }

        @Override
        public void onIncomingMessage(String otp) {
            if (inputOtpDialog != null) inputOtpDialog.dismiss();
            confirmOtp(otp, otpId);
        }

        @Override
        public void onError(Throwable err) {
            if (inputOtpDialog != null) inputOtpDialog.dismiss();
            failedLogin("Failed to request Otp. Please try again later.");
        }
    }
}
