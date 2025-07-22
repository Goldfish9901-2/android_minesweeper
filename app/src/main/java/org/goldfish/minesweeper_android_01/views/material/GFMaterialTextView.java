package org.goldfish.minesweeper_android_01.views.material;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.material.textview.MaterialTextView;

import org.goldfish.minesweeper_android_01.R;

public class GFMaterialTextView extends MaterialTextView {
    public GFMaterialTextView(@NonNull Context context) {
        super(context);
        self_init(context);
    }

    public GFMaterialTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        self_init(context);

    }

    public GFMaterialTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        self_init(context);
    }

    public void self_init(@NonNull Context context) {
        setTextColor(ContextCompat.getColor(context, R.color.grid_closed));
        setBackgroundColor(ContextCompat.getColor(context, R.color.highlight));
        setTextSize(25);
    }
}
