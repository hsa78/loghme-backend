package ie.repository.DAO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import ie.logic.Views;

import java.util.HashMap;

public class DeliveryDAO {
    private String id;
    private int velocity;
    private HashMap<String, Integer> location = new HashMap<String, Integer>();
    private float timeToDest;

    public String getId() {
        return id;
    }

    public HashMap<String, Integer> getLocation() {
        return location;
    }

    public float getTimeToDest() {
        return timeToDest;
    }

    public int getVelocity() {
        return velocity;
    }

    public void setLocation(HashMap<String, Integer> location) {
        this.location = location;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTimeToDest(float timeToDest) {
        this.timeToDest = timeToDest;
    }

    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }
}
