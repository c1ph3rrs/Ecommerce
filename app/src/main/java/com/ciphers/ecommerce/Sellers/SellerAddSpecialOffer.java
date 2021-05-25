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

public class SellerAddSpecialOffer extends AppCompatActivity {

    Spinner sellerProductCategory, sellerMainCategory, sellerThirdCategorySpinner;
    Button sellerAddProductBtn, loadSubCategoriesBtn, loadThirdCategoryBtn;

    ImageView specialProductImageMain, specialProductImageTwo, specialProductImageThree, specialProductImageFour;
    ImageView specialProductBackIcon;

    ArrayList<String> categoryList = new ArrayList<>();
    ArrayList<String> mainCategoryList = new ArrayList<>();
    ArrayList<String> thirdCategoryList = new ArrayList<>();

    int imageNo = 0;
    static final int galleryPic = 1;
    Uri imageUri1, imageUri2, imageUri3, imageUri4;

    String downloadImageUrl, productName, productCategory, productPrice, productSalePrice, productQuantity, productDescription;
    String downloadImageUr2, downloadImageUr3, downloadImageUr4;
    String currentDate, currentTime, productId;

    ProgressDialog progressDialog;
    Button addSpecialOfferProductBtn;
    TextInputLayout productNameTxt, productPriceTxt, productDescriptionTxt, productQuantityTxt;
    TextView commissionPrice;

    DatabaseReference categoryRef, specialOfferProductRef;
    StorageReference specialOfferProductImagesRef;
    DatabaseReference mainCategoryRef, thirdCategoryRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_add_special_offer);

        categoryRef = FirebaseDatabase.getInstance().getReference().child("Categories");
        mainCategoryRef = FirebaseDatabase.getInstance().getReference().child("MainCategory");
        thirdCategoryRef = FirebaseDatabase.getInstance().getReference().child("ThirdCategory");
        specialOfferProductRef = FirebaseDatabase.getInstance().getReference().child("Special Offers");
        specialOfferProductImagesRef = FirebaseStorage.getInstance().getReference().child("Special Offers Images");

        sellerMainCategory = findViewById(R.id.seller_add_product_parent_category_txt);
        loadSubCategoriesBtn = findViewById(R.id.load_sub_categories_btn);
        sellerThirdCategorySpinner = findViewById(R.id.seller_add_product_third_category_txt);
        loadThirdCategoryBtn = findViewById(R.id.load_third_category_btn);
        sellerProductCategory = findViewById(R.id.seller_add_product_category_txt);

        progressDialog = new ProgressDialog(this);

        specialProductBackIcon = findViewById(R.id.seller_add_special_offer_back_icon);
        specialProductImageMain = findViewById(R.id.seller_add_special_product_image_main);
        specialProductImageTwo = findViewById(R.id.seller_add_special_product_image_two);
        specialProductImageThree = findViewById(R.id.seller_add_special_product_image_three);
        specialProductImageFour = findViewById(R.id.seller_add_special_product_image_four);

        addSpecialOfferProductBtn = findViewById(R.id.admin_add_special_product_button);

        productNameTxt = findViewById(R.id.seller_add_special_product_name);
        productPriceTxt = findViewById(R.id.seller_add_special_product_price);
        productDescriptionTxt = findViewById(R.id.seller_add_special_product_description);
        productQuantityTxt = findViewById(R.id.admin_add_special_product_quantity);

        specialProductBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SellerAddSpecialOffer.super.onBackPressed();
            }
        });

        specialProductImageMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
                imageNo = 1;
            }
        });

        specialProductImageTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
                imageNo = 2;
            }
        });

        specialProductImageThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
                imageNo = 3;
            }
        });

        specialProductImageFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
                imageNo = 4;
            }
        });

        addSpecialOfferProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });

        showMainCategorySpinner();

        loadSubCategoriesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDataSpinner();
            }
        });

        loadThirdCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadThirdCategory();
            }
        });
    }

    private void showDataSpinner() {
        categoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryList.clear();
                for (DataSnapshot items : snapshot.getChildren()) {

                    if (items.child("parentCategory").getValue(String.class).equals(sellerMainCategory.getSelectedItem().toString())) {
                        categoryList.add(items.child("productCategory").getValue(String.class));

                    } else {
                        Log.d("Spinner Value ", "not found" + items.child("productCategory").getValue(String.class));
                    }
                }
                ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, categoryList);
                categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sellerProductCategory.setAdapter(categoryAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadThirdCategory(){

        thirdCategoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                thirdCategoryList.clear();
                for (DataSnapshot items : snapshot.getChildren()) {

                    if (items.child("parentCategory").getValue(String.class).equals(sellerProductCategory.getSelectedItem().toString())) {
                        thirdCategoryList.add(items.child("productCategory").getValue(String.class));

                    } else {
                        Log.d("Spinner Value ", "not found" + items.child("productCategory").getValue(String.class));
                    }
                }
                ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, thirdCategoryList);
                categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sellerThirdCategorySpinner.setAdapter(categoryAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void showMainCategorySpinner() {

        mainCategoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mainCategoryList.clear();
                for (DataSnapshot items : snapshot.getChildren()) {
                    mainCategoryList.add(items.child("productCategory").getValue(String.class));
                }
                ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, mainCategoryList);
                categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sellerMainCategory.setAdapter(categoryAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
                specialProductImageMain.setImageURI(imageUri1);
            }
        } else if (imageNo == 2) {
            if (requestCode == galleryPic && resultCode == RESULT_OK && data != null) {
                imageUri2 = data.getData();
                specialProductImageTwo.setImageURI(imageUri2);
            }
        } else if (imageNo == 3) {
            if (requestCode == galleryPic && resultCode == RESULT_OK && data != null) {
                imageUri3 = data.getData();
                specialProductImageThree.setImageURI(imageUri3);
            }
        } else if (imageNo == 4) {
            if (requestCode == galleryPic && resultCode == RESULT_OK && data != null) {
                imageUri4 = data.getData();
                specialProductImageFour.setImageURI(imageUri4);
            }
        }
    }

    private void validateData() {

        productName = productNameTxt.getEditText().getText().toString();
        productPrice = productPriceTxt.getEditText().getText().toString();
        productQuantity = productQuantityTxt.getEditText().getText().toString();
        productDescription = productDescriptionTxt.getEditText().getText().toString();


        if(TextUtils.isEmpty(productName)){
            Toast.makeText(getApplicationContext(), "Please Enter the Product Name", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(productPrice)){
            Toast.makeText(getApplicationContext(), "Please Enter the Product Price", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty((productQuantity))){
            Toast.makeText(getApplicationContext(), "Please Enter the Product Quantity", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(productDescription)){
            Toast.makeText(getApplicationContext(), "Please Enter the Product Description", Toast.LENGTH_SHORT).show();
        }else{
            storeSpecialProductInto();
        }
    }

    private void storeSpecialProductInto() {
        progressDialog.setTitle("Adding...");
        progressDialog.setMessage("Special Product is adding, Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat getDate = new SimpleDateFormat("MM dd, yyyy");
        currentDate = getDate.format(calendar.getTime());

        SimpleDateFormat getTIme = new SimpleDateFormat("HH:mm:ss a");
        currentTime = getTIme.format(calendar.getTime());

        productId = currentDate + currentTime;

        final StorageReference filePath = specialOfferProductImagesRef.child(imageUri1.getLastPathSegment() + productId + ".png");
        final UploadTask uploadTask = filePath.putFile(imageUri1);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Uploading Main Image Error : " + e, Toast.LENGTH_SHORT).show();
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


                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            downloadImageUrl = task.getResult().toString();
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

        final StorageReference filePath = specialOfferProductImagesRef.child(imageUri2.getLastPathSegment() + productId + ".png");
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


                        downloadImageUr2 = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            downloadImageUr2 = task.getResult().toString();
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

        final StorageReference filePath = specialOfferProductImagesRef.child(imageUri3.getLastPathSegment() + productId + ".png");
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


                        downloadImageUr3 = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            downloadImageUr3 = task.getResult().toString();
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

        final StorageReference filePath = specialOfferProductImagesRef.child(imageUri4.getLastPathSegment() + productId + ".png");
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


                        downloadImageUr4 = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            downloadImageUr4 = task.getResult().toString();
                            saveSpecialProductInfoToDatabase();
                        }
                    }
                });
            }
        });
    }

    private void saveSpecialProductInfoToDatabase() {

        String key = FirebaseDatabase.getInstance().getReference().push().getKey();

        HashMap<String, Object> specialOfferProductMap = new HashMap<>();

        specialOfferProductMap.put("specialOfferProductId", key);
        specialOfferProductMap.put("specialProductImage1", downloadImageUrl);
        specialOfferProductMap.put("specialProductImage2", downloadImageUr2);
        specialOfferProductMap.put("specialProductImage3", downloadImageUr3);
        specialOfferProductMap.put("specialProductImage4", downloadImageUr4);
        specialOfferProductMap.put("productParentCategory", sellerMainCategory.getSelectedItem().toString());
        specialOfferProductMap.put("productCategory", sellerProductCategory.getSelectedItem().toString());
        specialOfferProductMap.put("productThirdCategory", sellerThirdCategorySpinner.getSelectedItem().toString());
        specialOfferProductMap.put("specialProductName", productName);
        specialOfferProductMap.put("specialProductPrice", productPrice);
        specialOfferProductMap.put("specialProductDescription", productDescription);
        specialOfferProductMap.put("specialProductQuantity", productQuantity);
        specialOfferProductMap.put("specialProductQuantityLeft", productQuantity);
        specialOfferProductMap.put("specialProductSellerID", Prevalent.currentOnlineSeller.getSellerUsername());

        specialOfferProductRef.child(key).updateChildren(specialOfferProductMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Special Offer Added successfully", Toast.LENGTH_SHORT).show();
                    SellerAddSpecialOffer.super.onBackPressed();
                    finish();

                } else {
                    progressDialog.dismiss();
                    String errorNo = task.getException().toString();
                    Toast.makeText(getApplicationContext(), "Please Check Your Internet : " + errorNo, Toast.LENGTH_SHORT).show();
                    SellerAddSpecialOffer.super.onBackPressed();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        SellerAddSpecialOffer.super.onBackPressed();
    }
}