package Hasbi.contactapplication;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities={Contact.class},version=2,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase appDatabase;
    public static AppDatabase getInstance(Context context) {
        if(appDatabase==null)
        {
            //initialize database
            appDatabase= Room.databaseBuilder(context.getApplicationContext(),AppDatabase.class,"ContactDB").allowMainThreadQueries().fallbackToDestructiveMigration().build();

        }
        return appDatabase;
    }
    public abstract DAO contactDao();
}