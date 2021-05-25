package com.ciphers.ecommerce.Branch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.ciphers.ecommerce.Prevalent.Prevalent;
import com.ciphers.ecommerce.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import static com.ciphers.ecommerce.Branch.AdminAddNewDeliveryGuyActivity.galleryPic;

public class BranchChallanFormActivity extends AppCompatActivity {

    ImageView branchChallanBackIV, branchChallanPicIV;
    Button branchAddRecordBtn;
    Uri imageUri1;
    ProgressDialog loadingDialog;
    String currentDate, currentTime, branchImageId;

    StorageReference challanImageRef;
    DatabaseReference challanRefRef;
    String downloadImageUr1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_challan_form);

        challanRefRef = FirebaseDatabase.getInstance().getReference().child("Challan Record");
        challanImageRef = FirebaseStorage.getInstance().getReference().child("Challan Images");

        branchChallanBackIV = findViewById(R.id.branch_challan_form_back_icon);
        branchChallanPicIV = findViewById(R.id.branch_add_challan_pic_iv);
        branchAddRecordBtn = findViewById(R.id.branch_add_challan_record_btn);
        loadingDialog = new ProgressDialog(this);


        branchAddRecordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });

        branchChallanPicIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        branchChallanBackIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BranchChallanFormActivity.super.onBackPressed();
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
            imageUri1 = data.getData();
            branchChallanPicIV.setImageURI(imageUri1);
        }

    }

    private void validateData() {

        if (imageUri1.equals(Uri.EMPTY)) {
            Toast.makeText(getApplicationContext(), "Please Choose the Image First", Toast.LENGTH_LONG).show();
        } else {

            uploadRecord();

        }
    }

    private void uploadRecord() {
        loadingDialog.setTitle("Uploading...");
        loadingDialog.setMessage("Please wait white we are uploading...");
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat getDate = new SimpleDateFormat("MM dd, yyyy");
        currentDate = getDate.format(calendar.getTime());

        SimpleDateFormat getTIme = new SimpleDateFormat("HH:mm:ss a");
        currentTime = getTIme.format(calendar.getTime());

        branchImageId = Prevalent.currentOnlineBranch.getBranchCode() + ", " + currentDate + ", " + currentTime;

        final StorageReference filePath = challanImageRef.child(imageUri1.getLastPathSegment() + branchImageId + ".png");
        final UploadTask uploadTask = filePath.putFile(imageUri1);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loadingDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Uploading Challan Image Error : " + e, Toast.LENGTH_SHORT).show();
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
                            uploadChallanRecord(branchImageId);
                        }
                    }
                });
            }
        });
    }

    private void uploadChallanRecord(String branchChallanId) {

        String key = FirebaseDatabase.getInstance().getReference().push().getKey();

        HashMap<String, Object> challanMap = new HashMap<>();
        challanMap.put("imageUrl", downloadImageUr1);
        challanMap.put("challanDesc", branchChallanId);
        challanMap.put("challanId", key);

        challanRefRef.child(key).updateChildren(challanMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                loadingDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Record Updated", Toast.LENGTH_LONG).show();
                BranchChallanFormActivity.super.onBackPressed();
            }
        });
    }

}