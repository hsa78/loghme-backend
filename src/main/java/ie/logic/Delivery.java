package ie.logic;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import ie.logic.Views;

import java.util.HashMap;

public class Delivery {
    @JsonView(Views.DeliveryWithoutTimeToDest.class)
    private String id;
    @JsonView(Views.DeliveryWithoutTimeToDest.class)
    private int velocity;
    @JsonView(Views.DeliveryWithoutTimeToDest.class)
    private HashMap<String, Integer> location = new HashMap<String, Integer>();
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private float timeToDest;

    public HashMap<String, Integer> getLocation() {
        return location;
    }

    public String getId() {
        return id;
    }

    public int getVelocity() {
        return velocity;
    }

    public float getTimeToDest() {
        return timeToDest;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLocation(HashMap<String, Integer> location) {
        this.location = location;
    }

    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }

    public void setTimeToDest(float timeToDest) {
        this.timeToDest = timeToDest;
    }
}
