package com.example.chazhampton.hospicerouter;


import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    public String userName;
    public String userEmail;
    public String userPassword;
    public String userType;//Admin/User
    public Integer userMiles;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String name, String email, String password, String type, Integer miles) {
        this.userName = name;
        this.userEmail = email;
        this.userPassword = password;
        this.userType = type;
        this.userMiles = miles;
    }

    public String getuserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getuserType() {
        return userType;
    }

    public Integer getuserMiles() {
        return userMiles;
    }

    public void setuserName(String name) {  this.userName = name; }

    public void setuserEmail(String address) { this.userEmail = address; }

    public void setuserPassword(String comments) { this.userPassword = comments; }

    public void setuserType(String type) {  this.userType = type; }

    public void setuserMiles(Integer miles) {  this.userMiles = miles; }
}
