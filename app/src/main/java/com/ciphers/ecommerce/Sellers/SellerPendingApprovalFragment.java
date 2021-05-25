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

import com.ciphers.ecommerce.Model.PlacedOrder;
import com.ciphers.ecommerce.Prevalent.Prevalent;
import com.ciphers.ecommerce.R;
import com.ciphers.ecommerce.ViewHolder.PlacedOrderViewHolder;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shreyaspatil.firebase.recyclerpagination.DatabasePagingOptions;
import com.shreyaspatil.firebase.recyclerpagination.FirebaseRecyclerPagingAdapter;
import com.shreyaspatil.firebase.recyclerpagination.LoadingState;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SellerPendingApprovalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SellerPendingApprovalFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView ordersSellerRecycler;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerPagingAdapter<PlacedOrder, PlacedOrderViewHolder> placedOrdersAdapter;
    DatabaseReference placedOrderRef;
    String currentOnlineSeller;
    int itemTotalPrice =0, totalPrice = 0;

    SwipeRefreshLayout placeOrderRefreshLayout;


    public SellerPendingApprovalFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SellerOrderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SellerPendingApprovalFragment newInstance(String param1, String param2) {
        SellerPendingApprovalFragment fragment = new SellerPendingApprovalFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_seller_pending_approval, container, false);

        currentOnlineSeller = Prevalent.currentOnlineSeller.getSellerUsername();

        placeOrderRefreshLayout = view.findViewById(R.id.seller_orders_swipe_refresh_layout);
        placedOrderRef = FirebaseDatabase.getInstance().getReference().child("PlacedOrder").child(currentOnlineSeller);
        ordersSellerRecycler = view.findViewById(R.id.seller_order_recycler);

        startSellerOrdersRecycler();

        return view;

    }

    void startSellerOrdersRecycler(){
        ordersSellerRecycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        ordersSellerRecycler.setLayoutManager(layoutManager);

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(5)
                .setPageSize(10)
                .build();


        DatabasePagingOptions<PlacedOrder> options = new DatabasePagingOptions.Builder<PlacedOrder>()
                .setLifecycleOwner(this)
                .setQuery(placedOrderRef, config, PlacedOrder.class)
                .build();


        placedOrdersAdapter = new FirebaseRecyclerPagingAdapter<PlacedOrder, PlacedOrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull PlacedOrderViewHolder placedOrderViewHolder, int i, @NonNull PlacedOrder placedOrder) {
                itemTotalPrice = Integer.parseInt(placedOrder.getProductPrice()) * Integer.parseInt(placedOrder.getProductQty());
                totalPrice += itemTotalPrice;


                placedOrderViewHolder.sellerOrderPriceCV.setText(placedOrder.getProductPrice());
                placedOrderViewHolder.sellerOrderQtyCV.setText(placedOrder.getProductQty());
                placedOrderViewHolder.sellerOrderTitleCV.setText(placedOrder.getProductName());
                placedOrderViewHolder.sellerOrderBuyerNameCV.setText(placedOrder.getProductSellerName());
                placedOrderViewHolder.sellerOrderDateCV.setText(placedOrder.getProductDate());
                placedOrderViewHolder.sellerOrderTypeCV.setText(placedOrder.getProductType());
                placedOrderViewHolder.sellerOrderTotalPriceCV.setText("Total : " + String.valueOf(itemTotalPrice));
                placedOrderViewHolder.sellerOrderStatusCV.setText("Pending");
                Picasso.get().load(placedOrder.getProductImg()).placeholder(R.drawable.cart_logo).into(placedOrderViewHolder.sellerOrderImageCV);
                placedOrderViewHolder.sellerOrderShippingIDCV.setText("S. ID : " + placedOrder.getOrderShippingID());
                placedOrderViewHolder.sellerOrderTrackingIDCV.setText("T. ID : " + placedOrder.getProductOrderID());
                placedOrderViewHolder.sellerOrderPaymentType.setText(placedOrder.getProductOrderPayment());

                placedOrderViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent sellerDeliverOrderProcess = new Intent(getActivity().getApplicationContext(), SellerOrderDetailActivity.class);
                        Bundle sellerDeliverOrderBundle = new Bundle();
                        sellerDeliverOrderBundle.putString("orderID", placedOrder.getProductOrderID());
                        sellerDeliverOrderBundle.putString("orderPrice", placedOrder.getProductPrice());
                        sellerDeliverOrderBundle.putString("orderQty", placedOrder.getProductQty());
                        sellerDeliverOrderBundle.putString("orderTitle", placedOrder.getProductName());
                        sellerDeliverOrderBundle.putString("orderSellerName", placedOrder.getProductSellerName());
                        sellerDeliverOrderBundle.putString("orderType", placedOrder.getProductType());
                        sellerDeliverOrderBundle.putString("orderImage", placedOrder.getProductImg());
                        sellerDeliverOrderBundle.putString("orderDate", placedOrder.getProductDate());
                        sellerDeliverOrderBundle.putString("orderShippingID", placedOrder.getOrderShippingID());
                        sellerDeliverOrderBundle.putString("orderPaymentType", placedOrder.getProductOrderPayment());
                        sellerDeliverOrderBundle.putString("activityType", "Pending");
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
                        placeOrderRefreshLayout.setRefreshing(true);
                        break;

                    case LOADED:
                        // Stop Animation
                        placeOrderRefreshLayout.setRefreshing(false);
                        break;

                    case FINISHED:
                        //Reached end of Data set
                        placeOrderRefreshLayout.setRefreshing(false);
                        break;

                    case ERROR:
                        retry();
                        break;
                }
            }

            @Override
            protected void onError(@NonNull DatabaseError databaseError) {
                placeOrderRefreshLayout.setRefreshing(false);
                databaseError.toException().printStackTrace();
            }


            @NonNull
            @Override
            public PlacedOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_orders_cv, parent, false);
                PlacedOrderViewHolder holder = new PlacedOrderViewHolder(view);
                return holder;
            }
        };

        placeOrderRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                placedOrdersAdapter.refresh();
            }
        });

        ordersSellerRecycler.setAdapter(placedOrdersAdapter);
        placedOrdersAdapter.startListening();
    }

}