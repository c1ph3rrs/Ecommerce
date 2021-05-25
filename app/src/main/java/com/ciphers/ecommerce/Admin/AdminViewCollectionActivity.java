package com.ciphers.ecommerce.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ciphers.ecommerce.Model.Collection;
import com.ciphers.ecommerce.R;
import com.ciphers.ecommerce.ViewHolder.CollectionViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class AdminViewCollectionActivity extends AppCompatActivity {

    ImageView adminCollectionBackIcon;
    RecyclerView adminViewCollectionProductsRecyclerView;
    RecyclerView.LayoutManager layoutManager;

    DatabaseReference collectionRef;
    FirebaseRecyclerAdapter<Collection, CollectionViewHolder> collectionViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_collection);

        collectionRef = FirebaseDatabase.getInstance().getReference().child("Collections");

        adminCollectionBackIcon = findViewById(R.id.admin_view_collection_back_icon);
        adminViewCollectionProductsRecyclerView = findViewById(R.id.admin_collection_view_product_recycler);

        startCollectionRecycler();

        adminCollectionBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdminViewCollectionActivity.super.onBackPressed();
            }
        });

    }

    private void startCollectionRecycler() {

        adminViewCollectionProductsRecyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this, 2);
        adminViewCollectionProductsRecyclerView.setLayoutManager(layoutManager);

        FirebaseRecyclerOptions<Collection> option =
                new FirebaseRecyclerOptions.Builder<Collection>().setQuery(collectionRef, Collection.class).build();

        collectionViewAdapter = new FirebaseRecyclerAdapter<Collection, CollectionViewHolder>(option) {
            @Override
            protected void onBindViewHolder(@NonNull CollectionViewHolder collectionViewHolder, int i, @NonNull Collection collection) {
                collectionViewHolder.collectionProductName.setText(collection.getCollectionProductName());
                collectionViewHolder.collectionProductDescription.setText(collection.getCollectionProductDescription());
                collectionViewHolder.collectionProductPrice.setText(collection.getCollectionProductPrice() + "$");
                collectionViewHolder.collectionProductQuantity.setText(collection.getCollectionProductQuantity());
                Picasso.get().load(collection.getCollectionProductImage()).placeholder(R.drawable.cart_logo).into(collectionViewHolder.collectionProductImage);
            }

            @NonNull
            @Override
            public CollectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_collection_card_view, parent, false);
                CollectionViewHolder holder = new CollectionViewHolder(view);
                return holder;
            }
        };

        adminViewCollectionProductsRecyclerView.setAdapter(collectionViewAdapter);
        collectionViewAdapter.startListening();
    }

    @Override
    public void onBackPressed() {
        AdminViewCollectionActivity.super.onBackPressed();
    }
}