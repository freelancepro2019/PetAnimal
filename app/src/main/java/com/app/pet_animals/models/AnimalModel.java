package com.app.pet_animals.models;

import java.io.Serializable;

public class AnimalModel implements Serializable {
    private String animal_id;
    private String animal_name;
    private String animal_specification;
    private String animal_photo;
    private String owner_id;

    public AnimalModel() {
    }

    public AnimalModel(String animal_id, String animal_name, String animal_specification, String animal_photo, String owner_id) {
        this.animal_id = animal_id;
        this.animal_name = animal_name;
        this.animal_specification = animal_specification;
        this.animal_photo = animal_photo;
        this.owner_id = owner_id;
    }

    public String getAnimal_id() {
        return animal_id;
    }

    public void setAnimal_id(String animal_id) {
        this.animal_id = animal_id;
    }

    public String getAnimal_name() {
        return animal_name;
    }

    public void setAnimal_name(String animal_name) {
        this.animal_name = animal_name;
    }

    public String getAnimal_specification() {
        return animal_specification;
    }

    public void setAnimal_specification(String animal_specification) {
        this.animal_specification = animal_specification;
    }

    public String getAnimal_photo() {
        return animal_photo;
    }

    public void setAnimal_photo(String animal_photo) {
        this.animal_photo = animal_photo;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }
}
