package org.goldfish.minesweeper_android_01.persistance.dao;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import org.goldfish.minesweeper_android_01.persistance.entity.Result;

import java.util.List;

@Dao
public interface RecordDAO {
    @Query("SELECT * FROM Result")
    List<Result> getAll();

    @Query("SELECT * FROM Result WHERE win = 1")
    List<Result> getWinRecords();

    @Query("DELETE FROM Result")
    void deleteAll();

    @Insert(entity = Result.class)
    void recordGame(@NonNull Result result);
}
