package ch.tupperman.tupperman.models;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TupperFactory {

    public List<Tupper> toTuppers(JSONArray jsonTuppers) {
        List<Tupper> tupperList = new ArrayList<>();
        try {
            for (int i = 0; i < jsonTuppers.length(); i++) {
                JSONObject jsonTupper = jsonTuppers.getJSONObject(i);
                Tupper tupper = toTupper(jsonTupper);
                tupperList.add(tupper);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tupperList;
    }

    public Tupper toTupper(JSONObject jsonTupper) {
        Tupper tupper = new Tupper();
        try {
            tupper.uuid = jsonTupper.getString("uuid");
            tupper.name = jsonTupper.getString("name");
            tupper.description = jsonTupper.getString("description");
            tupper.dateOfFreeze = stringToDate(jsonTupper.getString("freezeDate"));
            tupper.expiryDate = stringToDate(jsonTupper.getString("expiryDate"));
            tupper.weight = jsonTupper.getInt("weight");
            tupper.foodGroup = jsonTupper.getString("foodGroups");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tupper;
    }

    private Date stringToDate(String stringOfDate){
        //"2016-22-11 11:11:11.111 +00:00"
        DateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
        Date date = new Date();
        try {
            date = mFormat.parse(stringOfDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
