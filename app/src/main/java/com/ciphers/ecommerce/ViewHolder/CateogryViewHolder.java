package com.ciphers.ecommerce.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ciphers.ecommerce.Interface.itemClickListener;
import com.ciphers.ecommerce.R;

public class CateogryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {



    public TextView categoryNameTV;
    public ImageView categoryDDIV;
    public itemClickListener itemClickListener;
    public RelativeLayout thirdCategoryLayout;
    public RecyclerView thirdCategoryRecyclerView;

    public CateogryViewHolder(@NonNull View itemView) {
        super(itemView);


        categoryNameTV = itemView.findViewById(R.id.sub_category_tv);
        categoryDDIV = itemView.findViewById(R.id.sub_category_iv);
        thirdCategoryLayout = itemView.findViewById(R.id.sub_category_detail_recycler);
        thirdCategoryRecyclerView = itemView.findViewById(R.id.third_category_recycler);

    }

    public void setItemClickListener(itemClickListener listener) {
        this.itemClickListener = listener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onCLick(view, getAdapterPosition(), false);
    }
}
