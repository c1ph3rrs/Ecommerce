package com.ciphers.ecommerce.Buyer;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ciphers.ecommerce.Model.Products;
import com.ciphers.ecommerce.R;
import com.ciphers.ecommerce.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchProductFragment extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView searchProductRecycler;
    RecyclerView.LayoutManager layoutManager;
//    FirebaseRecyclerAdapter<Products, ProductViewHolder> productsAdapter;
    MaterialSearchBar searchBarProduct;
    DatabaseReference productRef;
    String getProductName = "";

    public SearchProductFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchProductFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchProductFragment newInstance(String param1, String param2) {
        SearchProductFragment fragment = new SearchProductFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_product, container, false);


        productRef = FirebaseDatabase.getInstance().getReference().child("Products");
        searchProductRecycler = view.findViewById(R.id.buyer_products_recycler);
        searchBarProduct = view.findViewById(R.id.searchBar);
//        lastSearches = loadSearchSuggestionFromDisk();

        searchBarProduct.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getProductName = searchBarProduct.getText();
                textChangeRecycler(getProductName);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        searchBarProduct.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {

            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
//                Toast.makeText(getActivity(), "Product is :" + text.toString(), Toast.LENGTH_SHORT).show();
                getProductName = searchBarProduct.getText();
                startSearchProducts(getProductName);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

        return view;
    }


    private void startSearchProducts(String searchText) {

        Log.d("Function", "Product Called" + searchText);

        searchProductRecycler.setAdapter(null);
        searchProductRecycler.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getActivity(), 2);
        searchProductRecycler.setLayoutManager(layoutManager);
//.endAt(searchText + "\uf8ff")
        Query searchQuery = productRef.orderByChild("productName").startAt(searchText);

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>().
                setQuery(searchQuery, Products.class).build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> productsAdapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i, @NonNull Products products) {
                productViewHolder.itemNameTV.setText(products.getProductName());
                productViewHolder.itemSellerNameTV.setText(products.getProductSellerID());
                productViewHolder.itemProductPriceTV.setText(products.getProductPrice() + " pkr");
                Picasso.get().load(products.getProductImage1()).placeholder(R.drawable.cart_logo).into(productViewHolder.itemImageView);
            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.buyer_product_items_card_design, parent, false);
                ProductViewHolder holder = new ProductViewHolder(view);
                return holder;
            }
        };

        searchProductRecycler.setAdapter(productsAdapter);
        productsAdapter.startListening();

    }

    public void textChangeRecycler(String searchText) {

        searchProductRecycler.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2);
        searchProductRecycler.setLayoutManager(layoutManager);

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>().
                setQuery(productRef.orderByChild("productName").startAt(searchText), Products.class).build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> productsAdapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i, @NonNull Products products) {
                productViewHolder.itemNameTV.setText(products.getProductName());
                productViewHolder.itemSellerNameTV.setText(products.getProductSellerID());
                productViewHolder.itemProductPriceTV.setText(products.getProductPrice() + " pkr");
                Picasso.get().load(products.getProductImage1()).placeholder(R.drawable.cart_logo).into(productViewHolder.itemImageView);
            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.buyer_product_items_card_design, parent, false);
                ProductViewHolder holder = new ProductViewHolder(view);
                return holder;
            }
        };

        searchProductRecycler.setAdapter(productsAdapter);
        productsAdapter.startListening();

    }

}