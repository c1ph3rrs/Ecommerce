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
import android.widget.Spinner;
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

public class BranchAssignOrderForDeliveryActivity extends AppCompatActivity {

    Spinner adminChooseDeliveryGuySpinner;
    Button adminAddShippingRecordBtn;
    TextInputLayout adminAddShippingIDTxt, adminAddTrackingIDTxt, shippedGuy;

    String shippingID, trackingID, shippingFrom, shippingTo, shippingDate, orderID;

    DatabaseReference adminChooseDeliverySpinnerRef, deliveryGuyShipRef, branchOrders, branchesOrders, deliveryGuyRef;
    ArrayList<String> adminDeliveryGuyList = new ArrayList<>();
    ProgressDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_assign_order_for_delivery);

        shippingID = getIntent().getStringExtra("shippingId");
        trackingID = getIntent().getStringExtra("trackingId");
        shippingFrom = getIntent().getStringExtra("shippingFrom");
        shippingTo = getIntent().getStringExtra("shippingTo");
        shippingDate = getIntent().getStringExtra("shippingDate");
        orderID = getIntent().getStringExtra("orderID");

        adminChooseDeliverySpinnerRef = FirebaseDatabase.getInstance().getReference().child("DeliveryGuy");
        deliveryGuyShipRef = FirebaseDatabase.getInstance().getReference().child("deliveryGuyShip");
        branchOrders = FirebaseDatabase.getInstance().getReference().child("BranchOrders");
        branchesOrders = FirebaseDatabase.getInstance().getReference().child("BranchesOrders");

        adminAddShippingIDTxt = findViewById(R.id.admin_shipping_id_txt);
        adminAddTrackingIDTxt = findViewById(R.id.admin_tracking_id_txt);
        adminAddShippingRecordBtn = findViewById(R.id.admin_add_shipping_record_btn);
        adminChooseDeliveryGuySpinner = findViewById(R.id.admin_assign_order_delivery_spinner);
        shippedGuy = findViewById(R.id.branch_receive_order_delivery_guy_name_txt);
        loadingDialog = new ProgressDialog(this);

        adminAddShippingIDTxt.getEditText().setText(shippingID);
        adminAddTrackingIDTxt.getEditText().setText(trackingID);

        showDataSpinner();

        adminAddShippingRecordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });

    }

    private void validateData() {

        if (TextUtils.isEmpty(adminAddShippingIDTxt.getEditText().getText().toString().trim())) {
            Toast.makeText(getApplicationContext(), "Please Enter Shipping Id", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(adminAddTrackingIDTxt.getEditText().getText().toString().trim())) {
            Toast.makeText(getApplicationContext(), "Please Enter Tracking Id", Toast.LENGTH_LONG).show();
        }else if(TextUtils.isEmpty(shippedGuy.getEditText().getText().toString().trim())){
            Toast.makeText(getApplicationContext(), "Please Enter Shipping Guy Username", Toast.LENGTH_LONG).show();
        }else {
            uploadShippingRecord();
        }
    }

    private void uploadShippingRecord(){
        loadingDialog.setTitle("Record Adding");
        loadingDialog.setMessage("Please wait while we are adding");
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.show();

        String currentDate, currentTime, assignDate;

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat getDate = new SimpleDateFormat("MM dd, yyyy");
        currentDate = getDate.format(calendar.getTime());

        SimpleDateFormat getTIme = new SimpleDateFormat("HH:mm:ss a");
        currentTime = getTIme.format(calendar.getTime());

        assignDate = currentDate + " " + currentTime;

        HashMap<String, Object> deliveryGuyShipping = new HashMap<>();
        deliveryGuyShipping.put("shipping", adminAddShippingIDTxt.getEditText().getText().toString());
        deliveryGuyShipping.put("tracking", adminAddTrackingIDTxt.getEditText().getText().toString());
        deliveryGuyShipping.put("from", shippingFrom);
        deliveryGuyShipping.put("to", shippingTo);
        deliveryGuyShipping.put("date", shippingDate);
        deliveryGuyShipping.put("id", trackingID);
        deliveryGuyShipping.put("assignDate", assignDate);
        deliveryGuyShipRef.child(adminChooseDeliveryGuySpinner.getSelectedItem().toString()).child(trackingID).updateChildren(deliveryGuyShipping).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getApplicationContext(), "Record Added for Delivery Guy", Toast.LENGTH_LONG).show();
                adminAddShippingIDTxt.getEditText().getText().clear();
                adminAddTrackingIDTxt.getEditText().getText().clear();

                branchOrders.child("BranchPendingOrders").child(Prevalent.currentOnlineBranch.getBranchCode())
                        .child(trackingID).updateChildren(deliveryGuyShipping).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        branchesOrders.child("ReceiverBranch").child(Prevalent.currentOnlineBranch.getBranchCode())
                                .child(orderID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                deliveryGuyShipRef.child(shippedGuy.getEditText().getText().toString().trim())
                                        .child(trackingID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        loadingDialog.dismiss();
                                        Toast.makeText(getApplicationContext(), "Record Added Successfully", Toast.LENGTH_LONG).show();
                                        BranchAssignOrderForDeliveryActivity.super.onBackPressed();
                                    }
                                });

                            }
                        });
                    }
                });
            }
        });
    }

    private void showDataSpinner() {
        adminChooseDeliverySpinnerRef.orderByChild("deliveryGuyBranch").equalTo(Prevalent.currentOnlineBranch.getBranchCode()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                adminDeliveryGuyList.clear();
                for (DataSnapshot items : snapshot.getChildren()) {
                    DeliveryGuy deliveryGuy = items.getValue(DeliveryGuy.class);
                    if(deliveryGuy.getDeliveryGuyStatus().equals("0")){
                        adminDeliveryGuyList.add(items.child("deliveryGuyUsername").getValue(String.class));
                    }else{
                        Log.d("Spinner", "Not found Data");
                    }
                    Log.d("Spinner", "In loop Not found Data");
                }
                ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, adminDeliveryGuyList);
                categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                adminChooseDeliveryGuySpinner.setAdapter(categoryAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}