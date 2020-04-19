package ie.logic;

public class Order {
    private Food food;
    private int numOfFoods;
    private long orderPrice;
    private int id;
    private int cartId;
    private long foodId;

    public Order(){}

    public Order(Food food_, int numOfFoods_){
        food = food_;
        numOfFoods = numOfFoods_;
        this.orderPrice = food_.getPrice();
        this.id =0;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public void setFoodId(long foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() {
        return food.getName();
    }

    public int getNumOfFoods() {
        return numOfFoods;
    }

    public long getOrderPrice() {
        return orderPrice * numOfFoods;
    }

    public void setNumOfFoods(int numOfFoods) {
        this.numOfFoods = numOfFoods;
    }

    public void addNumOfFoods(int numOfFoods){
        this.numOfFoods += numOfFoods;
    }

    public Food getFood() {
        return food;
    }

    public Loghme.Status finalizeOrder() {
        return food.decreaseCount(numOfFoods);
    }

    public boolean isValid() {
        return food.isAvailable(numOfFoods);
    }

    public void decreaseNumOfFoods() { numOfFoods -= 1; }
}
