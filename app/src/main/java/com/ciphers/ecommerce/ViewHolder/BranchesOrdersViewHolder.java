package com.ciphers.ecommerce.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ciphers.ecommerce.Interface.itemClickListener;
import com.ciphers.ecommerce.R;

public class BranchesOrdersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView branchShippingIDCV, branchTrackingIDCV, branchShippingFromCV, branchShippingToCV, branchShippingDateCV;
    public ImageView sellerRequestIV;

    public itemClickListener itemClickListener;

    public BranchesOrdersViewHolder(@NonNull View itemView) {
        super(itemView);

        branchShippingIDCV = itemView.findViewById(R.id.branch_order_shipping_id_cv);
        branchTrackingIDCV = itemView.findViewById(R.id.branch_order_tracking_id_cv);
        branchShippingFromCV = itemView.findViewById(R.id.branch_shipping_from_cv);
        branchShippingToCV = itemView.findViewById(R.id.branch_shipping_to_cv);
        branchShippingDateCV = itemView.findViewById(R.id.branch_shipping_date_cv);

    }

    public void setItemClickListener(itemClickListener listener) {
        this.itemClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onCLick(v, getAdapterPosition(), false);
    }
}
