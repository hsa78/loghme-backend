package ie.repository.DAO;

public class OrderDAO {
    private int id;
    private int cartId;
    private int count;
    private long foodId;

    public OrderDAO(){

    }

    public OrderDAO(int cartId, int count, long foodId) {
        this.cartId = cartId;
        this.count = count;
        this.foodId = foodId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getFoodId() {
        return foodId;
    }

    public void setFoodId(long foodId) {
        this.foodId = foodId;
    }
}
