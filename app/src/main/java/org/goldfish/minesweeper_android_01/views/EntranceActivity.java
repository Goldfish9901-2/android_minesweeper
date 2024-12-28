package org.goldfish.minesweeper_android_01.views;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentTransaction;

import org.goldfish.minesweeper_android_01.MainApplication;
import org.goldfish.minesweeper_android_01.R;
import org.goldfish.minesweeper_android_01.Resources;
import org.goldfish.minesweeper_android_01.fragment.CustomModeFragment;
import org.goldfish.minesweeper_android_01.fragment.ModeSelectFragment;


public class EntranceActivity extends AppCompatActivity implements Resources {
    String TAG = MainApplication.TAG;
    FragmentContainerView fragmentContainerView;
    CustomModeFragment customModeFragment;
    Bundle savedInstanceState;

    public EntranceActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
//        transaction.add(R.id.nav_host_fragment_container, CustomModeFragment.class, savedInstanceState);
        transaction.add(R.id.nav_host_fragment_container, ModeSelectFragment.class, savedInstanceState);
        transaction.addToBackStack(null);
        int result = transaction.commit();
        Log.i(TAG, "onResume: " + result);
    }
}