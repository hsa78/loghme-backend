package ie.services.responses;

import ie.logic.Cart;

public class CartInfo {
    private String restaurantName;
    private int id;
    private Cart.CartStatus status;
    private int numOfOrders;

    public CartInfo(Cart cart){
        this.restaurantName = cart.getRestaurantName();
        this.id = cart.getId();
        this.status = cart.getCurrentStatus();
        this.numOfOrders = cart.getTotalNumOfOrders();
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cart.CartStatus getStatus() {
        return status;
    }

    public void setStatus(Cart.CartStatus status) {
        this.status = status;
    }

    public int getNumOfOrders() {
        return numOfOrders;
    }

    public void setNumOfOrders(int numOfOrders) {
        this.numOfOrders = numOfOrders;
    }
}
