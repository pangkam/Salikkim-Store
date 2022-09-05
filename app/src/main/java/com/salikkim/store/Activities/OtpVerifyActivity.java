package com.salikkim.store.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.salikkim.store.Helper.VolleyEventListener;
import com.salikkim.store.Helper.VolleyRequest;
import com.salikkim.store.R;
import com.salikkim.store.databinding.ActivityOtpVerifyBinding;

import java.util.concurrent.TimeUnit;

public class OtpVerifyActivity extends AppCompatActivity {
    private String set_profile_url = "https://hawtie.000webhostapp.com/salikkim_store/setprofile.php";
    private String user_exist = "https://hawtie.000webhostapp.com/salikkim_store/userexist.php";
    private String verificationId;
    private String phone;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private ActivityOtpVerifyBinding otpVerifyBinding;
    private String smsCode;
    private String[] keys = {"user_id"};
    private boolean isExist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        otpVerifyBinding = ActivityOtpVerifyBinding.inflate(getLayoutInflater());
        View view = otpVerifyBinding.getRoot();
        setContentView(view);
        mAuth = FirebaseAuth.getInstance();
        verificationId = getIntent().getStringExtra("verificationId");
        phone = getIntent().getStringExtra("phone");
        userExist();
        otpVerifyBinding.tvMobile.setText(String.format("+91-%s", phone));
        otpVerifyBinding.btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (otpVerifyBinding.etVeryfyCode.getText().toString().trim().isEmpty()) {
                    otpVerifyBinding.otpVerifyTvAlert.setText("Please enter OTP");
                    otpVerifyBinding.otpVerifyTvAlert.setVisibility(View.VISIBLE);
                } else {
                    if (verificationId != null) {
                        String code = otpVerifyBinding.etVeryfyCode.getText().toString().trim();
                        otpVerifyBinding.progressBarVerify.setVisibility(View.VISIBLE);
                        otpVerifyBinding.btnVerify.setVisibility(View.GONE);
                        verifyCode(code);
                    }
                }
            }
        });

        otpVerifyBinding.etVeryfyCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                otpVerifyBinding.otpVerifyTvAlert.setText("");
                otpVerifyBinding.otpVerifyTvAlert.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void userExist() {
        String[] key = keys;
        String[] value = new String[]{phone};
        new VolleyRequest(key, value, user_exist, getApplicationContext(), new VolleyEventListener<String>() {
            @Override
            public void onSuccess(String object) {
                if (object.equalsIgnoreCase("Exist")) {
                    isExist = true;
                } else {
                    isExist = false;
                }
            }
        });
    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        FirebaseAuth
                .getInstance()
                .signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (!isExist) {
                                otpVerifyBinding.progressBarVerify.setVisibility(View.GONE);
                                otpVerifyBinding.btnVerify.setVisibility(View.GONE);
                                Intent intent = new Intent(OtpVerifyActivity.this, SetupAcActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("user_id", phone);
                                intent.putExtra("user_name", "");
                                intent.putExtra("user_email", "");
                                intent.putExtra("address_id", "");
                                intent.putExtra("url", set_profile_url);
                                intent.putExtra("ss", false);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(OtpVerifyActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        } else {
                            otpVerifyBinding.progressBarVerify.setVisibility(View.GONE);
                            otpVerifyBinding.btnVerify.setVisibility(View.VISIBLE);
                            otpVerifyBinding.otpVerifyTvAlert.setVisibility(View.VISIBLE);
                            otpVerifyBinding.otpVerifyTvAlert.setText("OTP is not valid!");
                        }
                    }
                });
    }

    private void resend() {
        otpVerifyBinding.progressBarVerify.setVisibility(View.VISIBLE);
        otpVerifyBinding.btnVerify.setVisibility(View.GONE);
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                otpVerifyBinding.otpVerifyTvAlert.setText("");
                otpVerifyBinding.otpVerifyTvAlert.setVisibility(View.GONE);
                otpVerifyBinding.progressBarVerify.setVisibility(View.GONE);
                otpVerifyBinding.btnVerify.setVisibility(View.VISIBLE);
                smsCode = credential.getSmsCode();
                if (!smsCode.isEmpty()) {
                    otpVerifyBinding.etVeryfyCode.setText(smsCode);
                    verifyCode(smsCode);
                }
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                otpVerifyBinding.progressBarVerify.setVisibility(View.GONE);
                otpVerifyBinding.btnVerify.setVisibility(View.VISIBLE);
                otpVerifyBinding.otpVerifyTvAlert.setText(e.getLocalizedMessage());
                otpVerifyBinding.otpVerifyTvAlert.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                otpVerifyBinding.progressBarVerify.setVisibility(View.GONE);
                otpVerifyBinding.btnVerify.setVisibility(View.VISIBLE);
                otpVerifyBinding.otpVerifyTvAlert.setText("OTP is sent to your mobile number");
                otpVerifyBinding.otpVerifyTvAlert.setVisibility(View.VISIBLE);
                new CountDownTimer(60000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        otpVerifyBinding.tvResendBtn.setText("Seconds remaining: " + millisUntilFinished / 1000);
                        // logic to set the EditText could go here
                    }

                    public void onFinish() {
                        otpVerifyBinding.tvResendBtn.setText("RESEND OTP");
                    }

                }.start();
            }
        };

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+91" + phone)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    public void resendOTP(View view) {
        if (otpVerifyBinding.tvResendBtn.getText().equals("RESEND OTP"))
            resend();
    }
}
