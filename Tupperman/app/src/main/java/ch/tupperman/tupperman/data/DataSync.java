package ch.tupperman.tupperman.data;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.util.List;

import ch.tupperman.tupperman.models.Tupper;

public class DataSync {
    public void storeTuppers(List<Tupper> tuppers){
        new Delete().from(Tupper.class).execute();
        for(Tupper tupper: tuppers){
            tupper.save();
        }
    }

    public static List<Tupper> getAllTuppers() {
        return new Select()
                .from(Tupper.class)
                .execute();
    }
}

