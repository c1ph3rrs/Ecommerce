package com.ciphers.ecommerce.Branch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ciphers.ecommerce.Prevalent.Prevalent;
import com.ciphers.ecommerce.R;
import com.ciphers.ecommerce.SplashScreen;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class BranchDashboardActivity extends AppCompatActivity {

    ImageView receiveSellerOrderIV,  addNewDeliveryGuyIV, branchReceiveOtherOrdersIV;
    ImageView branchDeliveredOrderBtn, branchAddChallanFormIV, branchLogoutIV;
    TextView branchTotalPaymentTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_dashboard);

        receiveSellerOrderIV = findViewById(R.id.branch_receive_seller_order_btn);

        addNewDeliveryGuyIV = findViewById(R.id.admin_add_delivery_guy_iv);

        branchLogoutIV = findViewById(R.id.branch_logout);
        branchReceiveOtherOrdersIV = findViewById(R.id.receive_other_branch_order_iv);
        branchDeliveredOrderBtn = findViewById(R.id.branch_delivered_order_for_delivery_btn);
        branchAddChallanFormIV = findViewById(R.id.branch_insert_challan_iv);
        branchTotalPaymentTxt = findViewById(R.id.branch_payments_txt);

        receiveSellerOrderIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), BranchReceiveSellerOrderActivity.class));
            }
        });

        branchDeliveredOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), BranchOrdersActivity.class));
            }
        });

        branchReceiveOtherOrdersIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), BranchReceiveOtherOrdersActivity.class));
            }
        });

        branchAddChallanFormIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), BranchChallanFormActivity.class));
            }
        });

//        cityToCityDeliveryIV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(), BranchCityToCityDeliveryActivity.class));
//            }
//        });

        branchLogoutIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Paper.book().destroy();
                Intent logoutIntent = new Intent(getApplicationContext(), SplashScreen.class);
                logoutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                finish();
                startActivity(logoutIntent);
            }
        });

        checkBranchPaymentRecord();

    }

    private void checkBranchPaymentRecord(){

        DatabaseReference branchPaymentsRecordRef = FirebaseDatabase.getInstance().getReference();

        Query queries = branchPaymentsRecordRef.child("BranchesPayments").child(Prevalent.currentOnlineBranch.getBranchCode());
        queries.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    String branchPayment = dataSnapshot.child("availableBalance").getValue(String.class);

                    branchTotalPaymentTxt.setText(branchPayment);

                }
                else{
                    branchTotalPaymentTxt.setText("0.0");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}