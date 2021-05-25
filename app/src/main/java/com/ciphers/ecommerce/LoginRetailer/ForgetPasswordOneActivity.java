package com.ciphers.ecommerce.LoginRetailer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.ciphers.ecommerce.Admin.AdminDashboard;
import com.ciphers.ecommerce.Admin.SellerRequestResponseActivity;
import com.ciphers.ecommerce.Branch.BranchDashboardActivity;
import com.ciphers.ecommerce.Buyer.HomeActivity;
import com.ciphers.ecommerce.Delivery.DeliveryGuyHomeActivity;
import com.ciphers.ecommerce.MailService.JavaMailAPI;
import com.ciphers.ecommerce.Model.Admins;
import com.ciphers.ecommerce.Model.Branch;
import com.ciphers.ecommerce.Model.DeliveryGuy;
import com.ciphers.ecommerce.Model.Sellers;
import com.ciphers.ecommerce.Model.Users;
import com.ciphers.ecommerce.Prevalent.Prevalent;
import com.ciphers.ecommerce.R;
import com.ciphers.ecommerce.Sellers.NonVerifySellerActivity;
import com.ciphers.ecommerce.Sellers.SellerHome;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import io.paperdb.Paper;

public class ForgetPasswordOneActivity extends AppCompatActivity {

    DatabaseReference userTypeRef;
    ArrayList<String> userTypeList = new ArrayList<>();
    Spinner userTypeSpinner;

    TextInputLayout forgetPasswordUsernameTxt;
    Button forgetPasswordSendCodeBtn;
    String randomNumber;
    ProgressDialog loadingBar;
    DatabaseReference forgetPwdRef;

    ImageView forgetPasswordBackIcon;

    String spinnerValue, usernameTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password_one);

        forgetPwdRef = FirebaseDatabase.getInstance().getReference().child("Forget Password");

        userTypeRef = FirebaseDatabase.getInstance().getReference().child("loginType");
        userTypeSpinner = findViewById(R.id.choose_forget_category_spinner);

        forgetPasswordUsernameTxt = findViewById(R.id.forget_one_username_txt);
        forgetPasswordSendCodeBtn = findViewById(R.id.send_code_btn);
        forgetPasswordBackIcon = findViewById(R.id.forget_password_one_back_icon);

        randomNumber = getRandomNumberString();

        loadingBar = new ProgressDialog(this);

        forgetPasswordSendCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateUsername();
            }
        });

        showDataSpinner();

        forgetPasswordBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForgetPasswordOneActivity.super.onBackPressed();
            }
        });

    }

    private void validateUsername() {

        spinnerValue = userTypeSpinner.getSelectedItem().toString();
        usernameTxt = forgetPasswordUsernameTxt.getEditText().getText().toString();


        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                if (snapshot.child(spinnerValue).child(usernameTxt).exists()) {


                    if (spinnerValue.equals("Users")) {
                        loadingBar.dismiss();

                        String dbEmail = snapshot.child(spinnerValue).child(usernameTxt).child("email").getValue(String.class);
                        String dbUserName = snapshot.child(spinnerValue).child(usernameTxt).child("username").getValue(String.class);


                        String maskingEmail = dbEmail.replaceAll("(^[^@]{3}|(?!^)\\G)[^@]", "$1*");

                        sendEmail(dbEmail, dbUserName);
                        saveCodeToDB(dbUserName, "Users", randomNumber, maskingEmail);


                    } else if (spinnerValue.equals("Sellers")) {
                        loadingBar.dismiss();


                        String sellerDbUsername = snapshot.child(spinnerValue).child(usernameTxt).child("sellerUsername").getValue(String.class);
                        String sellerDbShopEmail = snapshot.child(spinnerValue).child(usernameTxt).child("sellerShopEmail").getValue(String.class);

                        String maskingEmail = sellerDbShopEmail.replaceAll("(^[^@]{3}|(?!^)\\G)[^@]", "$1*");

                        sendEmail(sellerDbShopEmail, sellerDbUsername);
                        saveCodeToDB(sellerDbUsername, "Sellers", randomNumber, maskingEmail);


                    } else {
                        loadingBar.dismiss();
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                    }


                } else {
                    loadingBar.dismiss();
                    Toast.makeText(getApplicationContext(), "Account with this " + usernameTxt + " username do not exists.", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void saveCodeToDB(String username, String userType, String code, String maskingEmail){

        HashMap<String, Object> forgetPwdMap = new HashMap<>();

        forgetPwdMap.put("username", username);
        forgetPwdMap.put("code", code);



        forgetPwdRef.child(userType).child(username).updateChildren(forgetPwdMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Intent forgetPwdTwoIntent = new Intent(getApplicationContext(), ForgetPasswordTwoActivity.class);
                forgetPwdTwoIntent.putExtra("userType", userType);
                forgetPwdTwoIntent.putExtra("userName", username);
                forgetPwdTwoIntent.putExtra("maskEmail", maskingEmail);
                startActivity(forgetPwdTwoIntent);

            }
        });

    }

    private void sendEmail(String email, String username) {

        String messageBody = "\t\t\n" +
                "Dear IB Express User, " + username +" \n" +
                "\n" +
                "We received a request to access your IB Express Account " + username + " through your email address. Your IB Express verification code is:\n" +
                "\n" +
                randomNumber+
                "\n\n" +
                "If you did not request this code, it is possible that someone else is trying to access the IB Express Account " + username + ". Do not forward this mail or do not give this code to anyone" +
                "\n\n"+
                "Regards : Ib Express Team ";

        String to = email;
        Log.d("Email is", "Email" + email);
        String subject = "IB Express Verification Code";
        String body = messageBody;

        JavaMailAPI javaMailAPI = new JavaMailAPI(this, to, subject, body);
        javaMailAPI.execute();

    }

    private void showDataSpinner() {
        userTypeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userTypeList.clear();
                for (DataSnapshot items : snapshot.getChildren()) {

                    if (items.child("type").getValue(String.class).equals("Admins")) {

                    } else if (items.child("type").getValue(String.class).equals("DeliveryGuy")) {

                    } else if (items.child("type").getValue(String.class).equals("Branches")) {

                    } else {
                        userTypeList.add(items.child("type").getValue(String.class));
                    }

                }
                ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, userTypeList);
                categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                userTypeSpinner.setAdapter(categoryAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        ForgetPasswordOneActivity.super.onBackPressed();
    }

    public static String getRandomNumberString() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        return String.format("%06d", number);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}