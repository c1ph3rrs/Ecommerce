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

import com.ciphers.ecommerce.Model.PlacedOrder;
import com.ciphers.ecommerce.Model.SellersOrders;
import com.ciphers.ecommerce.Prevalent.Prevalent;
import com.ciphers.ecommerce.R;
import com.ciphers.ecommerce.ViewHolder.PlacedOrderViewHolder;
import com.ciphers.ecommerce.ViewHolder.SellersOrdersViewHolder;
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
 * Use the {@link SellerApprovedOrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SellerApprovedOrderFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    SwipeRefreshLayout sellerApprovedOrdersSwipeLayout;
    RecyclerView sellerApprovedOrderRecycler;

    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerPagingAdapter<SellersOrders, SellersOrdersViewHolder> sellerOrdersAdapter;
    DatabaseReference sellerOrdersRef;
    String currentOnlineSeller;
    RelativeLayout emptyApproveLaout;

    ImageView emptyApproveOrderIcon;
    TextView emptyApproveOrderTxt;

    int itemTotalPrice =0, totalPrice = 0;

    public SellerApprovedOrderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SellerApprovedOrderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SellerApprovedOrderFragment newInstance(String param1, String param2) {
        SellerApprovedOrderFragment fragment = new SellerApprovedOrderFragment();
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

        View view = inflater.inflate(R.layout.fragment_seller_approved_order, container, false);

        currentOnlineSeller = Prevalent.currentOnlineSeller.getSellerUsername();

        sellerApprovedOrdersSwipeLayout = view.findViewById(R.id.seller_approved_orders_swipe_refresh_layout);
        sellerOrdersRef = FirebaseDatabase.getInstance().getReference().child("SellersOrders").child(currentOnlineSeller);
        sellerApprovedOrderRecycler = view.findViewById(R.id.seller_approved_order_recycler);

        emptyApproveOrderIcon = view.findViewById(R.id.empty_approve_icon);
        emptyApproveOrderTxt = view.findViewById(R.id.empty_approve_text);
        emptyApproveLaout = view.findViewById(R.id.seller_empty_approved_layout);

        emptyApprovedFragment();
        checkDeliveredOrders();

//        sellerApprovedOrdersSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                sellerApprovedOrdersSwipeLayout.setRefreshing(false);
//            }
//        });

        return view;
    }

    void checkDeliveredOrders(){

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("SellersOrders");
        Query queries = ref.child(currentOnlineSeller);
        queries.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    emptyApproveOrderIcon.setVisibility(View.GONE);
                    emptyApproveOrderTxt.setVisibility(View.GONE);
                    emptyApproveLaout.setVisibility(View.GONE);
                    sellerApprovedOrdersSwipeLayout.setRefreshing(true);
                    startSellerOrdersRecycler();

                }
                else{
                    sellerApprovedOrdersSwipeLayout.setRefreshing(false);
                    emptyApproveOrderIcon.setVisibility(View.GONE);
                    emptyApproveOrderTxt.setVisibility(View.GONE);
                    emptyApproveLaout.setVisibility(View.GONE);
                    emptyApprovedFragment();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    void emptyApprovedFragment(){
        emptyApproveLaout.setVisibility(View.VISIBLE);
        emptyApproveOrderTxt.setVisibility(View.VISIBLE);
        emptyApproveOrderIcon.setVisibility(View.VISIBLE);
        sellerApprovedOrdersSwipeLayout.setRefreshing(false);
    }

    void startSellerOrdersRecycler(){
        sellerApprovedOrderRecycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        sellerApprovedOrderRecycler.setLayoutManager(layoutManager);

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(5)
                .setPageSize(10)
                .build();

        DatabasePagingOptions<SellersOrders> options = new DatabasePagingOptions.Builder<SellersOrders>()
                .setLifecycleOwner(this)
                .setQuery(sellerOrdersRef, config, SellersOrders.class)
                .build();

        sellerOrdersAdapter = new FirebaseRecyclerPagingAdapter<SellersOrders, SellersOrdersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull SellersOrdersViewHolder sellersOrdersViewHolder, int i, @NonNull SellersOrders sellersOrders) {
                itemTotalPrice = Integer.parseInt(sellersOrders.getOrderPrice()) * Integer.parseInt(sellersOrders.getOrderQty());
                totalPrice += itemTotalPrice;

                sellersOrdersViewHolder.sellerOrderPriceCV.setText(sellersOrders.getOrderPrice());
                sellersOrdersViewHolder.sellerOrderQtyCV.setText(sellersOrders.getOrderQty());
                sellersOrdersViewHolder.sellerOrderTitleCV.setText(sellersOrders.getOrderName());
                sellersOrdersViewHolder.sellerOrderBuyerNameCV.setText(sellersOrders.getOrderBuyerName());
                sellersOrdersViewHolder.sellerOrderDateCV.setText(sellersOrders.getOrderDate());
                sellersOrdersViewHolder.sellerOrderTypeCV.setText(sellersOrders.getOrderType());
                sellersOrdersViewHolder.sellerOrderTotalPriceCV.setText("Total : " + String.valueOf(itemTotalPrice));
                sellersOrdersViewHolder.sellerOrderStatusCV.setText("Active");
                Picasso.get().load(sellersOrders.getOrderImg()).placeholder(R.drawable.cart_logo).into(sellersOrdersViewHolder.sellerOrderImageCV);
                sellersOrdersViewHolder.sellerOrderShippingIDCV.setText(sellersOrders.getOrderShippingID());
                sellersOrdersViewHolder.sellerOrderTrackingIDCV.setText(sellersOrders.getOrderID());
                sellersOrdersViewHolder.sellerOrderPaymentType.setText(sellersOrders.getOrderPaymentType());

                sellersOrdersViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent sellerDeliverOrderProcess = new Intent(getActivity().getApplicationContext(), SellerOrderDetailActivity.class);
                        Bundle sellerDeliverOrderBundle = new Bundle();
                        sellerDeliverOrderBundle.putString("orderID", sellersOrders.getOrderID());
                        sellerDeliverOrderBundle.putString("orderPrice", sellersOrders.getOrderPrice());
                        sellerDeliverOrderBundle.putString("orderQty", sellersOrders.getOrderQty());
                        sellerDeliverOrderBundle.putString("orderTitle", sellersOrders.getOrderName());
                        sellerDeliverOrderBundle.putString("orderSellerName", sellersOrders.getOrderSellerName());
                        sellerDeliverOrderBundle.putString("orderType", sellersOrders.getOrderType());
                        sellerDeliverOrderBundle.putString("orderImage", sellersOrders.getOrderImg());
                        sellerDeliverOrderBundle.putString("orderDate", sellersOrders.getOrderDate());
                        sellerDeliverOrderBundle.putString("orderShippingID", sellersOrders.getOrderShippingID());
                        sellerDeliverOrderBundle.putString("orderPaymentType", sellersOrders.getOrderPaymentType());
                        sellerDeliverOrderBundle.putString("activityType", "Approve Order");
                        sellerDeliverOrderBundle.putString("orderStatus", sellersOrders.getOrderStatus());
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
                        sellerApprovedOrdersSwipeLayout.setRefreshing(true);
                        break;

                    case LOADED:
                        // Stop Animation
                        sellerApprovedOrdersSwipeLayout.setRefreshing(false);
                        break;

                    case FINISHED:
                        //Reached end of Data set
                        sellerApprovedOrdersSwipeLayout.setRefreshing(false);
                        break;

                    case ERROR:
                        retry();
                        break;
                }
            }

            @Override
            protected void onError(@NonNull DatabaseError databaseError) {
                sellerApprovedOrdersSwipeLayout.setRefreshing(false);
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

        sellerApprovedOrdersSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sellerOrdersAdapter.refresh();
                sellerApprovedOrdersSwipeLayout.setRefreshing(false);
            }
        });

        sellerApprovedOrderRecycler.setAdapter(sellerOrdersAdapter);
        sellerOrdersAdapter.startListening();
    }
}
