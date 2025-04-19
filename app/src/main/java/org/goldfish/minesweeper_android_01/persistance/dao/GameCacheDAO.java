package org.goldfish.minesweeper_android_01.persistance.dao;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import org.goldfish.minesweeper_android_01.views.Grid;
import org.goldfish.minesweeper_android_01.views.activities.GameActivity;

import java.util.List;

@Dao
public interface GameCacheDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(@NonNull Grid grid);

    @Query("SELECT * FROM Grid")
    List<Grid> getAll();

    @Query("DELETE FROM Grid")
    void deleteCache();

    @Query("SELECT COUNT(*) FROM Grid")
    int count();

    @Query("SELECT * FROM Grid WHERE `row`=:row_provided AND col=:col_provided")
    Grid getGridByAbsoluteLocation(int row_provided, int col_provided);

    @Nullable
    default Grid getGridByRelativeLocation(int row, int col, @NonNull GameActivity activity) {
        Grid grid = getGridByAbsoluteLocation(row, col);
        if (grid == null) return null;
        grid.setDisplayGrid(activity);
        return grid;
    }
}
