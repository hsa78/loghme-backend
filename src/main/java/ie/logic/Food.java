package ie.logic;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import ie.logic.CustomDeserializer;

//@JsonIgnoreProperties(ignoreUnknown = true)
//@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonDeserialize(using = CustomDeserializer.class)
//@JsonSubTypes({
//        @JsonSubTypes.Type(value = DiscountFood.class, name = "discountFood"),
//})
public abstract class Food {
    @JsonProperty(required = true)
    private String name;
    @JsonProperty(required = true)
    private String description;
    @JsonProperty(required = true)
    private float popularity;
    @JsonProperty(required = true)
    private long price;
    @JsonProperty
    private String restaurantId;
    @JsonProperty(required = true)
    private String image;
    protected long id;


    public float getPopularity() {
        return popularity;
    }

    public long getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public String getImage() {
        return image;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPopularity(float popularity) {
        this.popularity = popularity;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public abstract Loghme.Status decreaseCount(int numOfFoods);

    public abstract boolean isAvailable(int numOfFoods);


    public abstract boolean hasDiscount() ;

    public abstract void setCount(int count);
}