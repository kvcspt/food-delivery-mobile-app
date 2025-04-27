package hu.kvcspt.mobilalkfejl_kotprog.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import hu.kvcspt.mobilalkfejl_kotprog.R;
import hu.kvcspt.mobilalkfejl_kotprog.models.CartManager;
import hu.kvcspt.mobilalkfejl_kotprog.models.FoodItem;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
    private final List<FoodItem> foodItems;

    public FoodAdapter(List<FoodItem> foodItems) {
        this.foodItems = foodItems;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodAdapter.FoodViewHolder holder, int position) {
        FoodItem foodItem = foodItems.get(position);
        holder.foodName.setText(foodItem.getName());
        holder.foodDescription.setText(foodItem.getDescription());
        holder.foodPrice.setText("$" + String.valueOf(foodItem.getPrice()));

        holder.addToCartButton.setOnClickListener(v -> {
            CartManager.getInstance().addItem(foodItem);

            v.animate()
                    .scaleX(1.2f)
                    .scaleY(1.2f)
                    .setDuration(100)
                    .withEndAction(() -> v.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(100)
                            .start()
                    )
                    .start();

            Toast.makeText(holder.itemView.getContext(), foodItem.getName() + " added to cart!", Toast.LENGTH_SHORT).show();
        });

        Glide.with(holder.foodImage.getContext()).load(foodItem.getImageUrl()).into(holder.foodImage);
    }

    @Override
    public int getItemCount() {
        return foodItems.size();
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        TextView foodName, foodDescription, foodPrice;
        ImageView foodImage;
        Button addToCartButton;

        public FoodViewHolder(View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.foodName);
            foodDescription = itemView.findViewById(R.id.foodDescription);
            foodPrice = itemView.findViewById(R.id.foodPrice);
            foodImage = itemView.findViewById(R.id.foodImage);
            addToCartButton = itemView.findViewById(R.id.addToCartButton);
        }
    }
}
