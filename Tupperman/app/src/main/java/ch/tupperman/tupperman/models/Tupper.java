package ch.tupperman.tupperman.models;

import java.util.Date;

public class Tupper {
    private String id;
    private String name;
    private String description;
    private String foodGroup;
    private Date dateOfFreeze;

    public Tupper(String name, String description){
        this.name = name;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFoodGroup() {
        return foodGroup;
    }

    public void setFoodGroup(String foodGroup) {
        this.foodGroup = foodGroup;
    }

    public Date getDateOfFreeze() {
        return dateOfFreeze;
    }

    public void setDateOfFreeze(Date dateOfFreeze) {
        this.dateOfFreeze = dateOfFreeze;
    }

}
