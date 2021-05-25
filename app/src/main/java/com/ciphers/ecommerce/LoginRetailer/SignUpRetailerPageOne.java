package com.ciphers.ecommerce.LoginRetailer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.ciphers.ecommerce.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.Permission;
import java.util.ArrayList;

public class SignUpRetailerPageOne extends AppCompatActivity {

    ImageView SignUpPageBackBtn;
    Button SignUpNextBtn;

    TextInputLayout signUpFullNameTxt, signUpUserNameTxt, signUpEmailTxt, signUpPasswordTxt;
    Spinner signUpCategorySpinner;
    ProgressDialog loadingProgressBar;
    DatabaseReference userTypeRef;
    ArrayList<String> userTypeList = new ArrayList<>();
    String lat, lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_retailer_page_one);

        lat = getIntent().getStringExtra("latitude");
        lng = getIntent().getStringExtra("longitude");

        Log.d("latitude is ", "Lat : " + lat);
        Log.d("longitude is ", "Lng : " + lng);

        userTypeRef = FirebaseDatabase.getInstance().getReference().child("loginType");

        SignUpPageBackBtn = findViewById(R.id.sign_up_page_one_back_btn);
        SignUpNextBtn = findViewById(R.id.sign_up_page_one_next_btn);

        signUpFullNameTxt = findViewById(R.id.sign_up_full_name_txt);
        signUpUserNameTxt = findViewById(R.id.sign_up_user_name_txt);
        signUpEmailTxt = findViewById(R.id.sign_up_email_txt);
        signUpPasswordTxt = findViewById(R.id.sign_up_password_txt);
        signUpCategorySpinner = findViewById(R.id.choose_sign_up_category_spinner);


        showDataSpinner();

        loadingProgressBar = new ProgressDialog(this);

        SignUpPageBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpRetailerPageOne.super.onBackPressed();
            }
        });

        SignUpNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateForm();
            }
        });

    }

    void showDataSpinner(){

        String spinnerValue = "";

        userTypeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userTypeList.clear();
                for (DataSnapshot items : snapshot.getChildren()) {

                    if(items.child("type").getValue(String.class).equals("Admins")){

                    }else if(items.child("type").getValue(String.class).equals("Delivery Guy")){

                    }else{
                        userTypeList.add(items.child("type").getValue(String.class));
                    }


                }
                ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, userTypeList);
                categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                signUpCategorySpinner.setAdapter(categoryAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void validateForm() {

        final String fullName = signUpFullNameTxt.getEditText().getText().toString();
        final String userName = signUpUserNameTxt.getEditText().getText().toString();
        final String email = signUpEmailTxt.getEditText().getText().toString();
        final String password = signUpPasswordTxt.getEditText().getText().toString();
        final String userType = signUpCategorySpinner.getSelectedItem().toString();

        if (TextUtils.isEmpty(fullName)) {
            Toast.makeText(getApplicationContext(), "Please Enter Name", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(userName)) {
            Toast.makeText(getApplicationContext(), "Please Enter Username", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Please Enter Email", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Please Enter Password", Toast.LENGTH_LONG).show();
        } else {
            loadingProgressBar.setTitle("Please Wait");
            loadingProgressBar.setMessage("Please Wait while we are checking user credentials");
            loadingProgressBar.setCanceledOnTouchOutside(false);
            loadingProgressBar.show();

            final DatabaseReference rootRef;
            rootRef = FirebaseDatabase.getInstance().getReference();

            rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (!(dataSnapshot.child(userType).child(userName).exists())) {
                        Intent signUpIntentPageOne = new Intent(getApplicationContext(), SignUpRetailerPageTwo.class);
                        loadingProgressBar.dismiss();

                        signUpIntentPageOne.putExtra("fullName", fullName);
                        signUpIntentPageOne.putExtra("userName", userName);
                        signUpIntentPageOne.putExtra("email", email);
                        signUpIntentPageOne.putExtra("password", password);
                        signUpIntentPageOne.putExtra("category", userType);
                        signUpIntentPageOne.putExtra("locationLatitude", lat);
                        signUpIntentPageOne.putExtra("locationLongitude", lng);

                        startActivity(signUpIntentPageOne);
                    } else {
                        loadingProgressBar.dismiss();
                        Toast.makeText(getApplicationContext(), "This username already exist", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}