package com.ciphers.ecommerce.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ciphers.ecommerce.Interface.itemClickListener;
import com.ciphers.ecommerce.R;

public class DealViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView dealProductName, dealProductDesc, dealProductPrice, dealProductQty;
    public ImageView dealProductImage;
    public itemClickListener listener;
    public RatingBar rattingBar;

    public TextView buyerDealProductNameTxt, buyerDealProductPriceTxt;
    public ImageView buyerDealProductImg;

    public TextView frontDealCardName, frontDealCardSeller, frontDealCardID;
    public ImageView frontDealCardImage;

    public DealViewHolder(@NonNull View itemView) {
        super(itemView);

        frontDealCardName = itemView.findViewById(R.id.special_offer_card_name_txt);
        frontDealCardSeller = itemView.findViewById(R.id.special_offer_card_user_name_txt);
        frontDealCardImage = itemView.findViewById(R.id.special_offer_card_one_img);

//        buyerDealProductNameTxt = itemView.findViewById(R.id.buyer_deal_name_card_home_txt);
//        buyerDealProductPriceTxt = itemView.findViewById(R.id.buyer_deal_price_home_txt);
        buyerDealProductImg = itemView.findViewById(R.id.buyer_deal_card_home_img);

    }

    public void setItemClickListener(itemClickListener listener ){
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        listener.onCLick(view, getAdapterPosition(), false);
    }


}
