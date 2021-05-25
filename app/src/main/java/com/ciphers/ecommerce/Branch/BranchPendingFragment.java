package com.ciphers.ecommerce.Branch;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ciphers.ecommerce.Model.BranchDeliveredOrders;
import com.ciphers.ecommerce.Model.BranchesOrders;
import com.ciphers.ecommerce.Model.PlacedOrder;
import com.ciphers.ecommerce.Prevalent.Prevalent;
import com.ciphers.ecommerce.R;
import com.ciphers.ecommerce.ViewHolder.BranchesOrdersViewHolder;
import com.ciphers.ecommerce.ViewHolder.PlacedOrderViewHolder;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shreyaspatil.firebase.recyclerpagination.DatabasePagingOptions;
import com.shreyaspatil.firebase.recyclerpagination.FirebaseRecyclerPagingAdapter;
import com.shreyaspatil.firebase.recyclerpagination.LoadingState;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BranchPendingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BranchPendingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    SwipeRefreshLayout branchPendingOrderSwipeRefreshLayout;

    RecyclerView ordersBranchPendingRecycler;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerPagingAdapter<BranchDeliveredOrders, BranchesOrdersViewHolder> branchOrderAdapter;
    DatabaseReference branchPendingOrderRef;

    public BranchPendingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BranchPendingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BranchPendingFragment newInstance(String param1, String param2) {
        BranchPendingFragment fragment = new BranchPendingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_branch_pending, container, false);

        branchPendingOrderRef = FirebaseDatabase.getInstance().getReference().child("BranchOrders")
                .child("BranchPendingOrders").child(Prevalent.currentOnlineBranch.getBranchCode());
        branchPendingOrderSwipeRefreshLayout = view.findViewById(R.id.branch_orders_swipe_refresh_layout);
        ordersBranchPendingRecycler = view.findViewById(R.id.branch_order_recycler);


        startBranchOrderPendingRecycler();

        return view;
    }

    private void startBranchOrderPendingRecycler(){

        ordersBranchPendingRecycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        ordersBranchPendingRecycler.setLayoutManager(layoutManager);


        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(5)
                .setPageSize(10)
                .build();


        DatabasePagingOptions<BranchDeliveredOrders> options = new DatabasePagingOptions.Builder<BranchDeliveredOrders>()
                .setLifecycleOwner(this)
                .setQuery(branchPendingOrderRef, config, BranchDeliveredOrders.class)
                .build();

        branchOrderAdapter = new FirebaseRecyclerPagingAdapter<BranchDeliveredOrders, BranchesOrdersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull BranchesOrdersViewHolder branchesOrdersViewHolder, int i, @NonNull BranchDeliveredOrders branchDeliveredOrders) {
                branchesOrdersViewHolder.branchShippingIDCV.setText(branchDeliveredOrders.getShipping());
                branchesOrdersViewHolder.branchTrackingIDCV.setText(branchDeliveredOrders.getTracking());
                branchesOrdersViewHolder.branchShippingFromCV.setText("Shipped From : " + branchDeliveredOrders.getFrom());
                branchesOrdersViewHolder.branchShippingToCV.setText("Shipped To : " + branchDeliveredOrders.getTo());
                branchesOrdersViewHolder.branchShippingDateCV.setText(branchDeliveredOrders.getDate() + " Assigning " + branchDeliveredOrders.getAssignDate());

//                branchesOrdersViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent branchesIntent = new Intent(getApplicationContext(), BranchAssignOrderForDeliveryActivity.class);
//                        branchesIntent.putExtra("shippingId", branchesOrders.getShippingID());
//                        branchesIntent.putExtra("trackingId", branchesOrders.getTrackingID());
//                        branchesIntent.putExtra("shippingFrom", branchesOrders.getSendFrom());
//                        branchesIntent.putExtra("shippingTo", branchesOrders.getSendTo());
//                        branchesIntent.putExtra("shippingDate", branchesOrders.getDate());
//                        branchesIntent.putExtra("orderID", branchesOrders.getOrderID());
//                        startActivity(branchesIntent);
//                    }
//                });
            }

            @Override
            protected void onLoadingStateChanged(@NonNull LoadingState state) {
                switch (state) {
                    case LOADING_INITIAL:
                    case LOADING_MORE:
                        // Do your loading animation
                        branchPendingOrderSwipeRefreshLayout.setRefreshing(true);
                        break;

                    case LOADED:
                        // Stop Animation
                        branchPendingOrderSwipeRefreshLayout.setRefreshing(false);
                        break;

                    case FINISHED:
                        //Reached end of Data set
                        branchPendingOrderSwipeRefreshLayout.setRefreshing(false);
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

        ordersBranchPendingRecycler.setAdapter(branchOrderAdapter);
        branchOrderAdapter.startListening();

    }
}