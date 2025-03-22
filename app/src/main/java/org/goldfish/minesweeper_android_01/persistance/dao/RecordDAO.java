package org.goldfish.minesweeper_android_01.persistance.dao;

import android.util.Log;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import org.goldfish.minesweeper_android_01.MainApplication;
import org.goldfish.minesweeper_android_01.persistance.entity.GFTime;
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

    default GFTime getTimeById(int id, boolean generateFromFields) {
        GFTime time = getTimeById(id);
        if (generateFromFields && time != null) try {
            time.generateTime();
        } catch (Exception e) {
            Log.e(MainApplication.TAG, "getTimeById: \n" + e.getLocalizedMessage());
        }
        return time;
    }

    @Insert(entity = Result.class)
    void recordGame(Result result);
}
