package com.ciphers.ecommerce.Sellers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ciphers.ecommerce.Model.OrderReviews;
import com.ciphers.ecommerce.R;
import com.ciphers.ecommerce.ViewHolder.OrderReviewsViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class SellerSearchUserDetailActivity extends AppCompatActivity {

    String usernameStr;

    CircleImageView buyerImage;
    TextView buyerUsername, buyerFrontName, buyerUsernameFront;
    AppCompatActivity buyerRatting;
    DatabaseReference userRef, reviewsRef;
    RecyclerView reviewRecycler;

    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<OrderReviews, OrderReviewsViewHolder> ordersReviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_search_user_detail);

        usernameStr = getIntent().getStringExtra("username");

        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(usernameStr);
        reviewsRef = FirebaseDatabase.getInstance().getReference().child("Reviews");

        buyerImage = findViewById(R.id.seller_profile_image);
        buyerUsername = findViewById(R.id.seller_name_txt);
        buyerFrontName = findViewById(R.id.seller_shop_name_txt);
        reviewRecycler = findViewById(R.id.buyers_reviews_recycler);
        buyerUsernameFront = findViewById(R.id.seller_buyer_username_txt);

        fetchData();

        buyerUsername.setText(usernameStr);
        buyerUsernameFront.setText(usernameStr);

        startSellerReviewRecycler();
    }

    private void fetchData(){

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    String profileName = snapshot.child("fullName").getValue(String.class);
                    String profileImage = snapshot.child("userImage").getValue(String.class);

                    Picasso.get().load(profileImage).into(buyerImage);
                    buyerFrontName.setText(profileName);

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
                        .setQuery(reviewsRef.child("BuyersReviews").child(usernameStr),
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
