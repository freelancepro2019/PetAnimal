package com.app.pet_animals.models;

import java.io.Serializable;

public class FavoriteModel implements Serializable {
    private String id;
    private String user_id;
    private String service_id;
    private String service_name;
    private String service_phone;

    public FavoriteModel() {
    }

    public FavoriteModel(String id, String user_id, String service_id, String service_name, String service_phone) {
        this.id = id;
        this.user_id = user_id;
        this.service_id = service_id;
        this.service_name = service_name;
        this.service_phone = service_phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public String getService_phone() {
        return service_phone;
    }

    public void setService_phone(String service_phone) {
        this.service_phone = service_phone;
    }
}
