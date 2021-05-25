package com.ciphers.ecommerce.LoginRetailer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.ciphers.ecommerce.Admin.AdminDashboard;
import com.ciphers.ecommerce.Branch.BranchDashboardActivity;
import com.ciphers.ecommerce.Buyer.HomeActivity;
import com.ciphers.ecommerce.Delivery.DeliveryGuyHomeActivity;
import com.ciphers.ecommerce.Model.Admins;
import com.ciphers.ecommerce.Model.Branch;
import com.ciphers.ecommerce.Model.DeliveryGuy;
import com.ciphers.ecommerce.Model.Sellers;
import com.ciphers.ecommerce.Model.Users;
import com.ciphers.ecommerce.Prevalent.Prevalent;
import com.ciphers.ecommerce.R;
import com.ciphers.ecommerce.Sellers.NonVerifySellerActivity;
import com.ciphers.ecommerce.Sellers.SellerHome;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.installations.InstallationTokenResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import io.paperdb.Paper;

public class LoginRetailer extends AppCompatActivity {

    ImageView loginPageBackIcon;
    TextInputLayout usernameTxt, passwordTxt;
    Button loginBtn, signUpBtn, adminLoginBtn, forgetPasswordBtn;
    ProgressDialog loadingBar;
    CheckBox rememberMe;
    DatabaseReference userTypeRef;
    ArrayList<String> userTypeList = new ArrayList<>();
    Spinner userTypeSpinner;
    String userToken = "";
    String spinnerValue = "abc";
    String customToken = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_retailer);

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (task.isSuccessful()) {
                            userToken = Objects.requireNonNull(task.getResult()).getToken();
                            Log.d("Token", "User Token is " + userToken);
                        }

                    }
                });

//        FirebaseInstallations.getInstance().getToken(true).addOnCompleteListener(new OnCompleteListener<InstallationTokenResult>() {
//            @Override
//            public void onComplete(@NonNull Task<InstallationTokenResult> task) {
//                if (task.isSuccessful()) {
//                    userToken = Objects.requireNonNull(task.getResult()).getToken();
//                    Log.d("Token", "User Token is " + userToken);
//                }
//            }
//        });


        userTypeRef = FirebaseDatabase.getInstance().getReference().child("loginType");
        userTypeSpinner = findViewById(R.id.choose_login_category_spinner);
        showDataSpinner();

        loginPageBackIcon = findViewById(R.id.login_page_back_icon);
        usernameTxt = findViewById(R.id.username_txt);
        passwordTxt = findViewById(R.id.password_txt);

        loginBtn = findViewById(R.id.login_btn);
        signUpBtn = findViewById(R.id.sign_up_btn);
        rememberMe = findViewById(R.id.login_user_remember_me);
        forgetPasswordBtn = findViewById(R.id.forget_password_btn);

        Paper.init(this);

        loadingBar = new ProgressDialog(this);


//        loginAsAdmin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                loginBtn.setText(R.string.login_admin);
//                loginAsAdmin.setVisibility(View.INVISIBLE);
//                notAdmin.setVisibility(View.VISIBLE);
//                parentDBName = "Admins";
//                isAdmin = "true";
//            }
//        });
//
//        notAdmin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                loginBtn.setText(R.string.login);
//                loginAsAdmin.setVisibility(View.VISIBLE);
//                notAdmin.setVisibility(View.INVISIBLE);
//                parentDBName = "Users";
//            }
//        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInternetConnection();

            }
        });

        forgetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ForgetPasswordOneActivity.class));
            }
        });


        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignUpRetailerPageOne.class));
                finish();
            }
        });

        loginPageBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginRetailer.super.onBackPressed();
            }
        });
    }

    private void checkInternetConnection() {
        if (!isConnected(this)) {
            showCustomDialog();
        } else {
            checkUserAndLogin();
        }
    }

    private void showDataSpinner() {
        userTypeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userTypeList.clear();
                for (DataSnapshot items : snapshot.getChildren()) {
                    userTypeList.add(items.child("type").getValue(String.class));
                }
                ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, userTypeList);
                categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                userTypeSpinner.setAdapter(categoryAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkUserAndLogin() {

        String username = usernameTxt.getEditText().getText().toString().trim();
        String password = passwordTxt.getEditText().getText().toString().trim();
        spinnerValue = userTypeSpinner.getSelectedItem().toString();


        if (TextUtils.isEmpty(username)) {
            Toast.makeText(getApplicationContext(), "Username Field is Empty", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Password Field is Empty", Toast.LENGTH_LONG).show();
        } else {
            loadingBar.setTitle("Login...");
            loadingBar.setMessage("Please wait...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            allowAccessToAccount(username, password, spinnerValue);
        }
    }

    private void allowAccessToAccount(final String username, final String password, final String spinnerValue) {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(spinnerValue).child(username).exists()) {

                    String passwordFromDB = dataSnapshot.child(spinnerValue).child(username).child("password").getValue(String.class);

                    if (passwordFromDB.equals(password)) {

                        if (rememberMe.isChecked()) {
                            Paper.book().write(Prevalent.userUserNameKey, username);
                            Paper.book().write(Prevalent.userUserPasswordKey, password);
                            Paper.book().write(Prevalent.dBName, spinnerValue);

                        }

                        if (spinnerValue.equals("Admins")) {
                            loadingBar.dismiss();

                            String dbBranch = dataSnapshot.child(spinnerValue).child(username).child("branch").getValue(String.class);
                            String dbCity = dataSnapshot.child(spinnerValue).child(username).child("city").getValue(String.class);
                            String dbEmail = dataSnapshot.child(spinnerValue).child(username).child("email").getValue(String.class);
                            String dbName = dataSnapshot.child(spinnerValue).child(username).child("fullName").getValue(String.class);
                            String dbPassword = dataSnapshot.child(spinnerValue).child(username).child("password").getValue(String.class);
                            String dbPhone = dataSnapshot.child(spinnerValue).child(username).child("phoneNo").getValue(String.class);
                            String dbUserName = dataSnapshot.child(spinnerValue).child(username).child("username").getValue(String.class);
                            String dbImage = dataSnapshot.child(spinnerValue).child(username).child("userImage").getValue(String.class);

                            Admins adminData = new Admins(dbBranch, dbCity, dbEmail, dbName, dbPassword, dbPhone, dbImage, dbUserName);
                            Prevalent.currentOnlineAdmin = adminData;


                            Intent intent = new Intent(getApplicationContext(), AdminDashboard.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            finish();
                            startActivity(intent);

                        } else if (spinnerValue.equals("Users")) {
                            loadingBar.dismiss();

                            String dbEmail = dataSnapshot.child(spinnerValue).child(username).child("email").getValue(String.class);
                            String dbName = dataSnapshot.child(spinnerValue).child(username).child("fullName").getValue(String.class);
                            String dbPassword = dataSnapshot.child(spinnerValue).child(username).child("password").getValue(String.class);
                            String dbPhone = dataSnapshot.child(spinnerValue).child(username).child("phoneNo").getValue(String.class);
                            String dbUserName = dataSnapshot.child(spinnerValue).child(username).child("username").getValue(String.class);
                            String dbImage = dataSnapshot.child(spinnerValue).child(username).child("userImage").getValue(String.class);
                            String dbJoin = dataSnapshot.child(spinnerValue).child(username).child("userJoin").getValue(String.class);

                            Users userData = new Users(dbEmail, dbName, dbPassword, dbPhone, dbUserName, dbImage, dbJoin);

                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                            Prevalent.currentOnlineUser = userData;

                            customToken = dbUserName + userToken;

                            final DatabaseReference tokenRef = FirebaseDatabase.getInstance().getReference().child("UserTokens");

                            HashMap<String, Object> tokenMap = new HashMap<>();
                            tokenMap.put("buyerToken", String.valueOf(customToken));

                            tokenRef.child(Prevalent.currentOnlineUser.getUsername()).updateChildren(tokenMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
//                                    Toast.makeText(getApplicationContext(), "Token Updated" + userToken, Toast.LENGTH_LONG).show();
                                }
                            });

                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            finish();
                            startActivity(intent);
                        } else if (spinnerValue.equals("Sellers")) {
                            loadingBar.dismiss();

                            String sellerDbName = dataSnapshot.child(spinnerValue).child(username).child("sellerName").getValue(String.class);
                            String sellerDbUsername = dataSnapshot.child(spinnerValue).child(username).child("sellerUsername").getValue(String.class);
                            String sellerDbPassword = dataSnapshot.child(spinnerValue).child(username).child("password").getValue(String.class);
                            String sellerDbShopName = dataSnapshot.child(spinnerValue).child(username).child("sellerShopName").getValue(String.class);
                            String sellerDbShopCategory = dataSnapshot.child(spinnerValue).child(username).child("sellerShopCategory").getValue(String.class);
                            String sellerDbShopEmail = dataSnapshot.child(spinnerValue).child(username).child("sellerShopEmail").getValue(String.class);
                            String sellerDbShopPhone = dataSnapshot.child(spinnerValue).child(username).child("sellerShopPhone").getValue(String.class);
                            String sellerDbShopStatus = dataSnapshot.child(spinnerValue).child(username).child("sellerStatus").getValue(String.class);
                            String sellerDbShopLocation = dataSnapshot.child(spinnerValue).child(username).child("sellerShopLocation").getValue(String.class);
                            String sellerDbIdentity = dataSnapshot.child(spinnerValue).child(username).child("sellerShopIdentity").getValue(String.class);
                            String sellerDbShopLogo = dataSnapshot.child(spinnerValue).child(username).child("shopImage").getValue(String.class);
                            String sellerDbJoin = dataSnapshot.child(spinnerValue).child(username).child("sellerJoin").getValue(String.class);

                            Sellers sellerData = new Sellers(sellerDbPassword, sellerDbJoin, sellerDbName, sellerDbShopCategory, sellerDbShopEmail, sellerDbIdentity,
                                    sellerDbShopLocation, sellerDbShopName, sellerDbShopPhone, sellerDbShopStatus, sellerDbUsername, sellerDbShopLogo);

                            Prevalent.currentOnlineSeller = sellerData;

                            if (sellerDbShopStatus.equals("1")) {
                                Intent intent = new Intent(getApplicationContext(), SellerHome.class);
                                intent.putExtra("status", sellerDbShopStatus);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                finish();
                                startActivity(intent);

                                customToken = sellerDbUsername + userToken;

                                DatabaseReference tokenRef = FirebaseDatabase.getInstance().getReference().child("SellersTokens");
                                HashMap<String, Object> tokenMap = new HashMap<>();
                                tokenMap.put("sellerToken", customToken);
                                tokenRef.child(Prevalent.currentOnlineSeller.getSellerUsername()).updateChildren(tokenMap);

                            } else if (sellerDbShopStatus.equals("-1") || sellerDbShopStatus.equals("0")) {
                                Intent intent = new Intent(getApplicationContext(), NonVerifySellerActivity.class);
                                intent.putExtra("status", sellerDbShopStatus);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                finish();
                                startActivity(intent);

                                customToken = sellerDbUsername + userToken;

                                DatabaseReference tokenRef = FirebaseDatabase.getInstance().getReference().child("SellersTokens");
                                HashMap<String, Object> tokenMap = new HashMap<>();
                                tokenMap.put("sellerToken", customToken);
                                tokenRef.child(Prevalent.currentOnlineSeller.getSellerUsername()).updateChildren(tokenMap);

                            }
                        } else if (spinnerValue.equals("Branches")) {
                            loadingBar.dismiss();

                            String dbAddress = dataSnapshot.child(spinnerValue).child(username).child("branchAddress").getValue(String.class);
                            String dbCity = dataSnapshot.child(spinnerValue).child(username).child("branchCity").getValue(String.class);
                            String dbCode = dataSnapshot.child(spinnerValue).child(username).child("branchCode").getValue(String.class);
                            String dbEmail = dataSnapshot.child(spinnerValue).child(username).child("branchEmail").getValue(String.class);
                            String dbPhone = dataSnapshot.child(spinnerValue).child(username).child("branchPhone").getValue(String.class);
                            String dbPassword = dataSnapshot.child(spinnerValue).child(username).child("password").getValue(String.class);
                            String dbUsername = dataSnapshot.child(spinnerValue).child(username).child("username").getValue(String.class);


                            Branch branchData = new Branch(dbAddress, dbCity, dbCode, dbEmail, dbPhone, dbPassword, dbUsername);

                            Intent intent = new Intent(getApplicationContext(), BranchDashboardActivity.class);
                            Prevalent.currentOnlineBranch = branchData;

                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            finish();
                            startActivity(intent);
                        } else if (spinnerValue.equals("DeliveryGuy")) {
                            loadingBar.dismiss();

                            String dbAddress = dataSnapshot.child(spinnerValue).child(username).child("deliveryGuyAddress").getValue(String.class);
                            String dbEmail = dataSnapshot.child(spinnerValue).child(username).child("deliveryGuyEmail").getValue(String.class);
                            String dbPassword = dataSnapshot.child(spinnerValue).child(username).child("password").getValue(String.class);
                            String dbFullName = dataSnapshot.child(spinnerValue).child(username).child("deliveryGuyFullName").getValue(String.class);
                            String dbImage = dataSnapshot.child(spinnerValue).child(username).child("deliveryGuyImage").getValue(String.class);
                            String dbPhone = dataSnapshot.child(spinnerValue).child(username).child("deliveryGuyPhone").getValue(String.class);
                            String dbStatus = dataSnapshot.child(spinnerValue).child(username).child("deliveryGuyStatus").getValue(String.class);
                            String dbUsername = dataSnapshot.child(spinnerValue).child(username).child("deliveryGuyUsername").getValue(String.class);

                            DeliveryGuy deliveryGuyData = new DeliveryGuy(dbAddress, dbEmail, dbPassword, dbFullName, dbImage, dbPhone, dbStatus, dbUsername);

                            Intent intent = new Intent(getApplicationContext(), DeliveryGuyHomeActivity.class);
                            Prevalent.currentOnlineDeliveryGuy = deliveryGuyData;

                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            finish();
                            startActivity(intent);
                        } else {
                            loadingBar.dismiss();
                            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                        }

                    } else {
                        loadingBar.dismiss();
                        Toast.makeText(getApplicationContext(), "Password does not Match", Toast.LENGTH_LONG).show();
                    }

                } else {
                    loadingBar.dismiss();
                    Toast.makeText(getApplicationContext(), "Account with this " + username + " username do not exists.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    private boolean isConnected(LoginRetailer loginRetailer) {
        ConnectivityManager connectivityManager = (ConnectivityManager) loginRetailer.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected())) {
            return true;
        } else {
            return false;
        }

    }

    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginRetailer.this);
        builder.setMessage("Please connect to the internet to proceed further").setCancelable(false)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(getApplicationContext(), RetailerMainPage.class));
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}