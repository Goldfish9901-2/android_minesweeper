package org.goldfish.minesweeper_android_01.persistance.entity;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.Relation;

import com.bin.david.form.annotation.SmartTable;

import org.goldfish.minesweeper_android_01.MainApplication;
import org.goldfish.minesweeper_android_01.views.activities.GameActivity;

import java.time.ZoneOffset;

/**
 * {@code Result} 游戏结果信息 <br/>
 */
@SmartTable(name="游戏结果")
@Entity
public class Result extends Intent implements ResultFieldNames {

    /**
     * 数据库存储字段
     */
    @PrimaryKey(autoGenerate = true)
    int id;
    /**
     * 雷区高度
     */
    int height;

    /**
     * 雷区宽度
     */
    int width;
    /**
     * 雷数
     */
    int mineCount;
    /**
     * 难度描述
     */
    String difficulty_description;
    /**
     * 是否胜利
     */
    @Nullable
    Boolean win;
    long interval;
    /**
     * 开始时间
     * 指向{@link GFTime}数据库表中的记录
     *
     * @see GFTime
     */
    int startTimeID;
    /**
     * 结束时间
     *
     * @see #startTimeID
     */
    int endTimeID;

    public Result(@NonNull Activity activity) {
        super(activity, GameActivity.class);
        startTimeID = -1;
        endTimeID = -1;
        win = null;
    }

    public Result() {
        super();
        startTimeID = -1;
        endTimeID = -1;
        win = null;
    }


    @NonNull
    public static Result EASY(@NonNull Activity activity) {
        Result result = new Result(activity);
        result.setHeight(9);
        result.setWidth(9);
        result.setMineCount(10);
        result.setDifficulty_description("简单");
        return result;

    }

    @NonNull
    public static Result MEDIUM(@NonNull Activity activity) {
        Result result = new Result(activity);
        result.setHeight(16);
        result.setWidth(16);
        result.setMineCount(40);
        result.setDifficulty_description("中等");
        return result;
    }

    @NonNull
    public static Result HARD(@NonNull Activity activity) {
        Result result = new Result(activity);
        result.setHeight(30);
        result.setWidth(16);
        result.setMineCount(99);
        result.setDifficulty_description("困难");
        return result;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getMineCount() {
        return mineCount;
    }

    public void setMineCount(int mineCount) {
        this.mineCount = mineCount;
    }

    @NonNull
    public String getDifficultyDescription() {
        return String.valueOf(difficulty_description);
    }

    @Nullable
    public Boolean isWin() {
        return win;
    }

    public void setWin(@Nullable Boolean win) {
        this.win = win;
    }

    public void start() {
        GFTime startTime = new GFTime(GFTime.TIME_SOURCE.FROM_SYSTEM);
        MainApplication.getInstance().getDao().insertTime(startTime);
        this.startTimeID = MainApplication.getInstance().getDao().getTimeId(startTime).get(0);
    }

    public void end() {
        GFTime endTime = new GFTime(GFTime.TIME_SOURCE.FROM_SYSTEM);
        MainApplication.getInstance().getDao().insertTime(endTime);
        this.endTimeID = MainApplication.getInstance().getDao().getTimeId(endTime).get(0);
        GFTime startTime = MainApplication.getInstance().getDao().getTimeById(startTimeID, true);
        long endStamp = endTime.getTime().toEpochSecond(ZoneOffset.ofHours(8));
        long startStamp = startTime.getTime().toEpochSecond(ZoneOffset.ofHours(8));
        setInterval(endStamp - startStamp);
        Log.w(MainApplication.TAG, MainApplication.getInstance().getDao().getAll().toString());
    }

    public int getEndTimeID() {
        return endTimeID;
    }

    public void setEndTimeID(int endTimeID) {
        this.endTimeID = endTimeID;
    }

    @NonNull
    public String getDifficulty_description() {
        return difficulty_description;
    }

    public void setDifficulty_description(@NonNull String difficulty_description) {
        this.difficulty_description = difficulty_description;
    }

    public int getStartTimeID() {
        return startTimeID;
    }

    public void setStartTimeID(int startTimeID) {
        this.startTimeID = startTimeID;
    }

    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    @Override
    public String toString() {
        return "Result{" +
                "\n\t win=" + win +
                "\n\t interval=" + interval +
                "\n\t startTimeID=" + startTimeID +
                "\n\t endTimeID=" + endTimeID +
                "\n\t id=" + id +
                "\n\t height=" + height +
                "\n\t width=" + width +
                "\n\t mineCount=" + mineCount +
                "\n\t difficulty_description='" + difficulty_description + '\'' +
                '}';
    }
}
