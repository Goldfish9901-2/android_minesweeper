package org.goldfish.minesweeper_android_01.persistance.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import org.goldfish.minesweeper_android_01.persistance.dao.GameCacheDAO;
import org.goldfish.minesweeper_android_01.views.Grid;

@Database(entities = {Grid.class}, version = 1, exportSchema = false)
public abstract class GameCacheDatabase extends RoomDatabase {
    public abstract GameCacheDAO dao();
}
