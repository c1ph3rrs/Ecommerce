package com.ciphers.ecommerce.Buyer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ciphers.ecommerce.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class SellerProfileActivity extends AppCompatActivity {

    ViewPager sellerViewPager;
    TabLayout sellerTabsLayout;
    TextView sellerUsername;
    String sellerUsernameStr;

    ImageView sellerBackIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_profile);

        sellerUsernameStr = getIntent().getStringExtra("username");

        sellerViewPager = findViewById(R.id.seller_view_pager);
        sellerTabsLayout = findViewById(R.id.seller_tabs_layout);

        sellerUsername = findViewById(R.id.seller_username_txt);

        sellerBackIcon = findViewById(R.id.seller_back_icon);

        sellerUsername.setText(sellerUsernameStr);

        sellerBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SellerProfileActivity.super.onBackPressed();
            }
        });

        ArrayList<String> sellerTabsArray = new ArrayList<>();
        sellerTabsArray.add("About");
        sellerTabsArray.add("Products");
        sellerTabsArray.add("Reviews");


        prepareViewPager(sellerViewPager, sellerTabsArray);
        sellerTabsLayout.setupWithViewPager(sellerViewPager, true);

    }

    void prepareViewPager(ViewPager cartViewPager, ArrayList<String> cartTabArray){


        SellerProfileActivity.MainAdapter cartAdapter = new SellerProfileActivity.MainAdapter(getSupportFragmentManager());
        SellerAboutFragment fragment = new SellerAboutFragment();

        for(int i=0; i< cartTabArray.size(); i++){
            Bundle bundle = new Bundle();
            fragment.setArguments(bundle);
            bundle.putString("usernameOfSeller", sellerUsernameStr);
            cartAdapter.addFragment(fragment, cartTabArray.get(i));
            fragment = new SellerAboutFragment();
        }

        cartViewPager.setAdapter(cartAdapter);
    }

    private class MainAdapter extends FragmentPagerAdapter {

        ArrayList<String> arrayList = new ArrayList<>();
        List<Fragment> fragmentList = new ArrayList<>();

        public void addFragment(Fragment fragment, String title){
            arrayList.add(title);
            fragmentList.add(fragment);

        }

        public MainAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    SellerAboutFragment sellerAboutFragment = new SellerAboutFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("usernameOfSeller", sellerUsernameStr);
                    sellerAboutFragment.setArguments(bundle);
//                    fragmentManager.beginTransaction().replace(R.id.fragmentContainer, argumentFragment).commit();

                    return sellerAboutFragment;
                case 1:
                    SellerProductsFragment sellerProductsFragment = new SellerProductsFragment();
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("usernameOfSeller", sellerUsernameStr);
                    sellerProductsFragment.setArguments(bundle2);
                    return sellerProductsFragment;
                case 2:
                    SellerReviewsFragment sellerReviewsFragment = new SellerReviewsFragment();
                    Bundle bundle3 = new Bundle();
                    bundle3.putString("usernameOfSeller", sellerUsernameStr);
                    sellerReviewsFragment.setArguments(bundle3);
                    return sellerReviewsFragment;

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return arrayList.get(position);
        }
    }

    @Override
    public void onBackPressed() {
        SellerProfileActivity.super.onBackPressed();
    }
}