package com.ciphers.ecommerce.Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ciphers.ecommerce.Model.Categories;
import com.ciphers.ecommerce.Model.Deals;
import com.ciphers.ecommerce.Model.Products;
import com.ciphers.ecommerce.Model.ThirdCategory;
import com.ciphers.ecommerce.R;
import com.ciphers.ecommerce.ViewHolder.CateogryViewHolder;
import com.ciphers.ecommerce.ViewHolder.DealViewHolder;
import com.ciphers.ecommerce.ViewHolder.ThirdCategoryViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shreyaspatil.firebase.recyclerpagination.DatabasePagingOptions;
import com.shreyaspatil.firebase.recyclerpagination.FirebaseRecyclerPagingAdapter;
import com.shreyaspatil.firebase.recyclerpagination.LoadingState;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BuyerMainCategoriesActivity extends AppCompatActivity {

    String selectedCategory, selectedCategoryStr = "";
    FirebaseRecyclerAdapter<Categories, CateogryViewHolder> categoryAdapter;
    FirebaseRecyclerAdapter<ThirdCategory, ThirdCategoryViewHolder> thirdCategoryAdapter;
    RecyclerView categoryRecyclerView;
    RecyclerView.LayoutManager layoutManager;

    ArrayList<String> clickList = new ArrayList<>();


    DatabaseReference subCategoryRef, thirdCategoryRef;

    TextView electronicsDeviceTv, electronicsAccessoriesTv, tvAndHomeTv, healthTV, babyTV, petTV, lifeStyleTV,
            menFashionTV, womenFashionTV, watchTV, sportsTV, automotiveTV;

    ImageView electronicsDeviceIV, electronicsAccessoriesIV, tvAndHomeIV, healthIV, babyIV, petIV, lifeStyleIV,
            menFashionIV, womenFashionIV, watchIV, sportsIV, automotiveIV;

    LinearLayout electronicsDeviceLayout, electronicsAccessoriesLayout, tvAndHomeLayout, healthLayout, babyLayout, petLayout, lifeStyleLayout,
            menFashionLayout, womenFashionLayout, watchLayout, sportsLayout, automotiveLayout;

    ImageView categoryBackIcon;
    Integer numberIndex = 0;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_main_categories);

        subCategoryRef = FirebaseDatabase.getInstance().getReference().child("Categories");
        thirdCategoryRef = FirebaseDatabase.getInstance().getReference().child("ThirdCategory");

        selectedCategory = getIntent().getStringExtra("type");

        categoryRecyclerView = findViewById(R.id.sub_category_recycler);
        categoryBackIcon = findViewById(R.id.categories_back_icon);

        electronicsDeviceTv = findViewById(R.id.electronics_devices_txt);
        electronicsAccessoriesTv = findViewById(R.id.electronics_accessories_txt);
        tvAndHomeTv = findViewById(R.id.tv_home_txt);
        healthTV = findViewById(R.id.health_txt);
        babyTV = findViewById(R.id.toy_txt);
        petTV = findViewById(R.id.pet_txt);
        lifeStyleTV = findViewById(R.id.life_style_txt);
        menFashionTV = findViewById(R.id.men_fashion_txt);
        womenFashionTV = findViewById(R.id.women_fasion_txt);
        watchTV = findViewById(R.id.watch_txt);
        sportsTV = findViewById(R.id.sports_txt);
        automotiveTV = findViewById(R.id.automotive_txt);

        electronicsDeviceIV = findViewById(R.id.electronics_devices_iv);
        electronicsAccessoriesIV = findViewById(R.id.electronics_accessories_iv);
        tvAndHomeIV = findViewById(R.id.tv_home_iv);
        healthIV = findViewById(R.id.health_iv);
        babyIV = findViewById(R.id.toy_iv);
        petIV = findViewById(R.id.pet_iv);
        lifeStyleIV = findViewById(R.id.life_style_iv);
        menFashionIV = findViewById(R.id.men_fashion_iv);
        womenFashionIV = findViewById(R.id.women_fashion_iv);
        watchIV = findViewById(R.id.watch_bag_iv);
        sportsIV = findViewById(R.id.sports_iv);
        automotiveIV = findViewById(R.id.automotive_iv);

        electronicsDeviceLayout = findViewById(R.id.electronics_devices_layout);
        electronicsAccessoriesLayout = findViewById(R.id.electronics_accessories_layout);
        tvAndHomeLayout = findViewById(R.id.tv_home_layout);
        healthLayout = findViewById(R.id.health_layout);
        babyLayout = findViewById(R.id.toy_layout);
        petLayout = findViewById(R.id.pet_layout);
        lifeStyleLayout = findViewById(R.id.life_style_layout);
        menFashionLayout = findViewById(R.id.men_fashion_layout);
        womenFashionLayout = findViewById(R.id.women_fashion_layout);
        watchLayout = findViewById(R.id.watch_layout);
        sportsLayout = findViewById(R.id.sports_layout);
        automotiveLayout = findViewById(R.id.automotive_layout);


        if (selectedCategory.equals("more")) {
            selectedCategoryStr = "Electronics Devices";
            electronicsDeviceTv.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            startRecyclerCategory(selectedCategoryStr);
            textColorChecker();
            numberIndex = 1;
        } else if (selectedCategory.equals(electronicsDeviceTv.getText().toString())) {
            selectedCategoryStr = electronicsDeviceTv.getText().toString();
            electronicsDeviceTv.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            startRecyclerCategory(selectedCategoryStr);
            textColorChecker();
            numberIndex = 1;
        } else if (selectedCategory.equals(electronicsAccessoriesTv.getText().toString())) {
            selectedCategoryStr = electronicsAccessoriesTv.getText().toString();
            electronicsAccessoriesTv.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            startRecyclerCategory(selectedCategoryStr);
            textColorChecker();
            numberIndex = 2;
        } else if (selectedCategory.equals(tvAndHomeTv.getText().toString())) {
            selectedCategoryStr = tvAndHomeTv.getText().toString();
            tvAndHomeTv.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            startRecyclerCategory(selectedCategoryStr);
            textColorChecker();
            numberIndex = 3;
        } else if (selectedCategory.equals(healthTV.getText().toString())) {
            selectedCategoryStr = healthTV.getText().toString();
            healthTV.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            startRecyclerCategory(selectedCategoryStr);
            textColorChecker();
            numberIndex = 4;
        } else if (selectedCategory.equals(babyTV.getText().toString())) {
            selectedCategoryStr = babyTV.getText().toString();
            babyTV.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            startRecyclerCategory(selectedCategoryStr);
            textColorChecker();
            numberIndex = 5;
        } else if (selectedCategory.equals(petTV.getText().toString())) {
            selectedCategoryStr = petTV.getText().toString();
            petTV.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            startRecyclerCategory(selectedCategoryStr);
            textColorChecker();
            numberIndex = 6;
        } else if (selectedCategory.equals(lifeStyleTV.getText().toString())) {
            selectedCategoryStr = lifeStyleTV.getText().toString();
            lifeStyleTV.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            startRecyclerCategory(selectedCategoryStr);
            textColorChecker();
            numberIndex = 7;
        } else if (selectedCategory.equals(menFashionTV.getText().toString())) {
            selectedCategoryStr = menFashionTV.getText().toString();
            menFashionTV.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            startRecyclerCategory(selectedCategoryStr);
            textColorChecker();
            numberIndex = 8;
        } else if (selectedCategory.equals(womenFashionTV.getText().toString())) {
            selectedCategoryStr = womenFashionTV.getText().toString();
            womenFashionTV.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            startRecyclerCategory(selectedCategoryStr);
            textColorChecker();
            numberIndex = 9;
        } else if (selectedCategory.equals(watchTV.getText().toString())) {
            selectedCategoryStr = watchTV.getText().toString();
            watchTV.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            startRecyclerCategory(selectedCategoryStr);
            textColorChecker();
            numberIndex = 10;
        } else if (selectedCategory.equals(sportsTV.getText().toString())) {
            selectedCategoryStr = sportsTV.getText().toString();
            sportsTV.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            startRecyclerCategory(selectedCategoryStr);
            textColorChecker();
            numberIndex = 11;
        } else if (selectedCategory.equals(automotiveTV.getText().toString())) {
            selectedCategoryStr = automotiveTV.getText().toString();
            automotiveTV.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            startRecyclerCategory(selectedCategoryStr);
            textColorChecker();
            numberIndex = 12;
        }


        electronicsDeviceLayout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                textColorChecker();
                selectedCategoryStr = electronicsDeviceTv.getText().toString();
                electronicsDeviceTv.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                startRecyclerCategory(selectedCategoryStr);
                numberIndex = 1;
            }
        });

        electronicsAccessoriesLayout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                textColorChecker();
                selectedCategoryStr = electronicsAccessoriesTv.getText().toString();
                electronicsAccessoriesTv.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                startRecyclerCategory(selectedCategoryStr);
                numberIndex = 2;
            }
        });

        tvAndHomeLayout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                textColorChecker();
                selectedCategoryStr = tvAndHomeTv.getText().toString();
                tvAndHomeTv.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                startRecyclerCategory(selectedCategoryStr);
                numberIndex = 3;
            }
        });

        healthLayout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                textColorChecker();
                selectedCategoryStr = healthTV.getText().toString();
                healthTV.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                startRecyclerCategory(selectedCategoryStr);
                numberIndex = 4;
            }
        });

        babyLayout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                textColorChecker();
                selectedCategoryStr = babyTV.getText().toString();
                babyTV.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                startRecyclerCategory(selectedCategoryStr);
                numberIndex = 5;
            }
        });

        petLayout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                textColorChecker();
                selectedCategoryStr = petTV.getText().toString();
                petTV.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                startRecyclerCategory(selectedCategoryStr);
                numberIndex = 6;
            }
        });

        lifeStyleLayout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                textColorChecker();
                selectedCategoryStr = lifeStyleTV.getText().toString();
                lifeStyleTV.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                startRecyclerCategory(selectedCategoryStr);
                numberIndex = 7;
            }
        });

        menFashionLayout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                textColorChecker();
                selectedCategoryStr = menFashionTV.getText().toString();
                menFashionTV.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                startRecyclerCategory(selectedCategoryStr);
                numberIndex = 8;
            }
        });

        womenFashionLayout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                textColorChecker();
                selectedCategoryStr = womenFashionTV.getText().toString();
                womenFashionTV.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                startRecyclerCategory(selectedCategoryStr);
                numberIndex = 9;
            }
        });

        watchLayout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                textColorChecker();
                selectedCategoryStr = watchTV.getText().toString();
                watchTV.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                startRecyclerCategory(selectedCategoryStr);
                numberIndex = 10;
            }
        });

        sportsLayout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                textColorChecker();
                selectedCategoryStr = sportsTV.getText().toString();
                sportsTV.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                startRecyclerCategory(selectedCategoryStr);
                numberIndex = 11;
            }
        });

        automotiveLayout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                textColorChecker();
                selectedCategoryStr = automotiveTV.getText().toString();
                automotiveTV.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                startRecyclerCategory(selectedCategoryStr);
                numberIndex = 12;
            }
        });

        categoryBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuyerMainCategoriesActivity.super.onBackPressed();
            }
        });

//        electronicsDeviceTv.setTextColor(R.color.orange);

    }

    public void startRecyclerCategory(String selectedCategoryString) {


        categoryRecyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getApplicationContext(), 1);
        categoryRecyclerView.setLayoutManager(layoutManager);

        //.child("parentCategory").equalTo(selectedCategoryString)

        FirebaseRecyclerOptions<Categories> options = new FirebaseRecyclerOptions.Builder<Categories>()
                .setQuery(subCategoryRef.orderByChild("parentCategory").equalTo(selectedCategoryString), Categories.class).build();


        categoryAdapter = new FirebaseRecyclerAdapter<Categories, CateogryViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CateogryViewHolder cateogryViewHolder, int i, @NonNull Categories categories) {

                cateogryViewHolder.categoryNameTV.setText(categories.getProductCategory());

                clickList.add("0");

                cateogryViewHolder.categoryDDIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (clickList.get(cateogryViewHolder.getAdapterPosition()).equals("0")) {

                            cateogryViewHolder.thirdCategoryLayout.setVisibility(View.VISIBLE);
                            cateogryViewHolder.categoryDDIV.setImageResource(R.drawable.drop_up_arrow);

                            startThirdCategoryRecycler(cateogryViewHolder.thirdCategoryRecyclerView, categories.getProductCategory());

                            clickList.set(cateogryViewHolder.getAdapterPosition(), "1");

                        } else {

                            cateogryViewHolder.thirdCategoryLayout.setVisibility(View.GONE);
                            cateogryViewHolder.categoryDDIV.setImageResource(R.drawable.drop_arrow);
                            clickList.set(cateogryViewHolder.getAdapterPosition(), "0");

                        }

                    }
                });


            }

            @NonNull
            @Override
            public CateogryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_card_view, parent, false);
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.buyer_sub_category_card_view, parent, false);
                CateogryViewHolder holder = new CateogryViewHolder(view);
                return holder;
            }
        };

        categoryRecyclerView.setAdapter(categoryAdapter);
        categoryAdapter.startListening();
    }

    private void startThirdCategoryRecycler(RecyclerView thirdCatRecycler, String categoryType) {

        thirdCatRecycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        thirdCatRecycler.setLayoutManager(layoutManager);

        //.child("parentCategory").equalTo(selectedCategoryString)

        FirebaseRecyclerOptions<ThirdCategory> options = new FirebaseRecyclerOptions.Builder<ThirdCategory>()
                .setQuery(thirdCategoryRef.orderByChild("parentCategory").equalTo(categoryType), ThirdCategory.class).build();


        thirdCategoryAdapter = new FirebaseRecyclerAdapter<ThirdCategory, ThirdCategoryViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ThirdCategoryViewHolder thirdCategoryViewHolder, int i, @NonNull ThirdCategory thirdCategory) {

                thirdCategoryViewHolder.categoryNameTxtOne.setText(thirdCategory.getProductCategory());
                Picasso.get().load(thirdCategory.getProductImage()).into(thirdCategoryViewHolder.categoryIV);

                thirdCategoryViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent categoryDetailIntent = new Intent(getApplicationContext(), BuyerCategoriesDetailsActivity.class);
                        categoryDetailIntent.putExtra("category", thirdCategory.getProductCategory());
                        startActivity(categoryDetailIntent);

                    }
                });

            }

            @NonNull
            @Override
            public ThirdCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_card_view, parent, false);
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_card_view, parent, false);
                ThirdCategoryViewHolder holder = new ThirdCategoryViewHolder(view);
                return holder;
            }
        };

        thirdCatRecycler.setAdapter(thirdCategoryAdapter);
        thirdCategoryAdapter.startListening();


    }

    @SuppressLint("ResourceAsColor")
    private void textColorChecker() {

        if (numberIndex.equals(1)) {
            electronicsDeviceTv.setTextColor(getResources().getColor(R.color.black));
        } else if (numberIndex.equals(2)) {
            electronicsAccessoriesTv.setTextColor(getResources().getColor(R.color.black));
        } else if (numberIndex.equals(3)) {
            tvAndHomeTv.setTextColor(getResources().getColor(R.color.black));
        } else if (numberIndex.equals(4)) {
            healthTV.setTextColor(getResources().getColor(R.color.black));
        } else if (numberIndex.equals(5)) {
            babyTV.setTextColor(getResources().getColor(R.color.black));
        } else if (numberIndex.equals(6)) {
            petTV.setTextColor(getResources().getColor(R.color.black));
        } else if (numberIndex.equals(7)) {
            lifeStyleTV.setTextColor(getResources().getColor(R.color.black));
        } else if (numberIndex.equals(8)) {
            menFashionTV.setTextColor(getResources().getColor(R.color.black));
        } else if (numberIndex.equals(9)) {
            womenFashionTV.setTextColor(getResources().getColor(R.color.black));
        } else if (numberIndex.equals(10)) {
            watchTV.setTextColor(getResources().getColor(R.color.black));
        } else if (numberIndex.equals(11)) {
            sportsTV.setTextColor(getResources().getColor(R.color.black));
        } else if (numberIndex.equals(12)) {
            automotiveTV.setTextColor(getResources().getColor(R.color.black));
        }

    }

}
