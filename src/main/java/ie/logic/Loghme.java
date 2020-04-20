package ie.logic;

import com.fasterxml.jackson.databind.ObjectMapper;
import ie.repository.DAO.*;
import ie.repository.DataManager;
import ie.repository.managers.*;

import java.util.*;

public class Loghme {
    private static Loghme instance;

    private static final float STANDARD_DISTANCE = 170;
    public static final int NOT_FOUND = -1;

    public void checkNotAssignedDeliveriesCarts() {
        ArrayList<CartDAO> searchingCarts = CartManager.getInstance().retrieveCartsByStatus("SearchingForDelivery");
        for(CartDAO cart: searchingCarts){
            Timer newTimer = new Timer();
            TimerTask scheduledTask = new AssignDeliveryTask(convertDAOToCart(cart), newTimer);
            newTimer.schedule(scheduledTask, 0, (5 * 1000));
        }
    }

    public static enum Status {INTERNAL_ERROR, NOT_FOUND, ACCESS_DENIED,OK, BAD_REQUEST, CONFLICT};

    static public ObjectMapper mapper;

    public static Loghme getInstance(){
        if(instance == null){
            instance = new Loghme();
        }
        return instance;
    }

    public Resturant convertDAOToRestaurant(RestaurantDAO restaurantDAO){
        Resturant resturant = new Resturant();
        resturant.setName(restaurantDAO.getName());
        resturant.setId(restaurantDAO.getId());
        resturant.setLogo(restaurantDAO.getLogo());
        resturant.setLocation(restaurantDAO.getLocation());
        return resturant;
    }

    public User convertDAOToUser(UserDAO userDAO){
        User user = new User();
        user.setFirstName(userDAO.getFirstName());
        user.setLastName(userDAO.getLastName());
        user.setEmail(userDAO.getEmail());
        user.setPhoneNum(userDAO.getPhone());
        user.setLocation(userDAO.getLocation());
        user.setCredit(userDAO.getCredit());
        user.setPassword(userDAO.getPassword());
        return user;
    }

    public DiscountFood convertDAOTODiscountFood(FoodDAO foodDAO){
        DiscountFood food = new DiscountFood();
        food.setName(foodDAO.getName());
        food.setImage(foodDAO.getImage());
        food.setDescription(foodDAO.getDescription());
        food.setPopularity(foodDAO.getPopularity());
        food.setCount(foodDAO.getCount());
        food.setOldPrice(foodDAO.getOldPrice());
        food.setPrice(foodDAO.getPrice());
        food.setRestaurantId(foodDAO.getRestaurantId());
        food.setId(foodDAO.getId());
        return food;
    }

    public OrdinaryFood convertDAOToOrdinaryFood(FoodDAO foodDAO){
        OrdinaryFood food = new OrdinaryFood();
        food.setName(foodDAO.getName());
        food.setImage(foodDAO.getImage());
        food.setDescription(foodDAO.getDescription());
        food.setPopularity(foodDAO.getPopularity());
        food.setPrice(foodDAO.getPrice());
        food.setRestaurantId(foodDAO.getRestaurantId());
        food.setId(foodDAO.getId());
        return food;
    }

    public Food convertDAOToFood(FoodDAO foodDAO){
        if(foodDAO.getType().equals("ordinary"))
            return convertDAOToOrdinaryFood(foodDAO);
        return convertDAOTODiscountFood(foodDAO);
    }

    public Order convertDAOToOrder(OrderDAO orderDAO){
        Order order = new Order();
        order.setId(orderDAO.getId());
        order.setCartId(orderDAO.getCartId());
        order.setFoodId(orderDAO.getFoodId());
        order.setNumOfFoods(orderDAO.getCount());
        return order;
    }

    public Cart convertDAOToCart(CartDAO cartDAO){
        Cart cart = new Cart(cartDAO.getId());
        cart.setRestaurantName(cartDAO.getRestaurantName());
        cart.setRestaurantId(cartDAO.getRestaurantId());
        cart.setStatus(cartDAO.getCartStatus());
        cart.setDeliveryId(cartDAO.getDeliveryId());
        return cart;
    }

    public ArrayList<Resturant> getNearResturants() {
        ArrayList<Resturant> nearRestaurants = new ArrayList<Resturant>();
        ArrayList<RestaurantDAO> allRestaurants = RestaurantManager.getInstance().retrieve();
        HashMap<String, Integer> userLoc = UserManager.getInstance().retrieveLocation("hsazarmsa@gmail.com");
        HashMap<String, Integer> restaurantLoc;
        for(RestaurantDAO restaurant: allRestaurants){
            restaurantLoc = restaurant.getLocation();
            if(isNear(userLoc,restaurantLoc))
                nearRestaurants.add(convertDAOToRestaurant(restaurant));
        }
        return nearRestaurants;
    }

    private boolean isNear(HashMap<String, Integer> userLoc, HashMap<String, Integer> restaurantLoc) {
        float distance = (float) Math.sqrt(Math.pow(restaurantLoc.get("x") - userLoc.get("x"), 2) +
                                           Math.pow(restaurantLoc.get("y") - userLoc.get("y"), 2));
        return distance < STANDARD_DISTANCE;
    }

    public Resturant getRestaurantById(String restaurantId){
        RestaurantDAO restaurantDAO = RestaurantManager.getInstance().retrieveById(restaurantId);
        if(restaurantDAO == null)
            return null;
        return convertDAOToRestaurant(restaurantDAO);
    }

    public Status addToCart(long foodId, int count){
        FoodDAO foodDAO = FoodManager.getInstance().retrieveFood(foodId);
        if(foodDAO == null)
            return Status.NOT_FOUND;
        Food food = convertDAOToFood(foodDAO);
        HashMap<String , Integer> restaurantLoc = RestaurantManager.getInstance().retrieveById(food.getRestaurantId()).getLocation();
        HashMap<String, Integer> userLoc = getLoginnedUser().getLocation();
        if(! isNear(userLoc, restaurantLoc))
            return Status.BAD_REQUEST;
        if(! food.isAvailable(count))
            return Status.BAD_REQUEST;
        Cart currentCart = getLoginnedUserCart();
        return currentCart.addOrder(food, count);
    }

    public Status deleteFromCart(long foodId){
        FoodDAO foodDAO = FoodManager.getInstance().retrieveFood(foodId);
        if(foodDAO == null)
            return Status.NOT_FOUND;
        Food food = convertDAOToFood(foodDAO);
        Cart currentCart = getLoginnedUserCart();
        return currentCart.deleteFromCart(food.getRestaurantId(), food);
    }

    public Status finalizeOrder(){
        return getLoginnedUser().finalizeOrder();
    }

    public Status assignDeliveriesToCart(Cart cart){
        float distance, timeToDelivery = Float.POSITIVE_INFINITY;
        HashMap<String, Integer> restaurantLoc, deliveryLoc, userLoc;
        DeliveryDAO assignedDelivery = null;
        restaurantLoc = RestaurantManager.getInstance().retrieveById(cart.getRestaurantId()).getLocation();
        User loggedUser = getLoginnedUser();
        userLoc = loggedUser.getLocation();
        ArrayList<DeliveryDAO> deliveryDAOs = DeliveryManager.getInstance().retrieveAll();
        for(DeliveryDAO deliveryDAO: deliveryDAOs){
            deliveryLoc = deliveryDAO.getLocation();
            distance = (float) (Math.sqrt(Math.pow((userLoc.get("x") - restaurantLoc.get("x")) , 2) +
                                          Math.pow((userLoc.get("y") - restaurantLoc.get("y")) , 2)) +
                                Math.sqrt(Math.pow((restaurantLoc.get("x") - deliveryLoc.get("x")) , 2) +
                                          Math.pow((restaurantLoc.get("y") - deliveryLoc.get("y")) , 2)));
            if(distance / deliveryDAO.getVelocity() < timeToDelivery)
            {
                timeToDelivery = distance / deliveryDAO.getVelocity();
                assignedDelivery = deliveryDAO;
            }
        }

        if(assignedDelivery == null)
            return Status.NOT_FOUND;
        DeliveryManager.getInstance().updateTimeToDest(assignedDelivery.getId(), timeToDelivery);
        CartManager.getInstance().updateDeliveryId(cart.getId(), assignedDelivery.getId());
        CartManager.getInstance().updateStatus(cart.getId(), "DeliveryIsOnTheWay");
        cart.setDeliveryId(assignedDelivery.getId());
        cart.setTimeToDelivery(timeToDelivery);
        return Status.OK;
    }

    public Status loadDeliveries(){
        DataManager.getInstance().setListOfDeliveries();
        return Status.OK;
    }

    public Cart getLoginnedUserCart() {
        CartDAO currentCartDAO = CartManager.getInstance().retrieveCurrentCart("hsazarmsa@gmail.com");
        return convertDAOToCart(currentCartDAO);
    }

    public User getLoginnedUser() {
        UserDAO userDAO = UserManager.getInstance().retrieve("hsazarmsa@gmail.com");
        return convertDAOToUser(userDAO);
    }

    public Status increaseCredit(long plusCredit) {
        User user = getLoginnedUser();
        return user.increaseCredit(plusCredit);
    }

    public ArrayList<DiscountFood> getDiscountFoods(){
        ArrayList<DiscountFood> foods = new ArrayList<>();
        ArrayList<FoodDAO> foodDAOS = FoodManager.getInstance().retrieveDiscountFood();
        for(FoodDAO foodDAO: foodDAOS)
            foods.add(convertDAOTODiscountFood(foodDAO));
        return foods;
    }

    public ArrayList<Cart> getLoginnedUserCartHistory() {
        ArrayList<CartDAO> cartDAOs = CartManager.getInstance().retrieveCartHistory("hsazarmsa@gmail.com");
        ArrayList<Cart> carts = new ArrayList<>();
        for(CartDAO cartDAO: cartDAOs){
            carts.add(convertDAOToCart(cartDAO));
        }
        return carts;
    }

    public Cart getUserPastCartById(int id){
        CartDAO currentCartDAO = CartManager.getInstance().retrieveCartById(id);
        return convertDAOToCart(currentCartDAO);
    }

    public long getFoodPartyRemainingTime(){
        return DataManager.getInstance().getFoodPartyRemainedTime();
    }

    public ArrayList<Resturant> searchRestaurants(String restaurantName, String foodName){
        ArrayList<Resturant> foundRestaurants = new ArrayList<>();
        ArrayList<RestaurantDAO> foundRestaurantsDAO = new ArrayList<>();
        if(foodName.equals(""))
            foundRestaurantsDAO = RestaurantManager.getInstance().searchByName(restaurantName);
        else if(restaurantName.equals(""))
            foundRestaurantsDAO = RestaurantManager.getInstance().searchByFoodName(foodName);
        else
            foundRestaurantsDAO = RestaurantManager.getInstance().searchByFoodAndRestaurantName(restaurantName, foodName);
        for(RestaurantDAO restaurantDAO: foundRestaurantsDAO)
            foundRestaurants.add(convertDAOToRestaurant(restaurantDAO));
        return foundRestaurants;
    }

    public void checkCarts() {
        ArrayList<UserDAO> users = UserManager.getInstance().retrieve();
        for(UserDAO user: users){
            CartDAO currentCart = CartManager.getInstance().retrieveCurrentCart(user.getEmail());
            if(currentCart.getRestaurantId() != null){
                checkCart(currentCart.getId());
            }
        }
    }

    public void checkCart(int cartId){
        ArrayList<OrderDAO> orders = OrderManager.getInstance().retrieveCartOrders(cartId);
        if(orders.size() == 0)
            CartManager.getInstance().updateRestaurant(cartId, null, null);
    }



}