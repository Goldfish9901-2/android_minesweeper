package org.goldfish.minesweeper_android_01.utils;

import android.os.Build;
import android.os.VibrationEffect;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.Q)
public interface VibrationTypes {
    int HEAVY_CLICK= VibrationEffect.EFFECT_HEAVY_CLICK;
    int CLICK=VibrationEffect.EFFECT_CLICK;
    int TICK=VibrationEffect.EFFECT_TICK;
}
