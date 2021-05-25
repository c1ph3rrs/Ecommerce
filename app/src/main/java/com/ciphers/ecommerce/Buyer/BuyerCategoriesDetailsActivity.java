package com.ciphers.ecommerce.Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ciphers.ecommerce.Model.Products;
import com.ciphers.ecommerce.Prevalent.Prevalent;
import com.ciphers.ecommerce.R;
import com.ciphers.ecommerce.ViewHolder.ProductViewHolder;
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

import java.util.HashMap;

public class BuyerCategoriesDetailsActivity extends AppCompatActivity {

    String categoryName = "";

    RecyclerView buyerCategoryDetailRecycler;
    FirebaseRecyclerAdapter<Products, ProductViewHolder> productAdapter;

    DatabaseReference categoryRef, wishListRef;
    RecyclerView.LayoutManager layoutManager;

    ImageView buyerCategoryDetailBackIcon;
    TextView buyerCategoryDetailNameTxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_categories_details);

        categoryName = getIntent().getStringExtra("category");

        categoryRef = FirebaseDatabase.getInstance().getReference().child("Products");
        wishListRef = FirebaseDatabase.getInstance().getReference().child("WishList").child(Prevalent.currentOnlineUser.getUsername());

        buyerCategoryDetailRecycler = findViewById(R.id.buyer_detail_categories_recycler);
        buyerCategoryDetailNameTxt = findViewById(R.id.buyer_category_detail_category_txt);
        buyerCategoryDetailBackIcon = findViewById(R.id.buyer_category_detail_back_icon);

        buyerCategoryDetailBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuyerCategoriesDetailsActivity.super.onBackPressed();
            }
        });

        buyerCategoryDetailNameTxt.setText(categoryName);

        startProductRecycler();

    }


    private void startProductRecycler(){

        buyerCategoryDetailRecycler.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        buyerCategoryDetailRecycler.setLayoutManager(layoutManager);

        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(categoryRef
                                        .orderByChild("productThirdCategory")
                                        .equalTo(categoryName),
                                Products.class)
                        .build();

        productAdapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @SuppressLint("SetTextI18n")
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i, @NonNull Products products) {
                productViewHolder.itemNameTV.setText(products.getProductName());
//                productViewHolder.itemDescriptionTV.setText(products.getProductDescription());
                productViewHolder.itemSellerNameTV.setText(products.getProductSellerID());
                productViewHolder.itemProductPriceTV.setText(products.getProductPrice() + " pkr");
//                productViewHolder.itemPriceTV.setText(model.getProductPrice() + "$");
                Picasso.get().load(products.getProductImage1()).placeholder(R.drawable.cart_logo).into(productViewHolder.itemImageView);

                wishListRef.child(products.getProductId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {

                            productViewHolder.likeBtn.setLiked(true);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                productViewHolder.likeBtn.setOnLikeListener(new OnLikeListener() {
                    @Override
                    public void liked(LikeButton likeButton) {

                        HashMap<String, Object> wishListMap = new HashMap<>();
                        wishListMap.put("productID", products.getProductId());

                        wishListRef.child(products.getProductId()).updateChildren(wishListMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getApplicationContext(), "Product Added to Wish list", Toast.LENGTH_LONG).show();
                            }
                        });

                    }

                    @Override
                    public void unLiked(LikeButton likeButton) {

                        wishListRef.child(products.getProductId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getApplicationContext(), "Product Removed from Wish list", Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                });

                productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent productIntent = new Intent(getApplicationContext(), ProductsDetail.class);
                        productIntent.putExtra("pID", products.getProductId());
                        productIntent.putExtra("activityType", "Buyer");
                        startActivity(productIntent);
                    }
                });

            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new ProductViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.buyer_product_items_card_design, parent, false));
            }

        };

        buyerCategoryDetailRecycler.setAdapter(productAdapter);
        productAdapter.startListening();

    }
}