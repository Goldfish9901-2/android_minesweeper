package org.goldfish.minesweeper_android_01.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;

@Entity
public class GFTime {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @Ignore
    private final LocalDateTime time;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;

    public GFTime() {
        time = LocalDateTime.now();
        setMonth(time.getMonthValue());
        setDay(time.getDayOfMonth());
        setHour(time.getHour());
        setMinute(time.getMinute());
        setSecond(time.getSecond());
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

    public LocalDateTime getTime() {
        return time;
    }
}
