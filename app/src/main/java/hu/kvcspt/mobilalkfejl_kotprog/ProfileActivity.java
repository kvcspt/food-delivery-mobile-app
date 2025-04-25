package hu.kvcspt.mobilalkfejl_kotprog;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hu.kvcspt.mobilalkfejl_kotprog.adapters.OrderAdapter;
import hu.kvcspt.mobilalkfejl_kotprog.models.Order;

public class ProfileActivity extends AppCompatActivity {

    private EditText nameEditText, addressEditText, phoneEditText;
    private Button updateButton;

    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        nameEditText = findViewById(R.id.profileName);
        addressEditText = findViewById(R.id.profileAddress);
        phoneEditText = findViewById(R.id.profilePhone);
        updateButton = findViewById(R.id.updateProfileButton);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        if (firebaseUser != null) {
            loadUserData(firebaseUser.getUid());
        }

        updateButton.setOnClickListener(v -> updateUserData());

        generateNavBar();

        RecyclerView ordersRecyclerView = findViewById(R.id.ordersRecyclerView);
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Order> orders = new ArrayList<>();
        OrderAdapter orderAdapter = new OrderAdapter(orders);
        ordersRecyclerView.setAdapter(orderAdapter);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentEmail = firebaseUser.getEmail();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Orders")
                .whereEqualTo("user", currentEmail)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Order order = doc.toObject(Order.class);
                        orders.add(order);
                    }
                    orderAdapter.notifyDataSetChanged();
                });

    }

    private void loadUserData(String uid) {
        DocumentReference docRef = db.collection("Users").document(uid);
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                nameEditText.setText(documentSnapshot.getString("name"));
                addressEditText.setText(documentSnapshot.getString("address"));
                phoneEditText.setText(documentSnapshot.getString("phoneNumber"));
            }
        });
    }

    private void updateUserData() {
        String name = nameEditText.getText().toString();
        String address = addressEditText.getText().toString();
        String phone = phoneEditText.getText().toString();

        if (firebaseUser != null) {
            DocumentReference docRef = db.collection("Users").document(firebaseUser.getUid());

            Map<String, Object> updatedData = new HashMap<>();
            updatedData.put("name", name);
            updatedData.put("address", address);
            updatedData.put("phoneNumber", phone);

            docRef.update(updatedData).addOnSuccessListener(unused -> Toast.makeText(this, "Profile updated!", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> {
                Toast.makeText(this, "Failed to update profile.", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void generateNavBar() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setSelectedItemId(R.id.nav_profile);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                startActivity(new Intent(ProfileActivity.this, FoodListActivity.class));
                return true;
            } else if (id == R.id.nav_profile) {
                return true;
            } else if (id == R.id.nav_cart) {
                startActivity(new Intent(ProfileActivity.this, CartActivity.class));
                return true;
            } else if (id == R.id.nav_logout) {
                mAuth.signOut();
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                finish();
                return true;
            }

            return false;
        });
    }
}
