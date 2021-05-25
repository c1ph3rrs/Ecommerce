package com.ciphers.ecommerce.Buyer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ciphers.ecommerce.Model.Products;
import com.ciphers.ecommerce.Model.Sellers;
import com.ciphers.ecommerce.R;
import com.ciphers.ecommerce.ViewHolder.ProductViewHolder;
import com.ciphers.ecommerce.ViewHolder.SellersViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchSellerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchSellerFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView searchSellerRecycler;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Sellers, SellersViewHolder> sellerAdapter;
    MaterialSearchBar searchBarSeller;
    DatabaseReference sellerRef;
    String getSellerName = "";
    String totalRattingNumbStr = "";

    int totalOrder;
    float totalEarning, ratting;

    public SearchSellerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchSellerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchSellerFragment newInstance(String param1, String param2) {
        SearchSellerFragment fragment = new SearchSellerFragment();
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
        View view = inflater.inflate(R.layout.fragment_search_seller, container, false);

        sellerRef = FirebaseDatabase.getInstance().getReference().child("Sellers");

        searchBarSeller = view.findViewById(R.id.seller_search_bar);

        searchSellerRecycler = view.findViewById(R.id.buyer_seller_recycler);

        searchBarSeller.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                Toast.makeText(getActivity(), "Category is :" + searchBarCategory.getText(), Toast.LENGTH_SHORT).show();
                getSellerName = searchBarSeller.getText();
                seeSearchSellers(getSellerName);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    private void seeSearchSellers(String sellerName){

        searchSellerRecycler.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getActivity(), 2);
        searchSellerRecycler.setLayoutManager(layoutManager);

        Query searchQuery = sellerRef.orderByChild("sellerShopName").startAt(sellerName);

        FirebaseRecyclerOptions<Sellers> options = new FirebaseRecyclerOptions.Builder<Sellers>().
                setQuery(searchQuery, Sellers.class).build();

        sellerAdapter = new FirebaseRecyclerAdapter<Sellers, SellersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull SellersViewHolder sellersViewHolder, int i, @NonNull Sellers sellers) {
                sellersViewHolder.sellerShopNameTV.setText(sellers.getSellerShopName());
                sellersViewHolder.sellerShopCategoryTV.setText(sellers.getSellerShopCategory());
                sellersViewHolder.sellerNameTV.setText(sellers.getSellerName());

                sellersViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent sellerProfileIntent = new Intent(getActivity(), SellerProfileActivity.class);
                        sellerProfileIntent.putExtra("username", sellers.getSellerUsername());
                        startActivity(sellerProfileIntent);
                    }
                });


                Picasso.get().load(sellers.getShopImage()).placeholder(R.drawable.profile).into(sellersViewHolder.sellerProfileIV);

                checkSellerAnalytics(sellersViewHolder.sellerRatting, sellersViewHolder.sellerNumberTV, sellersViewHolder.sellerLevelTV, sellers.getSellerUsername());

            }

            @NonNull
            @Override
            public SellersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_profile_card_view, parent, false);
                SellersViewHolder holder = new SellersViewHolder(view);
                return holder;
            }
        };

        searchSellerRecycler.setAdapter(sellerAdapter);
        sellerAdapter.startListening();

    }

    private void checkSellerAnalytics(AppCompatRatingBar sellerRatting, TextView sellerNumberTV, TextView sellerLevelTV, String sellerUserName) {

        DatabaseReference sellerAnalyticsRef = FirebaseDatabase.getInstance().getReference();

        Query sellerAnalyticsQuery = sellerAnalyticsRef.child("Analytics").child("Sellers");

        ((DatabaseReference) sellerAnalyticsQuery).child(sellerUserName).addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    String totalRatting = snapshot.child("totalRatting").getValue(String.class);
                    String totalNumberRatting = snapshot.child("totalNumberRatting").getValue(String.class);
                    String totalOrders = snapshot.child("totalOrders").getValue(String.class);
                    String totalEarning = snapshot.child("totalEarning").getValue(String.class);

                    totalRattingNumbStr = String.valueOf(Float.parseFloat(totalRatting) / Float.parseFloat(totalNumberRatting));
                    sellerNumberTV.setText("("+totalNumberRatting+")");

                    sellerRatting.setRating(Float.parseFloat(totalRattingNumbStr));
                    sellerRatting.setIsIndicator(true);

                    checkSellerLevel(sellerLevelTV, Float.parseFloat(totalRattingNumbStr), Integer.parseInt(totalOrders), totalEarning);

                }else{
                    sellerRatting.setRating(0);
                    sellerRatting.setIsIndicator(true);
                    sellerNumberTV.setText("(0)");
                    sellerLevelTV.setText("No level");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void checkSellerLevel(TextView sellerLevelTV, Float ratting, int totalOrders, String earning) {

        int levelOneOrders = 15, levelTwoOrders = 50, topOrders = 100;
        float levelOneEarning = 10000, levelTwoEarning = 30000, topEarning = 60000;
        float levelOneRatting = (float) 3.5, levelTwoRatting = (float) 3.9, topRatting = (float) 4.0;

        totalEarning = Float.parseFloat(earning);

        if (totalOrders >= topOrders && totalEarning >= topEarning && ratting >= topRatting) {
            sellerLevelTV.setText("Top Level");
        } else if (totalOrders >= levelTwoOrders && totalEarning >= levelTwoEarning && ratting >= levelTwoRatting) {
            sellerLevelTV.setText("Level Two");
        } else if (totalOrders >= levelOneOrders && totalEarning >= levelOneEarning && ratting >= levelOneRatting) {
            sellerLevelTV.setText("Level One");
        } else {
            sellerLevelTV.setText("No level");
        }

    }

}