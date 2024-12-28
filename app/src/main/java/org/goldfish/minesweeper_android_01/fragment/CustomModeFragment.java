package org.goldfish.minesweeper_android_01.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import org.goldfish.minesweeper_android_01.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomModeFragment extends AbstractEntranceFragment {
    public CustomModeFragment() {
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
        View view = inflater.inflate(R.layout.fragment_custom_mode, container, false);
        Button confirmButton = view.findViewById(R.id.custom_start_button);

        confirmButton.setOnClickListener(null);

        return view;
    }
}