package com.ciphers.ecommerce.Admin;

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

import com.ciphers.ecommerce.Buyer.ProductsDetail;
import com.ciphers.ecommerce.Model.Products;
import com.ciphers.ecommerce.Model.Sellers;
import com.ciphers.ecommerce.Prevalent.Prevalent;
import com.ciphers.ecommerce.R;
import com.ciphers.ecommerce.ViewHolder.AdminSellersRequestViewHolder;
import com.ciphers.ecommerce.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class AdminSellersRequestsActivity extends AppCompatActivity {

    ImageView adminSellerRequestBackIcon;

    DatabaseReference sellerRef;
    RecyclerView adminSellersRequestRecycler;
    FirebaseRecyclerAdapter<Sellers, AdminSellersRequestViewHolder> sellerRequestAdapter;
    RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_sellers_requests);

        sellerRef = FirebaseDatabase.getInstance().getReference().child("Sellers");

        adminSellerRequestBackIcon = findViewById(R.id.admin_seller_request_back_icon);
        adminSellersRequestRecycler = findViewById(R.id.seller_request_recycler);

        adminSellerRequestBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdminSellersRequestsActivity.super.onBackPressed();
            }
        });

        startSellerRequest();
    }

    private void startSellerRequest(){

        adminSellersRequestRecycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        adminSellersRequestRecycler.setLayoutManager(layoutManager);

        FirebaseRecyclerOptions<Sellers> options =
                new FirebaseRecyclerOptions.Builder<Sellers>()
                        .setQuery(sellerRef
                                        .orderByChild("sellerStatus")
                                        .equalTo("0"),
                                Sellers.class)
                        .build();

        sellerRequestAdapter = new FirebaseRecyclerAdapter<Sellers, AdminSellersRequestViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdminSellersRequestViewHolder adminSellersRequestViewHolder, int i, @NonNull Sellers sellers) {

                adminSellersRequestViewHolder.sellerRequestName.setText("Name : " + sellers.getSellerName());
                adminSellersRequestViewHolder.sellerRequestShopName.setText("Shop Name : " +sellers.getSellerShopName());
                adminSellersRequestViewHolder.sellerRequestUsername.setText("Username : " +sellers.getSellerUsername());
                adminSellersRequestViewHolder.sellerRequestEmail.setText("Email : " +sellers.getSellerShopEmail());
                adminSellersRequestViewHolder.sellerRequestPhone.setText("Phone : " +sellers.getSellerShopPhone());
                adminSellersRequestViewHolder.sellerRequestAddress.setText("Address : " +sellers.getSellerShopLocation());
                adminSellersRequestViewHolder.sellerRequestJoinDate.setText("Join : " +sellers.getSellerJoin());
                adminSellersRequestViewHolder.sellerRequestLicence.setText("Licence : " +sellers.getSellerShopIdentity());
                adminSellersRequestViewHolder.sellerRequestCategory.setText("Category : " + sellers.getSellerShopCategory());

                Picasso.get().load(sellers.getShopImage()).placeholder(R.drawable.profile).into(adminSellersRequestViewHolder.sellerRequestIV);


                adminSellersRequestViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent adminSellerRequestDetailIntent = new Intent(getApplicationContext(), AdminSellerRequestDetailActivity.class);
                        adminSellerRequestDetailIntent.putExtra("name", sellers.getSellerName());
                        adminSellerRequestDetailIntent.putExtra("shopName", sellers.getSellerShopName());
                        adminSellerRequestDetailIntent.putExtra("username", sellers.getSellerUsername());
                        adminSellerRequestDetailIntent.putExtra("email", sellers.getSellerShopEmail());
                        adminSellerRequestDetailIntent.putExtra("address", sellers.getSellerShopLocation());
                        adminSellerRequestDetailIntent.putExtra("phone", sellers.getSellerShopPhone());
                        adminSellerRequestDetailIntent.putExtra("join", sellers.getSellerJoin());
                        adminSellerRequestDetailIntent.putExtra("licence", sellers.getSellerShopIdentity());
                        adminSellerRequestDetailIntent.putExtra("image", sellers.getShopImage());
                        adminSellerRequestDetailIntent.putExtra("category",  sellers.getSellerShopCategory());
                        startActivity(adminSellerRequestDetailIntent);
                    }
                });

            }

            @NonNull
            @Override
            public AdminSellersRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new AdminSellersRequestViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_request_card_view, parent, false));
            }


        };

        adminSellersRequestRecycler.setAdapter(sellerRequestAdapter);
        sellerRequestAdapter.startListening();

    }

    @Override
    public void onBackPressed() {
        AdminSellersRequestsActivity.super.onBackPressed();
    }
}