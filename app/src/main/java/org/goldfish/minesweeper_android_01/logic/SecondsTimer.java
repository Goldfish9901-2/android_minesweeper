package org.goldfish.minesweeper_android_01.logic;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class SecondsTimer extends Handler {
    public SecondsTimer(@NonNull Looper looper) {
        super(looper);
    }

    private final AtomicBoolean timing = new AtomicBoolean(false);
    private final AtomicLong time = new AtomicLong(0);

    /**
     * count a second
     */
    private void time() {
        time.incrementAndGet();
        if (!timing.get()) return;
        postDelayed(this::time, 1000);
    }

    /**
     * start or resume timing
     */
    public void start() {
        timing.set(true);
        postDelayed(this::time,1000);
    }
    /**
     * pause timing
     */
    public void stop() {
        timing.set(false);
    }

    public long getTime() {
        return time.get();
    }
}
