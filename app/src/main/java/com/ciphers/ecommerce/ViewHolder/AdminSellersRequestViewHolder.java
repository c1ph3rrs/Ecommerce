package com.ciphers.ecommerce.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ciphers.ecommerce.Interface.itemClickListener;
import com.ciphers.ecommerce.R;

public class AdminSellersRequestViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView sellerRequestName, sellerRequestShopName, sellerRequestUsername, sellerRequestEmail, sellerRequestAddress, sellerRequestPhone, sellerRequestJoinDate, sellerRequestLicence, sellerRequestCategory;
    public ImageView sellerRequestIV;

    public itemClickListener itemClickListener;

    public AdminSellersRequestViewHolder(@NonNull View itemView) {
        super(itemView);

        sellerRequestName = itemView.findViewById(R.id.seller_request_name_txt_cv);
        sellerRequestShopName = itemView.findViewById(R.id.seller_request_shop_name_txt_cv);
        sellerRequestUsername = itemView.findViewById(R.id.seller_request_username_txt_cv);
        sellerRequestEmail = itemView.findViewById(R.id.seller_request_email_txt_cv);
        sellerRequestPhone = itemView.findViewById(R.id.seller_request_phone_no_txt_cv);
        sellerRequestAddress = itemView.findViewById(R.id.seller_request_address_txt_cv);
        sellerRequestJoinDate = itemView.findViewById(R.id.seller_request_join_date_txt_cv);
        sellerRequestLicence = itemView.findViewById(R.id.seller_request_licence_number_txt_cv);
        sellerRequestCategory = itemView.findViewById(R.id.seller_request_shop_category_txt_cv);

        sellerRequestIV = itemView.findViewById(R.id.seller_request_image_cv);

    }

    public void setItemClickListener(itemClickListener listener) {
        this.itemClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onCLick(v, getAdapterPosition(), false);
    }
}
