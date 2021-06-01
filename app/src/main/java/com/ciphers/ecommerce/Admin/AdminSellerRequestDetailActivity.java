package com.ciphers.ecommerce.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ciphers.ecommerce.FullScreenImageActivity;
import com.ciphers.ecommerce.R;
import com.ciphers.ecommerce.SplashScreen;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
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

public class AdminSellerRequestDetailActivity extends FragmentActivity implements OnMapReadyCallback {

    String image, fullName, shopName, username, category, email, phone, address, join, licence;

    TextView fullNameTV, shopNameTV, usernameTV, categoryTV, emailTV, phoneTV, addressTV, joinTV, licenceTV;
    ImageView sellerIV, sellerDocumentIV, sellerIDFrontIV, sellerIDBackIV, sellerVisitingCardIV;

    Button sellerApproveBtn, sellerModifyBtn;

    DatabaseReference sellerDocumentRef, sellerStatusRef;

    private GoogleMap mMap;

    String dbDocument, dbIDBack, dbIDFront, dbIDVisiting, imageTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_seller_request_detail);

        sellerDocumentRef = FirebaseDatabase.getInstance().getReference().child("SellersDocuments");
        sellerStatusRef = FirebaseDatabase.getInstance().getReference().child("Sellers");

        fullName = getIntent().getStringExtra("name");
        shopName = getIntent().getStringExtra("shopName");
        username = getIntent().getStringExtra("username");
        email = getIntent().getStringExtra("email");
        address = getIntent().getStringExtra("address");
        phone = getIntent().getStringExtra("phone");
        join = getIntent().getStringExtra("join");
        licence = getIntent().getStringExtra("licence");
        image = getIntent().getStringExtra("image");
        category = getIntent().getStringExtra("category");

        fullNameTV = findViewById(R.id.seller_request_name_txt_cv);
        shopNameTV = findViewById(R.id.seller_request_shop_name_txt_cv);
        usernameTV = findViewById(R.id.seller_request_username_txt_cv);
        categoryTV = findViewById(R.id.seller_request_category_txt_cv);
        emailTV = findViewById(R.id.seller_request_email_txt_cv);
        addressTV = findViewById(R.id.seller_request_address_txt_cv);
        phoneTV = findViewById(R.id.seller_request_phone_no_txt_cv);
        joinTV = findViewById(R.id.seller_request_join_date_txt_cv);
        licenceTV = findViewById(R.id.seller_request_licence_number_txt_cv);
        sellerIV = findViewById(R.id.seller_request_image_cv);

        sellerDocumentIV = findViewById(R.id.admin_seller_request_shop_document_IV);
        sellerIDFrontIV = findViewById(R.id.admin_seller_request_owner_cnic_front_IV);
        sellerIDBackIV = findViewById(R.id.admin_seller_request_owner_cnic_back_IV);
        sellerVisitingCardIV = findViewById(R.id.admin_seller_request_shop_visiting_card_IV);

        sellerApproveBtn = findViewById(R.id.seller_request_approve_btn);
        sellerModifyBtn = findViewById(R.id.seller_request_modification_btn);

        Picasso.get().load(image).into(sellerIV);

        fullNameTV.setText(fullName);
        shopNameTV.setText(shopName);
        usernameTV.setText(username);
        categoryTV.setText(category);
        emailTV.setText(email);
        addressTV.setText(address);
        phoneTV.setText(phone);
        joinTV.setText(join);
        licenceTV.setText(licence);

        showSellerDocuments();

        sellerApproveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence options[] = new CharSequence[]{
                        "Yes",
                        "No"
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(AdminSellerRequestDetailActivity.this);
                builder.setTitle("You are going to approve Seller Request. Are You sure you want to Approve Seller.?");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            approveSellerRequest();
                        } else {
                        }
                    }
                });
                builder.show();

            }
        });

        sellerModifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CharSequence options[] = new CharSequence[]{
                        "Yes",
                        "No"
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(AdminSellerRequestDetailActivity.this);
                builder.setTitle("You are requesting Seller To modify Changes. ");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            modifySellerRequest();
                        } else {
                        }
                    }
                });
                builder.show();
            }
        });

        sellerDocumentIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fullScreenIntent = new Intent(getApplicationContext(), FullScreenImageActivity.class);
                fullScreenIntent.setData(Uri.parse(dbDocument));
                fullScreenIntent.putExtra("image_title", "Document");
                startActivity(fullScreenIntent);
            }
        });

        sellerIDFrontIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fullScreenIntent = new Intent(getApplicationContext(), FullScreenImageActivity.class);
                fullScreenIntent.setData(Uri.parse(dbIDFront));
                fullScreenIntent.putExtra("image_title", "CNIC Front");
                startActivity(fullScreenIntent);
            }
        });

        sellerIDBackIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fullScreenIntent = new Intent(getApplicationContext(), FullScreenImageActivity.class);
                fullScreenIntent.setData(Uri.parse(dbIDBack));
                fullScreenIntent.putExtra("image_title", "CNIC Back");
                startActivity(fullScreenIntent);
            }
        });

        sellerVisitingCardIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fullScreenIntent = new Intent(getApplicationContext(), FullScreenImageActivity.class);
                fullScreenIntent.setData(Uri.parse(dbIDVisiting));
                fullScreenIntent.putExtra("image_title", "Visiting Card");
                startActivity(fullScreenIntent);
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.admin_seller_request_map);
        mapFragment.getMapAsync(this);
    }

    private void approveSellerRequest(){
        HashMap<String, Object> sellerApproveMap = new HashMap<>();
        sellerApproveMap.put("sellerStatus", "1");
        sellerStatusRef.child(username).updateChildren(sellerApproveMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent sellerApproveEmail = new Intent(getApplicationContext(), SellerRequestResponseActivity.class);
                sellerApproveEmail.putExtra("sellerEmail", email);
                sellerApproveEmail.putExtra("status", "Approve Request");
                sellerApproveEmail.putExtra("sellerShopName", shopName);
                startActivity(sellerApproveEmail);
                finish();
            }
        });
    }

    private void modifySellerRequest(){
        HashMap<String, Object> sellerApproveMap = new HashMap<>();
        sellerApproveMap.put("sellerStatus", "0");
        sellerStatusRef.child(username).updateChildren(sellerApproveMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent sellerApproveEmail = new Intent(getApplicationContext(), SellerRequestResponseActivity.class);
                sellerApproveEmail.putExtra("sellerEmail", email);
                sellerApproveEmail.putExtra("status", "Modification Request");
                sellerApproveEmail.putExtra("sellerShopName", shopName);
                startActivity(sellerApproveEmail);
                finish();
            }
        });
    }

    private void showSellerDocuments(){

        sellerDocumentRef.child(username).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dbDocument = snapshot.child("sellerDocument").getValue(String.class);
                dbIDBack = snapshot.child("sellerIDBack").getValue(String.class);
                dbIDFront = snapshot.child("sellerIDFront").getValue(String.class);
                dbIDVisiting = snapshot.child("sellerVisitingCard").getValue(String.class);

                Picasso.get().load(dbDocument).into(sellerDocumentIV);
                Picasso.get().load(dbIDBack).into(sellerIDBackIV);
                Picasso.get().load(dbIDFront).into(sellerIDFrontIV);
                Picasso.get().load(dbIDVisiting).into(sellerVisitingCardIV);


                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocationName(address, 1);
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
                } catch (Exception e) { }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
}