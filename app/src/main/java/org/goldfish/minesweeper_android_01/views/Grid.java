package org.goldfish.minesweeper_android_01.views;
//Grid.java

import static android.widget.Toast.LENGTH_SHORT;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.content.ContextCompat;
import androidx.room.Entity;
import androidx.room.Ignore;

import org.goldfish.minesweeper_android_01.MainApplication;
import org.goldfish.minesweeper_android_01.R;
import org.goldfish.minesweeper_android_01.logic.MineTriggeredException;
import org.goldfish.minesweeper_android_01.utils.Resources;
import org.goldfish.minesweeper_android_01.utils.VibrationTypes;
import org.goldfish.minesweeper_android_01.views.activities.GameActivity;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

/**
 * {@code Grid} 游戏的基本单位 <br/>
 * 用于表示游戏中的一个格子
 */
@SuppressLint("ViewConstructor")
@Entity(primaryKeys = {"row", "col"})
public class Grid
//        extends AppCompatImageButton
        implements VibrationTypes, Comparable<Grid> {

    @Setter
    @Getter
    private int row = 0;


    @Setter
    @Getter
    private int col = 0;
    private STATE state;
    //    @Nullable
    private boolean mine;

    public void setState(@NonNull STATE state) {
        this.state = state;
    }

    public void setMine(boolean mine) {
        this.mine = mine;
    }

    @Ignore
    private int surroundingMines;

    @Ignore
    @Nullable
    private DisplayGrid displayGrid;

    public Grid() {
    }

    public Grid(@NonNull GameActivity activity, int row, int col) {
        setDisplayGrid(activity);
        this.row = row;
        this.col = col;
        this.mine = false;

        this.state = STATE.CLOSE;
        this.surroundingMines = 0;

        activity.submitGridOpenAnimation(this);
    }

    public void setDisplayGrid(@NonNull GameActivity activity) {
        displayGrid = new DisplayGrid(activity);
    }

    @Nullable
    public ImageButton getDisplayGrid() {
        return displayGrid;
    }

    public boolean isMine() {
        return Boolean.TRUE.equals(mine);
    }

    @NonNull
    public STATE getState() {
        return state == null ? STATE.CLOSE : state;
    }

    @NonNull
    public Set<Grid> getNeighbors() {
        return displayGrid != null ? displayGrid.neighbors : new LinkedHashSet<>();
    }

    public boolean addNeighbor(@NonNull Grid neighbor) {
        return displayGrid != null && displayGrid.neighbors.add(neighbor);
    }

    public boolean setMine() {
        if (mine) return false;
        mine = true;
        surroundingMines = -1;
        return true;
    }

    @Nullable
    public Drawable getIcon() {
        return displayGrid != null ? displayGrid.icon : null;
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
                if (displayGrid != null) {
                    displayGrid.activity.getController().addFinished(this);
                }
                break CHECK_TRIGGERED;
            }
            if (reveal) break CHECK_TRIGGERED;
            throw new MineTriggeredException("Mine triggered.");
        }

        Log.d("open", toString());
        state = STATE.OPEN;
        if (displayGrid == null) return;
        displayGrid.activity.getController().addFinished(this);
//        updateState();
        displayGrid.activity.submitGridOpenAnimation(this);
    }

    public boolean flag() {
        MainApplication.vibrate(CLICK);
        if (displayGrid == null) return false;
        displayGrid.activity.submitGridOpenAnimation(this, true);
        if (state == STATE.OPEN || state == null) return false;
        switch (state) {
            case FLAG:
                this.state = STATE.CLOSE;
                break;
            case CLOSE:
                state = STATE.FLAG;
                break;
        }

        displayGrid.activity.getController().updateProgress();
        displayGrid.activity.submitGridOpenAnimation(this);
        return true;
    }

    @NonNull
    @Override
    public String toString() {
        char state;
        char mine;
        if (this.state == null)
            return "";
        if (this.mine) {
            mine = '#';
        } else {
            mine = (char) ('0' + surroundingMines);
        }
        state = (displayGrid == null) ? '!' : switch (this.state) {
            case CLOSE -> '-';
            case FLAG -> '>';
            case OPEN -> '+';
        };
        return "" + mine + state;
    }


    public void countSurroundings() {
        surroundingMines = 0;
        if (displayGrid == null) return;
        for (Grid neighbor : displayGrid.neighbors) {
            if (neighbor.mine) surroundingMines++;
        }
    }

    public int getSurroundingMines() {
        return surroundingMines;
    }

    public void prepared() {
        if (displayGrid == null) return;
        displayGrid.setOnLongClickListener((v) -> flag());
        displayGrid.setOnClickListener(v -> {
            try {
                displayGrid.activity.submitGridOpenAnimation(this, true);
                displayGrid.activity.getController().open(Grid.this, state == STATE.OPEN);
            } catch (MineTriggeredException e) {
                displayGrid.activity.getController().lose();
            }
        });
        displayGrid.setLongClickable(true);

    }

    public void updateDisplay() {
        synchronized (this) {
            if (displayGrid != null) {
                displayGrid.updateDisplay();
            }
        }
    }

    @Override
    public int compareTo(Grid o) {
        if (displayGrid == null || o.displayGrid == null) return 0;
        int size = displayGrid.activity.getController().getResult().getHeight();
        return Integer.compare(o.getCol() + o.getRow() * size, getCol() + getRow() * size);
    }

    public enum STATE {
        CLOSE, FLAG, OPEN
    }

    protected class DisplayGrid extends AppCompatImageButton {
        private String TAG = getClass().toString();
        private Drawable icon = null;
        private final Set<Grid> neighbors;
        private final Integer[] surroundingMinesResourceIDs;
        private final int SIZE = 100;
        @NonNull
//        @Ignore
        public final GameActivity activity;

        @Override
        public void setImageDrawable(@Nullable Drawable drawable) {
            super.setImageDrawable(drawable);
            this.icon = drawable;
        }

        public DisplayGrid(@NonNull GameActivity activity) {
            super(activity);
            this.activity = activity;
            neighbors = new HashSet<>();
            surroundingMinesResourceIDs = Resources.drawables;
            GridLayout.LayoutParams params = new GridLayout.LayoutParams(new ViewGroup.LayoutParams(SIZE, SIZE));
            params.setMargins(2, 2, 2, 2);
            setLayoutParams(params);
            setPadding(0, 0, 0, 0);
            setCropToPadding(true);
            setBackgroundColor(ContextCompat.getColor(activity,R.color.grid_closed));

            roundCorner(activity);

            setAdjustViewBounds(true);

            setOnClickListener(v -> {
                try {
                    activity.getController().generateMine(Grid.this);
                } catch (MineTriggeredException e) {
                    Log.w("Controller:generateMine", "MineTriggeredException");
                    Toast.makeText(activity, "内部错误0x0001", LENGTH_SHORT).show();
                    activity.finish();
                }
            });
        }

        private void roundCorner(@NonNull GameActivity activity) {
            roundCorner(activity, Color.GRAY);
        }

        private void roundCorner(@NonNull GameActivity activity, @ColorInt int color) {
            // 在DisplayGrid构造函数中添加以下代码：
            float cornerRadiusDp = 8; // 圆角半径（dp）
            float density = activity.getResources().getDisplayMetrics().density;
            int cornerRadiusPx = (int) (cornerRadiusDp * density + 0.5f);

            GradientDrawable shape = new GradientDrawable();
            shape.setShape(GradientDrawable.RECTANGLE);
            shape.setCornerRadius(cornerRadiusPx); // 设置圆角半径
            shape.setColor(color); // 原背景颜色
            setBackground(shape); // 应用背景
        }


        public void updateDisplay() {
            switch (state) {
                case FLAG:
                    setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.flag));
                    roundCorner(activity,ContextCompat.getColor(activity,R.color.grid_closed));
                    return;
                case CLOSE:
                    Log.v("Grid::updateState", "Not opened.");
                    setImageDrawable(null);
                    roundCorner(activity,ContextCompat.getColor(activity,R.color.grid_closed));
                    return;
            }
            // now left opened grids
            roundCorner(activity,Color.CYAN);
            if (Boolean.TRUE.equals(mine)) {
                // this only happens when the game is over
                setImageResource(R.drawable.exploded);
                return;
            }
            if (surroundingMines == 0) return;

            // now left opened grids with surrounding mines
            int resID = surroundingMinesResourceIDs[surroundingMines];
            Drawable drawable = ContextCompat.getDrawable(activity, resID);

            if (drawable == null) {
                Log.e(TAG, "updateDisplay: drawable is null");
            } else {
                DisplayGrid.this.setImageDrawable(drawable);
            }

//            setImageDrawable(drawable);
            Log.v(TAG, "Setting image resource to ImageButton.");
            setBackgroundColor(Color.TRANSPARENT);
            setVisibility(VISIBLE);
            ViewGroup.LayoutParams viewParams = new ViewGroup.LayoutParams(SIZE, SIZE);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams(viewParams);
            params.setMargins(0, 0, 0, 0);
            setLayoutParams(params);
            setPadding(0, 0, 0, 0);
        }
    }
}
