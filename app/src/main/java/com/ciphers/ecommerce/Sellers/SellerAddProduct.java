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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.firebase.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class SellerAddProduct extends AppCompatActivity {

    TextInputLayout sellerProductNameTxt, sellerProductDescTxt, sellerProductPriceTxt, sellerProductQtyTxt;
    Spinner sellerProductCategory, sellerMainCategory, sellerThirdCategorySpinner;
    Button sellerAddProductBtn, loadSubCategoriesBtn, loadThirdCategoryBtn;
    ImageView sellerAddProductBackIcon;
    DatabaseReference categoryRef, productsRef, mainCategoryRef, thirdCategoryRef;
    ImageView sellerAddProductImgOne, sellerAddProductImgTwo, sellerAddProductImgThree, sellerAddProductImgFour;
    TextView imgLblOne, imgLblTwo, imgLblThree, imgLblFour;
    String productName, productDesc, productCategory, productPrice, productQty, productSeller;
    ProgressDialog progressDialog;
    String currentDate, currentTime, productId;
    StorageReference productsImagesStorageRef;
    String downloadImageUr1, downloadImageUr2, downloadImageUr3, downloadImageUr4;

    LinearLayout subCategoryLayout;

    int imageNo = 0;
    static final int galleryPic = 1;
    Uri imageUri1, imageUri2, imageUri3, imageUri4;

    ArrayList<String> categoryList = new ArrayList<>();
    ArrayList<String> mainCategoryList = new ArrayList<>();
    ArrayList<String> thirdCategoryList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_add_product);

        categoryRef = FirebaseDatabase.getInstance().getReference().child("Categories");
        mainCategoryRef = FirebaseDatabase.getInstance().getReference().child("MainCategory");
        productsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        productsImagesStorageRef = FirebaseStorage.getInstance().getReference().child("Products Images");
        thirdCategoryRef = FirebaseDatabase.getInstance().getReference().child("ThirdCategory");

        sellerProductNameTxt = findViewById(R.id.seller_add_product_name_txt);
        sellerProductDescTxt = findViewById(R.id.seller_add_product_description_txt);
        sellerProductPriceTxt = findViewById(R.id.seller_add_product_price_txt);
        sellerProductQtyTxt = findViewById(R.id.seller_add_product_quantity_txt);
        sellerProductCategory = findViewById(R.id.seller_add_product_category_txt);
        sellerAddProductBackIcon = findViewById(R.id.seller_add_product_back_icon);
        sellerAddProductBtn = findViewById(R.id.seller_add_product_btn);
        imgLblOne = findViewById(R.id.seller_add_product_img_lbl_one);
        imgLblTwo = findViewById(R.id.seller_add_product_img_lbl_two);
        imgLblThree = findViewById(R.id.seller_add_product_img_lbl_three);
        imgLblFour = findViewById(R.id.seller_add_product_img_lbl_four);
        subCategoryLayout = findViewById(R.id.sub_category_spinner_layout);
        sellerMainCategory = findViewById(R.id.seller_add_product_parent_category_txt);
        loadSubCategoriesBtn = findViewById(R.id.load_sub_categories_btn);
        sellerThirdCategorySpinner = findViewById(R.id.seller_add_product_third_category_txt);
        loadThirdCategoryBtn = findViewById(R.id.load_third_category_btn);

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

        progressDialog = new ProgressDialog(this);

        sellerAddProductImgOne = findViewById(R.id.seller_add_product_image_main);
        sellerAddProductImgTwo = findViewById(R.id.seller_add_product_image_two);
        sellerAddProductImgThree = findViewById(R.id.seller_add_product_image_three);
        sellerAddProductImgFour = findViewById(R.id.seller_add_product_image_four);


        sellerAddProductBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SellerAddProduct.super.onBackPressed();
            }
        });

        sellerAddProductImgOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
                imageNo = 1;
            }
        });

        sellerAddProductImgTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
                imageNo = 2;
            }
        });

        sellerAddProductImgThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
                imageNo = 3;
            }
        });

        sellerAddProductImgFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
                imageNo = 4;
            }
        });

        sellerAddProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateProductData();
            }
        });

    }

    private void validateProductData() {

        productName = sellerProductNameTxt.getEditText().getText().toString();
        productDesc = sellerProductDescTxt.getEditText().getText().toString();
        productCategory = sellerProductCategory.getSelectedItem().toString();
        productPrice = sellerProductPriceTxt.getEditText().getText().toString();
        productQty = sellerProductQtyTxt.getEditText().getText().toString();

        if (TextUtils.isEmpty(productName)) {
            Toast.makeText(getApplicationContext(), "Please Enter the Product Name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(productDesc)) {
            Toast.makeText(getApplicationContext(), "Please Enter the Product Description", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(productCategory)) {
            Toast.makeText(getApplicationContext(), "Please Enter the Product Category", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(productPrice)) {
            Toast.makeText(getApplicationContext(), "Please Enter the Product Price", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(productQty)) {
            Toast.makeText(getApplicationContext(), "Please Enter the Product Quantity", Toast.LENGTH_SHORT).show();
        } else {
            uploadProductData();
        }
    }

    private void uploadProductData() {

        progressDialog.setTitle("Adding...");
        progressDialog.setMessage("Product is adding, Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat getDate = new SimpleDateFormat("MM dd, yyyy");
        currentDate = getDate.format(calendar.getTime());

        SimpleDateFormat getTIme = new SimpleDateFormat("HH:mm:ss a");
        currentTime = getTIme.format(calendar.getTime());

        productId = currentDate + currentTime;

        final StorageReference filePath = productsImagesStorageRef.child(imageUri1.getLastPathSegment() + productId + ".png");
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


                        downloadImageUr1 = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            downloadImageUr1 = task.getResult().toString();
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

        final StorageReference filePath = productsImagesStorageRef.child(imageUri2.getLastPathSegment() + productId + ".png");
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

        final StorageReference filePath = productsImagesStorageRef.child(imageUri3.getLastPathSegment() + productId + ".png");
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

        final StorageReference filePath = productsImagesStorageRef.child(imageUri4.getLastPathSegment() + productId + ".png");
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
                            saveProductInfoToDatabase();
                        }
                    }
                });
            }
        });

    }

    private void saveProductInfoToDatabase() {

        String productKey = FirebaseDatabase.getInstance().getReference().push().getKey();

        HashMap<String, Object> productMap = new HashMap<>();

        productMap.put("productId", productKey);
        productMap.put("productImage1", downloadImageUr1);
        productMap.put("productImage2", downloadImageUr2);
        productMap.put("productImage3", downloadImageUr3);
        productMap.put("productImage4", downloadImageUr4);
        productMap.put("productParentCategory", sellerMainCategory.getSelectedItem().toString());
        productMap.put("productCategory", sellerProductCategory.getSelectedItem().toString());
        productMap.put("productThirdCategory", sellerThirdCategorySpinner.getSelectedItem().toString());
        productMap.put("productName", productName);
        productMap.put("productPrice", productPrice);
        productMap.put("productDescription", productDesc);
        productMap.put("productQuantity", productQty);
        productMap.put("productQuantityLeft", productQty);
        productMap.put("productSellerID", Prevalent.currentOnlineSeller.getSellerUsername());

        productsRef.child(productKey).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Product Added successfully", Toast.LENGTH_SHORT).show();
                    SellerAddProduct.super.onBackPressed();
                    finish();

                } else {
                    progressDialog.dismiss();
                    String errorNo = task.getException().toString();
                    Toast.makeText(getApplicationContext(), "Please Check Your Internet : " + errorNo, Toast.LENGTH_SHORT).show();
                    SellerAddProduct.super.onBackPressed();
                }
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
                sellerAddProductImgOne.setImageURI(imageUri1);
                imgLblOne.setVisibility(View.GONE);
            }
        } else if (imageNo == 2) {
            if (requestCode == galleryPic && resultCode == RESULT_OK && data != null) {
                imageUri2 = data.getData();
                sellerAddProductImgTwo.setImageURI(imageUri2);
                imgLblTwo.setVisibility(View.GONE);
            }
        } else if (imageNo == 3) {
            if (requestCode == galleryPic && resultCode == RESULT_OK && data != null) {
                imageUri3 = data.getData();
                sellerAddProductImgThree.setImageURI(imageUri3);
                imgLblThree.setVisibility(View.GONE);
            }
        } else if (imageNo == 4) {
            if (requestCode == galleryPic && resultCode == RESULT_OK && data != null) {
                imageUri4 = data.getData();
                sellerAddProductImgFour.setImageURI(imageUri4);
                imgLblFour.setVisibility(View.GONE);
            }
        }
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

    @Override
    public void onBackPressed() {
        SellerAddProduct.super.onBackPressed();
    }
}