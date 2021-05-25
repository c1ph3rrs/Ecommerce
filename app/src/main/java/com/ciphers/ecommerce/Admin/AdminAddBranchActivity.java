package com.ciphers.ecommerce.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ciphers.ecommerce.MailService.JavaMailAPI;
import com.ciphers.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AdminAddBranchActivity extends AppCompatActivity {

    TextInputLayout adminAddBranchCodeTxt, adminAddBranchPasswordTxt, adminAddBranchCityTxt, adminAddBranchPhoneTxt, adminAddBranchAddressTxt, adminAddBranchEmailTxt;
    Button adminAddNewBranchBtn;
    DatabaseReference branchRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_branch);

        branchRef = FirebaseDatabase.getInstance().getReference().child("Branches");

        adminAddNewBranchBtn = findViewById(R.id.admin_add_new_branch_btn);

        adminAddBranchCodeTxt = findViewById(R.id.branch_code_txt);
        adminAddBranchPasswordTxt = findViewById(R.id.branch_password_txt);
        adminAddBranchCityTxt = findViewById(R.id.branch_city_txt);
        adminAddBranchPhoneTxt = findViewById(R.id.branch_phone_no_txt);
        adminAddBranchAddressTxt = findViewById(R.id.branch_address_txt);
        adminAddBranchEmailTxt = findViewById(R.id.branch_email_txt);


        adminAddNewBranchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validateBranchData();

            }
        });

    }

    private void validateBranchData() {

        if (TextUtils.isEmpty(adminAddBranchCodeTxt.getEditText().getText().toString().trim())) {
            Toast.makeText(getApplicationContext(), "Please Enter the Branch Code", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(adminAddBranchPasswordTxt.getEditText().getText().toString().trim())) {
            Toast.makeText(getApplicationContext(), "Please Enter the Branch Password", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(adminAddBranchEmailTxt.getEditText().getText().toString().trim())) {
            Toast.makeText(getApplicationContext(), "Please Enter the Branch Email", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(adminAddBranchCityTxt.getEditText().getText().toString().trim())) {
            Toast.makeText(getApplicationContext(), "Please Enter the Branch City", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(adminAddBranchPhoneTxt.getEditText().getText().toString().trim())) {
            Toast.makeText(getApplicationContext(), "Please Enter the Branch Phone", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(adminAddBranchAddressTxt.getEditText().getText().toString().trim())) {
            Toast.makeText(getApplicationContext(), "Please Enter the Branch Address", Toast.LENGTH_LONG).show();
        } else {
            uploadBranchRecord();
        }

    }

    private void uploadBranchRecord(){

        HashMap<String, Object> branchMap = new HashMap<>();
        branchMap.put("branchCode", adminAddBranchCodeTxt.getEditText().getText().toString().trim());
        branchMap.put("password", adminAddBranchPasswordTxt.getEditText().getText().toString().trim());
        branchMap.put("username", adminAddBranchCodeTxt.getEditText().getText().toString().trim());
        branchMap.put("branchEmail", adminAddBranchEmailTxt.getEditText().getText().toString().trim());
        branchMap.put("branchPhone", adminAddBranchPhoneTxt.getEditText().getText().toString().trim());
        branchMap.put("branchCity", adminAddBranchCityTxt.getEditText().getText().toString().trim());
        branchMap.put("branchAddress", adminAddBranchAddressTxt.getEditText().getText().toString().trim());

        branchRef.child(adminAddBranchCodeTxt.getEditText().getText().toString().trim()).updateChildren(branchMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getApplicationContext(), "Branch Added Successfully", Toast.LENGTH_LONG).show();
                sendEmail();
            }
        });
    }

    private void sendEmail() {

        String messageBody = "Hi " + adminAddBranchEmailTxt.getEditText().getText().toString() + " your branch is part of IbExpress. " +
                "We are going share you your account info. Please save this email. " +
                "If you forget your username and password you can check from this email. Here is the login credentials. " +
                "Username : " + adminAddBranchCodeTxt.getEditText().getText().toString() + "\n" +
                "Password : " + adminAddBranchPasswordTxt.getEditText().getText().toString() + "\n" +
                "Email : " + adminAddBranchEmailTxt.getEditText().getText().toString() + "\n" +
                "Phone No : " + adminAddBranchPhoneTxt.getEditText().getText().toString() + "\n" +
                "Address : " + adminAddBranchAddressTxt.getEditText().getText().toString() + "\n" +
                "Branch Code : " + adminAddBranchCodeTxt.getEditText().getText().toString() + "\n"+
                "Branch City : " + adminAddBranchCityTxt.getEditText().getText().toString() + "\n"+
                "You can use your username and password to login on this app.";

        String to = adminAddBranchEmailTxt.getEditText().getText().toString();
        String subject = "Application Approved from IB EXPRESS";
        String body = messageBody;

        JavaMailAPI javaMailAPI = new JavaMailAPI(this, to, subject, body);
        javaMailAPI.execute();


        AdminAddBranchActivity.super.onBackPressed();
    }
}