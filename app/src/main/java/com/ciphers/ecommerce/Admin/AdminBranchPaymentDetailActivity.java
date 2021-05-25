package com.ciphers.ecommerce.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ciphers.ecommerce.FullScreenImageActivity;
import com.ciphers.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.HashMap;

public class AdminBranchPaymentDetailActivity extends AppCompatActivity {

    Button checkPaymentBtn, adminBranchAmountPaidBtn;
    TextView adminBranchPaymentIdTxt, adminBranchPaymentDescTxt, adminBranchAvailableBalanceTxt, adminBranchImageId;
    ImageView adminBranchPaymentChallanIV, adminBranchPaymentChallanBackIV;
    TextInputLayout adminBranchCodeTxt, adminBranchPaidAmountTxt;
    String challanRecordId, challanRecordDesc, challanRecordImg, challanImageCode;
    float availableAmount = 0, addAmount = 0, updatedAmount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_branch_payment_detail);

        challanRecordId = getIntent().getStringExtra("challanRecordId");
        challanRecordDesc = getIntent().getStringExtra("challanRecordDesc");
        challanRecordImg = getIntent().getStringExtra("challanImg");

        adminBranchPaymentChallanIV = findViewById(R.id.admin_branch_payment_detail_challan_iv);
        adminBranchPaymentIdTxt = findViewById(R.id.admin_branch_payment_challan_id_txt);
        adminBranchPaymentDescTxt = findViewById(R.id.admin_branch_payment_challan_desc_txt);
        adminBranchCodeTxt = findViewById(R.id.admin_branch_payment_code_txt);
        adminBranchAvailableBalanceTxt = findViewById(R.id.admin_branch_payment_available_balance_txt);
        adminBranchPaidAmountTxt = findViewById(R.id.admin_branch_paid_amount_txt);
        adminBranchAmountPaidBtn = findViewById(R.id.admin_branch_amount_paid_btn);
        adminBranchPaymentChallanBackIV = findViewById(R.id.admin_branch_payment_detail_back_icon);
        adminBranchImageId = findViewById(R.id.admin_branch_image_id);

        checkPaymentBtn = findViewById(R.id.admin_check_branch_payment_btn);

        adminBranchPaymentIdTxt.setText(challanRecordId);
        adminBranchPaymentDescTxt.setText(challanRecordDesc);
        Picasso.get().load(challanRecordImg).into(adminBranchPaymentChallanIV);
        adminBranchImageId.setText(challanImageCode);

        adminBranchPaymentChallanBackIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdminBranchPaymentDetailActivity.super.onBackPressed();
            }
        });

        adminBranchPaymentChallanIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fullScreenIntent = new Intent(getApplicationContext(), FullScreenImageActivity.class);
                fullScreenIntent.setData(Uri.parse(challanRecordImg));
                fullScreenIntent.putExtra("image_title", "Branch Challan Image");
                startActivity(fullScreenIntent);
            }
        });

        adminBranchAmountPaidBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateBranchPaymentData();
            }
        });


        checkPaymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateCheckBranch();
            }
        });

    }

    private void validateBranchPaymentData() {

        if (TextUtils.isEmpty(adminBranchCodeTxt.getEditText().getText().toString().trim())) {
            Toast.makeText(getApplicationContext(), "Please Enter Branch Code", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(adminBranchPaidAmountTxt.getEditText().getText().toString().trim())) {
            Toast.makeText(getApplicationContext(), "Please Enter Branch Amount", Toast.LENGTH_LONG).show();
        } else {
            checkAmountData();
        }

    }

    private void checkAmountData() {

        availableAmount = Float.parseFloat(adminBranchAvailableBalanceTxt.getText().toString().trim());
        addAmount = Float.parseFloat(adminBranchPaidAmountTxt.getEditText().getText().toString().trim());

        updatedAmount = availableAmount - addAmount;

        if (availableAmount < addAmount) {
            Toast.makeText(getApplicationContext(), "Please Enter Valid Amount", Toast.LENGTH_LONG).show();
        } else {
            checkBranchExist();
        }

    }

    private void checkBranchExist() {

        DatabaseReference branchAvailableBalance = FirebaseDatabase.getInstance().getReference().child("BranchesPayments");
        Query branchPaymentQuery = branchAvailableBalance.child(adminBranchCodeTxt.getEditText().getText().toString().trim());

        branchPaymentQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    changeBranchAmount(updatedAmount);
                } else {
                    Toast.makeText(getApplicationContext(), "Please Enter Valid Branch Code", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void changeBranchAmount(float updatedAmount) {

        DatabaseReference updateBranchAmount = FirebaseDatabase.getInstance().getReference().child("BranchesPayments");
        DatabaseReference challanRef = FirebaseDatabase.getInstance().getReference().child("Challan Record");
        StorageReference challanImgRef = FirebaseStorage.getInstance().getReferenceFromUrl(challanRecordImg);

        HashMap<String, Object> updateBranchAmountMap = new HashMap<>();
        updateBranchAmountMap.put("availableBalance", String.valueOf(updatedAmount));

        updateBranchAmount.child(adminBranchCodeTxt.getEditText().getText().toString().trim())
                .updateChildren(updateBranchAmountMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                challanRef.child(challanRecordId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        challanImgRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(), "Branch Amount Updated", Toast.LENGTH_LONG).show();
                                AdminBranchPaymentDetailActivity.super.onBackPressed();
                            }
                        });

                    }
                });
            }
        });

    }

    private void validateCheckBranch() {

        if (TextUtils.isEmpty(adminBranchCodeTxt.getEditText().getText().toString().trim())) {
            Toast.makeText(getApplicationContext(), "Please Enter Branch Code", Toast.LENGTH_LONG).show();
        } else {
            checkBranchPayment();
        }

    }

    private void checkBranchPayment() {

        DatabaseReference branchAvailableBalance = FirebaseDatabase.getInstance().getReference().child("BranchesPayments");
        Query branchPaymentQuery = branchAvailableBalance.child(adminBranchCodeTxt.getEditText().getText().toString().trim());

        branchPaymentQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    String branchAvailableBalance = snapshot.child("availableBalance").getValue(String.class);
                    adminBranchAvailableBalanceTxt.setText(branchAvailableBalance);
                } else {
                    Toast.makeText(getApplicationContext(), "Please Enter Valid Branch Code", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}