package com.ciphers.ecommerce.LoginRetailer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ciphers.ecommerce.R;
import com.ciphers.ecommerce.SplashScreen;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class NewPasswordActivity extends AppCompatActivity {

    String userType, userName;
    TextInputLayout newPassword, confirmPassword;
    Button confirmPasswordBtn;

    DatabaseReference passwordRef;
    ProgressDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);

        passwordRef = FirebaseDatabase.getInstance().getReference();

        userType = getIntent().getStringExtra("activityType");
        userName = getIntent().getStringExtra("userName");

        newPassword = findViewById(R.id.new_password_txt);
        confirmPassword = findViewById(R.id.confirm_password_txt);
        confirmPasswordBtn = findViewById(R.id.change_password_btn);

        loadingDialog = new ProgressDialog(this);

        confirmPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });


    }

    private void validateData() {

        if (TextUtils.isEmpty(newPassword.getEditText().getText().toString().trim())) {
            Toast.makeText(getApplicationContext(), "Please enter New Password", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(confirmPassword.getEditText().getText().toString().trim())) {
            Toast.makeText(getApplicationContext(), "Please enter Confirm Password", Toast.LENGTH_SHORT).show();
        } else if (newPassword.getEditText().getText().toString().length() < 8) {
            Toast.makeText(getApplicationContext(), "Password length greater than 7", Toast.LENGTH_SHORT).show();
        } else if (!newPassword.getEditText().getText().toString().equals(confirmPassword.getEditText().getText().toString())) {
            Toast.makeText(getApplicationContext(), "Password not match", Toast.LENGTH_SHORT).show();
        } else {
            updatePassword();
        }
    }

    private void updatePassword() {
        loadingDialog.setTitle("Please wait");
        loadingDialog.setMessage("Password is Updating..");
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.show();

        HashMap<String, Object> passwordMap = new HashMap<>();
        passwordMap.put("password", newPassword.getEditText().getText().toString());

        passwordRef.child(userType).child(userName).updateChildren(passwordMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    loadingDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Password Updated", Toast.LENGTH_LONG).show();

                    Intent splashScreenIntent = new Intent(getApplicationContext(), SplashScreen.class);
                    splashScreenIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(splashScreenIntent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Make sure you have an active Internet", Toast.LENGTH_LONG).show();
                    loadingDialog.dismiss();
                }
            }
        });
    }
}