package com.ciphers.ecommerce.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ciphers.ecommerce.Interface.itemClickListener;
import com.ciphers.ecommerce.R;

public class WithdrawRequestViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public itemClickListener itemClickListener;
    public TextView sellerPaymentNameTxt, sellerPaymentPriceTxt, sellerPaymentDateTxt;

    public WithdrawRequestViewHolder(@NonNull View itemView) {
        super(itemView);

        sellerPaymentNameTxt = itemView.findViewById(R.id.admin_seller_payment_request_name_txt);
        sellerPaymentPriceTxt = itemView.findViewById(R.id.admin_seller_payment_request_price_txt);
        sellerPaymentDateTxt = itemView.findViewById(R.id.admin_seller_payment_request_date_txt);

    }

    public void setItemClickListener(itemClickListener listener) {
        this.itemClickListener = listener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onCLick(view, getAdapterPosition(), false);
    }

}
