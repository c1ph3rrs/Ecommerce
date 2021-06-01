package com.ciphers.ecommerce.Buyer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ciphers.ecommerce.R;

public class BuyerOrderPaymentTypeActivity extends AppCompatActivity {


    ImageButton buyerOnlinePaymentJazzCashBtn;
    String senderName, senderCity, senderPhone, receiverName, receiverCity, receiverPhone, shippingAddress, paymentType, totalCartItems, totalPrice;
    TextView senderNameTxt, receiverNameTxt, senderCityTxt, receiverCityTxt, senderPhoneTxt, receiverPhoneTxt, shippingAddressTxt;
    String ResponseCode;
    ImageView orderPaymentTypeActivityBackIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_order_payment_type);

        senderName = getIntent().getStringExtra("senderName");
        senderCity = getIntent().getStringExtra("senderCity");
        senderPhone = getIntent().getStringExtra("senderPhone");
        receiverName = getIntent().getStringExtra("receiverName");
        receiverCity = getIntent().getStringExtra("receiverCity");
        receiverPhone = getIntent().getStringExtra("receiverPhone");
        shippingAddress = getIntent().getStringExtra("shippingAddress");
        paymentType = getIntent().getStringExtra("paymentType");
        totalCartItems = getIntent().getStringExtra("totalCartItems");
        totalPrice = getIntent().getStringExtra("paymentNumber");

        senderNameTxt = findViewById(R.id.sender_name_lbl);
        receiverNameTxt = findViewById(R.id.receiver_name_lbl);
        senderCityTxt = findViewById(R.id.sender_city_lbl);
        receiverCityTxt = findViewById(R.id.receiver_city_lbl);
        senderPhoneTxt = findViewById(R.id.sender_phone_lbl);
        receiverPhoneTxt = findViewById(R.id.receiver_phone_lbl);
        shippingAddressTxt = findViewById(R.id.shipping_address_lbl);
        orderPaymentTypeActivityBackIcon = findViewById(R.id.buyer_order_payment_back_icon);

        senderNameTxt.setText(senderName);
        senderCityTxt.setText(senderCity);
        senderPhoneTxt.setText(senderPhone);
        receiverNameTxt.setText(receiverName);
        receiverCityTxt.setText(receiverCity);
        receiverPhoneTxt.setText(receiverPhone);
        shippingAddressTxt.setText(shippingAddress);

        buyerOnlinePaymentJazzCashBtn = findViewById(R.id.buyer_online_payment_jazz_cash_btn);

        orderPaymentTypeActivityBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuyerOrderPaymentTypeActivity.super.onBackPressed();
            }
        });

        buyerOnlinePaymentJazzCashBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent jazzCashPaymentIntent = new Intent(getApplicationContext(), JazzCash.class);
                jazzCashPaymentIntent.putExtra("price", totalPrice);
                startActivityForResult(jazzCashPaymentIntent, 0);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            ResponseCode = data.getStringExtra("pp_ResponseCode");
        } else {
            ResponseCode = "";
        }


        System.out.println("DateFn: ResponseCode:" + ResponseCode);

        if (ResponseCode.equals(null)) {

        } else {

            if (ResponseCode.equals("000")) {

                Toast.makeText(getApplicationContext(), "Payment Success", Toast.LENGTH_SHORT).show();

                Intent sendDataToCartPlaceOrderIntent = new Intent(getApplicationContext(), CartActivity.class);
                sendDataToCartPlaceOrderIntent.putExtra("senderName", senderName);
                sendDataToCartPlaceOrderIntent.putExtra("senderCity", senderCity);
                sendDataToCartPlaceOrderIntent.putExtra("senderPhone", senderPhone);
                sendDataToCartPlaceOrderIntent.putExtra("receiverName", receiverName);
                sendDataToCartPlaceOrderIntent.putExtra("receiverCity", receiverCity);
                sendDataToCartPlaceOrderIntent.putExtra("receiverPhone", receiverPhone);
                sendDataToCartPlaceOrderIntent.putExtra("shippingAddress", shippingAddress);
                sendDataToCartPlaceOrderIntent.putExtra("totalCartItems", totalCartItems);
                sendDataToCartPlaceOrderIntent.putExtra("paymentType", "Online");
                startActivity(sendDataToCartPlaceOrderIntent);


            } else {
                Toast.makeText(getApplicationContext(), "Payment Failed", Toast.LENGTH_SHORT).show();
            }
        }

    }
}