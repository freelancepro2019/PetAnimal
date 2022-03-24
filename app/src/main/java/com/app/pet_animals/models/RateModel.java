package com.app.pet_animals.models;

import java.io.Serializable;

public class RateModel implements Serializable {
    private String rate_id;
    private String service_id;
    private String order_id;
    private int rate;

    public RateModel() {
    }

    public RateModel(String rate_id, String service_id, String order_id, int rate) {
        this.rate_id = rate_id;
        this.service_id = service_id;
        this.order_id = order_id;
        this.rate = rate;
    }

    public String getRate_id() {
        return rate_id;
    }

    public void setRate_id(String rate_id) {
        this.rate_id = rate_id;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }
}
