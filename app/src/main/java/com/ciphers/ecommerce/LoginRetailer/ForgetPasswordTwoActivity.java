package com.ciphers.ecommerce.LoginRetailer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.ciphers.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ForgetPasswordTwoActivity extends AppCompatActivity {


    String userType, userName, userEmail, userEnterCode;
    TextView maskingEmailTV;
    PinView forgetOTPPinView;
    Button recoverPwdBtn;
    ImageView closeActivity;

    DatabaseReference forgetRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password_two);

        forgetRef = FirebaseDatabase.getInstance().getReference().child("Forget Password");

        userType = getIntent().getStringExtra("userType");
        userName = getIntent().getStringExtra("userName");
        userEmail = getIntent().getStringExtra("maskEmail");

        maskingEmailTV = findViewById(R.id.forget_password_username_txt);
        forgetOTPPinView = findViewById(R.id.forget_otp_code);
        recoverPwdBtn = findViewById(R.id.forget_recover_password_btn);
        closeActivity = findViewById(R.id.close_activity);

        maskingEmailTV.setText(userEmail);

        closeActivity.setOnClickListener(v -> forgetRef.child(userType).child(userName).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                ForgetPasswordTwoActivity.super.onBackPressed();
            }
        }));



        recoverPwdBtn.setOnClickListener(v -> {

            userEnterCode = forgetOTPPinView.getText().toString();

            if (TextUtils.isEmpty(userEnterCode)) {
                Toast.makeText(getApplicationContext(), "Field is Empty", Toast.LENGTH_LONG).show();
            } else if (userEnterCode.length() != 6) {
                Toast.makeText(getApplicationContext(), "Invalid Code", Toast.LENGTH_LONG).show();
            } else{
                verifyingCode();
            }

        });

    }

    private void verifyingCode(){



        forgetRef.child(userType).child(userName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    String code = snapshot.child("code").getValue(String.class);

                    if(userEnterCode.equals(code)){

                        forgetRef.child(userType).child(userName).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Intent newPasswordIntent = new Intent(getApplicationContext(), NewPasswordActivity.class);
                                newPasswordIntent.putExtra("activityType", userType);
                                newPasswordIntent.putExtra("userName", userName);
                                startActivity(newPasswordIntent);
                                finish();
                            }
                        });

                    }else{
                        Toast.makeText(getApplicationContext(), "Invalid Code", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        forgetRef.child(userType).child(userName).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        forgetRef.child(userType).child(userName).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }
}