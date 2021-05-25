package com.ciphers.ecommerce.Buyer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ciphers.ecommerce.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SellerAboutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SellerAboutFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    CircleImageView sellerShopImg;
    TextView sellerShopNameTxt, sellerShopUserTxt, sellerShopUsernameTxt, sellerShopCategoryTxt, sellerShopPhoneTxt,
            sellerShopEmailTxt, sellerShopAddressTxt, sellerShopMemberTxt, sellerShopReviewsNumberTxt, sellerLevelTxt;

    AppCompatRatingBar sellerRattingBar;
    String getUsername;

    String totalRattingNumbStr = "";

    int totalOrder;
    float totalEarning, ratting;

    public SellerAboutFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SellerAboutFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SellerAboutFragment newInstance(String param1, String param2) {
        SellerAboutFragment fragment = new SellerAboutFragment();
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
        View view = inflater.inflate(R.layout.fragment_seller_about, container, false);

        getUsername = getArguments().getString("usernameOfSeller");

        Log.d("Value is", " Username " + getUsername);

        sellerShopNameTxt = view.findViewById(R.id.seller_shop_name_txt);
        sellerShopUserTxt = view.findViewById(R.id.seller_name_txt);
        sellerShopReviewsNumberTxt = view.findViewById(R.id.seller_ratting_number);
        sellerShopCategoryTxt = view.findViewById(R.id.seller_shop_category_txt);
        sellerShopUsernameTxt = view.findViewById(R.id.seller_shop_username_txt);
        ;
        sellerShopPhoneTxt = view.findViewById(R.id.seller_shop_contact_txt);
        sellerShopEmailTxt = view.findViewById(R.id.seller_shop_email_txt);
        sellerShopAddressTxt = view.findViewById(R.id.seller_shop_address_txt);
        sellerShopMemberTxt = view.findViewById(R.id.seller_shop_member_txt);
        sellerLevelTxt = view.findViewById(R.id.seller_level_txt);

        sellerShopImg = view.findViewById(R.id.seller_profile_image);
        sellerRattingBar = view.findViewById(R.id.seller_ratting_bar);

        sellerShopUsernameTxt.setText(getUsername);

        getSellerInformation(getUsername);

        return view;
    }

    private void getSellerInformation(String username) {

        DatabaseReference sellerRef = FirebaseDatabase.getInstance().getReference();

        Query sellerQuery = sellerRef.child("Sellers").child(username);

        sellerQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    String sellerName = snapshot.child("sellerName").getValue(String.class);
                    String sellerShopCategory = snapshot.child("sellerShopCategory").getValue(String.class);
                    String sellerShopJoin = snapshot.child("sellerJoin").getValue(String.class);
                    String sellerShopEmail = snapshot.child("sellerShopEmail").getValue(String.class);
                    String sellerShopLocation = snapshot.child("sellerShopLocation").getValue(String.class);
                    String sellerShopName = snapshot.child("sellerShopName").getValue(String.class);
                    String sellerShopPhone = snapshot.child("sellerShopPhone").getValue(String.class);
                    String sellerShopImage = snapshot.child("shopImage").getValue(String.class);


                    sellerShopNameTxt.setText(sellerShopName);
                    sellerShopUserTxt.setText(sellerName);
                    sellerShopCategoryTxt.setText(sellerShopCategory);
                    sellerShopPhoneTxt.setText(sellerShopPhone);
                    sellerShopEmailTxt.setText(sellerShopEmail);
                    sellerShopAddressTxt.setText(sellerShopLocation);
                    sellerShopMemberTxt.setText(sellerShopJoin);

                    Picasso.get().load(sellerShopImage).placeholder(R.drawable.profile).into(sellerShopImg);

                    loadSellerAnalytics(username);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void loadSellerAnalytics(String username){

        DatabaseReference sellerAnalyticsRef = FirebaseDatabase.getInstance().getReference();

        Query sellerAnalyticsQuery = sellerAnalyticsRef.child("Analytics").child("Sellers").child(username);

        sellerAnalyticsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    String totalRatting = snapshot.child("totalRatting").getValue(String.class);
                    String totalNumberRatting = snapshot.child("totalNumberRatting").getValue(String.class);
                    String totalOrders = snapshot.child("totalOrders").getValue(String.class);
                    String totalEarning = snapshot.child("totalEarning").getValue(String.class);

                    totalRattingNumbStr = String.valueOf(Float.parseFloat(totalRatting) / Float.parseFloat(totalNumberRatting));
                    sellerShopReviewsNumberTxt.setText("("+totalNumberRatting+")");

                    sellerRattingBar.setRating(Float.parseFloat(totalRattingNumbStr));
                    sellerRattingBar.setIsIndicator(true);

                    checkSellerLevel(Float.parseFloat(totalRattingNumbStr), Integer.parseInt(totalOrders), totalEarning);

                }else{
                    sellerLevelTxt.setText("No Level");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void checkSellerLevel(Float ratting, int totalOrders, String earning) {

        int levelOneOrders = 15, levelTwoOrders = 50, topOrders = 100;
        float levelOneEarning = 10000, levelTwoEarning = 30000, topEarning = 60000;
        float levelOneRatting = (float) 3.5, levelTwoRatting = (float) 3.9, topRatting = (float) 4.0;

        totalEarning = Float.parseFloat(earning);

        if (totalOrders >= topOrders && totalEarning >= topEarning && ratting >= topRatting) {
            sellerLevelTxt.setText("Top Level");
        } else if (totalOrders >= levelTwoOrders && totalEarning >= levelTwoEarning && ratting >= levelTwoRatting) {
            sellerLevelTxt.setText("Level Two");
        } else if (totalOrders >= levelOneOrders && totalEarning >= levelOneEarning && ratting >= levelOneRatting) {
            sellerLevelTxt.setText("Level One");
        } else {
            sellerLevelTxt.setText("No level");
        }

    }

}