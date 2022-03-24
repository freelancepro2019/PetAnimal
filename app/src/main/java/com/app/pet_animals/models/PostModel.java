package com.app.pet_animals.models;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.app.pet_animals.BR;
import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class PostModel extends BaseObservable implements Serializable {
    private String post_id;
    private String post_details;
    private String post_available_date;
    private String post_available_time;
    private String post_status;
    //create at
    private String date;
    private String user_id;
    private String user_name;
    private String user_phone;
    private String animal_id;
    private String animal_specification;
    private String service_id;
    private String service_name;
    private String service_phone;
    @Exclude
    private boolean isValid = false;

    public PostModel() {
        isValid = false;
        post_details ="";
        animal_id ="";
        post_available_date ="";
        post_available_time ="";
    }

    public void isDataValid(){
        if (!post_details.isEmpty()&&!animal_id.isEmpty()){
            setValid(true);
        }else {
            setValid(false);

        }

    }

    public PostModel(String post_id, String post_details, String post_available_date, String post_available_time, String date, String user_id, String user_name, String user_phone, String animal_id, String animal_specification, String service_id, String service_name, String service_phone) {
        this.post_id = post_id;
        this.post_details = post_details;
        this.post_available_date = post_available_date;
        this.post_available_time = post_available_time;
        this.date = date;
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_phone = user_phone;
        this.animal_id = animal_id;
        this.animal_specification = animal_specification;
        this.service_id = service_id;
        this.service_name = service_name;
        this.service_phone = service_phone;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    @Bindable
    public String getPost_details() {
        return post_details;
    }

    public void setPost_details(String post_details) {
        this.post_details = post_details;
        notifyPropertyChanged(BR.post_details);
        isDataValid();
    }

    public String getPost_status() {
        return post_status;
    }

    public void setPost_status(String post_status) {
        this.post_status = post_status;
    }

    public String getPost_available_date() {
        return post_available_date;
    }

    public void setPost_available_date(String post_available_date) {
        this.post_available_date = post_available_date;
    }

    public String getPost_available_time() {
        return post_available_time;
    }

    public void setPost_available_time(String post_available_time) {
        this.post_available_time = post_available_time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getAnimal_id() {
        return animal_id;
    }

    public void setAnimal_id(String animal_id) {
        this.animal_id = animal_id;
        isDataValid();

    }

    public String getAnimal_specification() {
        return animal_specification;
    }

    public void setAnimal_specification(String animal_specification) {
        this.animal_specification = animal_specification;
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

    @Bindable
    @Exclude
    public boolean isValid() {
        return isValid;
    }

    @Exclude
    public void setValid(boolean valid) {
        isValid = valid;
        notifyPropertyChanged(BR.valid);
    }
}
