package com.ciphers.ecommerce.LoginRetailer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.ciphers.ecommerce.Buyer.HomeActivity;
import com.ciphers.ecommerce.Model.Sellers;
import com.ciphers.ecommerce.Model.Users;
import com.ciphers.ecommerce.Prevalent.Prevalent;
import com.ciphers.ecommerce.R;
import com.ciphers.ecommerce.Sellers.NonVerifySellerActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.installations.InstallationTokenResult;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class RetailerOTP extends AppCompatActivity {

    PinView signUpOTPCode;
    Button signUpVerifyBtn;
    ProgressDialog loadingDialog;
    TextView resendCode, dummyNumber;

    String fullName, username, email, password, phoneNo, image = "null", userType;
    String verificationCodeBySystem, lat, lng;
    private FirebaseAuth mAuth;
    ImageView closeTabsOtp;

    String sellerShopName = "null", sellerShopStatus = "-1", sellerShopIdentity = "null", userToken;

    private PhoneAuthProvider.ForceResendingToken forceResendingToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;
    String mVerificationId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_o_t_p);

        Intent verifyOTPIntent = getIntent();
        fullName = verifyOTPIntent.getStringExtra("fullName");
        username = verifyOTPIntent.getStringExtra("userName");
        email = verifyOTPIntent.getStringExtra("email");
        password = verifyOTPIntent.getStringExtra("password");
        userType = verifyOTPIntent.getStringExtra("category");
        phoneNo = verifyOTPIntent.getStringExtra("phoneNo");

        lat = verifyOTPIntent.getStringExtra("locationLatitude");
        lng = verifyOTPIntent.getStringExtra("locationLongitude");

//        Toast.makeText(getApplicationContext(), "Phone is" +phoneNo, Toast.LENGTH_SHORT).show();
        Log.d("Phone no ", "Phone no is" + phoneNo);
        mAuth = FirebaseAuth.getInstance();


//        FirebaseInstallations.getInstance().getToken(true).addOnCompleteListener(new OnCompleteListener<InstallationTokenResult>() {
//            @Override
//            public void onComplete(@NonNull Task<InstallationTokenResult> task) {
//                if (task.isSuccessful()) {
//                    userToken = Objects.requireNonNull(task.getResult()).getToken();
//                    Log.d("Token", "User Token is " + userToken);
//                }
//            }
//        });

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


        signUpOTPCode = findViewById(R.id.sign_up_otp_code_txt);
        signUpVerifyBtn = findViewById(R.id.sign_up_otp_verify_code_btn);
        resendCode = findViewById(R.id.resend_code);
        dummyNumber = findViewById(R.id.dummy_number);
        closeTabsOtp = findViewById(R.id.close_activity);

        dummyNumber.setText(phoneNo);

        loadingDialog = new ProgressDialog(this);

        signUpVerifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = signUpOTPCode.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    Toast.makeText(getApplicationContext(), "Field is Empty", Toast.LENGTH_LONG).show();
                } else if (code.length() != 6) {
                    Toast.makeText(getApplicationContext(), "Invalid Code", Toast.LENGTH_LONG).show();
                } else {
                    loadingDialog.setTitle("Please Wait");
                    loadingDialog.setMessage("while we are checking credentials");
                    loadingDialog.setCanceledOnTouchOutside(false);
                    loadingDialog.show();
                    dummyNumber.setText(phoneNo);
//                    addUserData();
                    verifyPhoneNumberWithCode(mVerificationId, code);

                }
            }
        });

        resendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                verifyCode();
                resendVerificationCode(phoneNo, forceResendingToken);

            }
        });

        closeTabsOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent otpIntent = new Intent(getApplicationContext(), RetailerMainPage.class);
                otpIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                finish();
                startActivity(otpIntent);
            }
        });

//        verifyCode();


        mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationID, @NonNull PhoneAuthProvider.ForceResendingToken tokenn) {
                super.onCodeSent(verificationID, forceResendingToken);

                Log.d("Code Send", "Code Send");

                mVerificationId = verificationID;

                Toast.makeText(getApplicationContext(), "Verification Code Sent.", Toast.LENGTH_SHORT).show();
                forceResendingToken = tokenn;

            }
        };

        startPhoneNumberVerification(phoneNo);

    }

    private void addUserData() {

        String currentDate;

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat getDate = new SimpleDateFormat("MM, yyyy");
        currentDate = getDate.format(calendar.getTime());

        final DatabaseReference rootRef;
        final DatabaseReference userLocationRef;
        rootRef = FirebaseDatabase.getInstance().getReference();
        userLocationRef = FirebaseDatabase.getInstance().getReference();

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (userType.equals("Sellers")) {

                    HashMap<String, Object> userData = new HashMap<>();
                    userData.put("sellerName", fullName);
                    userData.put("sellerShopName", sellerShopName);
                    userData.put("sellerUsername", username);
                    userData.put("sellerShopPhone", phoneNo);
                    userData.put("sellerShopEmail", email);
                    userData.put("sellerShopIdentity", sellerShopIdentity);
                    userData.put("sellerShopLocation", "null");
                    userData.put("sellerShopCategory", "null");
                    userData.put("sellerStatus", sellerShopStatus);
                    userData.put("password", password);
                    userData.put("sellerJoin", currentDate);
                    userData.put("shopImage", image);

                    rootRef.child("Sellers").child(username).updateChildren(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            HashMap<String, Object> locationMap = new HashMap<>();
                            locationMap.put("Lat", lat);
                            locationMap.put("Lng", lng);
                            locationMap.put("Status", "0");

                            userLocationRef.child("Locations").child("SellersLocations").child(username).updateChildren(locationMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    DatabaseReference tokenRef = FirebaseDatabase.getInstance().getReference().child("SellersTokens");
                                    HashMap<String, Object> tokenMap = new HashMap<>();
                                    tokenMap.put("sellerToken", userToken);
                                    tokenRef.child(username).updateChildren(tokenMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            loadingDialog.dismiss();
                                            Sellers sellerData = new Sellers(password, currentDate, fullName, "null", email, sellerShopIdentity, "null", sellerShopName, phoneNo, sellerShopStatus, username, image);
                                            Prevalent.currentOnlineSeller = sellerData;

                                            Intent otpIntent = new Intent(getApplicationContext(), NonVerifySellerActivity.class);
                                            otpIntent.putExtra("status", "-1");
                                            otpIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(otpIntent);
                                            finish();
                                        }
                                    });

                                }
                            });
                        }
                    });


                } else if (userType.equals("Users")) {

                    HashMap<String, Object> userData = new HashMap<>();
                    userData.put("fullName", fullName);
                    userData.put("username", username);
                    userData.put("phoneNo", phoneNo);
                    userData.put("email", email);
                    userData.put("password", password);
                    userData.put("userImage", image);
                    userData.put("userJoin", currentDate);

                    rootRef.child("Users").child(username).updateChildren(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            HashMap<String, Object> locationMap = new HashMap<>();
                            locationMap.put("Lat", lat);
                            locationMap.put("Lng", lng);

                            userLocationRef.child("Locations").child("UsersLocations").child(username).updateChildren(locationMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    DatabaseReference tokenRef = FirebaseDatabase.getInstance().getReference().child("UserTokens");
                                    HashMap<String, Object> tokenMap = new HashMap<>();
                                    tokenMap.put("buyerToken", userToken);
                                    tokenRef.child(username).updateChildren(tokenMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            loadingDialog.dismiss();
                                            Users userData = new Users(email, fullName, password, phoneNo, username, image, currentDate);
                                            Prevalent.currentOnlineUser = userData;

                                            Intent otpIntent = new Intent(getApplicationContext(), HomeActivity.class);
                                            otpIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(otpIntent);
                                            finish();
                                        }
                                    });
                                }
                            });
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Please Choose User Type", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        RetailerOTP.super.onBackPressed();
    }

    private void startPhoneNumberVerification(String phoneNumber) {

        new CountDownTimer(60000, 1000) {
                        @Override
            public void onTick(long l) {
                resendCode.setText("" + l / 1000);
                resendCode.setEnabled(false);
            }

            @Override
            public void onFinish() {
                resendCode.setText("Resend");
                resendCode.setEnabled(true);
            }
        }.start();

        // [START start_phone_auth]
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallBacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
        // [END start_phone_auth]
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Success", "signInWithCredential:success");
                            addUserData();

                            // Update UI
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w("Failed", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid

                                Toast.makeText(getApplicationContext(), "Invalid Code", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

//    resend Code

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallBacks)          // OnVerificationStateChangedCallbacks
                        .setForceResendingToken(token)     // ForceResendingToken from callbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

}