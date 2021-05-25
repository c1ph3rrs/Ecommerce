package com.ciphers.ecommerce.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ciphers.ecommerce.Interface.itemClickListener;
import com.ciphers.ecommerce.R;
import com.like.LikeButton;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView itemNameTV, itemDescriptionTV, itemSellerNameTV, itemProductPriceTV;
    public ImageView itemImageView;
    public LikeButton likeBtn;
    public itemClickListener listener;


    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        itemImageView = itemView.findViewById(R.id.buyer_product_home_img_cv);
        itemNameTV = itemView.findViewById(R.id.buyer_product_home_name_cv);
        itemSellerNameTV = itemView.findViewById(R.id.buyer_product_seller_name_home_cv);
        itemProductPriceTV = itemView.findViewById(R.id.product_price_lbl);
        likeBtn = itemView.findViewById(R.id.add_to_wish_list_like_iv);
//        itemDescriptionTV = itemView.findViewById(R.id.buyer_product_home_desc_cv);


    }


    public void setItemClickListener(itemClickListener listener ){
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        listener.onCLick(view, getAdapterPosition(), false);
    }
}
