package com.ciphers.ecommerce.Buyer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import com.ciphers.ecommerce.Prevalent.Prevalent;
import com.ciphers.ecommerce.R;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity {


    ChipNavigationBar buyerNavBar;

    DatabaseReference cartRef;
    String totalItems = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Paper.init(this);

//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DashboardFragment()).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new BuyerHomeFragment()).commit();

        cartRef = FirebaseDatabase.getInstance().getReference().child("Cart List").child(Prevalent.currentOnlineUser.getUsername());

        buyerNavBar = findViewById(R.id.buyer_bottom_nav_bar);
        buyerNavBar.setItemSelected(R.id.buyer_bottom_nav_dashboard, true);

        cartItemsCounter();
        BuyerBottomMenu();

    }

    private void BuyerBottomMenu() {

        buyerNavBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;
                switch (i) {

                    case R.id.buyer_bottom_nav_dashboard:
                        fragment = new BuyerHomeFragment();
//                        fragment = new DashboardFragment();
                        break;

                    case R.id.buyer_bottom_nav_search:
                        fragment = new BuyerSearchFragment();
                        break;

                    case R.id.buyer_bottom_nav_cart:
                        fragment = new CartFragment();
                        break;


                    case R.id.buyer_bottom_nav_notifications:
                        fragment = new BuyerNotificationsFragment();
                        break;

                    case R.id.buyer_bottom_nav_user:
                        fragment = new BuyerProfile();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            }
        });
    }

    public void cartItemsCounter() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Cart List");
        Query queries = ref.child(Prevalent.currentOnlineUser.getUsername());
        queries.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    startCartProductRecycler();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void startCartProductRecycler() {

        cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                totalItems = String.valueOf(dataSnapshot.getChildrenCount());
                buyerNavBar.showBadge(R.id.buyer_bottom_nav_cart, Integer.parseInt(totalItems));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}