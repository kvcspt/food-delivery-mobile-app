package hu.kvcspt.mobilalkfejl_kotprog;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.pm.PackageManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import hu.kvcspt.mobilalkfejl_kotprog.adapters.CartAdapter;
import hu.kvcspt.mobilalkfejl_kotprog.interfaces.FirestoreUserCallback;
import hu.kvcspt.mobilalkfejl_kotprog.models.Cart;
import hu.kvcspt.mobilalkfejl_kotprog.models.CartManager;
import hu.kvcspt.mobilalkfejl_kotprog.models.Order;
import hu.kvcspt.mobilalkfejl_kotprog.models.User;

public class CartActivity extends AppCompatActivity {
    private static final String TAG = CartActivity.class.getName();
    private RecyclerView cartRecyclerView;
    private CartAdapter cartAdapter;
    private Button placeOrderButton;
    private Cart cart;
    private FirebaseUser firebaseUser;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        placeOrderButton = findViewById(R.id.placeOrderButton);

        db = FirebaseFirestore.getInstance();
        cart = CartManager.getInstance().getCart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();

        TextView totalPriceText = findViewById(R.id.totalPriceText);
        setTotalPrice(totalPriceText);

        cartAdapter = new CartAdapter(cart.getCartItems(), totalPriceText);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartRecyclerView.setAdapter(cartAdapter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
        }

        placeOrderButton.setOnClickListener(v -> {
            if (cart.getCartItems().isEmpty()) {
                Toast.makeText(this, "Cart is empty!", Toast.LENGTH_SHORT).show();
                return;
            }

            convertFirebaseUserToUser(firebaseUser, user -> {
                Order order;
                if(user != null){
                    order = new Order(cart, user, cart.getTotalPrice());
                } else {
                    order = new Order(cart, null, cart.getTotalPrice());
                }

                db
                        .collection("Orders")
                        .add(order)
                        .addOnSuccessListener(doc -> Toast.makeText(this, "Order placed!", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(this, "Failed to place order.", Toast.LENGTH_SHORT).show());

                showOrderNotification();
                CartManager.getInstance().clearCart();
                setTotalPrice(totalPriceText);
                cartAdapter.notifyDataSetChanged();
            });
        });

        generateNavBar();
    }

    private void setTotalPrice(TextView totalPriceText) {
        totalPriceText.setText(String.format("Total: $%.2f", cart.getTotalPrice()));
    }

    private void generateNavBar() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setSelectedItemId(R.id.nav_cart);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                startActivity(new Intent(CartActivity.this, FoodListActivity.class));
                return true;
            } else if (id == R.id.nav_profile) {
                // startActivity(new Intent(FoodListActivity.this, ProfileActivity.class));
                return true;
            } else if (id == R.id.nav_cart) {
                return true;
            } else if (id == R.id.nav_logout) {
                mAuth.signOut();
                startActivity(new Intent(CartActivity.this, MainActivity.class));
                finish();
                return true;
            }

            return false;
        });
    }

    public void convertFirebaseUserToUser(FirebaseUser firebaseUser, FirestoreUserCallback callback) {
        if (firebaseUser != null) {
            String uid = firebaseUser.getUid();
            DocumentReference docRef = db.collection("Users").document(uid);

            docRef.get().addOnSuccessListener(documentSnapshot -> {
                User user = documentSnapshot.toObject(User.class);
                callback.onUserFetched(user);
            }).addOnFailureListener(e -> {
                callback.onUserFetched(null);
            });
        } else {
            callback.onUserFetched(null);
        }
    }

    private void showOrderNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "order_channel_id";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Order Notifications",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Order Confirmed")
                .setContentText("Your food will arrive in approx. 30-40 minutes.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        notificationManager.notify(1, builder.build());
        Log.d(TAG, "Notification should now appear");
    }

}

