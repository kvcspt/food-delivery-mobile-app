package hu.kvcspt.mobilalkfejl_kotprog.models;

public class CartManager {
    private static CartManager instance;
    private Cart cart;

    private CartManager() {
        cart = new Cart();
    }

    public static CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public Cart getCart() {
        return cart;
    }

    public void addItem(FoodItem item) {
        cart.addToCart(item);
    }

    public void clearCart(){
        cart.getCartItems().clear();
    }
}

