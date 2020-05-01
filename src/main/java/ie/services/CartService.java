package ie.services;

import ie.logic.AssignDeliveryTask;
import ie.logic.Cart;
import ie.logic.Loghme;

import java.text.ParseException;
import java.util.Timer;
import java.util.TimerTask;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import ie.services.responses.CartHistory;
import ie.services.responses.CartInfo;
import ie.services.responses.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class CartService {

    @RequestMapping(value = "/user/cartBadge", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CartInfo> getCurrentCartInfo(){
        return new ResponseEntity<CartInfo>(new CartInfo(Loghme.getInstance().getLoginnedUserCart()), HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/user/cart", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Cart> getCurrentCart(){
        Cart currentCart = Loghme.getInstance().getLoginnedUserCart();
        currentCart.setOrders(currentCart.getOrders());
        return new ResponseEntity<Cart>(currentCart, HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/user/cartHistory", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CartHistory> getCartHistory(){
        ArrayList<CartInfo> response = new ArrayList<CartInfo>();
        ArrayList<Cart> cartHistory = Loghme.getInstance().getLoginnedUserCartHistory();
        for(Cart cart: cartHistory)
            response.add(new CartInfo(cart));
        return new ResponseEntity<CartHistory>(new CartHistory(response), HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/user/cartHistory/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Cart> getPastCart(@PathVariable(value = "id") String id){
        try {
            int cartId = Integer.parseInt(id);
            Cart cart = Loghme.getInstance().getUserPastCartById(cartId);
            cart.setOrders(cart.getOrders());
            return new ResponseEntity<Cart>(cart, HttpStatus.ACCEPTED);
        }catch (Exception e){
            return new ResponseEntity<Cart>((Cart) null, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/user/cart", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StatusCode> addToCart(@RequestParam(value = "foodId") long foodId,
                                @RequestParam(value = "count") int count){
        Loghme.Status result = Loghme.getInstance().addToCart(foodId , count);
        return resultDecoder(result);
    }

    @RequestMapping(value = "/user/deleteFromCart", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StatusCode> deleteFromCart(@RequestParam(value = "foodId") long foodId){
        Loghme.Status result = Loghme.getInstance().deleteFromCart(foodId);
        return resultDecoder(result);
    }

    @RequestMapping(value = "/user/finalizeOrder", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StatusCode> finalizeCart(){
        Cart currentCart = Loghme.getInstance().getLoginnedUserCart();
        Loghme.Status result = Loghme.getInstance().finalizeOrder();
        if(result.equals(Loghme.Status.OK)){
            Timer newTimer = new Timer();
            TimerTask scheduledTask = new AssignDeliveryTask(currentCart, newTimer);
            newTimer.schedule(scheduledTask, 0, (30 * 1000));
        }
        return resultDecoder(result);
    }

    private ResponseEntity<StatusCode> resultDecoder(Loghme.Status status){
        if(status.equals(Loghme.Status.OK))
            return new ResponseEntity<StatusCode>(new StatusCode(200), HttpStatus.ACCEPTED);
        else if(status.equals(Loghme.Status.NOT_FOUND))
            return new ResponseEntity<StatusCode>(new StatusCode(404), HttpStatus.NOT_FOUND);
        else if(status.equals(Loghme.Status.BAD_REQUEST))
            return new ResponseEntity<StatusCode>(new StatusCode(400), HttpStatus.BAD_REQUEST);
        else if(status.equals(Loghme.Status.ACCESS_DENIED))
            return new ResponseEntity<StatusCode>(new StatusCode(403), HttpStatus.FORBIDDEN);
        else if(status.equals(Loghme.Status.CONFLICT))
            return new ResponseEntity<StatusCode>(new StatusCode(409), HttpStatus.CONFLICT);
        return new ResponseEntity<StatusCode>(new StatusCode(500), HttpStatus.INTERNAL_SERVER_ERROR);
    }


}


