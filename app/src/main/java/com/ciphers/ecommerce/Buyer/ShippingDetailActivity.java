package com.ciphers.ecommerce.Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ciphers.ecommerce.Prevalent.Prevalent;
import com.ciphers.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ShippingDetailActivity extends AppCompatActivity {

    CardView shippingDetailPlacedOrder, onlinePaymentBtn;
    DatabaseReference buyerCartRef, buyerPlacedOrderRef;
    String totalPrice;
    TextView totalPriceTV, shippingChargesTV, totalCartItems, grandTotal;
    TextInputLayout senderNameTxt, receiverNameTxt, senderCityTxt, receiverCityTxt, senderPhoneTxt, receiverPhoneTxt, shippingAddressTxt;
    String senderName, receiverName, senderCity, receiverCity, senderPhone, receiverPhone, shippingAddress;
    String totalCartItemsCounter, totalCartItem, totalShipping;
    int totalItems = 1;
    ImageView shippingDetailBackIcon;
    int grandTotalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_detail);

        totalPrice = getIntent().getStringExtra("totalPrice");
        totalCartItemsCounter = getIntent().getStringExtra("totalCartItems");
        totalShipping = getIntent().getStringExtra("shippingCharges");

        grandTotalPrice = Integer.parseInt(totalPrice) + Integer.parseInt(totalShipping);

        buyerCartRef = FirebaseDatabase.getInstance().getReference().child("Cart List").child(Prevalent.currentOnlineUser.username);
        buyerPlacedOrderRef = FirebaseDatabase.getInstance().getReference().child("Orders");

        shippingDetailPlacedOrder = findViewById(R.id.placed_order_btn);
        totalPriceTV = findViewById(R.id.shipping_detail_order_charges_txt);
        shippingChargesTV = findViewById(R.id.shipping_detail_shipping_charges_txt);
        onlinePaymentBtn = findViewById(R.id.online_payment_btn);

        senderNameTxt = findViewById(R.id.shipping_detail_sender_name_txt);
        senderCityTxt = findViewById(R.id.shipping_detail_sender_city_txt);
        senderPhoneTxt = findViewById(R.id.shipping_detail_sender_phone_txt);
        totalCartItems = findViewById(R.id.shipping_detail_total_items_txt);

        receiverNameTxt = findViewById(R.id.shipping_detail_receiver_name_txt);
        receiverCityTxt = findViewById(R.id.shipping_detail_receiver_city_txt);
        receiverPhoneTxt = findViewById(R.id.shipping_detail_receiver_phone_txt);
        shippingAddressTxt = findViewById(R.id.shipping_detail_shipping_address_txt);
        shippingDetailBackIcon = findViewById(R.id.shipping_detail_back_icon);
        grandTotal = findViewById(R.id.grand_total_price_txt);

        totalItems += Integer.parseInt(totalCartItemsCounter);

        totalPriceTV.setText(totalPrice);
        totalCartItems.setText(String.valueOf(totalItems));
        shippingChargesTV.setText(totalShipping);

        grandTotal.setText(String.valueOf(grandTotalPrice));

        shippingDetailPlacedOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String orderPayment = "COD";

                placedOrder(orderPayment);
            }
        });

        onlinePaymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String orderPayment = "Online";

                validateData(orderPayment);

//                startActivity(new Intent(getApplicationContext(), BuyerOrderPaymentTypeActivity.class));
            }
        });

        shippingDetailBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShippingDetailActivity.super.onBackPressed();
            }
        });

    }

    private void validateData(String orderPayment){

        senderName = senderNameTxt.getEditText().getText().toString();
        senderCity = senderCityTxt.getEditText().getText().toString();
        senderPhone = senderPhoneTxt.getEditText().getText().toString();

        receiverName = receiverNameTxt.getEditText().getText().toString();
        receiverCity = receiverCityTxt.getEditText().getText().toString();
        receiverPhone = receiverPhoneTxt.getEditText().getText().toString();
        shippingAddress = shippingAddressTxt.getEditText().getText().toString();
        totalCartItem = totalCartItems.getText().toString();

        if(TextUtils.isEmpty(senderName)){
            Toast.makeText(getApplicationContext(), "Please Enter the Sender Name", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(senderCity)){
            Toast.makeText(getApplicationContext(), "Please Enter the Sender City", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(senderPhone)){
            Toast.makeText(getApplicationContext(), "Please Enter the Sender Phone", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(receiverName)){
            Toast.makeText(getApplicationContext(), "Please Enter the Receiver Name", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(receiverCity)){
            Toast.makeText(getApplicationContext(), "Please Enter the Receiver City", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(receiverPhone)){
            Toast.makeText(getApplicationContext(), "Please Enter the Receiver Phone", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(shippingAddress)){
            Toast.makeText(getApplicationContext(), "Please Enter the Shipping Address", Toast.LENGTH_SHORT).show();
        }else{

            Intent paymentTypeIntent = new Intent(ShippingDetailActivity.this, BuyerOrderPaymentTypeActivity.class);
            paymentTypeIntent.putExtra("senderName", senderName);
            paymentTypeIntent.putExtra("senderCity", senderCity);
            paymentTypeIntent.putExtra("senderPhone", senderPhone);
            paymentTypeIntent.putExtra("receiverName", receiverName);
            paymentTypeIntent.putExtra("receiverCity", receiverCity);
            paymentTypeIntent.putExtra("receiverPhone", receiverPhone);
            paymentTypeIntent.putExtra("shippingAddress", shippingAddress);
            paymentTypeIntent.putExtra("totalCartItems", totalCartItem);
            paymentTypeIntent.putExtra("paymentType", orderPayment);
            paymentTypeIntent.putExtra("paymentNumber", String.valueOf(grandTotalPrice));
            startActivity(paymentTypeIntent);
        }


    }

    private void placedOrder(String orderPayment) {

        senderName = senderNameTxt.getEditText().getText().toString();
        senderCity = senderCityTxt.getEditText().getText().toString();
        senderPhone = senderPhoneTxt.getEditText().getText().toString();

        receiverName = receiverNameTxt.getEditText().getText().toString();
        receiverCity = receiverCityTxt.getEditText().getText().toString();
        receiverPhone = receiverPhoneTxt.getEditText().getText().toString();
        shippingAddress = shippingAddressTxt.getEditText().getText().toString();
        totalCartItem = totalCartItems.getText().toString();

        if(TextUtils.isEmpty(senderName)){
            Toast.makeText(getApplicationContext(), "Please Enter the Sender Name", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(senderCity)){
            Toast.makeText(getApplicationContext(), "Please Enter the Sender City", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(senderPhone)){
            Toast.makeText(getApplicationContext(), "Please Enter the Sender Phone", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(receiverName)){
            Toast.makeText(getApplicationContext(), "Please Enter the Receiver Name", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(receiverCity)){
            Toast.makeText(getApplicationContext(), "Please Enter the Receiver City", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(receiverPhone)){
            Toast.makeText(getApplicationContext(), "Please Enter the Receiver Phone", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(shippingAddress)){
            Toast.makeText(getApplicationContext(), "Please Enter the Shipping Address", Toast.LENGTH_SHORT).show();
        }else{
            Intent sendDataToCartPlaceOrderIntent = new Intent(getApplicationContext(), CartActivity.class);
            sendDataToCartPlaceOrderIntent.putExtra("senderName", senderName);
            sendDataToCartPlaceOrderIntent.putExtra("senderCity", senderCity);
            sendDataToCartPlaceOrderIntent.putExtra("senderPhone", senderPhone);
            sendDataToCartPlaceOrderIntent.putExtra("receiverName", receiverName);
            sendDataToCartPlaceOrderIntent.putExtra("receiverCity", receiverCity);
            sendDataToCartPlaceOrderIntent.putExtra("receiverPhone", receiverPhone);
            sendDataToCartPlaceOrderIntent.putExtra("shippingAddress", shippingAddress);
            sendDataToCartPlaceOrderIntent.putExtra("totalCartItems", totalCartItem);
            sendDataToCartPlaceOrderIntent.putExtra("paymentType", orderPayment);
            startActivity(sendDataToCartPlaceOrderIntent);
        }
//        String.valueOf(grandTotalPrice)
    }

}