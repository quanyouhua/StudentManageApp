package com.example.student;

/**
 * Created by 17822 on 2020/10/25.
 */

public class user {


    private int id;
    private String username;
    private String password;
    private String permission;


    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }



    public int getId() {return id;}

    public void setId(int id) {this.id = id;}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



}
