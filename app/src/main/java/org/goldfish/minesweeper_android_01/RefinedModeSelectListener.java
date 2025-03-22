package org.goldfish.minesweeper_android_01;

import android.app.Activity;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import org.goldfish.minesweeper_android_01.persistance.entity.Result;
import org.goldfish.minesweeper_android_01.persistance.entity.ResultFieldNames;

public class RefinedModeSelectListener implements View.OnClickListener, ResultFieldNames {
    private final Result result;
    Activity activity;
    AlertDialog.Builder builder;

    public RefinedModeSelectListener(
            Result result,
            Activity activity) {
        this.result = result;
        this.activity = activity;
        this.builder = new AlertDialog.Builder(activity);
    }

    @Override
    public void onClick(View v) {
        result.putExtra(HEIGHT, result.getHeight());
        result.putExtra(WIDTH, result.getWidth());
        result.putExtra(MINE_COUNT, result.getMineCount());
        result.putExtra(DIFFICULTY_DESCRIPTION, result.getDifficultyDescription());
        builder.setTitle("确认你的难度");
        builder.setMessage("模式:" + result.getDifficultyDescription() + "\n" +
                "雷区高度: " + result.getHeight() + "\n" +
                "雷区宽度: " + result.getWidth() + "\n" +
                "雷个数: " + result.getMineCount());
        builder.setPositiveButton("确认", (dialog, which) -> {
            activity.startActivity(result);
        });
        builder.setNegativeButton("取消", (dialog, which) -> {
        });
        builder.show();
    }

}

