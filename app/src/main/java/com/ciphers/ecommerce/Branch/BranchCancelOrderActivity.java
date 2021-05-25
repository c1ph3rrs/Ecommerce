package com.ciphers.ecommerce.Branch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ciphers.ecommerce.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BranchCancelOrderActivity extends AppCompatActivity {


    TextInputLayout cancelShippingIdTxt, cancelTrackingIdTxt, cancelToTxt, cancelSellerUsernameTxt, cancelBuyerUsernameTxt, cancelDeliveryGuyNameTxt;
    TextView cancelFromTxt;
    Button cancelOrderBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_cancel_order);

        cancelShippingIdTxt = findViewById(R.id.cancel_order_shipping_id_txt);
        cancelTrackingIdTxt = findViewById(R.id.cancel_order_tracking_id_txt);
        cancelFromTxt = findViewById(R.id.return_from_txt);
        cancelToTxt = findViewById(R.id.cancel_order_to_txt);
        cancelSellerUsernameTxt = findViewById(R.id.cancel_order_seller_username_txt);
        cancelBuyerUsernameTxt = findViewById(R.id.cancel_order_buyer_username_txt);
        cancelDeliveryGuyNameTxt = findViewById(R.id.cancel_order_delivery_guy_username_txt);

        cancelOrderBtn = findViewById(R.id.branch_order_cancel_btn);

        cancelOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });
    }

    private void validateData(){

        if(TextUtils.isEmpty(cancelShippingIdTxt.getEditText().getText().toString().trim())){
            Toast.makeText(getApplicationContext(), "Please Enter Shipping Id", Toast.LENGTH_LONG).show();
        }else if(TextUtils.isEmpty(cancelTrackingIdTxt.getEditText().getText().toString().trim())){
            Toast.makeText(getApplicationContext(), "Please Enter Tracking Id", Toast.LENGTH_LONG).show();
        }else if(TextUtils.isEmpty(cancelToTxt.getEditText().getText().toString().trim())){
            Toast.makeText(getApplicationContext(), "Please receiver branch Code", Toast.LENGTH_LONG).show();
        }else if(TextUtils.isEmpty(cancelSellerUsernameTxt.getEditText().getText().toString().trim())){
            Toast.makeText(getApplicationContext(), "Please Enter Seller Username", Toast.LENGTH_LONG).show();
        }else if(TextUtils.isEmpty(cancelBuyerUsernameTxt.getEditText().getText().toString().trim())){
            Toast.makeText(getApplicationContext(), "Please Enter Buyer Username", Toast.LENGTH_LONG).show();
        }else if(TextUtils.isEmpty(cancelDeliveryGuyNameTxt.getEditText().getText().toString().trim())){
            Toast.makeText(getApplicationContext(), "Please Enter Delivery Guy Name", Toast.LENGTH_LONG).show();
        }else{
            checkDataExist();
        }

    }

    private void checkDataExist(){

//        DatabaseReference trackingIdRef = FirebaseDatabase.getInstance().getReference().child("")

    }

}