package com.ciphers.ecommerce.Buyer;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ciphers.ecommerce.Model.Deals;
import com.ciphers.ecommerce.Model.Products;
import com.ciphers.ecommerce.Model.SpecialOffer;
import com.ciphers.ecommerce.Prevalent.Prevalent;
import com.ciphers.ecommerce.R;
import com.ciphers.ecommerce.ViewHolder.DealViewHolder;
import com.ciphers.ecommerce.ViewHolder.ProductViewHolder;
import com.ciphers.ecommerce.ViewHolder.SpecialOfferViewHolder;
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
import com.shreyaspatil.firebase.recyclerpagination.DatabasePagingOptions;
import com.shreyaspatil.firebase.recyclerpagination.FirebaseRecyclerPagingAdapter;
import com.shreyaspatil.firebase.recyclerpagination.LoadingState;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SellerProductsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SellerProductsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    DatabaseReference productsRef, categoryRef, specialOfferRef, dealRef, wishListRef;

    FirebaseRecyclerAdapter<SpecialOffer, SpecialOfferViewHolder> specialOfferAdapter;
    FirebaseRecyclerAdapter<Products, ProductViewHolder> productsAdapter;
    FirebaseRecyclerAdapter<Deals, DealViewHolder> dealAdapter;


    private RecyclerView productRecyclerView, specialOffersRecyclerView, dealRecyclerView;
    RecyclerView.LayoutManager layoutManager;

    String getUsername;


    public SellerProductsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SellerProductsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SellerProductsFragment newInstance(String param1, String param2) {
        SellerProductsFragment fragment = new SellerProductsFragment();
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
        View view = inflater.inflate(R.layout.fragment_seller_products, container, false);

        wishListRef = FirebaseDatabase.getInstance().getReference().child("WishList").child(Prevalent.currentOnlineUser.getUsername());

        getUsername = getArguments().getString("usernameOfSeller");

        specialOfferRef = FirebaseDatabase.getInstance().getReference().child("Special Offers");
        productsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        dealRef = FirebaseDatabase.getInstance().getReference().child("Deals");

        specialOffersRecyclerView = view.findViewById(R.id.seller_special_offers_recycler);
        productRecyclerView = view.findViewById(R.id.seller_products_recycler);
        dealRecyclerView = view.findViewById(R.id.seller_deal_offers_recycler);


        for (int i = 0; i < 3; i++) {

            if (i == 0) {

                startProductsRecycler();

            } else if (i == 1) {

                startSpecialOfferRecycler();

            } else if (i == 2) {
                startDealRecycler();
            }

        }

        getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (getFragmentManager() != null) {

                    for (int i = 0; i < 3; i++) {

                        if (i == 0) {

                            startProductsRecycler();

                        } else if (i == 1) {

                            startSpecialOfferRecycler();

                        } else if (i == 2) {
                            startDealRecycler();
                        }

                    }

                }
            }
        });

        return view;
    }

    private void startSpecialOfferRecycler() {

        specialOffersRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        specialOffersRecyclerView.setLayoutManager(layoutManager);

        FirebaseRecyclerOptions<SpecialOffer> option = new FirebaseRecyclerOptions.Builder<SpecialOffer>()
                .setQuery(specialOfferRef.orderByChild("specialProductSellerID").equalTo(getUsername), SpecialOffer.class).build();


        specialOfferAdapter = new FirebaseRecyclerAdapter<SpecialOffer, SpecialOfferViewHolder>(option) {
            @Override
            protected void onBindViewHolder(@NonNull SpecialOfferViewHolder specialOfferViewHolder, int i, @NonNull SpecialOffer specialOffer) {
                specialOfferViewHolder.sellerSpecialOfferNameTxt.setText(specialOffer.getSpecialProductName());
                specialOfferViewHolder.sellerSpecialOfferSellerNameTxt.setText(specialOffer.getSpecialProductSellerID());
                specialOfferViewHolder.sellerSpecialOfferPriceTxt.setText(specialOffer.getSpecialProductPrice());
                Picasso.get().load(specialOffer.getSpecialProductImage1()).placeholder(R.drawable.cart_logo).into(specialOfferViewHolder.sellerSpecialOfferIV);

                specialOfferViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent productIntent = new Intent(getActivity().getApplicationContext(), BuyerSpecialOfferDetailActivity.class);
                        productIntent.putExtra("pID", specialOffer.getSpecialOfferProductId());
                        productIntent.putExtra("activityType", "Special Offer");
                        startActivity(productIntent);
                    }
                });
            }

            @NonNull
            @Override
            public SpecialOfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.special_offer_card_view_two, parent, false);
                SpecialOfferViewHolder holder = new SpecialOfferViewHolder(view);
                return holder;
            }
        };

        specialOffersRecyclerView.setAdapter(specialOfferAdapter);
        specialOfferAdapter.startListening();
    }

    private void startProductsRecycler() {

        productRecyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2);
        productRecyclerView.setLayoutManager(layoutManager);

        FirebaseRecyclerOptions<Products> option = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(productsRef.orderByChild("productSellerID").equalTo(getUsername), Products.class).build();

        productsAdapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(option) {
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
                                Toast.makeText(getActivity().getApplicationContext(), "Product Added to Wish list", Toast.LENGTH_LONG).show();
                            }
                        });

                    }

                    @Override
                    public void unLiked(LikeButton likeButton) {

                        wishListRef.child(products.getProductId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getActivity().getApplicationContext(), "Product Removed from Wish list", Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                });

                productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent productIntent = new Intent(getActivity().getApplicationContext(), ProductsDetail.class);
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

        productRecyclerView.setAdapter(productsAdapter);
        productsAdapter.startListening();

    }

    public void startDealRecycler(){
        dealRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        dealRecyclerView.setLayoutManager(layoutManager);

        FirebaseRecyclerOptions<Deals> option =
                new FirebaseRecyclerOptions.Builder<Deals>().setQuery(dealRef
                        .orderByChild("dealProductSellerID").equalTo(getUsername), Deals.class).build();

        dealAdapter = new FirebaseRecyclerAdapter<Deals, DealViewHolder>(option) {
            @Override
            protected void onBindViewHolder(@NonNull DealViewHolder dealViewHolder, int i, @NonNull Deals deal) {
                Picasso.get().load(deal.getDealProductImage1()).placeholder(R.drawable.cart_logo).into(dealViewHolder.buyerDealProductImg);

                dealViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent productIntent = new Intent(getActivity().getApplicationContext(), BuyerSpecialOfferDetailActivity.class);
                        productIntent.putExtra("pID", deal.getDealProductId());
                        productIntent.putExtra("activityType", "Deal");
                        startActivity(productIntent);
                    }
                });

            }

            @NonNull
            @Override
            public DealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.buyer_deal_card_view_home, parent, false);
                DealViewHolder holder = new DealViewHolder(view);
                return holder;
            }
        };

        dealRecyclerView.setAdapter(dealAdapter);
        dealAdapter.startListening();
    }

    @Override
    public void onResume() {
        super.onResume();

        for (int i = 0; i < 3; i++) {

            if (i == 0) {

                startProductsRecycler();

            } else if (i == 1) {

                startSpecialOfferRecycler();

            } else if (i == 2) {
                startDealRecycler();
            }

        }

    }
}