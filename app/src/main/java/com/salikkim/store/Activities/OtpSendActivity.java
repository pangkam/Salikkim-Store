package com.salikkim.store.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.salikkim.store.databinding.ActivityOtpSendBinding;

import java.util.concurrent.TimeUnit;

public class OtpSendActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private ActivityOtpSendBinding otpSendBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        otpSendBinding = ActivityOtpSendBinding.inflate(getLayoutInflater());
        View view = otpSendBinding.getRoot();
        setContentView(view);
        mAuth = FirebaseAuth.getInstance();
        otpSendBinding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (otpSendBinding.etPhone.getText().toString().trim().isEmpty()) {
                    otpSendBinding.otpSendTvAlert.setText("Invalid Phone Number");
                    otpSendBinding.otpSendTvAlert.setVisibility(View.VISIBLE);
                } else if (otpSendBinding.etPhone.getText().toString().trim().length() != 10) {
                    otpSendBinding.otpSendTvAlert.setText("Type valid Phone Number");
                    otpSendBinding.otpSendTvAlert.setVisibility(View.VISIBLE);
                } else {
                    otpSend();
                }
            }
        });
        otpSendBinding.etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                otpSendBinding.otpSendTvAlert.setText("");
                otpSendBinding.otpSendTvAlert.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void otpSend() {
        otpSendBinding.progressBarSendOtp.setVisibility(View.VISIBLE);
        otpSendBinding.btnSend.setVisibility(View.GONE);
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                otpSendBinding.progressBarSendOtp.setVisibility(View.GONE);
                otpSendBinding.btnSend.setVisibility(View.VISIBLE);
                otpSendBinding.otpSendTvAlert.setText("");
                otpSendBinding.otpSendTvAlert.setVisibility(View.GONE);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                otpSendBinding.progressBarSendOtp.setVisibility(View.GONE);
                otpSendBinding.btnSend.setVisibility(View.VISIBLE);
                otpSendBinding.otpSendTvAlert.setText(e.getLocalizedMessage());
                otpSendBinding.otpSendTvAlert.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                otpSendBinding.progressBarSendOtp.setVisibility(View.GONE);
                otpSendBinding.btnSend.setVisibility(View.GONE);
                Intent intent = new Intent(OtpSendActivity.this, OtpVerifyActivity.class);
                intent.putExtra("phone", otpSendBinding.etPhone.getText().toString().trim());
                intent.putExtra("verificationId", verificationId);
                startActivity(intent);
            }
        };

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+91" + otpSendBinding.etPhone.getText().toString().trim())
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
}
