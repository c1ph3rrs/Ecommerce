package com.ciphers.ecommerce.Buyer;

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
import android.widget.TextView;
import android.widget.Toast;

import com.ciphers.ecommerce.Model.Users;
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
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class BuyerEditProfileActivity extends AppCompatActivity {


    CircleImageView buyerEditProfileIV;
    TextInputLayout buyerEditProfileNameTxt, buyerEditProfileEmailTxt, buyerEditProfilePhoneTxt;
    TextView buyerEditProfileUsernameTxt;
    ImageView buyerEditProfileBackIcon;
    Button buyerSubmitRecordBtn;
    int imageChecker = 0;
    static final int galleryPic = 1;
    Uri imageUri1;

    DatabaseReference userRef;
    StorageReference profileStorageRef;
    ProgressDialog loadingDialog;
    String downloadImageUri1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_edit_profile);

        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        profileStorageRef = FirebaseStorage.getInstance().getReference().child("Profile Pictures");

        buyerEditProfileIV = findViewById(R.id.edit_profile_buyer_iv);
        buyerEditProfileBackIcon = findViewById(R.id.edit_profile_back_icon);
        buyerEditProfileNameTxt = findViewById(R.id.buyer_full_name_txt);
        buyerEditProfileEmailTxt = findViewById(R.id.buyer_email_txt);
        buyerEditProfilePhoneTxt = findViewById(R.id.buyer_phone_no_txt);
        buyerEditProfileUsernameTxt = findViewById(R.id.buyer_edit_profile_username_txt);
        buyerSubmitRecordBtn = findViewById(R.id.buyer_update_record_btn);

        buyerEditProfileUsernameTxt.setText(Prevalent.currentOnlineUser.getUsername());

        buyerEditProfileNameTxt.getEditText().setText(Prevalent.currentOnlineUser.getFullName());
        buyerEditProfileEmailTxt.getEditText().setText(Prevalent.currentOnlineUser.getEmail());
        buyerEditProfilePhoneTxt.getEditText().setText(Prevalent.currentOnlineUser.getPhoneNo());

        Picasso.get().load(Prevalent.currentOnlineUser.getImage()).placeholder(R.drawable.profile).into(buyerEditProfileIV);

        loadingDialog = new ProgressDialog(this);

        buyerSubmitRecordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validateRecord();

            }
        });

        buyerEditProfileBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuyerEditProfileActivity.super.onBackPressed();
            }
        });

        buyerEditProfileIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChecker = 1;
                openGallery();
            }
        });

    }

    private void openGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, galleryPic);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == galleryPic && resultCode == RESULT_OK && data != null) {
            imageUri1 = data.getData();
            buyerEditProfileIV.setImageURI(imageUri1);
        }
    }

    private void validateRecord() {


        if (TextUtils.isEmpty(buyerEditProfileNameTxt.getEditText().getText().toString().trim())) {
            Toast.makeText(getApplicationContext(), "Please Enter Your Name", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(buyerEditProfileEmailTxt.getEditText().getText().toString().trim())) {
            Toast.makeText(getApplicationContext(), "Please Enter Your Email", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(buyerEditProfilePhoneTxt.getEditText().getText().toString().trim())) {
            Toast.makeText(getApplicationContext(), "Please Enter Your Phone No", Toast.LENGTH_LONG).show();
        } else {

            if (imageChecker == 1) {
                deleteImageFirst();
            } else {
                updateOnlyData();
            }
        }
    }

    private void updateOnlyData() {

        loadingDialog.setTitle("Updating..");
        loadingDialog.setMessage("Please wait while record is updating");
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.show();

        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("email", buyerEditProfileEmailTxt.getEditText().getText().toString().trim());
        userMap.put("fullName", buyerEditProfileNameTxt.getEditText().getText().toString().trim());
        userMap.put("phoneNo", buyerEditProfilePhoneTxt.getEditText().getText().toString().trim());

        userRef.child(Prevalent.currentOnlineUser.getUsername()).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                userRef.child(Prevalent.currentOnlineUser.getUsername()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        loadingDialog.dismiss();
                        String dbEmail = snapshot.child("email").getValue(String.class);
                        String dbName = snapshot.child("fullName").getValue(String.class);
                        String dbPassword = snapshot.child("password").getValue(String.class);
                        String dbPhone = snapshot.child("phoneNo").getValue(String.class);
                        String dbUserName = snapshot.child("username").getValue(String.class);
                        String dbImage = snapshot.child("userImage").getValue(String.class);
                        String dbJoin = snapshot.child("userJoin").getValue(String.class);

                        Users userData = new Users(dbEmail, dbName, dbPassword, dbPhone, dbUserName, dbImage, dbJoin);
                        Prevalent.currentOnlineUser = userData;

                        Toast.makeText(getApplicationContext(), "Record Updated Successfully", Toast.LENGTH_LONG).show();
                        BuyerEditProfileActivity.super.onBackPressed();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        loadingDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Something went wrong. Please try Later", Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

    }

    private void updateWithImageRecord() {


        final StorageReference filePath = profileStorageRef.child(imageUri1.getLastPathSegment() + Prevalent.currentOnlineUser.getUsername() + ".png");
        final UploadTask uploadTask = filePath.putFile(imageUri1);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loadingDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Uploading Image Error : " + e, Toast.LENGTH_SHORT).show();
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
                            updateRecordWithImageRecord();
                        }
                    }
                });
            }
        });
    }

    private void deleteImageFirst() {

        loadingDialog.setTitle("Updating...");
        loadingDialog.setMessage("Profile picture is Updating, please wait");
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.show();

        StorageReference imageOne = FirebaseStorage.getInstance().getReferenceFromUrl(Prevalent.currentOnlineUser.getImage());
        imageOne.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                updateWithImageRecord();
            }
        });

    }

    private void updateRecordWithImageRecord(){

        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("email", buyerEditProfileEmailTxt.getEditText().getText().toString().trim());
        userMap.put("fullName", buyerEditProfileNameTxt.getEditText().getText().toString().trim());
        userMap.put("phoneNo", buyerEditProfilePhoneTxt.getEditText().getText().toString().trim());
        userMap.put("userImage", downloadImageUri1);

        userRef.child(Prevalent.currentOnlineUser.getUsername()).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                userRef.child(Prevalent.currentOnlineUser.getUsername()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        loadingDialog.dismiss();
                        String dbEmail = snapshot.child("email").getValue(String.class);
                        String dbName = snapshot.child("fullName").getValue(String.class);
                        String dbPassword = snapshot.child("password").getValue(String.class);
                        String dbPhone = snapshot.child("phoneNo").getValue(String.class);
                        String dbUserName = snapshot.child("username").getValue(String.class);
                        String dbImage = snapshot.child("userImage").getValue(String.class);
                        String dbJoin = snapshot.child("userJoin").getValue(String.class);

                        Users userData = new Users(dbEmail, dbName, dbPassword, dbPhone, dbUserName, dbImage, dbJoin);
                        Prevalent.currentOnlineUser = userData;

                        Toast.makeText(getApplicationContext(), "Record Updated Successfully", Toast.LENGTH_LONG).show();
                        BuyerEditProfileActivity.super.onBackPressed();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        loadingDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Something went wrong. Please try Later", Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

    }

}