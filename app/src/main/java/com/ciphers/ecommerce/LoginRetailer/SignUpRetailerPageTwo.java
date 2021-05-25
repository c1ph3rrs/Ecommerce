package com.ciphers.ecommerce.LoginRetailer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.ciphers.ecommerce.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

public class SignUpRetailerPageTwo extends AppCompatActivity {

    ImageView SignUpPageBackBtn;
    Button SignUpNextBtn;
    TextInputLayout signUpPhoneNoTxt;
    CountryCodePicker ccp;
    ProgressDialog loadingBar;
    String fullName,userName,email,password, lat, lng, userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_retailer_page_two);

        signUpPhoneNoTxt = findViewById(R.id.sign_up_phone_no_txt);
        ccp = findViewById(R.id.sign_up_country_code_txt);
        loadingBar = new ProgressDialog(this);

        Intent signUpIntentPageTwoData = getIntent();
        fullName = signUpIntentPageTwoData.getStringExtra("fullName");
        userName = signUpIntentPageTwoData.getStringExtra("userName");
        email = signUpIntentPageTwoData.getStringExtra("email");
        password = signUpIntentPageTwoData.getStringExtra("password");
        userType = signUpIntentPageTwoData.getStringExtra("category");
        lat = signUpIntentPageTwoData.getStringExtra("locationLatitude");
        lng = signUpIntentPageTwoData.getStringExtra("locationLongitude");

        SignUpPageBackBtn = findViewById(R.id.sign_up_two_page_back_btn);
        SignUpNextBtn = findViewById(R.id.sign_up_page_two_next_btn);

        SignUpPageBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingBar.dismiss();
                SignUpRetailerPageTwo.super.onBackPressed();
            }
        });

        SignUpNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingBar.setTitle("Please Wait");
                loadingBar.setMessage("while we are checking credentials");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
                validatePageTwoSignUp();
                //startActivity(new Intent(getApplicationContext(), RetailerOTP.class));
            }
        });

    }

    public void validatePageTwoSignUp(){
        ccp.registerCarrierNumberEditText(signUpPhoneNoTxt.getEditText());
        final String phoneNo = ccp.getFullNumberWithPlus();
        final String number = phoneNo;



        //Toast.makeText(getApplicationContext(), phoneNo , Toast.LENGTH_LONG).show();
        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child("Users").child(number).exists())) {
                    Intent signUpIntentPagetwo = new Intent(getApplicationContext(), RetailerOTP.class);
                    loadingBar.dismiss();

                    signUpIntentPagetwo.putExtra("fullName", fullName);
                    signUpIntentPagetwo.putExtra("userName", userName);
                    signUpIntentPagetwo.putExtra("email", email);
                    signUpIntentPagetwo.putExtra("password", password);
                    signUpIntentPagetwo.putExtra("category", userType);
                    signUpIntentPagetwo.putExtra("phoneNo", phoneNo);
                    signUpIntentPagetwo.putExtra("locationLatitude", lat);
                    signUpIntentPagetwo.putExtra("locationLongitude", lng);
                    startActivity(signUpIntentPagetwo);


                } else {
                    loadingBar.dismiss();
                    Toast.makeText(getApplicationContext(), "Phone no already exist", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}