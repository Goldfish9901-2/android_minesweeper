package org.goldfish.minesweeper_android_01.views.activities;
//GameActivity.java

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Chronometer;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.goldfish.minesweeper_android_01.R;
import org.goldfish.minesweeper_android_01.persistance.entity.Result;
import org.goldfish.minesweeper_android_01.persistance.entity.ResultFieldNames;
import org.goldfish.minesweeper_android_01.logic.Controller;
import org.goldfish.minesweeper_android_01.views.Grid;

import java.util.Locale;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * {@code GameActivity} 游戏界面 <br/>
 */
public class GameActivity extends AppCompatActivity implements ResultFieldNames {
    @ColorInt
    private int opening_color;

    private Controller controller;

    private TextView minePrompt;

    private Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);

        Intent intent = getIntent();
        GridLayout layout;

        int height = intent.getIntExtra(HEIGHT, 0);
        int width = intent.getIntExtra(WIDTH, 0);
        int mines = intent.getIntExtra(MINE_COUNT, 0);
        if (height == 0 || width == 0 || mines == 0) {
            Toast.makeText(this, "无法获取难度信息", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        String difficulty_description = intent.getStringExtra(DIFFICULTY_DESCRIPTION);
        singleDelay = 300 / (height * width);
        Result mode;
        try {
            mode = (Result) intent;
            Objects.requireNonNull(mode);
        } catch (RuntimeException exception) {
            Toast.makeText(this, "无法自动获取游戏属性", Toast.LENGTH_SHORT).show();
            mode = new Result();
            mode.setHeight(height);
            mode.setWidth(width);
            mode.setMineCount(mines);
            mode.setDifficulty_description(difficulty_description);
        }

        controller = new Controller(mode);
        controller.setActivity(this);

        Toast.makeText(this, String.format(Locale.CHINA, "模式: %s 高度: %d, 宽度: %d, 雷数: %d", difficulty_description, height, width, mines), Toast.LENGTH_SHORT).show();
        try {
            handler = new Handler(getMainLooper());

            TextView titleTextView = findViewById(R.id.game_title);
            titleTextView.setText(difficulty_description);

            Chronometer chronometer = findViewById(R.id.goldfish_chronometer);
            chronometer.setBase(0);
            controller.setChronometer(chronometer);

            minePrompt = findViewById(R.id.mine_counter);
            minePrompt.setText(String.valueOf(mines));

            layout = findViewById(R.id.grids_field);
            layout.setColumnCount(width);
            layout.setRowCount(height);

            findViewById(R.id.exit_button).setOnClickListener(
                    v -> Controller.promptAndExit(this));
            findViewById(R.id.restart_button).setOnClickListener(
                    v -> finish());

            opening_color = getResources().getColor(R.color.opening, this.getTheme());

        } catch (NullPointerException nullPointerException) {
            Toast.makeText(this, nullPointerException.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "有组件无法定位", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        for (int num = 0; num < width * height; num++) {
            Grid button = new Grid(this, num / width, num % width);
            controller.add(button);
            layout.addView(button);
        }
        controller.findSurroundings();
    }

    @NonNull
    public Controller getController() {
        return controller;
    }

    @NonNull
    public TextView getMinePrompt() {
        return minePrompt;
    }

    // In GameActivity.java
    private int singleDelay;

    private final Queue<Grid> displayQueue = new LinkedBlockingDeque<>();

    public void submitGridOpenAnimation(@NonNull Grid grid) {
        submitGridOpenAnimation(grid, false);
    }

    public void submitGridOpenAnimation(@NonNull Grid grid, boolean refresh) {

        if (displayQueue.isEmpty()) {
            displayQueue.add(grid);
            handler.postDelayed(this::displayGridOpenAnimation, singleDelay);
        } else {
            Grid quickRemove;
            if (refresh) {
                while ((quickRemove = displayQueue.poll()) != null) {
                    quickRemove.updateDisplay();
                }
                return;
            }
            displayQueue.add(grid);
        }

    }

    private void displayGridOpenAnimation() {
        try {
            Grid grid = displayQueue.remove();
            if (grid == null) return;
            handler.postDelayed(this::displayGridOpenAnimation, singleDelay);
            if (updated(grid)) return;
            grid.setBackgroundColor(opening_color);
            handler.postDelayed(grid::updateDisplay, singleDelay);
        } catch (RuntimeException ignored) {
        }
    }

    private boolean updated(Grid grid) {
        ColorDrawable colorDrawable;
        try {
            colorDrawable = (ColorDrawable) grid.getBackground();
        } catch (ClassCastException e) {
            colorDrawable = null;
        }
        try {
            switch (grid.getState()) {
                case CLOSE:
                    if (Objects.requireNonNull(colorDrawable).getColor() == Color.GRAY
                            && grid.getIcon() == null
                    )
                        return true;
                    break;
                case OPEN:
                    return Objects.requireNonNull(colorDrawable).getColor() == Color.CYAN
                            || grid.getIcon() != null;
                case FLAG:
                    if (Objects.requireNonNull(colorDrawable).getColor() != Color.GRAY)
                        break;
                    if (grid.getIcon() == null)
                        break;
                    return true;
            }
        } catch (RuntimeException ignored) {

        }
        return false;
    }

}