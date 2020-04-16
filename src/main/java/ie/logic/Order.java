package ie.logic;

public class Order {
    private Food food;
    private int numOfFoods;
    private long orderPrice;
    private int id;

    public Order(Food food_, int numOfFoods_){
        food = food_;
        numOfFoods = numOfFoods_;
        this.orderPrice = food_.getPrice();
        this.id =0;
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

    public void setOrderPrice(long orderPrice) {
        this.orderPrice = orderPrice;
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
