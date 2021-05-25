 package com.ciphers.ecommerce.Sellers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.ciphers.ecommerce.Buyer.BuyerProfile;
import com.ciphers.ecommerce.Buyer.DashboardFragment;
import com.ciphers.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.Objects;

public class SellerHome extends AppCompatActivity {

    ChipNavigationBar sellerHomeNav;
    String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_home);

        status = getIntent().getStringExtra("status");


        sellerHomeNav = findViewById(R.id.seller_bottom_nav_bar);

        getSupportFragmentManager().beginTransaction().replace(R.id.seller_home_activity, new SellerDashboardFragment()).commit();
        sellerHomeNav.setItemSelected(R.id.seller_nav_dashboard, true);

//
        sellerBottomMenu();

    }


    private void sellerBottomMenu() {

        sellerHomeNav.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;
                switch (i){

                    case R.id.seller_nav_dashboard:
                        fragment = new SellerDashboardFragment();
                        break;

                    case R.id.seller_nav_messages:
//                        fragment = new CartFragment();
                        break;

                    case R.id.seller_nav_notification:
                        fragment = new SellerNotificationsFragment();
                        break;

                    case R.id.seller_nav_user:
                        fragment = new SellerProfileFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.seller_home_activity, fragment).commit();
            }
        });
    }
}