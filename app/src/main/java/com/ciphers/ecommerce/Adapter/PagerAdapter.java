package com.ciphers.ecommerce.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.ciphers.ecommerce.Buyer.CartProductFragment;
import com.ciphers.ecommerce.Buyer.BuyerCompletedFragment;
import com.ciphers.ecommerce.Buyer.InProgressFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;

    // an array of tab titles
    private String tabTitles[] = new String[]{"Cart", "In Progress", "Completed"};

    public PagerAdapter(@NonNull FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
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
                BuyerCompletedFragment completedTab = new BuyerCompletedFragment();
                return completedTab;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
