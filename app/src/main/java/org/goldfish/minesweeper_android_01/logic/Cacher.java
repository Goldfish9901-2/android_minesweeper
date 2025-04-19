package org.goldfish.minesweeper_android_01.logic;

import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;

import org.goldfish.minesweeper_android_01.MainApplication;
import org.goldfish.minesweeper_android_01.persistance.entity.GameInfo;
import org.goldfish.minesweeper_android_01.utils.SharedUtils;
import org.goldfish.minesweeper_android_01.views.Grid;

import java.util.List;

public class Cacher extends Handler {
    Controller controller;

    public Cacher(@NonNull Controller controller) {
        super(controller.activity.getMainLooper());
        this.controller = controller;
    }

    public void cache(@NonNull List<Grid> grid, @NonNull GameInfo gameInfo) {
        grid.forEach(MainApplication.getInstance().getGameCacheDAO()::insert);
        SharedUtils.startGame();
        SharedUtils.saveGameInfo(gameInfo);
        postDelayed(() -> {
            try {
                cache(grid, controller.getResult());
            } catch (Exception e) {
                Log.w(getClass().toString(), "cache: ", e);
            }
        }, 2000);
    }

}
