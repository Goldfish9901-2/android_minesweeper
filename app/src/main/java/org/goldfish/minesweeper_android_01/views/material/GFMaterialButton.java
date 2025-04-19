package org.goldfish.minesweeper_android_01.views.material;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;

import org.goldfish.minesweeper_android_01.R;
import org.goldfish.minesweeper_android_01.utils.MathUtils;

public class GFMaterialButton extends MaterialButton {
    public GFMaterialButton(@NonNull Activity context) {
        super(context);
        this_init(context);
    }

//    public GFMaterialButton(@NonNull Activity context, @Nullable AttributeSet attrs) {
//        super(context, attrs);
//        this_init(context);
//    }
//
//    public GFMaterialButton(@NonNull Activity context, @Nullable AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        this_init(context);
//    }

    private void this_init(@NonNull Activity context) {
        int color = ContextCompat.getColor(context, R.color.gf_button);
        ColorStateList list = ColorStateList.valueOf(color);
        setBackgroundTintList(list);
        setCornerRadius((int) MathUtils.intoDP(context, 20));
    }
}
