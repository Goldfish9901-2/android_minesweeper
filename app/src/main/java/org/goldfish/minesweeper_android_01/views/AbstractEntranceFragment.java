package org.goldfish.minesweeper_android_01.views;

import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import org.goldfish.minesweeper_android_01.MainApplication;

public abstract class AbstractEntranceFragment extends Fragment {
    @NonNull
    protected static AlertDialog.Builder getRestoreAlertBuilder(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("警告");
        builder.setMessage("重置所有储存数据?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            MainApplication.getInstance().getDao().deleteAll();
            Toast.makeText(view.getContext(), "数据已重置", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("No", (dialog, which) -> {
        });
        return builder;
    }
}
