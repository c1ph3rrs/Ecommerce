package com.ciphers.ecommerce.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.ciphers.ecommerce.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddNewProducts extends AppCompatActivity {

    ImageView categoryBackButton, categoryProductImage;
    Button saveProductButton;
    TextInputLayout productNameTxt, productDescriptionTxt, productPriceTxt;

    static final int galleryPic = 1;
    Uri imageUri;
    StorageReference productImagesRef;
    DatabaseReference productReference;

    String productCategory, productName, productDescription, productPrice, currentDate, currentTime, productId;
    String downloadImageUrl;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_products);

        productCategory = getIntent().getExtras().get("category").toString();
        productImagesRef = FirebaseStorage.getInstance().getReference().child("Product Images");

        productReference = FirebaseDatabase.getInstance().getReference().child("Products");

        categoryBackButton = findViewById(R.id.category_page_back_icon);
        saveProductButton = findViewById(R.id.category_product_button);

        productNameTxt = findViewById(R.id.category_product_name);
        productDescriptionTxt = findViewById(R.id.category_product_description);
        productPriceTxt = findViewById(R.id.category_product_price);

        categoryBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdminAddNewProducts.super.onBackPressed();
            }
        });

        categoryProductImage = findViewById(R.id.category_product_image);
        categoryProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        saveProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateForm();
            }
        });

        progressDialog = new ProgressDialog(this);

    }

    private void openGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, galleryPic);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == galleryPic && resultCode == RESULT_OK && data != null){
            imageUri = data.getData();
            categoryProductImage.setImageURI(imageUri);
        }

    }

    private void validateForm() {
        productName = productNameTxt.getEditText().getText().toString().trim();
        productDescription = productDescriptionTxt.getEditText().getText().toString().trim();
        productPrice = productPriceTxt.getEditText().getText().toString().trim();

        if(imageUri == null){
            Toast.makeText(getApplicationContext(), "Please Select the Image", Toast.LENGTH_SHORT).show();
        } else if(TextUtils.isEmpty(productName))
            Toast.makeText(getApplicationContext(), "Please Enter Product Name", Toast.LENGTH_SHORT).show();
        else if(TextUtils.isEmpty(productDescription))
            Toast.makeText(getApplicationContext(), "Please Enter Product Category", Toast.LENGTH_SHORT).show();
        else if(TextUtils.isEmpty(productPrice))
            Toast.makeText(getApplicationContext(), "Please Enter Product Price", Toast.LENGTH_SHORT).show();
        else
            storeProductInformation();
    }

    private void storeProductInformation() {

        progressDialog.setTitle("Adding...");
        progressDialog.setMessage("Product adding, Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat getDate = new SimpleDateFormat("MM dd, yyyy");
        currentDate = getDate.format(calendar.getTime());

        SimpleDateFormat getTIme = new SimpleDateFormat("HH:mm:ss a");
        currentTime = getTIme.format(calendar.getTime());

        productId = currentDate + currentTime;

        final StorageReference filePath = productImagesRef.child(imageUri.getLastPathSegment() + productId + ".jpg");
        final UploadTask uploadTask = filePath.putFile(imageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Uploading Error : " + e, Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //Toast.makeText(getApplicationContext(), "Image Uploaded Sucessful", Toast.LENGTH_SHORT).show();

                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            throw task.getException();
                        }


                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            downloadImageUrl = task.getResult().toString();
                            //Toast.makeText(getApplicationContext(), "Got Image Url", Toast.LENGTH_SHORT).show();

                            saveProductInfoToDatabase();
                        }
                    }
                });
            }
        });

    }

    private void saveProductInfoToDatabase() {


        HashMap<String, Object> productMap = new HashMap<>();

        productMap.put("productID", productId);
        productMap.put("productDate", currentDate);
        productMap.put("productTime", currentTime);
        productMap.put("productDescription", productDescription);
        productMap.put("productImage", downloadImageUrl);
        productMap.put("productCategory", productCategory);
        productMap.put("productName", productName);
        productMap.put("productPrice", productPrice);
        productReference.child(productId).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Product Added successfully", Toast.LENGTH_SHORT).show();

                    AdminAddNewProducts.super.onBackPressed();
                    finish();

                }

                else{
                    progressDialog.dismiss();
                    String errorNo = task.getException().toString();
                    Toast.makeText(getApplicationContext(), "Adding Error : " + errorNo, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
       AdminAddNewProducts.super.onBackPressed();
    }

}