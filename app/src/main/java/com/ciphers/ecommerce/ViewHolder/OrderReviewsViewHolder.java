package com.ciphers.ecommerce.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.recyclerview.widget.RecyclerView;

import com.ciphers.ecommerce.Interface.itemClickListener;
import com.ciphers.ecommerce.R;

public class OrderReviewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

    public itemClickListener listener;
    public TextView reviewName, reviewRatting, reviewText;
    public AppCompatRatingBar rattingBar;


    public OrderReviewsViewHolder(@NonNull View itemView) {
        super(itemView);

        reviewName = itemView.findViewById(R.id.seller_review_show_buyer_name_txt);
        reviewRatting = itemView.findViewById(R.id.seller_ratting_number);
        reviewText = itemView.findViewById(R.id.seller_review_show_buyer_review_txt);
        rattingBar = itemView.findViewById(R.id.seller_completed_buyer_ratting_bar);


    }

    public void setItemClickListener(itemClickListener listener ){
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        listener.onCLick(view, getAdapterPosition(), false);
    }
}
