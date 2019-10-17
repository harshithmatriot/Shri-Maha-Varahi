package com.matriot.varahi.model;

public class LognData {
    private String name;
    private String password;

    public LognData() {

    }

    public LognData(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
