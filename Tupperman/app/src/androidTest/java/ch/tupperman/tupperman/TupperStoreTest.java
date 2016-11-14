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

    private static final String mTupperName = "TestTupperName";

    @Before
    public void setUp() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        ActiveAndroid.initialize(appContext);
    }

    @Test
    public void selectTupper() throws Exception {
        saveTupper();
        List<Tupper> tuppers = new Select()
                .from(Tupper.class)
                .where("name = ?", mTupperName)
                .execute();

        assertEquals(tuppers.get(0).name, mTupperName);
    }

    @Test
    public void deleteTupper() throws Exception {
        Tupper tupper = saveTupper();
        List<Tupper> tuppers = getAllTuppers();

        assertEquals(tuppers.size(), 1);

        tupper.delete();

        tuppers = getAllTuppers();
        assertEquals(tuppers.size(), 0);
    }

    public void updateTupper() throws Exception {
        Tupper tupper = saveTupper();
        String updatedName = "updatedName";
        tupper.name = updatedName;
        tupper.save();

        List<Tupper> tuppers = getAllTuppers();
        assertEquals(tuppers.get(0).name, updatedName);
    }

    private Tupper saveTupper() {
        Tupper tupper = new Tupper();
        tupper.name = mTupperName;
        tupper.save();
        return tupper;
    }

    private List<Tupper> getAllTuppers() {
        return new Select()
                .from(Tupper.class)
                .execute();
    }

}