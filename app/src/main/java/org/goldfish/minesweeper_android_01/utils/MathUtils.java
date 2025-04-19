package org.goldfish.minesweeper_android_01.utils;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import androidx.annotation.NonNull;

public class MathUtils {

    private static DisplayMetrics displayMetrics(Activity context) {
        return context.getResources().getDisplayMetrics();
    }

    /**
     * converts a px int into dp using the current display metrics
     * @param px the px int
     * @param activity the activity to get system display info from
     * @return the dp int
     */
    public static float intoDP(@NonNull Activity activity, float px) {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                px,
                displayMetrics(activity)
        )
                ;
    }

    public static float intoSP(@NonNull Activity activity, float px){
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                px,
                displayMetrics(activity)
        );
    }
}
