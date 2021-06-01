package com.ciphers.ecommerce.Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ciphers.ecommerce.Prevalent.Prevalent;
import com.ciphers.ecommerce.R;
import com.ciphers.ecommerce.Sellers.SellerOrderDetailActivity;
import com.ciphers.ecommerce.Services.NotificationSender;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jsibbold.zoomage.AutoResetMode;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class BuyerOrderDetailActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    String orderImage, orderTitle, orderPrice, orderQty, orderShipping, orderType, orderSellerName, orderDate, orderStatus, paymentType;
    TextView orderTitleTxt, orderPriceTxt, orderQtyTxt, orderTotalPriceTxt, orderBuyerNameTxt, orderPaymentTypeTxt, orderShippingIDTxt;
    ImageView sellerOrderImage, orderBackImage;

    TextView buyerNameTxt, buyerReviewDescTxt, sellerNameTxt, sellerReviewDescTxt;
    ImageView buyerDropDownIV, sellerDropDownIV;

    public TextView orderSenderNameTxt, orderReceiverNameTxt, orderSenderCityTxt, orderReceiverCityTxt, orderSenderPhoneTxt, orderReceiverPhoneTxt, orderShippingAddressTxt, orderTrackingIDTxt;
    Button buyerOrderReceivedBtn;
    TextInputLayout buyerReviewTxt;

    String orderID, orderShippingAddressLatLng, orderBuyerName, currentOnlineUser;
    DatabaseReference shippingRef, buyerNotRef, trackingRef, branchOrderRef, analyticsRef;
    int itemTotalPrice = 0, totalPrice = 0;
    AppCompatRatingBar buyerRatting, buyerPostRatting, sellerPostRatting;

    String activityType = "null";
    String dbToken;
    CardView googleMapCardView;
    RelativeLayout addReviewLayout, buyerReviewShowLayout, sellerReviewLayout, buyerReviewLayout;

    int buyerReviewDetailChecker = 1, sellerReviewDetailChecker = 1;
    float commission, sellerOrderPriceReturn, totalRatting, totalNumbersRatting;

    int order;
    float availableBalance, earning, buying;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_order_detail);

        branchOrderRef = FirebaseDatabase.getInstance().getReference().child("BranchOrders").child("BranchPendingOrders");
        analyticsRef = FirebaseDatabase.getInstance().getReference().child("Analytics");

        Bundle sellerDeliverOrderBundleProcess = getIntent().getExtras();

        orderImage = sellerDeliverOrderBundleProcess.getString("orderImage");
        orderTitle = sellerDeliverOrderBundleProcess.getString("orderTitle");
        orderPrice = sellerDeliverOrderBundleProcess.getString("orderPrice");
        orderQty = sellerDeliverOrderBundleProcess.getString("orderQty");
        orderID = sellerDeliverOrderBundleProcess.getString("orderID");
        orderShipping = sellerDeliverOrderBundleProcess.getString("orderShippingID");
        orderType = sellerDeliverOrderBundleProcess.getString("orderType");
        orderSellerName = sellerDeliverOrderBundleProcess.getString("orderSellerName");
        orderDate = sellerDeliverOrderBundleProcess.getString("orderDate");
        orderStatus = sellerDeliverOrderBundleProcess.getString("orderStatus");
        paymentType = sellerDeliverOrderBundleProcess.getString("paymentType");
        activityType = sellerDeliverOrderBundleProcess.getString("activityType");

        shippingRef = FirebaseDatabase.getInstance().getReference();

        orderTitleTxt = findViewById(R.id.buyer_order_product_title);
        orderPriceTxt = findViewById(R.id.buyer_order_product_price);
        orderQtyTxt = findViewById(R.id.buyer_order_product_qty);
        orderTotalPriceTxt = findViewById(R.id.buyer_order_total_price);
        orderBuyerNameTxt = findViewById(R.id.buyer_order_buyer_name_txt);
        orderPaymentTypeTxt = findViewById(R.id.buyer_order_payment_type);
        sellerOrderImage = findViewById(R.id.buyer_order_image);
        buyerReviewTxt = findViewById(R.id.buyer_response_message);
        buyerRatting = findViewById(R.id.delivery_ratting_bar);


        orderSenderNameTxt = findViewById(R.id.buyer_order_sender_name_lbl);
        orderReceiverNameTxt = findViewById(R.id.buyer_order_receiver_name_lbl);
        orderSenderCityTxt = findViewById(R.id.buyer_order_sender_city_lbl);
        orderReceiverCityTxt = findViewById(R.id.buyer_order_receiver_city_lbl);
        orderSenderPhoneTxt = findViewById(R.id.buyer_order_sender_phone_lbl);
        orderReceiverPhoneTxt = findViewById(R.id.buyer_order_receiver_phone_lbl);
        orderShippingAddressTxt = findViewById(R.id.buyer_order_shipping_address_lbl);
        orderShippingIDTxt = findViewById(R.id.buyer_order_shipping_id_lbl);
        orderBackImage = findViewById(R.id.buyer_order_detail_back_icon);
        orderTrackingIDTxt = findViewById(R.id.buyer_order_tracking_id_lbl);
        orderTrackingIDTxt.setText("Tracking ID : " + orderID);
        googleMapCardView = findViewById(R.id.google_map_card_view);
        addReviewLayout = findViewById(R.id.buyer_add_review_layout);
        buyerReviewShowLayout = findViewById(R.id.buyer_review_show_layout);

        buyerNameTxt = findViewById(R.id.buyer_review_show_buyer_name_txt);
        buyerReviewDescTxt = findViewById(R.id.buyer_review_show_buyer_review_txt);
        sellerNameTxt = findViewById(R.id.buyer_review_show_seller_name_txt);
        sellerReviewDescTxt = findViewById(R.id.buyer_review_show_seller_review_txt);
        buyerPostRatting = findViewById(R.id.buyer_completed_ratting_bar);
        sellerPostRatting = findViewById(R.id.seller_completed_ratting_bar);
        buyerDropDownIV = findViewById(R.id.buyer_review_drop_iv);
        sellerDropDownIV = findViewById(R.id.seller_review_drop_iv);
        sellerReviewLayout = findViewById(R.id.seller_review_layout);
        buyerReviewLayout = findViewById(R.id.buyer_review_layout);


        buyerOrderReceivedBtn = findViewById(R.id.buyer_order_received_btn);

        currentOnlineUser = Prevalent.currentOnlineUser.getUsername();

        Picasso.get().load(orderImage).into(sellerOrderImage);
        orderTitleTxt.setText(orderTitle);
        orderPriceTxt.setText(orderPrice);
        orderQtyTxt.setText(orderQty);
        orderPaymentTypeTxt.setText(orderType);

        itemTotalPrice = Integer.parseInt(orderPrice) * Integer.parseInt(orderQty);
        totalPrice += itemTotalPrice;

        orderTotalPriceTxt.setText(String.valueOf(itemTotalPrice));
        orderBuyerNameTxt.setText(orderSellerName);
        orderShippingIDTxt.setText(orderShipping);

        startTextView();

        orderBackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuyerOrderDetailActivity.super.onBackPressed();
            }
        });


        buyerOrderReceivedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(buyerReviewTxt.getEditText().getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Please type something about your delivery. It may helps to other Buyers", Toast.LENGTH_LONG).show();
                } else {
//                    Toast.makeText(getApplicationContext(), " " + buyerRatting.getRating(), Toast.LENGTH_LONG).show();
                    checkSellerPaymentsRecord(orderSellerName);
                    competeDeliverOrder();
                }
            }
        });


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.buyer_order_google_map);


        if (activityType.equals("Completed")) {
            googleMapCardView.setVisibility(View.GONE);
            addReviewLayout.setVisibility(View.GONE);
            buyerOrderReceivedBtn.setVisibility(View.GONE);
            showBuyerReview();
            showSellerReview();
        } else if (activityType.equals("Delivered")) {
            mapFragment.getMapAsync(this);
            buyerReviewShowLayout.setVisibility(View.GONE);
        }

        if (orderStatus.equals("1")) {
            buyerOrderReceivedBtn.setVisibility(View.VISIBLE);
            addReviewLayout.setVisibility(View.VISIBLE);
        } else if (orderStatus.equals("0")) {
            buyerOrderReceivedBtn.setVisibility(View.GONE);
            addReviewLayout.setVisibility(View.GONE);
        }

        buyerDropDownIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (buyerReviewDetailChecker == 1) {
                    buyerDropDownIV.setImageResource(R.drawable.drop_up_arrow);
                    buyerReviewLayout.setVisibility(View.GONE);
                    buyerReviewDetailChecker = 0;
                } else {
                    buyerDropDownIV.setImageResource(R.drawable.drop_arrow);
                    buyerReviewLayout.setVisibility(View.VISIBLE);
                    buyerReviewDetailChecker = 1;
                }
            }
        });

        sellerDropDownIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sellerReviewDetailChecker == 1) {
                    sellerDropDownIV.setImageResource(R.drawable.drop_up_arrow);
                    sellerReviewLayout.setVisibility(View.GONE);
                    sellerReviewDetailChecker = 0;
                } else {
                    sellerDropDownIV.setImageResource(R.drawable.drop_arrow);
                    sellerReviewLayout.setVisibility(View.VISIBLE);
                    sellerReviewDetailChecker = 1;
                }
            }
        });

    }

    private void showSellerReview() {

        DatabaseReference sellerReviewsRef = FirebaseDatabase.getInstance().getReference().child("Reviews")
                .child("BuyersReviews").child(Prevalent.currentOnlineUser.getUsername()).child(orderID);

        sellerReviewsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    //                Log.d("Review is", "Review : " + snapshot);
                    String dbBuyerName = snapshot.child("sellerName").getValue(String.class);

                    String dbRatting = snapshot.child("ratting").getValue(String.class);
                    String dbBuyerReview = snapshot.child("review").getValue(String.class);

                    sellerNameTxt.setText(dbBuyerName);
                    sellerReviewDescTxt.setText(dbBuyerReview);
                    sellerPostRatting.setRating(Float.parseFloat(dbRatting));
                    sellerPostRatting.setIsIndicator(true);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void showBuyerReview() {
        DatabaseReference sellerReviewsRef = FirebaseDatabase.getInstance().getReference().child("Reviews")
                .child("SellersReviews").child(orderSellerName).child(orderID);

        sellerReviewsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    Log.d("Review is", "Review : " + snapshot);
                    String dbBuyerName = snapshot.child("buyerName").getValue(String.class);
                    Log.d("buyer Name", "Buyer : " + snapshot.child("buyerName").getValue(String.class));
                    String dbRatting = snapshot.child("ratting").getValue(String.class);
                    String dbBuyerReview = snapshot.child("review").getValue(String.class);
//
                    buyerNameTxt.setText(dbBuyerName);
                    buyerReviewDescTxt.setText(dbBuyerReview);
                    buyerPostRatting.setRating(Float.parseFloat(dbRatting));
                    buyerPostRatting.setIsIndicator(true);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void competeDeliverOrder() {

        final DatabaseReference buyerOrdersRef = FirebaseDatabase.getInstance().getReference().child("BuyerDeliverOrders");
        final DatabaseReference sellerOrdersRef = FirebaseDatabase.getInstance().getReference().child("SellersDeliverOrders");
//        final DatabaseReference shippingRef = FirebaseDatabase.getInstance().getReference().child("Shipping");
        final DatabaseReference completeBuyerOrdersRef = FirebaseDatabase.getInstance().getReference().child("CompletedBuyerOrders");
        final DatabaseReference completedSellersOrdersRef = FirebaseDatabase.getInstance().getReference().child("CompletedSellerOrders");
        final DatabaseReference trackingOrderRef = FirebaseDatabase.getInstance().getReference().child("Tracker");

        HashMap<String, Object> approveOrderMap = new HashMap<>();
        approveOrderMap.put("orderName", orderTitle);
        approveOrderMap.put("orderPrice", orderPrice);
        approveOrderMap.put("orderQty", orderQty);
        approveOrderMap.put("orderSellerName", orderSellerName);
        approveOrderMap.put("orderBuyerName", orderBuyerName);
        approveOrderMap.put("orderDate", orderDate);
        approveOrderMap.put("orderType", orderType);
        approveOrderMap.put("orderTotalPrice", String.valueOf(itemTotalPrice));
        approveOrderMap.put("orderImg", orderImage);
        approveOrderMap.put("orderID", orderID);
        approveOrderMap.put("paymentType", paymentType);
        approveOrderMap.put("orderShippingID", orderShipping);

        completedSellersOrdersRef.child(orderSellerName).child(orderID).updateChildren(approveOrderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                completeBuyerOrdersRef.child(orderBuyerName).child(orderID).updateChildren(approveOrderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        buyerOrdersRef.child(currentOnlineUser).child(orderID)
                                .removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            sellerOrdersRef.child(orderSellerName).child(orderID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        trackingOrderRef.child(orderID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                addReviewsDetail(orderSellerName, orderBuyerName, orderShipping, orderImage);
                                                            }
                                                        });
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                    }
                });
            }
        });

    }

    void addReviewsDetail(String orderSellerName, String orderBuyerName, String orderShipping, String image) {

        final DatabaseReference reviewsRef = FirebaseDatabase.getInstance().getReference().child("Reviews");

        String shipp = orderShipping;
        String imageUrl = image;


        HashMap<String, Object> reviewMap = new HashMap<>();
        reviewMap.put("review", buyerReviewTxt.getEditText().getText().toString());
        reviewMap.put("buyerName", orderBuyerName);
        reviewMap.put("sellerName", orderSellerName);
        reviewMap.put("ratting", String.valueOf(buyerRatting.getRating()));
        reviewsRef.child("SellersReviews").child(orderSellerName).child(orderID).updateChildren(reviewMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getApplicationContext(), "Order Completed", Toast.LENGTH_LONG).show();
                sellerOrderCompletedReviewNotification(orderSellerName, orderBuyerName, shipp, imageUrl);
            }
        });
    }

    private void sellerOrderCompletedReviewNotification(String orderSellerName, String orderBuyerName, String orderShip, String orderImg) {

        String notificationTitle = "Order Completed";
        String notificationBody = " marked your order as completed";
        String customBuyerNotificationKey = "";

        String saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        DatabaseReference notificationRef = FirebaseDatabase.getInstance().getReference().child("SellersNotifications");

        String notificationKey = notificationRef.push().getKey();
        customBuyerNotificationKey = "ONK" + notificationKey;

        HashMap<String, Object> notificationMap = new HashMap<>();
        notificationMap.put("notificationSellerKey", customBuyerNotificationKey);
        notificationMap.put("notificationSellerOrderKey", orderShip);
        notificationMap.put("notificationSellerBuyer", orderBuyerName);
        notificationMap.put("notificationSeller", orderSellerName);
        notificationMap.put("notificationSellerTitle", "Congratulation " + orderSellerName + "  ( " + orderBuyerName + " : ) marked your order as completed " +
                "You can check your buyer review when you give review to Buyer.");
        notificationMap.put("notificationSellerImg", orderImg);
        notificationMap.put("notificationSellerDate", saveCurrentDate);
        notificationMap.put("notificationSellerType", "Completed");

        notificationRef.child(orderSellerName).child(customBuyerNotificationKey).updateChildren(notificationMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                buyerNotRef = FirebaseDatabase.getInstance().getReference().child("SellersTokens");
                buyerNotRef.child(orderSellerName).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        dbToken = snapshot.child("sellerToken").getValue(String.class);
                        Log.d("Buyer Token", "Buyer Token is " + dbToken);

                        NotificationSender notificationSender = new NotificationSender(dbToken, notificationTitle, Prevalent.currentOnlineUser.getUsername() + " " + notificationBody,
                                getApplicationContext(), BuyerOrderDetailActivity.this);
                        notificationSender.SendNotifications();

//                        BuyerOrderDetailActivity.super.onBackPressed();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mMap.setMyLocationEnabled(true);
    }

    void startTextView() {

        trackingRef = FirebaseDatabase.getInstance().getReference().child("Tracker");

        shippingRef.child("Shipping").child(orderShipping).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String dbReceiverCity = snapshot.child("receiverCity").getValue(String.class);
                String dbReceiverName = snapshot.child("receiverName").getValue(String.class);
                String dbReceiverPhone = snapshot.child("receiverPhone").getValue(String.class);
                String dbSenderCity = snapshot.child("senderCity").getValue(String.class);
                String dbSenderName = snapshot.child("senderName").getValue(String.class);
                String dbSenderPhone = snapshot.child("senderPhone").getValue(String.class);
                String dbShippingAddress = snapshot.child("shippingAddress").getValue(String.class);
                String dbShippingID = snapshot.child("shippingID").getValue(String.class);
                String dbBuyerName = snapshot.child("buyerName").getValue(String.class);
                orderShippingAddressLatLng = dbShippingAddress;


                orderSenderNameTxt.setText("Sender Name : " + dbSenderName);
                orderSenderCityTxt.setText("Sender City : " + dbSenderCity);
                orderSenderPhoneTxt.setText("Sender Phone : " + dbSenderPhone);
                orderReceiverNameTxt.setText("Receiver Name : " + dbReceiverName);
                orderReceiverCityTxt.setText("Receiver City : " + dbReceiverCity);
                orderReceiverPhoneTxt.setText("Receiver Phone : " + dbReceiverPhone);
                orderShippingAddressTxt.setText("Shipping Address : " + dbShippingAddress);
                orderShippingIDTxt.setText("Shipping ID : " + dbShippingID);
                orderBuyerName = dbBuyerName;

                if (activityType.equals("Delivered")) {

                    trackingRef.child(orderID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.exists()) {

                                String dbLat = snapshot.child("lat").getValue(String.class);
                                String dbLng = snapshot.child("lng").getValue(String.class);

                                LatLng latLng = new LatLng(Double.parseDouble(dbLat), Double.parseDouble(dbLng));
                                mMap.addMarker(new MarkerOptions().position(latLng));
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                                Log.d("lat", "lat : " + dbLat);

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }


//                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
//                List<Address> addresses = null;
//                try {
//                    addresses = geocoder.getFromLocationName(orderShippingAddressLatLng, 1);
//                } catch (IOException e) {
////                    e.printStackTrace();
//                    try {
//                        addresses = geocoder.getFromLocationName("Pakistan", 1);
//                    } catch (IOException ioException) {
//                        Toast.makeText(getApplicationContext(), "Make Sure you have an active internet", Toast.LENGTH_SHORT).show();
//                    }
//                }
//                try {
////                    Address address = addresses.get(0);
//                    if (addresses.size() > 0) {
//                        double latitude = addresses.get(0).getLatitude();
//                        double longitude = addresses.get(0).getLongitude();
//
//                    }
//                } catch (Exception e) {
//
////                    Toast.makeText(getApplicationContext(), "Location Not Found", Toast.LENGTH_LONG).show();
////                    Address address = addresses.get(0);
////                    if (addresses.size() > 0) {
////                        double latitude = addresses.get(0).getLatitude();
////                        double longitude = addresses.get(0).getLongitude();
////                        LatLng latLng = new LatLng(latitude, longitude);
////                        mMap.addMarker(new MarkerOptions().position(latLng));
////                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
////                        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
////                    }
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(getApplicationContext(), "Make sure you have an active Internet", Toast.LENGTH_SHORT).show();
            }
        });

    }

    void checkSellerPaymentsRecord(String orderSellerName) {

        int newOrder = 1, newBalance, newEarning;
        float newRatting = 1f;

        Query queries = analyticsRef.child("Sellers").child(orderSellerName);
        queries.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    String dbAvailableBalance = dataSnapshot.child("availableBalance").getValue(String.class);
                    String dbTotalEarning = dataSnapshot.child("totalEarning").getValue(String.class);
                    String dbTotalOrders = dataSnapshot.child("totalOrders").getValue(String.class);
                    String dbWithdraw = dataSnapshot.child("withdraw").getValue(String.class);
                    String dbTotalRatting = dataSnapshot.child("totalRatting").getValue(String.class);
                    String dbTotalNumberRatting = dataSnapshot.child("totalNumberRatting").getValue(String.class);


                    commission = (float) (Float.parseFloat(String.valueOf(itemTotalPrice)) * 0.1);
                    sellerOrderPriceReturn = (Float.parseFloat(String.valueOf(itemTotalPrice)) - commission);

                    totalRatting = Float.parseFloat(dbTotalRatting) + Float.parseFloat(String.valueOf(buyerRatting.getRating()));
                    totalNumbersRatting = Float.parseFloat(dbTotalNumberRatting) + newRatting;

                    availableBalance = Float.parseFloat(dbAvailableBalance) + sellerOrderPriceReturn;
                    earning = Float.parseFloat(dbTotalEarning) + sellerOrderPriceReturn;
                    order = Integer.parseInt(dbTotalOrders) + newOrder;


                    HashMap<String, Object> paymentMap = new HashMap<>();
                    paymentMap.put("availableBalance", String.valueOf(availableBalance));
                    paymentMap.put("totalEarning", String.valueOf(earning));
                    paymentMap.put("totalOrders", String.valueOf(order));
                    paymentMap.put("withdraw", String.valueOf(dbWithdraw));
                    paymentMap.put("totalRatting", String.valueOf(totalRatting));
                    paymentMap.put("totalNumberRatting", String.valueOf(totalNumbersRatting));

                    analyticsRef.child("Sellers").child(orderSellerName).updateChildren(paymentMap);
                } else {

                    commission = (float) (Float.parseFloat(String.valueOf(itemTotalPrice)) * 0.1);
                    sellerOrderPriceReturn = (Float.parseFloat(String.valueOf(itemTotalPrice)) - commission);

                    HashMap<String, Object> paymentMap = new HashMap<>();
                    paymentMap.put("availableBalance", String.valueOf(sellerOrderPriceReturn));
                    paymentMap.put("totalEarning", String.valueOf(sellerOrderPriceReturn));
                    paymentMap.put("totalOrders", String.valueOf(newOrder));
                    paymentMap.put("totalRatting", String.valueOf(buyerRatting.getRating()));
                    paymentMap.put("totalNumberRatting", String.valueOf(newRatting));
                    paymentMap.put("withdraw", "0.0");


                    analyticsRef.child("Sellers").child(orderSellerName).updateChildren(paymentMap);
                }

                checkBuyerPaymentRecord();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void checkBuyerPaymentRecord() {

        int newOrder = 1;

        Query queries = analyticsRef.child("Buyers").child(Prevalent.currentOnlineUser.getUsername());
        queries.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    String dbAvailableBalance = snapshot.child("availableBalance").getValue(String.class);
                    String dbTotalBuying = snapshot.child("totalBuying").getValue(String.class);
                    String dbTotalOrders = snapshot.child("totalOrders").getValue(String.class);
                    String dbTotalRatting = snapshot.child("totalRatting").getValue(String.class);
                    String dbTotalNumberRatting = snapshot.child("totalNumberRatting").getValue(String.class);


                    buying = Float.parseFloat(dbTotalBuying) + Float.parseFloat(String.valueOf(itemTotalPrice));
                    order = Integer.parseInt(dbTotalOrders) + newOrder;


                    HashMap<String, Object> paymentBuyerMap = new HashMap<>();
                    paymentBuyerMap.put("availableBalance", dbAvailableBalance);
                    paymentBuyerMap.put("totalBuying", String.valueOf(buying));
                    paymentBuyerMap.put("totalOrders", String.valueOf(order));
                    paymentBuyerMap.put("totalRatting", dbTotalRatting);
                    paymentBuyerMap.put("totalNumberRatting", dbTotalNumberRatting);


                    analyticsRef.child("Buyers").child(Prevalent.currentOnlineUser.getUsername())
                            .updateChildren(paymentBuyerMap);

                } else {

                    HashMap<String, Object> paymentBuyerMap = new HashMap<>();
                    paymentBuyerMap.put("availableBalance", "0.0");
                    paymentBuyerMap.put("totalBuying", String.valueOf(itemTotalPrice));
                    paymentBuyerMap.put("totalOrders", String.valueOf(newOrder));
                    paymentBuyerMap.put("totalRatting", "0");
                    paymentBuyerMap.put("totalNumberRatting", "0");

                    analyticsRef.child("Buyers").child(Prevalent.currentOnlineUser.getUsername())
                            .updateChildren(paymentBuyerMap);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        BuyerOrderDetailActivity.super.onBackPressed();

    }
}