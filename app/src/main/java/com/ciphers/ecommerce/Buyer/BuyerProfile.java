package com.ciphers.ecommerce.Buyer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
 * Use the {@link BuyerProfile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BuyerProfile extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    CircleImageView buyerProfileImage;
    TextView buyerProfileUsernameTV, buyerProfileNameTV;

    CardView wishListCardView, buyerLogoutBtn, buyerAnalyticsCardView, buyerProfileCardView, buyerChangePasswordCardView, buyerEditProfileCardView;


    String totalRattingNumbStr;
    ImageView buyerAnalyticsLayoutDDIV;
    int buyerAnalyticsDropDownChecker = 0;
    AppCompatRatingBar buyerRatting;

    LinearLayout buyerAnalyticsLayout;
    DatabaseReference buyerAnalyticsRef, buyerTokenRef;
    RelativeLayout buyerReviewLayout;

    int totalOrder;
    float totalEarning, ratting;
    String sellerLevel;

    TextView buyerProfileLevelTxt, totalBuyingNumbTxt, totalOrderNumbTxt, totalRattingNumbTxt;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BuyerProfile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BuyerProfile.
     */
    // TODO: Rename and change types and number of parameters
    public static BuyerProfile newInstance(String param1, String param2) {
        BuyerProfile fragment = new BuyerProfile();
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
        View view = inflater.inflate(R.layout.fragment_buyer_profile, container, false);

        buyerAnalyticsRef = FirebaseDatabase.getInstance().getReference().child("Analytics").child("Buyers");
        buyerTokenRef = FirebaseDatabase.getInstance().getReference().child("UserTokens");

        Paper.init(getActivity());

        buyerLogoutBtn = view.findViewById(R.id.logout_card_view);
        buyerProfileUsernameTV = view.findViewById(R.id.buyer_profile_username);
        buyerProfileNameTV = view.findViewById(R.id.buyer_profile_frontName);
        buyerProfileImage = view.findViewById(R.id.buyer_profile_image);
        wishListCardView = view.findViewById(R.id.wish_list_card_view);

        buyerAnalyticsLayoutDDIV = view.findViewById(R.id.buyer_analytics_drop_arrow_iv);
        buyerAnalyticsLayout = view.findViewById(R.id.buyer_analytics_detail_layout);
        buyerRatting = view.findViewById(R.id.buyer_ratting_bar);

        buyerProfileLevelTxt = view.findViewById(R.id.buyer_profile_level);
        totalBuyingNumbTxt = view.findViewById(R.id.buyer_total_purchase);
        totalOrderNumbTxt = view.findViewById(R.id.buyer_completed_orders);
        totalRattingNumbTxt = view.findViewById(R.id.buyer_total_ratting);

        buyerAnalyticsCardView = view.findViewById(R.id.buyer_analytics_card_view);
        buyerProfileCardView = view.findViewById(R.id.buyer_display_seller_card_view);
        buyerEditProfileCardView = view.findViewById(R.id.buyer_edit_profile_card_view);

        buyerChangePasswordCardView = view.findViewById(R.id.buyer_change_password_card_view);

        buyerAnalyticsLayoutDDIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buyerAnalyticsDropDownChecker == 0) {
                    buyerAnalyticsLayoutDDIV.setImageResource(R.drawable.arrow_drop_down_white);
                    buyerAnalyticsLayout.setVisibility(View.VISIBLE);
                    buyerAnalyticsDropDownChecker = 1;
                } else {
                    buyerAnalyticsLayoutDDIV.setImageResource(R.drawable.arrow_drop_up_white);
                    buyerAnalyticsLayout.setVisibility(View.GONE);
                    buyerAnalyticsDropDownChecker = 0;
                }
            }
        });

        buyerAnalyticsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), BuyerAnalyticsActivity.class));
            }
        });

        buyerProfileCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), BuyerFindActivity.class));
            }
        });

        buyerChangePasswordCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), BuyerChangePasswordActivity.class));
            }
        });

        buyerEditProfileCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), BuyerEditProfileActivity.class));
            }
        });


        buyerLogoutBtn.setOnClickListener(new View.OnClickListener() {
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

                            buyerTokenRef.child(Prevalent.currentOnlineUser.getUsername()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Paper.book().destroy();
                                    Intent logoutIntent = new Intent(getActivity(), SplashScreen.class);
                                    logoutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    getActivity().getFragmentManager().popBackStack();
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

        buyerProfileUsernameTV.setText(Prevalent.currentOnlineUser.getUsername());
        buyerProfileNameTV.setText(Prevalent.currentOnlineUser.getFullName());
        if (Prevalent.currentOnlineUser.getImage().isEmpty()) {
            Picasso.get().load(R.drawable.profile).placeholder(R.drawable.profile).into(buyerProfileImage);
        } else {
            Picasso.get().load(Prevalent.currentOnlineUser.getImage()).placeholder(R.drawable.profile).into(buyerProfileImage);
        }


        wishListCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), BuyerWishListActivity.class));
            }
        });

        fetchBuyerRecord();

        return view;
    }

    private void fetchBuyerRecord() {

        buyerAnalyticsRef.child(Prevalent.currentOnlineUser.getUsername()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    String totalBuying = snapshot.child("totalBuying").getValue(String.class);
                    String totalNumberRatting = snapshot.child("totalNumberRatting").getValue(String.class);
                    String totalOrders = snapshot.child("totalOrders").getValue(String.class);
                    String totalRatting = snapshot.child("totalRatting").getValue(String.class);

                    totalRattingNumbStr = String.valueOf(Float.parseFloat(totalRatting) / Float.parseFloat(totalNumberRatting));

//                    sellerTotalAvailableBalanceTxt.setText(availableBalance);
//                    sellerWithDrawAmountTxt.setText(withdraw);
                    totalBuyingNumbTxt.setText(totalBuying);
                    totalOrderNumbTxt.setText(totalOrders);


                    totalRattingNumbTxt.setText(totalRattingNumbStr);

//                    sellerTotalCancelOrdersTxt.setText("2 (800)");

                    buyerRatting.setRating(Float.parseFloat(totalRattingNumbStr));
                    buyerRatting.setIsIndicator(true);

                    checkSellerAnalytics();

                } else {

                    totalBuyingNumbTxt.setText("0");
                    totalOrderNumbTxt.setText("0");
                    totalRattingNumbTxt.setText("0");
                    buyerProfileLevelTxt.setText("No level");

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
        totalEarning = Float.parseFloat(totalBuyingNumbTxt.getText().toString());
        ratting = Float.parseFloat(String.valueOf(buyerRatting.getRating()));

        if (totalOrder >= topOrders && totalEarning >= topEarning && ratting >= topRatting) {
            buyerProfileLevelTxt.setText("Top Level");
        } else if (totalOrder >= levelTwoOrders && totalEarning >= levelTwoEarning && ratting >= levelTwoRatting) {
            buyerProfileLevelTxt.setText("Level Two");
        } else if (totalOrder >= levelOneOrders && totalEarning >= levelOneEarning && ratting >= levelOneRatting) {
            buyerProfileLevelTxt.setText("Level One");
        } else {
            buyerProfileLevelTxt.setText("No level");
        }

    }
}