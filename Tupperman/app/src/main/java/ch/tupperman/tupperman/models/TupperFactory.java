package ch.tupperman.tupperman.models;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tupper;
    }
}
