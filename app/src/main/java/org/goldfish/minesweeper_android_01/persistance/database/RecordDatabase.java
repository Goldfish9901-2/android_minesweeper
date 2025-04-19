package org.goldfish.minesweeper_android_01.persistance.database;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.RoomDatabase;

import org.goldfish.minesweeper_android_01.persistance.dao.RecordDAO;
import org.goldfish.minesweeper_android_01.persistance.entity.GameInfo;


@Database(entities = {GameInfo.class}, version = 1, exportSchema = false)
public abstract class RecordDatabase extends RoomDatabase {
    @NonNull
    public abstract RecordDAO dao();
}
