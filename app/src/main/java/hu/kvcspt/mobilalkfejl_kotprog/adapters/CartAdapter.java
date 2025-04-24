package hu.kvcspt.mobilalkfejl_kotprog.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import hu.kvcspt.mobilalkfejl_kotprog.R;
import hu.kvcspt.mobilalkfejl_kotprog.models.CartItem;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItem> cartItems;
    private TextView totalPriceText;

    public CartAdapter(List<CartItem> cartItems, TextView totalPriceText) {
        this.cartItems = cartItems;
        this.totalPriceText = totalPriceText;
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_item, parent, false);  // Layout for each cart item
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);
        holder.foodName.setText(cartItem.getFoodItem().getName());
        holder.foodPrice.setText("$" + cartItem.getFoodItem().getPrice());
        holder.quantity.setText("Quantity: " + cartItem.getQuantity());

        holder.removeButton.setOnClickListener(v -> {
            cartItems.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, cartItems.size());

            double total = 0.0;
            for (CartItem item : cartItems) {
                total += item.getQuantity() * item.getFoodItem().getPrice();
            }
            totalPriceText.setText(String.format("Total: $%.2f", total));
        });

    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {

        public TextView foodName;
        public TextView foodPrice;
        public TextView quantity;
        public Button removeButton;

        public CartViewHolder(View itemView) {
            super(itemView);

            foodName = itemView.findViewById(R.id.foodName);
            foodPrice = itemView.findViewById(R.id.foodPrice);
            quantity = itemView.findViewById(R.id.quantity);
            removeButton = itemView.findViewById(R.id.removeButton);
        }
    }
}
