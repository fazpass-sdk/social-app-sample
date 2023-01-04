package com.fazpass.social;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fazpass.trusted_device.FazpassCd;

public class CrossDeviceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cross_device);

        String device = getIntent().getStringExtra("device");
        TextView message = findViewById(R.id.cd_message);
        message.setText(getString(R.string.cd_incoming_login_message, device.split(",")[1]));

        EditText input = findViewById(R.id.cd_input);
        Button accept = findViewById(R.id.cd_yes);
        Button decline = findViewById(R.id.cd_no);
        accept.setOnClickListener(v -> FazpassCd.onConfirmRequirePin(this, input.getText().toString(), isPinMatch -> {
            if (isPinMatch) finish();
            else Toast.makeText(this, "Pin doesn't match.", Toast.LENGTH_SHORT).show();
            return null;
        }));
        decline.setOnClickListener(v -> {
            FazpassCd.onDecline(this);
            finish();
        });
    }
}