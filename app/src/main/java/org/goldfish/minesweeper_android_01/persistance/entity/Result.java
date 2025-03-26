package org.goldfish.minesweeper_android_01.persistance.entity;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.goldfish.minesweeper_android_01.views.activities.GameActivity;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * {@code Result} 游戏结果信息 <br/>
 */
//@SmartTable(name="游戏结果")
@Entity
public sealed class Result
        extends Intent
        implements ResultFieldNames
        permits ResultSummary {
    @PrimaryKey(autoGenerate = true)
    protected int id;
    protected int height;
    protected int width;
    protected int mineCount;
    protected String difficulty_description;
    @Nullable
    protected Boolean win;
    protected long interval;
    protected long startTime;
    protected long endTime;
    @Ignore
    protected LocalDateTime startLocalDateTime;
    @Ignore
    protected LocalDateTime endLocalDateTime;

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
        this.startLocalDateTime = LocalDateTime.ofEpochSecond(startTime, 0, ZoneOffset.of("+8"));
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
        this.endLocalDateTime = LocalDateTime.ofEpochSecond(endTime, 0, ZoneOffset.of("+8"));
    }

    @NonNull
    public LocalDateTime getStartLocalDateTime() {
        return startLocalDateTime;
    }

    @Nullable
    public Boolean getWin() {
        return win;
    }

    @Nullable
    public LocalDateTime getEndLocalDateTime() {
        return endLocalDateTime;
    }


    public Result(@NonNull Activity activity) {
        super(activity, GameActivity.class);
        startTime = -1L;
        endTime = -1L;
        win = null;
    }

    public Result() {
        super();
        startTime = -1L;
        endTime = -1L;
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


    public void setWin(@Nullable Boolean win) {
        this.win = win;
    }

    public void start() {
        this.startTime = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
        setStartTime(this.startTime);
    }

    public void end(long time_true_spent) {
        this.endTime = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8")); // 记录结束时间戳
        setEndTime(endTime);
        setInterval(time_true_spent); // 转换为秒级时间差
    }


    @NonNull
    public String getDifficulty_description() {
        return difficulty_description;
    }

    public void setDifficulty_description(@NonNull String difficulty_description) {
        this.difficulty_description = difficulty_description;
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
                "\n\t start=" + startTime +
                "\n\t end=" + endTime +
                "\n\t id=" + id +
                "\n\t height=" + height +
                "\n\t width=" + width +
                "\n\t mineCount=" + mineCount +
                "\n\t difficulty_description='" + difficulty_description + '\'' +
                '}';
    }
}
