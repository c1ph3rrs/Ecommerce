package com.ciphers.ecommerce.Sellers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.ciphers.ecommerce.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class SellerOrdersActivity extends AppCompatActivity {

    ViewPager orderViewPager;
    TabLayout orderTabsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_orders);

        orderViewPager = findViewById(R.id.order_view_pager);
        orderTabsLayout = findViewById(R.id.order_tabs_layout);

        ArrayList<String> orderTabsArray = new ArrayList<>();
        orderTabsArray.add("Pending");
        orderTabsArray.add("Approved");
        orderTabsArray.add("Delivered");
        orderTabsArray.add("Completed");

        prepareViewPager(orderViewPager, orderTabsArray);
        orderTabsLayout.setupWithViewPager(orderViewPager, true);

    }

    private void prepareViewPager(ViewPager orderViewPager, ArrayList<String> orderTabsArray) {

        MainAdapter orderAdapter = new MainAdapter(getSupportFragmentManager());
        SellerPendingApprovalFragment fragment = new SellerPendingApprovalFragment();

        for(int i=0; i< orderTabsArray.size(); i++){
            Bundle bundle = new Bundle();
            fragment.setArguments(bundle);
            orderAdapter.addFragment(fragment, orderTabsArray.get(i));
            fragment = new SellerPendingApprovalFragment();
        }

        orderViewPager.setAdapter(orderAdapter);

    }

    private class MainAdapter extends FragmentPagerAdapter {


        ArrayList<String> arrayList = new ArrayList<>();
        List<Fragment> fragemtList = new ArrayList<>();
        private String[] tabTitles = new String[]{"Pending", "Approved", "Delivered", "Completed"};

        public void addFragment(Fragment fragment, String title){
            arrayList.add(title);
            fragemtList.add(fragment);

        }

        public MainAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    SellerPendingApprovalFragment orderProductTab = new SellerPendingApprovalFragment();
                    return orderProductTab;
                case 1:
                    SellerApprovedOrderFragment approveOrderProductTab = new SellerApprovedOrderFragment();
                    return approveOrderProductTab;
                case 2:
                    SellerDeliveredFragment orderDeliveredTab = new SellerDeliveredFragment();
                    return orderDeliveredTab;
                case 3:
                    SellerCompletedFragment sellerOrderTab = new SellerCompletedFragment();
                    return sellerOrderTab;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return fragemtList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
    }
}