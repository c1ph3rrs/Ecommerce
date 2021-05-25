package com.ciphers.ecommerce.Branch;

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

import com.ciphers.ecommerce.MailService.JavaMailAPI;
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

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminAddNewDeliveryGuyActivity extends AppCompatActivity {

    Button adminAddDeliveryGuyBtn;
    TextInputLayout adminAddDeliveryName, adminAddDeliveryUsername, adminAddDeliveryEmail, adminAddDeliveryPassword, adminAddDeliveryAddress, adminAddDeliveryPhone;
    TextView adminAddDeliveryBranchCodeTxt;
    CircleImageView adminAddDeliveryGuyIV;

    ImageView adminAddNewDeliveryGuyBackIV;

    static final int galleryPic = 1;
    Uri imageUri;
    DatabaseReference deliveryGuyRef;
    StorageReference deliveryGuyImgRef;

    String downloadImageUrl;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_delivery_guy);

        deliveryGuyRef = FirebaseDatabase.getInstance().getReference().child("DeliveryGuy");
        deliveryGuyImgRef = FirebaseStorage.getInstance().getReference().child("DeliveryGuyImage");

        adminAddDeliveryGuyBtn = findViewById(R.id.admin_add_delivery_guy_btn);
        adminAddDeliveryGuyIV = findViewById(R.id.admin_add_delivery_guy_pic_iv);
        adminAddNewDeliveryGuyBackIV = findViewById(R.id.admin_add_new_delivery_guy_back_btn);

        adminAddDeliveryName = findViewById(R.id.admin_add_delivery_guy_name_txt);
        adminAddDeliveryUsername = findViewById(R.id.admin_add_delivery_guy_username_txt);
        adminAddDeliveryEmail = findViewById(R.id.admin_add_delivery_guy_email_txt);
        adminAddDeliveryPassword = findViewById(R.id.admin_add_delivery_guy_password_txt);
        adminAddDeliveryAddress = findViewById(R.id.admin_add_delivery_guy_address_txt);
        adminAddDeliveryPhone = findViewById(R.id.admin_add_delivery_guy_phone_txt);
        adminAddDeliveryBranchCodeTxt = findViewById(R.id.admin_add_delivery_guy_branch_txt);

        adminAddDeliveryBranchCodeTxt.setText(Prevalent.currentOnlineAdmin.getBranch());

        progressDialog = new ProgressDialog(this);

        adminAddDeliveryGuyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });

        adminAddDeliveryGuyIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        adminAddNewDeliveryGuyBackIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdminAddNewDeliveryGuyActivity.super.onBackPressed();
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

        if (requestCode == galleryPic && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            adminAddDeliveryGuyIV.setImageURI(imageUri);
        }
    }

    private void validateData() {

        if (imageUri.equals(Uri.EMPTY)) {
            Toast.makeText(getApplicationContext(), "Please choose Image", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(adminAddDeliveryName.getEditText().getText().toString().trim())) {
            Toast.makeText(getApplicationContext(), "Please Enter the Name First", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(adminAddDeliveryUsername.getEditText().getText().toString().trim())) {
            Toast.makeText(getApplicationContext(), "Please Enter the Username First", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(adminAddDeliveryEmail.getEditText().getText().toString().trim())) {
            Toast.makeText(getApplicationContext(), "Please Enter the Email", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(adminAddDeliveryPassword.getEditText().getText().toString().trim())) {
            Toast.makeText(getApplicationContext(), "Please Enter the Password", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(adminAddDeliveryAddress.getEditText().getText().toString().trim())) {
            Toast.makeText(getApplicationContext(), "Please Enter the Address", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(adminAddDeliveryPhone.getEditText().getText().toString().trim())) {
            Toast.makeText(getApplicationContext(), "Please Enter the Phone", Toast.LENGTH_LONG).show();
        } else {
            checkUsername();
        }

    }

    private void checkUsername() {

        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child("DeliveryGuy").child(adminAddDeliveryUsername.getEditText().getText().toString().trim())).exists()) {
                    uploadImage();
                } else {
                    Toast.makeText(getApplicationContext(), "This username already exist", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void uploadImage() {

        progressDialog.setTitle("Uploading...");
        progressDialog.setMessage("Please wait while we are Uploading. It may take some time...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        String profileImage = adminAddDeliveryUsername.getEditText().getText().toString() + "_profile_Image";

        final StorageReference filePath = deliveryGuyImgRef.child(imageUri.getLastPathSegment() + profileImage + ".png");
        final UploadTask uploadTask = filePath.putFile(imageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
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


                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {

                            downloadImageUrl = task.getResult().toString();
                            uploadRecord();
                        }
                    }
                });
            }
        });

    }

    private void uploadRecord() {

        HashMap<String, Object> deliveryGuyMap = new HashMap<>();
        deliveryGuyMap.put("deliveryGuyUsername", adminAddDeliveryUsername.getEditText().getText().toString());
        deliveryGuyMap.put("deliveryGuyFullName", adminAddDeliveryName.getEditText().getText().toString());
        deliveryGuyMap.put("deliveryGuyEmail", adminAddDeliveryEmail.getEditText().getText().toString());
        deliveryGuyMap.put("password", adminAddDeliveryPassword.getEditText().getText().toString());
        deliveryGuyMap.put("deliveryGuyAddress", adminAddDeliveryAddress.getEditText().getText().toString());
        deliveryGuyMap.put("deliveryGuyPhone", adminAddDeliveryPhone.getEditText().getText().toString());
        deliveryGuyMap.put("deliveryGuyImage", downloadImageUrl);
        deliveryGuyMap.put("deliveryGuyStatus", downloadImageUrl);
        deliveryGuyMap.put("deliveryGuyBranch", adminAddDeliveryBranchCodeTxt);
        deliveryGuyRef.child(adminAddDeliveryUsername.getEditText().getText().toString()).updateChildren(deliveryGuyMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                sendEmail();
            }
        });

    }

    private void sendEmail() {

        String messageBody = "Hi " + adminAddDeliveryName.getEditText().getText().toString() + " you are now a part of IbExpress. Your account " +
                "as a Delivery Boy has been approved. Now you are able to Deliver Orders. We are going share you your account info. Please save this email. " +
                "If you forget your username and password you can check from this email. Here is the login credentials. " +
                "Username : " + adminAddDeliveryUsername.getEditText().getText().toString() + "\n" +
                "Password : " + adminAddDeliveryPassword.getEditText().getText().toString() + "\n" +
                "Email : " + adminAddDeliveryEmail.getEditText().getText().toString() + "\n" +
                "Phone No : " + adminAddDeliveryPhone.getEditText().getText().toString() + "\n" +
                "Address : " + adminAddDeliveryAddress.getEditText().getText().toString() + "\n" +
                "Branch Code : " + adminAddDeliveryBranchCodeTxt.getText().toString() + "\n"+
                "You can use your username and password to login on this app.";

        String to = adminAddDeliveryEmail.getEditText().getText().toString();
        String subject = "Application Approved from IB EXPRESS";
        String body = messageBody;

        JavaMailAPI javaMailAPI = new JavaMailAPI(this, to, subject, body);
        javaMailAPI.execute();

        progressDialog.dismiss();

        AdminAddNewDeliveryGuyActivity.super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        AdminAddNewDeliveryGuyActivity.super.onBackPressed();
    }
}
