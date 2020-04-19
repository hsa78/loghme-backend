package ie.logic;

import ie.repository.DAO.FoodDAO;
import ie.repository.managers.FoodManager;
import ie.repository.managers.OrderManager;

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
        return getFood().getName();
    }

    public int getNumOfFoods() {
        return numOfFoods;
    }

    public long getOrderPrice() {
        return getFood().getPrice() * numOfFoods;
    }

    public void setNumOfFoods(int numOfFoods) {
        this.numOfFoods = numOfFoods;
    }

    public void addNumOfFoods(int plusCount){
        OrderManager.getInstance().updateCount(id, numOfFoods + plusCount);
    }

    public Food getFood() {
        FoodDAO foodDAO = FoodManager.getInstance().retrieveFood(foodId);
        return Loghme.getInstance().convertDAOToFood(foodDAO);
    }

    public Loghme.Status finalizeOrder() {
        FoodDAO foodDAO = FoodManager.getInstance().retrieveFood(foodId);
        Food food = Loghme.getInstance().convertDAOToFood(foodDAO);
        return food.decreaseCount(numOfFoods);
    }

    public long getFoodId() {
        return foodId;
    }

    public boolean isValid() {
        return getFood().isAvailable(numOfFoods);
    }

    public void decreaseNumOfFoods() {
        OrderManager.getInstance().updateCount(id, numOfFoods - 1);
    }
}
