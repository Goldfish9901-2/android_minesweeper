package org.goldfish.minesweeper_android_01;

import android.app.Application;

import androidx.room.Room;

import org.goldfish.minesweeper_android_01.dao.RecordDAO;
import org.goldfish.minesweeper_android_01.database.RecordDatabase;

public class MainApplication extends Application {

    public static final String TAG = "Minesweeper";
    private static MainApplication instance=null;
    private RecordDAO dao;

    public static MainApplication getInstance() {
        return instance;
    }
    @Override
    public void onCreate() {
        instance = this;
        RecordDatabase database = Room.databaseBuilder(this, RecordDatabase.class, "minesweeper.db")
                .allowMainThreadQueries()
                .build();
        dao = database.dao();
        super.onCreate();
    }

    public RecordDAO getDao() {
        return dao;
    }
}
