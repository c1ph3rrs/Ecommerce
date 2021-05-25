package com.ciphers.ecommerce.Sellers;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ciphers.ecommerce.Prevalent.Prevalent;
import com.ciphers.ecommerce.R;
import com.ciphers.ecommerce.SplashScreen;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SellerDashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SellerDashboardFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    CardView sellerSpecialOfferBtn, sellerDealsBtn, sellerAddProductBtn, sellerViewProductsBtn, sellerNewOrderBtn;
    TextView availableBalanceTxt, newOrdersTxt;

    DatabaseReference availableBalanceRef, totalOrdersRef;

    ImageView sellerProfileIV;


    public SellerDashboardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SellerDashboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SellerDashboardFragment newInstance(String param1, String param2) {
        SellerDashboardFragment fragment = new SellerDashboardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_seller_dashboard, container, false);

        availableBalanceRef = FirebaseDatabase.getInstance().getReference().child("Analytics").child("Sellers");
        totalOrdersRef = FirebaseDatabase.getInstance().getReference().child("PlacedOrder");

        sellerSpecialOfferBtn = view.findViewById(R.id.seller_special_offers_btn);
        sellerDealsBtn = view.findViewById(R.id.seller_deal_icon);
        sellerAddProductBtn = view.findViewById(R.id.seller_add_product_btn);
        sellerNewOrderBtn = view.findViewById(R.id.seller_new_order_btn);
        sellerViewProductsBtn = view.findViewById(R.id.seller_view_products_btn);

        availableBalanceTxt = view.findViewById(R.id.seller_available_balance_txt);
        newOrdersTxt = view.findViewById(R.id.new_orders_number_txt);
        sellerProfileIV = view.findViewById(R.id.seller_profile_iv);

        sellerSpecialOfferBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SellerSpecialOfferOptions.class));
            }
        });

        sellerNewOrderBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(new Intent(getActivity(), SellerOrdersActivity.class));
            }
        });

        sellerDealsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SellerDealOfferOptions.class));
            }
        });

        sellerAddProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SellerAddProduct.class));
            }
        });

        sellerViewProductsBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(new Intent(getActivity(), SellerViewProductsActivity.class));
            }
        });

        checkSellerAvailableBalance();

        checkCurrentNewOrders();

        Picasso.get().load(Prevalent.currentOnlineSeller.getShopImage()).placeholder(R.drawable.profile).into(sellerProfileIV);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        checkSellerAvailableBalance();

        checkCurrentNewOrders();

    }

    private void checkSellerAvailableBalance(){

        availableBalanceRef.child(Prevalent.currentOnlineSeller.getSellerUsername()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    availableBalanceTxt.setText(snapshot.child("availableBalance").getValue(String.class));

                }else{

                    availableBalanceTxt.setText("0.0");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void checkCurrentNewOrders(){

        totalOrdersRef.child(Prevalent.currentOnlineSeller.getSellerUsername()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    newOrdersTxt.setText(String.valueOf(snapshot.getChildrenCount()));

                }else{

                    newOrdersTxt.setText("0");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}