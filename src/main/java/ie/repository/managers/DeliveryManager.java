package ie.repository.managers;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import ie.repository.DAO.DeliveryDAO;
import ie.repository.DataManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

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
            PreparedStatement pStatLoc = connection.prepareStatement(
                    "insert into Location (x, y)" +
                            " values (?, ?)"
            );
            PreparedStatement pStatDelivery = connection.prepareStatement(
                    "insert into Delivery (id, velocity, x, y)" +
                            " values (?, ?, ?, ?)"
            );
            for(DeliveryDAO delivery: deliveries) {
                pStatLoc.setInt(1, delivery.getLocation().get("x"));
                pStatLoc.setInt(2, delivery.getLocation().get("y"));
                pStatLoc.addBatch();

                pStatDelivery.setString(1, delivery.getId());
                pStatDelivery.setInt(2, delivery.getVelocity());
                pStatDelivery.setInt(3, delivery.getLocation().get("x"));
                pStatDelivery.setInt(4, delivery.getLocation().get("y"));
                pStatDelivery.addBatch();
            }
            pStatLoc.executeBatch();
            connection.commit();
            pStatLoc.close();

            pStatDelivery.executeBatch();
            connection.commit();
            pStatDelivery.close();

            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<DeliveryDAO> retrieveAll(){
        ArrayList<DeliveryDAO> deliveries = new ArrayList<>();
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement queryStatement = connection.prepareStatement(
                    "select * from Delivery d where d.timeToDest = 0"
            );
            ResultSet result = queryStatement.executeQuery();
            while(result.next()){
                DeliveryDAO delivery = new DeliveryDAO();
                HashMap<String, Integer> deliveryLoc = new HashMap<>();
                deliveryLoc.put("x", result.getInt("x"));
                deliveryLoc.put("y", result.getInt("y"));
                delivery.setLocation(deliveryLoc);
                delivery.setVelocity(result.getInt("velocity"));
                delivery.setId(result.getString("id"));
                delivery.setTimeToDest(result.getFloat("timeToDest"));
                deliveries.add(delivery);
            }
            result.close();
            queryStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return deliveries;
    }

    public void updateTimeToDest(String id, float timeToDest){
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "update Delivery set timeToDest = ? where id = ?"
            );
            statement.setFloat(1, timeToDest);
            statement.setString(2, id);
            statement.executeUpdate();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
