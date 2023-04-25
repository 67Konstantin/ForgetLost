package com.example.forgetlost;

public class HelperClass {
    String email, name, password, userName;

    public HelperClass() {
    }

    public HelperClass(String email, String name, String password, String userName) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
