package ie.services.responses;

import ie.logic.User;

public class UserInfo {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    public UserInfo(User user){
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.phone = user.getPhoneNum();
    }

    public UserInfo(){
        this.email = "a@b.com";
        this.firstName = "fatemeh";
        this.lastName = "alagheband";
        this.phone = "09123456789";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
