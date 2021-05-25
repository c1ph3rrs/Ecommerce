package com.ciphers.ecommerce.Sellers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ciphers.ecommerce.Buyer.BuyerOrderDetailActivity;
import com.ciphers.ecommerce.Buyer.BuyerWishListActivity;
import com.ciphers.ecommerce.Buyer.ProductsDetail;
import com.ciphers.ecommerce.Helper.WishListSwipeHelper;
import com.ciphers.ecommerce.Interface.MyButtonClickListener;
import com.ciphers.ecommerce.Model.ClosedBuyerLocations;
import com.ciphers.ecommerce.Model.Products;
import com.ciphers.ecommerce.Model.SellersOrders;
import com.ciphers.ecommerce.Prevalent.Prevalent;
import com.ciphers.ecommerce.R;
import com.ciphers.ecommerce.Services.NotificationSender;
import com.ciphers.ecommerce.ViewHolder.ClosedBuyerLocationsViewHolder;
import com.ciphers.ecommerce.ViewHolder.ProductViewHolder;
import com.ciphers.ecommerce.ViewHolder.SellersOrdersViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shreyaspatil.firebase.recyclerpagination.FirebaseRecyclerPagingAdapter;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class SellerViewFindBuyersActivity extends AppCompatActivity {

    RecyclerView sellerViewFindBuyerRecycler;
    RecyclerView.LayoutManager layoutManager;
    DatabaseReference buyerLocationRef, buyerRef;

    FirebaseRecyclerAdapter<ClosedBuyerLocations, ClosedBuyerLocationsViewHolder> closedBuyerAdapter;

    WishListSwipeHelper findBuyerSwipeHelper;
    String dbToken;

    ArrayList<String> clickList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_view_find_buyers);

        buyerLocationRef = FirebaseDatabase.getInstance().getReference().child("Locations").child("ClosedBuyerLocations");

        sellerViewFindBuyerRecycler = findViewById(R.id.seller_view_search_buyer_recycler);

        findBuyerSwipeHelper = new WishListSwipeHelper(this, sellerViewFindBuyerRecycler, 200) {
            @Override
            public void initiateMyButtons(RecyclerView.ViewHolder viewHolder, List<MyButtons> buffer) {
                buffer.add(new MyButtons(SellerViewFindBuyersActivity.this,
                        "Send Notification",
                        30,
                        R.drawable.notification_white,
                        Color.parseColor("#1DBF73"),
                        new MyButtonClickListener() {
                            @Override
                            public void onClick(int pos) {

                                for(int i=0; i < clickList.size() +1; i ++){

                                    if(i == pos){

                                        int finalI = i;
                                        buyerLocationRef.child(Prevalent.currentOnlineSeller.getSellerUsername()).child(clickList.get(i)).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                sellerRequestForOrderNotification(Prevalent.currentOnlineSeller.getSellerUsername(), clickList.get(finalI), "Request For Order", "null");
                                            }
                                        });

                                    }

                                }


                            }
                        }));
            }
        };

        checkIfDataExist();

    }

    private void sellerRequestForOrderNotification(String sellerName, String buyerName, String orderShip, String img) {

        String notificationTitle = "Order Request";
        String notificationBody = " want to give me an order";
        String customBuyerNotificationKey = "";

        String saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        DatabaseReference notificationRef = FirebaseDatabase.getInstance().getReference().child("BuyerNotifications");

        String notificationKey = notificationRef.push().getKey();
        customBuyerNotificationKey = "ONK" + notificationKey;

        HashMap<String, Object> notificationMap = new HashMap<>();
        notificationMap.put("notificationBuyerKey", customBuyerNotificationKey);
        notificationMap.put("notificationBuyerOrderKey", orderShip);
        notificationMap.put("notificationBuyerBuyer", buyerName);
        notificationMap.put("notificationBuyer", sellerName);
        notificationMap.put("notificationBuyerTitle", notificationTitle + "  ( " + sellerName + " : ) " + notificationBody);
        notificationMap.put("notificationBuyerImg", img);
        notificationMap.put("notificationBuyerDate", saveCurrentDate);
        notificationMap.put("notificationBuyerType", "Order Request");

        notificationRef.child(buyerName).child(customBuyerNotificationKey).updateChildren(notificationMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                buyerRef = FirebaseDatabase.getInstance().getReference().child("UserTokens");
                buyerRef.child(buyerName).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        dbToken = snapshot.child("buyerToken").getValue(String.class);
                        Log.d("Buyer Token", "Buyer Token is " + dbToken);
                        NotificationSender notificationSender = new NotificationSender(dbToken, notificationTitle, Prevalent.currentOnlineSeller.getSellerUsername() + " " + notificationBody,
                                getApplicationContext(), SellerViewFindBuyersActivity.this);
                        notificationSender.SendNotifications();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    private void checkIfDataExist(){

        buyerLocationRef.child(Prevalent.currentOnlineSeller.getSellerUsername()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    fetchFindBuyersRecord();
                }else{
                    noRecordExist();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void fetchFindBuyersRecord(){

        sellerViewFindBuyerRecycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        sellerViewFindBuyerRecycler.setLayoutManager(layoutManager);

        FirebaseRecyclerOptions<ClosedBuyerLocations> options =
                new FirebaseRecyclerOptions.Builder<ClosedBuyerLocations>()
                        .setQuery(buyerLocationRef
                                        .child(Prevalent.currentOnlineSeller.getSellerUsername()),
                                ClosedBuyerLocations.class)
                        .build();

        closedBuyerAdapter = new FirebaseRecyclerAdapter<ClosedBuyerLocations, ClosedBuyerLocationsViewHolder>(options) {
            @SuppressLint("SetTextI18n")
            @Override
            protected void onBindViewHolder(@NonNull ClosedBuyerLocationsViewHolder closedBuyerLocationsViewHolder, int i, @NonNull ClosedBuyerLocations closedBuyerLocations) {

                closedBuyerLocationsViewHolder.closeBuyerLocationTV.setText(closedBuyerLocations.getBuyerUsername());

                clickList.add(closedBuyerLocations.getBuyerUsername());

            }

            @NonNull
            @Override
            public ClosedBuyerLocationsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new ClosedBuyerLocationsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.wish_list_card_view, parent, false));
            }
        };

        sellerViewFindBuyerRecycler.setAdapter(closedBuyerAdapter);
        closedBuyerAdapter.startListening();

    }

    private void noRecordExist(){

    }

}