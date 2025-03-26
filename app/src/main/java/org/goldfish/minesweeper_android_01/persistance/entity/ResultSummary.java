package org.goldfish.minesweeper_android_01.persistance.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bin.david.form.annotation.SmartColumn;
import com.bin.david.form.annotation.SmartTable;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * entity built from {@link Result} for smart table framework display<br>
 * most of its fields are just for reflection and can be left null
 */

@SmartTable(name = "概况")
public final class ResultSummary extends Result {
    @SmartColumn(id = 2, name = "雷区大小")
    @Nullable
    private String mineSize;
    @SmartColumn(id = 3, name = "雷数")
    private int mineCount;
    @SmartColumn(id = 1, name = "难度")
    private String difficulty_description;

    /**
     * 开始时间
     */
    @SmartColumn(id = 4, name = "开始时间")
    private LocalDateTime startTime;

    @SmartColumn(id = 5, name = "结束时间")
    private LocalDateTime endTime;
    @SmartColumn(id = 6, name = "耗时")
    private long interval;

    public ResultSummary(@NonNull Result result) {
        difficulty_description = result.getDifficulty_description();
        mineCount = result.getMineCount();
        mineSize = result.getHeight() > 0 && result.getWidth() > 0
                ? String.format(Locale.CHINA, "%d*%d",
                result.getHeight(), result.getWidth())
                : "未知大小";
        startTime = LocalDateTime.ofEpochSecond(result.getStartTime(), 0, ZoneOffset.of("+8"));
        endTime = LocalDateTime.ofEpochSecond(result.getEndTime(), 0, ZoneOffset.of("+8"));
        interval = result.getInterval();
    }

    @NonNull
    public static List<ResultSummary> summaries(@NonNull List<Result> raws) {
        List<ResultSummary> summaries = new ArrayList<>();
        raws.forEach(
                raw -> summaries.add(new ResultSummary(raw))
        );
        return summaries;
    }

    @NonNull
    public String getMineSize() {
        return mineSize;
    }

    @Override
    public int getMineCount() {
        return mineCount;
    }

    @NonNull
    @Override
    public String getDifficulty_description() {
        return difficulty_description;
    }


    public long getStartTime() {
        return super.startTime;
    }

    @Override
    public long getInterval() {
        return interval;
    }
}
