package com.ciphers.ecommerce.Buyer;

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

import com.ciphers.ecommerce.Model.ActiveOrders;
import com.ciphers.ecommerce.Model.SellersOrders;
import com.ciphers.ecommerce.Prevalent.Prevalent;
import com.ciphers.ecommerce.R;
import com.ciphers.ecommerce.ViewHolder.ActiveOrdersViewHolder;
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
 * Use the {@link InProgressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InProgressFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    RecyclerView orderInProgressRecycler;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerPagingAdapter<ActiveOrders, ActiveOrdersViewHolder> orderAdapter;
    DatabaseReference orderRef;
    String currentOnlineUser;
    int itemTotalPrice =0, totalPrice = 0;
    SwipeRefreshLayout pendingOrderSwipeRefresh;

    TextView emptyPendingOrdersTxt;
    ImageView emptyPendingOrdersIV;
    RelativeLayout pendingOrdersLayout;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public InProgressFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InProgressFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InProgressFragment newInstance(String param1, String param2) {
        InProgressFragment fragment = new InProgressFragment();
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
        View view = inflater.inflate(R.layout.fragment_in_progress, container, false);

        currentOnlineUser = Prevalent.currentOnlineUser.getUsername();
        orderRef = FirebaseDatabase.getInstance().getReference().child("ActiveOrders").child(currentOnlineUser);

        pendingOrdersLayout = view.findViewById(R.id.buyer_pending_orders_layout);

        orderInProgressRecycler = view.findViewById(R.id.buyer_order_in_progress_recycler);
        pendingOrderSwipeRefresh = view.findViewById(R.id.buyer_orders_pending_swipe_refresh_layout);
        emptyPendingOrdersTxt = view.findViewById(R.id.empty_pending_order_text);
        emptyPendingOrdersIV = view.findViewById(R.id.empty_pending_orders_icon);

        checkPendingOrders();

        pendingOrderSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pendingOrderSwipeRefresh.setRefreshing(false);
            }
        });

        return view;
    }

    private void checkPendingOrders(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("ActiveOrders");
        Query queries = ref.child(currentOnlineUser);
        queries.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    pendingOrderSwipeRefresh.setRefreshing(false);
                    pendingOrdersLayout.setVisibility(View.VISIBLE);
                    emptyPendingOrdersTxt.setVisibility(View.GONE);
                    emptyPendingOrdersIV.setVisibility(View.GONE);
                    startInProgressOrders();
                }
                else{
                    pendingOrderSwipeRefresh.setRefreshing(false);
                    pendingOrdersLayout.setVisibility(View.GONE);
                    emptyPendingOrdersTxt.setVisibility(View.VISIBLE);
                    emptyPendingOrdersIV.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void startInProgressOrders(){
        orderInProgressRecycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        orderInProgressRecycler.setLayoutManager(layoutManager);

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(5)
                .setPageSize(10)
                .build();


        DatabasePagingOptions<ActiveOrders> options = new DatabasePagingOptions.Builder<ActiveOrders>()
                .setLifecycleOwner(this)
                .setQuery(orderRef, config, ActiveOrders.class)
                .build();


        orderAdapter = new FirebaseRecyclerPagingAdapter<ActiveOrders, ActiveOrdersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ActiveOrdersViewHolder activeOrdersViewHolder, int i, @NonNull ActiveOrders activeOrders) {
                itemTotalPrice = Integer.parseInt(activeOrders.getProductPrice()) * Integer.parseInt(activeOrders.getProductQuantity());
                totalPrice += itemTotalPrice;
                activeOrdersViewHolder.cartProductNameCV.setText(activeOrders.getProductName());
                activeOrdersViewHolder.cartProductPriceCV.setText(activeOrders.getProductPrice());
                activeOrdersViewHolder.cartProductQtyCV.setText(activeOrders.getProductQuantity());
                activeOrdersViewHolder.cartProductSellerNameCV.setText(activeOrders.getProductSeller());
                activeOrdersViewHolder.cartProductDateCV.setText(activeOrders.getProductDate());
                activeOrdersViewHolder.cartProductTypeCV.setText(activeOrders.getProductType());
                activeOrdersViewHolder.cartProductTotalPriceCV.setText("Total : " + String.valueOf(itemTotalPrice));
                activeOrdersViewHolder.cartProductStatus.setText("Pending");

                Picasso.get().load(activeOrders.getProductImg()).placeholder(R.drawable.cart_logo).into(activeOrdersViewHolder.cartProductImageCV);
            }

            @Override
            protected void onLoadingStateChanged(@NonNull LoadingState state) {
                switch (state) {
                    case LOADING_INITIAL:
                    case LOADING_MORE:
                        // Do your loading animation
                        pendingOrderSwipeRefresh.setRefreshing(true);
                        break;

                    case LOADED:
                        // Stop Animation
                        pendingOrderSwipeRefresh.setRefreshing(false);
                        break;

                    case FINISHED:
                        //Reached end of Data set
                        pendingOrderSwipeRefresh.setRefreshing(false);
                        break;

                    case ERROR:
                        retry();
                        break;
                }
            }

            @Override
            protected void onError(@NonNull DatabaseError databaseError) {
                pendingOrderSwipeRefresh.setRefreshing(false);
                databaseError.toException().printStackTrace();
            }

            @NonNull
            @Override
            public ActiveOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_card_view, parent, false);
                ActiveOrdersViewHolder holder = new ActiveOrdersViewHolder(view);
                return holder;
            }
        };

        pendingOrderSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                orderAdapter.refresh();
            }
        });

        orderInProgressRecycler.setAdapter(orderAdapter);
        orderAdapter.startListening();
    }

}