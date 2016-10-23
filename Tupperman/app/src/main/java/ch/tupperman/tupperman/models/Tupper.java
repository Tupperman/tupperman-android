package ch.tupperman.tupperman.models;


import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;
import java.util.Date;

@Table(name = "Tuppers")
public class Tupper extends Model implements Serializable {
    public Tupper(){
        super();
    }
    @Column(name = "uuid")
    public String id;

    @Column(name = "name")
    public String name;

    @Column(name = "Description")
    public String description;

    @Column(name = "FoodGroup")
    public String foodGroup;

    @Column(name = "DateOfFreeze")
    public Date dateOfFreeze;

    public Tupper(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
