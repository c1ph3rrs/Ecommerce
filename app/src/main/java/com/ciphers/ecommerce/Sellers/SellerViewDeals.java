package com.ciphers.ecommerce.Sellers;

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
import android.widget.TextView;
import android.widget.Toast;

import com.ciphers.ecommerce.Prevalent.Prevalent;
import com.ciphers.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class SellerViewDeals extends AppCompatActivity {

    ImageView sellerCloseBtn, sellerSaveIcon, sellerImage1, sellerImage2, sellerImage3, sellerImage4;
    TextInputLayout sellerDealNameTxt, sellerDealPriceTxt, sellerDealQtyTxt, sellerDealDescTxt;
    TextView sellerDealIdTxt;

    DatabaseReference dealRef;
    StorageReference dealStorageRef;
    ProgressDialog progressDialog;

    String productID, productName, productDesc, productQty, productPrice, productImage1, productImage2, productImage3, productImage4;
    String productIDTxt, productNameTxt, productDescTxt, productQtyTxt, productPriceTxt;
    Button sellerViewDealDeleteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_view_deals);

        productID = getIntent().getStringExtra("dealProductID");

        dealRef = FirebaseDatabase.getInstance().getReference().child("Deals").child(productID);
        dealStorageRef = FirebaseStorage.getInstance().getReference().child("Deals Images");

        productName = getIntent().getStringExtra("dealProductName");
        productDesc = getIntent().getStringExtra("dealProductDescription");
        productQty = getIntent().getStringExtra("dealProductQtyLeft");
        productPrice = getIntent().getStringExtra("dealProductPrice");
        productImage1 = getIntent().getStringExtra("dealProductImage1");
        productImage2 = getIntent().getStringExtra("dealProductImage2");
        productImage3 = getIntent().getStringExtra("dealProductImage3");
        productImage4 = getIntent().getStringExtra("dealProductImage4");


        sellerCloseBtn = findViewById(R.id.seller_deal_view_close_btn);
        sellerSaveIcon = findViewById(R.id.seller_save_deal_btn);

        sellerDealNameTxt = findViewById(R.id.seller_view_deal_name);
        sellerDealDescTxt = findViewById(R.id.seller_view_deal_description);
        sellerDealPriceTxt = findViewById(R.id.seller_view_deal_price);
        sellerDealQtyTxt = findViewById(R.id.admin_view_deal_quantity);
        sellerDealIdTxt = findViewById(R.id.seller_deal_product_id);
        sellerViewDealDeleteBtn = findViewById(R.id.seller_view_deal_delete_button);

        sellerImage1 = findViewById(R.id.seller_view_deal_image_main);
        sellerImage2 = findViewById(R.id.seller_view_deal_image_two);
        sellerImage3 = findViewById(R.id.seller_view_deal_image_three);
        sellerImage4 = findViewById(R.id.seller_view_deal_image_four);

        progressDialog = new ProgressDialog(this);

        sellerDealNameTxt.getEditText().setText(productName);
        sellerDealDescTxt.getEditText().setText(productDesc);
        sellerDealPriceTxt.getEditText().setText(productPrice);
        sellerDealQtyTxt.getEditText().setText(productQty);
        sellerDealIdTxt.setText(productID);


        Picasso.get().load(productImage1).placeholder(R.drawable.insert_photo).fit().centerCrop().into(sellerImage1);
        Picasso.get().load(productImage2).placeholder(R.drawable.insert_photo).fit().centerCrop().into(sellerImage2);
        Picasso.get().load(productImage3).placeholder(R.drawable.insert_photo).fit().centerCrop().into(sellerImage3);
        Picasso.get().load(productImage4).placeholder(R.drawable.insert_photo).fit().centerCrop().into(sellerImage4);

        sellerCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SellerViewDeals.super.onBackPressed();
            }
        });

        sellerSaveIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });

        sellerViewDealDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence options[] = new CharSequence[]{
                        "Yes",
                        "No"
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(SellerViewDeals.this);
                builder.setTitle("Are you sure you want to Delete this Deal.?");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            deleteData();
                        } else {
                            finish();
                        }
                    }
                });
                builder.show();

            }
        });

    }

    private void deleteData() {

        progressDialog.setTitle("Deleting");
        progressDialog.setMessage("Please wait....");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        dealRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                SellerViewDeals.super.onBackPressed();
                finish();
                Toast.makeText(getApplicationContext(), "Deal Deleted Successfully", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void validateData() {

        productNameTxt = sellerDealNameTxt.getEditText().getText().toString().trim();
        productDescTxt = sellerDealDescTxt.getEditText().getText().toString().trim();
        productPriceTxt = sellerDealPriceTxt.getEditText().getText().toString().trim();
        productQtyTxt = sellerDealQtyTxt.getEditText().getText().toString().trim();

        if (TextUtils.isEmpty(productNameTxt)) {
            Toast.makeText(getApplicationContext(), "Deal Name is Empty", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(productDescTxt)) {
            Toast.makeText(getApplicationContext(), "Deal Description is Empty", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(productPriceTxt)) {
            Toast.makeText(getApplicationContext(), "Deal Price is Empty", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(productQtyTxt)) {
            Toast.makeText(getApplicationContext(), "Deal Qty is Empty", Toast.LENGTH_SHORT).show();
        } else {
            updateDealData();
        }
    }

    private void updateDealData() {

        progressDialog.setTitle("Apply Changing");
        progressDialog.setMessage("Please wait....");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        //            productMap.put("specialProductImage1", productDescription);
        //            productMap.put("specialProductImage2", productName);
        //            productMap.put("specialProductImage3", productPrice);
        //            productMap.put("specialProductImage4", productId);

        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("dealProductId", productID);
        productMap.put("dealProductName", sellerDealNameTxt.getEditText().getText().toString().trim());
        productMap.put("dealProductPrice", sellerDealPriceTxt.getEditText().getText().toString().trim());
        productMap.put("dealProductDescription", sellerDealDescTxt.getEditText().getText().toString().trim());
        productMap.put("dealProductQuantity", sellerDealQtyTxt.getEditText().getText().toString().trim());
        productMap.put("dealProductQuantityLeft", sellerDealQtyTxt.getEditText().getText().toString().trim());
        productMap.put("dealProductSellerID", Prevalent.currentOnlineSeller.getSellerUsername());
        dealRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Record Updated Successfully", Toast.LENGTH_SHORT).show();
                    SellerViewDeals.super.onBackPressed();
                    finish();
                } else {
                    progressDialog.dismiss();
                    String errorNo = task.getException().toString();
                    Toast.makeText(getApplicationContext(), "Make Sure you have an active INTERNET. OR Please Try Again ", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        SellerViewDeals.super.onBackPressed();
    }
}