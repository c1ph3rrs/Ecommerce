package com.ciphers.ecommerce.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ciphers.ecommerce.Interface.itemClickListener;
import com.ciphers.ecommerce.R;

public class ChallanRecordViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView challanRecordIdTV, challanRecordDescTV;
    public itemClickListener itemClickListener;

    public ChallanRecordViewHolder(@NonNull View itemView) {
        super(itemView);

        challanRecordIdTV = itemView.findViewById(R.id.admin_branch_payment_challan_id_txt);
        challanRecordDescTV = itemView.findViewById(R.id.admin_branch_payment_challan_desc_txt);

    }

    public void setItemClickListener(itemClickListener listener) {
        this.itemClickListener = listener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onCLick(view, getAdapterPosition(), false);
    }


}
