package com.ciphers.ecommerce.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ciphers.ecommerce.Buyer.ProductsDetail;
import com.ciphers.ecommerce.Interface.itemClickListener;
import com.ciphers.ecommerce.Model.Products;
import com.ciphers.ecommerce.R;
import com.ciphers.ecommerce.ViewHolder.ProductViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder>
{
    List<Products> productsList;
    Context context;

    public ProductAdapter( Context context)
    {
        this.productsList = new ArrayList<>();
        this.context = context;
    }

    public void addAll(List<Products> newProducts)
    {
        int initsize=productsList.size();
        productsList.addAll(newProducts);
        notifyItemRangeChanged(initsize,newProducts.size());
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.buyer_product_items_card_design, parent, false);
        ProductViewHolder holder = new ProductViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int position) {
        productViewHolder.itemNameTV.setText(productsList.get(position).getProductName());
        productViewHolder.itemDescriptionTV.setText(productsList.get(position).getProductDescription());
        productViewHolder.itemSellerNameTV.setText(productsList.get(position).getProductSellerID());
//                productViewHolder.itemPriceTV.setText(model.getProductPrice() + "$");
        Picasso.get().load(productsList.get(position).getProductImage1()).placeholder(R.drawable.cart_logo).into(productViewHolder.itemImageView);



//        productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent productIntent = new Intent(getActivity().getApplicationContext(), ProductsDetail.class);
//                productIntent.putExtra("pID", model.getProductId());
//                startActivity(productIntent);
//            }
//        });
    }


    public static class ProductViewHolder extends RecyclerView.ViewHolder
    {

        public TextView itemNameTV, itemDescriptionTV, itemSellerNameTV;
        public ImageView itemImageView;
        public itemClickListener listener;

        public ProductViewHolder(@NonNull View itemView)
        {
            super(itemView);
            itemImageView = itemView.findViewById(R.id.buyer_product_home_img_cv);
            itemNameTV = itemView.findViewById(R.id.buyer_product_home_name_cv);
            itemSellerNameTV = itemView.findViewById(R.id.buyer_product_seller_name_home_cv);
//            itemDescriptionTV = itemView.findViewById(R.id.buyer_product_home_desc_cv);

        }
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }
}



/*

public class UserFragment extends Fragment
{

    View UserFragment;
    RecyclerView recycler_view_user;
  //   LinearLayoutManager manager;    //for linear layout
    NewAdapter adapter;
    String last_key="",last_node="";
    boolean isMaxData=false,isScrolling=false;
    int ITEM_LOAD_COUNT= 10;
    ProgressBar progressBar;

    int currentitems,tottalitems,scrolledoutitems;




}


 */