package ie.repository.managers;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import ie.repository.DAO.CartDAO;
import ie.repository.DAO.OrderDAO;
import ie.repository.DataManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderManager {
    private static OrderManager instance;
    private ComboPooledDataSource dataSource;


    private OrderManager(){
        this.dataSource = DataManager.getDataSource();
    }

    public static OrderManager getInstance() {
        if(instance == null){
            instance = new OrderManager();
        }
        return instance;
    }

    public void save(OrderDAO order){
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement pStatement = connection.prepareStatement(
                    "insert ignore into CartOrder (cartId, count, foodId)" +
                            " values (? ,?, ?)"
            );
            pStatement.setInt(1, order.getCartId());
            pStatement.setInt(2, order.getCount());
            pStatement.setLong(3, order.getFoodId());
            pStatement.executeUpdate();
            pStatement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<OrderDAO> retrieveCartOrders(int cartId){
        ArrayList<OrderDAO> orders = new ArrayList<>();
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement queryStatement = connection.prepareStatement(
                    "select * from CartOrder c where c.cartId = ?"
            );
            queryStatement.setInt(1, cartId);
            ResultSet result = queryStatement.executeQuery();
            while(result.next()){
                OrderDAO order = new OrderDAO();
                order.setCartId(cartId);
                order.setFoodId(result.getLong("foodId"));
                order.setCount(result.getInt("count"));
                order.setId(result.getInt("id"));
                orders.add(order);
            }
            result.close();
            queryStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public void updateCount(int orderId, int newCount){
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "update CartOrder set count = ? where id = ?"
            );
            statement.setInt(1, newCount);
            statement.setInt(2, orderId);
            statement.executeUpdate();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int orderId){
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "delete from CartOrder c where c.id = ?"
            );
            statement.setInt(1, orderId);
            statement.executeUpdate();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
