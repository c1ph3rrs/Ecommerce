package com.ciphers.ecommerce.Buyer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ciphers.ecommerce.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {

//    List<String> collectionOfCartItems;

    ViewPager cartViewPager;
    TabLayout cartTabsLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        cartViewPager = view.findViewById(R.id.cart_view_pager);
        cartTabsLayout = view.findViewById(R.id.cart_tabs_layout);


        ArrayList<String> cartTabsArray = new ArrayList<>();
        cartTabsArray.add("Cart");
        cartTabsArray.add("Pending");
        cartTabsArray.add("Approved");
        cartTabsArray.add("Delivered");
        cartTabsArray.add("Completed");
        cartTabsArray.add("Cancel");

        prepareViewPager(cartViewPager, cartTabsArray);
        cartTabsLayout.setupWithViewPager(cartViewPager, true);

//        return inflater.inflate(R.layout.fragment_cart, container, false);
        return view;
    }


    void prepareViewPager(ViewPager cartViewPager, ArrayList<String> cartTabArray){


        MainAdapter cartAdapter = new MainAdapter(getActivity().getSupportFragmentManager());
        CartProductFragment fragment = new CartProductFragment();

        for(int i=0; i< cartTabArray.size(); i++){
            Bundle bundle = new Bundle();
            fragment.setArguments(bundle);
            cartAdapter.addFragment(fragment, cartTabArray.get(i));
            fragment = new CartProductFragment();
        }

        cartViewPager.setAdapter(cartAdapter);
    }

    private class MainAdapter extends FragmentPagerAdapter{


        ArrayList<String> arrayList = new ArrayList<>();
        List<Fragment> fragemtList = new ArrayList<>();

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
                    CartProductFragment cartProductTab = new CartProductFragment();
                    return cartProductTab;
                case 1:
                    InProgressFragment inProgressTab = new InProgressFragment();
                    return inProgressTab;
                case 2:
                    BuyerApprovedFragment approvedOrderTab = new BuyerApprovedFragment();
                    return approvedOrderTab;
                case 3:
                    BuyerDeliveredOrderFragment buyerDeliverOrderTab = new BuyerDeliveredOrderFragment();
                    return buyerDeliverOrderTab;
                case 4:
                    BuyerCompletedFragment completedTab = new BuyerCompletedFragment();
                    return completedTab;
                case 5:
                    CancelBuyerOrderFragment cancelBuyerOrderTab = new CancelBuyerOrderFragment();
                    return cancelBuyerOrderTab;
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