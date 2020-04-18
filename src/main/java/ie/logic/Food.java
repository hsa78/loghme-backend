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
    @JsonView(Views.FoodWithoutRestaurantName.class)
    @JsonProperty(required = true)
    private String name;
    @JsonView(Views.FoodWithoutRestaurantName.class)
    @JsonProperty(required = true)
    private String description;
    @JsonView(Views.FoodWithoutRestaurantName.class)
    @JsonProperty(required = true)
    private float popularity;
    @JsonView(Views.FoodWithoutRestaurantName.class)
    @JsonProperty(required = true)
    private long price;
    @JsonProperty
    private String restaurantId;
    @JsonView(Views.FoodWithoutRestaurantName.class)
    @JsonProperty(required = true)
    private String image;


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