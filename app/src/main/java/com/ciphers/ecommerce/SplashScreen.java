package com.ciphers.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.ciphers.ecommerce.Admin.AdminDashboard;
import com.ciphers.ecommerce.Branch.BranchDashboardActivity;
import com.ciphers.ecommerce.Buyer.HomeActivity;
import com.ciphers.ecommerce.Delivery.DeliveryGuyHomeActivity;
import com.ciphers.ecommerce.LoginRetailer.RetailerMainPage;
import com.ciphers.ecommerce.Model.Admins;
import com.ciphers.ecommerce.Model.Branch;
import com.ciphers.ecommerce.Model.DeliveryGuy;
import com.ciphers.ecommerce.Model.Sellers;
import com.ciphers.ecommerce.Model.Users;
import com.ciphers.ecommerce.Prevalent.Prevalent;
import com.ciphers.ecommerce.Sellers.NonVerifySellerActivity;
import com.ciphers.ecommerce.Sellers.SellerHome;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class SplashScreen extends AppCompatActivity {


    private static int screenTimer = 5000;

    ImageView backgroundImage;
    TextView poweredByLine;

    Animation sideAnim, bottomAnim;

    String passwordKey, usernameKey, parentDBNameKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_screen);

        backgroundImage = findViewById(R.id.splashScreenBackground);
        poweredByLine = findViewById(R.id.poweredByLine);

        sideAnim = AnimationUtils.loadAnimation(this, R.anim.side_anim);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_anim);

        backgroundImage.setAnimation(sideAnim);
        poweredByLine.setAnimation(bottomAnim);

        Paper.init(this);
        usernameKey = Paper.book().read(Prevalent.userUserNameKey);
        passwordKey = Paper.book().read(Prevalent.userUserPasswordKey);
        parentDBNameKey = Paper.book().read(Prevalent.dBName);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (usernameKey != "" && passwordKey != "") {
                    if (!TextUtils.isEmpty(usernameKey) && !TextUtils.isEmpty(passwordKey)) {
                        allowAccess(usernameKey, passwordKey, parentDBNameKey);
                    } else {
                        Intent loginIntent = new Intent(getApplicationContext(), RetailerMainPage.class);
                        loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        finish();
                        startActivity(loginIntent);

                    }
                } else {
                    Intent loginIntent = new Intent(getApplicationContext(), RetailerMainPage.class);
                    loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    finish();
                    startActivity(loginIntent);

                }
            }
        }, screenTimer);
    }

    private void allowAccess(final String usernameKey, final String passwordKey, final String parentDBNameKey) {

        if (!isConnected(this)) {
            showCustomDialog();
        }

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();


        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(parentDBNameKey).child(usernameKey).exists()) {

                    String passwordFromDB = dataSnapshot.child(parentDBNameKey).child(usernameKey).child("password").getValue(String.class);

                    if (passwordFromDB.equals(passwordKey)) {



                        if (parentDBNameKey.equals("Admins")) {

                            String dbBranch = dataSnapshot.child(parentDBNameKey).child(usernameKey).child("branch").getValue(String.class);
                            String dbCity = dataSnapshot.child(parentDBNameKey).child(usernameKey).child("city").getValue(String.class);
                            String dbEmail = dataSnapshot.child(parentDBNameKey).child(usernameKey).child("email").getValue(String.class);
                            String dbName = dataSnapshot.child(parentDBNameKey).child(usernameKey).child("fullName").getValue(String.class);
                            String dbPassword = dataSnapshot.child(parentDBNameKey).child(usernameKey).child("password").getValue(String.class);
                            String dbPhone = dataSnapshot.child(parentDBNameKey).child(usernameKey).child("phoneNo").getValue(String.class);
                            String dbUserName = dataSnapshot.child(parentDBNameKey).child(usernameKey).child("username").getValue(String.class);
                            String dbImage = dataSnapshot.child(parentDBNameKey).child(usernameKey).child("userImage").getValue(String.class);

                            Admins adminData = new Admins(dbBranch, dbCity, dbEmail, dbName, dbPassword, dbPhone, dbImage, dbUserName);
                            Prevalent.currentOnlineAdmin = adminData;

                            Intent intent = new Intent(getApplicationContext(), AdminDashboard.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            finish();
                            startActivity(intent);

                        } else if (parentDBNameKey.equals("Users")) {

                            String dbEmail = dataSnapshot.child(parentDBNameKey).child(usernameKey).child("email").getValue(String.class);
                            String dbName = dataSnapshot.child(parentDBNameKey).child(usernameKey).child("fullName").getValue(String.class);
                            String dbPassword = dataSnapshot.child(parentDBNameKey).child(usernameKey).child("password").getValue(String.class);
                            String dbPhone = dataSnapshot.child(parentDBNameKey).child(usernameKey).child("phoneNo").getValue(String.class);
                            String dbUserName = dataSnapshot.child(parentDBNameKey).child(usernameKey).child("username").getValue(String.class);
                            String dbImage = dataSnapshot.child(parentDBNameKey).child(usernameKey).child("userImage").getValue(String.class);
                            String dbJoin = dataSnapshot.child(parentDBNameKey).child(usernameKey).child("userJoin").getValue(String.class);

                            Users userData = new Users(dbEmail, dbName, dbPassword, dbPhone, dbUserName, dbImage, dbJoin);

                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                            Prevalent.currentOnlineUser = userData;

                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            finish();
                            startActivity(intent);


                        } else if (parentDBNameKey.equals("Sellers")) {

                            String sellerDbName = dataSnapshot.child(parentDBNameKey).child(usernameKey).child("sellerName").getValue(String.class);
                            String sellerDbUsername = dataSnapshot.child(parentDBNameKey).child(usernameKey).child("sellerUsername").getValue(String.class);
                            String sellerDbPassword = dataSnapshot.child(parentDBNameKey).child(usernameKey).child("password").getValue(String.class);
                            String sellerDbShopName = dataSnapshot.child(parentDBNameKey).child(usernameKey).child("sellerShopName").getValue(String.class);
                            String sellerDbShopCategory = dataSnapshot.child(parentDBNameKey).child(usernameKey).child("sellerShopCategory").getValue(String.class);
                            String sellerDbShopEmail = dataSnapshot.child(parentDBNameKey).child(usernameKey).child("sellerShopEmail").getValue(String.class);
                            String sellerDbShopPhone = dataSnapshot.child(parentDBNameKey).child(usernameKey).child("sellerShopPhone").getValue(String.class);
                            String sellerDbShopLocation = dataSnapshot.child(parentDBNameKey).child(usernameKey).child("sellerShopLocation").getValue(String.class);
                            String sellerDbShopStatus = dataSnapshot.child(parentDBNameKey).child(usernameKey).child("sellerStatus").getValue(String.class);
                            String sellerDbIdentity = dataSnapshot.child(parentDBNameKey).child(usernameKey).child("sellerShopIdentity").getValue(String.class);
                            String sellerDbShopLogo = dataSnapshot.child(parentDBNameKey).child(usernameKey).child("shopImage").getValue(String.class);
                            String sellerDbJoin = dataSnapshot.child(parentDBNameKey).child(usernameKey).child("sellerJoin").getValue(String.class);

                            Sellers sellerData = new Sellers(sellerDbPassword, sellerDbJoin, sellerDbName,sellerDbShopCategory, sellerDbShopEmail, sellerDbIdentity,
                                    sellerDbShopLocation, sellerDbShopName, sellerDbShopPhone, sellerDbShopStatus, sellerDbUsername, sellerDbShopLogo);

                            Prevalent.currentOnlineSeller = sellerData;

                            if (sellerDbShopStatus.equals("1")) {
                                Intent intent = new Intent(getApplicationContext(), SellerHome.class);
                                intent.putExtra("status", sellerDbShopStatus);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                finish();
                                startActivity(intent);
                            } else if (sellerDbShopStatus.equals("-1") || sellerDbShopStatus.equals("0")) {
                                Intent intent = new Intent(getApplicationContext(), NonVerifySellerActivity.class);
                                intent.putExtra("status", sellerDbShopStatus);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                finish();
                                startActivity(intent);
                            }
                        } else if (parentDBNameKey.equals("DeliveryGuy")) {

                            String dbAddress = dataSnapshot.child(parentDBNameKey).child(usernameKey).child("deliveryGuyAddress").getValue(String.class);
                            String dbEmail = dataSnapshot.child(parentDBNameKey).child(usernameKey).child("deliveryGuyEmail").getValue(String.class);
                            String dbPassword = dataSnapshot.child(parentDBNameKey).child(usernameKey).child("password").getValue(String.class);
                            String dbFullName = dataSnapshot.child(parentDBNameKey).child(usernameKey).child("deliveryGuyFullName").getValue(String.class);
                            String dbImage = dataSnapshot.child(parentDBNameKey).child(usernameKey).child("deliveryGuyImage").getValue(String.class);
                            String dbPhone = dataSnapshot.child(parentDBNameKey).child(usernameKey).child("deliveryGuyPhone").getValue(String.class);
                            String dbStatus = dataSnapshot.child(parentDBNameKey).child(usernameKey).child("deliveryGuyStatus").getValue(String.class);
                            String dbUsername = dataSnapshot.child(parentDBNameKey).child(usernameKey).child("deliveryGuyUsername").getValue(String.class);

                            DeliveryGuy deliveryGuyData = new DeliveryGuy(dbAddress, dbEmail, dbPassword, dbFullName, dbImage, dbPhone,dbStatus, dbUsername);

                            Intent intent = new Intent(getApplicationContext(), DeliveryGuyHomeActivity.class);
                            Prevalent.currentOnlineDeliveryGuy = deliveryGuyData;

                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            finish();
                            startActivity(intent);
                        }else if (parentDBNameKey.equals("Branches")) {

                            String dbAddress = dataSnapshot.child(parentDBNameKey).child(usernameKey).child("branchAddress").getValue(String.class);
                            String dbCity = dataSnapshot.child(parentDBNameKey).child(usernameKey).child("branchCity").getValue(String.class);
                            String dbCode = dataSnapshot.child(parentDBNameKey).child(usernameKey).child("branchCode").getValue(String.class);
                            String dbEmail = dataSnapshot.child(parentDBNameKey).child(usernameKey).child("branchEmail").getValue(String.class);
                            String dbPhone = dataSnapshot.child(parentDBNameKey).child(usernameKey).child("branchPhone").getValue(String.class);
                            String dbPassword = dataSnapshot.child(parentDBNameKey).child(usernameKey).child("password").getValue(String.class);
                            String dbUsername =dataSnapshot.child(parentDBNameKey).child(usernameKey).child("username").getValue(String.class);

                            Branch branchData = new Branch(dbAddress, dbCity, dbCode, dbEmail, dbPhone, dbPassword, dbUsername);

                            Intent intent = new Intent(getApplicationContext(), BranchDashboardActivity.class);
                            Prevalent.currentOnlineBranch = branchData;

                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            finish();
                            startActivity(intent);
                        }

                    } else {
                        startActivity(new Intent(getApplicationContext(), RetailerMainPage.class));

                    }

                } else {
                    startActivity(new Intent(getApplicationContext(), RetailerMainPage.class));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private boolean isConnected(SplashScreen splashScreen) {
        ConnectivityManager connectivityManager = (ConnectivityManager) splashScreen.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected())) {
            return true;
        } else {
            return false;
        }
    }


    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreen.this);
        builder.setMessage("Please connect to the internet to proceed further").setCancelable(false)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                        System.exit(0);
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}