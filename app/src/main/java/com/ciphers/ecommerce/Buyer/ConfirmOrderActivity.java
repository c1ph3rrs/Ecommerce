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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmOrderActivity extends AppCompatActivity {

    String totalAmount = "";
    ImageView confirmOrderCloseBtn;

    Button confirmOrderBtn;
    TextInputLayout senderNameTxt, receiverNameTxt, senderCityTxt, receiverCityTxt, senderPhoneTxt, receiverPhoneTxt, shippingAddressTxt;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

        totalAmount = getIntent().getStringExtra("Total Price");
        confirmOrderCloseBtn = findViewById(R.id.confirm_order_back_btn);
        confirmOrderBtn = findViewById(R.id.confirm_order_btn);

        senderNameTxt = findViewById(R.id.confirm_order_sender_name_txt);
        senderCityTxt = findViewById(R.id.confirm_order_sender_city_txt);
        senderPhoneTxt = findViewById(R.id.confirm_order_sender_phone_txt);

        receiverNameTxt = findViewById(R.id.confirm_order_receiver_name_txt);
        receiverCityTxt = findViewById(R.id.confirm_order_receiver_city_txt);
        receiverPhoneTxt = findViewById(R.id.confirm_order_receiver_phone_txt);
        shippingAddressTxt = findViewById(R.id.confirm_order_shipping_address_txt);
        progressDialog = new ProgressDialog(this);

        confirmOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateForm();
            }
        });

        confirmOrderCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmOrderActivity.super.onBackPressed();
            }
        });
    }

    private void validateForm() {

        if (TextUtils.isEmpty(senderNameTxt.getEditText().getText().toString().trim())) {
            Toast.makeText(getApplicationContext(), "Field is Empty", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(senderCityTxt.getEditText().getText().toString().trim())) {
            Toast.makeText(getApplicationContext(), "Field is Empty", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(senderPhoneTxt.getEditText().getText().toString().trim())) {
            Toast.makeText(getApplicationContext(), "Field is Empty", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(receiverNameTxt.getEditText().getText().toString().trim())) {
            Toast.makeText(getApplicationContext(), "Field is Empty", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(receiverCityTxt.getEditText().getText().toString().trim())) {
            Toast.makeText(getApplicationContext(), "Field is Empty", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(receiverPhoneTxt.getEditText().getText().toString().trim())) {
            Toast.makeText(getApplicationContext(), "Field is Empty", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(shippingAddressTxt.getEditText().getText().toString().trim())) {
            Toast.makeText(getApplicationContext(), "Field is Empty", Toast.LENGTH_SHORT).show();
        } else {
            progressDialog.setTitle("Register Order");
            progressDialog.setMessage("Please wait to upload...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            confirmOrder();

        }

    }

    private void confirmOrder() {
        final String saveCurrentTime, saveCurrentDate;

        Calendar calForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child(Prevalent.currentOnlineUser.getUsername());

        HashMap<String, Object> ordersMap = new HashMap<>();
        ordersMap.put("totalAmount", totalAmount);
        ordersMap.put("userName", Prevalent.currentOnlineUser.getUsername());
        ordersMap.put("senderName", senderNameTxt.getEditText().getText().toString());
        ordersMap.put("receiverName", receiverNameTxt.getEditText().getText().toString());
        ordersMap.put("senderPhone", senderPhoneTxt.getEditText().getText().toString());
        ordersMap.put("receiverPhone", receiverPhoneTxt.getEditText().getText().toString());
        ordersMap.put("senderCity", senderCityTxt.getEditText().getText().toString());
        ordersMap.put("receiverCity", receiverCityTxt.getEditText().getText().toString());
        ordersMap.put("shippingAddress", shippingAddressTxt.getEditText().getText().toString());
        ordersMap.put("date", saveCurrentDate);
        ordersMap.put("time", saveCurrentTime);
        ordersMap.put("state", "Not Shipped");

        ordersRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                FirebaseDatabase.getInstance().getReference()
                        .child("Cart List")
                        .child("User View")
                        .child(Prevalent.currentOnlineUser.getUsername())
                        .removeValue()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Your Order Has been Registered", Toast.LENGTH_SHORT).show();
                                    Toast.makeText(getApplicationContext(), "We will Deliver it soon, Thanks", Toast.LENGTH_SHORT).show();

                                    Intent confirmOrderActivity = new Intent(getApplicationContext(), HomeActivity.class);
                                    confirmOrderActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(confirmOrderActivity);
                                    finish();
                                }
                            }
                        });
            }
        });
    }

    @Override
    public void onBackPressed() {
       ConfirmOrderActivity.super.onBackPressed();
    }
}