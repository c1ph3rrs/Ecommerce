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

import com.ciphers.ecommerce.Model.CompletedSellerOrders;
import com.ciphers.ecommerce.Model.WithdrawRequest;
import com.ciphers.ecommerce.R;
import com.ciphers.ecommerce.Sellers.SellerOrderDetailActivity;
import com.ciphers.ecommerce.ViewHolder.SellerCompletedOrderViewHolder;
import com.ciphers.ecommerce.ViewHolder.WithdrawRequestViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shreyaspatil.firebase.recyclerpagination.DatabasePagingOptions;
import com.shreyaspatil.firebase.recyclerpagination.FirebaseRecyclerPagingAdapter;
import com.shreyaspatil.firebase.recyclerpagination.LoadingState;
import com.squareup.picasso.Picasso;

public class AdminSellerPaymentsRequestsActivity extends AppCompatActivity {

    RecyclerView sellerRequestPaymentRecycler;
    RecyclerView.LayoutManager layoutManager;

    DatabaseReference withdrawRef;
    FirebaseRecyclerPagingAdapter<WithdrawRequest, WithdrawRequestViewHolder> withdrawAdapter;
    SwipeRefreshLayout withdrawRequestLayout;
    ImageView paymentRequestBackIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_seller_payments_requests);

        withdrawRef = FirebaseDatabase.getInstance().getReference().child("WithdrawRequest");

        sellerRequestPaymentRecycler = findViewById(R.id.payments_request_recycler);
        withdrawRequestLayout = findViewById(R.id.withdraw_request_swipe_refresh_layout);
        paymentRequestBackIcon = findViewById(R.id.seller_payments_request_back_icon);

        paymentRequestBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdminSellerPaymentsRequestsActivity.super.onBackPressed();
            }
        });

        getSellerPaymentsRequest();

    }

    @Override
    public void onBackPressed() {
        AdminSellerPaymentsRequestsActivity.super.onBackPressed();
    }

    private void getSellerPaymentsRequest(){

        sellerRequestPaymentRecycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        sellerRequestPaymentRecycler.setLayoutManager(layoutManager);

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(5)
                .setPageSize(10)
                .build();


        DatabasePagingOptions<WithdrawRequest> options = new DatabasePagingOptions.Builder<WithdrawRequest>()
                .setLifecycleOwner(this)
                .setQuery(withdrawRef, config, WithdrawRequest.class)
                .build();


        withdrawAdapter = new FirebaseRecyclerPagingAdapter<WithdrawRequest, WithdrawRequestViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull WithdrawRequestViewHolder withdrawRequestViewHolder, int i, @NonNull WithdrawRequest withdrawRequest) {

                withdrawRequestViewHolder.sellerPaymentNameTxt.setText(withdrawRequest.getSellerUsername());
                withdrawRequestViewHolder.sellerPaymentDateTxt.setText(withdrawRequest.getSellerWithdrawDate());
                withdrawRequestViewHolder.sellerPaymentPriceTxt.setText(withdrawRequest.getSellerPayment());

                withdrawRequestViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent paymentDetailIntent = new Intent(getApplicationContext(), AdminSellerPaymentDetailActivity.class);
                        paymentDetailIntent.putExtra("sellerUsername", withdrawRequest.getSellerUsername());
                        startActivity(paymentDetailIntent);
                    }
                });

            }

            @Override
            protected void onLoadingStateChanged(@NonNull LoadingState state) {
                switch (state) {
                    case LOADING_INITIAL:
                    case LOADING_MORE:
                        // Do your loading animation
                        withdrawRequestLayout.setRefreshing(true);
                        break;

                    case LOADED:
                        // Stop Animation
                        withdrawRequestLayout.setRefreshing(false);
                        break;

                    case FINISHED:
                        //Reached end of Data set
                        withdrawRequestLayout.setRefreshing(false);
                        break;

                    case ERROR:
                        retry();
                        break;
                }
            }

            @Override
            protected void onError(@NonNull DatabaseError databaseError) {
                withdrawRequestLayout.setRefreshing(false);
                databaseError.toException().printStackTrace();
            }


            @NonNull
            @Override
            public WithdrawRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_seller_payments_request_card_view, parent, false);
                WithdrawRequestViewHolder holder = new WithdrawRequestViewHolder(view);
                return holder;
            }
        };

        withdrawRequestLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                withdrawAdapter.refresh();

            }
        });

        sellerRequestPaymentRecycler.setAdapter(withdrawAdapter);
        withdrawAdapter.startListening();

    }
}