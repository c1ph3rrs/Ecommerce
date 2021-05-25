package com.ciphers.ecommerce.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.recyclerview.widget.RecyclerView;

import com.ciphers.ecommerce.Interface.itemClickListener;
import com.ciphers.ecommerce.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class SellersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public CircleImageView sellerProfileIV;
    public TextView sellerShopNameTV, sellerShopCategoryTV, sellerNameTV, sellerLevelTV, sellerNumberTV;
    public AppCompatRatingBar sellerRatting;

    public itemClickListener itemClickListener;

    public SellersViewHolder(@NonNull View itemView) {
        super(itemView);

        sellerShopNameTV = itemView.findViewById(R.id.seller_shop_name);
        sellerShopCategoryTV = itemView.findViewById(R.id.seller_shop_category_lbl);
        sellerNameTV = itemView.findViewById(R.id.seller_name_lbl);
        sellerLevelTV = itemView.findViewById(R.id.seller_level_lbl);
        sellerNumberTV = itemView.findViewById(R.id.seller_ratting_number);

        sellerProfileIV = itemView.findViewById(R.id.seller_image);
        sellerRatting = itemView.findViewById(R.id.seller_profile_ratting);

    }

    public void setItemClickListener(itemClickListener listener) {
        this.itemClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onCLick(v, getAdapterPosition(), false);
    }
}
