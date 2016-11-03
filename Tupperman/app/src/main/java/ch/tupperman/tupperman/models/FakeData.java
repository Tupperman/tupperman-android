package ch.tupperman.tupperman.models;

import org.json.JSONException;
import org.json.JSONObject;


public class FakeData {
    public JSONObject data;

    public FakeData() {
        String fake = "{\"tuppers\":[{\"uuid\":\"asdfasdf\",\"name\":\"Fleisch\", \"description\":\"gut\"}, {\"uuid\":\"asd\",\"name\":\"Indisch\", \"description\":\"awesome\"}]}";
        try {
            data = new JSONObject(fake);
        } catch (JSONException e) {
            System.out.println("error in json string");
        }
    }
}
