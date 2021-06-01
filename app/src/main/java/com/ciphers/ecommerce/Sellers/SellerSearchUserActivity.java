package com.ciphers.ecommerce.Sellers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ciphers.ecommerce.Buyer.SellerProfileActivity;
import com.ciphers.ecommerce.Model.Sellers;
import com.ciphers.ecommerce.Model.Users;
import com.ciphers.ecommerce.Prevalent.Prevalent;
import com.ciphers.ecommerce.R;
import com.ciphers.ecommerce.ViewHolder.SellersViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

public class SellerSearchUserActivity extends AppCompatActivity {


    MaterialSearchBar searchBarSeller;
    DatabaseReference sellerRef, buyerAnalyticsRef;

    private RecyclerView searchSellerRecycler;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Users, SellersViewHolder> sellerAdapter;

    String totalRattingNumbStr = "";
    int totalOrder;
    float totalEarning, ratting;
    String getSellerName = "";

    String sellerLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_search_user);

        sellerRef = FirebaseDatabase.getInstance().getReference().child("Users");
        buyerAnalyticsRef = FirebaseDatabase.getInstance().getReference().child("Analytics");

        searchBarSeller = findViewById(R.id.seller_buyer_search_bar);

        searchSellerRecycler = findViewById(R.id.seller_search_buyer_recycler);

        searchBarSeller.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                Toast.makeText(getActivity(), "Category is :" + searchBarCategory.getText(), Toast.LENGTH_SHORT).show();
                getSellerName = searchBarSeller.getText();
                seeSearchSellers(getSellerName);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void seeSearchSellers(String username) {

        searchSellerRecycler.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        searchSellerRecycler.setLayoutManager(layoutManager);

        Query searchQuery = sellerRef.orderByChild("username").startAt(username);

        FirebaseRecyclerOptions<Users> options = new FirebaseRecyclerOptions.Builder<Users>().
                setQuery(searchQuery, Users.class).build();

        sellerAdapter = new FirebaseRecyclerAdapter<Users, SellersViewHolder>(options) {
            @SuppressLint("SetTextI18n")
            @Override
            protected void onBindViewHolder(@NonNull SellersViewHolder sellersViewHolder, int i, @NonNull Users users) {
                sellersViewHolder.sellerShopNameTV.setText(users.getFullName());
                sellersViewHolder.sellerShopCategoryTV.setVisibility(View.GONE);
                sellersViewHolder.sellerNameTV.setText(users.getUsername());

                sellersViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent sellerProfileIntent = new Intent(getApplicationContext(), SellerSearchUserDetailActivity.class);
                        sellerProfileIntent.putExtra("username", users.getUsername());
                        startActivity(sellerProfileIntent);
                    }
                });


                Picasso.get().load(users.getImage()).placeholder(R.drawable.profile).into(sellersViewHolder.sellerProfileIV);

//                checkSellerAnalytics(sellersViewHolder.sellerRatting, sellersViewHolder.sellerNumberTV, sellersViewHolder.sellerLevelTV, sellers.getSellerUsername());
                fetchBuyerRecord(sellersViewHolder.sellerRatting, sellersViewHolder.sellerNumberTV, sellersViewHolder.sellerLevelTV, users.getUsername());

            }

            @NonNull
            @Override
            public SellersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_profile_card_view, parent, false);
                SellersViewHolder holder = new SellersViewHolder(view);
                return holder;
            }
        };

        searchSellerRecycler.setAdapter(sellerAdapter);
        sellerAdapter.startListening();

    }


    private void fetchBuyerRecord(AppCompatRatingBar sellerRatting, TextView sellerNumberTV, TextView sellerLevelTV, String username) {

        buyerAnalyticsRef.child("Buyers").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    String totalBuying = snapshot.child("totalBuying").getValue(String.class);
                    String totalNumberRatting = snapshot.child("totalNumberRatting").getValue(String.class);
                    String totalOrders = snapshot.child("totalOrders").getValue(String.class);
                    String totalRatting = snapshot.child("totalRatting").getValue(String.class);

                    totalRattingNumbStr = String.valueOf(Float.parseFloat(totalRatting) / Float.parseFloat(totalNumberRatting));

                    sellerRatting.setRating(Float.parseFloat(totalRattingNumbStr));
                    sellerRatting.setIsIndicator(true);
                    sellerNumberTV.setText(totalOrders);


                    checkSellerAnalytics(sellerLevelTV, totalRattingNumbStr, totalOrders, totalBuying);

                } else {

                    sellerLevelTV.setText("No level");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void checkSellerAnalytics(TextView buyerProfileLevelTxt, String totalRattingNumbStr, String totalOrders, String totalBuying) {

        int levelOneOrders = 15, levelTwoOrders = 50, topOrders = 100;
        float levelOneEarning = 10000, levelTwoEarning = 30000, topEarning = 60000;
        float levelOneRatting = (float) 3.5, levelTwoRatting = (float) 3.9, topRatting = (float) 4.0;

        totalOrder = Integer.parseInt(totalOrders);
        totalEarning = Float.parseFloat(totalBuying);
        ratting = Float.parseFloat(String.valueOf(totalRattingNumbStr));

        if (totalOrder >= topOrders && totalEarning >= topEarning && ratting >= topRatting) {
            buyerProfileLevelTxt.setText("Top Level");
        } else if (totalOrder >= levelTwoOrders && totalEarning >= levelTwoEarning && ratting >= levelTwoRatting) {
            buyerProfileLevelTxt.setText("Level Two");
        } else if (totalOrder >= levelOneOrders && totalEarning >= levelOneEarning && ratting >= levelOneRatting) {
            buyerProfileLevelTxt.setText("Level One");
        } else {
            buyerProfileLevelTxt.setText("No level");
        }

    }

}