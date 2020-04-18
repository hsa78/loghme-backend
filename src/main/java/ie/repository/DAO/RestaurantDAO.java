package ie.repository.DAO;

import ie.logic.Food;

import java.util.ArrayList;
import java.util.HashMap;

public class RestaurantDAO {
    private  String id;
    private  String name;
    private  String logo;
    private HashMap<String, Integer> location;
    private ArrayList<FoodDAO> menu;

    public HashMap<String, Integer> getLocation() {
        return location;
    }

    public String getId() {
        return id;
    }

    public String getLogo() {
        return logo;
    }

    public String getName() {
        return name;
    }

    public ArrayList<FoodDAO> getMenu() {
        return menu;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLocation(HashMap<String, Integer> location) {
        this.location = location;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMenu(ArrayList<FoodDAO> menu) {
        this.menu = menu;
    }
}
