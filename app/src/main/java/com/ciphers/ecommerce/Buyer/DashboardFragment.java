package com.ciphers.ecommerce.Buyer;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ciphers.ecommerce.Model.Categories;
import com.ciphers.ecommerce.Model.Deals;
import com.ciphers.ecommerce.Model.Products;
import com.ciphers.ecommerce.Model.SpecialOffer;
import com.ciphers.ecommerce.R;
import com.ciphers.ecommerce.ViewHolder.CateogryViewHolder;
import com.ciphers.ecommerce.ViewHolder.DealViewHolder;
import com.ciphers.ecommerce.ViewHolder.ProductViewHolder;
import com.ciphers.ecommerce.ViewHolder.SpecialOfferViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shreyaspatil.firebase.recyclerpagination.DatabasePagingOptions;
import com.shreyaspatil.firebase.recyclerpagination.FirebaseRecyclerPagingAdapter;
import com.shreyaspatil.firebase.recyclerpagination.LoadingState;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashboardFragment extends Fragment{


//    NavigationView navigationView;
//    DrawerLayout drawerLayout;
    ImageView menuIcon, searchIcon, specialOfferCaretDropImg, dealCaretDropImg, categoryCaretDropImg;
    View headerView;

    TextView fullNameTxtView, userNameTxt;
    String fullName, userName, email, password, phoneNo;


    private RecyclerView productRecyclerView, categoryRecyclerView, specialOffersRecyclerView, dealRecyclerView;
    RecyclerView.LayoutManager layoutManager;

    DatabaseReference productsRef, categoryRef, specialOfferRef, dealRef;

    FirebaseRecyclerPagingAdapter<Products, ProductViewHolder> productsAdapter;
    FirebaseRecyclerPagingAdapter<Categories, CateogryViewHolder> categoryAdapter;
    FirebaseRecyclerPagingAdapter<SpecialOffer, SpecialOfferViewHolder> specialOfferAdapter;
    FirebaseRecyclerAdapter<Deals, DealViewHolder> dealAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    CircleImageView profileImage;
    int categoryLimit = 1;
    int specialOfferDropDownChecker = 1, dealDropDownChecker =1, categoryDropDownChecker = 1;
    private final int specialOfferlimit = 10;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        productsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        categoryRef = FirebaseDatabase.getInstance().getReference().child("Categories");
        specialOfferRef = FirebaseDatabase.getInstance().getReference().child("Special Offers");
        dealRef = FirebaseDatabase.getInstance().getReference().child("Deals");

//        drawerLayout = view.findViewById(R.id.drawer_layout);
//        navigationView = view.findViewById(R.id.navigation_view);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
//        headerView = navigationView.getHeaderView(0);
//        menuIcon = view.findViewById(R.id.menu_icon);
        searchIcon = view.findViewById(R.id.search_icon);

//        navigationDrawer();

//        fullNameTxtView = headerView.findViewById(R.id.profile_full_name_navigation);
//        userNameTxt = headerView.findViewById(R.id.profile_user_name_navigation);
//        profileImage = headerView.findViewById(R.id.navigation_user_profile_image);

//        fullNameTxtView.setText(Prevalent.currentOnlineUser.getFullName());
//        userNameTxt.setText("@" + Prevalent.currentOnlineUser.getUsername());
//        if (Prevalent.currentOnlineUser.getImage().equals("")) {
//
//        } else {
//            Picasso.get().load(Prevalent.currentOnlineUser.getImage()).placeholder(R.drawable.profile).into(profileImage);
//        }

        productRecyclerView = view.findViewById(R.id.item_recycler);
        categoryRecyclerView = view.findViewById(R.id.product_category_recycler);
//        specialOffersRecyclerView = view.findViewById(R.id.product_special_offers_recycler);
//        dealRecyclerView = view.findViewById(R.id.product_deal_recycler);
//        specialOfferCaretDropImg = view.findViewById(R.id.special_offer_drop_caret_img);
//        dealCaretDropImg = view.findViewById(R.id.deal_drop_caret_img);
        categoryCaretDropImg = view.findViewById(R.id.category_drop_caret_img);

//        startSpecialOfferRecycler();
        startRecyclerCategory();
//        startDealRecycler();
        startProductRecyclerView();
//        startRecyclerProduct();


//        specialOfferCaretDropImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (specialOfferDropDownChecker == 1) {
//                    specialOfferCaretDropImg.setImageResource(R.drawable.drop_up_arrow);
//                    specialOffersRecyclerView.setVisibility(View.GONE);
//                    specialOfferDropDownChecker = 0;
//                } else {
//                    specialOfferCaretDropImg.setImageResource(R.drawable.drop_arrow);
//                    specialOffersRecyclerView.setVisibility(View.VISIBLE);
//                    specialOfferDropDownChecker = 1;
//                }
//            }
//        });

//        dealCaretDropImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (dealDropDownChecker == 1) {
//                    dealCaretDropImg.setImageResource(R.drawable.drop_up_arrow);
//                    dealRecyclerView.setVisibility(View.GONE);
//                    dealDropDownChecker = 0;
//                } else {
//                    dealCaretDropImg.setImageResource(R.drawable.drop_arrow);
//                    dealRecyclerView.setVisibility(View.VISIBLE);
//                    dealDropDownChecker = 1;
//                }
//            }
//        });

        categoryCaretDropImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (categoryDropDownChecker == 1) {
                    categoryCaretDropImg.setImageResource(R.drawable.drop_up_arrow);
                    categoryRecyclerView.setVisibility(View.GONE);
                    categoryDropDownChecker = 0;
                } else {
                    categoryCaretDropImg.setImageResource(R.drawable.drop_arrow);
                    categoryRecyclerView.setVisibility(View.VISIBLE);
                    categoryDropDownChecker = 1;
                }
            }
        });

//

        return view;

    }


//    private void navigationDrawer() {
//
//        navigationView.bringToFront();
//        navigationView.setNavigationItemSelectedListener(this);
//        navigationView.setCheckedItem(R.id.nav_home);
//
//        menuIcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
//                    drawerLayout.closeDrawer(GravityCompat.START);
//                } else {
//                    drawerLayout.openDrawer(GravityCompat.START);
//                }
//            }
//        });
//    }


//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//
//        int id = item.getItemId();
//
//        if (id == R.id.nav_home) {
//
//            if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
//                drawerLayout.closeDrawer(GravityCompat.START);
//
//            } else {
//                drawerLayout.openDrawer(GravityCompat.START);
//            }
//
//        } else if (id == R.id.nav_cart) {
////            startActivity(new Intent(getApplicationContext(), CartActivity.class));
//
//        } else if (id == R.id.nav_orders) {
//
//        } else if (id == R.id.nav_settings) {
////            startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
//
//        } else if (id == R.id.nav_logout) {
//            Paper.book().destroy();
////            Intent logoutIntent = new Intent(getApplicationContext(), SplashScreen.class);
////            logoutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
////            finish();
////            startActivity(logoutIntent);
//        }
//        return true;
//    }

//    public void startRecyclerProduct() {
//
//        productRecyclerView.setHasFixedSize(true);
//        layoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 3);
//        productRecyclerView.setLayoutManager(layoutManager);
//
//        FirebaseRecyclerOptions<Products> options =
//                new FirebaseRecyclerOptions.Builder<Products>().setQuery(productsRef, Products.class).build();
//
//
//        adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
//            @Override
//            protected void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i, @NonNull final Products model) {
//
////                productViewHolder.itemNameTV.setText(model.getProductName());
////                productViewHolder.itemDescriptionTV.setText(model.getProductDescription());
////                productViewHolder.itemSellerNameTV.setText(model.getProductSellerID());
//////                productViewHolder.itemPriceTV.setText(model.getProductPrice() + "$");
////                Picasso.get().load(model.getProductImage1()).placeholder(R.drawable.cart_logo).into(productViewHolder.itemImageView);
//
////                Glide.with(getApplicationContext()).load(model.getProductImage()).into(productViewHolder.itemImageView);
//
//
//                productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent productIntent = new Intent(getActivity().getApplicationContext(), ProductsDetail.class);
//                        productIntent.putExtra("pID", model.getProductId());
//                        startActivity(productIntent);
//                    }
//                });
//            }
//
//            @NonNull
//            @Override
//            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.buyer_product_items_card_design, parent, false);
//                ProductViewHolder holder = new ProductViewHolder(view);
//                return holder;
//            }
//        };
//
//        productRecyclerView.setAdapter(adapter);
//        adapter.startListening();
//    }

    public void startRecyclerCategory() {

        categoryRecyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 5);
        categoryRecyclerView.setLayoutManager(layoutManager);

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(5)
                .setPageSize(10)
                .build();


        DatabasePagingOptions<Categories> options = new DatabasePagingOptions.Builder<Categories>()
                .setLifecycleOwner(this)
                .setQuery(categoryRef, config, Categories.class)
                .build();



        categoryAdapter = new FirebaseRecyclerPagingAdapter<Categories, CateogryViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CateogryViewHolder cateogryViewHolder, int i, @NonNull Categories categories) {
//                cateogryViewHolder.categoryNameTxtOne.setText(categories.getProductCategory());
//                Picasso.get().load(categories.getProductImage()).placeholder(R.drawable.cart_logo).into(cateogryViewHolder.categoryImageTVOne);
            }

            @Override
            protected void onLoadingStateChanged(@NonNull LoadingState state) {
                switch (state) {
                    case LOADING_INITIAL:
                    case LOADING_MORE:
                        // Do your loading animation
                        mSwipeRefreshLayout.setRefreshing(true);
                        break;

                    case LOADED:
                        // Stop Animation
                        mSwipeRefreshLayout.setRefreshing(false);
                        break;

                    case FINISHED:
                        //Reached end of Data set
                        mSwipeRefreshLayout.setRefreshing(false);
                        break;

                    case ERROR:
                        retry();
                        break;
                }
            }

            @NonNull
            @Override
            public CateogryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_card_view, parent, false);
                CateogryViewHolder holder = new CateogryViewHolder(view);
                return holder;
            }

            @Override
            public int getItemCount() {
                return super.getItemCount();
            }
        };

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                categoryAdapter.refresh();
            }
        });


        categoryRecyclerView.setAdapter(categoryAdapter);
        categoryAdapter.startListening();
    }

    private void startSpecialOfferRecycler() {

        specialOffersRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        specialOffersRecyclerView.setLayoutManager(layoutManager);

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(5)
                .setPageSize(10)
                .build();


        DatabasePagingOptions<SpecialOffer> options = new DatabasePagingOptions.Builder<SpecialOffer>()
                .setLifecycleOwner(this)
                .setQuery(specialOfferRef, config, SpecialOffer.class)
                .build();


        specialOfferAdapter = new FirebaseRecyclerPagingAdapter<SpecialOffer, SpecialOfferViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull SpecialOfferViewHolder specialOfferViewHolder, int i, @NonNull SpecialOffer specialOffer) {
//                specialOfferViewHolder.buyerSpecialProductName.setText(specialOffer.getSpecialProductName());
//                specialOfferViewHolder.buyerSpecialProductSeller.setText(specialOffer.getSpecialProductSellerID());
//                specialOfferViewHolder.buyerSpecialProductPrice.setText(specialOffer.getSpecialProductPrice() + " Rs");
//                Picasso.get().load(specialOffer.getSpecialProductImage1()).placeholder(R.drawable.cart_logo).into(specialOfferViewHolder.buyerSpecialProductImage);

                specialOfferViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent productIntent = new Intent(getActivity().getApplicationContext(), BuyerSpecialOfferDetailActivity.class);
                        productIntent.putExtra("pID", specialOffer.getSpecialOfferProductId());
                        startActivity(productIntent);
                    }
                });

            }

            @Override
            protected void onLoadingStateChanged(@NonNull LoadingState state) {
                switch (state) {
                    case LOADING_INITIAL:
                    case LOADING_MORE:
                        // Do your loading animation
                        mSwipeRefreshLayout.setRefreshing(true);
                        break;

                    case LOADED:
                        // Stop Animation
                        mSwipeRefreshLayout.setRefreshing(false);
                        break;

                    case FINISHED:
                        //Reached end of Data set
                        mSwipeRefreshLayout.setRefreshing(false);
                        break;

                    case ERROR:
                        retry();
                        break;
                }
            }


            @NonNull
            @Override
            public SpecialOfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.buyer_special_offer_cardview, parent, false);
                SpecialOfferViewHolder holder = new SpecialOfferViewHolder(view);
                return holder;
            }


        };

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                specialOfferAdapter.refresh();
            }
        });

        specialOffersRecyclerView.setAdapter(specialOfferAdapter);
        specialOfferAdapter.startListening();
    }

    public void startDealRecycler(){
        dealRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        dealRecyclerView.setLayoutManager(layoutManager);

        FirebaseRecyclerOptions<Deals> option =
                new FirebaseRecyclerOptions.Builder<Deals>().setQuery(dealRef, Deals.class).build();

        dealAdapter = new FirebaseRecyclerAdapter<Deals, DealViewHolder>(option) {
            @Override
            protected void onBindViewHolder(@NonNull DealViewHolder dealViewHolder, int i, @NonNull Deals deal) {
                Picasso.get().load(deal.getDealProductImage1()).placeholder(R.drawable.cart_logo).into(dealViewHolder.buyerDealProductImg);

//                dealViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent productIntent = new Intent(getActivity().getApplicationContext(), BuyerSpecialOfferDetailActivity.class);
//                        productIntent.putExtra("pID", deal.getDealProductId());
//                        startActivity(productIntent);
//                    }
//                });

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


    void startProductRecyclerView(){

        productRecyclerView.setNestedScrollingEnabled(false);
        productRecyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2);
        productRecyclerView.setLayoutManager(layoutManager);

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(5)
                .setPageSize(10)
                .build();


        DatabasePagingOptions<Products> options = new DatabasePagingOptions.Builder<Products>()
                .setLifecycleOwner(this)
                .setQuery(productsRef, config, Products.class)
                .build();

        productsAdapter = new FirebaseRecyclerPagingAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i, @NonNull Products products) {
                productViewHolder.itemNameTV.setText(products.getProductName());
//                productViewHolder.itemDescriptionTV.setText(products.getProductDescription());
                productViewHolder.itemSellerNameTV.setText(products.getProductSellerID());
//                productViewHolder.itemPriceTV.setText(model.getProductPrice() + "$");
                Picasso.get().load(products.getProductImage1()).placeholder(R.drawable.cart_logo).into(productViewHolder.itemImageView);

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

            @Override
            protected void onLoadingStateChanged(@NonNull LoadingState state) {
                switch (state) {
                    case LOADING_INITIAL:
                    case LOADING_MORE:
                        // Do your loading animation
                        mSwipeRefreshLayout.setRefreshing(true);
                        break;

                    case LOADED:
                        // Stop Animation
                        mSwipeRefreshLayout.setRefreshing(false);
                        break;

                    case FINISHED:
                        //Reached end of Data set
                        mSwipeRefreshLayout.setRefreshing(false);
                        break;

                    case ERROR:
                        retry();
                        break;
                }
            }

            @Override
            protected void onError(@NonNull DatabaseError databaseError) {
                super.onError(databaseError);
                mSwipeRefreshLayout.setRefreshing(false);
                databaseError.toException().printStackTrace();
            }
        };



//        Set listener to SwipeRefreshLayout for refresh action
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                productsAdapter.refresh();
            }
        });

        productRecyclerView.setAdapter(productsAdapter);
        productsAdapter.startListening();

    }

//    @Override
//    public void onBackPressed() {
//        if (drawerLayout.isDrawerVisible(GravityCompat.START))
//            drawerLayout.closeDrawer(GravityCompat.START);
//        else
////            super.onBackPressed();
//    }


//    New Code




}