package com.italianswapp.tipcounter.OfflineDatabase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Tip.class}, version = 1)
@TypeConverters({TipDatabaseConverter.class})
public abstract class TipDatabase extends RoomDatabase {
    private static TipDatabase INSTANCE;
    public abstract TipDao tipDao();

    public static TipDatabase getDatabase(Context context) {
        if(INSTANCE==null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext() , TipDatabase.class, "tip_db").allowMainThreadQueries().build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() { INSTANCE=null; }
}
