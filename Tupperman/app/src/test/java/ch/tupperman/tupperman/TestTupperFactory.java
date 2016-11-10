package ch.tupperman.tupperman;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import ch.tupperman.tupperman.models.Tupper;
import ch.tupperman.tupperman.models.TupperFactory;

import static org.junit.Assert.*;

public class TestTupperFactory {
    private TupperFactory mTupperFactory;
    private JSONArray mTestJsonTuppers;
    private final String mTestStringTuppers = "{\"tuppers\":[{\"uuid\":\"asdfasdf\",\"name\":\"Fleisch\", " +
            "\"description\":\"gut\", \"freezeDate\": \"2016-11-11 11:11:11.111 +00:00\",\n" +
            " \"expiryDate\": \"2016-22-11 11:11:11.111 +00:00\"}, " +
            "{\"uuid\":\"asd\",\"name\":\"Indisch\", \"description\":\"awesome\",\"freezeDate\": " +
            "\"2016-11-11 11:11:11.111 +00:00\",\n" +
            "  \"expiryDate\": \"2016-22-11 11:11:11.111 +00:00\"}]}";

    @Before
    public void setUp() throws Exception {
        mTupperFactory = new TupperFactory();
        try {
            JSONObject tuppers = new JSONObject("{\"test\":\"12\"}");
            mTestJsonTuppers = tuppers.getJSONArray("tuppers");
        } catch (JSONException e) {
            System.out.print(e.toString());
        }
    }

    @Test
    public void toTuppers() throws Exception{
        List<Tupper> tuppers = mTupperFactory.toTuppers(mTestJsonTuppers);
        assertEquals(tuppers.size(), 2);
    }
}
