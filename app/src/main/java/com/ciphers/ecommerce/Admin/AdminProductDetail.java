package com.ciphers.ecommerce.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.ciphers.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminProductDetail extends AppCompatActivity {

    ImageView adminProductDetailImage, adminProductDetailDelete, adminProductDetailClose;
    TextInputLayout adminProductDetailName, adminProductDetailPrice, adminProductDetailDescription;
    DatabaseReference adminProductDetailRef;
    String productId;

    Button adminApplyChangeBtn;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product_detail);

        productId = getIntent().getStringExtra("pID").toString();

        adminProductDetailRef = FirebaseDatabase.getInstance().getReference().child("Products").child(productId);


        adminProductDetailImage = findViewById(R.id.admin_product_detail_image);
        adminProductDetailName = findViewById(R.id.admin_product_detail_name_edit_txt);
        adminProductDetailPrice = findViewById(R.id.admin_product_detail_price_edit_txt);
        adminProductDetailDescription = findViewById(R.id.admin_product_detail_description_edit_txt);
        adminApplyChangeBtn = findViewById(R.id.admin_product_detail_apply_change_btn);
        progressDialog = new ProgressDialog(this);
        adminProductDetailDelete = findViewById(R.id.admin_product_detail_delete_btn);
        adminProductDetailClose = findViewById(R.id.close_product_detail_icon);

        displaySpecificProductInfo();

        adminApplyChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applyChange();
            }
        });

        adminProductDetailDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence options[] = new CharSequence[]{
                        "Yes",
                        "No"
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(AdminProductDetail.this);
                builder.setTitle("Are you sure you want to Delete this Product.?");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            deleteProduct();
                        } else {
                            finish();
                        }
                    }
                });
                builder.show();

            }
        });

        adminProductDetailClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdminProductDetail.super.onBackPressed();
            }
        });

    }

    private void deleteProduct() {
        adminProductDetailRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                AdminProductDetail.super.onBackPressed();
                Toast.makeText(getApplicationContext(), "Product Deleted Successfully", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void applyChange() {

        String productName = adminProductDetailName.getEditText().getText().toString();
        String productPrice = adminProductDetailPrice.getEditText().getText().toString();
        String productDescription = adminProductDetailDescription.getEditText().getText().toString();

        if (TextUtils.isEmpty(productName)) {
            Toast.makeText(getApplicationContext(), "Product Name is empty", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(productPrice)) {
            Toast.makeText(getApplicationContext(), "Product Price is empty", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(productDescription)) {
            Toast.makeText(getApplicationContext(), "Product Description is empty", Toast.LENGTH_LONG).show();
        } else {

            progressDialog.setTitle("Apply Changing");
            progressDialog.setMessage("Please wait....");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            HashMap<String, Object> productMap = new HashMap<>();
            productMap.put("productID", productId);
            productMap.put("productDescription", productDescription);
            productMap.put("productName", productName);
            productMap.put("productPrice", productPrice);
            adminProductDetailRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Product Updated", Toast.LENGTH_SHORT).show();
                        AdminProductDetail.super.onBackPressed();
                        finish();
                    } else {
                        progressDialog.dismiss();
                        String errorNo = task.getException().toString();
                        Toast.makeText(getApplicationContext(), "Adding Error : " + errorNo, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    private void displaySpecificProductInfo() {

        adminProductDetailRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String productName = snapshot.child("productName").getValue().toString();
                    String productPrice = snapshot.child("productPrice").getValue().toString();
                    String productDescription = snapshot.child("productDescription").getValue().toString();
                    String productImage = snapshot.child("productImage").getValue().toString();

                    Picasso.get().load(productImage).into(adminProductDetailImage);
                    adminProductDetailName.getEditText().setText(productName);
                    adminProductDetailPrice.getEditText().setText(productPrice);
                    adminProductDetailDescription.getEditText().setText(productDescription);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        AdminProductDetail.super.onBackPressed();
    }
}