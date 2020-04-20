package ie.logic;

import java.util.Timer;
import java.util.TimerTask;

public class AssignDeliveryTask extends TimerTask {
    private Cart cart;
    private Timer timer;
    public AssignDeliveryTask(Cart cart, Timer timer){
        this.cart = cart;
        this.timer = timer;
    }
    @Override
    public void run() {
        try {
            Loghme.Status addDeliveriesStatus = Loghme.getInstance().loadDeliveries();
            if (addDeliveriesStatus.equals(Loghme.Status.INTERNAL_ERROR))
                return;
            Loghme.Status assignDeliveryStatus = Loghme.getInstance().assignDeliveriesToCart(cart);
            if (assignDeliveryStatus.equals(Loghme.Status.OK)) {
                cart.startDelivering();
                System.out.println("Found Delivery!");
                timer.cancel();
                timer.purge();
            }
            else{
                System.out.println("Delivery not found. Retry after 30 seconds...");
            }
        }catch (Exception e){
            System.out.println("Exception in timer");
        }
    }
}