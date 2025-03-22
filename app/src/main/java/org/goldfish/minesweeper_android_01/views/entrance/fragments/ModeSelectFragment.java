package org.goldfish.minesweeper_android_01.views.entrance.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import org.goldfish.minesweeper_android_01.R;
import org.goldfish.minesweeper_android_01.RefinedModeSelectListener;
import org.goldfish.minesweeper_android_01.persistance.entity.Result;
import org.goldfish.minesweeper_android_01.views.entrance.activities.EntranceActivity;
import org.goldfish.minesweeper_android_01.views.record.RecordActivity;
import org.goldfish.minesweeper_android_01.views.AbstractEntranceFragment;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ModeSelectFragment extends AbstractEntranceFragment {
    private static final int REQUEST_CODE_PERMISSIONS = 1001;
    Button easyButton, mediumButton, hardButton;
    Button customButton;
    Button recordButton;

    public ModeSelectFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_mode_select, container, false);
        easyButton = view.findViewById(R.id.easy_mode_button);
        mediumButton = view.findViewById(R.id.medium_mode_button);
        hardButton = view.findViewById(R.id.hard_mode_button);
        recordButton = view.findViewById(R.id.record_button);
        customButton = view.findViewById(R.id.custom_mode_button);

        easyButton.setOnClickListener(new RefinedModeSelectListener(Result.EASY(requireActivity()), requireActivity()));
        mediumButton.setOnClickListener(new RefinedModeSelectListener(Result.MEDIUM(requireActivity()), requireActivity()));
        hardButton.setOnClickListener(new RefinedModeSelectListener(Result.HARD(requireActivity()), requireActivity()));

        recordButton.setOnClickListener(v ->
                startActivity(new Intent(view.getContext(), RecordActivity.class)));
        recordButton.setOnLongClickListener(v -> {
            getRestoreAlertBuilder(view).create().show();
            return true;
        });
        customButton.setOnClickListener(v -> {
            if (!(getActivity() instanceof EntranceActivity)) {
                return;
            }
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.nav_host_fragment_container, CustomModeFragment.class, savedInstanceState);
            transaction.commit();
        });
        return view;
    }


}