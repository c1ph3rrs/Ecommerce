package com.ciphers.ecommerce.Admin;

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
import android.widget.Toast;

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
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import static com.ciphers.ecommerce.R.drawable.insert_photo;

public class AdminAddNewCategory extends AppCompatActivity {

    Button addNewCategoryBtn, loadSubCategoryBtn;
    ImageView addNewCategoryBackIcon, categoryIV;
    TextInputLayout categoryNameTxt;
    String currentDate, currentTime, productId;

    DatabaseReference categoryReference, shopCategoryRef, mainCategoryRef, thirdCategoryRef;
    StorageReference categoryImageStorageRef;
    int selectedPosition = 0;

    static final int galleryPic = 1;
    Uri imageUri1 = Uri.EMPTY;
    String downloadImageUr1;
    String categoryName;
    ArrayList<String> userTypeList = new ArrayList<>();
    ArrayList<String> mainTypeList = new ArrayList<>();
    ArrayList<String> subTypeList = new ArrayList<>();
    Spinner productCategoryTypeSpinner, mainCategorySpinner, subCategorySpinner;

    ProgressDialog progressDialog;
    String activityType;
    String databaseChecker = "";
    LinearLayout subCategoryLayout, mainCategoryLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_category);

        activityType = getIntent().getStringExtra("type");

        categoryReference = FirebaseDatabase.getInstance().getReference().child("Categories");
        mainCategoryRef = FirebaseDatabase.getInstance().getReference().child("MainCategory");
        shopCategoryRef = FirebaseDatabase.getInstance().getReference().child("ShopCategory");
        thirdCategoryRef = FirebaseDatabase.getInstance().getReference().child("ThirdCategory");

        categoryImageStorageRef = FirebaseStorage.getInstance().getReference().child("categoryIcons");

        addNewCategoryBackIcon = findViewById(R.id.add_new_category_page_back_icon);
        addNewCategoryBtn = findViewById(R.id.add_new_category_btn);
        categoryNameTxt = findViewById(R.id.admin_new_category_name_txt);
        productCategoryTypeSpinner = findViewById(R.id.choose_category_type_spinner);
        mainCategorySpinner = findViewById(R.id.choose_main_category_type_spinner);
        subCategorySpinner = findViewById(R.id.choose_sub_category_type_spinner);
        categoryIV = findViewById(R.id.category_image_insert_iv);
        subCategoryLayout = findViewById(R.id.sub_category_layout);
        loadSubCategoryBtn = findViewById(R.id.load_sub_category_btn);

        if (activityType.equals("new shop category")) {
            productCategoryTypeSpinner.setVisibility(View.GONE);
            subCategoryLayout.setVisibility(View.GONE);
        } else {
            productCategoryTypeSpinner.setVisibility(View.VISIBLE);
        }

        loadSubCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadSubCategories();
            }
        });

        categoryIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        addNewCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activityType.equals("new shop category")) {
                    shopCategoryValidate();
                } else {
                    addCategoryValidate();
                }
            }
        });

        addNewCategoryBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdminAddNewCategory.super.onBackPressed();
            }
        });

        progressDialog = new ProgressDialog(this);


        showMainCategorySpinnerData();

        showSpinnerData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == galleryPic && resultCode == RESULT_OK && data != null) {
            imageUri1 = data.getData();
            categoryIV.setImageURI(imageUri1);
        }
    }

    private void openGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, galleryPic);
    }

    private void uploadProductData() {

        progressDialog.setTitle("Adding...");
        progressDialog.setMessage("Category is adding, Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        String productId = FirebaseDatabase.getInstance().getReference().push().getKey();

        HashMap<String, Object> categoryMap = new HashMap<>();

        categoryMap.put("productCategory", categoryName);
        categoryMap.put("productID", productId);
        categoryMap.put("parentCategory", mainCategorySpinner.getSelectedItem().toString());

        categoryReference.child(productId).updateChildren(categoryMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Category Added successfully", Toast.LENGTH_SHORT).show();
//                    categoryIV.setBackgroundResource(insert_photo);
//                    imageUri1 = Uri.EMPTY;
                } else {
                    Toast.makeText(getApplicationContext(), "Make sure you have an active Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void showMainCategorySpinnerData() {

        mainCategoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mainTypeList.clear();
                for (DataSnapshot items : snapshot.getChildren()) {

                    mainTypeList.add(items.child("productCategory").getValue(String.class));

                }
                ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, mainTypeList);
                categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mainCategorySpinner.setAdapter(categoryAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void loadSubCategories() {
        categoryReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                subTypeList.clear();
                for (DataSnapshot items : snapshot.getChildren()) {

                    if (items.child("parentCategory").getValue(String.class).equals(mainCategorySpinner.getSelectedItem().toString())) {
                        subTypeList.add(items.child("productCategory").getValue(String.class));

                    } else {
                        Log.d("Spinner Value ", "not found" + items.child("productCategory").getValue(String.class));
                    }


                }
                ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, subTypeList);
                categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                subCategorySpinner.setAdapter(categoryAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    private void showSpinnerData() {

        userTypeList.clear();
        userTypeList.add("Main Category");
        userTypeList.add("Sub Category");
        userTypeList.add("Third Category");
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, userTypeList);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        productCategoryTypeSpinner.setAdapter(categoryAdapter);

    }

    private void shopCategoryValidate() {

        categoryName = categoryNameTxt.getEditText().getText().toString().trim();

        if (TextUtils.isEmpty(categoryName)) {
            Toast.makeText(getApplicationContext(), "Please Enter the Category Name First", Toast.LENGTH_SHORT).show();
        } else {
            categoryName = categoryName.substring(0, 1).toUpperCase() + categoryName.substring(1).toLowerCase();
            storeShopCategoryInfo();
        }
    }

    private void storeShopCategoryInfo() {

        String shopKey = FirebaseDatabase.getInstance().getReference().push().getKey();

        progressDialog.setTitle("Adding...");
        progressDialog.setMessage("Product Category is adding, Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        HashMap<String, Object> shopCategory = new HashMap<>();
        shopCategory.put("shopCategory", categoryName);
        shopCategoryRef.child(shopKey).updateChildren(shopCategory).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Shop Category is Added", Toast.LENGTH_LONG).show();
                categoryNameTxt.getEditText().getText().clear();
            }
        });
    }

    private void addCategoryValidate() {

        categoryName = categoryNameTxt.getEditText().getText().toString().trim();

        if (TextUtils.isEmpty(categoryName)) {
            Toast.makeText(getApplicationContext(), "Please Enter the Category Name First", Toast.LENGTH_SHORT).show();
        } else {

            if (productCategoryTypeSpinner.getSelectedItem().equals("Sub Category")) {

                uploadProductData();

            } else if (productCategoryTypeSpinner.getSelectedItem().equals("Main Category")) {


                    saveMainCategoryInfoToDatabase();


            } else if (productCategoryTypeSpinner.getSelectedItem().equals("Third Category")) {

                if (imageUri1.equals(Uri.EMPTY)) {
                    Toast.makeText(AdminAddNewCategory.this, "Please Choose Image First", Toast.LENGTH_SHORT).show();
                } else {
                    uploadCategoryImage();
                }

            }
        }
    }

    private void uploadCategoryImage() {

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

        final StorageReference filePath = categoryImageStorageRef.child(imageUri1.getLastPathSegment() + productId + ".png");
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


                                saveCategoryInfoToDatabase();

                        }
                    }
                });
            }
        });

    }

    private void saveMainCategoryInfoToDatabase() {

        progressDialog.setTitle("Adding...");
        progressDialog.setMessage("Category is adding, Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        String productId = FirebaseDatabase.getInstance().getReference().push().getKey();

        HashMap<String, Object> categoryMap = new HashMap<>();

        categoryMap.put("productCategory", categoryName);

        mainCategoryRef.child(productId).updateChildren(categoryMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Category Added successfully", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Make sure you have an active Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void saveCategoryInfoToDatabase() {

        progressDialog.setTitle("Adding...");
        progressDialog.setMessage("Category is adding, Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        String productId = FirebaseDatabase.getInstance().getReference().push().getKey();

        HashMap<String, Object> categoryMap = new HashMap<>();

        categoryMap.put("productCategory", categoryName);
        categoryMap.put("categoryID", productId);
        categoryMap.put("productImage", downloadImageUr1);
        categoryMap.put("parentCategory", subCategorySpinner.getSelectedItem().toString());

        thirdCategoryRef.child(productId).updateChildren(categoryMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Category Added successfully", Toast.LENGTH_SHORT).show();
                    categoryIV.setBackgroundResource(insert_photo);
                    imageUri1 = Uri.EMPTY;
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Make sure you have an active Internet", Toast.LENGTH_SHORT).show();
                    imageUri1 = Uri.EMPTY;
                }
            }
        });
    }

}