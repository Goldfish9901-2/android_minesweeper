package org.goldfish.minesweeper_android_01.logic;

import static android.widget.Toast.LENGTH_SHORT;

import android.app.Activity;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import org.goldfish.minesweeper_android_01.MainApplication;
import org.goldfish.minesweeper_android_01.persistance.entity.GameInfo;
import org.goldfish.minesweeper_android_01.utils.SharedUtils;
import org.goldfish.minesweeper_android_01.views.Grid;
import org.goldfish.minesweeper_android_01.views.activities.GameActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 * 控制器<br/>
 * 接受点击 长按等事件<br/>
 * 控制游戏的进行
 */

public class Controller {
    public final static String thrower = "GOLDFISH_CAUGHT";
    private final int height;
    private final int width;
    private final int mines;
    private final Grid[][] grids;
    /**
     * Set of finished grids<br/>
     * its size ( i.e. {@link Collection#size()}) is used to determine if the game is finished
     */
    Set<Grid> finishedGrids;
    GameActivity activity;
    Chronometer chronometer;
    CacheManager cacheManager;
    private int used;
    private boolean finished;
    private GameInfo gameInfo;

    @NonNull
    public GameInfo getResult() {
        gameInfo.setInterval(activity.getTimer().getTime());
        return gameInfo;
    }


    public Controller(@NonNull GameInfo gameInfo) {
        this(gameInfo.getHeight(), gameInfo.getWidth(), gameInfo.getMineCount());
        this.gameInfo = gameInfo;
    }

    /**
     * 构造函数
     *
     * @param height 雷区的高度
     * @param width  雷区的宽度
     * @param mines  雷区的雷数
     */


    public Controller(int height, int width, int mines) {
        this.height = height;
        this.width = width;
        this.mines = mines;

        this.used = 0;
        this.grids = new Grid[height][width];
        this.finished = false;
    }

    public static void promptAndExit(@NonNull Activity activity) {
        Toast.makeText(activity, "下次扫雷再见！", LENGTH_SHORT).show();
        activity.finish();
    }

    public boolean isFinished() {
        return finished = (finishedGrids.size() + mines == height * width);
    }

    /**
     * 设置游戏主窗口
     *
     * @param activity 指定的窗口
     */
    public void setActivity(@NonNull GameActivity activity) {
        this.activity = activity;
        this.cacheManager = new CacheManager(this);
    }

    /**
     * 添加格子
     *
     * @param grid 要添加的格子
     */

    public void add(@NonNull Grid grid) {
        if (used >= height * width) throw new RuntimeException("Array Already Full");
        grids[used / width][used % width] = grid;
        used++;
    }

    /**
     * 寻找周围格子
     */

    public void findSurroundings() {
        if (used < width * height) throw new RuntimeException("Array Not Full");
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Grid grid = grids[row][col];
                //在九宫格范围内寻找相邻格
                if (findGridSurrounding(row, col, grid)) return;
            }
        }
    }

    private boolean findGridSurrounding(int row, int col, Grid grid) {
        for (int d_row = -1; d_row <= 1; d_row++) {
            for (int d_col = -1; d_col <= 1; d_col++) {
                if (d_row == 0 && d_col == 0) continue;
                //起点格坐标

                int neighbor_candidate_row = row + d_row;
                int neighbor_candidate_col = col + d_col;
                //候选坐标生成

                if (neighbor_candidate_row < 0) continue;//超上界
                if (neighbor_candidate_row >= height) continue;//超下界
                if (neighbor_candidate_col < 0) continue;//超左界
                if (neighbor_candidate_col >= width) continue;//超右界

                Grid gridCandidate = grids[neighbor_candidate_row][neighbor_candidate_col];
                if (gridCandidate == null) {
                    String messageBuf = "(" + neighbor_candidate_row + "," + neighbor_candidate_col + ')';
                    Log.w("GOLDFISH_SELF_CAUGHT", messageBuf);
                    return true;
                }

                if (grid.addNeighbor(gridCandidate)) continue;

                //添加邻居不成功 则执行以下处理
                if (activity == null) throw new NullPointerException("Call SetActivity first");
                Toast.makeText(activity, "邻接错误", LENGTH_SHORT).show();
                return true;
            }
        }
        return false;
    }

    /**
     * 获取格子
     *
     * @param index 格子的索引 <br/>
     *              从左上角开始 从左到右 从上到下 比如<br/>
     *              0 1 2 3 4 5 6 7 8<br/>
     *              9 10 11 12 13 14 15 16 17<br/>
     * @return 格子
     */
    @Nullable
    public Grid getGrid(int index) {
        return getGrid(index / width, index % width);
    }

    /**
     * @param row 格子的行号
     * @param col 格子的列号
     * @return 格子的对象
     */
    @Nullable
    public Grid getGrid(int row, int col) {
        try {
            return grids[row][col];
        } catch (RuntimeException e) {
            return null;
        }
    }

    /**
     * 添加计时器
     *
     * @param chronometer 计时器
     */
    public void setChronometer(@NonNull Chronometer chronometer)
            throws NullPointerException {
        this.chronometer = Objects.requireNonNull(chronometer);
    }

    /**
     * 生成雷 并开始游戏
     *
     * @param start 用户选中的格子 此格及周围不得为雷
     */

    public void generateMine(@NonNull Grid start) throws MineTriggeredException {
        Log.i("Controller:generateMine",
                "generateMine: " + "<" + start.getRow() + '-' + start.getCol() + '>');
        Set<Grid> invalidGrids = new LinkedHashSet<>();
        invalidGrids.add(start);
        invalidGrids.addAll(start.getNeighbors());

        finishedGrids = new LinkedHashSet<>();
        finishedGrids.add(start);
        finishedGrids.addAll(start.getNeighbors());


        for (int i = 0; i < mines; ) {
            int h = (int) (Math.random() * height);
            int w = (int) (Math.random() * width);
            boolean exist = invalidGrids
                    .stream()
                    .anyMatch(
                            grid ->
                                    grid.getRow() == h &&
                                            grid.getCol() == w
                    );

            if (exist) continue;
            // loop exited because loc generated cannot be set mine
            // so just try generating another one

            Grid candidateMinedGrid = getGrid(h, w);
            if (candidateMinedGrid == null) {
                Log.w(Controller.thrower, "Controller:generateMine: " + h + ',' + w);
                return;
            }
            if (candidateMinedGrid.setMine()) {
                invalidGrids.add(candidateMinedGrid);
                i++;
            }
        }


        for (int index = 0; index < width * height; index++) {
            Log.v("Controller:generateMine", "UPDATE");
            Objects.requireNonNull(getGrid(index)).countSurroundings();
        }
        open(start);

        for (int index = 0; index < width * height; index++) {
//            getGrid(index).updateState();
//            activity.submitGridOpenAnimation(getGrid(index));
            Objects.requireNonNull(getGrid(index)).prepared();
        }
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
        gameInfo.start();
//        cacheManager.postDelayed(() -> cacheManager.cache(gridList(), getResult()),
//                2000);
    }

    public void cache() {
        cacheManager.cache(gridList(), getResult());
    }

    /**
     * 调试用 输出地雷分布
     *
     * @return 地雷分布
     */
    @NonNull
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        for (Grid[] row : grids) {
            for (Grid grid : row)
                buffer.append(grid).append(' ');
            buffer.append('\n');
        }
        return buffer.toString();
    }

    /**
     * 打开格子</br>
     * 默认游戏<strong>没有开始</strong>
     *
     * @param start 起始格子
     * @throws MineTriggeredException 触雷
     */
    public void open(@NonNull Grid start) throws MineTriggeredException {
        open(start, false);
    }

    /**
     * 打开格子
     *
     * @param started 是否已经开始游戏<br/>
     *                如果已经开始游戏 则检查周围格子插旗数是否等于周围雷数<br/>
     *                再决定是否打开周围格子<br/>
     * @param start   起始格子<br/>
     *                如果格子周围有雷数为0 则打开周围格子<br/>
     *                如果格子已经打开 则检查周围格子插旗数是否等于周围雷数<br/>
     *                如果格子周围插旗数不等于周围雷数 则提示<br/>
     *                如果格子周围插旗数等于周围雷数 则打开周围格子<br/>
     * @throws MineTriggeredException 触雷
     */

    public void open(@NonNull Grid start, boolean started) throws MineTriggeredException {
        Queue<Grid> queue = new LinkedList<>();
        boolean[][] visited = new boolean[height][width];
        final boolean[] refreshActivity = {true};
        if (finished) {
            Toast.makeText(activity, "游戏已结束", LENGTH_SHORT).show();
            return;
        }
        if (started) {
            // if the grid the player clicked is flagged,
            // add security check to prevent opening
            if (start.getState() == Grid.STATE.FLAG) {
                Toast.makeText(activity, "🚩", LENGTH_SHORT).show();
                return;
            }
            AtomicInteger flagCount = new AtomicInteger();
            Stream<Grid> notOpened = start.getNeighbors().stream().filter(
                    grid -> !(grid.getState() == Grid.STATE.OPEN)
            );
            notOpened.forEach(n -> {
                if (Objects.requireNonNull(n.getState()) == Grid.STATE.FLAG) {
                    flagCount.getAndIncrement();
                }else
                {
                    queue.add(n);
                }
            });
            int remaining = start.getSurroundingMines() - flagCount.get();
            String message;
            if (remaining > 0) {
                message = String.format(Locale.CHINA, "少插了%d个旗子", remaining);
                Toast.makeText(activity, message, LENGTH_SHORT).show();
                return;
            }
            if (remaining < 0) {
                message = String.format(Locale.CHINA, "多插了%d个旗子", -remaining);
                Toast.makeText(activity, message, LENGTH_SHORT).show();
                return;
            }
        } else {
            queue.add(start);
        }
        while (!queue.isEmpty()) {
            Grid current = queue.poll();
            if (current == null) {
                Log.w("Controller:open", "Null stored in neighbor");
                continue;
            }
            current.open();
            visited[current.getRow()][current.getCol()] = true;
            if (current.getSurroundingMines() > 0) continue;
            Stream<Grid> openCandidates = current.getNeighbors().stream().filter(
                    grid -> !visited[grid.getRow()][grid.getCol()]
            );
            openCandidates.forEach(open_candidate_grid -> {
//                if (openCandidates == null) return;
                queue.add(open_candidate_grid);
                activity.submitGridOpenAnimation(open_candidate_grid, refreshActivity[0]);
                refreshActivity[0] = false;
//                visited[grid.getRow()][grid.getCol()] = true;
            });

            // 在日志中输出雷区信息
            System.out.println(activity.getController());
        }
        if (isFinished()) {
            win();
        }
    }

    @NonNull
    public List<Grid> gridList() {
        synchronized (this) {
            List<Grid> list = new ArrayList<>();
            if (grids == null)
                return list;
            for (Grid[] row : grids) {
                if (row == null)
                    continue;
                list.addAll(Arrays.asList(row));
            }
            return list;
        }
    }

    /**
     * 添加非雷且已经打开的格子<br/>
     *
     * @param grid 要添加的格子
     */
    public void addFinished(@NonNull Grid grid) {
        switch (grid.getState()) {
            case FLAG:
                if (!grid.isMine()) return;
                break;
            case OPEN:
                if (grid.isMine()) return;
                break;
        }
        finishedGrids.add(grid);
//        checkFinished();
        updateProgress();
    }

    /**
     * 计算插旗数 给{@link Controller#updateProgress()}调用<br/>
     *
     * @return 插旗数
     */
    public int countFlagTotal() {
        int count = 0;
        for (Grid[] row : grids) {
            for (Grid g : row) {
                if (g.getState() == Grid.STATE.FLAG) count++;
            }
        }
        return count;
    }

    /**
     * 数总共插旗数 给用户提示游戏进度
     */
    public void updateProgress() {
        activity.getMinePrompt().setText(String.valueOf(mines - countFlagTotal()));
    }

    /**
     * 检查游戏是否结束
     */
    private void win() {
        endGame(true);
        getFinishDialog(true).show();
    }

    /**
     * 输掉游戏
     */
    public void lose() {
        endGame(false);
        reveal();
        getFinishDialog(false).show();
    }

    private void endGame(boolean win) {
        finished = true;
        SharedUtils.end();
        chronometer.stop();
        activity.getTimer().stop();
        gameInfo.end(activity.getTimer().getTime());
        gameInfo.setWin(win);
        MainApplication.getInstance().getRecordDAO().recordGame(gameInfo);
    }

    /**
     * 显示所有格子 告诉用户输掉的原因
     */

    public void reveal() {
        boolean refreshActivity = true;
        for (Grid[] row : grids) {
            for (Grid grid_to_reveal : row) {
                try {
                    grid_to_reveal.open(true);
                    activity.submitGridOpenAnimation(grid_to_reveal, refreshActivity);
                    refreshActivity = false;
                } catch (MineTriggeredException exception) {
                    Log.w("Controller:reveal", "MineTriggeredException");
                }
            }
        }
    }

    /**
     * 构造一个游戏结束的对话框<br/>
     * 在构造时进行游戏结束的处理
     *
     * @param win 是否赢得游戏
     * @return 对话框
     */

    @NonNull
    public AlertDialog getFinishDialog(boolean win) {
        finished = true;
        for (Grid[] row : grids) {
            for (Grid g : row) {
                ImageButton button = g.getDisplayGrid();
                if (button == null) continue;
                button.setOnClickListener(null);
                button.setOnLongClickListener(null);
            }
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        String title = win ? "您赢了" : "您输了";

        String content = getEndMessage(win);

        builder.setTitle(title);
        builder.setMessage(content);

        builder.setPositiveButton("确认", (dialog, which) -> promptAndExit(activity));
        builder.setNegativeButton("查看结果", (dialog, which) -> {
        });

        return builder.create();
    }

    private @NonNull String getEndMessage(boolean win) {
        long timeUsed = gameInfo.getInterval();
        return win ? "用时：" + timeUsed + "秒" : "";
    }
}

