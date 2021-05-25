package com.ciphers.ecommerce.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ciphers.ecommerce.MailService.JavaMailAPI;
import com.ciphers.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminSellerPaymentDetailActivity extends AppCompatActivity {

    TextView sellerAvailableBalanceTxt, sellerPaymentRequestTxt, sellerShopEmailTxt, sellerShopUsernameTxt, sellerAccountNoTxt, sellerBankNameTxt, sellerDateTxt, transactionID;
    Button approveRequestBtn, declineRequestBtn;

    DatabaseReference analyticsRef, withdrawReqRef, sellerEmailRef, paymentMap;
    ImageView sellerPaymentDetailBackIcon;

    String sellerNameStr, paymentKey, paymentBankStr;

    float availableBalanceFloat, requestPayment, newPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_seller_payment_detail);

        sellerNameStr = getIntent().getStringExtra("sellerUsername");

        analyticsRef = FirebaseDatabase.getInstance().getReference().child("Analytics").child("Sellers");
        withdrawReqRef = FirebaseDatabase.getInstance().getReference().child("WithdrawRequest");
        sellerEmailRef = FirebaseDatabase.getInstance().getReference().child("Sellers");
        paymentMap = FirebaseDatabase.getInstance().getReference().child("PaymentHistory");

        sellerAvailableBalanceTxt = findViewById(R.id.seller_available_balance_txt);
        sellerPaymentRequestTxt = findViewById(R.id.seller_request_payment_txt);
        sellerShopEmailTxt = findViewById(R.id.seller_email_txt);
        sellerShopUsernameTxt = findViewById(R.id.seller_username_txt);
        sellerAccountNoTxt = findViewById(R.id.seller_account_no_txt);
        sellerBankNameTxt = findViewById(R.id.seller_account_name_txt);
        sellerDateTxt = findViewById(R.id.seller_payment_date_txt);
        sellerPaymentDetailBackIcon = findViewById(R.id.payment_detail_back_icon);
        transactionID = findViewById(R.id.transaction_id_txt);


        getWithdrawRecordOfSeller();

        sellerShopUsernameTxt.setText(sellerNameStr);

        getSellerAvailableBalance();

        approveRequestBtn = findViewById(R.id.approve_payment_btn);

        sellerPaymentDetailBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdminSellerPaymentDetailActivity.super.onBackPressed();
            }
        });

        approveRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reduceValueFromSeller();
            }
        });

    }

    private void getWithdrawRecordOfSeller() {

        withdrawReqRef.child(sellerNameStr).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    String paymentRequest = snapshot.child("sellerPayment").getValue(String.class);
                    String accountNo = snapshot.child("sellerAccount").getValue(String.class);
                    String accountBank = snapshot.child("sellerBank").getValue(String.class);
                    String withdrawDate = snapshot.child("sellerWithdrawDate").getValue(String.class);
                    String transactionId = snapshot.child("paymentID").getValue(String.class);

                    sellerPaymentRequestTxt.setText("Payment Request : " + paymentRequest);
                    sellerAccountNoTxt.setText("Account No : " + accountNo);
                    sellerBankNameTxt.setText("Bank Name : " + accountBank);
                    sellerDateTxt.setText("Withdraw Date : " + withdrawDate);
                    requestPayment = Float.parseFloat(paymentRequest);
                    transactionID.setText("Transaction ID : " + transactionId);
                    paymentKey = transactionId;

                    paymentBankStr = accountBank;

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getSellerAvailableBalance() {

        analyticsRef.child(sellerNameStr).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    String availableBalance = snapshot.child("availableBalance").getValue(String.class);

                    sellerAvailableBalanceTxt.setText(availableBalance);
                    availableBalanceFloat = Float.parseFloat(availableBalance);

                    getSellerShopEmail();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getSellerShopEmail() {

        sellerEmailRef.child(sellerNameStr).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    String sellerEmail = snapshot.child("sellerShopEmail").getValue(String.class);

                    sellerShopEmailTxt.setText(sellerEmail);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void reduceValueFromSeller() {

        newPayment = availableBalanceFloat - requestPayment;

        HashMap<String, Object> amountMap = new HashMap<>();
        amountMap.put("availableBalance", String.valueOf(newPayment));

        analyticsRef.child(sellerNameStr).updateChildren(amountMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                withdrawReqRef.child(sellerNameStr).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "Payment Success", Toast.LENGTH_SHORT).show();
                        AdminSellerPaymentDetailActivity.super.onBackPressed();
                        updatePaymentStatus();
                    }
                });

            }
        });

    }

    private void updatePaymentStatus() {

        HashMap<String, Object> paymentStatusMap = new HashMap<>();
        paymentStatusMap.put("paymentStatus", "1");

        paymentMap.child(sellerNameStr).child(paymentKey).updateChildren(paymentStatusMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                sendMailToUser();
                Toast.makeText(getApplicationContext(), "Record Updated", Toast.LENGTH_SHORT).show();
                AdminSellerPaymentDetailActivity.super.onBackPressed();
            }
        });
    }

    private void sendMailToUser(){

        String saveCurrentTime, saveCurrentDate;

        Calendar calForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        String messageBody = "Transaction details\n\n" +
                "Transaction number: " + paymentKey + "\n" +
                "Recipient: " + sellerNameStr  + "\n" +
                "Date sent: " + saveCurrentDate + " " + saveCurrentTime + "\n" +
                "Amount sent : " + requestPayment + "\n" +
                "Money sent to : " + paymentBankStr;
                ;

        String to = sellerShopEmailTxt.getText().toString();
        String subject = "Transaction " + paymentKey + " is complete" ;
        String body = messageBody;

        JavaMailAPI javaMailAPI = new JavaMailAPI(this, to, subject, body);
        javaMailAPI.execute();

    }
}