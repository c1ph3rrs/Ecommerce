package com.ciphers.ecommerce.Sellers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.ciphers.ecommerce.R;

public class SellerDealOfferOptions extends AppCompatActivity {

    ImageView addNewDealBtn, viewDealCardsBtn, backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_deal_offer_options);

        addNewDealBtn = findViewById(R.id.seller_add_deal_img);
        viewDealCardsBtn = findViewById(R.id.seller_view_deals_btn);
        backBtn = findViewById(R.id.deal_offer_options_back_icon);

        addNewDealBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SellerAddDealOffers.class));
            }
        });

        viewDealCardsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SellerViewDealCard.class));
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SellerDealOfferOptions.super.onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        SellerDealOfferOptions.super.onBackPressed();
    }
}