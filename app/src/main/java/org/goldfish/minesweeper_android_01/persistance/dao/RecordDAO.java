package org.goldfish.minesweeper_android_01.persistance.dao;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import org.goldfish.minesweeper_android_01.persistance.entity.GameInfo;

import java.util.List;

@Dao
public interface RecordDAO {
    @Query("SELECT * FROM GameInfo")
    List<GameInfo> getAll();

    @Query("SELECT * FROM GameInfo WHERE win = 1")
    List<GameInfo> getWinRecords();

    @Query("DELETE FROM GameInfo")
    void deleteAll();

    @Insert(entity = GameInfo.class)
    void recordGame(@NonNull GameInfo gameInfo);
}
