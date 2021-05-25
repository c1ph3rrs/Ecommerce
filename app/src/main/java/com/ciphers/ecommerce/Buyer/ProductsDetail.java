package com.ciphers.ecommerce.Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.ciphers.ecommerce.FullScreenImageActivity;
import com.ciphers.ecommerce.Model.Products;
import com.ciphers.ecommerce.Prevalent.Prevalent;
import com.ciphers.ecommerce.R;
import com.ciphers.ecommerce.Sellers.SellerEditProductActivity;
import com.ciphers.ecommerce.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductsDetail extends AppCompatActivity {

    ImageView productDetailCloseBtn;
    TextView productDetailNameTxt, productDetailDescriptionTxt, productDetailPriceTxt, productSellerTxt, productCategoryTxt;
    ElegantNumberButton elegantNumberButton;
    FloatingActionButton addToCartButton;
    String productId = "", state = "normal";
    ImageView productDetailImage1, productDetailImage2, productDetailImage3, productDetailImage4, editProductIcon;
    String imageUri1, imageUri2, imageUri3, imageUri4, imageTitle, activityType;
    DatabaseReference cartListRef, orderRef, wishListRef, productsRef;
    FirebaseRecyclerAdapter<Products, ProductViewHolder> productsAdapter;
    RecyclerView.LayoutManager layoutManager;

    CardView sellerViewProfileCardView;

    RecyclerView productDetailRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        orderRef = FirebaseDatabase.getInstance().getReference().child("Orders");

        wishListRef = FirebaseDatabase.getInstance().getReference().child("WishList").child(Prevalent.currentOnlineUser.getUsername());
        productsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        productId = getIntent().getStringExtra("pID");
        activityType = getIntent().getStringExtra("activityType");

        productDetailCloseBtn = findViewById(R.id.close_product_detail_icon);
        productDetailImage1 = findViewById(R.id.buyer_product_detail_main_img);
        productDetailImage2 = findViewById(R.id.buyer_product_detail_two_img);
        productDetailImage3 = findViewById(R.id.buyer_product_detail_three_img);
        productDetailImage4 = findViewById(R.id.buyer_product_detail_four_img);
        productDetailNameTxt = findViewById(R.id.product_detail_name);
        productDetailDescriptionTxt = findViewById(R.id.product_detail_description);
        productDetailPriceTxt = findViewById(R.id.product_detail_price);
        productSellerTxt = findViewById(R.id.product_seller_name);
        elegantNumberButton = findViewById(R.id.product_increment_btn);
        addToCartButton = findViewById(R.id.product_cart_btn);
        editProductIcon = findViewById(R.id.seller_edit_product);
        productCategoryTxt = findViewById(R.id.product_category_txt);
        sellerViewProfileCardView = findViewById(R.id.seller_view_profile_card_view);
        productDetailRecycler = findViewById(R.id.product_detail_recycler);

        productDetailCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductsDetail.super.onBackPressed();
            }
        });

        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (state.equals("Order Shipped") || state.equals("Order Placed")) {
                    Toast.makeText(getApplicationContext(), "You can purchase more Products once your order is Shipped", Toast.LENGTH_LONG).show();
                } else {
                    addTOCartList();
                }

            }
        });

        productDetailImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fullScreenIntent = new Intent(getApplicationContext(), FullScreenImageActivity.class);
                fullScreenIntent.setData(Uri.parse(imageUri1));
                fullScreenIntent.putExtra("image_title", imageTitle);
                startActivity(fullScreenIntent);
            }
        });

        productDetailImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fullScreenIntent = new Intent(getApplicationContext(), FullScreenImageActivity.class);
                fullScreenIntent.setData(Uri.parse(imageUri2));
                fullScreenIntent.putExtra("image_title", imageTitle);
                startActivity(fullScreenIntent);
            }
        });

        productDetailImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fullScreenIntent = new Intent(getApplicationContext(), FullScreenImageActivity.class);
                fullScreenIntent.setData(Uri.parse(imageUri3));
                fullScreenIntent.putExtra("image_title", imageTitle);
                startActivity(fullScreenIntent);
            }
        });

        productDetailImage4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fullScreenIntent = new Intent(getApplicationContext(), FullScreenImageActivity.class);
                fullScreenIntent.setData(Uri.parse(imageUri4));
                fullScreenIntent.putExtra("image_title", imageTitle);
                startActivity(fullScreenIntent);
            }
        });

        if (activityType.equals("Seller")) {
            elegantNumberButton.setVisibility(View.GONE);
            addToCartButton.setVisibility(View.GONE);
        } else if (activityType.equals("Buyer")) {
            elegantNumberButton.setVisibility(View.VISIBLE);
            addToCartButton.setVisibility(View.VISIBLE);
            editProductIcon.setVisibility(View.GONE);
        }

        editProductIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editProductIntent = new Intent(getApplicationContext(), SellerEditProductActivity.class);
                editProductIntent.putExtra("productID", productId);
                startActivity(editProductIntent);
            }
        });

        sellerViewProfileCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sellerProfileIntent = new Intent(getApplicationContext(), SellerProfileActivity.class);
                sellerProfileIntent.putExtra("username", productSellerTxt.getText().toString());
                startActivity(sellerProfileIntent);
            }
        });


        getProductDetail(productId);

    }


    private void addTOCartList() {

        String saveCurrentTime, saveCurrentDate;

        Calendar calForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("productID", productId.toString());
        cartMap.put("productName", productDetailNameTxt.getText().toString());
        cartMap.put("productImg", imageUri1);
        cartMap.put("productPrice", productDetailPriceTxt.getText().toString());
        cartMap.put("productSeller", productSellerTxt.getText().toString());
        cartMap.put("productBuyer", Prevalent.currentOnlineUser.getUsername());
        cartMap.put("productType", "Product");
        cartMap.put("productDate", saveCurrentDate);
        cartMap.put("productTime", saveCurrentTime);
        cartMap.put("productQuantity", elegantNumberButton.getNumber().toString());

        cartListRef.child(Prevalent.currentOnlineUser.username).child(productId).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    ProductsDetail.super.onBackPressed();
                    Toast.makeText(getApplicationContext(), "Product Added to Cart", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void getProductDetail(final String productId) {

//        Toast.makeText(getApplicationContext(), "Function Called", Toast.LENGTH_LONG).show();

        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Products");

        productRef.child(productId).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {


                    Products products = dataSnapshot.getValue(Products.class);

                    productDetailNameTxt.setText(products.getProductName());
                    productDetailPriceTxt.setText(products.getProductPrice());
                    productSellerTxt.setText(products.getProductSellerID());
                    productDetailDescriptionTxt.setText(products.getProductDescription());
                    productCategoryTxt.setText(products.getProductParentCategory() + " > " + products.getProductCategory() + " > " + products.getProductThirdCategory());

                    Picasso.get().load(products.getProductImage1()).into(productDetailImage1);
                    Picasso.get().load(products.getProductImage2()).into(productDetailImage2);
                    Picasso.get().load(products.getProductImage3()).into(productDetailImage3);
                    Picasso.get().load(products.getProductImage4()).into(productDetailImage4);

                    imageUri1 = products.getProductImage1();
                    imageUri2 = products.getProductImage2();
                    imageUri3 = products.getProductImage3();
                    imageUri4 = products.getProductImage4();
                    imageTitle = products.getProductThirdCategory();

                    startProductRecycler();

                } else {
                    Toast.makeText(getApplicationContext(), "Data not exist", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void startProductRecycler() {


        productDetailRecycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        productDetailRecycler.setLayoutManager(layoutManager);

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(productsRef.orderByChild("productSellerID").equalTo(productSellerTxt.getText().toString()), Products.class).build();

        productsAdapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i, @NonNull Products products) {

                productViewHolder.itemNameTV.setText(products.getProductName());
                productViewHolder.itemSellerNameTV.setText(products.getProductSellerID());
                productViewHolder.itemProductPriceTV.setText(products.getProductPrice() + " pkr");
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
                        wishListMap.put("productTitle", products.getProductName());

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

        productDetailRecycler.setAdapter(productsAdapter);
        productsAdapter.startListening();

    }


    @Override
    public void onBackPressed() {
        ProductsDetail.super.onBackPressed();
    }
}