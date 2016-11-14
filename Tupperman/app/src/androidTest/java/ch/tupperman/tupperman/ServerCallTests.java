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

import ch.tupperman.tupperman.data.ServerCall;
import ch.tupperman.tupperman.models.Tupper;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class ServerCallTests {

    private static final String mServerAdress = "http://ark-5.citrin.ch:9080/api/";

    private ServerCall mServerCall;

    @Before
    public void setUp() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        ActiveAndroid.initialize(appContext);
        mServerCall = new ServerCall(appContext, mServerAdress);
    }

    @Test
    public void selectTupper() throws Exception {
        List<Tupper> tuppers = new Select()
                .from(Tupper.class)
                .where("name = ?", mTupperName)
                .execute();

        assertEquals(tuppers.get(0).name, mTupperName);
    }

}
