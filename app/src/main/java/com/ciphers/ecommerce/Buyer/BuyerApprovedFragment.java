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
import android.widget.Toast;

import com.ciphers.ecommerce.Model.PlacedOrder;
import com.ciphers.ecommerce.Model.RejectOrders;
import com.ciphers.ecommerce.Model.SellersOrders;
import com.ciphers.ecommerce.Prevalent.Prevalent;
import com.ciphers.ecommerce.R;
import com.ciphers.ecommerce.Sellers.SellerOrderDetailActivity;
import com.ciphers.ecommerce.ViewHolder.PlacedOrderViewHolder;
import com.ciphers.ecommerce.ViewHolder.RejectOrdersViewHolder;
import com.ciphers.ecommerce.ViewHolder.SellersOrdersViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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
 * Use the {@link BuyerApprovedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BuyerApprovedFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView approveOrderRecycler;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerPagingAdapter<SellersOrders, SellersOrdersViewHolder> approveAdapter;
    DatabaseReference approveRef;
    String currentOnlineUser;
    int itemTotalPrice =0, totalPrice = 0;
    SwipeRefreshLayout approveOrderSwipeRefresh;

    TextView emptyApproveTxt;
    ImageView emptyApproveImg;
    RelativeLayout emptyApproveLayout;

    public BuyerApprovedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ApprovedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BuyerApprovedFragment newInstance(String param1, String param2) {
        BuyerApprovedFragment fragment = new BuyerApprovedFragment();
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
        View view = inflater.inflate(R.layout.fragment_buyer_approved, container, false);

        currentOnlineUser = Prevalent.currentOnlineUser.getUsername();

        approveOrderRecycler = view.findViewById(R.id.buyer_order_approve_recycler);
        approveOrderSwipeRefresh = view.findViewById(R.id.buyer_orders_approve_swipe_refresh_layout);
        emptyApproveTxt = view.findViewById(R.id.empty_approve_text);
        emptyApproveImg = view.findViewById(R.id.empty_approve_icon);
        emptyApproveLayout = view.findViewById(R.id.empty_approve_layout);

        approveRef = FirebaseDatabase.getInstance().getReference().child("BuyerOrders").child(currentOnlineUser);

        emptyApproveOrder();
        checkApproveOrders();

        approveOrderSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                approveOrderSwipeRefresh.setRefreshing(false);
            }
        });

        return view;
    }

    void checkApproveOrders(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("BuyerOrders");
        Query queries = ref.child(currentOnlineUser);
        queries.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    emptyApproveLayout.setVisibility(View.GONE);
                    emptyApproveImg.setVisibility(View.GONE);
                    emptyApproveTxt.setVisibility(View.GONE);
                    approveOrderSwipeRefresh.setRefreshing(false);
                    approveOrderFetchData();
                }
                else{
                    emptyApproveLayout.setVisibility(View.GONE);
                    emptyApproveImg.setVisibility(View.GONE);
                    emptyApproveTxt.setVisibility(View.GONE);
                    approveOrderSwipeRefresh.setRefreshing(false);
                    emptyApproveOrder();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void emptyApproveOrder(){
        emptyApproveImg.setVisibility(View.VISIBLE);
        emptyApproveTxt.setVisibility(View.VISIBLE);
    }

    void approveOrderFetchData(){
        approveOrderRecycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        approveOrderRecycler.setLayoutManager(layoutManager);

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(5)
                .setPageSize(10)
                .build();


        DatabasePagingOptions<SellersOrders> options = new DatabasePagingOptions.Builder<SellersOrders>()
                .setLifecycleOwner(this)
                .setQuery(approveRef, config, SellersOrders.class)
                .build();


        approveAdapter = new FirebaseRecyclerPagingAdapter<SellersOrders, SellersOrdersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull SellersOrdersViewHolder sellersOrdersViewHolder, int i, @NonNull SellersOrders sellersOrders) {
                itemTotalPrice = Integer.parseInt(sellersOrders.getOrderPrice()) * Integer.parseInt(sellersOrders.getOrderQty());
                totalPrice += itemTotalPrice;


                sellersOrdersViewHolder.sellerOrderPriceCV.setText(sellersOrders.getOrderPrice());
                sellersOrdersViewHolder.sellerOrderQtyCV.setText(sellersOrders.getOrderQty());
                sellersOrdersViewHolder.sellerOrderTitleCV.setText(sellersOrders.getOrderName());
                sellersOrdersViewHolder.sellerOrderBuyerNameCV.setText(sellersOrders.getOrderSellerName());
                sellersOrdersViewHolder.sellerOrderDateCV.setText(sellersOrders.getOrderDate());
                sellersOrdersViewHolder.sellerOrderTypeCV.setText(sellersOrders.getOrderType());
                sellersOrdersViewHolder.sellerOrderTotalPriceCV.setText("Total : " + String.valueOf(itemTotalPrice));
                sellersOrdersViewHolder.sellerOrderStatusCV.setText("Approved");
                Picasso.get().load(sellersOrders.getOrderImg()).placeholder(R.drawable.cart_logo).into(sellersOrdersViewHolder.sellerOrderImageCV);
                sellersOrdersViewHolder.sellerOrderShippingIDCV.setText(sellersOrders.getOrderShippingID());
                sellersOrdersViewHolder.sellerOrderPaymentType.setText(sellersOrders.getOrderPaymentType());
                sellersOrdersViewHolder.sellerOrderTrackingIDCV.setText(sellersOrders.getOrderID());
//                sellersOrdersViewHolder.sellerOrderPaymentType.setText(sellersOrders.get);


//                placedOrderViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        Intent sellerDeliverOrderProcess = new Intent(getActivity().getApplicationContext(), SellerOrderDetailActivity.class);
//                        Bundle sellerDeliverOrderBundle = new Bundle();
//                        sellerDeliverOrderBundle.putString("orderID", placedOrder.getProductOrderID());
//                        sellerDeliverOrderBundle.putString("orderPrice", placedOrder.getProductPrice());
//                        sellerDeliverOrderBundle.putString("orderQty", placedOrder.getProductQty());
//                        sellerDeliverOrderBundle.putString("orderTitle", placedOrder.getProductName());
//                        sellerDeliverOrderBundle.putString("orderSellerName", placedOrder.getProductSellerName());
//                        sellerDeliverOrderBundle.putString("orderType", placedOrder.getProductType());
//                        sellerDeliverOrderBundle.putString("orderImage", placedOrder.getProductImg());
//                        sellerDeliverOrderBundle.putString("orderDate", placedOrder.getProductDate());
//                        sellerDeliverOrderBundle.putString("orderShippingID", placedOrder.getOrderShippingID());
//                        sellerDeliverOrderBundle.putString("activityType", "Pending");
//                        sellerDeliverOrderProcess.putExtras(sellerDeliverOrderBundle);
//                        startActivity(sellerDeliverOrderProcess);
//                    }
//                });

            }

            @Override
            protected void onLoadingStateChanged(@NonNull LoadingState state) {
                switch (state) {
                    case LOADING_INITIAL:
                    case LOADING_MORE:
                        // Do your loading animation
                        approveOrderSwipeRefresh.setRefreshing(true);
                        break;

                    case LOADED:
                        // Stop Animation
                        approveOrderSwipeRefresh.setRefreshing(false);
                        break;

                    case FINISHED:
                        //Reached end of Data set
                        approveOrderSwipeRefresh.setRefreshing(false);
                        break;

                    case ERROR:
                        retry();
                        break;
                }
            }

            @Override
            protected void onError(@NonNull DatabaseError databaseError) {
                approveOrderSwipeRefresh.setRefreshing(false);
                databaseError.toException().printStackTrace();
            }


            @NonNull
            @Override
            public SellersOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_orders_cv, parent, false);
                SellersOrdersViewHolder holder = new SellersOrdersViewHolder(view);
                return holder;
            }
        };


        approveOrderSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                approveAdapter.refresh();
            }
        });

        approveOrderRecycler.setAdapter(approveAdapter);
        approveAdapter.startListening();
    }
}