package org.goldfish.minesweeper_android_01;

import android.app.Application;
import android.content.Context;

import android.os.VibrationEffect;
import android.os.Vibrator;

import androidx.room.Room;

import org.goldfish.minesweeper_android_01.persistance.dao.RecordDAO;
import org.goldfish.minesweeper_android_01.persistance.database.RecordDatabase;

import java.util.ArrayList;
import java.util.List;


public class MainApplication extends Application {

    public static final String TAG = "Minesweeper";
    private static MainApplication instance = null;
    private RecordDAO dao;

    private List<Integer> validEffectIds;
    private Vibrator vibrator;

//    private ExecutorService executorService;

    public static MainApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        RecordDatabase database = Room.databaseBuilder(this, RecordDatabase.class, "minesweeper.db")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

        dao = database.dao();
        try {
            vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            if (!vibrator.hasVibrator())
                throw new AssertionError("vibration.hasVibrator failed");
        } catch (Throwable ignored) {
            vibrator = null;
        }
        validEffectIds = new ArrayList<>();
        validEffectIds.add(VibrationEffect.EFFECT_TICK);
        validEffectIds.add(VibrationEffect.EFFECT_CLICK);
        validEffectIds.add(VibrationEffect.EFFECT_HEAVY_CLICK);
    }

    public static void vibrate(int effectId) {
        Vibrator vibrator = getInstance().vibrator;
        if (vibrator == null)
            return;
        if (getInstance().validEffectIds.stream().noneMatch(id->id==effectId))
            return;
        vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK));
    }


    public RecordDAO getDao() {
        return dao;
    }
}
