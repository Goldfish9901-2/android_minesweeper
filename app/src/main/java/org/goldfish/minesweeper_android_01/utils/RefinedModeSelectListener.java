package org.goldfish.minesweeper_android_01.utils;

import android.app.Activity;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import org.goldfish.minesweeper_android_01.persistance.entity.GameInfo;
import org.goldfish.minesweeper_android_01.persistance.entity.ResultFieldNames;

public class RefinedModeSelectListener implements View.OnClickListener, ResultFieldNames {
    private final GameInfo gameInfo;
    Activity activity;
    AlertDialog.Builder builder;

    public RefinedModeSelectListener(
            @NonNull GameInfo gameInfo,
            @NonNull Activity activity) {
        this.gameInfo = gameInfo;
        this.activity = activity;
        this.builder = new AlertDialog.Builder(activity);
    }

    @Override
    public void onClick(View v) {
        gameInfo.putExtra(HEIGHT, gameInfo.getHeight());
        gameInfo.putExtra(WIDTH, gameInfo.getWidth());
        gameInfo.putExtra(MINE_COUNT, gameInfo.getMineCount());
        gameInfo.putExtra(DIFFICULTY_DESCRIPTION, gameInfo.getDifficultyDescription());
        builder.setTitle("确认你的难度");
        builder.setMessage("模式:" + gameInfo.getDifficultyDescription() + "\n" +
                "雷区高度: " + gameInfo.getHeight() + "\n" +
                "雷区宽度: " + gameInfo.getWidth() + "\n" +
                "雷个数: " + gameInfo.getMineCount());
        builder.setPositiveButton("确认", (dialog, which) -> {
            activity.startActivity(gameInfo);
        });
        builder.setNegativeButton("取消", (dialog, which) -> {
        });
        builder.show();
    }

}

