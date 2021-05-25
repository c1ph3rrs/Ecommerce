package com.ciphers.ecommerce.Sellers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ciphers.ecommerce.Admin.AdminProductDetail;
import com.ciphers.ecommerce.Prevalent.Prevalent;
import com.ciphers.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class SellerViewSpecialOffers extends AppCompatActivity {

    String imageUri1, imageUri2, imageUri3, imageUri4, imageTitle;

    ImageView closeIcon, binIcon;

    ImageView sellerProductImage1, sellerProductImage2, sellerProductImage3, sellerProductImage4;
    TextView productCategoryTxt, productNameTxt, productSellerUsernameTxt, productPriceTxt, productDescriptionTxt;

    DatabaseReference categoryRef, specialOfferProductRef;

    String activityType, productID;
    ProgressDialog loadingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_view_special_offers);

        categoryRef = FirebaseDatabase.getInstance().getReference().child("Categories");

        productID = getIntent().getStringExtra("specialProductID");
        activityType = getIntent().getStringExtra("activityType");

        specialOfferProductRef = FirebaseDatabase.getInstance().getReference().child("Special Offers");

        loadingDialog = new ProgressDialog(this);

        closeIcon = findViewById(R.id.seller_view_special_offer_back_icon);
        binIcon = findViewById(R.id.special_offer_delete_icon);


        productCategoryTxt = findViewById(R.id.special_product_category_txt);
        productNameTxt = findViewById(R.id.product_detail_name);
        productSellerUsernameTxt = findViewById(R.id.product_seller_name);
        productPriceTxt = findViewById(R.id.product_detail_price);
        productDescriptionTxt = findViewById(R.id.product_detail_description);


        sellerProductImage1 = findViewById(R.id.seller_view_special_product_image_main);
        sellerProductImage2 = findViewById(R.id.seller_view_special_product_image_two);
        sellerProductImage3 = findViewById(R.id.seller_view_special_product_image_three);
        sellerProductImage4 = findViewById(R.id.seller_view_special_product_image_four);


        closeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SellerViewSpecialOffers.super.onBackPressed();
            }
        });

        binIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.setTitle("Deleting..");
                loadingDialog.setMessage("Please wait while we are deleting");
                loadingDialog.setCanceledOnTouchOutside(false);
                loadingDialog.show();


                deleteSpecialOfferImageFirst();
            }
        });

        fetchSpecialOfferRecord();
    }

    private void deleteSpecialOfferImageFirst(){

        StorageReference imageOne = FirebaseStorage.getInstance().getReferenceFromUrl(imageUri1);
        imageOne.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                deleteSecondImage();
            }
        });
    }

    private void deleteSecondImage(){

        StorageReference imageTwo = FirebaseStorage.getInstance().getReferenceFromUrl(imageUri2);
        imageTwo.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                deleteThirdImage();
            }
        });

    }

    private void deleteThirdImage(){

        StorageReference imageThree = FirebaseStorage.getInstance().getReferenceFromUrl(imageUri3);
        imageThree.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                deleteFourImage();
            }
        });

    }

    private void deleteFourImage(){

        StorageReference imageFour = FirebaseStorage.getInstance().getReferenceFromUrl(imageUri4);
        imageFour.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                deleteSpecialOfferProduct();
            }
        });

    }

    private void fetchSpecialOfferRecord() {

        specialOfferProductRef.child(productID).addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    productNameTxt.setText(snapshot.child("specialProductName").getValue(String.class));
                    productSellerUsernameTxt.setText(snapshot.child("specialProductSellerID").getValue(String.class));
                    productPriceTxt.setText(snapshot.child("specialProductPrice").getValue(String.class));
                    productDescriptionTxt.setText(snapshot.child("specialProductDescription").getValue(String.class));

                    String mainCategory = snapshot.child("productParentCategory").getValue(String.class);
                    String subCategory = snapshot.child("productCategory").getValue(String.class);
                    String thirdCategory = snapshot.child("productThirdCategory").getValue(String.class);

                    productCategoryTxt.setText(mainCategory +  " > " + subCategory +  " > " + thirdCategory);

                    Picasso.get().load(snapshot.child("specialProductImage1").getValue(String.class)).into(sellerProductImage1);
                    Picasso.get().load(snapshot.child("specialProductImage2").getValue(String.class)).into(sellerProductImage2);
                    Picasso.get().load(snapshot.child("specialProductImage3").getValue(String.class)).into(sellerProductImage3);
                    Picasso.get().load(snapshot.child("specialProductImage4").getValue(String.class)).into(sellerProductImage4);

                    imageUri1 = snapshot.child("specialProductImage1").getValue(String.class);
                    imageUri2 = snapshot.child("specialProductImage2").getValue(String.class);
                    imageUri3 = snapshot.child("specialProductImage3").getValue(String.class);
                    imageUri4 = snapshot.child("specialProductImage4").getValue(String.class);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void deleteSpecialOfferProduct() {
        specialOfferProductRef.child(productID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                loadingDialog.dismiss();
                SellerViewSpecialOffers.super.onBackPressed();
                Toast.makeText(getApplicationContext(), "Product Deleted Successfully", Toast.LENGTH_LONG).show();
            }
        });
    }
}