package ie.repository.DAO;

public class CartDAO {
    private int id;
    private String userEmail;
    private String restaurantId;
    private String restaurantName;
    private String cartStatus;
    private String deliveryId;

    public CartDAO(String userEmail) {
        this.userEmail = userEmail;
        this.restaurantId = null;
        this.restaurantName = null;
        this.cartStatus = "OnProgress";
        this.deliveryId = null;
    }

    public CartDAO() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getCartStatus() {
        return cartStatus;
    }

    public void setCartStatus(String cartStatus) {
        this.cartStatus = cartStatus;
    }

    public String getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(String deliveryId) {
        this.deliveryId = deliveryId;
    }


}
