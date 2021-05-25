package com.ciphers.ecommerce.Branch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ciphers.ecommerce.Model.BranchesOrders;
import com.ciphers.ecommerce.Model.Products;
import com.ciphers.ecommerce.Model.SellersOrders;
import com.ciphers.ecommerce.Prevalent.Prevalent;
import com.ciphers.ecommerce.R;
import com.ciphers.ecommerce.ViewHolder.BranchesOrdersViewHolder;
import com.ciphers.ecommerce.ViewHolder.SellersOrdersViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shreyaspatil.firebase.recyclerpagination.DatabasePagingOptions;
import com.shreyaspatil.firebase.recyclerpagination.FirebaseRecyclerPagingAdapter;
import com.shreyaspatil.firebase.recyclerpagination.LoadingState;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BranchReceiveOtherOrdersActivity extends AppCompatActivity {

    ImageView branchReceiveOtherOrdersBackIV;
    RecyclerView branchReceiveOtherOrdersRecycler;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerPagingAdapter<BranchesOrders, BranchesOrdersViewHolder> branchOrderAdapter;

//    FirebaseRecyclerAdapter<BranchesOrders, BranchesOrdersViewHolder> branchOrderAdapter;
    DatabaseReference branchOrderRef;
    SwipeRefreshLayout branchesOrderSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_receive_other_orders);

        branchOrderRef = FirebaseDatabase.getInstance().getReference().child("BranchesOrders").child("ReceiverBranch").child(Prevalent.currentOnlineBranch.getBranchCode());
        branchesOrderSwipeRefreshLayout = findViewById(R.id.branches_order_swipe_refresh_layout);
        branchReceiveOtherOrdersRecycler = findViewById(R.id.receive_others_branch_orders_recycler);

        branchReceiveOtherOrdersBackIV = findViewById(R.id.branch_receive_order_back_icon);
        Log.d("Reference is ", "data " + branchOrderRef);
        Log.d("Activity Activated", "activated");

        branchReceiveOtherOrdersBackIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BranchReceiveOtherOrdersActivity.super.onBackPressed();
            }
        });

        startBranchOrderRecycler();
//        startBranchOrderRecyclerAdapter();

    }

    private void startBranchOrderRecycler(){


        branchReceiveOtherOrdersRecycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(BranchReceiveOtherOrdersActivity.this, LinearLayoutManager.VERTICAL, false);
        branchReceiveOtherOrdersRecycler.setLayoutManager(layoutManager);


        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(5)
                .setPageSize(10)
                .build();


        DatabasePagingOptions<BranchesOrders> options = new DatabasePagingOptions.Builder<BranchesOrders>()
                .setLifecycleOwner(this)
                .setQuery(branchOrderRef, config, BranchesOrders.class)
                .build();

        branchOrderAdapter = new FirebaseRecyclerPagingAdapter<BranchesOrders, BranchesOrdersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull BranchesOrdersViewHolder branchesOrdersViewHolder, int i, @NonNull BranchesOrders branchesOrders) {
                branchesOrdersViewHolder.branchShippingIDCV.setText(branchesOrders.getShippingID());
                branchesOrdersViewHolder.branchTrackingIDCV.setText(branchesOrders.getTrackingID());
                branchesOrdersViewHolder.branchShippingFromCV.setText("Shipped From : " + branchesOrders.getSendFrom());
                branchesOrdersViewHolder.branchShippingToCV.setText("Shipped To : " + branchesOrders.getSendTo());
                branchesOrdersViewHolder.branchShippingDateCV.setText(branchesOrders.getDate());

                branchesOrdersViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent branchesIntent = new Intent(getApplicationContext(), BranchAssignOrderForDeliveryActivity.class);
                        branchesIntent.putExtra("shippingId", branchesOrders.getShippingID());
                        branchesIntent.putExtra("trackingId", branchesOrders.getTrackingID());
                        branchesIntent.putExtra("shippingFrom", branchesOrders.getSendFrom());
                        branchesIntent.putExtra("shippingTo", branchesOrders.getSendTo());
                        branchesIntent.putExtra("shippingDate", branchesOrders.getDate());
                        branchesIntent.putExtra("orderID", branchesOrders.getOrderID());
                        startActivity(branchesIntent);
                    }
                });
            }

            @Override
            protected void onLoadingStateChanged(@NonNull LoadingState state) {
                switch (state) {
                    case LOADING_INITIAL:
                    case LOADING_MORE:
                        // Do your loading animation
                        branchesOrderSwipeRefreshLayout.setRefreshing(true);
                        break;

                    case LOADED:
                        // Stop Animation
                        branchesOrderSwipeRefreshLayout.setRefreshing(false);
                        break;

                    case FINISHED:
                        //Reached end of Data set
                        branchesOrderSwipeRefreshLayout.setRefreshing(false);
                        break;

                    case ERROR:
                        retry();
                        break;
                }
            }

            @NonNull
            @Override
            public BranchesOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.branches_orders_cv, parent, false);
                BranchesOrdersViewHolder holder = new BranchesOrdersViewHolder(view);
                return holder;
            }
        };

        branchReceiveOtherOrdersRecycler.setAdapter(branchOrderAdapter);
        branchOrderAdapter.startListening();

    }

    @Override
    public void onBackPressed() {
        BranchReceiveOtherOrdersActivity.super.onBackPressed();
    }
}