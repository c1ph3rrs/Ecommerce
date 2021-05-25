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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ciphers.ecommerce.Prevalent.Prevalent;
import com.ciphers.ecommerce.R;
import com.google.android.gms.tasks.Continuation;
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
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    ImageView closeSettingActivityBtn, saveSettingsDataIV;

    TextInputLayout fullNameTxt, userNameTxt, emailTxt;
    TextView changeProfileTxt;
    CircleImageView profileImageView;

    private Uri imageUri;
    private StorageTask uploadTask;

    private String myUri = "";
    private StorageReference storageProfilePicRef;

    private String checker = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        storageProfilePicRef = FirebaseStorage.getInstance().getReference().child("Profile Pictures");

        closeSettingActivityBtn = findViewById(R.id.close_settings_activity_icon);
        saveSettingsDataIV = findViewById(R.id.save_settings_btn_icon);

        changeProfileTxt = findViewById(R.id.settings_change_profile_tv);

        fullNameTxt = findViewById(R.id.settings_full_name_txt);
        userNameTxt = findViewById(R.id.settings_user_name_txt);
        emailTxt = findViewById(R.id.settings_email_txt);

        profileImageView = findViewById(R.id.settings_user_profile_image);

        userInfoDisplay(profileImageView, fullNameTxt, userNameTxt, emailTxt);

        changeProfileTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checker = "Clicked";

                CropImage.activity(imageUri).setAspectRatio(1, 1)
                        .start(SettingsActivity.this);
            }
        });

        saveSettingsDataIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checker == "Clicked") {
                    userInfoSaved();
                } else {
                    updateOnlyUserInfo();
                }
            }
        });


        closeSettingActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsActivity.super.onBackPressed();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();

            profileImageView.setImageURI(imageUri);

        } else {
            Toast.makeText(getApplicationContext(), "Error.! Try Again", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
            finish();
        }
    }

    private void updateOnlyUserInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

        HashMap< String, Object> userMap = new HashMap<>();
        userMap.put("fullName", fullNameTxt.getEditText().getText().toString());
        userMap.put("email", emailTxt.getEditText().getText().toString());

//        userMap.put("password", passwordTxt.getEditText().getText().toString());

        ref.child(Prevalent.currentOnlineUser.getUsername()).updateChildren(userMap);

        Toast.makeText(getApplicationContext(), "Profile Updated", Toast.LENGTH_SHORT).show();
        SettingsActivity.super.onBackPressed();
        finish();
    }

    private void userInfoDisplay(final CircleImageView profileImageView, final TextInputLayout fullNameTxt, final TextInputLayout userNameTxt, final TextInputLayout emailTxt) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getUsername());

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.child("userImage").exists()) {
                        String image = dataSnapshot.child("userImage").getValue().toString();
                        String fullName = dataSnapshot.child("fullName").getValue().toString();
                        String email = dataSnapshot.child("email").getValue().toString();
                        String username = dataSnapshot.child("username").getValue().toString();
                        if(image.equals("")){

                        }else{
                            Picasso.get().load(image).into(profileImageView);
                        }
                        fullNameTxt.getEditText().setText(fullName);
                        emailTxt.getEditText().setText(email);
                        userNameTxt.getEditText().setText(username);

                    }else{
                        String image = dataSnapshot.child("userImage").getValue().toString();
                        String fullName = dataSnapshot.child("fullName").getValue().toString();
                        String email = dataSnapshot.child("email").getValue().toString();
                        String username = dataSnapshot.child("username").getValue().toString();
                        fullNameTxt.getEditText().setText(fullName);
                        emailTxt.getEditText().setText(email);
                        userNameTxt.getEditText().setText(username);
                        if(image.equals("")){

                        }else{
                            Picasso.get().load(image).into(profileImageView);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }




    public void userInfoSaved() {

        if (TextUtils.isEmpty(fullNameTxt.getEditText().getText().toString())) {
            Toast.makeText(getApplicationContext(), "Please Enter Name", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(userNameTxt.getEditText().getText().toString())) {
            Toast.makeText(getApplicationContext(), "Please Enter Username", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(emailTxt.getEditText().getText().toString())) {
            Toast.makeText(getApplicationContext(), "Please Enter Email", Toast.LENGTH_LONG).show();
        } else if (checker.equals("Clicked")) {
            uploadImage();
        }

    }

    private void uploadImage() {

        final ProgressDialog progressDialog = new ProgressDialog(this);

        progressDialog.setTitle("Uploading Image");
        progressDialog.setMessage("Please Wait, while image is uploading");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if (imageUri != null) {
            final StorageReference fileRef = storageProfilePicRef.child(Prevalent.currentOnlineUser.getUsername() + ".jpg");


            uploadTask = fileRef.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        myUri = downloadUri.toString();

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

                        HashMap<String,  Object> userMap = new HashMap<>();
                        userMap.put("fullName", userNameTxt.getEditText().getText().toString());
                        userMap.put("email", emailTxt.getEditText().getText().toString());
//                        userMap.put("password", passwordTxt.getEditText().getText().toString());
                        userMap.put("userImage", myUri);

                        ref.child(Prevalent.currentOnlineUser.getUsername()).updateChildren(userMap);

                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Profile Updated", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        finish();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } else {
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Image is Not Selected", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onBackPressed() {
        SettingsActivity.super.onBackPressed();
    }

}