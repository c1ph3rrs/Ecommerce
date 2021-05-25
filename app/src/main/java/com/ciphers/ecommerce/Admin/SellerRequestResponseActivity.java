package com.ciphers.ecommerce.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ciphers.ecommerce.MailService.JavaMailAPI;
import com.ciphers.ecommerce.R;
import com.google.android.material.textfield.TextInputLayout;

public class SellerRequestResponseActivity extends AppCompatActivity {

    String sellerEmail, sellerShopName, sellerStatus;
    TextView sellerRequestResponseStatus, sellerRequestResponseEmail, sellerRequestResponseShopName;
    TextInputLayout sellerRequestResponseTitle, sellerRequestResponseBody;
    Button sellerRequestResponseBtn;
    ProgressDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_request_response);

        sellerEmail = getIntent().getStringExtra("sellerEmail");
        sellerShopName = getIntent().getStringExtra("sellerShopName");
        sellerStatus = getIntent().getStringExtra("status");

        sellerRequestResponseStatus = findViewById(R.id.seller_request_response_status);
        sellerRequestResponseEmail = findViewById(R.id.seller_request_response_email_txt);
        sellerRequestResponseTitle = findViewById(R.id.seller_request_response_email_title_txt);
        sellerRequestResponseBody = findViewById(R.id.seller_request_response_email_body_txt);
        sellerRequestResponseShopName = findViewById(R.id.seller_request_response_shop_txt);

        sellerRequestResponseBtn = findViewById(R.id.seller_request_response_email_btn);

        sellerRequestResponseStatus.setText(sellerStatus);
        sellerRequestResponseEmail.setText(sellerEmail);
        sellerRequestResponseShopName.setText(sellerShopName);

        loadingDialog = new ProgressDialog(this);

        sellerRequestResponseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });
    }

    private void validateData(){

        if(TextUtils.isEmpty(sellerRequestResponseTitle.getEditText().getText().toString().trim())){
            Toast.makeText(getApplicationContext(), "Email Title is Empty", Toast.LENGTH_LONG).show();
        }else if(TextUtils.isEmpty(sellerRequestResponseBody.getEditText().getText().toString().trim())){
            Toast.makeText(getApplicationContext(), "Email Body is Empty", Toast.LENGTH_LONG).show();
        }else{
            sendEmail();
        }
    }

    private void sendEmail() {

        loadingDialog.setTitle("Please Wait...");
        loadingDialog.setMessage("While we are sending Email....");
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.show();

        String messageBody = sellerRequestResponseBody.getEditText().getText().toString().trim();

        String to = sellerEmail;
        String subject = sellerRequestResponseTitle.getEditText().getText().toString().trim();
        String body = messageBody;

        JavaMailAPI javaMailAPI = new JavaMailAPI(this, to, subject, body);
        javaMailAPI.execute();

        loadingDialog.dismiss();

        SellerRequestResponseActivity.super.onBackPressed();

    }

    @Override
    public void onBackPressed() {
        SellerRequestResponseActivity.super.onBackPressed();
    }
}