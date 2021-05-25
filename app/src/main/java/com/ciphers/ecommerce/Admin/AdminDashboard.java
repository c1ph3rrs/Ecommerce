package com.ciphers.ecommerce.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.ciphers.ecommerce.R;
import com.ciphers.ecommerce.SplashScreen;

import io.paperdb.Paper;

public class AdminDashboard extends AppCompatActivity {

    Button adminLogoutBtn;
    ImageView adminCategoriesBtn, adminAddNewCategory;
    ImageView adminSellersRequestBtn, addNewShopCategory, viewShopCategories;
    ImageView sellerPaymentRequest;
    ImageView adminAddNewBranchIV, adminBranchPaymentsIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        adminLogoutBtn = findViewById(R.id.admin_logout_btn);
        adminCategoriesBtn = findViewById(R.id.admin_categories_btn);
        adminAddNewCategory = findViewById(R.id.admin_add_new_category);

        adminAddNewBranchIV = findViewById(R.id.admin_add_new_branch_btn);
//        adminAddNewDeliveryGuyIV = findViewById(R.id.admin_add_delivery_guy_iv);
        addNewShopCategory = findViewById(R.id.admin_add_new_shop_category_btn);
        viewShopCategories = findViewById(R.id.admin_view_all_shop_categories_btn);
//        adminAssignOrderForDeliveryBtn = findViewById(R.id.admin_assign_order_for_delivery_btn);
        adminBranchPaymentsIV = findViewById(R.id.admin_branch_payments_btn);

        adminSellersRequestBtn = findViewById(R.id.admin_new_sellers_requests_btn);

        sellerPaymentRequest = findViewById(R.id.admin_view_seller_payment_request_iv);

        adminSellersRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AdminSellersRequestsActivity.class));
            }
        });

        sellerPaymentRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AdminSellerPaymentsRequestsActivity.class));
            }
        });

        addNewShopCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addNewShopCategory = new Intent(getApplicationContext(), AdminAddNewCategory.class);
                addNewShopCategory.putExtra("type", "new shop category");
                startActivity(addNewShopCategory);
            }
        });

        adminBranchPaymentsIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AdminBranchPaymentsActivity.class));
            }
        });

        adminAddNewBranchIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AdminAddBranchActivity.class));
            }
        });

        viewShopCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        adminCategoriesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AdminCategoriesActivity.class));
            }
        });

        adminAddNewCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent adminNewCategoryIntent = new Intent(getApplicationContext(), AdminAddNewCategory.class);
                adminNewCategoryIntent.putExtra("type", "product category");
                startActivity(adminNewCategoryIntent);
            }
        });


        adminLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Paper.book().destroy();
                Intent logoutIntent = new Intent(getApplicationContext(), SplashScreen.class);
                logoutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                finish();
                startActivity(logoutIntent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        AdminDashboard.super.onBackPressed();
    }

}