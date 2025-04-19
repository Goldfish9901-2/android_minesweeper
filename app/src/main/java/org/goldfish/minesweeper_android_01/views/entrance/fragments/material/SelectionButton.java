package org.goldfish.minesweeper_android_01.views.entrance.fragments.material;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.app.Activity;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import org.goldfish.minesweeper_android_01.utils.MathUtils;
import org.goldfish.minesweeper_android_01.views.material.GFMaterialButton;

public class SelectionButton extends GFMaterialButton {
    public SelectionButton(@NonNull Activity activity) {
        super(activity);
        setWidth(WRAP_CONTENT);
        setHeight(WRAP_CONTENT);
        ViewGroup.LayoutParams viewParams = new ViewGroup.LayoutParams(WRAP_CONTENT,WRAP_CONTENT);
        setLayoutParams(viewParams);
        setTextSize(MathUtils.intoSP(activity, 40));
    }
}
