package com.ciphers.ecommerce.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ciphers.ecommerce.Interface.itemClickListener;
import com.ciphers.ecommerce.R;

public class SellerNotificationsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public itemClickListener itemClickListener;
    public TextView notificationSellerTitle, notificationSellerDescription, notificationSellerDate, notificationType;
    public ImageView notificationSellerImg;

    public SellerNotificationsViewHolder(@NonNull View itemView) {
        super(itemView);

        notificationSellerTitle = itemView.findViewById(R.id.notification_title_cv);
        notificationSellerDescription = itemView.findViewById(R.id.notification_description_cv);
        notificationSellerDate = itemView.findViewById(R.id.notification_date_cv);
        notificationSellerImg = itemView.findViewById(R.id.notification_image_cv);
        notificationType = itemView.findViewById(R.id.notification_category_cv);

    }

    public void setItemClickListener(itemClickListener listener) {
        this.itemClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onCLick(v, getAdapterPosition(), false);
    }
}
