package org.goldfish.minesweeper_android_01.persistance.dao;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import org.goldfish.minesweeper_android_01.views.Grid;

import java.util.List;

@Dao
public interface GameCacheDAO {
    @Insert
    void insert(@NonNull Grid grid);

    @Query("SELECT * FROM Grid")
    List<Grid> getAll();

    @Query("DELETE FROM Grid")
    void deleteAll();

    @Query("SELECT COUNT(*) FROM Grid")
    int count();

    @Query("SELECT * FROM Grid WHERE `row`=:row_provided AND col=:col_provided")
    Grid getGridByAbsloluteLocation(int row_provided, int col_provided);
}
