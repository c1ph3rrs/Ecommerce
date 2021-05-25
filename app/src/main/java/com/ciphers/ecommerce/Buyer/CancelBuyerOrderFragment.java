package com.ciphers.ecommerce.Buyer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ciphers.ecommerce.Model.ActiveOrders;
import com.ciphers.ecommerce.Model.RejectOrders;
import com.ciphers.ecommerce.Prevalent.Prevalent;
import com.ciphers.ecommerce.R;
import com.ciphers.ecommerce.ViewHolder.ActiveOrdersViewHolder;
import com.ciphers.ecommerce.ViewHolder.RejectOrdersViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CancelBuyerOrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CancelBuyerOrderFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    RecyclerView rejectOrderRecycler;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<RejectOrders, RejectOrdersViewHolder> rejectAdapter;
    DatabaseReference rejectRef;
    String currentOnlineUser;
    int itemTotalPrice =0, totalPrice = 0;
    SwipeRefreshLayout rejectOrderSwipeRefresh;


    public CancelBuyerOrderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CancelFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CancelBuyerOrderFragment newInstance(String param1, String param2) {
        CancelBuyerOrderFragment fragment = new CancelBuyerOrderFragment();
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

        View view = inflater.inflate(R.layout.fragment_buyer_order_cancel, container, false);

        currentOnlineUser = Prevalent.currentOnlineUser.getUsername();

        rejectOrderRecycler = view.findViewById(R.id.buyer_order_cancel_recycler);
        rejectOrderSwipeRefresh = view.findViewById(R.id.buyer_orders_cancel_swipe_refresh_layout);

        rejectRef = FirebaseDatabase.getInstance().getReference().child("RejectOrders").child(currentOnlineUser);

        checkRejectOrders();
//        rejectOrderFetchData();
        return view;
    }

    void checkRejectOrders(){

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("RejectOrders");
        Query queries = ref.child(currentOnlineUser);
        queries.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    rejectOrderFetchData();
                }
                else{
                    Toast.makeText(getActivity(),"No data exists",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    void rejectOrderFetchData(){
        rejectOrderRecycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rejectOrderRecycler.setLayoutManager(layoutManager);

        FirebaseRecyclerOptions<RejectOrders> options =
                new FirebaseRecyclerOptions.Builder<RejectOrders>().setQuery(rejectRef, RejectOrders.class).build();

        rejectAdapter = new FirebaseRecyclerAdapter<RejectOrders, RejectOrdersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull RejectOrdersViewHolder rejectOrdersViewHolder, int i, @NonNull RejectOrders rejectOrders) {

                itemTotalPrice = Integer.parseInt(rejectOrders.getOrderPrice()) * Integer.parseInt(rejectOrders.getOrderQty());
                totalPrice += itemTotalPrice;
                rejectOrdersViewHolder.cartProductNameCV.setText(rejectOrders.getOrderName());
                rejectOrdersViewHolder.cartProductPriceCV.setText(rejectOrders.getOrderPrice());
                rejectOrdersViewHolder.cartProductQtyCV.setText(rejectOrders.getOrderQty());
                rejectOrdersViewHolder.cartProductSellerNameCV.setText(rejectOrders.getOrderSellerName());
                rejectOrdersViewHolder.cartProductDateCV.setText(rejectOrders.getOrderDate());
                rejectOrdersViewHolder.cartProductTypeCV.setText(rejectOrders.getOrderType());
                rejectOrdersViewHolder.cartProductTotalPriceCV.setText("Total : " + String.valueOf(itemTotalPrice));
                rejectOrdersViewHolder.cartProductStatus.setText("Canceled");
                rejectOrdersViewHolder.cartProductRejectMessageCV.setVisibility(View.VISIBLE);
                rejectOrdersViewHolder.cartProductRejectMessageCV.setText("Reason : " + rejectOrders.getRejectMessage());

                Picasso.get().load(rejectOrders.getOrderImg()).placeholder(R.drawable.cart_logo).into(rejectOrdersViewHolder.cartProductImageCV);
            }

            @NonNull
            @Override
            public RejectOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_card_view, parent, false);
                RejectOrdersViewHolder holder = new RejectOrdersViewHolder(view);
                return holder;
            }
        };

        rejectOrderRecycler.setAdapter(rejectAdapter);
        rejectAdapter.startListening();
    }

}