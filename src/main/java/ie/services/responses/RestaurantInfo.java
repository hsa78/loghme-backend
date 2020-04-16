package ie.services.responses;

import ie.logic.Resturant;

public class RestaurantInfo {
    private String name;
    private String id;
    private String logo;

    public RestaurantInfo(Resturant mainData){
        this.name = mainData.getName();
        this.id = mainData.getId();
        this.logo = mainData.getLogo();
    }

    public String getName() {
        return name;
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

    public void setId(String id) {
        this.id = id;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
