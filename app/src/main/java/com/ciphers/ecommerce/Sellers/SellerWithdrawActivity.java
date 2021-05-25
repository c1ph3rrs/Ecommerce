package com.ciphers.ecommerce.Sellers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ciphers.ecommerce.Prevalent.Prevalent;
import com.ciphers.ecommerce.R;
import com.ciphers.ecommerce.SplashScreen;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import io.paperdb.Paper;

public class SellerWithdrawActivity extends AppCompatActivity {

    Button sellerWithdrawBtn;
    TextInputLayout sellerWithdrawBalanceTxt, sellerWithdrawAccountNoTxt, sellerWithdrawAccountNameTxt;
    TextView availableBalanceTxt;
    DatabaseReference availableBalanceRef, withdrawRequest;
    ImageView sellerWithdrawBackIcon;

    String availableBalance;

    boolean isRequest = false;
    float personalBalance = 0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_withdraw);

        availableBalanceRef = FirebaseDatabase.getInstance().getReference().child("Analytics").child("Sellers");
        withdrawRequest = FirebaseDatabase.getInstance().getReference().child("WithdrawRequest");

        availableBalanceTxt = findViewById(R.id.available_balance_txt);
        sellerWithdrawBtn = findViewById(R.id.withdraw_balance_btn);
        sellerWithdrawBalanceTxt = findViewById(R.id.seller_withdraw_amount_number_txt);
        sellerWithdrawAccountNoTxt = findViewById(R.id.seller_withdraw_account_number_txt);
        sellerWithdrawAccountNameTxt = findViewById(R.id.seller_withdraw_account_name_txt);
        sellerWithdrawBackIcon = findViewById(R.id.seller_withdraw_back_icon);

        sellerWithdrawBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                personalBalance = Float.parseFloat(availableBalance);
                validateData();


            }
        });

        getAvailableBalance();


        checkRequestOFWithdraw();

        sellerWithdrawBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SellerWithdrawActivity.super.onBackPressed();
            }
        });
    }

    private void validateData() {

        if (TextUtils.isEmpty(sellerWithdrawBalanceTxt.getEditText().getText().toString().trim())) {
            Toast.makeText(getApplicationContext(), "Please Enter the Amount", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(sellerWithdrawAccountNoTxt.getEditText().getText().toString().trim())) {
            Toast.makeText(getApplicationContext(), "Please Enter the Account No", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(sellerWithdrawAccountNameTxt.getEditText().getText().toString().trim())) {
            Toast.makeText(getApplicationContext(), "Please Enter the Account Bank Name", Toast.LENGTH_SHORT).show();
        } else if (Integer.parseInt(sellerWithdrawBalanceTxt.getEditText().getText().toString().trim()) < 4999) {
            Toast.makeText(getApplicationContext(), "Please Enter the Valid Amount", Toast.LENGTH_SHORT).show();
        } else if (personalBalance < 4999f) {
            Toast.makeText(getApplicationContext(), "Insufficient Balance to withdraw", Toast.LENGTH_SHORT).show();
        } else if (isRequest) {
            Toast.makeText(getApplicationContext(), "Withdraw request already is in Pending", Toast.LENGTH_SHORT).show();
        } else {

            CharSequence options[] = new CharSequence[]{
                    "Yes",
                    "No"
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(SellerWithdrawActivity.this);
            builder.setTitle("Make sure you have put correct Information.?. Otherwise you loss your payment.");
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == 0) {

                        withdrawRequest();

                    } else {
                    }
                }
            });
            builder.show();

        }

    }

    private void getAvailableBalance() {

        availableBalanceRef.child(Prevalent.currentOnlineSeller.getSellerUsername()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    availableBalance = snapshot.child("availableBalance").getValue(String.class);
                    availableBalanceTxt.setText("Available Balance : " + availableBalance);
                } else {
                    availableBalance = "0";
                    availableBalanceTxt.setText("Available Balance : 0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void withdrawRequest() {

        String paymentKey = FirebaseDatabase.getInstance().getReference().push().getKey();

        String saveCurrentTime, saveCurrentDate;

        Calendar calForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        HashMap<String, Object> paymentMap = new HashMap<>();
        paymentMap.put("sellerUsername", Prevalent.currentOnlineSeller.getSellerUsername());
        paymentMap.put("sellerPayment", sellerWithdrawBalanceTxt.getEditText().getText().toString().trim());
        paymentMap.put("sellerWithdrawDate", saveCurrentDate + " " + saveCurrentTime);
        paymentMap.put("sellerAccount", sellerWithdrawAccountNoTxt.getEditText().getText().toString().trim());
        paymentMap.put("sellerBank", sellerWithdrawAccountNameTxt.getEditText().getText().toString().trim());
        paymentMap.put("paymentID", paymentKey);

        withdrawRequest.child(Prevalent.currentOnlineSeller.getSellerUsername()).updateChildren(paymentMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                paymentRecord(paymentKey, Prevalent.currentOnlineSeller.getSellerUsername(), sellerWithdrawBalanceTxt.getEditText().getText().toString().trim()
                , saveCurrentDate + " " + saveCurrentTime, sellerWithdrawAccountNoTxt.getEditText().getText().toString().trim(),
                        sellerWithdrawAccountNameTxt.getEditText().getText().toString().trim());

            }
        });

    }

    private void paymentRecord(String paymentKey, String sellerUsername, String withdrawBalance, String time, String withdrawAccountNo, String bankName) {

        final DatabaseReference paymentHistoryMap = FirebaseDatabase.getInstance().getReference().child("PaymentHistory");

        HashMap<String, Object> paymentHistory = new HashMap<>();
        paymentHistory.put("sellerUsername", sellerUsername);
        paymentHistory.put("sellerPayment", withdrawBalance);
        paymentHistory.put("sellerWithdrawDate", time);
        paymentHistory.put("sellerAccount", withdrawAccountNo);
        paymentHistory.put("sellerBank", bankName);
        paymentHistory.put("sellerKey", paymentKey);
        paymentHistory.put("paymentStatus", "0");

        paymentHistoryMap.child(sellerUsername).child(paymentKey).updateChildren(paymentHistory).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getApplicationContext(), "Payment Request Send", Toast.LENGTH_SHORT).show();
                SellerWithdrawActivity.super.onBackPressed();
            }
        });

    }

    private void checkRequestOFWithdraw() {

        withdrawRequest.child(Prevalent.currentOnlineSeller.getSellerUsername()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    isRequest = true;
                } else {
                    isRequest = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}