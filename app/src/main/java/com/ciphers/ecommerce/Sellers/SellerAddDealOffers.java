package com.ciphers.ecommerce.Sellers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
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

import com.ciphers.ecommerce.Prevalent.Prevalent;
import com.ciphers.ecommerce.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class SellerAddDealOffers extends AppCompatActivity {

    int imageNo = 0;
    static final int galleryPic = 1;
    Uri imageUri1, imageUri2, imageUri3, imageUri4;

    String downloadImageUri1, downloadImageUri2, downloadImageUri3, downloadImageUri4;

    ImageView sellerDealImage1, sellerDealImage2, sellerDealImage3, sellerDealImage4, sellerDealBackBtn;
    TextInputLayout sellerDealNameTxt, sellerDealPriceTxt, sellerDealQtyTxt, sellerDealDescriptionTxt;
    Button sellerSaveDealBtn;

    String currentDate, currentTime, productId;
    ProgressDialog progressDialog;

    Spinner sellerProductCategory, sellerMainCategory, sellerThirdCategorySpinner;
    Button sellerAddProductBtn, loadSubCategoriesBtn, loadThirdCategoryBtn;
    DatabaseReference categoryRef, productsRef, mainCategoryRef, thirdCategoryRef;

    DatabaseReference dealRef;
    StorageReference dealStorageRef;

    ArrayList<String> categoryList = new ArrayList<>();
    ArrayList<String> mainCategoryList = new ArrayList<>();
    ArrayList<String> thirdCategoryList = new ArrayList<>();

    String dealName, dealPrice, dealQty, dealDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_add_deal_offers);

        dealRef = FirebaseDatabase.getInstance().getReference().child("Deals");
        dealStorageRef = FirebaseStorage.getInstance().getReference().child("Deals Images");

        sellerDealBackBtn = findViewById(R.id.seller_deal_add_back_btn);
        sellerDealImage1 = findViewById(R.id.seller_add_deal_product_image_main);
        sellerDealImage2 = findViewById(R.id.seller_add_deal_product_image_two);
        sellerDealImage3 = findViewById(R.id.seller_add_deal_product_image_three);
        sellerDealImage4 = findViewById(R.id.seller_add_deal_product_image_four);

        sellerDealNameTxt =findViewById(R.id.seller_deal_add_product_name);
        sellerDealPriceTxt = findViewById(R.id.seller_deal_add_product_price);
        sellerDealDescriptionTxt = findViewById(R.id.seller_deal_add_product_description);
        sellerDealQtyTxt = findViewById(R.id.seller_deal_add_product_quantity);

        sellerMainCategory = findViewById(R.id.seller_add_product_parent_category_txt);
        loadSubCategoriesBtn = findViewById(R.id.load_sub_categories_btn);
        sellerThirdCategorySpinner = findViewById(R.id.seller_add_product_third_category_txt);
        loadThirdCategoryBtn = findViewById(R.id.load_third_category_btn);
        sellerProductCategory = findViewById(R.id.seller_add_product_category_txt);
        sellerSaveDealBtn = findViewById(R.id.seller_add_deal_product_button);

        progressDialog = new ProgressDialog(this);

        sellerDealBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SellerAddDealOffers.super.onBackPressed();
            }
        });

        sellerDealImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
                imageNo = 1;
            }
        });

        sellerDealImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
                imageNo = 2;
            }
        });

        sellerDealImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
                imageNo = 3;
            }
        });

        sellerDealImage4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
                imageNo = 4;
            }
        });


        sellerSaveDealBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });

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

        if (imageNo == 1) {
            if (requestCode == galleryPic && resultCode == RESULT_OK && data != null) {
                imageUri1 = data.getData();
                sellerDealImage1.setImageURI(imageUri1);
            }
        } else if (imageNo == 2) {
            if (requestCode == galleryPic && resultCode == RESULT_OK && data != null) {
                imageUri2 = data.getData();
                sellerDealImage2.setImageURI(imageUri2);
            }
        } else if (imageNo == 3) {
            if (requestCode == galleryPic && resultCode == RESULT_OK && data != null) {
                imageUri3 = data.getData();
                sellerDealImage3.setImageURI(imageUri3);
            }
        } else if (imageNo == 4) {
            if (requestCode == galleryPic && resultCode == RESULT_OK && data != null) {
                imageUri4 = data.getData();
                sellerDealImage4.setImageURI(imageUri4);
            }
        }
    }

    private void validateData() {

        dealName = sellerDealNameTxt.getEditText().getText().toString().trim();
        dealPrice = sellerDealPriceTxt.getEditText().getText().toString().trim();
        dealDesc = sellerDealDescriptionTxt.getEditText().getText().toString();
        dealQty = sellerDealQtyTxt.getEditText().getText().toString();

        if(TextUtils.isEmpty(dealName)){
            Toast.makeText(getApplicationContext(), "Please Enter the Deal Name", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(dealDesc)){
            Toast.makeText(getApplicationContext(), "Please Enter the Deal Description", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(dealPrice)){
            Toast.makeText(getApplicationContext(), "Please Enter the Deal Price", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(dealQty)){
            Toast.makeText(getApplicationContext(), "Please Enter the Deal Qty", Toast.LENGTH_SHORT).show();
        }else{
            storeDataToDataBase();
        }
    }

    private void storeDataToDataBase() {
        progressDialog.setTitle("Adding...");
        progressDialog.setMessage("Deal is adding, Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat getDate = new SimpleDateFormat("MM dd, yyyy");
        currentDate = getDate.format(calendar.getTime());

        SimpleDateFormat getTIme = new SimpleDateFormat("HH:mm:ss a");
        currentTime = getTIme.format(calendar.getTime());

        productId = currentDate + currentTime;

        final StorageReference filePath = dealStorageRef.child(imageUri1.getLastPathSegment() + productId + ".png");
        final UploadTask uploadTask = filePath.putFile(imageUri1);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Uploading Image 1 Error : " + e, Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }


                        downloadImageUri1 = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            downloadImageUri1 = task.getResult().toString();
                            uploadImageTwo();
                        }
                    }
                });
            }
        });
    }
    private void uploadImageTwo() {

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat getDate = new SimpleDateFormat("MM dd, yyyy");
        currentDate = getDate.format(calendar.getTime());

        SimpleDateFormat getTIme = new SimpleDateFormat("HH:mm:ss a");
        currentTime = getTIme.format(calendar.getTime());

        productId = currentDate + currentTime;
        productId = productId + "2";

        final StorageReference filePath = dealStorageRef.child(imageUri2.getLastPathSegment() + productId + ".png");
        final UploadTask uploadTask = filePath.putFile(imageUri2);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Uploading Image Two Error : " + e, Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }


                        downloadImageUri2 = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            downloadImageUri2 = task.getResult().toString();
                            uploadImageThree();
                        }
                    }
                });
            }
        });
    }

    private void uploadImageThree() {
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat getDate = new SimpleDateFormat("MM dd, yyyy");
        currentDate = getDate.format(calendar.getTime());

        SimpleDateFormat getTIme = new SimpleDateFormat("HH:mm:ss a");
        currentTime = getTIme.format(calendar.getTime());

        productId = currentDate + currentTime;
        productId = productId + "3";

        final StorageReference filePath = dealStorageRef.child(imageUri3.getLastPathSegment() + productId + ".png");
        final UploadTask uploadTask = filePath.putFile(imageUri3);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Uploading Image Three Error : " + e, Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }


                        downloadImageUri3 = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            downloadImageUri3 = task.getResult().toString();
                            uploadImageFour();
                        }
                    }
                });
            }
        });
    }

    private void uploadImageFour() {
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat getDate = new SimpleDateFormat("MM dd, yyyy");
        currentDate = getDate.format(calendar.getTime());

        SimpleDateFormat getTIme = new SimpleDateFormat("HH:mm:ss a");
        currentTime = getTIme.format(calendar.getTime());

        productId = currentDate + currentTime;
        productId = productId + "4";

        final StorageReference filePath = dealStorageRef.child(imageUri4.getLastPathSegment() + productId + ".png");
        final UploadTask uploadTask = filePath.putFile(imageUri4);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Uploading Image Four Error : " + e, Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }


                        downloadImageUri4 = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            downloadImageUri4 = task.getResult().toString();
                            saveSpecialProductInfoToDatabase();
                        }
                    }
                });
            }
        });
    }

    private void saveSpecialProductInfoToDatabase() {
        HashMap<String, Object> dealProductMap = new HashMap<>();

        dealProductMap.put("dealProductId", productId);
        dealProductMap.put("dealProductImage1", downloadImageUri1);
        dealProductMap.put("dealProductImage2", downloadImageUri2);
        dealProductMap.put("dealProductImage3", downloadImageUri3);
        dealProductMap.put("dealProductImage4", downloadImageUri4);
        dealProductMap.put("dealProductName", dealName);
        dealProductMap.put("dealProductPrice", dealPrice);
        dealProductMap.put("dealProductDescription", dealDesc);
        dealProductMap.put("dealProductQuantity", dealQty);
        dealProductMap.put("dealProductQuantityLeft", dealQty);
        dealProductMap.put("dealProductSellerID", Prevalent.currentOnlineSeller.getSellerUsername());

        dealRef.child(productId).updateChildren(dealProductMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Deal Added successfully", Toast.LENGTH_SHORT).show();
                    SellerAddDealOffers.super.onBackPressed();
                    finish();

                } else {
                    progressDialog.dismiss();
                    String errorNo = task.getException().toString();
                    Toast.makeText(getApplicationContext(), "Please Check Your Internet : " + errorNo, Toast.LENGTH_SHORT).show();
                    SellerAddDealOffers.super.onBackPressed();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        SellerAddDealOffers.super.onBackPressed();
    }
}