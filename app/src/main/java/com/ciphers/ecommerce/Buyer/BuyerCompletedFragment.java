package com.ciphers.ecommerce.Buyer;

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

import com.ciphers.ecommerce.Model.CompletedBuyerOrders;
import com.ciphers.ecommerce.Model.SellersDeliverOrders;
import com.ciphers.ecommerce.Prevalent.Prevalent;
import com.ciphers.ecommerce.R;
import com.ciphers.ecommerce.ViewHolder.BuyerCompletedOrdersViewHolder;
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
 * Use the {@link BuyerCompletedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BuyerCompletedFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView emptyCompletedOrderTxt;
    ImageView emptyCompletedOrderIV;

    SwipeRefreshLayout sellerCompletedOrdersSwipeLayout;
    RecyclerView sellerCompletedOrderRecycler;
    RecyclerView.LayoutManager layoutManager;
    DatabaseReference sellerOrdersCompletedRef;
    FirebaseRecyclerPagingAdapter<CompletedBuyerOrders, BuyerCompletedOrdersViewHolder> sellerCompletedOrdersAdapter;
    RelativeLayout emptyLayout;

    String currentOnlineBuyer;
    int itemTotalPrice =0, totalPrice = 0;


    public BuyerCompletedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CompletedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BuyerCompletedFragment newInstance(String param1, String param2) {
        BuyerCompletedFragment fragment = new BuyerCompletedFragment();
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
        View view =inflater.inflate(R.layout.fragment_buyer_completed, container, false);

        currentOnlineBuyer = Prevalent.currentOnlineUser.getUsername();

        sellerCompletedOrdersSwipeLayout = view.findViewById(R.id.buyer_completed_order_swipe_refresh_layout);
        sellerOrdersCompletedRef = FirebaseDatabase.getInstance().getReference().child("CompletedBuyerOrders").child(currentOnlineBuyer);
        sellerCompletedOrderRecycler = view.findViewById(R.id.buyer_completed_order_recycler);

        emptyCompletedOrderIV = view.findViewById(R.id.empty_buyer_completed_icon);
        emptyCompletedOrderTxt = view.findViewById(R.id.empty_buyer_completed_text);
        emptyLayout = view.findViewById(R.id.empty_buyer_completed_layout);


        emptyCompletedFragment();
        checkCompletedOrders();

        return view;
    }

    private void checkCompletedOrders(){

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("CompletedBuyerOrders");
        Query queries = ref.child(currentOnlineBuyer);
        queries.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
//                    sellerDeliveredOrdersSwipeLayout.setRefreshing(false);
                    emptyLayout.setVisibility(View.GONE);
                    emptyCompletedOrderIV.setVisibility(View.GONE);
                    emptyCompletedOrderTxt.setVisibility(View.GONE);
                    startBuyerCompletedOrdersRecycler();

                }
                else{
//                    sellerDeliveredOrdersSwipeLayout.setRefreshing(false);
                    emptyLayout.setVisibility(View.GONE);
                    emptyCompletedOrderIV.setVisibility(View.GONE);
                    emptyCompletedOrderTxt.setVisibility(View.GONE);
                    emptyCompletedFragment();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void emptyCompletedFragment(){
//        sellerDeliveredOrdersSwipeLayout.setRefreshing(false);
        emptyLayout.setVisibility(View.VISIBLE);
        emptyCompletedOrderIV.setVisibility(View.VISIBLE);
        emptyCompletedOrderTxt.setVisibility(View.VISIBLE);
    }

    private void startBuyerCompletedOrdersRecycler(){
        sellerCompletedOrderRecycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        sellerCompletedOrderRecycler.setLayoutManager(layoutManager);

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(5)
                .setPageSize(10)
                .build();


        DatabasePagingOptions<CompletedBuyerOrders> options = new DatabasePagingOptions.Builder<CompletedBuyerOrders>()
                .setLifecycleOwner(this)
                .setQuery(sellerOrdersCompletedRef, config, CompletedBuyerOrders.class)
                .build();


        sellerCompletedOrdersAdapter = new FirebaseRecyclerPagingAdapter<CompletedBuyerOrders, BuyerCompletedOrdersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull BuyerCompletedOrdersViewHolder buyerCompletedOrdersViewHolder, int i, @NonNull CompletedBuyerOrders completedBuyerOrders) {
                itemTotalPrice = Integer.parseInt(completedBuyerOrders.getOrderPrice()) * Integer.parseInt(completedBuyerOrders.getOrderQty());
                totalPrice += itemTotalPrice;

                buyerCompletedOrdersViewHolder.sellerOrderPriceCV.setText(completedBuyerOrders.getOrderPrice());
                buyerCompletedOrdersViewHolder.sellerOrderQtyCV.setText(completedBuyerOrders.getOrderQty());
                buyerCompletedOrdersViewHolder.sellerOrderTitleCV.setText(completedBuyerOrders.getOrderName());
                buyerCompletedOrdersViewHolder.sellerOrderBuyerNameCV.setText(completedBuyerOrders.getOrderBuyerName());
                buyerCompletedOrdersViewHolder.sellerOrderDateCV.setText(completedBuyerOrders.getOrderDate());
                buyerCompletedOrdersViewHolder.sellerOrderTypeCV.setText(completedBuyerOrders.getOrderType());
                buyerCompletedOrdersViewHolder.sellerOrderTotalPriceCV.setText("Total : " + String.valueOf(itemTotalPrice));
                buyerCompletedOrdersViewHolder.sellerOrderStatusCV.setText("Completed");
                Picasso.get().load(completedBuyerOrders.getOrderImg()).placeholder(R.drawable.cart_logo).into(buyerCompletedOrdersViewHolder.sellerOrderImageCV);
                buyerCompletedOrdersViewHolder.sellerOrderShippingIDCV.setText(completedBuyerOrders.getOrderShippingID());
                buyerCompletedOrdersViewHolder.sellerOrderTrackingIDCV.setText(completedBuyerOrders.getOrderID());
                buyerCompletedOrdersViewHolder.sellerOrderPaymentType.setText(completedBuyerOrders.getPaymentType());

                buyerCompletedOrdersViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent sellerDeliverOrderProcess = new Intent(getActivity().getApplicationContext(), BuyerOrderDetailActivity.class);
                        Bundle sellerDeliverOrderBundle = new Bundle();
                        sellerDeliverOrderBundle.putString("orderID", completedBuyerOrders.getOrderID());
                        sellerDeliverOrderBundle.putString("orderPrice", completedBuyerOrders.getOrderPrice());
                        sellerDeliverOrderBundle.putString("orderQty", completedBuyerOrders.getOrderQty());
                        sellerDeliverOrderBundle.putString("orderTitle", completedBuyerOrders.getOrderName());
                        sellerDeliverOrderBundle.putString("orderSellerName", completedBuyerOrders.getOrderSellerName());
                        sellerDeliverOrderBundle.putString("orderType", completedBuyerOrders.getOrderType());
                        sellerDeliverOrderBundle.putString("orderImage", completedBuyerOrders.getOrderImg());
                        sellerDeliverOrderBundle.putString("orderDate", completedBuyerOrders.getOrderDate());
                        sellerDeliverOrderBundle.putString("orderShippingID", completedBuyerOrders.getOrderShippingID());
                        sellerDeliverOrderBundle.putString("paymentType", completedBuyerOrders.getPaymentType());
                        sellerDeliverOrderBundle.putString("orderStatus", "0");
                        sellerDeliverOrderBundle.putString("activityType", "Completed");
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
            public BuyerCompletedOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_orders_cv, parent, false);
                BuyerCompletedOrdersViewHolder holder = new BuyerCompletedOrdersViewHolder(view);
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