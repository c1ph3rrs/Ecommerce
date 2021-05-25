package com.ciphers.ecommerce.Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ciphers.ecommerce.Buyer.ProductsDetail;
import com.ciphers.ecommerce.Model.Products;
import com.ciphers.ecommerce.R;
import com.ciphers.ecommerce.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

public class SearchProductActivity extends AppCompatActivity {

    MaterialSearchBar searchProduct;
    RecyclerView searchProductRecycler;
    RecyclerView.LayoutManager layoutManager;

    private String searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);

        searchProduct = findViewById(R.id.searchBar);
        searchProductRecycler = findViewById(R.id.search_product_recycler);

        searchProduct.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {

            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                searchText = text.toString();
                searchText = searchText.substring(0, 1).toUpperCase() + searchText.substring(1).toLowerCase();
                Toast.makeText(getApplicationContext(), searchText, Toast.LENGTH_LONG).show();
//                searchProductRecyclerView();

                onStart();
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        searchProductRecycler.setAdapter(null);
        searchProductRecycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        searchProductRecycler.setLayoutManager(layoutManager);

        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Products");

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>().
                setQuery(productRef.orderByChild("productName").startAt(searchText), Products.class).build();


        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i, @NonNull final Products products) {
                productViewHolder.itemNameTV.setText(products.getProductName());
                productViewHolder.itemDescriptionTV.setText(products.getProductDescription());
//                productViewHolder.itemPriceTV.setText(products.getProductPrice() + "$");
                Picasso.get().load(products.getProductImage1()).placeholder(R.drawable.cart_logo).into(productViewHolder.itemImageView);

                productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent searchIntent = new Intent(getApplicationContext(), ProductsDetail.class);
                        searchIntent.putExtra("pID", products.getProductId());
                        startActivity(searchIntent);

                    }
                });

            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.buyer_product_items_card_design, parent, false);
                ProductViewHolder holder = new ProductViewHolder(view);
                return holder;
            }
        };

        searchProductRecycler.setAdapter(adapter);
        adapter.startListening();

    }
}