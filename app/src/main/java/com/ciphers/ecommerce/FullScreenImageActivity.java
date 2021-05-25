package com.ciphers.ecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;
import com.squareup.picasso.Picasso;

public class FullScreenImageActivity extends AppCompatActivity {

    ImageView fullScreenImage, fullScreenBackIcon;
    TextView imageTitleText;
    String imageTitleTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        fullScreenImage = findViewById(R.id.full_screen_image);
        imageTitleText = findViewById(R.id.image_title);
        fullScreenBackIcon = findViewById(R.id.full_screen_back_icon);

        imageTitleTxt = getIntent().getStringExtra("image_title");
        imageTitleText.setText(imageTitleTxt);

        fullScreenBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullScreenImageActivity.super.onBackPressed();
                finish();
            }
        });

        fullScreenImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullScreenImage.setOnTouchListener(new ImageMatrixTouchHandler(v.getContext()));
            }
        });

        Intent callingActivityIntent = getIntent();
        if(callingActivityIntent != null){
            Uri imageUri = callingActivityIntent.getData();
            if(imageUri != null && fullScreenImage != null){
                Picasso.get().load(imageUri).into(fullScreenImage);
            }
        }
    }

    @Override
    public void onBackPressed() {
        FullScreenImageActivity.super.onBackPressed();
        finish();
    }
}