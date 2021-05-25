package com.ciphers.ecommerce.Sellers;


import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
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
import com.ciphers.ecommerce.Services.NotificationSender;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class SellerOrderDetailActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    String orderImage, orderTitle, orderPrice, orderQty, orderShipping, orderType, orderSellerName, orderDate, orderStatus, orderPayment;
    TextView orderTitleTxt, orderPriceTxt, orderQtyTxt, orderTotalPriceTxt, orderBuyerNameTxt, orderPaymentTypeTxt, orderShippingIDTxt;
    ImageView sellerOrderImage, orderBackImage, sellerReviewDDIV, buyerReviewDDIV;

    TextView sellerReviewName, sellerReviewDesc, sellerReviewBuyerName, sellerReviewBuyerDesc;

    public TextView orderSenderNameTxt, orderReceiverNameTxt, orderSenderCityTxt, orderReceiverCityTxt, orderSenderPhoneTxt, orderReceiverPhoneTxt, orderShippingAddressTxt, orderTrackingIDTxt;
    Button acceptOrderBtn, rejectOrderBtn, readyForShippingBtn, shareYourResponseBtn;
    TextInputLayout rejectOrderReasonEt, shareYourResponseET;

    String orderID, orderShippingAddressLatLng, orderBuyerName, currentOnlineSeller;
    DatabaseReference shippingRef, sellerRef, buyerNotRef, analyticsRef;
    String dbToken;
    int itemTotalPrice = 0, totalPrice = 0, buyerReviewDetailChecker = 1, sellerReviewDetailChecker = 1, totalNumbersRatting;
    String customBuyerNotificationKey;

    RelativeLayout sellerAddReviewLayout, sellerShowReviewLayout, sellerReviewLayout, buyerReviewLayout;

    AppCompatRatingBar sellerAddRatting, sellerBuyerRatting, buyerSellerRatting;

    String activityType = "null";
    float totalRatting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_order_detail);

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
        activityType = sellerDeliverOrderBundleProcess.getString("activityType");
        orderStatus = sellerDeliverOrderBundleProcess.getString("orderStatus");
        orderPayment = sellerDeliverOrderBundleProcess.getString("orderPaymentType");

        shippingRef = FirebaseDatabase.getInstance().getReference();

        orderTitleTxt = findViewById(R.id.seller_order_product_title);
        orderPriceTxt = findViewById(R.id.seller_order_product_price);
        orderQtyTxt = findViewById(R.id.seller_order_product_qty);
        orderTotalPriceTxt = findViewById(R.id.seller_order_total_price);
        orderBuyerNameTxt = findViewById(R.id.seller_order_buyer_name_txt);
        orderPaymentTypeTxt = findViewById(R.id.seller_order_payment_type);
        sellerOrderImage = findViewById(R.id.seller_order_image);

        orderSenderNameTxt = findViewById(R.id.seller_order_sender_name_lbl);
        orderReceiverNameTxt = findViewById(R.id.seller_order_receiver_name_lbl);
        orderSenderCityTxt = findViewById(R.id.seller_order_sender_city_lbl);
        orderReceiverCityTxt = findViewById(R.id.seller_order_receiver_city_lbl);
        orderSenderPhoneTxt = findViewById(R.id.seller_order_sender_phone_lbl);
        orderReceiverPhoneTxt = findViewById(R.id.seller_order_receiver_phone_lbl);
        orderShippingAddressTxt = findViewById(R.id.seller_order_shipping_address_lbl);
        orderShippingIDTxt = findViewById(R.id.seller_order_shipping_id_lbl);
        orderBackImage = findViewById(R.id.seller_order_detail_back_icon);
        orderTrackingIDTxt = findViewById(R.id.seller_order_tracking_id_lbl);
        orderTrackingIDTxt.setText("Tracking ID : " + orderID);

        acceptOrderBtn = findViewById(R.id.accept_order_btn);
        rejectOrderBtn = findViewById(R.id.reject_order_btn);
        rejectOrderReasonEt = findViewById(R.id.reject_reason_message);
        readyForShippingBtn = findViewById(R.id.order_detail_ready_for_shipping_order);

        sellerAddReviewLayout = findViewById(R.id.seller_add_review_layout);
        sellerShowReviewLayout = findViewById(R.id.seller_review_show_layout);
        shareYourResponseBtn = findViewById(R.id.seller_share_response_btn);
        shareYourResponseET = findViewById(R.id.seller_response_message);
        sellerAddRatting = findViewById(R.id.seller_delivery_ratting_bar);


        sellerReviewName = findViewById(R.id.seller_review_show_seller_name_txt);
        sellerReviewDesc = findViewById(R.id.seller_review_show_seller_review_txt);
        buyerSellerRatting = findViewById(R.id.seller_completed_seller_ratting_bar);

        sellerReviewBuyerName = findViewById(R.id.seller_review_show_buyer_name_txt);
        sellerReviewBuyerDesc = findViewById(R.id.seller_review_show_buyer_review_txt);
        sellerBuyerRatting = findViewById(R.id.seller_completed_buyer_ratting_bar);

        sellerReviewDDIV = findViewById(R.id.seller_review_drop_iv);
        buyerReviewDDIV = findViewById(R.id.seller_review_seller_drop_iv);

        sellerReviewLayout = findViewById(R.id.seller_review_layout);
        buyerReviewLayout = findViewById(R.id.seller_review_show_seller_layout);

        currentOnlineSeller = Prevalent.currentOnlineSeller.getSellerUsername();


        Picasso.get().load(orderImage).into(sellerOrderImage);
        orderTitleTxt.setText(orderTitle);
        orderPriceTxt.setText(orderPrice);
        orderQtyTxt.setText(orderQty);
        orderPaymentTypeTxt.setText(orderType);

        itemTotalPrice = Integer.parseInt(orderPrice) * Integer.parseInt(orderQty);
        totalPrice += itemTotalPrice;

        orderTotalPriceTxt.setText(String.valueOf(itemTotalPrice));

        startTextView();

        orderBackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SellerOrderDetailActivity.super.onBackPressed();
            }
        });

        acceptOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptOrder();
            }
        });

        rejectOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String rejectMessage = rejectOrderReasonEt.getEditText().getText().toString();

                if (rejectMessage.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please write the reason to convince buyer", Toast.LENGTH_LONG).show();
                } else {
                    deleteOrder(rejectMessage);
                }


            }
        });

        if (activityType.equals("Approve Order")) {
            acceptOrderBtn.setVisibility(View.GONE);
            rejectOrderBtn.setVisibility(View.GONE);
            rejectOrderReasonEt.setVisibility(View.GONE);
            sellerAddReviewLayout.setVisibility(View.GONE);
            sellerShowReviewLayout.setVisibility(View.GONE);
        } else if (activityType.equals("Pending")) {
            acceptOrderBtn.setVisibility(View.VISIBLE);
            rejectOrderBtn.setVisibility(View.VISIBLE);
            rejectOrderReasonEt.setVisibility(View.VISIBLE);
            readyForShippingBtn.setVisibility(View.GONE);
            sellerAddReviewLayout.setVisibility(View.GONE);
            sellerShowReviewLayout.setVisibility(View.GONE);
        } else if (activityType.equals("Deliver Order")) {
            acceptOrderBtn.setVisibility(View.GONE);
            rejectOrderBtn.setVisibility(View.GONE);
            rejectOrderReasonEt.setVisibility(View.GONE);
            readyForShippingBtn.setVisibility(View.GONE);
            rejectOrderReasonEt.setVisibility(View.GONE);
            sellerAddReviewLayout.setVisibility(View.GONE);
            sellerShowReviewLayout.setVisibility(View.GONE);
        } else if (activityType.equals("Completed")) {

            acceptOrderBtn.setVisibility(View.GONE);
            rejectOrderBtn.setVisibility(View.GONE);
            rejectOrderReasonEt.setVisibility(View.GONE);
            readyForShippingBtn.setVisibility(View.GONE);

            checkReviewSeller();

        }

        if (orderStatus.equals("0")) {
            readyForShippingBtn.setVisibility(View.GONE);
        } else if (orderStatus.equals("1")) {
            readyForShippingBtn.setVisibility(View.VISIBLE);
        }

        readyForShippingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readyForShipping();
            }
        });

        shareYourResponseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(shareYourResponseET.getEditText().getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Please write something for buyer", Toast.LENGTH_LONG).show();
                } else {
                    saveSellerResponse();
                }
            }
        });

        sellerReviewDDIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sellerReviewDetailChecker == 1) {
                    sellerReviewDDIV.setImageResource(R.drawable.drop_up_arrow);
                    sellerReviewLayout.setVisibility(View.GONE);
                    sellerReviewDetailChecker = 0;
                } else {
                    sellerReviewDDIV.setImageResource(R.drawable.drop_arrow);
                    sellerReviewLayout.setVisibility(View.VISIBLE);
                    sellerReviewDetailChecker = 1;
                }
            }
        });

        buyerReviewDDIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buyerReviewDetailChecker == 1) {
                    buyerReviewDDIV.setImageResource(R.drawable.drop_up_arrow);
                    buyerReviewLayout.setVisibility(View.GONE);
                    buyerReviewDetailChecker = 0;
                } else {
                    buyerReviewDDIV.setImageResource(R.drawable.drop_arrow);
                    buyerReviewLayout.setVisibility(View.VISIBLE);
                    buyerReviewDetailChecker = 1;
                }
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.seller_order_google_map);
//        mapFragment.getMapAsync(this);
        mapFragment.getMapAsync(this);

    }

    private void saveSellerResponse() {

        int newRatting = 1;

        Query queries = analyticsRef.child("Buyers").child(orderBuyerName);
        final DatabaseReference reviewsRef = FirebaseDatabase.getInstance().getReference().child("Reviews");

        String shipp = orderShipping;
        String imageUrl = orderImage;

        HashMap<String, Object> reviewMap = new HashMap<>();
        reviewMap.put("review", shareYourResponseET.getEditText().getText().toString());
        reviewMap.put("buyerName", orderBuyerName);
        reviewMap.put("sellerName", orderSellerName);
        reviewMap.put("ratting", String.valueOf(sellerAddRatting.getRating()));
        reviewsRef.child("BuyersReviews").child(orderBuyerName).child(orderID).updateChildren(reviewMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                queries.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {


                            String dbTotalRatting = snapshot.child("totalRatting").getValue(String.class);
                            String dbTotalNumberRatting = snapshot.child("totalNumberRatting").getValue(String.class);


                            totalRatting = Float.parseFloat(dbTotalRatting) + Float.parseFloat(String.valueOf(sellerAddRatting.getRating()));
                            totalNumbersRatting = Integer.parseInt(dbTotalNumberRatting) + newRatting;


                            HashMap<String, Object> paymentMap = new HashMap<>();
                            paymentMap.put("totalRatting", String.valueOf(totalRatting));
                            paymentMap.put("totalNumberRatting", String.valueOf(totalNumbersRatting));

                            analyticsRef.child("Buyers").child(orderBuyerName).updateChildren(paymentMap);
                        } else {

                            HashMap<String, Object> paymentMap = new HashMap<>();

                            paymentMap.put("totalRatting", String.valueOf(sellerAddRatting.getRating()));
                            paymentMap.put("totalNumberRatting", String.valueOf(newRatting));

                            analyticsRef.child("Buyers").child(orderBuyerName).updateChildren(paymentMap);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                Toast.makeText(getApplicationContext(), "Review Added", Toast.LENGTH_LONG).show();

                sellerOrderCompletedReviewNotification(orderSellerName, orderBuyerName, shipp, imageUrl);
                SellerOrderDetailActivity.super.onBackPressed();
            }
        });

    }

    private void showReviewLayout() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Reviews")
                .child("BuyersReviews").child(orderSellerName);
        Query queries = ref.child(orderID);
        queries.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    sellerAddReviewLayout.setVisibility(View.GONE);
                    sellerShowReviewLayout.setVisibility(View.VISIBLE);
                    displayReview();
                    displaySellerReview();
                } else {
                    sellerAddReviewLayout.setVisibility(View.VISIBLE);
                    sellerShowReviewLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void displaySellerReview() {
        DatabaseReference sellerReviewsRef = FirebaseDatabase.getInstance().getReference().child("Reviews")
                .child("SellersReviews").child(Prevalent.currentOnlineSeller.getSellerUsername()).child(orderID);

        sellerReviewsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    String dbBuyerName = snapshot.child("sellerName").getValue(String.class);

                    String dbRatting = snapshot.child("ratting").getValue(String.class);
                    String dbBuyerReview = snapshot.child("review").getValue(String.class);
                    String dbReviewDate = snapshot.child("reviewDate").getValue(String.class);

                    sellerReviewBuyerName.setText(dbBuyerName);
                    sellerReviewBuyerDesc.setText(dbBuyerReview);
                    sellerBuyerRatting.setRating(Float.parseFloat(dbRatting));
                    sellerBuyerRatting.setIsIndicator(true);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void displayReview() {
        DatabaseReference sellerReviewsRef = FirebaseDatabase.getInstance().getReference().child("Reviews")
                .child("BuyersReviews").child(orderBuyerName).child(orderID);

        sellerReviewsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Log.d("Review is", "Review : " + snapshot);
                String dbBuyerName = snapshot.child("buyerName").getValue(String.class);
//                Log.d("buyer Name", "Buyer : " + snapshot.child("buyerName").getValue(String.class));
                String dbRatting = snapshot.child("ratting").getValue(String.class);
                String dbBuyerReview = snapshot.child("review").getValue(String.class);
//
                sellerReviewName.setText(dbBuyerName);
                sellerReviewDesc.setText(dbBuyerReview);
                buyerSellerRatting.setRating(Float.parseFloat(dbRatting));
                buyerSellerRatting.setIsIndicator(true);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sellerOrderCompletedReviewNotification(String orderSellerName, String orderBuyerName, String shipp, String imageUrl) {
        String notificationTitle = "Review";
        String notificationBody = " left you a review";
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
        notificationMap.put("notificationBuyerOrderKey", shipp);
        notificationMap.put("notificationBuyerBuyer", orderBuyerName);
        notificationMap.put("notificationBuyer", orderSellerName);
        notificationMap.put("notificationBuyerTitle", "Congratulation " + orderSellerName + "  ( " + orderBuyerName + " : ) left you a review " +
                "You can check your review in complete order tab");
        notificationMap.put("notificationBuyerImg", imageUrl);
        notificationMap.put("notificationBuyerDate", saveCurrentDate);
        notificationMap.put("notificationBuyerType", "Review");

        notificationRef.child(orderSellerName).child(customBuyerNotificationKey).updateChildren(notificationMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                buyerNotRef = FirebaseDatabase.getInstance().getReference().child("UserTokens");
                buyerNotRef.child(orderBuyerName).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        dbToken = snapshot.child("buyerToken").getValue(String.class);
                        Log.d("Buyer Token", "Buyer Token is " + dbToken);

                        NotificationSender notificationSender = new NotificationSender(dbToken, notificationTitle, Prevalent.currentOnlineSeller.getSellerUsername() + " " + notificationBody,
                                getApplicationContext(), SellerOrderDetailActivity.this);
                        notificationSender.SendNotifications();
                        SellerOrderDetailActivity.super.onBackPressed();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }


    void checkReviewSeller() {

        Log.d("Buyer Name Review", "Name is : " + orderSellerName);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Reviews")
                .child("BuyersReviews").child(orderSellerName);

        Query queries = ref.child(orderID);
        queries.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    sellerAddReviewLayout.setVisibility(View.GONE);
                    sellerShowReviewLayout.setVisibility(View.VISIBLE);
                    showReviewLayout();
                } else {
                    sellerAddReviewLayout.setVisibility(View.VISIBLE);
                    sellerShowReviewLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void readyForShipping() {
        final DatabaseReference sellersDeliverOrders = FirebaseDatabase.getInstance().getReference().child("SellersDeliverOrders");
        final DatabaseReference buyerDeliverOrders = FirebaseDatabase.getInstance().getReference().child("BuyerDeliverOrders");
        final DatabaseReference sellersOrders = FirebaseDatabase.getInstance().getReference().child("SellersOrders");
        final DatabaseReference buyerOrders = FirebaseDatabase.getInstance().getReference().child("BuyerOrders");

        HashMap<String, Object> approveOrderMap = new HashMap<>();
        approveOrderMap.put("deliverOrderName", orderTitle);
        approveOrderMap.put("deliverOrderPrice", orderPrice);
        approveOrderMap.put("deliverOrderQty", orderQty);
        approveOrderMap.put("deliverOrderSellerName", orderSellerName);
        approveOrderMap.put("deliverOrderBuyerName", orderBuyerName);
        approveOrderMap.put("deliverOrderDate", orderDate);
        approveOrderMap.put("deliverOrderType", orderType);
        approveOrderMap.put("deliverOrderTotalPrice", String.valueOf(itemTotalPrice));
        approveOrderMap.put("deliverOrderImg", orderImage);
        approveOrderMap.put("deliverOrderID", orderID);
        approveOrderMap.put("deliverOrderPaymentType", orderPayment);
        approveOrderMap.put("deliverOrderStatus", "0");
        approveOrderMap.put("deliverOrderShippingID", orderShipping);

        sellersDeliverOrders.child(orderSellerName).child(orderID).updateChildren(approveOrderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                buyerDeliverOrders.child(orderBuyerName).child(orderID).updateChildren(approveOrderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        sellersOrders.child(currentOnlineSeller).child(orderID)
                                .removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            buyerOrders.child(orderBuyerName).child(orderID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(getApplicationContext(), "Order Ready for Delivery", Toast.LENGTH_LONG).show();
                                                        readyForShippingBuyerNotification(orderBuyerName, orderTitle, orderImage, orderID, orderSellerName);
                                                        SellerOrderDetailActivity.super.onBackPressed();
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

    private void readyForShippingBuyerNotification(String productBuyer, String productName, String productImg, String shippingKey, String productSeller) {

        String notificationTitle = "Congratulation.!";
        String notificationBody = " deliver your order to our branch. And it is ready for shipping.";

        String saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        DatabaseReference notificationRef = FirebaseDatabase.getInstance().getReference().child("BuyerNotifications");

        String notificationKey = notificationRef.push().getKey();
        customBuyerNotificationKey = "ONK" + notificationKey;

        Log.d("notification Key ", "Notification Key" + customBuyerNotificationKey);

        HashMap<String, Object> notificationMap = new HashMap<>();
        notificationMap.put("notificationBuyerKey", customBuyerNotificationKey);
        notificationMap.put("notificationBuyerOrderKey", shippingKey);
        notificationMap.put("notificationBuyerBuyer", productBuyer);
        notificationMap.put("notificationBuyer", productSeller);
        notificationMap.put("notificationBuyerTitle", "Congratulation " + productSeller + " : deliver your Order ( " + productName + " ), to our branch and it is ready for shipping. " +
                "You can check your order location from your order detail.");
        notificationMap.put("notificationBuyerImg", productImg);
        notificationMap.put("notificationBuyerDate", saveCurrentDate);
        notificationMap.put("notificationBuyerType", "On the Way");
        notificationMap.put("notificationBuyerStatus", "0");

        notificationRef.child(productBuyer).child(customBuyerNotificationKey).updateChildren(notificationMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                sellerRef = FirebaseDatabase.getInstance().getReference().child("UserTokens");
                sellerRef.child(orderBuyerName).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        dbToken = snapshot.child("buyerToken").getValue(String.class);
                        Log.d("Buyer Token", "Buyer Token is " + dbToken);
                        NotificationSender notificationSender = new NotificationSender(dbToken, notificationTitle, currentOnlineSeller + " " + notificationBody,
                                getApplicationContext(), SellerOrderDetailActivity.this);
                        notificationSender.SendNotifications();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


    }

    void deleteOrder(String rejectMessage) {

        final DatabaseReference rejectRef = FirebaseDatabase.getInstance().getReference().child("RejectOrders");
        final DatabaseReference placedOrderRef = FirebaseDatabase.getInstance().getReference().child("PlacedOrder");
        final DatabaseReference activeOrdersRef = FirebaseDatabase.getInstance().getReference().child("ActiveOrders");

        HashMap<String, Object> rejectOrderMap = new HashMap<>();
        rejectOrderMap.put("orderName", orderTitle);
        rejectOrderMap.put("orderPrice", orderPrice);
        rejectOrderMap.put("orderQty", orderQty);
        rejectOrderMap.put("orderSellerName", orderSellerName);
        rejectOrderMap.put("orderBuyerName", orderBuyerName);
        rejectOrderMap.put("orderDate", orderDate);
        rejectOrderMap.put("orderType", orderType);
        rejectOrderMap.put("orderShippingID", orderID);
        rejectOrderMap.put("orderTotalPrice", String.valueOf(itemTotalPrice));
        rejectOrderMap.put("orderImg", orderImage);
        rejectOrderMap.put("rejectMessage", rejectMessage);

        rejectRef.child(orderBuyerName).child(orderID).updateChildren(rejectOrderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {


                placedOrderRef.child(currentOnlineSeller).child(orderID)
                        .removeValue()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    activeOrdersRef.child(orderBuyerName).child(orderID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getApplicationContext(), "Order Rejected", Toast.LENGTH_LONG).show();
                                                rejectBuyerNotification(orderBuyerName, orderTitle, orderImage, orderID, orderSellerName, rejectMessage);
                                                SellerOrderDetailActivity.super.onBackPressed();
                                            }
                                        }
                                    });
                                }
                            }
                        });
            }
        });
    }

    private void rejectBuyerNotification(String productBuyer, String productName, String productImg, String shippingKey, String productSeller, String rejectMessage) {

        String notificationTitle = "Order Rejected.!";
        String notificationBody = " rejected your Order";

        String saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        DatabaseReference notificationRef = FirebaseDatabase.getInstance().getReference().child("BuyerNotifications");

        String notificationKey = notificationRef.push().getKey();
        customBuyerNotificationKey = "ONK" + notificationKey;

        Log.d("notification Key ", "Notification Key" + customBuyerNotificationKey);

        HashMap<String, Object> notificationMap = new HashMap<>();
        notificationMap.put("notificationBuyerKey", customBuyerNotificationKey);
        notificationMap.put("notificationBuyerOrderKey", shippingKey);
        notificationMap.put("notificationBuyerBuyer", productBuyer);
        notificationMap.put("notificationBuyer", productSeller);
        notificationMap.put("notificationBuyerTitle", "Unfortunately " + productSeller + " : rejected your Order ( " + productName + " ) due to this Reason : " + rejectMessage);
        notificationMap.put("notificationBuyerImg", productImg);
        notificationMap.put("notificationBuyerDate", saveCurrentDate);
        notificationMap.put("notificationBuyerType", "Order Cancelled");
        notificationMap.put("notificationBuyerStatus", "0");

        notificationRef.child(productBuyer).child(customBuyerNotificationKey).updateChildren(notificationMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                sellerRef = FirebaseDatabase.getInstance().getReference().child("UserTokens");
                sellerRef.child(orderBuyerName).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        dbToken = snapshot.child("buyerToken").getValue(String.class);
                        Log.d("Buyer Token", "Buyer Token is " + dbToken);
                        NotificationSender notificationSender = new NotificationSender(dbToken, notificationTitle, currentOnlineSeller + " " + notificationBody,
                                getApplicationContext(), SellerOrderDetailActivity.this);
                        notificationSender.SendNotifications();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


    }

    void acceptOrder() {


        final DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("SellersOrders");
        final DatabaseReference buyerOrdersRef = FirebaseDatabase.getInstance().getReference().child("BuyerOrders");
        final DatabaseReference placedOrderRef = FirebaseDatabase.getInstance().getReference().child("PlacedOrder");
        final DatabaseReference activeOrdersRef = FirebaseDatabase.getInstance().getReference().child("ActiveOrders");

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
        approveOrderMap.put("orderStatus", "0");
        approveOrderMap.put("orderShippingID", orderShipping);
        approveOrderMap.put("orderPaymentType", orderPayment);

        ordersRef.child(orderSellerName).child(orderID).updateChildren(approveOrderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                buyerOrdersRef.child(orderBuyerName).child(orderID).updateChildren(approveOrderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        placedOrderRef.child(currentOnlineSeller).child(orderID)
                                .removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            activeOrdersRef.child(orderBuyerName).child(orderID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {

                                                        addBuyerNotification(orderBuyerName, orderTitle, orderImage, orderID, orderSellerName);
                                                        SellerOrderDetailActivity.super.onBackPressed();

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

    private void addBuyerNotification(String productBuyer, String productName, String productImg, String shippingKey, String productSeller) {

        String notificationTitle = "Order Accepted";
        String notificationBody = "just accepted your Order";

        String saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        DatabaseReference notificationRef = FirebaseDatabase.getInstance().getReference().child("BuyerNotifications");

        String notificationKey = notificationRef.push().getKey();
        customBuyerNotificationKey = "ONK" + notificationKey;

        Log.d("notification Key ", "Notification Key" + customBuyerNotificationKey);

        HashMap<String, Object> notificationMap = new HashMap<>();
        notificationMap.put("notificationBuyerKey", customBuyerNotificationKey);
        notificationMap.put("notificationBuyerOrderKey", shippingKey);
        notificationMap.put("notificationBuyerBuyer", productBuyer);
        notificationMap.put("notificationBuyer", productSeller);
        notificationMap.put("notificationBuyerTitle", productSeller + " : just accepted your order of ( " + productName + " )");
        notificationMap.put("notificationBuyerImg", productImg);
        notificationMap.put("notificationBuyerDate", saveCurrentDate);
        notificationMap.put("notificationBuyerType", "Order Accepted");
        notificationMap.put("notificationBuyerStatus", "0");

        notificationRef.child(productBuyer).child(customBuyerNotificationKey).updateChildren(notificationMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                sellerRef = FirebaseDatabase.getInstance().getReference().child("UserTokens");
                sellerRef.child(orderBuyerName).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        dbToken = snapshot.child("buyerToken").getValue(String.class);
                        Log.d("Buyer Token", "Buyer Token is " + dbToken);
                        NotificationSender notificationSender = new NotificationSender(dbToken, notificationTitle, currentOnlineSeller + " " + notificationBody,
                                getApplicationContext(), SellerOrderDetailActivity.this);
                        notificationSender.SendNotifications();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    void startTextView() {

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
                orderBuyerNameTxt.setText(dbBuyerName);
                orderBuyerName = dbBuyerName;

                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                List<Address> addresses = null;

//                if(addresses.size() > 0){
                try {
                    addresses = geocoder.getFromLocationName(orderShippingAddressLatLng, 1);
                } catch (IOException e) {
//                    e.printStackTrace();
                    try {
                        addresses = geocoder.getFromLocationName("Pakistan", 1);
                    } catch (IOException ioException) {
                        Toast.makeText(getApplicationContext(), "Make Sure you have an active internet", Toast.LENGTH_SHORT).show();
                    }
                }
                try {
//                    Address address = addresses.get(0);
                    if (addresses.size() > 0) {
                        double latitude = addresses.get(0).getLatitude();
                        double longitude = addresses.get(0).getLongitude();
                        LatLng latLng = new LatLng(latitude, longitude);
                        mMap.addMarker(new MarkerOptions().position(latLng));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                    }
                } catch (Exception e) {
//                    Address address = addresses.get(0);
//                    if (addresses.size() > 0) {
//                        double latitude = addresses.get(0).getLatitude();
//                        double longitude = addresses.get(0).getLongitude();
//                        LatLng latLng = new LatLng(latitude, longitude);
//                        mMap.addMarker(new MarkerOptions().position(latLng));
//                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//                        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
//                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Make sure you have an active Internet", Toast.LENGTH_SHORT).show();
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
//        mMap.getUiSettings().setZoomControlsEnabled(true);
//        mMap.getUiSettings().setRotateGesturesEnabled(false);
//        mMap.getUiSettings().setScrollGesturesEnabled(false);
//        mMap.getUiSettings().setTiltGesturesEnabled(false);

    }

}