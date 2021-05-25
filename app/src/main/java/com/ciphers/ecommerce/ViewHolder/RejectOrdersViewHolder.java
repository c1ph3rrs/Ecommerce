package com.ciphers.ecommerce.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ciphers.ecommerce.Interface.itemClickListener;
import com.ciphers.ecommerce.R;

public class RejectOrdersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView cartProductNameCV, cartProductPriceCV, cartProductQtyCV, cartProductSellerNameCV, cartProductDateCV, cartProductTypeCV, cartProductTotalPriceCV, cartProductStatus, cartProductRejectMessageCV;
    public ImageView cartProductImageCV;
    public itemClickListener itemClickListener;

    public RejectOrdersViewHolder(@NonNull View itemView) {
        super(itemView);

        cartProductNameCV = itemView.findViewById(R.id.cart_product_title);
        cartProductPriceCV = itemView.findViewById(R.id.cart_product_price);
        cartProductQtyCV = itemView.findViewById(R.id.cart_product_qty);
        cartProductSellerNameCV = itemView.findViewById(R.id.cart_product_seller_name);
        cartProductDateCV = itemView.findViewById(R.id.cart_item_date);
        cartProductTypeCV = itemView.findViewById(R.id.cart_product_type);
        cartProductImageCV = itemView.findViewById(R.id.cart_product_main_img);
        cartProductTotalPriceCV = itemView.findViewById(R.id.cart_product_total_price);
        cartProductStatus = itemView.findViewById(R.id.cart_product_status);
        cartProductRejectMessageCV = itemView.findViewById(R.id.cart_product_reject_message);

    }

    public void setItemClickListener(itemClickListener listener) {
        this.itemClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onCLick(v, getAdapterPosition(), false);
    }

}
