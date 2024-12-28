package org.goldfish.minesweeper_android_01.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.DeleteTable;
import androidx.room.Insert;
import androidx.room.Query;

import org.goldfish.minesweeper_android_01.entity.GFTime;
import org.goldfish.minesweeper_android_01.entity.Result;

import java.util.List;

@Dao
public interface RecordDAO {
    @Query("SELECT * FROM Result")
    List<Result> getAll();

    @Query("SELECT * FROM Result WHERE win = true")
    List<Result> getWinRecords();

    @Delete(entity = Result.class)
    void delete(Result result);

    @Query("DELETE FROM Result")
    void deleteAll();

    @Query("SELECT * FROM GFTime WHERE id=:id")
    GFTime getTime(int id);

    @Insert(entity = GFTime.class)
    void insertTime(GFTime time);

    @Query("SELECT id FROM GFTime WHERE month = :month AND day = :day AND hour = :hour AND minute = :minute AND second = :second")
    List<Integer> getTimeId(int month, int day, int hour, int minute, int second);
    default List<Integer> getTimeId(GFTime time) {
        return getTimeId(time.getMonth(), time.getDay(), time.getHour(), time.getMinute(), time.getSecond());
    }

    @Query("SELECT * FROM GFTime WHERE id = :id")
    GFTime getTimeById(int id);
}
