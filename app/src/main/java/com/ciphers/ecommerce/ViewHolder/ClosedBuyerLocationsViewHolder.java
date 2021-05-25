package com.ciphers.ecommerce.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ciphers.ecommerce.Interface.itemClickListener;
import com.ciphers.ecommerce.R;

public class ClosedBuyerLocationsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView closeBuyerLocationTV;
    public itemClickListener itemClickListener;


    public ClosedBuyerLocationsViewHolder(@NonNull View itemView) {
        super(itemView);

        closeBuyerLocationTV = itemView.findViewById(R.id.wish_list_txt);

    }

    public void setItemClickListener(itemClickListener listener) {
        this.itemClickListener = listener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onCLick(view, getAdapterPosition(), false);
    }
}
