package com.fazpass.social;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.fazpass.trusted_device.Fazpass;
import com.fazpass.trusted_device.FazpassCd;
import com.fazpass.trusted_device.MODE;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fazpass.initialize(this, getResources().getString(R.string.merchant_key), MODE.STAGING);
        Fazpass.requestPermission(this);
        FazpassCd.initialize(this, true, CrossDeviceActivity.class);
    }
}