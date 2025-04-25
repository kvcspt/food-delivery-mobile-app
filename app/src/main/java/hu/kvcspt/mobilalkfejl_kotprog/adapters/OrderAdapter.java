package hu.kvcspt.mobilalkfejl_kotprog.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hu.kvcspt.mobilalkfejl_kotprog.R;
import hu.kvcspt.mobilalkfejl_kotprog.models.CartItem;
import hu.kvcspt.mobilalkfejl_kotprog.models.Order;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private List<Order> orders;

    public OrderAdapter(List<Order> orders) {
        this.orders = orders;
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderInfo;

        public OrderViewHolder(View itemView) {
            super(itemView);
            orderInfo = itemView.findViewById(R.id.orderInfo);
        }
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);
        StringBuilder orderDetails = new StringBuilder();
        orderDetails.append("Ordered at: ").append(order.getCreatedAt()).append("\n");
        orderDetails.append("Total: ").append(order.getTotal()).append(" $\n");
        orderDetails.append("Items:\n");

        for (CartItem item : order.getCart().getCartItems()) {
            orderDetails.append("- ")
                    .append(item.getFoodItem().getName())
                    .append(" x ")
                    .append(item.getQuantity())
                    .append("\n");
        }

        holder.orderInfo.setText(orderDetails.toString());
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }
}

