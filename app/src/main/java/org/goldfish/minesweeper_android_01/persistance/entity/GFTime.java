package org.goldfish.minesweeper_android_01.persistance.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * {@code GFTime} 游戏时间信息 <br/>
 * 因为SQLite不支持{@link java.time.LocalDateTime}类型<br/>
 * 所以使用{@link GFTime}来存储时间信息<br/>
 * 用于记录游戏开始时间和结束时间<br/>
 */
@Entity
public class GFTime implements Comparable<GFTime> {
    @Ignore
    final ZoneOffset offset = ZoneOffset.ofHours(8);
    @Ignore
    private LocalDateTime time;
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;

    /**
     * 无参构造函数<br/>
     * 生成当前时间戳 但不填入字段<br/>
     * 用于数据库操作<br/>
     * 参见：<br/>
     * {@link java.time.LocalDateTime#now()} <br/>
     * {@link GFTime#GFTime(TIME_SOURCE)}<br/>
     */
    public GFTime() {
        this(TIME_SOURCE.FROM_FIELDS);
    }

    /**
     * 有参构造函数
     *
     * @param generateFromFields {@link java.lang.Boolean} 是否从字段生成<br/>
     *                           <p>
     *                           {@code true} 逐字段指定时间 从字段生成<br/>
     *                           {@code false} 记录当前时间戳 写入字段<br/>
     */
    public GFTime(TIME_SOURCE generateFromFields) {
        //如果是从字段生成 则不按时间戳来决定字段的值
        //而是先填入字段 再生成时间戳
        if (generateFromFields == TIME_SOURCE.FROM_FIELDS) return;

        time = LocalDateTime.now();
        setYear(time.getYear());
        setMonth(time.getMonthValue());
        setDay(time.getDayOfMonth());
        setHour(time.getHour());
        setMinute(time.getMinute());
        setSecond(time.getSecond());

    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    @NonNull
    public LocalDateTime getTime() {
        if (time == null) generateTime();
        return time;
    }

    public void generateTime() {
        time = LocalDateTime.of(year, month, day, hour, minute, second);
    }

    @NonNull
    @Override
    public String toString() {
        String stringBuffer = month + "/" +
                day + "-" +
                hour + ":" +
                minute + ":" +
                second;
        return stringBuffer;
    }


    public long subtract(@NonNull GFTime past_time) {
        LocalDateTime time_to_compare = past_time.getTime();
        long latter_stamp = getTime().toEpochSecond(offset);
        long former_stamp = time_to_compare.toEpochSecond(offset);
        return latter_stamp - former_stamp;
    }

    @Override
    public int compareTo(GFTime o) {
        return Long.compare(0L, subtract(o));
    }

    public enum TIME_SOURCE {
        FROM_FIELDS,
        FROM_SYSTEM
    }


}
