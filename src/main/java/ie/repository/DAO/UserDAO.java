package ie.repository.DAO;

import java.util.HashMap;

public class UserDAO {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private long credit;
    private HashMap<String, Integer> location;
    private String password;

    public UserDAO(String firstName, String lastName, String email, String phone, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.credit = 0;
        this.location = new HashMap<>();
        location.put("x", 0);
        location.put("y", 0);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public long getCredit() {
        return credit;
    }

    public void setCredit(long credit) {
        this.credit = credit;
    }

    public HashMap<String, Integer> getLocation() {
        return location;
    }

    public void setLocation(HashMap<String, Integer> location) {
        this.location = location;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
