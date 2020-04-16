package ie.services.responses;

import java.util.ArrayList;

public class CartHistory {
    private ArrayList<CartInfo> items = new ArrayList<CartInfo>();

    public ArrayList<CartInfo> getItems() {
        return items;
    }

    public void setItems(ArrayList<CartInfo> items) {
        this.items = items;
    }

    public CartHistory(ArrayList<CartInfo> items){
        this.items = items;
    }
}
