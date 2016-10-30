package ch.tupperman.tupperman.models;


import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    public JSONObject toJSON(){
        try {
            JSONObject jsonTupper = new JSONObject();
            jsonTupper.put("id", id);
            jsonTupper.put("name", name);
            jsonTupper.put("description", description);
            return jsonTupper;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
