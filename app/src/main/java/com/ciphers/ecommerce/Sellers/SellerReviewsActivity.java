package com.ciphers.ecommerce.Sellers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.ciphers.ecommerce.Buyer.ProductsDetail;
import com.ciphers.ecommerce.Model.OrderReviews;
import com.ciphers.ecommerce.Model.Products;
import com.ciphers.ecommerce.Model.SellersOrders;
import com.ciphers.ecommerce.Prevalent.Prevalent;
import com.ciphers.ecommerce.R;
import com.ciphers.ecommerce.ViewHolder.OrderReviewsViewHolder;
import com.ciphers.ecommerce.ViewHolder.ProductViewHolder;
import com.ciphers.ecommerce.ViewHolder.SellersOrdersViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shreyaspatil.firebase.recyclerpagination.FirebaseRecyclerPagingAdapter;
import com.squareup.picasso.Picasso;

public class SellerReviewsActivity extends AppCompatActivity {

    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<OrderReviews, OrderReviewsViewHolder> ordersReviewAdapter;
    DatabaseReference orderReviewRef;
    RecyclerView reviewRecycler;
    ImageView sellerReviewBackIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_reviews);

        orderReviewRef = FirebaseDatabase.getInstance().getReference().child("Reviews");
        sellerReviewBackIV = findViewById(R.id.seller_reviews_back_icon);
        reviewRecycler = findViewById(R.id.seller_review_recycler);

        checkReviewExist();

        sellerReviewBackIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SellerReviewsActivity.super.onBackPressed();
            }
        });

    }

    private void checkReviewExist() {

        orderReviewRef.child("SellersReviews").child(Prevalent.currentOnlineSeller.getSellerUsername())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            startSellerReviewRecycler();
                        } else {
                            Toast.makeText(getApplicationContext(), "No Reviews Till Yet", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void startSellerReviewRecycler() {

        reviewRecycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        reviewRecycler.setLayoutManager(layoutManager);
        FirebaseRecyclerOptions<OrderReviews> options =
                new FirebaseRecyclerOptions.Builder<OrderReviews>()
                        .setQuery(orderReviewRef.child("SellersReviews").child(Prevalent.currentOnlineSeller.getSellerUsername()),
                                OrderReviews.class)
                        .build();

        ordersReviewAdapter = new FirebaseRecyclerAdapter<OrderReviews, OrderReviewsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrderReviewsViewHolder orderReviewsViewHolder, int i, @NonNull OrderReviews orderReviews) {
                orderReviewsViewHolder.reviewName.setText(orderReviews.getBuyerName());
                orderReviewsViewHolder.reviewText.setText(orderReviews.getReview());
                orderReviewsViewHolder.reviewRatting.setText(orderReviews.getRatting());
                orderReviewsViewHolder.rattingBar.setRating(1);
                orderReviewsViewHolder.rattingBar.setIsIndicator(true);

            }

            @NonNull
            @Override
            public OrderReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new OrderReviewsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.order_review_cv, parent, false));
            }


        };

        reviewRecycler.setAdapter(ordersReviewAdapter);
        ordersReviewAdapter.startListening();

    }

}