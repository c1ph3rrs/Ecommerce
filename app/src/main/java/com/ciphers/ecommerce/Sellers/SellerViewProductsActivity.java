package com.ciphers.ecommerce.Sellers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ciphers.ecommerce.Buyer.ProductsDetail;
import com.ciphers.ecommerce.Model.Products;
import com.ciphers.ecommerce.Prevalent.Prevalent;
import com.ciphers.ecommerce.R;
import com.ciphers.ecommerce.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shreyaspatil.firebase.recyclerpagination.DatabasePagingOptions;
import com.shreyaspatil.firebase.recyclerpagination.FirebaseRecyclerPagingAdapter;
import com.shreyaspatil.firebase.recyclerpagination.LoadingState;
import com.squareup.picasso.Picasso;

public class SellerViewProductsActivity extends AppCompatActivity {

    ImageView sellerViewProductsBackIcon;
    RecyclerView sellerViewProductsRecycler;
    DatabaseReference sellerViewProductsRef;

    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Products, ProductViewHolder> productsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_view_products);

        sellerViewProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        sellerViewProductsBackIcon = findViewById(R.id.seller_view_products_back_icon);
        sellerViewProductsRecycler = findViewById(R.id.seller_view_products_recycler);

        startProductsRecycler();

        sellerViewProductsBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SellerViewProductsActivity.super.onBackPressed();
            }
        });

    }

    private void startProductsRecycler(){

        sellerViewProductsRecycler.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        sellerViewProductsRecycler.setLayoutManager(layoutManager);

        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(sellerViewProductsRef
                                .orderByChild("productSellerID")
                                .equalTo(Prevalent.currentOnlineSeller.getSellerUsername()),
                                Products.class)
                        .build();

        productsAdapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @SuppressLint("SetTextI18n")
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i, @NonNull Products products) {
                productViewHolder.itemNameTV.setText(products.getProductName());
//                productViewHolder.itemDescriptionTV.setText(products.getProductDescription());
                productViewHolder.itemSellerNameTV.setText(products.getProductSellerID());
                productViewHolder.itemProductPriceTV.setText(products.getProductPrice() + " pkr");
//                productViewHolder.itemPriceTV.setText(model.getProductPrice() + "$");
                Picasso.get().load(products.getProductImage1()).placeholder(R.drawable.cart_logo).into(productViewHolder.itemImageView);
                productViewHolder.likeBtn.setVisibility(View.GONE);

                productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent productIntent = new Intent(getApplicationContext(), ProductsDetail.class);
                        productIntent.putExtra("pID", products.getProductId());
                        productIntent.putExtra("activityType", "Seller");
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

        sellerViewProductsRecycler.setAdapter(productsAdapter);
        productsAdapter.startListening();

    }

    @Override
    public void onBackPressed() {
        SellerViewProductsActivity.super.onBackPressed();
    }
}