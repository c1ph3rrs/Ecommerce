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

import com.ciphers.ecommerce.Model.Deals;
import com.ciphers.ecommerce.R;
import com.ciphers.ecommerce.ViewHolder.DealViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;

public class SellerViewDealCard extends AppCompatActivity {

    ImageView sellerDealCardFrontBackIcon;
    RecyclerView sellerDealCardFrontRecyclerView;
    RecyclerView.LayoutManager layoutManager;

    DatabaseReference sellerDealRef;
    FirebaseRecyclerAdapter<Deals, DealViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_view_deal_card);

        Paper.init(this);

        sellerDealRef = FirebaseDatabase.getInstance().getReference().child("Deals");

        sellerDealCardFrontBackIcon = findViewById(R.id.seller_deal_card_back_btn);
        sellerDealCardFrontRecyclerView = findViewById(R.id.seller_deal_recycler_view);

        sellerDealCardFrontBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SellerViewDealCard.super.onBackPressed();
            }
        });

        dealCardFrontRecycler();

    }

    private void dealCardFrontRecycler() {

        sellerDealCardFrontRecyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this, 2);
        sellerDealCardFrontRecyclerView.setLayoutManager(layoutManager);

        FirebaseRecyclerOptions<Deals> options =
                new FirebaseRecyclerOptions.Builder<Deals>().setQuery(sellerDealRef, Deals.class).build();

        adapter = new FirebaseRecyclerAdapter<Deals, DealViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull DealViewHolder dealViewHolder, int i, @NonNull Deals deals) {
                dealViewHolder.frontDealCardName.setText(deals.getDealProductName());
                dealViewHolder.frontDealCardSeller.setText(deals.getDealProductSellerID());
                Picasso.get().load(deals.getDealProductImage1()).placeholder(R.drawable.cart_logo).into(dealViewHolder.frontDealCardImage);

                dealViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent dealIntent = new Intent(getApplicationContext(), SellerViewDeals.class);
                        dealIntent.putExtra("dealProductID", deals.getDealProductId());
                        dealIntent.putExtra("dealProductName", deals.getDealProductName());
                        dealIntent.putExtra("dealProductDescription", deals.getDealProductDescription());
                        dealIntent.putExtra("dealProductPrice", deals.getDealProductPrice());
                        dealIntent.putExtra("dealProductQtyLeft", deals.getDealProductQuantityLeft());
                        dealIntent.putExtra("dealProductImage1", deals.getDealProductImage1());
                        dealIntent.putExtra("dealProductImage2", deals.getDealProductImage2());
                        dealIntent.putExtra("dealProductImage3", deals.getDealProductImage3());
                        dealIntent.putExtra("dealProductImage4", deals.getDealProductImage4());
                        startActivity(dealIntent);
                    }
                });
            }

            @NonNull
            @Override
            public DealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.special_offers_card_view_front, parent, false);
                DealViewHolder holder = new DealViewHolder(view);
                return holder;
            }
        };

        sellerDealCardFrontRecyclerView.setAdapter(adapter);
        adapter.startListening();

    }
}