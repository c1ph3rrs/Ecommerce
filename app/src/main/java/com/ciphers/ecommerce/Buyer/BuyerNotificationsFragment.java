package com.ciphers.ecommerce.Buyer;

import android.graphics.Color;
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
import android.widget.Toast;

import com.ciphers.ecommerce.Helper.WishListSwipeHelper;
import com.ciphers.ecommerce.Interface.MyButtonClickListener;
import com.ciphers.ecommerce.Model.BuyerNotifications;
import com.ciphers.ecommerce.Model.SellersNotifications;
import com.ciphers.ecommerce.Prevalent.Prevalent;
import com.ciphers.ecommerce.R;
import com.ciphers.ecommerce.ViewHolder.BuyerNotificationsViewHolder;
import com.ciphers.ecommerce.ViewHolder.SellerNotificationsViewHolder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BuyerNotificationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BuyerNotificationsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    DatabaseReference buyerNotificationRef;

    SwipeRefreshLayout buyerNotificationSwipeLayout;
    RecyclerView buyerNotificationRecycler;

    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerPagingAdapter<BuyerNotifications, BuyerNotificationsViewHolder> buyerNotificationAdapter;
    ArrayList<String> clickList = new ArrayList<>();
    WishListSwipeHelper notificationSwipeHelper;

    public BuyerNotificationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BuyerNotificationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BuyerNotificationsFragment newInstance(String param1, String param2) {
        BuyerNotificationsFragment fragment = new BuyerNotificationsFragment();
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
        View view = inflater.inflate(R.layout.fragment_buyer_notifications, container, false);

        buyerNotificationRef = FirebaseDatabase.getInstance().getReference()
                .child("BuyerNotifications").child(Prevalent.currentOnlineUser.getUsername());

        buyerNotificationSwipeLayout = view.findViewById(R.id.buyer_notification_refresh_layout);
        buyerNotificationRecycler = view.findViewById(R.id.buyer_notifications_recycler);

        notificationSwipeHelper = new WishListSwipeHelper(getActivity(), buyerNotificationRecycler, 150) {
            @Override
            public void initiateMyButtons(RecyclerView.ViewHolder viewHolder, List<MyButtons> buffer) {
                buffer.add(new MyButtons(getActivity(),
                        "Delete",
                        30,
                        R.drawable.bin_white,
                        Color.parseColor("#FF3C30"),
                        new MyButtonClickListener() {
                            @Override
                            public void onClick(int pos) {

                                for(int i=0; i < clickList.size() +1; i ++){

                                    if(i == pos){

                                        buyerNotificationRef.child(clickList.get(i)).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                buyerNotificationAdapter.refresh();
                                            }
                                        });
                                    }
                                }
                            }
                        }));
            }
        };

        checkNotifications();

        return view;
    }

    void checkNotifications(){

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("BuyerNotifications");
        Query queries = ref.child(Prevalent.currentOnlineUser.getUsername());
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

        buyerNotificationRecycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true);
        buyerNotificationRecycler.setLayoutManager(layoutManager);

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(5)
                .setPageSize(10)
                .build();


        DatabasePagingOptions<BuyerNotifications> options = new DatabasePagingOptions.Builder<BuyerNotifications>()
                .setLifecycleOwner(this)
                .setQuery(buyerNotificationRef, config, BuyerNotifications.class)
                .build();


        buyerNotificationAdapter = new FirebaseRecyclerPagingAdapter<BuyerNotifications, BuyerNotificationsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull BuyerNotificationsViewHolder buyerNotificationsViewHolder, int i, @NonNull BuyerNotifications buyerNotifications) {

                clickList.add(buyerNotifications.getNotificationBuyerKey());

                buyerNotificationsViewHolder.notificationSellerTitle.setText(buyerNotifications.getNotificationBuyerTitle());
                buyerNotificationsViewHolder.notificationSellerDescription.setText("Order ID : " + buyerNotifications.getNotificationBuyerKey());
                buyerNotificationsViewHolder.notificationSellerDate.setText(buyerNotifications.getNotificationBuyerDate());
                buyerNotificationsViewHolder.notificationType.setText(buyerNotifications.getNotificationBuyerType());
                if(buyerNotifications.getNotificationBuyerType().equals("Order Accepted")){
                    buyerNotificationsViewHolder.notificationType.setBackgroundColor(Color.parseColor("#68D4A1"));
                    Picasso.get().load(buyerNotifications.getNotificationBuyerImg()).placeholder(R.drawable.notification_icon).into(buyerNotificationsViewHolder.notificationSellerImg);
//                    buyerNotificationsViewHolder.notificationType.setTextColor(Color.parseColor("#F7F7F7"));
                }else if(buyerNotifications.getNotificationBuyerType().equals("Order Cancelled")){
                    buyerNotificationsViewHolder.notificationType.setBackgroundColor(Color.parseColor("#808080"));
                    buyerNotificationsViewHolder.notificationType.setTextColor(Color.parseColor("#F7F7F7"));
                    Picasso.get().load(buyerNotifications.getNotificationBuyerImg()).placeholder(R.drawable.notification_icon).into(buyerNotificationsViewHolder.notificationSellerImg);
                }else if(buyerNotifications.getNotificationBuyerType().equals("On the Way")){
                    buyerNotificationsViewHolder.notificationType.setBackgroundColor(Color.parseColor("#ffe400"));
                    Picasso.get().load(buyerNotifications.getNotificationBuyerImg()).placeholder(R.drawable.notification_icon).into(buyerNotificationsViewHolder.notificationSellerImg);
//                    buyerNotificationsViewHolder.notificationType.setTextColor(Color.parseColor("#F7F7F7"));
                }else if(buyerNotifications.getNotificationBuyerType().equals("Order Request")){
                    buyerNotificationsViewHolder.notificationType.setBackgroundColor(Color.parseColor("#ffa600"));
                    Picasso.get().load(R.drawable.notification_icon).placeholder(R.drawable.notification_icon).into(buyerNotificationsViewHolder.notificationSellerImg);
                }



            }

            @Override
            protected void onLoadingStateChanged(@NonNull LoadingState state) {
                switch (state) {
                    case LOADING_INITIAL:
                    case LOADING_MORE:
                        // Do your loading animation
                        buyerNotificationSwipeLayout.setRefreshing(true);
                        break;

                    case LOADED:
                        // Stop Animation
                        buyerNotificationSwipeLayout.setRefreshing(false);
                        break;

                    case FINISHED:
                        //Reached end of Data set
                        buyerNotificationSwipeLayout.setRefreshing(false);
                        break;

                    case ERROR:
                        retry();
                        break;
                }
            }

            @Override
            protected void onError(@NonNull DatabaseError databaseError) {
                buyerNotificationSwipeLayout.setRefreshing(false);
                databaseError.toException().printStackTrace();
            }

            @NonNull
            @Override
            public BuyerNotificationsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_card_view, parent, false);
                BuyerNotificationsViewHolder holder = new BuyerNotificationsViewHolder(view);
                return holder;
            }
        };

        buyerNotificationSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                buyerNotificationAdapter.refresh();
            }
        });

        buyerNotificationRecycler.setAdapter(buyerNotificationAdapter);
        buyerNotificationAdapter.startListening();

    }

}