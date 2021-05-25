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

import com.ciphers.ecommerce.Model.SellersDeliverOrders;
import com.ciphers.ecommerce.Model.SellersOrders;
import com.ciphers.ecommerce.Prevalent.Prevalent;
import com.ciphers.ecommerce.R;
import com.ciphers.ecommerce.ViewHolder.SellersDeliverOrderViewHolder;
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
 * Use the {@link SellerDeliveredFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SellerDeliveredFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    SwipeRefreshLayout sellerDeliveredOrdersSwipeLayout;
    RecyclerView sellerDeliveredOrderRecycler;
    RecyclerView.LayoutManager layoutManager;
    DatabaseReference sellerOrdersDeliveredRef;
    FirebaseRecyclerPagingAdapter<SellersDeliverOrders, SellersDeliverOrderViewHolder> sellerOrdersAdapter;
    ImageView emptyDeliveredIcon;
    TextView emptyDeliveredTxt;
    RelativeLayout emptyLayout;

//    FirebaseRecyclerPagingAdapter<SellersOrders, SellersOrdersViewHolder> sellerOrdersAdapter;
    String currentOnlineSeller;
    int itemTotalPrice =0, totalPrice = 0;

    public SellerDeliveredFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SellerDeliveredFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SellerDeliveredFragment newInstance(String param1, String param2) {
        SellerDeliveredFragment fragment = new SellerDeliveredFragment();
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
        View view = inflater.inflate(R.layout.fragment_seller_delivered, container, false);

        currentOnlineSeller = Prevalent.currentOnlineSeller.getSellerUsername();

        sellerDeliveredOrdersSwipeLayout = view.findViewById(R.id.seller_delivered_orders_swipe_refresh_layout);
        sellerOrdersDeliveredRef = FirebaseDatabase.getInstance().getReference().child("SellersDeliverOrders").child(currentOnlineSeller);
        sellerDeliveredOrderRecycler = view.findViewById(R.id.seller_delivered_order_recycler);

        emptyDeliveredIcon = view.findViewById(R.id.empty_delivered_icon);
        emptyDeliveredTxt = view.findViewById(R.id.empty_delivered_text);
        emptyLayout = view.findViewById(R.id.seller_empty_delivered_layout);

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

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("SellersDeliverOrders");
        Query queries = ref.child(currentOnlineSeller);
        queries.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    sellerDeliveredOrdersSwipeLayout.setRefreshing(false);
                    emptyDeliveredIcon.setVisibility(View.GONE);
                    emptyDeliveredTxt.setVisibility(View.GONE);
                    emptyLayout.setVisibility(View.GONE);
                    startSellerDeliverOrdersRecycler();

                }
                else{
                    sellerDeliveredOrdersSwipeLayout.setRefreshing(false);
                    emptyDeliveredIcon.setVisibility(View.GONE);
                    emptyDeliveredTxt.setVisibility(View.GONE);
                    emptyLayout.setVisibility(View.GONE);
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
        emptyDeliveredIcon.setVisibility(View.VISIBLE);
        emptyDeliveredTxt.setVisibility(View.VISIBLE);
    }

    void startSellerDeliverOrdersRecycler(){
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
                itemTotalPrice = Integer.parseInt(sellersDeliverOrders.getDeliverOrderPrice()) * Integer.parseInt(sellersDeliverOrders.getDeliverOrderQty());
                totalPrice += itemTotalPrice;

                sellersDeliverOrderViewHolder.sellerOrderPriceCV.setText(sellersDeliverOrders.getDeliverOrderPrice());
                sellersDeliverOrderViewHolder.sellerOrderQtyCV.setText(sellersDeliverOrders.getDeliverOrderQty());
                sellersDeliverOrderViewHolder.sellerOrderTitleCV.setText(sellersDeliverOrders.getDeliverOrderName());
                sellersDeliverOrderViewHolder.sellerOrderBuyerNameCV.setText(sellersDeliverOrders.getDeliverOrderBuyerName());
                sellersDeliverOrderViewHolder.sellerOrderDateCV.setText(sellersDeliverOrders.getDeliverOrderDate());
                sellersDeliverOrderViewHolder.sellerOrderTypeCV.setText(sellersDeliverOrders.getDeliverOrderType());
                sellersDeliverOrderViewHolder.sellerOrderTotalPriceCV.setText("Total : " + String.valueOf(itemTotalPrice));
                sellersDeliverOrderViewHolder.sellerOrderStatusCV.setText("Active");
                Picasso.get().load(sellersDeliverOrders.getDeliverOrderImg()).placeholder(R.drawable.cart_logo).into(sellersDeliverOrderViewHolder.sellerOrderImageCV);
                sellersDeliverOrderViewHolder.sellerOrderShippingIDCV.setText(sellersDeliverOrders.getDeliverOrderShippingID());
                sellersDeliverOrderViewHolder.sellerOrderTrackingIDCV.setText(sellersDeliverOrders.getDeliverOrderID());
                sellersDeliverOrderViewHolder.sellerOrderPaymentCV.setText(sellersDeliverOrders.getDeliverOrderPaymentType());


                sellersDeliverOrderViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent sellerDeliverOrderProcess = new Intent(getActivity().getApplicationContext(), SellerOrderDetailActivity.class);
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
                        sellerDeliverOrderBundle.putString("orderPaymentType", sellersDeliverOrders.getDeliverOrderPaymentType());
                        sellerDeliverOrderBundle.putString("activityType", "Deliver Order");
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