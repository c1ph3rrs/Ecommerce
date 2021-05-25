package com.ciphers.ecommerce.Sellers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ciphers.ecommerce.Prevalent.Prevalent;
import com.ciphers.ecommerce.R;
import com.ciphers.ecommerce.SplashScreen;
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

import java.util.ArrayList;
import java.util.HashMap;

import io.paperdb.Paper;

public class NonVerifySellerActivity extends AppCompatActivity {

    ImageView sellerShopProfileIV, sellerShopDocumentIV, sellerOwnerIDFrontIV, sellerOwnerIDBackIV, sellerShopVisitingCardIV;
    ImageView sellerShopProfileDDIV, sellerShopDocumentDDIV, sellerShopIDFrontDDIV, sellerShopIDBackDDIV, sellerShopVisitingCardDDIV;


    int profileDDIV = 0, documentDDIV = 0, iDFrontDDIV = 0, iDBackDDIV = 0, visitingCardDDIV = 0;

    RelativeLayout shopProfileLayout, shopDocumentLayout, shopIDFrontLayout, shopIDBackLayout, shopVisitingCardLayout, sellerPendingStatusLayout, sellerUsernameLayout;

    Button submitSellerDocumentsBtn, sellerLogoutBtn, sellerLogoutTwoBtn;
    TextView sellerUsernameTxt, sellerShopProfileTV, sellerDocumentTV, sellerIDFrontTV, sellerIDBackTV, sellerVisitingCardTV;
    TextInputLayout sellerShopNameTxt, sellerShopEmailTxt, sellerShopAddressTxt, sellerShopLicenceTxt;

    int imageNo = 0;
    static final int galleryPic = 1;
    private int STORAGE_PERMISSION_CODE = 1;
    Uri imageUri1, imageUri2, imageUri3, imageUri4, imageUri5;
    ProgressDialog progressDialog;

    StorageReference sellerShopLogo, sellerDocument, sellerIDFront, sellerIDBack, sellerVisitingCard;
    String downloadImageUr1, downloadImageUr2, downloadImageUr3, downloadImageUr4, downloadImageUr5;

    Spinner sellerShopSpinner;
    ArrayList<String> sellerShopTypeList = new ArrayList<>();


    DatabaseReference sellerRecordRef, sellersRef, sellerShopCategoryRef;

    String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_non_verify_seller);

        status = getIntent().getStringExtra("status");

        sellersRef = FirebaseDatabase.getInstance().getReference().child("Sellers");
        sellerRecordRef = FirebaseDatabase.getInstance().getReference().child("SellersDocuments");
        sellerShopCategoryRef = FirebaseDatabase.getInstance().getReference().child("ShopCategory");


        sellerShopLogo = FirebaseStorage.getInstance().getReference().child("Seller Logo");
        sellerDocument = FirebaseStorage.getInstance().getReference().child("Seller Documents");
        sellerIDFront = FirebaseStorage.getInstance().getReference().child("Seller ID Front");
        sellerIDBack = FirebaseStorage.getInstance().getReference().child("Seller ID Back");
        sellerVisitingCard = FirebaseStorage.getInstance().getReference().child("Seller Visiting Card");

        sellerShopProfileIV = findViewById(R.id.seller_shop_profile_iv);
        sellerShopDocumentIV = findViewById(R.id.seller_shop_document_iv);
        sellerOwnerIDFrontIV = findViewById(R.id.seller_shop_owner_id_front_iv);
        sellerOwnerIDBackIV = findViewById(R.id.seller_shop_owner_id_back_iv);
        sellerShopVisitingCardIV = findViewById(R.id.seller_shop_visiting_card_iv);
        sellerShopSpinner = findViewById(R.id.choose_shop_category_spinner);

        sellerShopProfileDDIV = findViewById(R.id.profile_drop_iv);
        sellerShopDocumentDDIV = findViewById(R.id.document_drop_iv);
        sellerShopIDFrontDDIV = findViewById(R.id.id_front_drop_iv);
        sellerShopIDBackDDIV = findViewById(R.id.id_back_drop_iv);
        sellerShopVisitingCardDDIV = findViewById(R.id.visiting_card_drop_iv);

        shopProfileLayout = findViewById(R.id.shop_image_layout);
        shopDocumentLayout = findViewById(R.id.shop_document_layout);
        shopIDFrontLayout = findViewById(R.id.shop_owner_id_front_layout);
        shopIDBackLayout = findViewById(R.id.shop_owner_id_back_layout);
        shopVisitingCardLayout = findViewById(R.id.shop_visiting_card_layout);
        sellerPendingStatusLayout = findViewById(R.id.seller_pending_status_layout);
        sellerUsernameLayout = findViewById(R.id.seller_username_layout);

        sellerUsernameTxt = findViewById(R.id.seller_username_txt);
        sellerShopNameTxt = findViewById(R.id.seller_shop_name_txt);
        sellerShopEmailTxt = findViewById(R.id.seller_shop_email_txt);
        sellerShopAddressTxt = findViewById(R.id.seller_shop_address_txt);
        sellerShopLicenceTxt = findViewById(R.id.seller_shop_licence_txt);

        sellerShopProfileTV = findViewById(R.id.shop_image);
        sellerDocumentTV = findViewById(R.id.shop_document);
        sellerIDFrontTV = findViewById(R.id.seller_cnic_front_tv);
        sellerIDBackTV = findViewById(R.id.seller_cnic_back_tv);
        sellerVisitingCardTV = findViewById(R.id.seller_visiting_card_tv);
        sellerLogoutBtn = findViewById(R.id.seller_pending_logout_btn);
        sellerLogoutTwoBtn = findViewById(R.id.seller_pending_logout_two_btn);

        submitSellerDocumentsBtn = findViewById(R.id.seller_submit_record);
        progressDialog = new ProgressDialog(this);

        if (status.equals("0")) {
            sellerUsernameTxt.setVisibility(View.GONE);
            sellerShopNameTxt.setVisibility(View.GONE);
            sellerShopEmailTxt.setVisibility(View.GONE);
            sellerShopAddressTxt.setVisibility(View.GONE);
            sellerShopLicenceTxt.setVisibility(View.GONE);

            sellerVisitingCardTV.setVisibility(View.GONE);
            sellerShopProfileTV.setVisibility(View.GONE);
            sellerDocumentTV.setVisibility(View.GONE);
            sellerIDFrontTV.setVisibility(View.GONE);
            sellerIDBackTV.setVisibility(View.GONE);

            shopVisitingCardLayout.setVisibility(View.GONE);
            sellerUsernameLayout.setVisibility(View.GONE);
            shopDocumentLayout.setVisibility(View.GONE);
            shopProfileLayout.setVisibility(View.GONE);
            shopIDFrontLayout.setVisibility(View.GONE);
            shopIDBackLayout.setVisibility(View.GONE);

            sellerShopVisitingCardDDIV.setVisibility(View.GONE);
            sellerShopDocumentDDIV.setVisibility(View.GONE);
            sellerShopProfileDDIV.setVisibility(View.GONE);
            sellerShopIDFrontDDIV.setVisibility(View.GONE);
            sellerShopIDBackDDIV.setVisibility(View.GONE);

            submitSellerDocumentsBtn.setVisibility(View.GONE);


            sellerPendingStatusLayout.setVisibility(View.VISIBLE);

        } else if (status.equals("-1")) {

            sellerPendingStatusLayout.setVisibility(View.GONE);

            sellerUsernameTxt.setText(Prevalent.currentOnlineSeller.getSellerUsername());
            sellerShopNameTxt.getEditText().setText(Prevalent.currentOnlineSeller.getSellerShopName());
            sellerShopEmailTxt.getEditText().setText(Prevalent.currentOnlineSeller.getSellerShopEmail());
            sellerShopAddressTxt.getEditText().setText(Prevalent.currentOnlineSeller.getSellerShopLocation());
            sellerShopLicenceTxt.getEditText().setText(Prevalent.currentOnlineSeller.getSellerShopIdentity());
        }

        sellerLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence options[] = new CharSequence[]{
                        "Yes",
                        "No"
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(NonVerifySellerActivity.this);
                builder.setTitle("Are you sure you want to Logout.?");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            Paper.book().destroy();
                            Intent logoutIntent = new Intent(NonVerifySellerActivity.this, SplashScreen.class);
                            logoutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(logoutIntent);
                        } else {
                        }
                    }
                });
                builder.show();
            }
        });

        sellerLogoutTwoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence options[] = new CharSequence[]{
                        "Yes",
                        "No"
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(NonVerifySellerActivity.this);
                builder.setTitle("Are you sure you want to Logout.?");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            Paper.book().destroy();
                            Intent logoutIntent = new Intent(NonVerifySellerActivity.this, SplashScreen.class);
                            logoutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(logoutIntent);
                        } else {
                        }
                    }
                });
                builder.show();
            }
        });

        sellerShopProfileIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                    imageNo = 1;
                } else {
                    requestStoragePermission();
                }
            }
        });

        sellerShopDocumentIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                    imageNo = 2;
                } else {
                    requestStoragePermission();
                }
            }
        });

        sellerOwnerIDFrontIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                    imageNo = 3;
                } else {
                    requestStoragePermission();
                }

            }
        });

        sellerOwnerIDBackIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                    imageNo = 4;
                } else {
                    requestStoragePermission();
                }
            }
        });

        sellerShopVisitingCardIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                    imageNo = 5;
                } else {
                    requestStoragePermission();
                }

            }
        });


        submitSellerDocumentsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });


        sellerShopProfileDDIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (profileDDIV == 0) {
                    sellerShopProfileDDIV.setImageResource(R.drawable.drop_arrow);
                    shopProfileLayout.setVisibility(View.VISIBLE);
                    profileDDIV = 1;
                } else {
                    sellerShopProfileDDIV.setImageResource(R.drawable.drop_up_arrow);
                    shopProfileLayout.setVisibility(View.GONE);
                    profileDDIV = 0;
                }
            }
        });

        sellerShopDocumentDDIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (documentDDIV == 0) {
                    sellerShopDocumentDDIV.setImageResource(R.drawable.drop_arrow);
                    shopDocumentLayout.setVisibility(View.VISIBLE);
                    documentDDIV = 1;
                } else {
                    sellerShopDocumentDDIV.setImageResource(R.drawable.drop_up_arrow);
                    shopDocumentLayout.setVisibility(View.GONE);
                    documentDDIV = 0;
                }
            }
        });

        sellerShopIDFrontDDIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iDFrontDDIV == 0) {
                    sellerShopIDFrontDDIV.setImageResource(R.drawable.drop_arrow);
                    shopIDFrontLayout.setVisibility(View.VISIBLE);
                    iDFrontDDIV = 1;
                } else {
                    sellerShopIDFrontDDIV.setImageResource(R.drawable.drop_up_arrow);
                    shopIDFrontLayout.setVisibility(View.GONE);
                    iDFrontDDIV = 0;
                }
            }
        });


        sellerShopIDBackDDIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iDBackDDIV == 0) {
                    sellerShopIDBackDDIV.setImageResource(R.drawable.drop_arrow);
                    shopIDBackLayout.setVisibility(View.VISIBLE);
                    iDBackDDIV = 1;
                } else {
                    sellerShopIDBackDDIV.setImageResource(R.drawable.drop_up_arrow);
                    shopIDBackLayout.setVisibility(View.GONE);
                    iDBackDDIV = 0;
                }
            }
        });

        sellerShopVisitingCardDDIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (visitingCardDDIV == 0) {
                    sellerShopVisitingCardDDIV.setImageResource(R.drawable.drop_arrow);
                    shopVisitingCardLayout.setVisibility(View.VISIBLE);
                    visitingCardDDIV = 1;
                } else {
                    sellerShopVisitingCardDDIV.setImageResource(R.drawable.drop_up_arrow);
                    shopVisitingCardLayout.setVisibility(View.GONE);
                    visitingCardDDIV = 0;
                }
            }
        });

        showShopCategoryDataSpinner();

    }

    private void openGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, galleryPic);
    }

    private void showShopCategoryDataSpinner(){
        sellerShopCategoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sellerShopTypeList.clear();
                for (DataSnapshot items : snapshot.getChildren()) {
                    sellerShopTypeList.add(items.child("shopCategory").getValue(String.class));
                }
                ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, sellerShopTypeList);
                categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sellerShopSpinner.setAdapter(categoryAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission Required")
                    .setMessage("We need permission to access gallery to choose picture")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(NonVerifySellerActivity.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (imageNo == 1) {
            if (requestCode == galleryPic && resultCode == RESULT_OK && data != null) {
                imageUri1 = data.getData();
                sellerShopProfileIV.setImageURI(imageUri1);
            }
        } else if (imageNo == 2) {
            if (requestCode == galleryPic && resultCode == RESULT_OK && data != null) {
                imageUri2 = data.getData();
                sellerShopDocumentIV.setImageURI(imageUri2);
            }
        } else if (imageNo == 3) {
            if (requestCode == galleryPic && resultCode == RESULT_OK && data != null) {
                imageUri3 = data.getData();
                sellerOwnerIDFrontIV.setImageURI(imageUri3);
            }
        } else if (imageNo == 4) {
            if (requestCode == galleryPic && resultCode == RESULT_OK && data != null) {
                imageUri4 = data.getData();
                sellerOwnerIDBackIV.setImageURI(imageUri4);
            }
        } else if (imageNo == 5) {
            if (requestCode == galleryPic && resultCode == RESULT_OK && data != null) {
                imageUri5 = data.getData();
                sellerShopVisitingCardIV.setImageURI(imageUri5);
            }
        }
    }

    private void validateData() {
        if (TextUtils.isEmpty(sellerShopNameTxt.getEditText().getText().toString().trim()) || sellerShopNameTxt.getEditText().getText().toString().trim().equals("null")) {
            Toast.makeText(getApplicationContext(), "Please Enter the Shop Name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(sellerShopEmailTxt.getEditText().getText().toString().trim()) || sellerShopEmailTxt.getEditText().getText().toString().trim().equals("null")) {
            Toast.makeText(getApplicationContext(), "Please Enter the Shop Email", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(sellerShopAddressTxt.getEditText().getText().toString().trim()) || sellerShopAddressTxt.getEditText().getText().toString().trim().equals("null")) {
            Toast.makeText(getApplicationContext(), "Please Enter the Shop Address", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(sellerShopLicenceTxt.getEditText().getText().toString().trim()) || sellerShopLicenceTxt.getEditText().getText().toString().trim().equals("null")) {
            Toast.makeText(getApplicationContext(), "Please Enter the Shop Licence", Toast.LENGTH_SHORT).show();
        } else if (imageUri1.equals(Uri.EMPTY)) {
            Toast.makeText(getApplicationContext(), "Please Enter the Shop Image or logo", Toast.LENGTH_SHORT).show();
        } else if (imageUri2.equals(Uri.EMPTY)) {
            Toast.makeText(getApplicationContext(), "Please Enter the Shop Document", Toast.LENGTH_SHORT).show();
        } else if (imageUri3.equals(Uri.EMPTY)) {
            Toast.makeText(getApplicationContext(), "Please Enter the Shop Owner ID Card Front Pic", Toast.LENGTH_SHORT).show();
        } else if (imageUri4.equals(Uri.EMPTY)) {
            Toast.makeText(getApplicationContext(), "Please Enter the Shop Owner ID Card Back Pic", Toast.LENGTH_SHORT).show();
        } else if (imageUri5.equals(Uri.EMPTY)) {
            Toast.makeText(getApplicationContext(), "Please Enter the Shop Visiting Card", Toast.LENGTH_SHORT).show();
        } else {
            submitUserData();
        }
    }

    private void submitUserData() {
        progressDialog.setTitle("Uploading...");
        progressDialog.setMessage("Please wait while we are uploading Documents. It may take some time...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        String profileImage = Prevalent.currentOnlineSeller.getSellerUsername() + "_profile_Image";

        final StorageReference filePath = sellerShopLogo.child(imageUri1.getLastPathSegment() + profileImage + ".png");
        final UploadTask uploadTask = filePath.putFile(imageUri1);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Uploading Shop Image Error : " + e, Toast.LENGTH_SHORT).show();
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
                            uploadDocument();
                        }
                    }
                });
            }
        });

    }

    private void uploadDocument() {

        String document = Prevalent.currentOnlineSeller.getSellerUsername() + "_document";

        final StorageReference filePath = sellerDocument.child(imageUri2.getLastPathSegment() + document + ".png");
        final UploadTask uploadTask = filePath.putFile(imageUri2);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Uploading Shop Document Error : " + e, Toast.LENGTH_SHORT).show();
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
                            uploadFrontID();
                        }
                    }
                });
            }
        });
    }

    private void uploadFrontID() {
        String cnicFront = Prevalent.currentOnlineSeller.getSellerUsername() + "_cnic_front";

        final StorageReference filePath = sellerIDFront.child(imageUri3.getLastPathSegment() + cnicFront + ".png");
        final UploadTask uploadTask = filePath.putFile(imageUri3);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Uploading ID Front Error : " + e, Toast.LENGTH_SHORT).show();
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
                            uploadBackID();
                        }
                    }
                });
            }
        });
    }

    private void uploadBackID() {
        String cnicBack = Prevalent.currentOnlineSeller.getSellerUsername() + "_cnic_back";

        final StorageReference filePath = sellerIDBack.child(imageUri4.getLastPathSegment() + cnicBack + ".png");
        final UploadTask uploadTask = filePath.putFile(imageUri4);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Uploading ID Back Error : " + e, Toast.LENGTH_SHORT).show();
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
                            uploadVisitingCard();
                        }
                    }
                });
            }
        });
    }

    private void uploadVisitingCard() {
        String visitingCard = Prevalent.currentOnlineSeller.getSellerUsername() + "_visiting_card";

        final StorageReference filePath = sellerVisitingCard.child(imageUri5.getLastPathSegment() + visitingCard + ".png");
        final UploadTask uploadTask = filePath.putFile(imageUri5);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Uploading Visiting Card Error : " + e, Toast.LENGTH_SHORT).show();
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


                        downloadImageUr5 = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            downloadImageUr5 = task.getResult().toString();
                            uploadRecord();
                        }
                    }
                });
            }
        });
    }

    private void uploadRecord() {

        HashMap<String, Object> submitRecordMap = new HashMap<>();
        submitRecordMap.put("sellerShopEmail", sellerShopEmailTxt.getEditText().getText().toString().trim());
        submitRecordMap.put("sellerShopIdentity", sellerShopLicenceTxt.getEditText().getText().toString().trim());
        submitRecordMap.put("sellerShopLocation", sellerShopAddressTxt.getEditText().getText().toString().trim());
        submitRecordMap.put("sellerShopName", sellerShopNameTxt.getEditText().getText().toString().trim());
        submitRecordMap.put("sellerShopCategory", sellerShopSpinner.getSelectedItem().toString());
        submitRecordMap.put("sellerStatus", "0");
        submitRecordMap.put("shopImage", downloadImageUr1);
        sellersRef.child(Prevalent.currentOnlineSeller.getSellerUsername()).updateChildren(submitRecordMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                HashMap<String, Object> documentRecordMap = new HashMap<>();
                documentRecordMap.put("sellerDocument", downloadImageUr2);
                documentRecordMap.put("sellerIDFront", downloadImageUr3);
                documentRecordMap.put("sellerIDBack", downloadImageUr4);
                documentRecordMap.put("sellerVisitingCard", downloadImageUr5);

                sellerRecordRef.child(Prevalent.currentOnlineSeller.getSellerUsername()).updateChildren(documentRecordMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Record Uploaded", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), SplashScreen.class));
                    }
                });
            }
        });
    }
}