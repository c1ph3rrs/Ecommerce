package com.ciphers.ecommerce.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ciphers.ecommerce.Interface.itemClickListener;
import com.ciphers.ecommerce.R;

public class ShippingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public itemClickListener itemClickListener;

    public ShippingViewHolder(@NonNull View itemView) {
        super(itemView);



    }
    public void setItemClickListener(itemClickListener listener) {
        this.itemClickListener = listener;
    }
    @Override
    public void onClick(View v) {
        itemClickListener.onCLick(v, getAdapterPosition(), false);
    }
}
