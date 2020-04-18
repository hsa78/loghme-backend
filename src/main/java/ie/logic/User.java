package ie.logic;

import ie.logic.Cart;
import ie.logic.Delivery;
import ie.repository.DataManager;
import ie.repository.managers.UserManager;

import java.util.ArrayList;
import java.util.HashMap;

public class User {
    ArrayList <Cart> cartsHistory = new ArrayList<Cart>();
    private Cart currentCart;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNum;
    private String password;
    private long credit;
    private HashMap<String, Integer> location = new HashMap<String, Integer>();


    public User(){
        currentCart = new Cart(cartsHistory.size() + 1);
    }

    public User(String firstName_, String lastName_, String email_, String phoneNum){
        currentCart = new Cart(cartsHistory.size() + 1);
        firstName = firstName_;
        lastName = lastName_;
        email = email_;
        this.phoneNum = phoneNum;
        credit = 0;
        location.put("x", 0);
        location.put("y", 0);
    }

    public Loghme.Status addToCart(Resturant resturant, Food order, int count){
        return currentCart.addOrder(order,count);
    }

    public Loghme.Status finalizeOrder(){
        Cart currentCart = DataManager.getInstance().getUserCurrentCart("");
        long totalPrice = currentCart.getTotalPrice();
        if(credit < totalPrice) {
            System.out.println("Your credit is not enough");
            return Loghme.Status.BAD_REQUEST;
        }
        Loghme.Status result = currentCart.finalizeOrder();
        if(result.equals(Loghme.Status.OK)){
            DataManager.getInstance().addCartToHistory(currentCart, this);
            credit -= totalPrice;
            DataManager.getInstance().addNewCart(new Cart(cartsHistory.size() + 1), this);
            return Loghme.Status.OK;
        }
        else
            return result;
    }

    public HashMap<String, Integer> getLocation() {
        return location;
    }

    public void assignDeliveryToCart(Delivery delivery, Cart cart){
        cart.setAssignedDelivery(delivery);
    }

    public Cart getCart() {
        return currentCart;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public long getCredit() {
        return credit;
    }

    public ArrayList<Cart> getCartsHistory() {
        return cartsHistory;
    }

    public String getEmail() {
        return email;
    }

    public Loghme.Status increaseCredit(long plusCredit) {
        if(credit + plusCredit < credit)
            return Loghme.Status.INTERNAL_ERROR;

        UserManager.getInstance().updateCredit(plusCredit, email);
        return Loghme.Status.OK;
    }

    public void setCartsHistory(ArrayList<Cart> cartsHistory) {
        this.cartsHistory = cartsHistory;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public void setCredit(long credit) {
        this.credit = credit;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLocation(HashMap<String, Integer> location) {
        this.location = location;
    }

    public Cart findCartById(int id){
        for(Cart cart: cartsHistory)
            if(cart.getId() == id)
                return cart;
        return null;
    }

    public void setCurrentCart(Cart cart) {
        currentCart = cart;
    }
}
