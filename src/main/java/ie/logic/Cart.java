package ie.logic;

import ie.repository.DataManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class Cart {
    private String restaurantId;
    private String restaurantName;
    private ArrayList<Order> orders = new ArrayList<Order>();
    private long totalPrice;
    private int id;
    private Delivery assignedDelivery;
    private CartStatus currentStatus;
    private float timeToDelivery = 0;

    public void setStatus(CartStatus cartStatus) {
        currentStatus = cartStatus;
    }

    public enum CartStatus {OnProgress, SearchingForDelivery, DeliveryIsOnTheWay, Delivered};

    private static int NOT_FOUND = -1;

    public Cart(int id_){
        restaurantId = null;
        totalPrice = 0;
        id = id_;
        currentStatus = CartStatus.OnProgress;
    }

    public int fineOrderIndex(String foodName){
        for(int i = 0; i < orders.size(); i++){
            if(orders.get(i).getFoodName().equals(foodName))
                return i;
        }
        return NOT_FOUND;
    }

    //TODO
    public Loghme.Status addOrder(Food food, int count){
        if(restaurantId == null){
            restaurantName = DataManager.getInstance().getRestaurantName(food.getRestaurantId());
            restaurantId = food.getRestaurantId();
        }
        else if(!food.getRestaurantId().equals(restaurantId)){
            System.out.println("You can not order from two different restaurant");
            return Loghme.Status.BAD_REQUEST;
        }
        Order foundOrder = DataManager.getInstance().findOrder(food.getName(), this);
        if (foundOrder == null)
            DataManager.getInstance().addOrder(new Order(food, count), this);
        else{
            if(foundOrder.getFood().isAvailable(foundOrder.getNumOfFoods() + count))
                DataManager.getInstance().changeOrderCount(foundOrder, count);
            else
                return Loghme.Status.BAD_REQUEST;
        }
        totalPrice += (food.getPrice() * count);
        return Loghme.Status.OK;
    }

    public Loghme.Status finalizeOrder(){
        if(orders.size() == 0)
            return Loghme.Status.BAD_REQUEST;

        for(Order order: orders){
            Loghme.Status status = order.finalizeOrder();
            if (status.equals(Loghme.Status.OK))
                return status;
        }
        DataManager.getInstance().changeCartStatus(this, CartStatus.SearchingForDelivery);
        return Loghme.Status.OK;
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public long getTotalPrice() {
        long totalPrice = 0;
        for(Order order: orders){
            totalPrice += order.getOrderPrice();
        }
        return totalPrice;
    }

    public Delivery getAssignedDelivery() {
        return assignedDelivery;
    }

    public void setAssignedDelivery(Delivery assignedDelivery) {
        this.assignedDelivery = assignedDelivery;
        this.timeToDelivery = assignedDelivery.getTimeToDest();
        currentStatus = CartStatus.DeliveryIsOnTheWay;
    }

    public int getId() {
        return id;
    }

    public int getTotalNumOfOrders(){
        int result = 0;
        for(Order order: orders)
            result += order.getNumOfFoods();
        return result;
    }

    public CartStatus getCurrentStatus() {
        return currentStatus;
    }

    public Loghme.Status deleteFromCart(String restaurantId, Food food){
        if(!restaurantId.equals(this.restaurantId)){
            System.out.println("You have ordered from a different restaurant");
            return Loghme.Status.BAD_REQUEST;
        }
        Order foundOrder = DataManager.getInstance().findOrder(food.getName(), this);
        if(foundOrder == null){
            System.out.println("You did not ordered this food");
            return Loghme.Status.BAD_REQUEST;
        }
        DataManager.getInstance().deleteFromCart(foundOrder, this);
        return Loghme.Status.OK;
    }

    public void startDelivering(){
        System.out.println("Should change to delivered after: " + timeToDelivery + " seconds.");
        Timer newTimer = new Timer();
        TimerTask scheduledTask = new ChangeStatusToDeliveredTask(newTimer);
        newTimer.schedule(scheduledTask, (int)timeToDelivery * 1000, (10 * 1000));
    }

    class ChangeStatusToDeliveredTask extends TimerTask {
        private Timer timer;
        public ChangeStatusToDeliveredTask(Timer timer){
            this.timer = timer;
        }
        @Override
        public void run() {
            currentStatus = CartStatus.Delivered;
            timer.cancel();
            timer.purge();
        }
    }

}
