package ie.repository.managers;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import ie.repository.DAO.DeliveryDAO;
import ie.repository.DAO.FoodDAO;
import ie.repository.DataManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class DeliveryManager {
    private static DeliveryManager instance;
    private ComboPooledDataSource dataSource;


    private DeliveryManager(){
        this.dataSource = DataManager.getDataSource();
    }

    public static DeliveryManager getInstance() {
        if(instance == null){
            instance = new DeliveryManager();
        }
        return instance;
    }

    public void save(ArrayList<DeliveryDAO> deliveries){
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement pStatDelivery = connection.prepareStatement(
                    "insert ignore into Delivery (id, velocity, x, y)" +
                            " values (?, ?, ?, ?)"
            );
            for(DeliveryDAO delivery: deliveries) {
                pStatDelivery.setString(1, delivery.getId());
                pStatDelivery.setInt(2, delivery.getVelocity());
                pStatDelivery.setInt(4, delivery.getLocation().get("x"));
                pStatDelivery.setInt(5, delivery.getLocation().get("y"));
                pStatDelivery.addBatch();
            }
            pStatDelivery.executeBatch();
            connection.commit();
            pStatDelivery.close();

            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
