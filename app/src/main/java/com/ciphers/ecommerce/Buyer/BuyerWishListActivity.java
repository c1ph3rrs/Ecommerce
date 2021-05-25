package com.ciphers.ecommerce.Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.ciphers.ecommerce.Helper.WishListSwipeHelper;
import com.ciphers.ecommerce.Interface.MyButtonClickListener;
import com.ciphers.ecommerce.Model.Products;
import com.ciphers.ecommerce.Model.WishList;
import com.ciphers.ecommerce.Prevalent.Prevalent;
import com.ciphers.ecommerce.R;
import com.ciphers.ecommerce.SplashScreen;
import com.ciphers.ecommerce.ViewHolder.ProductViewHolder;
import com.ciphers.ecommerce.ViewHolder.WishListViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.paperdb.Paper;

public class BuyerWishListActivity extends AppCompatActivity {

    RecyclerView wishListRecyclerView;
    FirebaseRecyclerAdapter<WishList, WishListViewHolder> wishListAdapter;
    RecyclerView.LayoutManager layoutManager;
    DatabaseReference wishListRef;

    WishListSwipeHelper wishListSwipeHelper;

    ArrayList<String> clickList = new ArrayList<>();
    ImageView wishListBackIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_wish_list);

        wishListRef = FirebaseDatabase.getInstance().getReference().child("WishList");
        wishListBackIV = findViewById(R.id.wish_list_back_icon);


        wishListRecyclerView = findViewById(R.id.buyer_wish_list_recycler);

        wishListSwipeHelper = new WishListSwipeHelper(this, wishListRecyclerView, 150) {
            @Override
            public void initiateMyButtons(RecyclerView.ViewHolder viewHolder, List<WishListSwipeHelper.MyButtons> buffer) {
                buffer.add(new MyButtons(BuyerWishListActivity.this,
                        "Delete",
                        30,
                        R.drawable.bin_white,
                        Color.parseColor("#FF3C30"),
                        new MyButtonClickListener() {
                            @Override
                            public void onClick(int pos) {

                                for(int i=0; i < clickList.size() +1; i ++){

                                    if(i == pos){

                                        wishListRef.child(Prevalent.currentOnlineUser.getUsername()).child(clickList.get(i)).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(getApplicationContext(), "Product Removed from Wish list", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }

                                }


                            }
                        }));
            }
        };

        wishListBackIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuyerWishListActivity.super.onBackPressed();
            }
        });

        startWishListRecycler();

    }

    private void startWishListRecycler() {

        wishListRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        wishListRecyclerView.setLayoutManager(layoutManager);

        FirebaseRecyclerOptions<WishList> options = new FirebaseRecyclerOptions.Builder<WishList>()
                .setQuery(wishListRef.child(Prevalent.currentOnlineUser.getUsername()), WishList.class).build();

        wishListAdapter = new FirebaseRecyclerAdapter<WishList, WishListViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull WishListViewHolder wishListViewHolder, int i, @NonNull WishList wishList) {

                wishListViewHolder.wishListNameTxt.setText(wishList.getProductTitle());
                clickList.add(wishList.getProductID());

                wishListViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent productDetailIntent = new Intent(getApplicationContext(), ProductsDetail.class);
                        productDetailIntent.putExtra("pID", wishList.getProductID());
                        productDetailIntent.putExtra("activityType", "Buyer");
                        startActivity(productDetailIntent);
                    }
                });

            }

            @NonNull
            @Override
            public WishListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new WishListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.wish_list_card_view, parent, false));
            }
        };

        wishListRecyclerView.setAdapter(wishListAdapter);
        wishListAdapter.startListening();

    }
}

/*
*
* CharSequence options[] = new CharSequence[]{
                                                "Yes",
                                                "No"
                                        };

                                        AlertDialog.Builder builder = new AlertDialog.Builder(BuyerWishListActivity.this);
                                        builder.setTitle("Are you sure you want to Remove this.?");
                                        builder.setItems(options, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (which == 0) {

                                                    wishListRef.child(wishList.getProductID()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            Toast.makeText(getApplicationContext(), "Product Removed", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });

                                                } else {
                                                }
                                            }
                                        });
                                        builder.show();
*
*
* */