package org.goldfish.minesweeper_android_01.views;
//Grid.java

import static android.widget.Toast.LENGTH_SHORT;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.content.ContextCompat;
import androidx.room.Entity;
import androidx.room.Ignore;

import org.goldfish.minesweeper_android_01.MainApplication;
import org.goldfish.minesweeper_android_01.R;
import org.goldfish.minesweeper_android_01.utils.Resources;
import org.goldfish.minesweeper_android_01.utils.VibrationTypes;
import org.goldfish.minesweeper_android_01.logic.MineTriggeredException;
import org.goldfish.minesweeper_android_01.views.activities.GameActivity;

import java.util.HashSet;
import java.util.Set;

/**
 * {@code Grid} 游戏的基本单位 <br/>
 * 用于表示游戏中的一个格子
 */
@SuppressLint("ViewConstructor")
@Entity(primaryKeys = {"row", "column"})
public class Grid extends AppCompatImageButton
        implements VibrationTypes, Comparable<Grid> {
    //info for controller only
    private int row;
    private int col;
    private STATE state;
    private boolean mine;
    private int surroundingMines;
    @Ignore
    private Drawable icon = null;
    @Ignore
    private final Set<Grid> neighbors;
    @Ignore
    private final Integer[] surroundingMinesResourceIDs;
    @Ignore
    private final int SIZE = 100;
    @NonNull
    @Ignore
    public GameActivity activity;


    public Grid(@NonNull GameActivity activity, int row, int col) {
        super(activity);
        this.row = row;
        this.col = col;
        this.mine = false;

        this.state = STATE.CLOSE;
        this.neighbors = new HashSet<>();
        this.surroundingMines = 0;
        this.activity = activity;
        this.surroundingMinesResourceIDs = Resources.drawables;
        GridLayout.LayoutParams params = new GridLayout.LayoutParams(new ViewGroup.LayoutParams(SIZE, SIZE));
        params.setMargins(2, 2, 2, 2);
        setLayoutParams(params);
        setPadding(0, 0, 0, 0);

        setBackgroundColor(Color.GRAY);

        setAdjustViewBounds(true);

        setOnClickListener(v -> {
            try {
                this.activity.getController().generateMine(this);
            } catch (MineTriggeredException e) {
                Log.w("Controller:generateMine", "MineTriggeredException");
                Toast.makeText(activity, "内部错误0x0001", LENGTH_SHORT).show();
                activity.finish();
            }
        });
//        updateState();
        activity.submitGridOpenAnimation(this);

    }

    public boolean isMine() {
        return mine;
    }

    @NonNull
    public STATE getState() {
        return state;
    }

    @NonNull
    public Set<Grid> getNeighbors() {
        return neighbors;
    }

    public boolean addNeighbor(@NonNull Grid neighbor) {
        return neighbors.add(neighbor);
    }

    public boolean setMine() {
        if (mine) return false;
        mine = true;
        surroundingMines = -1;
//        updateState();
        return true;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void open() throws MineTriggeredException {
        open(false);
    }

    public void open(boolean reveal) throws MineTriggeredException {
        if (state == STATE.FLAG) {
            return;
        }
        CHECK_TRIGGERED:
        {
            if (!isMine()) {
                activity.getController().addFinished(this);
                break CHECK_TRIGGERED;
            }
            if (reveal) break CHECK_TRIGGERED;
            throw new MineTriggeredException("Mine triggered.");
        }

        Log.d("open", toString());
        state = STATE.OPEN;
        activity.getController().addFinished(this);
//        updateState();
        activity.submitGridOpenAnimation(this);
    }

    public boolean flag() {
        MainApplication.vibrate(CLICK);
        if (state == STATE.OPEN)
            return false;
        switch (state) {
            case FLAG:
                this.state = STATE.CLOSE;
                break;
            case CLOSE:
                state = STATE.FLAG;
                break;
        }
        activity.getController().updateProgress();
        activity.submitGridOpenAnimation(this);
        return true;
    }

    @NonNull
    @Override
    public String toString() {
        char state;
        char mine;
        if (this.mine) {
            mine = '#';
        } else {
            mine = (char) ('0' + surroundingMines);
        }
        state = switch (this.state) {
            case CLOSE -> '-';
            case FLAG -> '>';
            case OPEN -> '+';
        };
        return "" + mine + state;
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        super.setImageDrawable(drawable);
        this.icon = drawable;
    }

    @Nullable
    public Drawable getIcon() {
        return icon;
    }

    public void countSurroundings() {
        surroundingMines = 0;
        for (Grid neighbor : neighbors) {
            if (neighbor.mine) surroundingMines++;
        }
    }

    public int getSurroundingMines() {
        return surroundingMines;
    }

    public void updateDisplay() {
        switch (state) {
            case FLAG:
                setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.flag));
                setBackgroundColor(Color.GRAY);
                return;
            case CLOSE:
                Log.v("Grid::updateState", "Not opened.");
                setImageDrawable(null);
                setBackgroundColor(Color.GRAY);
                return;
        }
        // now left opened grids
        setBackgroundColor(Color.CYAN);
        if (mine) {
            // this only happens when the game is over
            setImageResource(R.drawable.exploded);
            return;
        }
        if (surroundingMines == 0) return;

        // now left opened grids with surrounding mines
        setImageDrawable(ContextCompat.getDrawable(activity, surroundingMinesResourceIDs[surroundingMines]));
        Log.v("Grid::updateState", "Setting image resource to ImageButton.");
        setBackgroundColor(Color.TRANSPARENT);
        setVisibility(VISIBLE);
        ViewGroup.LayoutParams viewParams = new ViewGroup.LayoutParams(SIZE, SIZE);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams(viewParams);
        params.setMargins(0, 0, 0, 0);
        setLayoutParams(params);
        setPadding(0, 0, 0, 0);
    }

    public void prepared() {
        setOnLongClickListener((v) -> flag());
        setOnClickListener(new ClickListener());
        setLongClickable(true);

    }

    @Override
    public int compareTo(Grid o) {
        int size = activity.getController().getResult().getHeight();
        return Integer.compare(
                o.getCol() + o.getRow() * size,
                getCol() + getRow() * size
        );
    }

    public enum STATE {
        CLOSE, FLAG, OPEN
    }

    private class ClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
//            MainApplication.vibrate(TICK);
            try {
                activity.getController().open(Grid.this, state == STATE.OPEN);
            } catch (MineTriggeredException e) {
                activity.getController().lose();
            }
        }
    }
}
