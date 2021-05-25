package com.ciphers.ecommerce.Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.ciphers.ecommerce.FullScreenImageActivity;
import com.ciphers.ecommerce.Model.Deals;
import com.ciphers.ecommerce.Model.SpecialOffer;
import com.ciphers.ecommerce.Prevalent.Prevalent;
import com.ciphers.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class BuyerSpecialOfferDetailActivity extends AppCompatActivity {

    String specialOfferProductId;

    DatabaseReference specialOfferRef, dealRef;
    ImageView buyerSpecialOfferMainImage, buyerSpecialOfferTwoImage, buyerSpecialOfferThreeImage, buyerSpecialOfferFourImage, buyerSpecialOfferBackIcon;
    TextView buyerSpecialOfferTitleTxt, buyerSpecialOfferSellerTxt, buyerSpecialOfferPriceTxt, buyerSpecialOfferDescTxt, activityTypeTxt;
    FloatingActionButton productCartBtn;
    ElegantNumberButton productIncrementBtn;

    boolean isImageFitToScreen;
    String imageUri1, imageUri2, imageUri3, imageUri4, imageTitle, activityType;

    DatabaseReference cartListRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_special_offer_detail);

        specialOfferProductId = getIntent().getStringExtra("pID");
        activityType = getIntent().getStringExtra("activityType");


        specialOfferRef = FirebaseDatabase.getInstance().getReference().child("Special Offers");
        dealRef = FirebaseDatabase.getInstance().getReference().child("Deals");

        cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        buyerSpecialOfferMainImage = findViewById(R.id.buyer_special_product_detail_main_img);
        buyerSpecialOfferTwoImage = findViewById(R.id.buyer_special_product_detail_two_img);
        buyerSpecialOfferThreeImage = findViewById(R.id.buyer_special_product_detail_three_img);
        buyerSpecialOfferFourImage = findViewById(R.id.buyer_special_product_detail_four_img);
        buyerSpecialOfferBackIcon = findViewById(R.id.buyer_special_product_detail_back_img);


        activityTypeTxt = findViewById(R.id.activity_type_tv);

        buyerSpecialOfferTitleTxt = findViewById(R.id.buyer_special_product_detail_name_txt);
        buyerSpecialOfferSellerTxt = findViewById(R.id.buyer_special_product_detail_seller_txt);
        buyerSpecialOfferPriceTxt = findViewById(R.id.buyer_special_product_detail_price_txt);
        buyerSpecialOfferDescTxt = findViewById(R.id.buyer_special_product_detail_desc_txt);

        productCartBtn = findViewById(R.id.buyer_special_offer_cart_btn);
        productIncrementBtn = findViewById(R.id.special_offer_increment_btn);

        buyerSpecialOfferBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuyerSpecialOfferDetailActivity.super.onBackPressed();
            }
        });

        buyerSpecialOfferMainImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                buyerSpecialOfferMainImage.setOnTouchListener(new ImageMatrixTouchHandler(v.getContext()));
                Intent fullScreenIntent = new Intent(getApplicationContext(), FullScreenImageActivity.class);
                fullScreenIntent.setData(Uri.parse(imageUri1));
                fullScreenIntent.putExtra("image_title", imageTitle);
                startActivity(fullScreenIntent);
            }
        });

        productCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSpecialOfferToCard();
            }
        });



        buyerSpecialOfferTwoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                buyerSpecialOfferMainImage.setOnTouchListener(new ImageMatrixTouchHandler(v.getContext()));
                Intent fullScreenIntent = new Intent(getApplicationContext(), FullScreenImageActivity.class);
                fullScreenIntent.setData(Uri.parse(imageUri2));
                fullScreenIntent.putExtra("image_title", imageTitle);
                startActivity(fullScreenIntent);
            }
        });

        buyerSpecialOfferThreeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                buyerSpecialOfferMainImage.setOnTouchListener(new ImageMatrixTouchHandler(v.getContext()));
                Intent fullScreenIntent = new Intent(getApplicationContext(), FullScreenImageActivity.class);
                fullScreenIntent.setData(Uri.parse(imageUri3));
                fullScreenIntent.putExtra("image_title", imageTitle);
                startActivity(fullScreenIntent);
            }
        });

        buyerSpecialOfferFourImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                buyerSpecialOfferMainImage.setOnTouchListener(new ImageMatrixTouchHandler(v.getContext()));
                Intent fullScreenIntent = new Intent(getApplicationContext(), FullScreenImageActivity.class);
                fullScreenIntent.setData(Uri.parse(imageUri4));
                fullScreenIntent.putExtra("image_title", imageTitle);
                startActivity(fullScreenIntent);
            }
        });

        if (activityType.equals("Deal")) {
            getProductDeal();
            activityTypeTxt.setText("Deal Detail");
        } else if (activityType.equals("Special Offer")) {
            getSpecialProductDetail();
            activityTypeTxt.setText("Special Offer Detail");
        }


    }

    private void addSpecialOfferToCard() {

        String saveCurrentTime, saveCurrentDate;

        Calendar calForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("productID", specialOfferProductId);
        cartMap.put("productName", buyerSpecialOfferTitleTxt.getText().toString());
        cartMap.put("productImg", imageUri1);
        cartMap.put("productPrice", buyerSpecialOfferPriceTxt.getText().toString());
        cartMap.put("productSeller", buyerSpecialOfferSellerTxt.getText().toString());
        cartMap.put("productBuyer", Prevalent.currentOnlineUser.getUsername().toString());
        cartMap.put("productType", activityType);
        cartMap.put("productDate", saveCurrentDate);
        cartMap.put("productTime", saveCurrentTime);
        cartMap.put("productQuantity", productIncrementBtn.getNumber().toString());

        cartListRef.child(Prevalent.currentOnlineUser.username).child(specialOfferProductId).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    BuyerSpecialOfferDetailActivity.super.onBackPressed();
                    Toast.makeText(getApplicationContext(), "Product Added to Cart", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void getProductDeal() {

        dealRef.child(specialOfferProductId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    Deals deals = dataSnapshot.getValue(Deals.class);

                    buyerSpecialOfferTitleTxt.setText(deals.getDealProductName());
                    buyerSpecialOfferPriceTxt.setText(deals.getDealProductPrice());
                    buyerSpecialOfferSellerTxt.setText(deals.getDealProductSellerID());
                    buyerSpecialOfferDescTxt.setText(deals.getDealProductDescription());

                    Picasso.get().load(deals.getDealProductImage1()).into(buyerSpecialOfferMainImage);
                    Picasso.get().load(deals.getDealProductImage2()).into(buyerSpecialOfferTwoImage);
                    Picasso.get().load(deals.getDealProductImage3()).into(buyerSpecialOfferThreeImage);
                    Picasso.get().load(deals.getDealProductImage4()).into(buyerSpecialOfferFourImage);

                    imageUri1 = deals.getDealProductImage1();
                    imageUri2 = deals.getDealProductImage2();
                    imageUri3 = deals.getDealProductImage3();
                    imageUri4 = deals.getDealProductImage4();
                    imageTitle = deals.getDealProductName();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getSpecialProductDetail() {

        specialOfferRef.child(specialOfferProductId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    SpecialOffer specialOffer = dataSnapshot.getValue(SpecialOffer.class);

                    buyerSpecialOfferTitleTxt.setText(specialOffer.getSpecialProductName());
                    buyerSpecialOfferPriceTxt.setText(specialOffer.getSpecialProductPrice());
                    buyerSpecialOfferSellerTxt.setText(specialOffer.getSpecialProductSellerID());
                    buyerSpecialOfferDescTxt.setText(specialOffer.getSpecialProductDescription());

                    Picasso.get().load(specialOffer.getSpecialProductImage1()).into(buyerSpecialOfferMainImage);
                    Picasso.get().load(specialOffer.getSpecialProductImage2()).into(buyerSpecialOfferTwoImage);
                    Picasso.get().load(specialOffer.getSpecialProductImage3()).into(buyerSpecialOfferThreeImage);
                    Picasso.get().load(specialOffer.getSpecialProductImage4()).into(buyerSpecialOfferFourImage);

                    imageUri1 = specialOffer.getSpecialProductImage1();
                    imageUri2 = specialOffer.getSpecialProductImage2();
                    imageUri3 = specialOffer.getSpecialProductImage3();
                    imageUri4 = specialOffer.getSpecialProductImage4();
                    imageTitle = specialOffer.getSpecialProductName();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}