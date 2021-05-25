package com.ciphers.ecommerce.Branch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.ciphers.ecommerce.R;
import com.ciphers.ecommerce.Sellers.SellerApprovedOrderFragment;
import com.ciphers.ecommerce.Sellers.SellerCompletedFragment;
import com.ciphers.ecommerce.Sellers.SellerDeliveredFragment;
import com.ciphers.ecommerce.Sellers.SellerOrdersActivity;
import com.ciphers.ecommerce.Sellers.SellerPendingApprovalFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class BranchOrdersActivity extends AppCompatActivity {

    ViewPager branchOrderViewPager;
    TabLayout branchOrderTabsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_orders);

        branchOrderViewPager = findViewById(R.id.branch_order_view_pager);
        branchOrderTabsLayout = findViewById(R.id.branch_order_tabs_layout);

        ArrayList<String> branchOrderTabsArray = new ArrayList<>();
        branchOrderTabsArray.add("Pending");
        branchOrderTabsArray.add("Delivered");
        branchOrderTabsArray.add("Completed");

        prepareViewPager(branchOrderViewPager, branchOrderTabsArray);
        branchOrderTabsLayout.setupWithViewPager(branchOrderViewPager, true);

    }

    private void prepareViewPager(ViewPager orderViewPager, ArrayList<String> orderTabsArray) {


        BranchOrdersActivity.MainAdapter orderBranchAdapter = new BranchOrdersActivity.MainAdapter(getSupportFragmentManager());
        BranchPendingFragment fragment = new BranchPendingFragment();

        for (int i = 0; i < orderTabsArray.size(); i++) {
            Bundle bundle = new Bundle();
            fragment.setArguments(bundle);
            orderBranchAdapter.addFragment(fragment, orderTabsArray.get(i));
            fragment = new BranchPendingFragment();
        }

        orderViewPager.setAdapter(orderBranchAdapter);

    }

    private class MainAdapter extends FragmentPagerAdapter {


        ArrayList<String> arrayList = new ArrayList<>();
        List<Fragment> fragmentList = new ArrayList<>();
        private String[] tabTitles = new String[]{"Pending", "Delivered", "Completed"};

        public void addFragment(Fragment fragment, String title) {
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
                    BranchPendingFragment orderProductTab = new BranchPendingFragment();
                    return orderProductTab;

                case 1:
                    BranchDeliveredOrdersFragment deliveredOrderTab = new BranchDeliveredOrdersFragment();
                    return deliveredOrderTab;

                case 2:
                    BranchCompletedOrderFragment completedOrderTab = new BranchCompletedOrderFragment();
                    return completedOrderTab;

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return fragmentList.size();
//            return 2;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
    }

}