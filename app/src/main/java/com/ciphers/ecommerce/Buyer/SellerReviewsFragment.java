package com.ciphers.ecommerce.Buyer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ciphers.ecommerce.Model.OrderReviews;
import com.ciphers.ecommerce.Prevalent.Prevalent;
import com.ciphers.ecommerce.R;
import com.ciphers.ecommerce.ViewHolder.OrderReviewsViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SellerReviewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SellerReviewsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    LinearLayout sellerReviewLayout, emptyReviewLayout;

    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<OrderReviews, OrderReviewsViewHolder> ordersReviewAdapter;
    DatabaseReference orderReviewRef;
    RecyclerView reviewRecycler;

    TextView emptyReviewTV;

    String getUsername;
    ImageView noReviewIV;

    public SellerReviewsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SellerReviewsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SellerReviewsFragment newInstance(String param1, String param2) {
        SellerReviewsFragment fragment = new SellerReviewsFragment();
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
        View view = inflater.inflate(R.layout.fragment_seller_reviews, container, false);

        orderReviewRef = FirebaseDatabase.getInstance().getReference().child("Reviews");

        getUsername = getArguments().getString("usernameOfSeller");

        emptyReviewLayout = view.findViewById(R.id.no_review_layout);
        sellerReviewLayout = view.findViewById(R.id.review_recycler_layout);
        reviewRecycler = view.findViewById(R.id.seller_review_recycler);

        emptyReviewTV = view.findViewById(R.id.no_review_tv);
        noReviewIV = view.findViewById(R.id.no_review_img);

        for (int i = 0; i < 2; i++) {

            if (i == 0) {
                showEmptyLayout();
            } else if (i == 1) {
                checkReviewExist();
            }

        }

        return view;
    }

    private void checkReviewExist() {

        orderReviewRef.child("SellersReviews").child(getUsername)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            emptyReviewLayout.setVisibility(View.GONE);
                            startSellerReviewRecycler();
                        } else {
                            showEmptyLayout();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void showEmptyLayout() {

        emptyReviewLayout.setVisibility(View.VISIBLE);

    }

    private void startSellerReviewRecycler() {

        reviewRecycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        reviewRecycler.setLayoutManager(layoutManager);
        FirebaseRecyclerOptions<OrderReviews> options =
                new FirebaseRecyclerOptions.Builder<OrderReviews>()
                        .setQuery(orderReviewRef.child("SellersReviews").child(getUsername),
                                OrderReviews.class)
                        .build();

        ordersReviewAdapter = new FirebaseRecyclerAdapter<OrderReviews, OrderReviewsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrderReviewsViewHolder orderReviewsViewHolder, int i, @NonNull OrderReviews orderReviews) {
                orderReviewsViewHolder.reviewName.setText(orderReviews.getBuyerName());
                orderReviewsViewHolder.reviewText.setText(orderReviews.getReview());
                orderReviewsViewHolder.reviewRatting.setText(orderReviews.getRatting());
                orderReviewsViewHolder.rattingBar.setRating(1);
                orderReviewsViewHolder.rattingBar.setIsIndicator(true);

            }

            @NonNull
            @Override
            public OrderReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new OrderReviewsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.order_review_cv, parent, false));
            }

        };

        reviewRecycler.setAdapter(ordersReviewAdapter);
        ordersReviewAdapter.startListening();

    }
}