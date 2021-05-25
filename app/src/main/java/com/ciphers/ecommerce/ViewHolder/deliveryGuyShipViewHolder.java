package com.ciphers.ecommerce.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ciphers.ecommerce.Interface.itemClickListener;
import com.ciphers.ecommerce.R;

public class deliveryGuyShipViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView deliveryGuyShippingTxt, deliveryGuyTrackingTxt;
    public itemClickListener listener;

    public deliveryGuyShipViewHolder(@NonNull View itemView) {
        super(itemView);

        deliveryGuyShippingTxt = itemView.findViewById(R.id.delivery_guy_layout_shipping_txt);
        deliveryGuyTrackingTxt = itemView.findViewById(R.id.delivery_guy_layout_tracking_txt);

    }

    public void setItemClickListener(itemClickListener listener ){
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        listener.onCLick(view, getAdapterPosition(), false);
    }

}
