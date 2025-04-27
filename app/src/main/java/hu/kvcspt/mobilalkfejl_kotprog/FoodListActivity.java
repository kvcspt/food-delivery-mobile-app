package hu.kvcspt.mobilalkfejl_kotprog;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import hu.kvcspt.mobilalkfejl_kotprog.adapters.FoodAdapter;
import hu.kvcspt.mobilalkfejl_kotprog.models.FoodItem;

public class FoodListActivity extends AppCompatActivity {
    private static final String LOG_TAG = FoodListActivity.class.getName();
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private FoodAdapter foodAdapter;
    private List<FoodItem> foodItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodlist);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference foodRef = db.collection("FoodItems");

        recyclerView = findViewById(R.id.foodRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        foodRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                foodItems.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    FoodItem item = document.toObject(FoodItem.class);
                    foodItems.add(item);
                }
                foodAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getApplicationContext(), "Error getting documents", Toast.LENGTH_SHORT).show();
            }
        });

        foodAdapter = new FoodAdapter(foodItems);
        recyclerView.setAdapter(foodAdapter);

        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        generateNavBar();
    }

    private void generateNavBar() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setSelectedItemId(R.id.nav_home);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(FoodListActivity.this, ProfileActivity.class));
                return true;
            } else if (id == R.id.nav_cart) {
                startActivity(new Intent(FoodListActivity.this, CartActivity.class));
                return true;
            } else if (id == R.id.nav_logout) {
                mAuth.signOut();
                startActivity(new Intent(FoodListActivity.this, MainActivity.class));
                finish();
                return true;
            }

            return false;
        });
    }
}
