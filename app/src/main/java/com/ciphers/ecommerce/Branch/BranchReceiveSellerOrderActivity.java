package com.ciphers.ecommerce.Branch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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

import com.ciphers.ecommerce.Model.DeliveryGuy;
import com.ciphers.ecommerce.Prevalent.Prevalent;
import com.ciphers.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class BranchReceiveSellerOrderActivity extends AppCompatActivity {

    Button receiveSellerOrderBtn;
    TextInputLayout shippingId, trackingID, sellerUsername;
    Spinner branchesAddressSpinner, branchDeliveryGuySpinner;

    DatabaseReference showBranchesRef, receiveSellerOrderRef, branchDataRef, adminChooseDeliverySpinnerRef, deliveryGuyShipRef;
    ArrayList<String> branchTypeList = new ArrayList<>();
    ArrayList<String> adminDeliveryGuyList = new ArrayList<>();
    TextView branchStoreCodeTxt;
    ProgressDialog loadingDialog;

    ImageView branchReceiveSellerOrderBackIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_receive_seller_order);

        adminChooseDeliverySpinnerRef = FirebaseDatabase.getInstance().getReference().child("DeliveryGuy");

        showBranchesRef = FirebaseDatabase.getInstance().getReference().child("Branches");
        branchDataRef = FirebaseDatabase.getInstance().getReference().child("BranchesOrders");
        receiveSellerOrderRef = FirebaseDatabase.getInstance().getReference().child("SellersOrders");
        deliveryGuyShipRef = FirebaseDatabase.getInstance().getReference().child("deliveryGuyShip");

        branchesAddressSpinner = findViewById(R.id.branch_store_spinner);
        branchDeliveryGuySpinner = findViewById(R.id.branch_delivery_guy_spinner);
        shippingId = findViewById(R.id.admin_shipping_id_txt);
        trackingID = findViewById(R.id.admin_tracking_id_txt);
        receiveSellerOrderBtn = findViewById(R.id.branch_receive_seller_order_btn);
        branchStoreCodeTxt = findViewById(R.id.branch_store_code_txt);
        sellerUsername = findViewById(R.id.branch_seller_name_txt);
        loadingDialog = new ProgressDialog(this);
        branchReceiveSellerOrderBackIcon = findViewById(R.id.receive_seller_order_back_icon);

        branchStoreCodeTxt.setText(Prevalent.currentOnlineBranch.getBranchCode());

        receiveSellerOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateSellerOrderData();
            }
        });

        branchReceiveSellerOrderBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BranchReceiveSellerOrderActivity.super.onBackPressed();
            }
        });

        showBranchesSpinner();
        showDeliveryGuys();
    }

    private void showDeliveryGuys() {

        adminChooseDeliverySpinnerRef.orderByChild("deliveryGuyBranch").equalTo(Prevalent.currentOnlineBranch.getBranchCode()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                adminDeliveryGuyList.clear();
                for (DataSnapshot items : snapshot.getChildren()) {
                    DeliveryGuy deliveryGuy = items.getValue(DeliveryGuy.class);
                    if (deliveryGuy.getDeliveryGuyStatus().equals("1")) {
                        adminDeliveryGuyList.add(items.child("deliveryGuyUsername").getValue(String.class));
                    } else {
                    }
                }
                ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, adminDeliveryGuyList);
                categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                branchDeliveryGuySpinner.setAdapter(categoryAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void uploadShippingRecord() {

        HashMap<String, Object> deliveryGuyShipping = new HashMap<>();
        deliveryGuyShipping.put("shipping", shippingId.getEditText().getText().toString());
        deliveryGuyShipping.put("tracking", trackingID.getEditText().getText().toString());
        deliveryGuyShipRef.child(branchDeliveryGuySpinner.getSelectedItem().toString()).child(trackingID.getEditText().getText().toString()).updateChildren(deliveryGuyShipping).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getApplicationContext(), "Record Added for Delivery Guy", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void validateSellerOrderData() {

        if (TextUtils.isEmpty(shippingId.getEditText().getText().toString().trim())) {
            Toast.makeText(getApplicationContext(), "Please add Shipping Id", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(trackingID.getEditText().getText().toString().trim())) {
            Toast.makeText(getApplicationContext(), "Please add Tracking Id", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(sellerUsername.getEditText().getText().toString().trim())) {
            Toast.makeText(getApplicationContext(), "Please add Seller Name", Toast.LENGTH_LONG).show();
        } else {
            checkOrderID();

        }
    }

    private void checkOrderID() {

        receiveSellerOrderRef.child(sellerUsername.getEditText().getText().toString().trim())
                .child(trackingID.getEditText().getText().toString().trim()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    addSellerOrderRecord();
                } else {
                    Toast.makeText(getApplicationContext(), "Please Enter Valid ID", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void addSellerOrderRecord() {

        loadingDialog.setTitle("Uploading Record..");
        loadingDialog.setMessage("Please wait while we are uploading Record");
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.show();

        String key = FirebaseDatabase.getInstance().getReference().push().getKey();

        String currentDate, currentTime, date;

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat getDate = new SimpleDateFormat("MM dd, yyyy");
        currentDate = getDate.format(calendar.getTime());

        SimpleDateFormat getTIme = new SimpleDateFormat("HH:mm:ss a");
        currentTime = getTIme.format(calendar.getTime());

        date = currentDate + " " + currentTime;

        HashMap<String, Object> sellerStatusUpdateMap = new HashMap<>();
        sellerStatusUpdateMap.put("orderStatus", "1");

        HashMap<String, Object> branchMap = new HashMap<>();
        branchMap.put("shippingID", shippingId.getEditText().getText().toString().trim());
        branchMap.put("trackingID", trackingID.getEditText().getText().toString().trim());
        branchMap.put("sendFrom", String.valueOf(Prevalent.currentOnlineBranch.getBranchCode()));
        branchMap.put("orderID", key);
        branchMap.put("sendTo", branchesAddressSpinner.getSelectedItem().toString());
        branchMap.put("date", date);
        branchDataRef.child("SenderBranch").child(String.valueOf(Prevalent.currentOnlineBranch.getBranchCode()))
                .child(key).updateChildren(branchMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                branchDataRef.child("ReceiverBranch").child(branchesAddressSpinner.getSelectedItem().toString())
                        .child(key).updateChildren(branchMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        receiveSellerOrderRef.child(sellerUsername.getEditText().getText().toString().trim())
                                .child(trackingID.getEditText().getText().toString().trim())
                                .updateChildren(sellerStatusUpdateMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getApplicationContext(), "Record Added", Toast.LENGTH_LONG).show();
                                uploadShippingRecord();
                                shippingId.getEditText().getText().clear();
                                trackingID.getEditText().getText().clear();
                                loadingDialog.dismiss();
                            }
                        });
                    }
                });
            }
        });
    }

    private void showBranchesSpinner() {

        showBranchesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                branchTypeList.clear();
                for (DataSnapshot items : snapshot.getChildren()) {
                    branchTypeList.add(items.child("branchCode").getValue(String.class));
                }
                ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, branchTypeList);
                categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                branchesAddressSpinner.setAdapter(categoryAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}