package ch.tupperman.tupperman.models;


import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Table(name = "Tuppers")
public class Tupper extends Model implements Serializable {
    public Tupper(){
        super();
        UUID randomUUID = UUID.randomUUID();
        uuid = randomUUID.toString();
        dateOfFreeze = new Date();
        expiryDate = new Date();
    }

    @Column(name = "uuid")
    public String uuid;

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

    @Column(name = "ExpiryDate")
    public Date expiryDate;

    public Tupper(String name, String description) {
        this.name = name;
        this.description = description;
    }
    public JSONObject toJSON(){
        try {
            JSONObject jsonTupper = new JSONObject();
            jsonTupper.put("uuid", uuid);
            jsonTupper.put("name", name);
            jsonTupper.put("description", description);
            jsonTupper.put("weight", 42);
            jsonTupper.put("foodGroups", "Vegan :-P");
            jsonTupper.put("freezeDate", dateOfFreeze.toString());
            jsonTupper.put("expiryDate", expiryDate.toString());
            return jsonTupper;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
