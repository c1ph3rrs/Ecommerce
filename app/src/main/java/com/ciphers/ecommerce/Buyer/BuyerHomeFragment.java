                                            package com.ciphers.ecommerce.Buyer;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ciphers.ecommerce.Model.Categories;
import com.ciphers.ecommerce.Model.Products;
import com.ciphers.ecommerce.Prevalent.Prevalent;
import com.ciphers.ecommerce.R;
import com.ciphers.ecommerce.ViewHolder.CateogryViewHolder;
import com.ciphers.ecommerce.ViewHolder.ProductViewHolder;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BuyerHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BuyerHomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ImageSlider imageSlider;
    List<SlideModel> slideModels = new ArrayList<>();

    FirebaseRecyclerAdapter<Categories, CateogryViewHolder> categoryAdapter;
    FirebaseRecyclerAdapter<Products, ProductViewHolder> productAdapter;
    RecyclerView.LayoutManager layoutManager;
    DatabaseReference productsRef, categoryRef, specialOfferRef, dealRef;
    DatabaseReference wishListRef;

    RecyclerView buyerCategoryRecycler, buyerProductsRecycler, popularProductsRecycler;

    TextView viewAllCategoriesTV;

    TextView electronicsDeviceTv, electronicsAccessoriesTv, tvAndHomeTv, healthTV, babyTV, petTV, lifeStyleTV,
            menFashionTV, womenFashionTV, watchTV, sportsTV, automotiveTV;

    LinearLayout electronicsDeviceLayout, electronicsAccessoriesLayout, tvAndHomeLayout, healthLayout, babyLayout, petLayout, lifeStyleLayout,
            menFashionLayout, womenFashionLayout, watchLayout, sportsLayout, automotiveLayout;

    public BuyerHomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BuyerHomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BuyerHomeFragment newInstance(String param1, String param2) {
        BuyerHomeFragment fragment = new BuyerHomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_buyer_home, container, false);

        imageSlider = view.findViewById(R.id.image_slider);
//        buyerCategoryRecycler = view.findViewById(R.id.buyer_category_recycler);

        categoryRef = FirebaseDatabase.getInstance().getReference().child("Categories");
        productsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        wishListRef = FirebaseDatabase.getInstance().getReference().child("WishList").child(Prevalent.currentOnlineUser.getUsername());

        slideModels.add(new SlideModel(R.drawable.banner1, "A complete warm Sweater", ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.banner2, "T Shirt with complete package", ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.banner3, "Sports Shirt with complete kit", ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.banner4, "Ladies Dress very beautiful Design", ScaleTypes.CENTER_CROP));
        imageSlider.setImageList(slideModels);

        buyerProductsRecycler = view.findViewById(R.id.buyer_products_recycler);
        viewAllCategoriesTV = view.findViewById(R.id.category_see_all);

        electronicsDeviceTv = view.findViewById(R.id.electronics_devices_tv);
        electronicsAccessoriesTv = view.findViewById(R.id.electronics_accessories_tv);
        tvAndHomeTv = view.findViewById(R.id.tv_home_tv);
        healthTV = view.findViewById(R.id.health_tv);
        babyTV = view.findViewById(R.id.toy_tv);
        petTV = view.findViewById(R.id.pet_tv);
        lifeStyleTV = view.findViewById(R.id.life_style_tv);
        menFashionTV = view.findViewById(R.id.men_fashion_tv);
        womenFashionTV = view.findViewById(R.id.women_fashion_tv);
        watchTV = view.findViewById(R.id.watch_tv);
        sportsTV = view.findViewById(R.id.sports_tv);
        automotiveTV = view.findViewById(R.id.automotive_tv);

        electronicsDeviceLayout = view.findViewById(R.id.electronics_devices_layout);
        electronicsAccessoriesLayout = view.findViewById(R.id.electronics_accessories_layout);
        tvAndHomeLayout = view.findViewById(R.id.tv_home_layout);
        healthLayout = view.findViewById(R.id.health_layout);
        babyLayout = view.findViewById(R.id.toy_layout);
        petLayout = view.findViewById(R.id.pet_layout);
        lifeStyleLayout = view.findViewById(R.id.life_style_layout);
        menFashionLayout = view.findViewById(R.id.men_fashion_layout);
        womenFashionLayout = view.findViewById(R.id.women_fashion_layout);
        watchLayout = view.findViewById(R.id.watch_layout);
        sportsLayout = view.findViewById(R.id.sports_layout);
        automotiveLayout = view.findViewById(R.id.automotive_layout);


        startBuyerProductRecycler();

        viewAllCategoriesTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent seeMoreCategoryIntent = new Intent(getActivity().getApplicationContext(), BuyerMainCategoriesActivity.class);
                seeMoreCategoryIntent.putExtra("type", "more");
                startActivity(seeMoreCategoryIntent);

            }
        });

        electronicsDeviceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                categoryRecyclerView.setAdapter(null);
                Intent seeMoreCategoryIntent = new Intent(getActivity().getApplicationContext(), BuyerMainCategoriesActivity.class);
                seeMoreCategoryIntent.putExtra("type", electronicsDeviceTv.getText().toString());
                startActivity(seeMoreCategoryIntent);
            }
        });

        electronicsAccessoriesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent seeMoreCategoryIntent = new Intent(getActivity().getApplicationContext(), BuyerMainCategoriesActivity.class);
                seeMoreCategoryIntent.putExtra("type", electronicsAccessoriesTv.getText().toString());
                startActivity(seeMoreCategoryIntent);
            }
        });

        tvAndHomeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent seeMoreCategoryIntent = new Intent(getActivity().getApplicationContext(), BuyerMainCategoriesActivity.class);
                seeMoreCategoryIntent.putExtra("type", tvAndHomeTv.getText().toString());
                startActivity(seeMoreCategoryIntent);
            }
        });

        healthLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent seeMoreCategoryIntent = new Intent(getActivity().getApplicationContext(), BuyerMainCategoriesActivity.class);
                seeMoreCategoryIntent.putExtra("type", healthTV.getText().toString());
                startActivity(seeMoreCategoryIntent);
            }
        });

        babyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent seeMoreCategoryIntent = new Intent(getActivity().getApplicationContext(), BuyerMainCategoriesActivity.class);
                seeMoreCategoryIntent.putExtra("type", babyTV.getText().toString());
                startActivity(seeMoreCategoryIntent);
            }
        });

        petLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent seeMoreCategoryIntent = new Intent(getActivity().getApplicationContext(), BuyerMainCategoriesActivity.class);
                seeMoreCategoryIntent.putExtra("type", petTV.getText().toString());
                startActivity(seeMoreCategoryIntent);
            }
        });

        lifeStyleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent seeMoreCategoryIntent = new Intent(getActivity().getApplicationContext(), BuyerMainCategoriesActivity.class);
                seeMoreCategoryIntent.putExtra("type", lifeStyleTV.getText().toString());
                startActivity(seeMoreCategoryIntent);
            }
        });

        menFashionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent seeMoreCategoryIntent = new Intent(getActivity().getApplicationContext(), BuyerMainCategoriesActivity.class);
                seeMoreCategoryIntent.putExtra("type", menFashionTV.getText().toString());
                startActivity(seeMoreCategoryIntent);
            }
        });

        womenFashionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent seeMoreCategoryIntent = new Intent(getActivity().getApplicationContext(), BuyerMainCategoriesActivity.class);
                seeMoreCategoryIntent.putExtra("type", womenFashionTV.getText().toString());
                startActivity(seeMoreCategoryIntent);
            }
        });

        watchLayout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                Intent seeMoreCategoryIntent = new Intent(getActivity().getApplicationContext(), BuyerMainCategoriesActivity.class);
                seeMoreCategoryIntent.putExtra("type", watchTV.getText().toString());
                startActivity(seeMoreCategoryIntent);
            }
        });

        sportsLayout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                Intent seeMoreCategoryIntent = new Intent(getActivity().getApplicationContext(), BuyerMainCategoriesActivity.class);
                seeMoreCategoryIntent.putExtra("type", sportsTV.getText().toString());
                startActivity(seeMoreCategoryIntent);
            }
        });

        automotiveLayout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                Intent seeMoreCategoryIntent = new Intent(getActivity().getApplicationContext(), BuyerMainCategoriesActivity.class);
                seeMoreCategoryIntent.putExtra("type", automotiveTV.getText().toString());
                startActivity(seeMoreCategoryIntent);
            }
        });

        return view;
    }


    private void startBuyerProductRecycler() {

        buyerProductsRecycler.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2);
        buyerProductsRecycler.setLayoutManager(layoutManager);

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(productsRef, Products.class).build();

        productAdapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
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

        buyerProductsRecycler.setAdapter(productAdapter);
        productAdapter.startListening();

    }

    private void startBuyerCategoriesAdapter() {

//        buyerCategoryRecycler.setHasFixedSize(true);
//        layoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2);
//        buyerCategoryRecycler.setLayoutManager(layoutManager);


        FirebaseRecyclerOptions<Categories> options = new FirebaseRecyclerOptions.Builder<Categories>()
                .setQuery(categoryRef, Categories.class)
                .build();

        categoryAdapter = new FirebaseRecyclerAdapter<Categories, CateogryViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CateogryViewHolder cateogryViewHolder, int i, @NonNull Categories categories) {

                if (i >= 3) {
//                    cateogryViewHolder.categoryNameTxtOne.setText(categories.getProductCategory());
//                    Picasso.get().load(categories.getProductImage()).placeholder(R.drawable.cart_logo).into(cateogryViewHolder.categoryImageTVOne);
                } else {
                }
            }

            @NonNull
            @Override
            public CateogryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_card_view, parent, false);
                CateogryViewHolder holder = new CateogryViewHolder(view);
                return holder;
            }

        };

        buyerCategoryRecycler.setAdapter(categoryAdapter);
        categoryAdapter.startListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        startBuyerProductRecycler();
    }
}