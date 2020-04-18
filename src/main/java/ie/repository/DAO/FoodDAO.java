package ie.repository.DAO;

public class FoodDAO {
    private String name;
    private String restaurantId;
    private String image;
    private long price;
    private long oldPrice;
    private String description;
    private float popularity;
    private int count;
    private String type;

    public String getName() {
        return name;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public String getImage() {
        return image;
    }

    public long getPrice() {
        return price;
    }

    public long getOldPrice() {
        return oldPrice;
    }

    public String getDescription() {
        return description;
    }

    public float getPopularity() {
        return popularity;
    }

    public int getCount() {
        return count;
    }

    public String getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOldPrice(long oldPrice) {
        this.oldPrice = oldPrice;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(String image) {
        this.image = image;
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

    public void setType(String type) {
        this.type = type;
    }
}
