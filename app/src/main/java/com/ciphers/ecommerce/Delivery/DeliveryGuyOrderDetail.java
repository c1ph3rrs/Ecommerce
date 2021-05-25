package com.ciphers.ecommerce.Delivery;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ciphers.ecommerce.LoginRetailer.RetailerMainPage;
import com.ciphers.ecommerce.LoginRetailer.SignUpRetailerPageOne;
import com.ciphers.ecommerce.Model.DeliveryGuy;
import com.ciphers.ecommerce.Prevalent.Prevalent;
import com.ciphers.ecommerce.R;
import com.ciphers.ecommerce.SplashScreen;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class DeliveryGuyOrderDetail extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    Button deliveryGuySetCurrentLocationBtn, deliveryGuyDeliverOrderBtn;
    TextView deliveryGuySenderNameTxt, deliveryGuyReceiverNameTxt, deliveryGuySenderPhoneTxt, deliveryGuyReceiverPhoneTxt, deliveryGuyAddressTxt, deliveryGuySenderCityTxt, deliveryGuyReceiverCityTxt;
    TextView deliveryGuyShippingIdTxt, deliveryGuyTrackingIdTxt;
    TextView productTitleTxt, productQtyTxt, productPriceTxt, productTotalPriceTxt, productBuyerNameTxt, productPaymentTypeTxt;
    ImageView productIV;
    String paymentType = "";

    String shippingID, trackingID, buyerName, from, to, date, assignDate, id;
    DatabaseReference shippingRef, buyerOrderRef, removeDeliveryRef, shippedRef, branchDeliveredOrdersRef, branchPaymentRef;
    DatabaseReference shippingPendingOrders;

    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    String lat, lng, newAmountTotal;
    float totalEarning, totalAvailable, previousAmount, newAmount;

    DatabaseReference trackingRef;
    int i = 0, shippingCharges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_guy_order_detail);

        trackingRef = FirebaseDatabase.getInstance().getReference().child("Tracker");
        buyerOrderRef = FirebaseDatabase.getInstance().getReference().child("BuyerDeliverOrders");
        removeDeliveryRef = FirebaseDatabase.getInstance().getReference().child("deliveryGuyShip");
        shippedRef = FirebaseDatabase.getInstance().getReference().child("DeliveryGuyShipped");
        branchDeliveredOrdersRef = FirebaseDatabase.getInstance().getReference().child("BranchOrders").child("BranchDeliveredOrders");
        branchPaymentRef = FirebaseDatabase.getInstance().getReference().child("BranchesPayments");
        shippingPendingOrders = FirebaseDatabase.getInstance().getReference().child("BranchOrders").child("BranchPendingOrders");

        shippingRef = FirebaseDatabase.getInstance().getReference();

        shippingID = getIntent().getStringExtra("shippingID");
        trackingID = getIntent().getStringExtra("trackingID");
        from = getIntent().getStringExtra("from");
        to = getIntent().getStringExtra("to");
        id = getIntent().getStringExtra("id");
        assignDate = getIntent().getStringExtra("assignDate");
        date = getIntent().getStringExtra("date");

        deliveryGuySetCurrentLocationBtn = findViewById(R.id.delivery_guy_set_current_location_btn);
        deliveryGuyDeliverOrderBtn = findViewById(R.id.delivery_guy_deliver_order_btn);

        deliveryGuyShippingIdTxt = findViewById(R.id.delivery_guy_shipping_id_lbl);
        deliveryGuyTrackingIdTxt = findViewById(R.id.delivery_guy_tracking_id_lbl);
        deliveryGuySenderNameTxt = findViewById(R.id.delivery_guy_sender_name_lbl);
        deliveryGuyReceiverNameTxt = findViewById(R.id.delivery_guy_receiver_name_lbl);
        deliveryGuySenderPhoneTxt = findViewById(R.id.delivery_guy_sender_phone_lbl);
        deliveryGuyReceiverPhoneTxt = findViewById(R.id.delivery_guy_receiver_phone_lbl);
        deliveryGuyAddressTxt = findViewById(R.id.delivery_guy_shipping_address_lbl);
        deliveryGuySenderCityTxt = findViewById(R.id.delivery_guy_sender_city_lbl);
        deliveryGuyReceiverCityTxt = findViewById(R.id.delivery_guy_receiver_city_lbl);

        productTitleTxt = findViewById(R.id.seller_order_product_title);
        productQtyTxt = findViewById(R.id.seller_order_product_qty);
        productPriceTxt = findViewById(R.id.seller_order_product_price);
        productTotalPriceTxt = findViewById(R.id.seller_order_total_price);
        productBuyerNameTxt = findViewById(R.id.seller_order_buyer_name_txt);
        productPaymentTypeTxt = findViewById(R.id.seller_order_payment_type);
        productIV = findViewById(R.id.seller_order_image);


        deliveryGuyShippingIdTxt.setText(shippingID);
        deliveryGuyTrackingIdTxt.setText(trackingID);

        if (Prevalent.currentOnlineDeliveryGuy.getDeliveryGuyStatus().equals("1")) {
            deliveryGuyDeliverOrderBtn.setVisibility(View.GONE);
        } else if (Prevalent.currentOnlineDeliveryGuy.getDeliveryGuyStatus().equals("1")) {
            deliveryGuyDeliverOrderBtn.setVisibility(View.VISIBLE);
        }

        startTextView();

        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.delivery_guy_google_map);
        mapFrag.getMapAsync(this);

        deliveryGuySetCurrentLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadTrackingRecordData();

            }
        });

        deliveryGuyDeliverOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence options[] = new CharSequence[]{
                        "Yes",
                        "No"
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(DeliveryGuyOrderDetail.this);
                builder.setTitle("Are you sure your deliver this Order to Buyer.?");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {

                            if(paymentType.equals("COD")){
                                addPaymentRecordToBranch();
                            }else{
                                changeBuyerOrderStatus();
                            }

                        } else {
                        }
                    }
                });
                builder.show();
            }
        });

    }

    private void changeBuyerOrderStatus() {

        HashMap<String, Object> shippedMap = new HashMap<>();
        shippedMap.put("id", id);
        shippedMap.put("tracking", trackingID);
        shippedMap.put("shipping", shippingID);
        shippedMap.put("from", from);
        shippedMap.put("to", to);
        shippedMap.put("date", date);
        shippedMap.put("assignDate", assignDate);
        shippedMap.put("buyer", buyerName);

        HashMap<String, Object> deliverOrderMap = new HashMap<>();
        deliverOrderMap.put("deliverOrderStatus", "1");
        buyerOrderRef.child(buyerName).child(trackingID).updateChildren(deliverOrderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                removeDeliveryRef.child(Prevalent.currentOnlineDeliveryGuy.getDeliveryGuyUsername())
                        .child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        shippedRef.child(Prevalent.currentOnlineDeliveryGuy.getDeliveryGuyUsername())
                                .child(id).updateChildren(shippedMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                branchDeliveredOrdersRef.child(to).child(trackingID).updateChildren(shippedMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        shippingPendingOrders.child(to).child(trackingID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(getApplicationContext(), "Order Delivered Successfully", Toast.LENGTH_LONG).show();
                                                DeliveryGuyOrderDetail.super.onBackPressed();
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });

    }

    private void addPaymentRecordToBranch() {


        branchPaymentRef.child(to).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    String dbBranchTotalEarning = snapshot.child("totalEarning").getValue(String.class);
                    String dbBranchTotalAvailable = snapshot.child("availableBalance").getValue(String.class);

                    totalEarning = Float.parseFloat(dbBranchTotalEarning) + Float.parseFloat(productTotalPriceTxt.getText().toString());
                    totalAvailable = Float.parseFloat(dbBranchTotalAvailable) + Float.parseFloat(productTotalPriceTxt.getText().toString());

                    HashMap<String, Object> branchPaymentMap = new HashMap<>();
                    branchPaymentMap.put("totalEarning", String.valueOf(totalEarning));
                    branchPaymentMap.put("availableBalance", String.valueOf(totalAvailable));
                    branchPaymentRef.child(to).updateChildren(branchPaymentMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.d("Existing", "Loop");
                            Log.d("value ", "Value of i is " + i);
                            i += 1;
                        }
                    });
                } else {
                    HashMap<String, Object> branchPaymentMap = new HashMap<>();
                    branchPaymentMap.put("totalEarning", productTotalPriceTxt.getText().toString());
                    branchPaymentMap.put("availableBalance", productTotalPriceTxt.getText().toString());
                    branchPaymentRef.child(to).updateChildren(branchPaymentMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.d("New", "Loop");
                            Log.d("value ", "Value of i is " + i);
                            i += 1;
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        changeBuyerOrderStatus();

    }

    private void uploadTrackingRecordData() {

        HashMap<String, Object> latLngMap = new HashMap<>();
        latLngMap.put("lat", lat);
        latLngMap.put("lng", lng);

        trackingRef.child(trackingID).updateChildren(latLngMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getApplicationContext(), "Location Added", Toast.LENGTH_LONG).show();
                DeliveryGuyOrderDetail.super.onBackPressed();
            }
        });
    }

    void startTextView() {

        shippingRef.child("Shipping").child(shippingID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String dbReceiverCity = snapshot.child("receiverCity").getValue(String.class);
                    String dbReceiverName = snapshot.child("receiverName").getValue(String.class);
                    String dbReceiverPhone = snapshot.child("receiverPhone").getValue(String.class);
                    String dbSenderCity = snapshot.child("senderCity").getValue(String.class);
                    String dbSenderName = snapshot.child("senderName").getValue(String.class);
                    String dbSenderPhone = snapshot.child("senderPhone").getValue(String.class);
                    String dbShippingAddress = snapshot.child("shippingAddress").getValue(String.class);
                    buyerName = snapshot.child("buyerName").getValue(String.class);

                    deliveryGuySenderNameTxt.setText("Sender Name : " + dbSenderName);
                    deliveryGuySenderCityTxt.setText("Sender City : " + dbSenderCity);
                    deliveryGuySenderPhoneTxt.setText("Sender Phone : " + dbSenderPhone);
                    deliveryGuyReceiverNameTxt.setText("Receiver Name : " + dbReceiverName);
                    deliveryGuyReceiverCityTxt.setText("Receiver City : " + dbReceiverCity);
                    deliveryGuyReceiverPhoneTxt.setText("Receiver Phone : " + dbReceiverPhone);
                    deliveryGuyAddressTxt.setText("Shipping Address : " + dbShippingAddress);

                showOrderData(buyerName);
                }else{
                    Toast.makeText(getApplicationContext(), "Record Not Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Make sure you have an active Internet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showOrderData(String buyerName) {

        buyerOrderRef.child(buyerName).child(trackingID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String dbDeliverOrderName = snapshot.child("deliverOrderName").getValue(String.class);
                String dbDeliverOrderIV = snapshot.child("deliver0rderImg").getValue(String.class);
                String dbDeliverOrderPayment = snapshot.child("deliverOrderPaymentType").getValue(String.class);
                String dbDeliverOrderPrice = snapshot.child("deliverOrderPrice").getValue(String.class);
                String dbDeliverOrderQty = snapshot.child("deliverOrderQty").getValue(String.class);
                String dbDeliverOrderTPrice = snapshot.child("deliverOrderTotalPrice").getValue(String.class);

                if (Integer.parseInt(dbDeliverOrderTPrice) <= 100) {
                    shippingCharges = 40;
                } else if (Integer.parseInt(dbDeliverOrderTPrice) <= 200 && Integer.parseInt(dbDeliverOrderTPrice) > 100) {
                    shippingCharges = 60;
                } else if (Integer.parseInt(dbDeliverOrderTPrice) <= 400 && Integer.parseInt(dbDeliverOrderTPrice) > 200) {
                    shippingCharges = 100;
                } else if (Integer.parseInt(dbDeliverOrderTPrice) <= 800 && Integer.parseInt(dbDeliverOrderTPrice) > 400) {
                    shippingCharges = 170;
                } else if (Integer.parseInt(dbDeliverOrderTPrice) <= 1200 && Integer.parseInt(dbDeliverOrderTPrice) > 800) {
                    shippingCharges = 200;
                } else if (Integer.parseInt(dbDeliverOrderTPrice) <= 1500 && Integer.parseInt(dbDeliverOrderTPrice) > 1200) {
                    shippingCharges = 250;
                } else if (Integer.parseInt(dbDeliverOrderTPrice) <= 2000 && Integer.parseInt(dbDeliverOrderTPrice) > 1500) {
                    shippingCharges = 270;
                } else if (Integer.parseInt(dbDeliverOrderTPrice) <= 3000 && Integer.parseInt(dbDeliverOrderTPrice) > 2000) {
                    shippingCharges = 310;
                } else if (Integer.parseInt(dbDeliverOrderTPrice) <= 5000 && Integer.parseInt(dbDeliverOrderTPrice) > 3000) {
                    shippingCharges = 360;
                } else if (Integer.parseInt(dbDeliverOrderTPrice) <= 7000 && Integer.parseInt(dbDeliverOrderTPrice) > 5000) {
                    shippingCharges = 410;
                } else if (Integer.parseInt(dbDeliverOrderTPrice) <= 10000 && Integer.parseInt(dbDeliverOrderTPrice) > 7000) {
                    shippingCharges = 450;
                } else if (Integer.parseInt(dbDeliverOrderTPrice) <= 15000 && Integer.parseInt(dbDeliverOrderTPrice) > 10000) {
                    shippingCharges = 550;
                } else {
                    shippingCharges = 750;
                }

                newAmount = Float.parseFloat(dbDeliverOrderTPrice) + Float.parseFloat(String.valueOf(shippingCharges));
                newAmountTotal = String.valueOf(newAmount);

                productTitleTxt.setText(dbDeliverOrderName);
                productQtyTxt.setText(dbDeliverOrderQty);
                productPriceTxt.setText(dbDeliverOrderPrice);
                productTotalPriceTxt.setText(newAmountTotal);
                productBuyerNameTxt.setText(buyerName);
                productPaymentTypeTxt.setText(dbDeliverOrderPayment);
                Picasso.get().load(dbDeliverOrderIV).into(productIV);

                paymentType = dbDeliverOrderPayment;


//                buyerName = snapshot.child("buyerName").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        lat = String.valueOf(location.getLatitude());
        lng = String.valueOf(location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);

        //move map camera
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
//        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("IB Express needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(DeliveryGuyOrderDetail.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }

    }
}