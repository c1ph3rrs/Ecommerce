package com.ciphers.ecommerce.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ciphers.ecommerce.Interface.itemClickListener;
import com.ciphers.ecommerce.R;

public class SellerCompletedOrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView sellerOrderPriceCV, sellerOrderTitleCV, sellerOrderQtyCV, sellerOrderBuyerNameCV, sellerOrderStatusCV, sellerOrderTotalPriceCV, sellerOrderTypeCV, sellerOrderDateCV, sellerOrderShippingIDCV, sellerOrderTrackingIDCV;
    public ImageView sellerOrderImageCV;
    public com.ciphers.ecommerce.Interface.itemClickListener itemClickListener;

    public SellerCompletedOrderViewHolder(@NonNull View itemView) {
        super(itemView);

        sellerOrderPriceCV = itemView.findViewById(R.id.seller_order_product_price);
        sellerOrderTitleCV = itemView.findViewById(R.id.seller_order_product_title);
        sellerOrderQtyCV = itemView.findViewById(R.id.seller_order_product_qty);
        sellerOrderBuyerNameCV = itemView.findViewById(R.id.seller_order_product_buyer_name);
        sellerOrderStatusCV = itemView.findViewById(R.id.seller_order_product_status);
        sellerOrderTotalPriceCV = itemView.findViewById(R.id.seller_order_product_total_price);
        sellerOrderDateCV = itemView.findViewById(R.id.seller_order_item_date);
        sellerOrderTypeCV = itemView.findViewById(R.id.seller_order_product_type);
        sellerOrderShippingIDCV = itemView.findViewById(R.id.seller_order_shipping_id);
        sellerOrderTrackingIDCV = itemView.findViewById(R.id.seller_order_tracking_id);
        sellerOrderImageCV = itemView.findViewById(R.id.seller_order_product_main_img);

    }

    public void setItemClickListener(itemClickListener listener) {
        this.itemClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onCLick(v, getAdapterPosition(), false);
    }
}
