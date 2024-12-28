package org.goldfish.minesweeper_android_01.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;


import org.goldfish.minesweeper_android_01.dao.RecordDAO;
import org.goldfish.minesweeper_android_01.entity.GFTime;
import org.goldfish.minesweeper_android_01.entity.Result;


@Database(entities = {Result.class, GFTime.class}, version = 1,exportSchema = false)
public abstract class RecordDatabase extends RoomDatabase {
    public abstract RecordDAO dao();
}
