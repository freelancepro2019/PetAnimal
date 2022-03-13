package com.app.pet_animals.models;

import java.io.Serializable;

public class UserModel implements Serializable {
    private String user_id;
    private String image_url;
    private String first_name;
    private String last_name;
    private String full_name;
    private String phone_code;
    private String phone;
    private String email;
    private String password;
    private String user_type;
    private int rate;
    private String filter_attr;

    public UserModel() {
    }

    public UserModel(String user_id, String first_name, String last_name, String phone_code, String phone, String email, String password, String user_type,String filter_attr) {
        this.user_id = user_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone_code = phone_code;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.user_type = user_type;
        this.filter_attr = filter_attr;
        this.rate = 0;
        this.full_name = first_name+" "+last_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPhone_code() {
        return phone_code;
    }

    public void setPhone_code(String phone_code) {
        this.phone_code = phone_code;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getFilter_attr() {
        return filter_attr;
    }

    public void setFilter_attr(String filter_attr) {
        this.filter_attr = filter_attr;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }
}
