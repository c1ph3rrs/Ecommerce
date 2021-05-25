package com.ciphers.ecommerce.Delivery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ciphers.ecommerce.Buyer.ProductsDetail;
import com.ciphers.ecommerce.Model.Products;
import com.ciphers.ecommerce.Model.SellersOrders;
import com.ciphers.ecommerce.Model.deliveryGuyShip;
import com.ciphers.ecommerce.Prevalent.Prevalent;
import com.ciphers.ecommerce.R;
import com.ciphers.ecommerce.SplashScreen;
import com.ciphers.ecommerce.ViewHolder.ProductViewHolder;
import com.ciphers.ecommerce.ViewHolder.SellersOrdersViewHolder;
import com.ciphers.ecommerce.ViewHolder.deliveryGuyShipViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shreyaspatil.firebase.recyclerpagination.FirebaseRecyclerPagingAdapter;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;

public class DeliveryGuyHomeActivity extends AppCompatActivity {

    Button deliveryGuyLogoutBtn;

    TextView deliveryGuyUsernameTxt;
    RecyclerView deliveryGuyOrderRecycler;

    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<deliveryGuyShip, deliveryGuyShipViewHolder> deliveryGuyAdapter;
    DatabaseReference deliveryGuyShipRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_guy_home);

        deliveryGuyShipRef = FirebaseDatabase.getInstance().getReference().child("deliveryGuyShip");

        deliveryGuyLogoutBtn = findViewById(R.id.delivery_guy_logout);
        deliveryGuyUsernameTxt = findViewById(R.id.delivery_guy_username_txt);

        deliveryGuyOrderRecycler = findViewById(R.id.delivery_guy_recycler);

        startDeliveryGuyAdapter();

        deliveryGuyUsernameTxt.setText(Prevalent.currentOnlineDeliveryGuy.getDeliveryGuyUsername());

        deliveryGuyLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence options[] = new CharSequence[]{
                        "Yes",
                        "No"
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(DeliveryGuyHomeActivity.this);
                builder.setTitle("Are you sure you want to Logout.?");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            Paper.book().destroy();
                            Intent logoutIntent = new Intent(DeliveryGuyHomeActivity.this, SplashScreen.class);
                            logoutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(logoutIntent);
                        } else {
                        }
                    }
                });
                builder.show();
            }
        });

    }

    private void startDeliveryGuyAdapter(){

        deliveryGuyOrderRecycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        deliveryGuyOrderRecycler.setLayoutManager(layoutManager);

        FirebaseRecyclerOptions<deliveryGuyShip> options =
                new FirebaseRecyclerOptions.Builder<deliveryGuyShip>()
                        .setQuery(deliveryGuyShipRef.child(Prevalent.currentOnlineDeliveryGuy.getDeliveryGuyUsername()),
                                deliveryGuyShip.class)
                        .build();

        deliveryGuyAdapter = new FirebaseRecyclerAdapter<deliveryGuyShip, deliveryGuyShipViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull deliveryGuyShipViewHolder deliveryGuyShipViewHolderr, int i, @NonNull deliveryGuyShip deliveryGuyShipp) {
                deliveryGuyShipViewHolderr.deliveryGuyShippingTxt.setText(deliveryGuyShipp.getShipping());
                deliveryGuyShipViewHolderr.deliveryGuyTrackingTxt.setText(deliveryGuyShipp.getTracking());

                deliveryGuyShipViewHolderr.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent deliveryIntent = new Intent(getApplicationContext(), DeliveryGuyOrderDetail.class);
                        deliveryIntent.putExtra("shippingID", deliveryGuyShipp.getShipping());
                        deliveryIntent.putExtra("trackingID", deliveryGuyShipp.getTracking());
                        deliveryIntent.putExtra("from", deliveryGuyShipp.getFrom());
                        deliveryIntent.putExtra("to", deliveryGuyShipp.getTo());
                        deliveryIntent.putExtra("id", deliveryGuyShipp.getId());
                        deliveryIntent.putExtra("assignDate", deliveryGuyShipp.getAssignDate());
                        deliveryIntent.putExtra("date", deliveryGuyShipp.getDate());
                        startActivity(deliveryIntent);
                    }
                });
            }

            @NonNull
            @Override
            public deliveryGuyShipViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new deliveryGuyShipViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.delivery_guy_shipping_layout, parent, false));
            }
        };

        deliveryGuyOrderRecycler.setAdapter(deliveryGuyAdapter);
        deliveryGuyAdapter.startListening();

    }
}