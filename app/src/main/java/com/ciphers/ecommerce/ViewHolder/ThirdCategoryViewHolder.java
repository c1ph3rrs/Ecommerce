package com.ciphers.ecommerce.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ciphers.ecommerce.Interface.itemClickListener;
import com.ciphers.ecommerce.R;

public class ThirdCategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public itemClickListener itemClickListener;
    public TextView categoryNameTxtOne;
    public ImageView categoryIV;


    public ThirdCategoryViewHolder(@NonNull View itemView) {
        super(itemView);

        categoryNameTxtOne = itemView.findViewById(R.id.admin_category_card_one_txt);
        categoryIV = itemView.findViewById(R.id.admin_category_card_one_img);

    }

    public void setItemClickListener(itemClickListener listener) {
        this.itemClickListener = listener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onCLick(view, getAdapterPosition(), false);
    }
}
