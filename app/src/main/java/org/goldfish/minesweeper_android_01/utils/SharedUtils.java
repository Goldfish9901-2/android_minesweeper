package org.goldfish.minesweeper_android_01.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.goldfish.minesweeper_android_01.MainApplication;
import org.goldfish.minesweeper_android_01.persistance.dao.GameCacheDAO;
import org.goldfish.minesweeper_android_01.persistance.entity.GameInfo;

import java.util.Objects;

public class SharedUtils {

    private static final String TAG = SharedUtils.class.toString();
    private static final String STARTED = "started";
    private static final String MINES_COUNT = "goldfish_minesweeper_mines_count";
    private static final String WIDTH = "goldfish_minesweeper_width";
    private static final String HEIGHT = "goldfish_minesweeper_height";
    private static final String DIFFICULTY = "goldfish_minesweeper_difficulty";

    private static final String TIME_SPENT = "goldfish_minesweeper_time_spent";

    public static void startGame() {
        if (gameInfoPrepared())
            editor().putBoolean(STARTED, true).apply();
    }

    public static boolean isStarted() {
        try {
            return preferences().getBoolean(STARTED, false) && gameInfoPrepared();
        } catch (ClassCastException e) {
            return false;
        }
    }

    public static void end() {
        MainApplication.getInstance().getGameCacheDAO().deleteCache();
        editor().clear();
        editor().putBoolean(STARTED, false).apply();
    }

    private static SharedPreferences preferences() {
        return MainApplication.getInstance().getSharedPreferences(
                MainApplication.getInstance().getPackageName(),
                Context.MODE_PRIVATE
        );
    }

    private static SharedPreferences.Editor editor() {
        return preferences().edit();
    }

    public static void saveGameInfo(@NonNull GameInfo gameInfo) {
        editor().putInt(WIDTH, gameInfo.getWidth())
                .putInt(HEIGHT, gameInfo.getHeight())
                .putInt(MINES_COUNT, gameInfo.getMineCount())
                .putString(DIFFICULTY, gameInfo.getDifficulty_description())
                .putLong(TIME_SPENT, gameInfo.getInterval())
                .apply();
    }

    public static boolean gameInfoPrepared() {
        try {
            return isStarted() &&
                    preferences().getInt(WIDTH, 0) != 0 &&
                    preferences().getInt(HEIGHT, 0) != 0 &&
                    preferences().getInt(MINES_COUNT, 0) != 0 &&
                    !preferences().getString(DIFFICULTY, "").isEmpty() &&
                    preferences().getLong(TIME_SPENT, 0) != 0;
        } catch (RuntimeException e) {
            return false;
        }
    }

    @Nullable
    public static GameInfo loadGameInfo(@NonNull Activity activity) {
        int width, height, mines;
        long time_spent;
        String difficulty;
        try {
            if (!isStarted())
                throw new IllegalStateException("no saved game");
            if ((width = preferences().getInt(WIDTH, 0)) == 0)
                throw new IllegalStateException("unable to load saved width");
            if ((height = preferences().getInt(HEIGHT, 0)) == 0)
                throw new IllegalStateException("unable to load saved height");
            if ((mines = preferences().getInt(MINES_COUNT, 0)) == 0)
                throw new IllegalStateException("unable to load saved mines");
            if ((time_spent = preferences().getLong(TIME_SPENT, 0)) == 0)
                throw new IllegalStateException("unable to load saved time spent");
            if ((difficulty = preferences().getString(DIFFICULTY, "")).isEmpty())
                throw new IllegalStateException("unable to load saved difficulty");
            GameCacheDAO dao = MainApplication.getInstance().getGameCacheDAO();
            for (int r = 0; r < height; r++) {
                for (int c = 0; c < width; c++) {
                    Objects.requireNonNull(
                            dao.getGridByAbsoluteLocation(
                                    r, c
                            ));
                }
            }
        } catch (RuntimeException e) {
            Log.w(TAG, "loadGameInfo: ", e);
            end();
            return null;
        }
        GameInfo gameInfo = new GameInfo(activity);
        gameInfo.setWidth(width);
        gameInfo.setHeight(height);
        gameInfo.setMineCount(mines);
        gameInfo.setDifficulty_description(difficulty);
        gameInfo.setInterval(time_spent);
        return gameInfo;
    }
}
