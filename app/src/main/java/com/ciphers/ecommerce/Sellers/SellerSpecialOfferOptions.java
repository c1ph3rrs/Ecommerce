package com.ciphers.ecommerce.Sellers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.ciphers.ecommerce.R;

public class SellerSpecialOfferOptions extends AppCompatActivity {

    ImageView sellerAddSpecialOfferIcon, sellerSpecialOfferOptionsBackIcon, sellerViewSpecialOfferIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_special_offer_options);

        sellerAddSpecialOfferIcon = findViewById(R.id.seller_add_special_offer_img);
        sellerSpecialOfferOptionsBackIcon = findViewById(R.id.seller_special_offer_options_back_icon);
        sellerViewSpecialOfferIcon = findViewById(R.id.seller_show_special_offer_img);

        sellerAddSpecialOfferIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SellerAddSpecialOffer.class));
            }
        });

        sellerSpecialOfferOptionsBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SellerSpecialOfferOptions.super.onBackPressed();
            }
        });

        sellerViewSpecialOfferIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), sellerViewSpecialOfferCardActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        SellerSpecialOfferOptions.super.onBackPressed();
    }
}