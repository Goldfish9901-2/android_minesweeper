package org.goldfish.minesweeper_android_01.views.entrance.fragments.material;

import static android.view.ViewGroup.LayoutParams.*;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.goldfish.minesweeper_android_01.utils.MathUtils;
import org.goldfish.minesweeper_android_01.views.material.GFMaterialButton;

public class SelectionButton extends GFMaterialButton {
    public SelectionButton(@NonNull Context activity) {
        super(activity);
        self_init(activity);
    }

    private void self_init(@NonNull Context context) {

        ViewGroup.LayoutParams viewParams = new ViewGroup.LayoutParams(WRAP_CONTENT,WRAP_CONTENT);
        setLayoutParams(viewParams);
        setTextSize(
//                MathUtils.intoSP(context, 40)
                40
        );
    }

    public SelectionButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        self_init(context);
    }

    public SelectionButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        self_init(context);
    }
}
