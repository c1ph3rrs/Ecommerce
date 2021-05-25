package com.ciphers.ecommerce.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ciphers.ecommerce.Branch.BranchOrderDetailActivity;
import com.ciphers.ecommerce.Model.BranchDeliveredOrders;
import com.ciphers.ecommerce.Model.ChallanRecord;
import com.ciphers.ecommerce.R;
import com.ciphers.ecommerce.ViewHolder.BranchesOrdersViewHolder;
import com.ciphers.ecommerce.ViewHolder.ChallanRecordViewHolder;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shreyaspatil.firebase.recyclerpagination.DatabasePagingOptions;
import com.shreyaspatil.firebase.recyclerpagination.FirebaseRecyclerPagingAdapter;
import com.shreyaspatil.firebase.recyclerpagination.LoadingState;

public class AdminBranchPaymentsActivity extends AppCompatActivity {

    RecyclerView adminBranchPaymentRecycler;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerPagingAdapter<ChallanRecord, ChallanRecordViewHolder> adminBranchPaymentAdapter;
    DatabaseReference adminChallanRef;
    SwipeRefreshLayout adminBranchPaymentRefreshLayout;
    ImageView adminBranchPaymentBackIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_branch_payments);

        adminChallanRef = FirebaseDatabase.getInstance().getReference().child("Challan Record");

        adminBranchPaymentRecycler = findViewById(R.id.admin_branch_payment_recycler);
        adminBranchPaymentRefreshLayout = findViewById(R.id.admin_branch_payments_refresh_layout);
        adminBranchPaymentBackIcon = findViewById(R.id.admin_branch_payment_back_icon);

        startBranchPaymentAdapter();

        adminBranchPaymentBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdminBranchPaymentsActivity.super.onBackPressed();
            }
        });

    }

    private void startBranchPaymentAdapter(){

        adminBranchPaymentRecycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        adminBranchPaymentRecycler.setLayoutManager(layoutManager);

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(5)
                .setPageSize(10)
                .build();

        DatabasePagingOptions<ChallanRecord> options = new DatabasePagingOptions.Builder<ChallanRecord>()
                .setLifecycleOwner(this)
                .setQuery(adminChallanRef, config, ChallanRecord.class)
                .build();

        adminBranchPaymentAdapter = new FirebaseRecyclerPagingAdapter<ChallanRecord, ChallanRecordViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ChallanRecordViewHolder challanRecordViewHolder, int i, @NonNull ChallanRecord challanRecord) {
                challanRecordViewHolder.challanRecordIdTV.setText(challanRecord.getChallanId());
                challanRecordViewHolder.challanRecordDescTV.setText(challanRecord.getChallanDesc());

                challanRecordViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent branchesIntent = new Intent(getApplicationContext(), AdminBranchPaymentDetailActivity.class);
                        branchesIntent.putExtra("challanRecordId", challanRecord.getChallanId());
                        branchesIntent.putExtra("challanRecordDesc", challanRecord.getChallanDesc());
                        branchesIntent.putExtra("challanImg", challanRecord.getImageUrl());
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
                        adminBranchPaymentRefreshLayout.setRefreshing(true);
                        break;

                    case LOADED:
                        // Stop Animation
                        adminBranchPaymentRefreshLayout.setRefreshing(false);
                        break;

                    case FINISHED:
                        //Reached end of Data set
                        adminBranchPaymentRefreshLayout.setRefreshing(false);
                        break;

                    case ERROR:
                        retry();
                        break;
                }
            }

            @NonNull
            @Override
            public ChallanRecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.branch_payments_card_view, parent, false);
                ChallanRecordViewHolder holder = new ChallanRecordViewHolder(view);
                return holder;
            }
        };

        adminBranchPaymentRecycler.setAdapter(adminBranchPaymentAdapter);
        adminBranchPaymentAdapter.startListening();

    }
}