package org.goldfish.minesweeper_android_01.views.entrance.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentTransaction;

import org.goldfish.minesweeper_android_01.MainApplication;
import org.goldfish.minesweeper_android_01.R;
import org.goldfish.minesweeper_android_01.persistance.entity.GameInfo;
import org.goldfish.minesweeper_android_01.utils.Resources;
import org.goldfish.minesweeper_android_01.utils.SharedUtils;
import org.goldfish.minesweeper_android_01.views.entrance.fragments.CustomModeFragment;
import org.goldfish.minesweeper_android_01.views.entrance.fragments.ModeSelectFragment;


public class EntranceActivity
        extends AppCompatActivity
        implements Resources {
    String TAG = MainApplication.TAG;
    FragmentContainerView fragmentContainerView;
    CustomModeFragment customModeFragment;
    Bundle savedInstanceState;

    public EntranceActivity() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        fragmentContainerView = findViewById(R.id.nav_host_fragment_container);
        customModeFragment = new CustomModeFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment_container, ModeSelectFragment.class, savedInstanceState);
        transaction.addToBackStack(null);
        int commit_result = transaction.commit();
        Log.i(TAG, "onResume: " + commit_result);
        GameInfo[] gameInfo = {SharedUtils.loadGameInfo(this)};
        if (gameInfo[0] == null) return;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("游戏已开始")
                .setMessage("是否继续游戏？")
                .setPositiveButton("继续游戏", (dialog, which) -> {
                    startActivity(gameInfo[0]);
                })
                .setNegativeButton("重新开始", (dialog, which) -> {
                    SharedUtils.end();
                })
                .setCancelable(false)
                .show();

    }
}