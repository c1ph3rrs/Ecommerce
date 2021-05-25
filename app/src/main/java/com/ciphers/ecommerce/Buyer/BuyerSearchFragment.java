package com.ciphers.ecommerce.Buyer;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

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
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class BuyerSearchFragment extends Fragment {

    ViewPager searchViewPager;
    TabLayout searchTabsLayout;
    ChipNavigationBar buyerNavBar;

    String searchText;
    int positionFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_buyer_search, container, false);

        buyerNavBar = view.findViewById(R.id.buyer_bottom_nav_bar);

        searchViewPager = view.findViewById(R.id.search_view_pager);
        searchTabsLayout = view.findViewById(R.id.search_tabs_layout);

        ArrayList<String> searchTabsArray = new ArrayList<>();
        searchTabsArray.add("Products");
        searchTabsArray.add("Category");
        searchTabsArray.add("Seller");

        prepareViewPager(searchViewPager, searchTabsArray);
        searchTabsLayout.setupWithViewPager(searchViewPager, true);

        searchViewPager.getCurrentItem();

        return view;
    }


    void prepareViewPager(ViewPager cartViewPager, ArrayList<String> searchTabsArray) {


        BuyerSearchFragment.MainAdapter cartAdapter = new MainAdapter(getActivity().getSupportFragmentManager());
        SearchProductFragment fragment = new SearchProductFragment();

        for (int i = 0; i < searchTabsArray.size(); i++) {
            Bundle bundle = new Bundle();
            fragment.setArguments(bundle);
            cartAdapter.addFragment(fragment, searchTabsArray.get(i));
            fragment = new SearchProductFragment();
        }

        cartViewPager.setAdapter(cartAdapter);
    }


    private class MainAdapter extends FragmentPagerAdapter {


        ArrayList<String> arrayList = new ArrayList<>();
        List<Fragment> fragemtList = new ArrayList<>();

        public void addFragment(Fragment fragment, String title) {
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
                    SearchProductFragment searchProductFragment = new SearchProductFragment();
                    positionFragment = 0;
                    Bundle bundle = new Bundle();
                    bundle.putString("productName", searchText);
                    searchProductFragment.setArguments(bundle);
                    return searchProductFragment;

                case 1:
                    SearchCategoryFragment searchCategoryFragment = new SearchCategoryFragment();
                    positionFragment = 1;
                    return searchCategoryFragment;
                case 2:
                    SearchSellerFragment searchSellerFragment = new SearchSellerFragment();
                    positionFragment = 2;
                    return searchSellerFragment;

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
            return arrayList.get(position);
        }
    }

}