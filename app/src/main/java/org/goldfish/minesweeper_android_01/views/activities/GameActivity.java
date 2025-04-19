package org.goldfish.minesweeper_android_01.views.activities;
//GameActivity.java

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Chronometer;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import org.goldfish.minesweeper_android_01.MainApplication;
import org.goldfish.minesweeper_android_01.R;
import org.goldfish.minesweeper_android_01.logic.Controller;
import org.goldfish.minesweeper_android_01.logic.SecondsTimer;
import org.goldfish.minesweeper_android_01.persistance.dao.GameCacheDAO;
import org.goldfish.minesweeper_android_01.persistance.entity.GameInfo;
import org.goldfish.minesweeper_android_01.persistance.entity.ResultFieldNames;
import org.goldfish.minesweeper_android_01.utils.SharedUtils;
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
    private Handler displayQueueHandler;
    private SecondsTimer timer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent intent = getIntent();
        GameInfo gameInfo;
        boolean isStarted = SharedUtils.isStarted();
        try {

            gameInfo = isStarted
                    ? Objects.requireNonNull(SharedUtils.loadGameInfo(this))
                    : new GameInfo(this);
            loadIntent(intent, gameInfo);
        } catch (RuntimeException runtimeException) {
            Toast.makeText(this, runtimeException.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // prepare controller
        controller = new Controller(gameInfo);
        controller.setActivity(this);

        Toast.makeText(
                this,
                String.format(
                        Locale.CHINA,
                        "模式: %s 高度: %d, 宽度: %d, 雷数: %d",
                        gameInfo.getDifficultyDescription(), gameInfo.getHeight(),
                        gameInfo.getWidth(), gameInfo.getMineCount()
                ), Toast.LENGTH_SHORT
        ).show();
        try {
            initComponents(gameInfo.getDifficulty_description(), gameInfo.getMineCount());
            initMainLayout(gameInfo.getWidth(), gameInfo.getHeight(), isStarted);
        } catch (NullPointerException nullPointerException) {
            Toast.makeText(this, nullPointerException.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "有组件无法定位", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @NonNull
    public SecondsTimer getTimer() {
        return timer;
    }

    @Override
    protected void onResume() {
        super.onResume();
        timer.start();
    }


    @Override
    protected void onStop() {
        super.onStop();

    }

    private void loadIntent(Intent intent, GameInfo mode) {
        int height = intent.getIntExtra(HEIGHT, 0);
        int width = intent.getIntExtra(WIDTH, 0);
        int mines = intent.getIntExtra(MINE_COUNT, 0);
        if (height == 0 || width == 0 || mines == 0)
            throw new IllegalArgumentException("无法获取难度信息");
        String difficulty_description = intent.getStringExtra(DIFFICULTY_DESCRIPTION);
        difficulty_description = Objects.requireNonNullElse(difficulty_description, "");
        singleDelay = 300f / (height * width);
        // prepare persistence record

        mode.setHeight(height);
        mode.setWidth(width);
        mode.setMineCount(mines);
        mode.setDifficulty_description(difficulty_description);
    }


    private void initMainLayout(int width, int height, boolean cached) {
        GridLayout layout;
        layout = findViewById(R.id.grids_field);
        layout.setColumnCount(width);
        layout.setRowCount(height);
        GameCacheDAO cacheDAO = MainApplication.getInstance().getGameCacheDAO();
        for (int num = 0; num < width * height; num++) {
            int row = num / width;
            int col = num % width;
            Grid button = cached
                    ? cacheDAO.getGridByRelativeLocation(row, col, this)
                    : new Grid(this, num / width, num % width);
            if (button == null) {
                String message = String.format(
                        Locale.CHINA,
                        "unable to load cached button at ( %s, %s ) for size [ %d , %d ]",
                        row, col, height, width
                );
                Log.w(getClass().toString() , "initMainLayout: ", new IllegalStateException(message));
                SharedUtils.end();
                finish();
                return;
            }
            controller.add(button);
            layout.addView(button.getDisplayGrid());
        }
        controller.findSurroundings();
    }

    private void initComponents(String difficulty_description, int mines) {
        displayQueueHandler = new Handler(getMainLooper());
        timer = new SecondsTimer(getMainLooper());

        // init components
        TextView titleTextView = findViewById(R.id.game_title);
        titleTextView.setText(difficulty_description);

        Chronometer chronometer = findViewById(R.id.goldfish_chronometer);
        chronometer.setBase(0);
        controller.setChronometer(chronometer);

        minePrompt = findViewById(R.id.mine_counter);
        minePrompt.setText(String.valueOf(mines));


        findViewById(R.id.exit_button).setOnClickListener(
                v -> Controller.promptAndExit(this));
        findViewById(R.id.restart_button).setOnClickListener(
                v -> finish());

        opening_color = getResources().getColor(R.color.opening, this.getTheme());
    }

    @NonNull
    public Controller getController() {
        return controller;
    }

    @NonNull
    public TextView getMinePrompt() {
        return minePrompt;
    }

    /**
     * indicates the delay between each grid opening
     */
    private float singleDelay;

    private final Queue<Grid> displayQueue = new LinkedBlockingDeque<>();

    public void submitGridOpenAnimation(@NonNull Grid grid) {
        submitGridOpenAnimation(grid, false);
    }

    public void submitGridOpenAnimation(@NonNull Grid grid, boolean refresh) {
        Grid quickRemove;
        if (refresh) {
            while ((quickRemove = displayQueue.poll()) != null) {
                quickRemove.updateDisplay();
            }
            return;
        }
        if (displayQueue.isEmpty()) {
            displayQueue.add(grid);
            displayQueueHandler.postDelayed(this::displayGridOpenAnimation, (long) singleDelay);
        } else {
            displayQueue.add(grid);
        }

    }

    private void displayGridOpenAnimation() {
        try {
            Grid grid = displayQueue.remove();
            if (grid == null) return;
            displayQueueHandler.postDelayed(this::displayGridOpenAnimation, (long) singleDelay);
            if (updated(grid)) return;
            ImageButton button = grid.getDisplayGrid();
            if (button == null) return;
            button.setBackgroundColor(opening_color);
            displayQueueHandler.postDelayed(grid::updateDisplay, (long) singleDelay);
        } catch (RuntimeException ignored) {
        }
    }

    private boolean updated(Grid grid) {
        ColorDrawable colorDrawable;
        try {
            colorDrawable = (ColorDrawable) Objects.requireNonNull(grid.getDisplayGrid()).getBackground();
        } catch (ClassCastException | NullPointerException e) {
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