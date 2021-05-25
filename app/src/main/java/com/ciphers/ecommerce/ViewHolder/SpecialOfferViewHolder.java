package com.ciphers.ecommerce.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ciphers.ecommerce.Interface.itemClickListener;
import com.ciphers.ecommerce.R;

public class SpecialOfferViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    public TextView specialOfferProductNameTxt, specialOfferProductSellerTxt, specialOfferProductPriceTxt;
    public ImageView frontSpecialCardImage;

    public TextView sellerSpecialOfferNameTxt, sellerSpecialOfferSellerNameTxt, sellerSpecialOfferPriceTxt;
    public ImageView sellerSpecialOfferIV;

    public itemClickListener itemClickListener;

    public SpecialOfferViewHolder(@NonNull View itemView) {
        super(itemView);

        specialOfferProductNameTxt = itemView.findViewById(R.id.special_offer_card_name_txt);
        specialOfferProductSellerTxt = itemView.findViewById(R.id.special_offer_card_user_name_txt);
        specialOfferProductPriceTxt = itemView.findViewById(R.id.special_offer_price_label);
        frontSpecialCardImage = itemView.findViewById(R.id.special_offer_card_one_img);

        sellerSpecialOfferNameTxt = itemView.findViewById(R.id.seller_special_offer_name_txt);
        sellerSpecialOfferSellerNameTxt = itemView.findViewById(R.id.seller_special_offer_seller_name);
        sellerSpecialOfferPriceTxt = itemView.findViewById(R.id.seller_special_offer_product_price);
        sellerSpecialOfferIV = itemView.findViewById(R.id.seller_special_offer_iv);

    }

    public void setItemClickListener(itemClickListener listener) {
        this.itemClickListener = listener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onCLick(view, getAdapterPosition(), false);
    }
}
