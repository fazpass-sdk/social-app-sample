package com.fazpass.social.ui.confirm_login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.fazpass.social.R;
import com.fazpass.social.helper.LOGIN_TYPE;

import java.util.Objects;

public class ConfirmLoginFragment extends Fragment {

    private ConfirmLoginViewModel mViewModel;
    private TextView errorTxt;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ConfirmLoginViewModel.class);

        Bundle args = getArguments();
        mViewModel.setUser(Objects.requireNonNull(args).getStringArrayList("ARGS_USER"));
        mViewModel.setCDAvailable(args.getBoolean("ARGS_CD_IS_AVAILABLE"));
        mViewModel.setLoginType(args.getString("ARGS_LOGIN_TYPE"));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_confirm_login, container, false);

        ImageButton backBtn = v.findViewById(R.id.back_btn);
        backBtn.setOnClickListener(mViewModel::navigateUp);

        TextView greetingTxt = v.findViewById(R.id.greeting_txt);
        String userIdentity;
        if (mViewModel.getUser().getName().isEmpty()) {
            userIdentity = mViewModel.getUser().getPhone();
        } else {
            userIdentity = mViewModel.getUser().getName();
        }
        greetingTxt.setText(getString(R.string.greeting_message, userIdentity));

        Button cdNotifBtn = v.findViewById(R.id.cd_notif_btn);
        if (!mViewModel.isCDAvailable()) cdNotifBtn.setEnabled(false);
        else cdNotifBtn.setOnClickListener(mViewModel::crossDeviceNotificationOption);

        Button smsVerBtn = v.findViewById(R.id.sms_ver_btn);
        Button miscallVerBtn = v.findViewById(R.id.miscall_ver_btn);
        Button emailVerBtn = v.findViewById(R.id.email_ver_btn);
        Button waVerBtn = v.findViewById(R.id.wa_ver_btn);
        Button waLongVerBtn = v.findViewById(R.id.wa_long_ver_btn);
        Button heVerBtn = v.findViewById(R.id.he_ver_btn);
        if (mViewModel.getLoginType() == LOGIN_TYPE.phone) {
            smsVerBtn.setOnClickListener(mViewModel::smsVerificationOption);
            miscallVerBtn.setOnClickListener(mViewModel::miscallVerificationOption);
            waVerBtn.setOnClickListener(mViewModel::waVerificationOption);
            waLongVerBtn.setOnClickListener(mViewModel::waLongVerificationOption);
            heVerBtn.setOnClickListener(mViewModel::heVerificationOption);
            emailVerBtn.setVisibility(View.GONE);
        } else {
            smsVerBtn.setVisibility(View.GONE);
            miscallVerBtn.setVisibility(View.GONE);
            waVerBtn.setVisibility(View.GONE);
            waLongVerBtn.setVisibility(View.GONE);
            heVerBtn.setVisibility(View.GONE);
            emailVerBtn.setOnClickListener(mViewModel::emailVerificationOption);
        }

        errorTxt = v.findViewById(R.id.error_txt2);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        mViewModel.initialize(this);
    }

    public void showErrorMessage(String message) {
        errorTxt.setText(message);

        if (errorTxt.getVisibility() == View.GONE)
            errorTxt.setVisibility(View.VISIBLE);
    }
}
