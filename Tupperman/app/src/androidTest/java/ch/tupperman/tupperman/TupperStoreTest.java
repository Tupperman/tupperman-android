package ch.tupperman.tupperman;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import ch.tupperman.tupperman.models.Tupper;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class TupperStoreTest {

    @Before
    public void setUp() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        ActiveAndroid.initialize(appContext);
    }


    @Test
    public void saveAndGetTupper() throws Exception {
        Tupper tupper = new Tupper();
        String name = "SavedTupper";
        tupper.name = name;
        tupper.save();

        List<Tupper> tuppers = new Select()
                .from(Tupper.class)
                .where("name = ?", name)
                .execute();

        assertEquals(tuppers.get(0).name, name);
    }
}