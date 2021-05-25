package com.ciphers.ecommerce.Sellers;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ciphers.ecommerce.Model.SellersNotifications;
import com.ciphers.ecommerce.Model.SellersOrders;
import com.ciphers.ecommerce.Prevalent.Prevalent;
import com.ciphers.ecommerce.R;
import com.ciphers.ecommerce.ViewHolder.SellerNotificationsViewHolder;
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
 * Use the {@link SellerNotificationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SellerNotificationsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    DatabaseReference sellerNotificationRef;

    SwipeRefreshLayout sellerNotificationSwipeLayout;
    RecyclerView sellerNotificationRecycler;

    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerPagingAdapter<SellersNotifications, SellerNotificationsViewHolder> sellerNotificationAdapter;

    public SellerNotificationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SellerNotificationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SellerNotificationsFragment newInstance(String param1, String param2) {
        SellerNotificationsFragment fragment = new SellerNotificationsFragment();
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
        View view = inflater.inflate(R.layout.fragment_seller_notifications, container, false);

        sellerNotificationRef = FirebaseDatabase.getInstance().getReference()
                .child("SellersNotifications").child(Prevalent.currentOnlineSeller.getSellerUsername());

        sellerNotificationSwipeLayout = view.findViewById(R.id.notification_refresh_layout);
        sellerNotificationRecycler = view.findViewById(R.id.seller_notifications_recycler);

        checkNotifications();

        return view;
    }

    void checkNotifications(){

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("SellersNotifications");
        Query queries = ref.child(Prevalent.currentOnlineSeller.getSellerUsername());
        queries.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
//                    emptyApproveOrderIcon.setVisibility(View.GONE);
//                    emptyApproveOrderTxt.setVisibility(View.GONE);
//                    emptyApproveLaout.setVisibility(View.GONE);
//                    sellerApprovedOrdersSwipeLayout.setRefreshing(false);
                    startSellerOrdersRecycler();

                }
                else{
//                    sellerApprovedOrdersSwipeLayout.setRefreshing(false);
//                    emptyApproveOrderIcon.setVisibility(View.GONE);
//                    emptyApproveOrderTxt.setVisibility(View.GONE);
//                    emptyApproveLaout.setVisibility(View.GONE);
//                    emptyApprovedFragment();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void startSellerOrdersRecycler() {

        sellerNotificationRecycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        sellerNotificationRecycler.setLayoutManager(layoutManager);

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(5)
                .setPageSize(10)
                .build();


        DatabasePagingOptions<SellersNotifications> options = new DatabasePagingOptions.Builder<SellersNotifications>()
                .setLifecycleOwner(this)
                .setQuery(sellerNotificationRef, config, SellersNotifications.class)
                .build();


        sellerNotificationAdapter = new FirebaseRecyclerPagingAdapter<SellersNotifications, SellerNotificationsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull SellerNotificationsViewHolder sellerNotificationsViewHolder, int i, @NonNull SellersNotifications sellersNotifications) {

                sellerNotificationsViewHolder.notificationSellerTitle.setText(sellersNotifications.getNotificationSellerTitle());
                sellerNotificationsViewHolder.notificationSellerDescription.setText("Order ID : " + sellersNotifications.getNotificationSellerKey());
                sellerNotificationsViewHolder.notificationSellerDate.setText(sellersNotifications.getNotificationSellerDate());
                sellerNotificationsViewHolder.notificationType.setText(sellersNotifications.getNotificationSellerType());

                Picasso.get().load(sellersNotifications.getNotificationSellerImg()).placeholder(R.drawable.cart_logo).into(sellerNotificationsViewHolder.notificationSellerImg);

            }

            @Override
            protected void onLoadingStateChanged(@NonNull LoadingState state) {
                switch (state) {
                    case LOADING_INITIAL:
                    case LOADING_MORE:
                        // Do your loading animation
                        sellerNotificationSwipeLayout.setRefreshing(true);
                        break;

                    case LOADED:
                        // Stop Animation
                        sellerNotificationSwipeLayout.setRefreshing(false);
                        break;

                    case FINISHED:
                        //Reached end of Data set
                        sellerNotificationSwipeLayout.setRefreshing(false);
                        break;

                    case ERROR:
                        retry();
                        break;
                }
            }

            @Override
            protected void onError(@NonNull DatabaseError databaseError) {
                sellerNotificationSwipeLayout.setRefreshing(false);
                databaseError.toException().printStackTrace();
            }


            @NonNull
            @Override
            public SellerNotificationsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_card_view, parent, false);
                SellerNotificationsViewHolder holder = new SellerNotificationsViewHolder(view);
                return holder;
            }
        };

        sellerNotificationSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sellerNotificationAdapter.refresh();
                sellerNotificationSwipeLayout.setRefreshing(false);
            }
        });

        sellerNotificationRecycler.setAdapter(sellerNotificationAdapter);
        sellerNotificationAdapter.startListening();

    }
}