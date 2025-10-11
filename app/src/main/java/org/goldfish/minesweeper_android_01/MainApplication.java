package org.goldfish.minesweeper_android_01;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.VibratorManager;

import androidx.annotation.NonNull;
import androidx.room.Room;

import org.goldfish.minesweeper_android_01.logic.Controller;
import org.goldfish.minesweeper_android_01.persistance.dao.GameCacheDAO;
import org.goldfish.minesweeper_android_01.persistance.dao.RecordDAO;
import org.goldfish.minesweeper_android_01.persistance.database.GameCacheDatabase;
import org.goldfish.minesweeper_android_01.persistance.database.RecordDatabase;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;


public class MainApplication extends Application {

    public static final String TAG = MainApplication.class.toString();
    private static MainApplication instance = null;

    static {
        System.loadLibrary("minesweeper_android_01");
    }

    @Getter
    private RecordDAO recordDAO;
    @Getter
    private GameCacheDAO gameCacheDAO;

    private final List<Integer> validEffectIds;
    private Vibrator vibrator;

    public MainApplication() {
        validEffectIds = List.of(
                VibrationEffect.EFFECT_CLICK,
                VibrationEffect.EFFECT_TICK,
                VibrationEffect.EFFECT_HEAVY_CLICK
        );
    }

    @NonNull
    public static MainApplication getInstance() {
        return instance;
    }

    @Override
    @SuppressLint({"-Xlint:deprecated"})
    public void onCreate() {
        super.onCreate();
        instance = this;
        RecordDatabase recordDatabase = Room
                .databaseBuilder(this, RecordDatabase.class, "minesweeper.db")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigrationOnDowngrade(true)
                .build();
        GameCacheDatabase gameCacheDatabase = Room
                .databaseBuilder(this, GameCacheDatabase.class, "game_cache.db")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration(true)
                .build();
        gameCacheDAO = gameCacheDatabase.dao();


        recordDAO = recordDatabase.dao();
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                // Android 12 及以上：通过 VibratorManager 获取
                VibratorManager vibratorManager = (VibratorManager) getSystemService(Context.VIBRATOR_MANAGER_SERVICE);
                vibrator = vibratorManager.getDefaultVibrator();
            } else {
                // 旧版本：继续使用旧方法（需 @SuppressLint 注解忽略弃用警告）
                vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            }
        } catch (Throwable ignored) {
            vibrator = null;
        }

    }

    public static void vibrate(int effectId) {
        Vibrator vibrator = getInstance().vibrator;
        if (vibrator == null)
            return;
        if (getInstance().validEffectIds.stream().noneMatch(id -> id == effectId))
            return;
        vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK));
    }


}
