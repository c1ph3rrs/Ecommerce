package com.ciphers.ecommerce.Sellers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ciphers.ecommerce.Model.Products;
import com.ciphers.ecommerce.Prevalent.Prevalent;
import com.ciphers.ecommerce.R;
import com.ciphers.ecommerce.SplashScreen;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import io.paperdb.Paper;

public class SellerEditProductActivity extends AppCompatActivity {


    ImageView backIV, saveIV, mainIV, twoIV, threeIV, fourIV, dellIcon;

    TextInputLayout titleTxt, descTxt, priceTxt, qtyTxt;
    Spinner mainCategorySpinner, subCategorySpinner, thirdCategorySpinner;
    Button loadSubCategoryBtn, loadThirdCategoryBtn;
    TextView sellerProductCategoryTxt;
    ProgressDialog loadingDialog;

    ArrayList<String> categoryList = new ArrayList<>();
    ArrayList<String> mainCategoryList = new ArrayList<>();
    ArrayList<String> thirdCategoryList = new ArrayList<>();

    DatabaseReference categoryRef, productsRef, mainCategoryRef, thirdCategoryRef;
    String productId = "";

    String imageUri1, imageUri2, imageUri3, imageUri4, imageTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_edit_product);

        productId = getIntent().getStringExtra("productID");

        mainCategoryRef = FirebaseDatabase.getInstance().getReference().child("MainCategory");
        categoryRef = FirebaseDatabase.getInstance().getReference().child("Categories");
        thirdCategoryRef = FirebaseDatabase.getInstance().getReference().child("ThirdCategory");
        productsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        backIV = findViewById(R.id.seller_edit_product_back_icon);
        saveIV = findViewById(R.id.save_product_icon);

        loadSubCategoryBtn = findViewById(R.id.seller_edit_product_load_sub_spinner_btn);
        loadThirdCategoryBtn = findViewById(R.id.seller_edit_product_load_third_spinner_btn);

        mainIV = findViewById(R.id.product_image_one);
        twoIV = findViewById(R.id.product_image_two);
        threeIV = findViewById(R.id.product_image_three);
        fourIV = findViewById(R.id.product_image_four);
        dellIcon = findViewById(R.id.product_delete_icon);

        titleTxt = findViewById(R.id.seller_edit_product_name_txt);
        descTxt = findViewById(R.id.seller_edit_product_desc_txt);
        priceTxt = findViewById(R.id.seller_edit_product_price_txt);
        qtyTxt = findViewById(R.id.seller_edit_product_qty_txt);
        sellerProductCategoryTxt = findViewById(R.id.seller_product_category_txt);

        mainCategorySpinner = findViewById(R.id.seller_edit_product_main_spinner);
        subCategorySpinner = findViewById(R.id.seller_edit_product_load_sub_spinner);
        thirdCategorySpinner = findViewById(R.id.seller_edit_product_load_third_spinner);

        loadingDialog = new ProgressDialog(this);

        loadSubCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadSubCategories();
            }
        });

        loadThirdCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadThirdCategory();
            }
        });

        saveIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence options[] = new CharSequence[]{
                        "Yes",
                        "No"
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(SellerEditProductActivity.this);
                builder.setTitle("Are you sure you want to Save this.?");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            validateData();
                        } else {
                        }
                    }
                });
                builder.show();
            }
        });

        dellIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence options[] = new CharSequence[]{
                        "Yes",
                        "No"
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(SellerEditProductActivity.this);
                builder.setTitle("Are you sure you want to Delete this.?");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            deleteProduct();
                        } else { }
                    }
                });
                builder.show();
            }
        });

        loadMainCategory();

        getProductDetail(productId);

    }

    private void getProductDetail(String productId){

        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Products");

        productRef.child(productId).addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    Products products = dataSnapshot.getValue(Products.class);

                    titleTxt.getEditText().setText(products.getProductName());
                    descTxt.getEditText().setText(products.getProductPrice());
                    priceTxt.getEditText().setText(products.getProductSellerID());
                    qtyTxt.getEditText().setText(products.getProductDescription());

                    sellerProductCategoryTxt.setText(products.getProductParentCategory() +" > " + products.getProductCategory() + " > " + products.getProductThirdCategory());

                    Picasso.get().load(products.getProductImage1()).into(mainIV);
                    Picasso.get().load(products.getProductImage2()).into(twoIV);
                    Picasso.get().load(products.getProductImage3()).into(threeIV);
                    Picasso.get().load(products.getProductImage4()).into(fourIV);

                    imageUri1 = products.getProductImage1();
                    imageUri2 = products.getProductImage2();
                    imageUri3 = products.getProductImage3();
                    imageUri4 = products.getProductImage4();
                    imageTitle = products.getProductName();

                }else{
                    Toast.makeText(getApplicationContext(), "Data not exist", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void deleteProduct(){
        loadingDialog.setTitle("Deleting..");
        loadingDialog.setMessage("Please wait while we are deleting...");
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.show();

        productsRef.child(productId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                loadingDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Record Deleted", Toast.LENGTH_LONG).show();
                SellerEditProductActivity.super.onBackPressed();
            }
        });

    }

    private void validateData() {

        if (TextUtils.isEmpty(titleTxt.getEditText().getText().toString().trim())) {
            Toast.makeText(getApplicationContext(), "Please Enter Product Title", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(descTxt.getEditText().getText().toString().trim())) {
            Toast.makeText(getApplicationContext(), "Please Enter Product Description", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(priceTxt.getEditText().getText().toString().trim())) {
            Toast.makeText(getApplicationContext(), "Please Enter Product Price", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(qtyTxt.getEditText().getText().toString().trim())) {
            Toast.makeText(getApplicationContext(), "Please Enter Product Quantity", Toast.LENGTH_LONG).show();
        } else {
            uploadProductRecord();
        }

    }



    private void uploadProductRecord() {

        loadingDialog.setTitle("Updating..");
        loadingDialog.setMessage("Please wait while we are updating...");
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.show();

        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("productId", productId);
        productMap.put("productImage1", imageUri1);
        productMap.put("productImage2", imageUri2);
        productMap.put("productImage3", imageUri3);
        productMap.put("productImage4", imageUri4);
        productMap.put("productParentCategory", mainCategorySpinner.getSelectedItem().toString());
        productMap.put("productCategory", subCategorySpinner.getSelectedItem().toString());
        productMap.put("productThirdCategory", thirdCategorySpinner.getSelectedItem().toString());
        productMap.put("productName", titleTxt.getEditText().getText().toString());
        productMap.put("productPrice", priceTxt.getEditText().getText().toString());
        productMap.put("productDescription", descTxt.getEditText().getText().toString());
        productMap.put("productQuantity", qtyTxt.getEditText().getText().toString());
        productMap.put("productQuantityLeft", qtyTxt.getEditText().getText().toString());
        productMap.put("productSellerID", Prevalent.currentOnlineSeller.getSellerUsername());

        productsRef.child(productId).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                loadingDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Record Updated", Toast.LENGTH_LONG).show();
                SellerEditProductActivity.super.onBackPressed();
            }
        });

    }

    private void loadMainCategory() {

        mainCategoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mainCategoryList.clear();
                for (DataSnapshot items : snapshot.getChildren()) {
                    mainCategoryList.add(items.child("productCategory").getValue(String.class));
                }
                ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, mainCategoryList);
                categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mainCategorySpinner.setAdapter(categoryAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void loadSubCategories() {

        categoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryList.clear();
                for (DataSnapshot items : snapshot.getChildren()) {

                    if (items.child("parentCategory").getValue(String.class).equals(mainCategorySpinner.getSelectedItem().toString())) {
                        categoryList.add(items.child("productCategory").getValue(String.class));

                    } else {

                    }
                }
                ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, categoryList);
                categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                subCategorySpinner.setAdapter(categoryAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void loadThirdCategory() {

        thirdCategoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                thirdCategoryList.clear();
                for (DataSnapshot items : snapshot.getChildren()) {

                    if (items.child("parentCategory").getValue(String.class).equals(subCategorySpinner.getSelectedItem().toString())) {
                        thirdCategoryList.add(items.child("productCategory").getValue(String.class));

                    } else {

                    }
                }
                ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, thirdCategoryList);
                categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                thirdCategorySpinner.setAdapter(categoryAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}