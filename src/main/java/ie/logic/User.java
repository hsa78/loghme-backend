package ie.logic;

import ie.repository.DAO.CartDAO;
import ie.repository.managers.CartManager;
import ie.repository.managers.UserManager;

import java.util.HashMap;

public class User {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNum;
    private String password;
    private long credit;
    private HashMap<String, Integer> location = new HashMap<String, Integer>();

    public User(){ }

    public User(String firstName_, String lastName_, String email_, String phoneNum){
        firstName = firstName_;
        lastName = lastName_;
        email = email_;
        this.phoneNum = phoneNum;
        credit = 0;
        location.put("x", 0);
        location.put("y", 0);
    }

    public Loghme.Status finalizeOrder(){
        Cart currentCart = Loghme.getInstance().getLoginnedUserCart();
        long totalPrice = currentCart.getTotalPrice();
        if(credit < totalPrice) {
            System.out.println("Your credit is not enough");
            return Loghme.Status.BAD_REQUEST;
        }
        Loghme.Status result = currentCart.finalizeOrder();
        if(result.equals(Loghme.Status.OK)){
            UserManager.getInstance().updateCredit(-totalPrice, email);
            CartDAO newCart = new CartDAO(email);
            CartManager.getInstance().save(newCart);
            return Loghme.Status.OK;
        }
        else
            return result;
    }

    public HashMap<String, Integer> getLocation() {
        return location;
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

    public String getEmail() {
        return email;
    }

    public Loghme.Status increaseCredit(long plusCredit) {
        if(credit + plusCredit < credit)
            return Loghme.Status.INTERNAL_ERROR;

        UserManager.getInstance().updateCredit(plusCredit, email);
        return Loghme.Status.OK;
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

}
