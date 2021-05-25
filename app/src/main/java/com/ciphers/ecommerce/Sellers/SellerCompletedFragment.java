package com.ciphers.ecommerce.Sellers;

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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ciphers.ecommerce.Model.CompletedSellerOrders;
import com.ciphers.ecommerce.Model.SellersDeliverOrders;
import com.ciphers.ecommerce.Prevalent.Prevalent;
import com.ciphers.ecommerce.R;
import com.ciphers.ecommerce.ViewHolder.SellerCompletedOrderViewHolder;
import com.ciphers.ecommerce.ViewHolder.SellersDeliverOrderViewHolder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.shreyaspatil.firebase.recyclerpagination.DatabasePagingOptions;
import com.shreyaspatil.firebase.recyclerpagination.FirebaseRecyclerPagingAdapter;
import com.shreyaspatil.firebase.recyclerpagination.LoadingState;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SellerCompletedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SellerCompletedFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView emptyCompletedOrderTxt;
    ImageView emptyCompletedOrderIcon;

    SwipeRefreshLayout sellerCompletedOrdersSwipeLayout;
    RecyclerView sellerCompletedOrderRecycler;
    RecyclerView.LayoutManager layoutManager;
    DatabaseReference sellerOrdersCompletedRef;
    FirebaseRecyclerPagingAdapter<CompletedSellerOrders, SellerCompletedOrderViewHolder> sellerCompletedOrdersAdapter;
    RelativeLayout emptyLayout;

    String currentOnlineSeller;
    int itemTotalPrice =0, totalPrice = 0;


    public SellerCompletedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SellerCompletedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SellerCompletedFragment newInstance(String param1, String param2) {
        SellerCompletedFragment fragment = new SellerCompletedFragment();
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
        View view = inflater.inflate(R.layout.fragment_seller_completed, container, false);

        currentOnlineSeller = Prevalent.currentOnlineSeller.getSellerUsername();

        sellerCompletedOrdersSwipeLayout = view.findViewById(R.id.seller_completed_orders_swipe_refresh_layout);
        sellerOrdersCompletedRef = FirebaseDatabase.getInstance().getReference().child("CompletedSellerOrders").child(currentOnlineSeller);
        sellerCompletedOrderRecycler = view.findViewById(R.id.seller_completed_order_recycler);

        emptyCompletedOrderIcon = view.findViewById(R.id.empty_completed_icon);
        emptyCompletedOrderTxt = view.findViewById(R.id.empty_completed_text);
        emptyLayout = view.findViewById(R.id.seller_empty_completed_layout);


        emptyCompletedFragment();
        checkCompletedOrders();

        return view;
    }

    private void emptyCompletedFragment() {

//        sellerCompletedOrdersSwipeLayout.setRefreshing(false);
        emptyLayout.setVisibility(View.VISIBLE);
        emptyCompletedOrderIcon.setVisibility(View.VISIBLE);
        emptyCompletedOrderTxt.setVisibility(View.VISIBLE);

    }

    private void checkCompletedOrders(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("CompletedSellerOrders");
        Query queries = ref.child(currentOnlineSeller);
        queries.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
//                    sellerDeliveredOrdersSwipeLayout.setRefreshing(false);
                    emptyCompletedOrderIcon.setVisibility(View.GONE);
                    emptyCompletedOrderTxt.setVisibility(View.GONE);
                    emptyLayout.setVisibility(View.GONE);
                    startSellerCompletedOrdersRecycler();

                }
                else{
//                    sellerDeliveredOrdersSwipeLayout.setRefreshing(false);
                    emptyCompletedOrderIcon.setVisibility(View.GONE);
                    emptyCompletedOrderTxt.setVisibility(View.GONE);
                    emptyLayout.setVisibility(View.GONE);
                    emptyCompletedFragment();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void startSellerCompletedOrdersRecycler(){

        sellerCompletedOrderRecycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        sellerCompletedOrderRecycler.setLayoutManager(layoutManager);

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(5)
                .setPageSize(10)
                .build();


        DatabasePagingOptions<CompletedSellerOrders> options = new DatabasePagingOptions.Builder<CompletedSellerOrders>()
                .setLifecycleOwner(this)
                .setQuery(sellerOrdersCompletedRef, config, CompletedSellerOrders.class)
                .build();


        sellerCompletedOrdersAdapter = new FirebaseRecyclerPagingAdapter<CompletedSellerOrders, SellerCompletedOrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull SellerCompletedOrderViewHolder sellerCompletedOrderViewHolder, int i, @NonNull CompletedSellerOrders completedSellerOrders) {
                itemTotalPrice = Integer.parseInt(completedSellerOrders.getOrderPrice()) * Integer.parseInt(completedSellerOrders.getOrderQty());
                totalPrice += itemTotalPrice;

                sellerCompletedOrderViewHolder.sellerOrderPriceCV.setText(completedSellerOrders.getOrderPrice());
                sellerCompletedOrderViewHolder.sellerOrderQtyCV.setText(completedSellerOrders.getOrderQty());
                sellerCompletedOrderViewHolder.sellerOrderTitleCV.setText(completedSellerOrders.getOrderName());
                sellerCompletedOrderViewHolder.sellerOrderBuyerNameCV.setText(completedSellerOrders.getOrderBuyerName());
                sellerCompletedOrderViewHolder.sellerOrderDateCV.setText(completedSellerOrders.getOrderDate());
                sellerCompletedOrderViewHolder.sellerOrderTypeCV.setText(completedSellerOrders.getOrderType());
                sellerCompletedOrderViewHolder.sellerOrderTotalPriceCV.setText("Total : " + String.valueOf(itemTotalPrice));
                sellerCompletedOrderViewHolder.sellerOrderStatusCV.setText("Active");
                Picasso.get().load(completedSellerOrders.getOrderImg()).placeholder(R.drawable.cart_logo).into(sellerCompletedOrderViewHolder.sellerOrderImageCV);
                sellerCompletedOrderViewHolder.sellerOrderShippingIDCV.setText(completedSellerOrders.getOrderShippingID());
                sellerCompletedOrderViewHolder.sellerOrderTrackingIDCV.setText(completedSellerOrders.getOrderID());


                sellerCompletedOrderViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent sellerDeliverOrderProcess = new Intent(getActivity().getApplicationContext(), SellerOrderDetailActivity.class);
                        Bundle sellerDeliverOrderBundle = new Bundle();
                        sellerDeliverOrderBundle.putString("orderID", completedSellerOrders.getOrderID());
                        sellerDeliverOrderBundle.putString("orderPrice", completedSellerOrders.getOrderPrice());
                        sellerDeliverOrderBundle.putString("orderQty", completedSellerOrders.getOrderQty());
                        sellerDeliverOrderBundle.putString("orderTitle", completedSellerOrders.getOrderName());
                        sellerDeliverOrderBundle.putString("orderSellerName", completedSellerOrders.getOrderBuyerName());
                        sellerDeliverOrderBundle.putString("orderType", completedSellerOrders.getOrderType());
                        sellerDeliverOrderBundle.putString("orderImage", completedSellerOrders.getOrderImg());
                        sellerDeliverOrderBundle.putString("orderDate", completedSellerOrders.getOrderDate());
                        sellerDeliverOrderBundle.putString("orderShippingID", completedSellerOrders.getOrderShippingID());
                        sellerDeliverOrderBundle.putString("activityType", "Completed");
                        sellerDeliverOrderBundle.putString("orderStatus", "0");
                        sellerDeliverOrderProcess.putExtras(sellerDeliverOrderBundle);
                        startActivity(sellerDeliverOrderProcess);
                    }
                });

            }

            @Override
            protected void onLoadingStateChanged(@NonNull LoadingState state) {
                switch (state) {
                    case LOADING_INITIAL:
                    case LOADING_MORE:
                        // Do your loading animation
                        sellerCompletedOrdersSwipeLayout.setRefreshing(true);
                        break;

                    case LOADED:
                        // Stop Animation
                        sellerCompletedOrdersSwipeLayout.setRefreshing(false);
                        break;

                    case FINISHED:
                        //Reached end of Data set
                        sellerCompletedOrdersSwipeLayout.setRefreshing(false);
                        break;

                    case ERROR:
                        retry();
                        break;
                }
            }

            @Override
            protected void onError(@NonNull DatabaseError databaseError) {
                sellerCompletedOrdersSwipeLayout.setRefreshing(false);
                databaseError.toException().printStackTrace();
            }


            @NonNull
            @Override
            public SellerCompletedOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_orders_cv, parent, false);
                SellerCompletedOrderViewHolder holder = new SellerCompletedOrderViewHolder(view);
                return holder;
            }
        };

        sellerCompletedOrdersSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sellerCompletedOrdersAdapter.refresh();
                sellerCompletedOrdersSwipeLayout.setRefreshing(false);
            }
        });

        sellerCompletedOrderRecycler.setAdapter(sellerCompletedOrdersAdapter);
        sellerCompletedOrdersAdapter.startListening();

    }

}