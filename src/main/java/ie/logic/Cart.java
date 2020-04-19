package ie.logic;

import ie.repository.DAO.OrderDAO;
import ie.repository.DataManager;
import ie.repository.managers.CartManager;
import ie.repository.managers.DeliveryManager;
import ie.repository.managers.OrderManager;
import ie.repository.managers.RestaurantManager;

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
    private CartStatus currentStatus;
    private float timeToDelivery = 0;
    private String deliveryId;

    public enum CartStatus {OnProgress, SearchingForDelivery, DeliveryIsOnTheWay, Delivered};

    public void setStatus(String status) {
        if(status.equals("OnProgress"))
            currentStatus = CartStatus.OnProgress;
        else if(status.equals("SearchingForDelivery"))
            currentStatus = CartStatus.SearchingForDelivery;
        else if(status.equals("DeliveryIsOnTheWay"))
            currentStatus = CartStatus.DeliveryIsOnTheWay;
        else
            currentStatus = CartStatus.Delivered;
    }

    public Cart(int id_){
        restaurantId = null;
        totalPrice = 0;
        id = id_;
        currentStatus = CartStatus.OnProgress;
    }

    public void setDeliveryId(String deliveryId) {
        this.deliveryId = deliveryId;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public void setOrders(ArrayList<Order> orders) {
        this.orders = orders;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public void setTimeToDelivery(float timeToDelivery) {
        this.timeToDelivery = timeToDelivery;
    }

    public Loghme.Status addOrder(Food food, int count){
        if(restaurantId == null){
            restaurantName = RestaurantManager.getInstance().retrieveById(food.getRestaurantId()).getName();
            restaurantId = food.getRestaurantId();
            CartManager.getInstance().updateRestaurant(id, restaurantName, restaurantId);
        }
        else if(!food.getRestaurantId().equals(restaurantId)){
            System.out.println("You can not order from two different restaurant");
            return Loghme.Status.BAD_REQUEST;
        }
        OrderDAO orderDAO = OrderManager.getInstance().retrieve(id, food.getId());
        int previousCount = orderDAO == null ? 0 : orderDAO.getCount();
        if(!food.isAvailable(previousCount + count))
            return Loghme.Status.BAD_REQUEST;
        OrderDAO newOrder = new OrderDAO();
        newOrder.setCount(previousCount + count);
        newOrder.setFoodId(food.getId());
        newOrder.setCartId(id);
        OrderManager.getInstance().save(newOrder);
        return Loghme.Status.OK;
    }

    public Loghme.Status finalizeOrder(){
        ArrayList<Order> orders = getOrders();
        if(orders.size() == 0)
            return Loghme.Status.BAD_REQUEST;
        for(Order order: orders){
            Loghme.Status status = order.finalizeOrder();
            if (!status.equals(Loghme.Status.OK))
                return status;
        }
        CartManager.getInstance().updateStatus(id, "SearchingForDelivery");
        return Loghme.Status.OK;
    }

    public ArrayList<Order> getOrders() {
        ArrayList<OrderDAO> orderDAOs = OrderManager.getInstance().retrieveCartOrders(id);
        ArrayList<Order> orders = new ArrayList<>();
        for(OrderDAO orderDAO: orderDAOs)
            orders.add(Loghme.getInstance().convertDAOToOrder(orderDAO));
        return orders;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public long getTotalPrice() {
        ArrayList<Order> orders = getOrders();
        long totalPrice = 0;
        for(Order order: orders){
            totalPrice += order.getOrderPrice();
        }
        return totalPrice;
    }

    public int getId() {
        return id;
    }

    public int getTotalNumOfOrders(){
        ArrayList<Order> orders = getOrders();
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
        OrderManager.getInstance().delete(id, food.getId());
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
            CartManager.getInstance().updateStatus(id, "Delivered");
            DeliveryManager.getInstance().updateTimeToDest(deliveryId, 0);
            timer.cancel();
            timer.purge();
        }
    }

}
