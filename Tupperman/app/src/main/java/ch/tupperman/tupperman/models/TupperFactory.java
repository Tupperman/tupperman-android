package ch.tupperman.tupperman.models;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TupperFactory {

    public List<Tupper> toTuppers(JSONObject jsonTuppers) {
        List<Tupper> tupperList = new ArrayList<>();
        try {
            JSONArray jsonArray = jsonTuppers.getJSONArray("tuppers");
            for (int n = 0; n < jsonArray.length(); n++) {
                JSONObject object = jsonArray.getJSONObject(n);
                Tupper tupper = new Tupper();
                tupper.id = object.getString("id");
                tupper.name = object.getString("name");
                tupper.description = object.getString("description");
                tupperList.add(tupper);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tupperList;
    }
}
