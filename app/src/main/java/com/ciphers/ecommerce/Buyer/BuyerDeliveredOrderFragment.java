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

import com.ciphers.ecommerce.Model.SellersDeliverOrders;
import com.ciphers.ecommerce.Prevalent.Prevalent;
import com.ciphers.ecommerce.R;
import com.ciphers.ecommerce.Sellers.SellerOrderDetailActivity;
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
 * Use the {@link BuyerDeliveredOrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BuyerDeliveredOrderFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    TextView emptyDeliveredOrderTxt;
    ImageView emptyDeliveredOrderIV;

    SwipeRefreshLayout sellerDeliveredOrdersSwipeLayout;
    RecyclerView sellerDeliveredOrderRecycler;
    RecyclerView.LayoutManager layoutManager;
    DatabaseReference sellerOrdersDeliveredRef;
    FirebaseRecyclerPagingAdapter<SellersDeliverOrders, SellersDeliverOrderViewHolder> sellerOrdersAdapter;
    RelativeLayout emptyLayout;

    String currentOnlineBuyer;
    int itemTotalPrice =0, totalPrice = 0;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BuyerDeliveredOrderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DeliveredFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BuyerDeliveredOrderFragment newInstance(String param1, String param2) {
        BuyerDeliveredOrderFragment fragment = new BuyerDeliveredOrderFragment();
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
        View view = inflater.inflate(R.layout.fragment_buyer_delivered_order, container, false);

        currentOnlineBuyer = Prevalent.currentOnlineUser.getUsername();

        sellerDeliveredOrdersSwipeLayout = view.findViewById(R.id.buyer_delivered_order_swipe_refresh_layout);
        sellerOrdersDeliveredRef = FirebaseDatabase.getInstance().getReference().child("BuyerDeliverOrders").child(currentOnlineBuyer);
        sellerDeliveredOrderRecycler = view.findViewById(R.id.buyer_deliver_order_recycler);

        emptyDeliveredOrderIV = view.findViewById(R.id.empty_buyer_delivered_icon);
        emptyDeliveredOrderTxt = view.findViewById(R.id.empty_buyer_delivered_text);
        emptyLayout = view.findViewById(R.id.empty_buyer_delivered_layout);

        sellerDeliveredOrdersSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sellerDeliveredOrdersSwipeLayout.setRefreshing(false);
            }
        });

        emptyDeliveredFragment();
        checkDeliveredOrders();

        return view;
    }

    void checkDeliveredOrders(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("BuyerDeliverOrders");
        Query queries = ref.child(currentOnlineBuyer);
        queries.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    sellerDeliveredOrdersSwipeLayout.setRefreshing(false);
                    emptyLayout.setVisibility(View.GONE);
                    emptyDeliveredOrderIV.setVisibility(View.GONE);
                    emptyDeliveredOrderTxt.setVisibility(View.GONE);
                    startBuyerDeliverOrdersRecycler();

                }
                else{
                    sellerDeliveredOrdersSwipeLayout.setRefreshing(false);
                    emptyLayout.setVisibility(View.GONE);
                    emptyDeliveredOrderIV.setVisibility(View.GONE);
                    emptyDeliveredOrderTxt.setVisibility(View.GONE);
                    emptyDeliveredFragment();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void emptyDeliveredFragment(){
        sellerDeliveredOrdersSwipeLayout.setRefreshing(false);
        emptyLayout.setVisibility(View.VISIBLE);
        emptyDeliveredOrderIV.setVisibility(View.VISIBLE);
        emptyDeliveredOrderTxt.setVisibility(View.VISIBLE);
    }

    void startBuyerDeliverOrdersRecycler(){

        sellerDeliveredOrderRecycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        sellerDeliveredOrderRecycler.setLayoutManager(layoutManager);

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(5)
                .setPageSize(10)
                .build();


        DatabasePagingOptions<SellersDeliverOrders> options = new DatabasePagingOptions.Builder<SellersDeliverOrders>()
                .setLifecycleOwner(this)
                .setQuery(sellerOrdersDeliveredRef, config, SellersDeliverOrders.class)
                .build();


        sellerOrdersAdapter = new FirebaseRecyclerPagingAdapter<SellersDeliverOrders, SellersDeliverOrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull SellersDeliverOrderViewHolder sellersDeliverOrderViewHolder, int i, @NonNull SellersDeliverOrders sellersDeliverOrders) {
//                itemTotalPrice = Integer.parseInt(sellersDeliverOrders.getDeliverOrderPrice()) * Integer.parseInt(sellersDeliverOrders.getDeliverOrderQty());
//                totalPrice += itemTotalPrice;

                sellersDeliverOrderViewHolder.sellerOrderPriceCV.setText(sellersDeliverOrders.getDeliverOrderPrice());
                sellersDeliverOrderViewHolder.sellerOrderQtyCV.setText(sellersDeliverOrders.getDeliverOrderQty());
                sellersDeliverOrderViewHolder.sellerOrderTitleCV.setText(sellersDeliverOrders.getDeliverOrderName());
                sellersDeliverOrderViewHolder.sellerOrderBuyerNameCV.setText(sellersDeliverOrders.getDeliverOrderSellerName());
                sellersDeliverOrderViewHolder.sellerOrderDateCV.setText(sellersDeliverOrders.getDeliverOrderDate());
                sellersDeliverOrderViewHolder.sellerOrderTypeCV.setText(sellersDeliverOrders.getDeliverOrderType());
                sellersDeliverOrderViewHolder.sellerOrderTotalPriceCV.setText("Total : " + sellersDeliverOrders.getDeliverOrderTotalPrice());
                sellersDeliverOrderViewHolder.sellerOrderStatusCV.setText("Delivered");
                Picasso.get().load(sellersDeliverOrders.getDeliverOrderImg()).placeholder(R.drawable.cart_logo).into(sellersDeliverOrderViewHolder.sellerOrderImageCV);
                sellersDeliverOrderViewHolder.sellerOrderShippingIDCV.setText(sellersDeliverOrders.getDeliverOrderShippingID());
                sellersDeliverOrderViewHolder.sellerOrderTrackingIDCV.setText(sellersDeliverOrders.getDeliverOrderID());
                sellersDeliverOrderViewHolder.sellerOrderPaymentCV.setText(sellersDeliverOrders.getDeliverOrderPaymentType());

                sellersDeliverOrderViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent sellerDeliverOrderProcess = new Intent(getActivity().getApplicationContext(), BuyerOrderDetailActivity.class);
                        Bundle sellerDeliverOrderBundle = new Bundle();
                        sellerDeliverOrderBundle.putString("orderID", sellersDeliverOrders.getDeliverOrderID());
                        sellerDeliverOrderBundle.putString("orderPrice", sellersDeliverOrders.getDeliverOrderPrice());
                        sellerDeliverOrderBundle.putString("orderQty", sellersDeliverOrders.getDeliverOrderQty());
                        sellerDeliverOrderBundle.putString("orderTitle", sellersDeliverOrders.getDeliverOrderName());
                        sellerDeliverOrderBundle.putString("orderSellerName", sellersDeliverOrders.getDeliverOrderSellerName());
                        sellerDeliverOrderBundle.putString("orderType", sellersDeliverOrders.getDeliverOrderType());
                        sellerDeliverOrderBundle.putString("orderImage", sellersDeliverOrders.getDeliverOrderImg());
                        sellerDeliverOrderBundle.putString("orderDate", sellersDeliverOrders.getDeliverOrderDate());
                        sellerDeliverOrderBundle.putString("orderShippingID", sellersDeliverOrders.getDeliverOrderShippingID());
                        sellerDeliverOrderBundle.putString("orderStatus", sellersDeliverOrders.getDeliverOrderStatus());
                        sellerDeliverOrderBundle.putString("paymentType", sellersDeliverOrders.getDeliverOrderPaymentType());
                        sellerDeliverOrderBundle.putString("activityType", "Delivered");
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
                        sellerDeliveredOrdersSwipeLayout.setRefreshing(true);
                        break;

                    case LOADED:
                        // Stop Animation
                        sellerDeliveredOrdersSwipeLayout.setRefreshing(false);
                        break;

                    case FINISHED:
                        //Reached end of Data set
                        sellerDeliveredOrdersSwipeLayout.setRefreshing(false);
                        break;

                    case ERROR:
                        retry();
                        break;
                }
            }

            @Override
            protected void onError(@NonNull DatabaseError databaseError) {
                sellerDeliveredOrdersSwipeLayout.setRefreshing(false);
                databaseError.toException().printStackTrace();
            }


            @NonNull
            @Override
            public SellersDeliverOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_orders_cv, parent, false);
                SellersDeliverOrderViewHolder holder = new SellersDeliverOrderViewHolder(view);
                return holder;
            }
        };

        sellerDeliveredOrdersSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sellerOrdersAdapter.refresh();
                sellerDeliveredOrdersSwipeLayout.setRefreshing(false);
            }
        });

        sellerDeliveredOrderRecycler.setAdapter(sellerOrdersAdapter);
        sellerOrdersAdapter.startListening();

    }
}