package com.ciphers.ecommerce.Sellers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ciphers.ecommerce.Admin.AdminAddNewProducts;
import com.ciphers.ecommerce.Model.SpecialOffer;
import com.ciphers.ecommerce.R;
import com.ciphers.ecommerce.ViewHolder.CateogryViewHolder;
import com.ciphers.ecommerce.ViewHolder.SpecialOfferViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;

public class sellerViewSpecialOfferCardActivity extends AppCompatActivity {

    ImageView sellerSpecialOfferViewCardFrontBackIcon;
    RecyclerView specialOfferCardFrontRecyclerView;
    RecyclerView.LayoutManager layoutManager;

    DatabaseReference specialOfferRef;
    FirebaseRecyclerAdapter<SpecialOffer, SpecialOfferViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_view_special_offer_card);

        Paper.init(this);

        specialOfferRef = FirebaseDatabase.getInstance().getReference().child("Special Offers");

        sellerSpecialOfferViewCardFrontBackIcon = findViewById(R.id.seller_special_offer_view_card_front_back_icon);
        specialOfferCardFrontRecyclerView = findViewById(R.id.special_offers_front_cards_recycler);

        sellerSpecialOfferViewCardFrontBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sellerViewSpecialOfferCardActivity.super.onBackPressed();
            }
        });

        specialOfferCardFrontRecycler();
    }

    private void specialOfferCardFrontRecycler() {

        specialOfferCardFrontRecyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this, 2);
        specialOfferCardFrontRecyclerView.setLayoutManager(layoutManager);

        FirebaseRecyclerOptions<SpecialOffer> options =
                new FirebaseRecyclerOptions.Builder<SpecialOffer>().setQuery(specialOfferRef, SpecialOffer.class).build();


        adapter = new FirebaseRecyclerAdapter<SpecialOffer, SpecialOfferViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull SpecialOfferViewHolder specialOfferViewHolder, int i, @NonNull SpecialOffer specialOffer) {
                specialOfferViewHolder.specialOfferProductNameTxt.setText(specialOffer.getSpecialProductName());
                specialOfferViewHolder.specialOfferProductSellerTxt.setText(specialOffer.getSpecialProductSellerID());
                specialOfferViewHolder.specialOfferProductPriceTxt.setText(specialOffer.getSpecialProductPrice());
                Picasso.get().load(specialOffer.getSpecialProductImage1()).placeholder(R.drawable.cart_logo).into(specialOfferViewHolder.frontSpecialCardImage);

                specialOfferViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent productIntent = new Intent(getApplicationContext(), SellerViewSpecialOffers.class);
                        productIntent.putExtra("specialProductID", specialOffer.getSpecialOfferProductId());
                        productIntent.putExtra("activityType", "Seller");
                        startActivity(productIntent);
                    }
                });
            }

            @NonNull
            @Override
            public SpecialOfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.special_offers_card_view_front, parent, false);
                SpecialOfferViewHolder holder = new SpecialOfferViewHolder(view);
                return holder;
            }
        };

        specialOfferCardFrontRecyclerView.setAdapter(adapter);
        adapter.startListening();

    }
}