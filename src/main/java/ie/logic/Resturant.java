package ie.logic;

import com.fasterxml.jackson.annotation.JsonProperty;
import ie.logic.DiscountFood;
import ie.repository.DAO.FoodDAO;
import ie.repository.managers.FoodManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Resturant {
    @JsonProperty(required = true)
    private String id;
    @JsonProperty(required = true)
    private String name;
    @JsonProperty(required = true)
    private HashMap<String, Integer> location = new HashMap<String, Integer>();
    @JsonProperty(required = true)
    private List<Food> menu = new ArrayList<Food>();
    @JsonProperty(required = true)
    private String logo;

    static int NOT_FOUND = -1;

    public String getName() {
        return name;
    }


    public ArrayList<Food> getMenu() {
        ArrayList<Food> foods = new ArrayList<>();
        ArrayList<FoodDAO> foodDAOs = FoodManager.getInstance().retrieveMenu(id, "ordinary");
        for(FoodDAO foodDAO: foodDAOs)
            foods.add(Loghme.getInstance().convertDAOToFood(foodDAO));
        return foods;
    }

    public HashMap<String, Integer> getLocation() {
        return location;
    }

    public String getId() {
        return id;
    }

    public String getLogo() {
        return logo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(HashMap<String, Integer> location) {
        this.location = location;
    }

    public void setMenu(List<Food> menu) {
        this.menu = menu;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

}