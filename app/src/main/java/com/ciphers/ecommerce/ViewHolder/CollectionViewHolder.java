package com.ciphers.ecommerce.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ciphers.ecommerce.Interface.itemClickListener;
import com.ciphers.ecommerce.R;

public class CollectionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView collectionProductName, collectionProductDescription, collectionProductPrice, collectionProductQuantity;
    public ImageView collectionProductImage;
    public itemClickListener itemClickListener;

    public CollectionViewHolder(@NonNull View itemView) {
        super(itemView);

        collectionProductName = itemView.findViewById(R.id.admin_collection_card_name_txt);
        collectionProductDescription = itemView.findViewById(R.id.admin_collection_card_description_txt);
        collectionProductPrice = itemView.findViewById(R.id.admin_collection_card_price_number_txt);
        collectionProductQuantity = itemView.findViewById(R.id.admin_collection_card_quantity_number_txt);
        collectionProductImage = itemView.findViewById(R.id.admin_collection_card_one_img);

    }

    public void setItemClickListener(itemClickListener listener) {
        this.itemClickListener = listener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onCLick(view, getAdapterPosition(), false);
    }
}
