package com.ciphers.ecommerce.Buyer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ciphers.ecommerce.Model.Cart;
import com.ciphers.ecommerce.Model.Products;
import com.ciphers.ecommerce.Prevalent.Prevalent;
import com.ciphers.ecommerce.R;
import com.ciphers.ecommerce.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.shreyaspatil.firebase.recyclerpagination.DatabasePagingOptions;
import com.shreyaspatil.firebase.recyclerpagination.FirebaseRecyclerPagingAdapter;
import com.shreyaspatil.firebase.recyclerpagination.LoadingState;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartProductFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView cartProductRecyclerView;
    ImageView emptyCartIcon;
    TextView emptyCartText;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerPagingAdapter<Cart, CartViewHolder> cartAdapter;
    DatabaseReference cartRef;
    int itemTotalPrice = 0, totalPrice = 0;
    Button proceedFurther;
    int totalCartItemCounter = 0;
    SwipeRefreshLayout cartFragmentProductRefreshLayout;
    int shippingCharges, totalShippingCharges = 0;

    String currentOnlineUser;

    public CartProductFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartProductFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CartProductFragment newInstance(String param1, String param2) {
        CartProductFragment fragment = new CartProductFragment();
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
        View view = inflater.inflate(R.layout.fragment_cart_product, container, false);

        currentOnlineUser = Prevalent.currentOnlineUser.getUsername();
        cartRef = FirebaseDatabase.getInstance().getReference().child("Cart List").child(currentOnlineUser);

        emptyCartIcon = view.findViewById(R.id.empty_cart_icon);
        emptyCartText = view.findViewById(R.id.empty_cart_text);
        cartProductRecyclerView = view.findViewById(R.id.buyer_cart_recycler);
        proceedFurther = view.findViewById(R.id.cart_proceed_more_btn);
        cartFragmentProductRefreshLayout = view.findViewById(R.id.cart_fragment_product_refresh_layout);

        proceedFurther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shippingDetailIntent = new Intent(getActivity(), ShippingDetailActivity.class);
                shippingDetailIntent.putExtra("totalPrice", String.valueOf(totalPrice));
                shippingDetailIntent.putExtra("totalCartItems", String.valueOf(totalCartItemCounter));
                shippingDetailIntent.putExtra("shippingCharges", String.valueOf(totalShippingCharges));
                startActivity(shippingDetailIntent);
            }
        });
        startNoDataLayout();
        checkRejectOrders();

        cartFragmentProductRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cartFragmentProductRefreshLayout.setRefreshing(false);
            }
        });

        return view;
    }


    void checkRejectOrders() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Cart List");
        Query queries = ref.child(currentOnlineUser);
        queries.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    cartFragmentProductRefreshLayout.setRefreshing(false);
                    proceedFurther.setVisibility(View.VISIBLE);
                    emptyCartIcon.setVisibility(View.GONE);
                    emptyCartText.setVisibility(View.GONE);
                    startCartProductRecycler();

                } else {
                    cartFragmentProductRefreshLayout.setRefreshing(false);
                    startNoDataLayout();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    void startNoDataLayout() {
        cartFragmentProductRefreshLayout.setRefreshing(false);
        emptyCartIcon.setVisibility(View.VISIBLE);
        emptyCartText.setVisibility(View.VISIBLE);
        proceedFurther.setVisibility(View.GONE);
    }

    private void startCartProductRecycler() {

        cartProductRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        cartProductRecyclerView.setLayoutManager(layoutManager);

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(5)
                .setPageSize(10)
                .build();


        DatabasePagingOptions<Cart> options = new DatabasePagingOptions.Builder<Cart>()
                .setLifecycleOwner(this)
                .setQuery(cartRef, config, Cart.class)
                .build();


        cartAdapter = new FirebaseRecyclerPagingAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, int i, @NonNull Cart cart) {

                cartFragmentProductRefreshLayout.setRefreshing(false);


                itemTotalPrice = Integer.parseInt(cart.getProductPrice()) * Integer.parseInt(cart.getProductQuantity());
                totalPrice += itemTotalPrice;

                if (Integer.parseInt(cart.getProductPrice()) <= 100) {
                    shippingCharges = 40;
                } else if (Integer.parseInt(cart.getProductPrice()) <= 200 && Integer.parseInt(cart.getProductPrice()) > 100) {
                    shippingCharges = 60;
                } else if (Integer.parseInt(cart.getProductPrice()) <= 400 && Integer.parseInt(cart.getProductPrice()) > 200) {
                    shippingCharges = 100;
                } else if (Integer.parseInt(cart.getProductPrice()) <= 800 && Integer.parseInt(cart.getProductPrice()) > 400) {
                    shippingCharges = 170;
                } else if (Integer.parseInt(cart.getProductPrice()) <= 1200 && Integer.parseInt(cart.getProductPrice()) > 800) {
                    shippingCharges = 200;
                } else if (Integer.parseInt(cart.getProductPrice()) <= 1500 && Integer.parseInt(cart.getProductPrice()) > 1200) {
                    shippingCharges = 250;
                } else if (Integer.parseInt(cart.getProductPrice()) <= 2000 && Integer.parseInt(cart.getProductPrice()) > 1500) {
                    shippingCharges = 270;
                } else if (Integer.parseInt(cart.getProductPrice()) <= 3000 && Integer.parseInt(cart.getProductPrice()) > 2000) {
                    shippingCharges = 310;
                } else if (Integer.parseInt(cart.getProductPrice()) <= 5000 && Integer.parseInt(cart.getProductPrice()) > 3000) {
                    shippingCharges = 360;
                } else if (Integer.parseInt(cart.getProductPrice()) <= 7000 && Integer.parseInt(cart.getProductPrice()) > 5000) {
                    shippingCharges = 410;
                } else if (Integer.parseInt(cart.getProductPrice()) <= 10000 && Integer.parseInt(cart.getProductPrice()) > 7000) {
                    shippingCharges = 450;
                } else if (Integer.parseInt(cart.getProductPrice()) <= 15000 && Integer.parseInt(cart.getProductPrice()) > 10000) {
                    shippingCharges = 550;
                } else {
                    shippingCharges = 750;
                }

                totalShippingCharges += shippingCharges;

                totalCartItemCounter = i;

//                totalCartItemsCounter = String.copyValueOf(i);

                cartViewHolder.cartProductNameCV.setText(cart.getProductName());
                cartViewHolder.cartProductPriceCV.setText(cart.getProductPrice());
                cartViewHolder.cartProductQtyCV.setText(cart.getProductQuantity());
                cartViewHolder.cartProductSellerNameCV.setText(cart.getProductSeller());
                cartViewHolder.cartProductDateCV.setText(cart.getProductDate());
                cartViewHolder.cartProductTypeCV.setText(cart.getProductType());
                cartViewHolder.cartProductTotalPriceCV.setText("Total : " + String.valueOf(itemTotalPrice + shippingCharges));
                cartViewHolder.cartProductStatus.setText("In Cart");

                Picasso.get().load(cart.getProductImg()).placeholder(R.drawable.cart_logo).into(cartViewHolder.cartProductImageCV);

                cartViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[] = new CharSequence[]{
                                "Edit",
                                "Remove"
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Cart Options : ");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {

                                if (i == 0) {
                                    Intent productIntent = new Intent(getActivity().getApplicationContext(), ProductsDetail.class);
                                    productIntent.putExtra("pID", cart.getProductID());
                                    startActivity(productIntent);
                                } else if (i == 1) {
                                    cartFragmentProductRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                                        @Override
                                        public void onRefresh() {
                                            cartAdapter.refresh();
                                        }
                                    });
                                    cartRef.child(cart.getProductID())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(getActivity().getApplicationContext(), "Item Removed from Cart", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }

                            }
                        });
                        builder.show();

                    }
                });

            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_card_view, parent, false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }

            @Override
            protected void onLoadingStateChanged(@NonNull LoadingState state) {
                switch (state) {
                    case LOADING_INITIAL:
                    case LOADING_MORE:
                        // Do your loading animation
                        cartFragmentProductRefreshLayout.setRefreshing(true);
                        break;

                    case LOADED:
                        // Stop Animation
                        cartFragmentProductRefreshLayout.setRefreshing(false);
                        break;

                    case FINISHED:
                        //Reached end of Data set
                        cartFragmentProductRefreshLayout.setRefreshing(false);
                        break;

                    case ERROR:
                        retry();
                        break;
                }
            }

            @Override
            protected void onError(@NonNull DatabaseError databaseError) {
                super.onError(databaseError);
                cartFragmentProductRefreshLayout.setRefreshing(false);
                databaseError.toException().printStackTrace();
            }
        };


        cartProductRecyclerView.setAdapter(cartAdapter);
        cartAdapter.startListening();

    }
}