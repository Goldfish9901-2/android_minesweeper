package org.goldfish.minesweeper_android_01.persistance.views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bin.david.form.annotation.SmartColumn;
import com.bin.david.form.annotation.SmartTable;

import org.goldfish.minesweeper_android_01.persistance.entity.GameInfo;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * entity built from {@link GameInfo} for smart table framework display<br>
 * most of its fields are just for reflection and can be left null
 */

@SmartTable(name = "概况")
public final class GameInfoSummary extends GameInfo {
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

    public GameInfoSummary(@NonNull GameInfo gameInfo) {
        difficulty_description = gameInfo.getDifficulty_description();
        mineCount = gameInfo.getMineCount();
        mineSize = gameInfo.getHeight() > 0 && gameInfo.getWidth() > 0
                ? String.format(Locale.CHINA, "%d*%d",
                gameInfo.getHeight(), gameInfo.getWidth())
                : "未知大小";
        startTime = LocalDateTime.ofEpochSecond(gameInfo.getStartTime(), 0, ZoneOffset.of("+8"));
        endTime = LocalDateTime.ofEpochSecond(gameInfo.getEndTime(), 0, ZoneOffset.of("+8"));
        interval = gameInfo.getInterval();
    }

    @NonNull
    public static List<GameInfoSummary> summaries(@NonNull List<GameInfo> raws) {
        List<GameInfoSummary> summaries = new ArrayList<>();
        raws.forEach(
                raw -> summaries.add(new GameInfoSummary(raw))
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
