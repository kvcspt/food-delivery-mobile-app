package hu.kvcspt.mobilalkfejl_kotprog.models;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<CartItem> cartItems = new ArrayList<>();

    public void addToCart(FoodItem item) {
        for (CartItem cartItem : cartItems) {
            if (cartItem.getFoodItem().getId().equals(item.getId())) {
                cartItem.setQuantity(cartItem.getQuantity() + 1);
                return;
            }
        }
        cartItems.add(new CartItem(item, 1));
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }
}
