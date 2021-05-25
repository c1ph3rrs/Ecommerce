package com.ciphers.ecommerce.Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ciphers.ecommerce.Model.Cart;
import com.ciphers.ecommerce.Prevalent.Prevalent;
import com.ciphers.ecommerce.R;
import com.ciphers.ecommerce.Sellers.SellerOrderDetailActivity;
import com.ciphers.ecommerce.Services.NotificationSender;
import com.ciphers.ecommerce.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class CartActivity extends AppCompatActivity {

    RecyclerView cartPlaceOrderProductRecyclerView;
    DatabaseReference cartRef;
    String currentOnlineUser;

    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Cart, CartViewHolder> cartAdapter;
    int itemTotalPrice =0, totalPrice = 0, currentItem =0;
    ImageView cartNextPageIV;

    String senderName, senderCity, senderPhone, receiverName, receiverPhone, receiverCity, shippingAddress, totalCartItems, paymentType;
    DatabaseReference placeOrderRef, activeOrderRef, shippingRef, trackingRef;
    String orderID, customShippingKey, customSellerNotificationKey;
    TextView afterOrderPlacedTxt;
    Button afterOrderPlacedTxtBtn;
    DatabaseReference buyerRef;
    String dbToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        senderName = getIntent().getStringExtra("senderName");
        senderCity = getIntent().getStringExtra("senderCity");
        senderPhone = getIntent().getStringExtra("senderPhone");
        receiverName = getIntent().getStringExtra("receiverName");
        receiverCity = getIntent().getStringExtra("receiverCity");
        receiverPhone = getIntent().getStringExtra("receiverPhone");
        shippingAddress = getIntent().getStringExtra("shippingAddress");
        totalCartItems = getIntent().getStringExtra("totalCartItems");
        paymentType = getIntent().getStringExtra("paymentType");

        placeOrderRef = FirebaseDatabase.getInstance().getReference().child("PlacedOrder");
        activeOrderRef = FirebaseDatabase.getInstance().getReference().child("ActiveOrders");
        shippingRef = FirebaseDatabase.getInstance().getReference().child("Shipping");
        trackingRef = FirebaseDatabase.getInstance().getReference().child("Tracker");
        cartRef = FirebaseDatabase.getInstance().getReference().child("Cart List").child(Prevalent.currentOnlineUser.username);

       cartPlaceOrderProductRecyclerView = findViewById(R.id.buyer_cart_place_order_recycler);
       afterOrderPlacedTxt = findViewById(R.id.after_order_placed_txt);
       afterOrderPlacedTxtBtn = findViewById(R.id.after_order_placed_button);

//        Toast.makeText(getApplicationContext(), totalCartItems.toString(), Toast.LENGTH_SHORT).show();

        orderShippingDetail();
        startCartProductRecycler();


        afterOrderPlacedTxtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.activity_orders_placed_cart, new DashboardFragment()).commit();
//                finish();

                Intent orderDoneIntent = new Intent(getApplicationContext(), HomeActivity.class);
                orderDoneIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(orderDoneIntent);
                finish();
            }
        });

    }

    private void startCartProductRecycler() {


        cartPlaceOrderProductRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        cartPlaceOrderProductRecyclerView.setLayoutManager(layoutManager);

        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>().setQuery(cartRef, Cart.class).build();


        cartAdapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, int i, @NonNull Cart cart) {

                orderID = placeOrderRef.push().getKey();
                String customID = "CCODI" + orderID;

                currentItem = i;

                itemTotalPrice = Integer.parseInt(cart.getProductPrice()) * Integer.parseInt(cart.getProductQuantity());
                totalPrice += itemTotalPrice;

                cartViewHolder.cartProductNameCV.setText(cart.getProductName());
                cartViewHolder.cartProductPriceCV.setText(cart.getProductPrice());
                cartViewHolder.cartProductQtyCV.setText(cart.getProductQuantity());
                cartViewHolder.cartProductSellerNameCV.setText(cart.getProductSeller());
                cartViewHolder.cartProductDateCV.setText(cart.getProductDate());
                cartViewHolder.cartProductTypeCV.setText(cart.getProductType());
                cartViewHolder.cartProductTotalPriceCV.setText("Total : " + String.valueOf(itemTotalPrice));

                Picasso.get().load(cart.getProductImg()).placeholder(R.drawable.cart_logo).into(cartViewHolder.cartProductImageCV);

                HashMap<String, Object> placeOrderMap = new HashMap<>();
                placeOrderMap.put("productName", cart.getProductName());
                placeOrderMap.put("productPrice", cart.getProductPrice());
                placeOrderMap.put("productQty", cart.getProductQuantity());
                placeOrderMap.put("productSellerName", cart.getProductSeller());
                placeOrderMap.put("productDate", cart.getProductDate());
                placeOrderMap.put("productType", cart.getProductType());
                placeOrderMap.put("productTotalPrice", String.valueOf(itemTotalPrice));
                placeOrderMap.put("productImg", cart.getProductImg());
                placeOrderMap.put("productOrderID", customID);
                placeOrderMap.put("productOrderPayment", paymentType);
                placeOrderMap.put("orderShippingID", customShippingKey);

                placeOrderRef.child(cart.getProductSeller()).child(customID).updateChildren(placeOrderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        addBuyerOrderProgress(cart.getProductID(), cart.getProductName(), cart.getProductPrice(), cart.getProductQuantity(),
                                cart.getProductSeller(), cart.getProductDate(), cart.getProductType(), String.valueOf(itemTotalPrice),
                                cart.getProductImg(), customID
                        );

                        addSellerNotification(cart.getProductBuyer(), cart.getProductName(), cart.getProductImg(),
                                customID,  cart.getProductSeller());

                        if(totalCartItems.equals(String.valueOf(i +1))){
                            cartPlaceOrderProductRecyclerView.setVisibility(View.GONE);
                            afterOrderPlacedTxt.setVisibility(View.VISIBLE);
                            afterOrderPlacedTxtBtn.setVisibility(View.VISIBLE);

//                            afterOrderPlacedTxt.setGravity(Gravity.CENTER_VERTICAL);



                        }
//                        Toast.makeText(getApplicationContext(), "Length is" + totalCartItems , Toast.LENGTH_SHORT).show();
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
        };

        cartPlaceOrderProductRecyclerView.setAdapter(cartAdapter);
        cartAdapter.startListening();
    }

    private void addSellerNotification(String productBuyer, String productName, String productImg, String shippingKey, String productSeller) {

        String notificationTitle = "Hi ";
        String notificationBody = "just placed you an order";

        String saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        DatabaseReference notificationRef = FirebaseDatabase.getInstance().getReference().child("SellersNotifications");

        String notificationKey = notificationRef.push().getKey();
        customSellerNotificationKey = "ONK" + notificationKey;

        Log.d("notification Key ", "Notification Key" + customSellerNotificationKey);

        HashMap<String, Object> notificationMap = new HashMap<>();
        notificationMap.put("notificationSellerKey", customSellerNotificationKey);
        notificationMap.put("notificationSellerOrderKey", shippingKey);
        notificationMap.put("notificationSellerBuyer", productBuyer);
        notificationMap.put("notificationSeller", productSeller);
        notificationMap.put("notificationSellerTitle", productBuyer + " just placed you an order of " + productName);
        notificationMap.put("notificationSellerImg", productImg);
        notificationMap.put("notificationSellerDate", saveCurrentDate);
        notificationMap.put("notificationSellerType", "Order Placed");
        notificationMap.put("notificationSellerStatus", "0");

        notificationRef.child(productSeller).child(customSellerNotificationKey).updateChildren(notificationMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                buyerRef = FirebaseDatabase.getInstance().getReference().child("SellersTokens");
                buyerRef.child(productSeller).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        dbToken = snapshot.child("sellerToken").getValue(String.class);
                        Log.d("Buyer Token", "Buyer Token is " + dbToken);

                        NotificationSender notificationSender = new NotificationSender(dbToken, notificationTitle + " " + productSeller, Prevalent.currentOnlineUser.getUsername() + " "+ notificationBody,
                                getApplicationContext(), CartActivity.this);
                        notificationSender.SendNotifications();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


    }

    private void orderShippingDetail() {

        String shippingKey = shippingRef.push().getKey();
        customShippingKey = "CCSI" + shippingKey;

        final HashMap<String, Object> shippingMap = new HashMap<>();
        shippingMap.put("shippingID", customShippingKey);
        shippingMap.put("senderName", senderName);
        shippingMap.put("senderCity", senderCity);
        shippingMap.put("senderPhone", senderPhone);
        shippingMap.put("receiverName", receiverName);
        shippingMap.put("receiverCity", receiverCity);
        shippingMap.put("receiverPhone", receiverPhone);
        shippingMap.put("shippingAddress", shippingAddress);
        shippingMap.put("buyerName", Prevalent.currentOnlineUser.getUsername());

        shippingRef.child(customShippingKey).updateChildren(shippingMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }

    private void addBuyerOrderProgress(String productId, String productName, String productPrice, String productQuantity, String productSeller, String productDate, String productType, String valueOf, String productImg, String customID) {

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("productID", productId);
        cartMap.put("productName", productName);
        cartMap.put("productImg", productImg);
        cartMap.put("productPrice", productPrice);
        cartMap.put("productSeller", productSeller);
        cartMap.put("productBuyer", Prevalent.currentOnlineUser.getUsername().toString());
        cartMap.put("productType", productType);
        cartMap.put("productDate", productDate);
        cartMap.put("productQuantity", productQuantity);
        cartMap.put("productOrderID", customID);
        cartMap.put("productOrderPayment", paymentType);
        cartMap.put("productShippingID", customShippingKey);

        activeOrderRef.child(Prevalent.currentOnlineUser.username).child(customID).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    cartRef.child(productId)
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        orderTrackingID(customID, customShippingKey);
                                    }
                                }
                            });
                }
            }
        });
    }

    private void orderTrackingID(String customID, String customShippingKey) {

        HashMap<String, Object> trackingMap = new HashMap<>();
        trackingMap.put("orderID", customID);
        trackingMap.put("shippingID", customShippingKey);
        trackingMap.put("lat", "0");
        trackingMap.put("lng", "0");
        trackingRef.child(customID).updateChildren(trackingMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
//                Toast.makeText(getApplicationContext(), "Product Tracking Id Saved", Toast.LENGTH_SHORT).show();
            }
        });
    }


//    public void cartRecycler() {
//
//        recyclerView.setHasFixedSize(true);
//        layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//
//        checkOrderState();
//
//        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
//
//        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>().setQuery(cartListRef.child("User View")
//                .child(Prevalent.currentOnlineUser.getUsername()).child("Products"), Cart.class).build();
//
//        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
//            @Override
//            protected void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, int i, @NonNull final Cart cart) {
////                cartViewHolder.txtProductQuantity.setText("Quantity : " + cart.getProductQuantity());
////                cartViewHolder.txtProductName.setText("Product : " + cart.getProductName());
////                cartViewHolder.txtProductPrice.setText("Price per item : " + cart.getProductPrice() + " $");
//
//
//                int priceOfOneProduct = ((Integer.valueOf(cart.getProductPrice()))) * Integer.valueOf(cart.getProductQuantity());
//
//                overTotalPrice = overTotalPrice + priceOfOneProduct;
//
//                cartViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        CharSequence options[] = new CharSequence[]{
//                                "Edit",
//                                "Remove"
//                        };
//
//                        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
//                        builder.setTitle("Cart Options : ");
//                        builder.setItems(options, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int i) {
//
//                                if (i == 0) {
//                                    Intent productIntent = new Intent(getApplicationContext(), ProductsDetail.class);
//                                    productIntent.putExtra("pID", cart.getProductID());
//                                    startActivity(productIntent);
//                                } else if (i == 1) {
//                                    cartListRef.child("User View")
//                                            .child(Prevalent.currentOnlineUser.getUsername())
//                                            .child("Products")
//                                            .child(cart.getProductID())
//                                            .removeValue()
//                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                @Override
//                                                public void onComplete(@NonNull Task<Void> task) {
//                                                    if (task.isSuccessful()) {
//                                                        Toast.makeText(getApplicationContext(), "Item Removed from Cart", Toast.LENGTH_SHORT).show();
//                                                        CartActivity.super.onBackPressed();
//                                                    }
//                                                }
//                                            });
//                                }
//
//                            }
//                        });
//                        builder.show();
//                    }
//                });
//
//            }
//
//            @NonNull
//            @Override
//            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_card_view, parent, false);
//                CartViewHolder holder = new CartViewHolder(view);
//                return holder;
//            }
//        };
//
//        recyclerView.setAdapter(adapter);
//        adapter.startListening();
//    }

//    public void checkOrderState() {
//
//        DatabaseReference ordersRef;
//        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getUsername());
//
//        ordersRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    String shippingState = dataSnapshot.child("state").getValue().toString();
//                    String name = dataSnapshot.child("senderName").getValue().toString();
//
//                    if (shippingState.equals("Not Shipped")) {
//                        totalPriceNumberTxt.setText("Your order is not Shipped Please wait...");
//                        totalPriceTxt.setVisibility(View.GONE);
//                        recyclerView.setVisibility(View.GONE);
//                        cartListEmptyMessage.setText("You can purchase more Products once your order is shipped");
//                        cartListEmptyMessage.setVisibility(View.VISIBLE);
//                        cartNextBtn.setVisibility(View.GONE);
//
//                    } else if (shippingState.equals("Shipped")) {
//                        totalPriceNumberTxt.setText("Dear" + name + "\n Your order has been shipped Sucessfully. Thanks to use our Service");
//                        recyclerView.setVisibility(View.GONE);
//
//                        cartListEmptyMessage.setVisibility(View.VISIBLE);
//                        cartNextBtn.setVisibility(View.GONE);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }

    @Override
    public void onBackPressed() {
       CartActivity.super.onBackPressed();
    }
}