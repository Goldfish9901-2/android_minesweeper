package org.goldfish.minesweeper_android_01.activities;
//GameActivity.java

import android.content.Intent;
import android.os.Bundle;
import android.widget.Chronometer;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.goldfish.minesweeper_android_01.R;
import org.goldfish.minesweeper_android_01.entity.Result;
import org.goldfish.minesweeper_android_01.entity.ResultFieldNames;
import org.goldfish.minesweeper_android_01.logic.Controller;
import org.goldfish.minesweeper_android_01.logic.Grid;

import java.util.Locale;

public class GameActivity extends AppCompatActivity implements ResultFieldNames {

    private Controller controller;

    private Result mode;

    private TextView minePrompt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);

        Intent intent = getIntent();
        GridLayout layout;
        if (intent instanceof Result) {
            mode = (Result) intent;
        }
        int height = intent.getIntExtra(HEIGHT, 10);
        int width = intent.getIntExtra(WIDTH, 10);
        int mines = intent.getIntExtra(MINE_COUNT, 10);
        String difficulty_description = intent.getStringExtra("difficulty_description");

        controller = new Controller(height, width, mines, difficulty_description);
        controller.setActivity(this);

        Toast.makeText(this, String.format(Locale.CHINA, "模式: %s 高度: %d, 宽度: %d, 雷数: %d", difficulty_description, height, width, mines), Toast.LENGTH_SHORT).show();
        try {
            TextView titleTextView = findViewById(R.id.game_title);
            titleTextView.setText(difficulty_description);

            Chronometer chronometer = findViewById(R.id.goldfish_chronometer);
            chronometer.setBase(0);
            controller.setChronometer(chronometer);

            minePrompt=findViewById(R.id.mine_counter);
            minePrompt.setText(String.valueOf(mines));

            layout = findViewById(R.id.grids_field);
            layout.setColumnCount(width);
            layout.setRowCount(height);

            findViewById(R.id.exit_button).setOnClickListener(
                    v -> Controller.promptAndExit(this));
            findViewById(R.id.restart_button).setOnClickListener(
                    v -> finish());


        } catch (NullPointerException nullPointerException) {
            Toast.makeText(this, nullPointerException.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "有组件无法定位", Toast.LENGTH_SHORT).show();
            return;
        }
        for (int num = 0; num < width * height; num++) {
            Grid button = new Grid(this, num / width, num % width);
            controller.add(button);
            layout.addView(button);
        }
        controller.findSurroundings();
    }

    public Controller getController() {
        return controller;
    }

    public TextView getMinePrompt() {
        return minePrompt;
    }

}