package com.example.forgetlost;

public class HelperClass {
    String email, name, uid;

    public HelperClass() {
    }

    public HelperClass(String email, String name, String uid) {
        this.email = email;
        this.name = name;
        this.uid = uid;
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

    public String getUid() {
        return uid;
    }

    public void setUid(String password) {
        this.uid = password;
    }

}
