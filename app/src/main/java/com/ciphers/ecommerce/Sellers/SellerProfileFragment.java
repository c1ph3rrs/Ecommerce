package com.ciphers.ecommerce.Sellers;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ciphers.ecommerce.Prevalent.Prevalent;
import com.ciphers.ecommerce.R;
import com.ciphers.ecommerce.SplashScreen;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SellerProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SellerProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    int totalOrder;
    float totalEarning, ratting;
    String sellerLevel;

    TextView sellerProfileNameTxt, sellerProfileUsernameTxt, sellerProfileSellerLevelTxt, totalEarningNumbTxt, totalOrderNumbTxt, totalRattingNumbTxt;
    TextView sellerTotalAvailableBalanceTxt, sellerTotalActiveOrdersTxt, sellerWithDrawAmountTxt;
    CircleImageView sellerProfileImg;
    String totalRattingNumbStr;
    ImageView sellerAnalyticsLayoutDDIV;
    int sellerAnalyticsDropDownChecker = 0;
    AppCompatRatingBar sellerRatting;

    LinearLayout sellerAnalyticsLayout;
    DatabaseReference sellerAnalyticsRef, sellerTokenRef;
    RelativeLayout sellerReviewLayout;

    CardView sellerAnalyticsCardView, sellerLogoutCardView, sellerFindBuyerCardView, sellerWithdrawCardView;

    public SellerProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SellerProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SellerProfileFragment newInstance(String param1, String param2) {
        SellerProfileFragment fragment = new SellerProfileFragment();
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

        View view = inflater.inflate(R.layout.fragment_seller_profile, container, false);

        sellerAnalyticsRef = FirebaseDatabase.getInstance().getReference().child("Analytics").child("Sellers");

        sellerTokenRef = FirebaseDatabase.getInstance().getReference().child("SellersTokens");

        sellerProfileNameTxt = view.findViewById(R.id.seller_profile_frontName);
        sellerProfileUsernameTxt = view.findViewById(R.id.seller_profile_username);
        sellerProfileSellerLevelTxt = view.findViewById(R.id.seller_profile_level);
        totalEarningNumbTxt = view.findViewById(R.id.seller_total_earning);
        totalOrderNumbTxt = view.findViewById(R.id.seller_completed_orders);
        totalRattingNumbTxt = view.findViewById(R.id.seller_total_ratting);
        sellerTotalAvailableBalanceTxt = view.findViewById(R.id.seller_available_balance_number_txt);
        sellerTotalActiveOrdersTxt = view.findViewById(R.id.active_orders_number_txt);

        sellerProfileImg = view.findViewById(R.id.seller_profile_image);
        sellerWithDrawAmountTxt = view.findViewById(R.id.seller_withdraw_number_txt);
        sellerAnalyticsLayoutDDIV = view.findViewById(R.id.seller_analytics_drop_arrow_iv);
        sellerAnalyticsLayout = view.findViewById(R.id.seller_analytics_detail_layout);
        sellerRatting = view.findViewById(R.id.seller_ratting_bar);
        sellerReviewLayout = view.findViewById(R.id.seller_reviews_layout);
        sellerAnalyticsCardView = view.findViewById(R.id.seller_analytics_card_view);
        sellerLogoutCardView = view.findViewById(R.id.logout_seller_card_view);
        sellerFindBuyerCardView = view.findViewById(R.id.near_by_buyers_card_view);
        sellerWithdrawCardView = view.findViewById(R.id.seller_withdraw_card_view);

        fetchSellerRecord();
        fetchSellerActiveOrders();
        fetchSellerCancelOrders();

        sellerProfileUsernameTxt.setText(Prevalent.currentOnlineSeller.getSellerUsername());
        sellerProfileNameTxt.setText(Prevalent.currentOnlineSeller.getSellerShopName());
        Picasso.get().load(Prevalent.currentOnlineSeller.getShopImage()).into(sellerProfileImg);

        sellerAnalyticsLayoutDDIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (sellerAnalyticsDropDownChecker == 0) {
                    sellerAnalyticsLayoutDDIV.setImageResource(R.drawable.arrow_drop_down_white);
                    sellerAnalyticsLayout.setVisibility(View.VISIBLE);
                    sellerAnalyticsDropDownChecker = 1;
                } else {
                    sellerAnalyticsLayoutDDIV.setImageResource(R.drawable.arrow_drop_up_white);
                    sellerAnalyticsLayout.setVisibility(View.GONE);
                    sellerAnalyticsDropDownChecker = 0;
                }

            }
        });

        sellerFindBuyerCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SellerFindBuyersActivity.class));
            }
        });

        sellerReviewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SellerReviewsActivity.class));
            }
        });

        sellerAnalyticsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SellerAnalyticsActivity.class));
            }
        });

        sellerWithdrawCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SellerWithdrawActivity.class));
            }
        });

        sellerLogoutCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence options[] = new CharSequence[]{
                        "Yes",
                        "No"
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Are you sure you want to Logout.?");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            sellerTokenRef.child(Prevalent.currentOnlineSeller.getSellerUsername()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Paper.book().destroy();
                                    Intent logoutIntent = new Intent(getActivity(), SplashScreen.class);
                                    logoutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(logoutIntent);
                                }
                            });

                        } else {
                        }
                    }
                });
                builder.show();
            }
        });

        return view;
    }

    private void fetchSellerActiveOrders() {

        DatabaseReference activeOrderRef = FirebaseDatabase.getInstance().getReference()
                .child("SellersOrders").child(Prevalent.currentOnlineSeller.getSellerUsername());

        activeOrderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    sellerTotalActiveOrdersTxt.setText(String.valueOf((int) snapshot.getChildrenCount()));
                } else {
                    sellerTotalActiveOrdersTxt.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void fetchSellerCancelOrders() {
    }

    private void fetchSellerRecord() {

        sellerAnalyticsRef.child(Prevalent.currentOnlineSeller.getSellerUsername()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    String availableBalance = snapshot.child("availableBalance").getValue(String.class);
                    String totalEarning = snapshot.child("totalEarning").getValue(String.class);
                    String totalNumberRatting = snapshot.child("totalNumberRatting").getValue(String.class);
                    String totalOrders = snapshot.child("totalOrders").getValue(String.class);
                    String totalRatting = snapshot.child("totalRatting").getValue(String.class);
                    String withdraw = snapshot.child("withdraw").getValue(String.class);

                    totalRattingNumbStr = String.valueOf(Float.parseFloat(totalRatting) / Float.parseFloat(totalNumberRatting));

                    sellerTotalAvailableBalanceTxt.setText(availableBalance);
                    sellerWithDrawAmountTxt.setText(withdraw);
                    totalEarningNumbTxt.setText(totalEarning);
                    totalOrderNumbTxt.setText(totalOrders);


                    totalRattingNumbTxt.setText(totalRattingNumbStr);


                    sellerRatting.setRating(Float.parseFloat(totalRattingNumbStr));
                    sellerRatting.setIsIndicator(true);

                    checkSellerAnalytics();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void checkSellerAnalytics() {

        int levelOneOrders = 15, levelTwoOrders = 50, topOrders = 100;
        float levelOneEarning = 10000, levelTwoEarning = 30000, topEarning = 60000;
        float levelOneRatting = (float) 3.5, levelTwoRatting = (float) 3.9, topRatting = (float) 4.0;

        totalOrder = Integer.parseInt(totalOrderNumbTxt.getText().toString());
        totalEarning = Float.parseFloat(totalEarningNumbTxt.getText().toString());
        ratting = Float.parseFloat(String.valueOf(sellerRatting.getRating()));

        if (totalOrder >= topOrders && totalEarning >= topEarning && ratting >= topRatting) {
            sellerProfileSellerLevelTxt.setText("Top Level");
        } else if (totalOrder >= levelTwoOrders && totalEarning >= levelTwoEarning && ratting >= levelTwoRatting) {
            sellerProfileSellerLevelTxt.setText("Level Two");
        } else if (totalOrder >= levelOneOrders && totalEarning >= levelOneEarning && ratting >= levelOneRatting) {
            sellerProfileSellerLevelTxt.setText("Level One");
        } else {
            sellerProfileSellerLevelTxt.setText("No level");
        }

    }

}