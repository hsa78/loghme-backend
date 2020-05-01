package ie.repository.managers;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import ie.logic.User;
import ie.repository.DAO.FoodDAO;
import ie.repository.DAO.UserDAO;
import ie.repository.DataManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class UserManager {
    private static UserManager instance;
    private ComboPooledDataSource dataSource;


    private UserManager(){
        this.dataSource = DataManager.getDataSource();
    }

    public static UserManager getInstance() {
        if(instance == null){
            instance = new UserManager();
        }
        return instance;
    }

    public boolean save(UserDAO user){
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement pStatUser = connection.prepareStatement(
                    "insert into User (firstName, lastName, email, phone, credit, x, y, password)" +
                            " values (?, ?, ?, ?, ?, ?, ?, ?)"
            );
            PreparedStatement pStatLoc = connection.prepareStatement(
                    "insert ignore into Location (x, y) values (?, ?)"
            );
            pStatLoc.setInt(1, user.getLocation().get("x"));
            pStatLoc.setInt(2, user.getLocation().get("y"));
            pStatLoc.executeUpdate();
            pStatLoc.close();

            pStatUser.setNString(1, user.getFirstName());
            pStatUser.setNString(2, user.getLastName());
            pStatUser.setString(3, user.getEmail());
            pStatUser.setString(4, user.getPhone());
            pStatUser.setLong(5, user.getCredit());
            pStatUser.setInt(6, user.getLocation().get("x"));
            pStatUser.setInt(7, user.getLocation().get("y"));
            pStatUser.setNString(8, user.getPassword());
            pStatUser.executeUpdate();
            pStatUser.close();
            connection.close();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public HashMap<String, Integer> retrieveLocation(String userEmail){
        HashMap<String, Integer> loc = null;
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "select u.x, u.y from User u where u.email = ?"
            );
            statement.setString(1, userEmail);
            ResultSet result = statement.executeQuery();
            if(result.next()){
                loc = new HashMap<>();
                loc.put("x", result.getInt("u.x"));
                loc.put("y", result.getInt("u.y"));
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return loc;
    }

    public UserDAO retrieve(String userEmail){
        UserDAO user = null;
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "select * from User u where u.email = ?"
            );
            statement.setString(1, userEmail);
            ResultSet result = statement.executeQuery();
            if(result.next()){
                user = new UserDAO();
                HashMap<String, Integer> loc = new HashMap<>();
                user.setCredit(result.getLong("credit"));
                user.setEmail(result.getString("email"));
                user.setFirstName(result.getNString("firstName"));
                user.setLastName(result.getNString("lastName"));
                user.setPhone(result.getString("phone"));
                user.setPassword(result.getNString("password"));
                loc.put("x", result.getInt("x"));
                loc.put("y", result.getInt("y"));
                user.setLocation(loc);
            }
            result.close();
            statement.close();
            connection.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public void updateCredit(long plusCredit, String email){
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "update User set credit = credit + ? where email = ?"
            );
            statement.setLong(1, plusCredit);
            statement.setString(2, email);
            statement.executeUpdate();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<UserDAO> retrieve() {
        ArrayList<UserDAO> users = new ArrayList<>();
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "select * from User u"
            );
            ResultSet result = statement.executeQuery();
            while(result.next()){
                UserDAO user = new UserDAO();
                HashMap<String, Integer> loc = new HashMap<>();
                user.setCredit(result.getLong("credit"));
                user.setEmail(result.getString("email"));
                user.setFirstName(result.getNString("firstName"));
                user.setLastName(result.getNString("lastName"));
                user.setPhone(result.getString("phone"));
                user.setPassword(result.getNString("password"));
                loc.put("x", result.getInt("x"));
                loc.put("y", result.getInt("y"));
                user.setLocation(loc);
                users.add(user);
            }
            result.close();
            statement.close();
            connection.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
}
