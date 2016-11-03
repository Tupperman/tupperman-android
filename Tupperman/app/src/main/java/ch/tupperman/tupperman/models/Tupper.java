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

    @Column(name = "weight")
    public int weight;

    @Column(name = "description")
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
            jsonTupper.put("uuid", "12345");
            jsonTupper.put("name", name);
            jsonTupper.put("description", description);
            jsonTupper.put("weight", 42);
            jsonTupper.put("foodGroups", "Vegan :-P");
            jsonTupper.put("freezeDate", "2016-11-11 11:11:11.111 +00:00");
            jsonTupper.put("expiryDate", "2017-11-11 11:11:11.111 +00:00");
            return jsonTupper;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
