package com.ciphers.ecommerce.Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.ciphers.ecommerce.Prevalent.Prevalent;
import com.ciphers.ecommerce.R;
import com.ciphers.ecommerce.SplashScreen;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import io.paperdb.Paper;

public class BuyerChangePasswordActivity extends AppCompatActivity {

    TextInputLayout oldPasswordTxt, newPasswordTxt, confirmPasswordTxt;
    Button changePasswordBtn;
    ImageView changePasswordBackIcon;
    ProgressDialog loadingDialog;

    DatabaseReference passwordRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_change_password);

        passwordRef = FirebaseDatabase.getInstance().getReference();

        changePasswordBackIcon = findViewById(R.id.change_password_back_icon);
        oldPasswordTxt = findViewById(R.id.old_password_txt);
        newPasswordTxt = findViewById(R.id.new_password_txt);
        confirmPasswordTxt = findViewById(R.id.confirm_password_txt);
        changePasswordBtn = findViewById(R.id.change_password_btn);

        loadingDialog = new ProgressDialog(this);

        changePasswordBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuyerChangePasswordActivity.super.onBackPressed();
            }
        });

        changePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(oldPasswordTxt.getEditText().getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Please Enter Old Password", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(newPasswordTxt.getEditText().getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Please Enter New Password", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(confirmPasswordTxt.getEditText().getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Please Enter Confirm Password", Toast.LENGTH_SHORT).show();
                } else if (newPasswordTxt.getEditText().getText().toString().length() < 8) {
                    Toast.makeText(getApplicationContext(), "Password length greater than 7", Toast.LENGTH_SHORT).show();
                } else if (!newPasswordTxt.getEditText().getText().toString().equals(confirmPasswordTxt.getEditText().getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Password not match", Toast.LENGTH_SHORT).show();
                } else if (!oldPasswordTxt.getEditText().getText().toString().equals(Prevalent.currentOnlineUser.getPassword())) {
                    Toast.makeText(getApplicationContext(), "Current Password Wrong", Toast.LENGTH_SHORT).show();
                } else {
                    updatePassword();
                }
            }
        });

    }

    private void updatePassword() {

        loadingDialog.setTitle("Please wait");
        loadingDialog.setMessage("Password is Updating..");
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.show();

        HashMap<String, Object> passwordMap = new HashMap<>();
        passwordMap.put("password", newPasswordTxt.getEditText().getText().toString());

        passwordRef.child("Users").child(Prevalent.currentOnlineUser.getUsername()).updateChildren(passwordMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {

                    loadingDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Password Updated", Toast.LENGTH_LONG).show();

                    Prevalent.currentOnlineUser.setPassword(newPasswordTxt.getEditText().getText().toString());
                    Paper.book().write(Prevalent.userUserPasswordKey, newPasswordTxt.getEditText().getText().toString());
                    BuyerChangePasswordActivity.super.onBackPressed();


                } else {

                    Toast.makeText(getApplicationContext(), "Make sure you have an active Internet", Toast.LENGTH_LONG).show();
                    loadingDialog.dismiss();

                }
            }
        });

    }
}