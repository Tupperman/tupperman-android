package ch.tupperman.tupperman;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.activeandroid.ActiveAndroid;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import ch.tupperman.tupperman.models.Tupper;
import ch.tupperman.tupperman.models.TupperFactory;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class TupperFactoryTest {
    private TupperFactory mTupperFactory;
    private final String mTestStringTuppers = "[{\"uuid\":\"asdfasdf\",\"name\":\"Fleisch\", \"description\":\"gut\", \"freezeDate\": \"2016-11-11 11:11:11.111 +00:00\", \"expiryDate\": \"2016-22-11 11:11:11.111 +00:00\"}, " +
            "{\"uuid\":\"asd\",\"name\":\"Indisch\", \"description\":\"awesome\",\"freezeDate\": \"2016-11-11 11:11:11.111 +00:00\", \"expiryDate\": \"2016-22-11 11:11:11.111 +00:00\"}]";

    @Before
    public void setUp() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        ActiveAndroid.initialize(appContext);
        mTupperFactory = new TupperFactory();
    }

    @Test
    public void toTuppers() throws Exception {
        List<Tupper> tuppers = new ArrayList<>();
        try {
            JSONArray jsonTuppers = new JSONArray(mTestStringTuppers);
            tuppers = mTupperFactory.toTuppers(jsonTuppers);
        } catch (JSONException e) {
            System.out.print(e.toString());
        }
        assertEquals(tuppers.size(), 2);
    }

    @Test
    public void toTupper() throws Exception {
        Tupper tupper = new Tupper();
        try {
            JSONArray jsonTuppers = new JSONArray(mTestStringTuppers);
            JSONObject jsonTupper = jsonTuppers.getJSONObject(0);
            tupper = mTupperFactory.toTupper(jsonTupper);
        } catch (JSONException e) {
            System.out.print(e.toString());
        }
        assertEquals(tupper.description, "gut");
    }
}
