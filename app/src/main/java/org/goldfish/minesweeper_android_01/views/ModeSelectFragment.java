package org.goldfish.minesweeper_android_01.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import org.goldfish.minesweeper_android_01.MainApplication;
import org.goldfish.minesweeper_android_01.R;
import org.goldfish.minesweeper_android_01.RefinedModeSelectListener;
import org.goldfish.minesweeper_android_01.entity.Result;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ModeSelectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ModeSelectFragment extends Fragment {
    private static final int REQUEST_CODE_PERMISSIONS = 1001;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Button easyButton, mediumButton, hardButton, recordButton;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ModeSelectFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ModeSelectFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ModeSelectFragment newInstance(String param1, String param2) {
        ModeSelectFragment fragment = new ModeSelectFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ViewCompat.setOnApplyWindowInsetsListener(requireView(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
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

        easyButton.setOnClickListener(new RefinedModeSelectListener(Result.EASY(), requireActivity()));
        mediumButton.setOnClickListener(new RefinedModeSelectListener(Result.MEDIUM(), requireActivity()));
        hardButton.setOnClickListener(new RefinedModeSelectListener(Result.HARD(), requireActivity()));

        recordButton.setOnClickListener(v -> {
            startActivity(new Intent(view.getContext(), RecordActivity.class));
        });
        recordButton.setOnLongClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("警告");
            builder.setMessage("重置所有储存数据?");
            builder.setPositiveButton("Yes", (dialog, which) -> {
                MainApplication.getInstance().getDao().deleteAll();
                Toast.makeText(view.getContext(), "数据已重置", Toast.LENGTH_SHORT).show();
            });
            builder.setNegativeButton("No", (dialog, which) -> {
            });
            builder.create().show();
            return true;
        });
        return view;
    }
}