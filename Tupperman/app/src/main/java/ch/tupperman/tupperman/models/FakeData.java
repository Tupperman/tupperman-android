package ch.tupperman.tupperman.models;

import org.json.JSONException;
import org.json.JSONObject;


public class FakeData {
    public JSONObject data;

    public FakeData() {
        String fake = "{\"tuppers\":[{\"id\":\"asdfasdf\",\"name\":\"Fleisch\", \"description\":\"gut\"}, {\"id\":\"asd\",\"name\":\"Indisch\", \"description\":\"awesome\"}]}";
        try {
            data = new JSONObject(fake);
            System.out.println(data.toString());
        } catch (JSONException e) {
            System.out.println("error in json string");
        }
    }
}
