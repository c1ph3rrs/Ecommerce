package com.ciphers.ecommerce.Branch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.fonts.Font;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ciphers.ecommerce.Prevalent.Prevalent;
import com.ciphers.ecommerce.R;
import com.ciphers.ecommerce.SplashScreen;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Document;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import io.paperdb.Paper;

public class BranchOrderDetailActivity extends AppCompatActivity {

    TextView productTitleTxt, productPriceTxt, productQtyTxt, productTotalPriceTxt, productBuyerNameTxt, productPaymentTypeTxt, shippingIdTxt, trackingIdTxt;
    TextView productSenderNameTxt, productReceiverNameTxt, productSenderCityTxt, productReceiverCityTxt, productSenderPhoneNoTxt, productReceiverPhoneNoTxt, productAddressTxt;
    ImageView productIV, branchOrderDetailBackIV;
    Button branchCompleteOrderBtn, downloadPdfBtn;
    String orderName, orderQty, orderPrice, orderTotal, paymentType;

    DatabaseReference orderDetailRef, shippingDetailRef, branchDeliveredOrderRef;

    String trackingId, shippingId, activityType, buyerName, from, to;
    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_order_detail);

        trackingId = getIntent().getStringExtra("trackingId");
        shippingId = getIntent().getStringExtra("shippingId");
        activityType = getIntent().getStringExtra("activityType");
        from = getIntent().getStringExtra("from");
        to = getIntent().getStringExtra("to");

        shippingDetailRef = FirebaseDatabase.getInstance().getReference().child("Shipping");
        orderDetailRef = FirebaseDatabase.getInstance().getReference().child("CompletedBuyerOrders");
        branchDeliveredOrderRef = FirebaseDatabase.getInstance().getReference().child("BranchOrders");


        productTitleTxt = findViewById(R.id.branch_order_product_title);
        productPriceTxt = findViewById(R.id.branch_order_product_price);
        productQtyTxt = findViewById(R.id.branch_order_product_qty);
        productTotalPriceTxt = findViewById(R.id.branch_order_total_price);
        productBuyerNameTxt = findViewById(R.id.branch_order_buyer_name_txt);
        productPaymentTypeTxt = findViewById(R.id.branch_order_payment_type);
        shippingIdTxt = findViewById(R.id.branch_order_shipping_id_lbl);
        trackingIdTxt = findViewById(R.id.branch_order_tracking_id_lbl);
        productSenderNameTxt = findViewById(R.id.branch_order_sender_name_lbl);
        productReceiverNameTxt = findViewById(R.id.branch_order_receiver_name_lbl);
        productSenderCityTxt = findViewById(R.id.branch_order_sender_city_lbl);
        productReceiverCityTxt = findViewById(R.id.branch_order_receiver_city_lbl);
        productSenderPhoneNoTxt = findViewById(R.id.branch_order_sender_phone_lbl);
        productReceiverPhoneNoTxt = findViewById(R.id.branch_order_receiver_phone_lbl);
        productAddressTxt = findViewById(R.id.branch_order_shipping_address_lbl);
        productIV = findViewById(R.id.branch_order_image);
        branchOrderDetailBackIV = findViewById(R.id.branch_order_detail_back_icon);
        branchCompleteOrderBtn = findViewById(R.id.branch_complete_order_btn);
        downloadPdfBtn = findViewById(R.id.branch_download_pdf_btn);

        if (activityType.equals("Delivered")) {
            branchCompleteOrderBtn.setVisibility(View.VISIBLE);
            downloadPdfBtn.setVisibility(View.GONE);
        } else if (activityType.equals("Completed")) {
            branchCompleteOrderBtn.setVisibility(View.GONE);
            downloadPdfBtn.setVisibility(View.VISIBLE);
        }

        branchCompleteOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CharSequence options[] = new CharSequence[]{
                        "Yes",
                        "No"
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(BranchOrderDetailActivity.this);
                builder.setTitle("Are you sure you want to Complete this Order.?");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            if (Build.VERSION.SDK_INT >= 23) {
                                if (checkPermission()) {
                                    createMyPDF(v);
                                    completeThisOrder();
                                } else {
                                    requestPermission(); // Code for permission
                                }
                            }
                        } else {
                        }
                    }
                });
                builder.show();

            }
        });

        downloadPdfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkPermission()) {
                        createMyPDF(v);
                    } else {
                        requestPermission(); // Code for permission
                    }
                }
            }
        });

        fetchOrderDetail();

    }

    public void createMyPDF(View view) {

        int totalPrice;

        PdfDocument myPdfDocument = new PdfDocument();
        PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
        PdfDocument.Page myPage = myPdfDocument.startPage(myPageInfo);

        Paint myPaint = new Paint();

        totalPrice = Integer.parseInt(orderQty) * Integer.parseInt(orderPrice);

        int x = 10, y = 25;

        myPage.getCanvas().drawText("Ib Express", 100, 25, myPaint);
        myPage.getCanvas().drawText("Item Name \t \t" + orderName, 10, 50, myPaint);
        myPage.getCanvas().drawText("Item Qty \t \t" + orderQty, 10, 75, myPaint);
        myPage.getCanvas().drawText("Order Price \t \t" + orderPrice, 10, 100, myPaint);
        myPage.getCanvas().drawText("Total Price \t \t" + totalPrice, 10, 125, myPaint);
        myPage.getCanvas().drawText("Grand Total \t \t" + orderTotal, 10, 150, myPaint);

        myPdfDocument.finishPage(myPage);

        String myFilePath = Environment.getExternalStorageDirectory().getPath() + "/Ib Express/" + trackingId + ".pdf";
        File storagePath = new File(Environment.getExternalStorageDirectory() + "/Ib Express");

        if (storagePath.exists()) {

            File myFile = new File(myFilePath);
            try {
                myPdfDocument.writeTo(new FileOutputStream(myFile));
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Please check Your Storage Permission", Toast.LENGTH_LONG).show();
            }

        } else {
            Log.d("Pdf", "Folder Created");
            storagePath.mkdirs();

            File myFile = new File(myFilePath);
            try {
                myPdfDocument.writeTo(new FileOutputStream(myFile));
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Please check Your Storage Permission", Toast.LENGTH_LONG).show();
            }

        }

        myPdfDocument.close();
    }

    private void completeThisOrder() {

        String currentDate, currentTime, date;

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat getDate = new SimpleDateFormat("MM dd, yyyy");
        currentDate = getDate.format(calendar.getTime());

        SimpleDateFormat getTIme = new SimpleDateFormat("HH:mm:ss a");
        currentTime = getTIme.format(calendar.getTime());

        date = currentDate + " " + currentTime;

        HashMap<String, Object> shippedMap = new HashMap<>();
        shippedMap.put("id", trackingId);
        shippedMap.put("tracking", trackingId);
        shippedMap.put("shipping", shippingId);
        shippedMap.put("from", from);
        shippedMap.put("to", to);
        shippedMap.put("date", date);
        shippedMap.put("buyer", buyerName);


        branchDeliveredOrderRef.child("BranchCompletedOrders").child(Prevalent.currentOnlineBranch.getBranchCode()).child(trackingId)
                .updateChildren(shippedMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                branchDeliveredOrderRef.child("BranchDeliveredOrders").child(Prevalent.currentOnlineBranch.getBranchCode())
                        .child(trackingId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "Record Updated. Go to App folder and get pdf and pay to Bank", Toast.LENGTH_LONG).show();
                        BranchOrderDetailActivity.super.onBackPressed();
                    }
                });
            }
        });
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(BranchOrderDetailActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(BranchOrderDetailActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(BranchOrderDetailActivity.this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(BranchOrderDetailActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }

    private void fetchOrderDetail() {

        shippingDetailRef.child(shippingId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String dbReceiverCity = snapshot.child("receiverCity").getValue(String.class);
                String dbReceiverName = snapshot.child("receiverName").getValue(String.class);
                String dbReceiverPhone = snapshot.child("receiverPhone").getValue(String.class);
                String dbSenderCity = snapshot.child("senderCity").getValue(String.class);
                String dbSenderName = snapshot.child("senderName").getValue(String.class);
                String dbSenderPhone = snapshot.child("senderPhone").getValue(String.class);
                String dbShippingAddress = snapshot.child("shippingAddress").getValue(String.class);
                buyerName = snapshot.child("buyerName").getValue(String.class);

                productSenderNameTxt.setText("Sender Name : " + dbSenderName);
                productSenderCityTxt.setText("Sender City : " + dbSenderCity);
                productSenderPhoneNoTxt.setText("Sender Phone : " + dbSenderPhone);
                productReceiverNameTxt.setText("Receiver Name : " + dbReceiverName);
                productReceiverCityTxt.setText("Receiver City : " + dbReceiverCity);
                productReceiverPhoneNoTxt.setText("Receiver Phone : " + dbReceiverPhone);
                productAddressTxt.setText("Shipping Address : " + dbShippingAddress);

                showOrderData(buyerName);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Make sure you have an active Internet", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void showOrderData(String orderBuyerName) {

        orderDetailRef.child(orderBuyerName).child(trackingId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String dbDeliverOrderName = snapshot.child("orderName").getValue(String.class);
                String dbDeliverOrderIV = snapshot.child("orderImg").getValue(String.class);
                String dbDeliverOrderPayment = snapshot.child("paymentType").getValue(String.class);
                String dbDeliverOrderPrice = snapshot.child("orderPrice").getValue(String.class);
                String dbDeliverOrderQty = snapshot.child("orderQty").getValue(String.class);
                String dbDeliverOrderTPrice = snapshot.child("orderTotalPrice").getValue(String.class);

                productTitleTxt.setText(dbDeliverOrderName);
                orderName = dbDeliverOrderName;

                productQtyTxt.setText(dbDeliverOrderQty);
                orderQty = dbDeliverOrderQty;

                productPriceTxt.setText(dbDeliverOrderPrice);
                orderPrice = dbDeliverOrderPrice;

                productTotalPriceTxt.setText(dbDeliverOrderTPrice);
                orderTotal = dbDeliverOrderTPrice;

                productBuyerNameTxt.setText(buyerName);

                productPaymentTypeTxt.setText(dbDeliverOrderPayment);
                paymentType = dbDeliverOrderPayment;

                Picasso.get().load(dbDeliverOrderIV).into(productIV);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}