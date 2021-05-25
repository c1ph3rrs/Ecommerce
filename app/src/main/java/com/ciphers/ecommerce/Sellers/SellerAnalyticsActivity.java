package com.ciphers.ecommerce.Sellers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ciphers.ecommerce.Buyer.BuyerAnalyticsActivity;
import com.ciphers.ecommerce.Prevalent.Prevalent;
import com.ciphers.ecommerce.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SellerAnalyticsActivity extends AppCompatActivity {

    ImageView sellerAnalyticsBackIcon;
    TextView currentLevelTV, currentOrdersTV, currentRattingTV, currentBuyingTV;
    DatabaseReference sellerAnalyticsRef;

    int totalOrder;
    float totalEarning, ratting;
    String totalRattingNumbStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_analytics);

        sellerAnalyticsRef = FirebaseDatabase.getInstance().getReference().child("Analytics").child("Sellers");

        currentLevelTV = findViewById(R.id.seller_current_level_txt);
        currentOrdersTV = findViewById(R.id.seller_total_orders_txt);
        currentRattingTV = findViewById(R.id.seller_overall_ratting_txt);
        currentBuyingTV = findViewById(R.id.seller_total_purchase_txt);
        sellerAnalyticsBackIcon = findViewById(R.id.seller_analytics_back_icon);

        checkCurrentLevel();

        sellerAnalyticsBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SellerAnalyticsActivity.super.onBackPressed();
            }
        });

    }

    private void checkCurrentLevel(){

        sellerAnalyticsRef.child(Prevalent.currentOnlineSeller.getSellerUsername()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    String totalEarning = snapshot.child("totalEarning").getValue(String.class);
                    String totalNumberRatting = snapshot.child("totalNumberRatting").getValue(String.class);
                    String totalOrders = snapshot.child("totalOrders").getValue(String.class);
                    String totalRatting = snapshot.child("totalRatting").getValue(String.class);

                    totalRattingNumbStr = String.valueOf(Float.parseFloat(totalRatting) / Float.parseFloat(totalNumberRatting));

                    currentBuyingTV.setText(totalEarning);
                    currentOrdersTV.setText(totalOrders);


                    currentRattingTV.setText(totalRattingNumbStr);


                    checkSellerAnalytics();

                }else{


                    currentBuyingTV.setText("0");
                    currentOrdersTV.setText("0");

                    currentRattingTV.setText("0");
                    currentLevelTV.setText("No level");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void checkSellerAnalytics(){

        int levelOneOrders = 15, levelTwoOrders = 50, topOrders = 100;
        float levelOneEarning = 10000, levelTwoEarning = 30000, topEarning = 60000;
        float levelOneRatting = (float) 3.5, levelTwoRatting = (float) 3.9, topRatting = (float) 4.0;

        totalOrder = Integer.parseInt(currentOrdersTV.getText().toString());
        totalEarning = Float.parseFloat(currentBuyingTV.getText().toString());
        ratting = Float.parseFloat(String.valueOf(currentRattingTV.getText().toString()));

        if (totalOrder >= topOrders && totalEarning >= topEarning && ratting >= topRatting) {
            currentLevelTV.setText("Top Level");
        } else if (totalOrder >= levelTwoOrders && totalEarning >= levelTwoEarning && ratting >= levelTwoRatting) {
            currentLevelTV.setText("Level Two");
        } else if (totalOrder >= levelOneOrders && totalEarning >= levelOneEarning && ratting >= levelOneRatting) {
            currentLevelTV.setText("Level One");
        } else {
            currentLevelTV.setText("No level");
        }

    }

    @Override
    public void onBackPressed() {
        SellerAnalyticsActivity.super.onBackPressed();
    }
}