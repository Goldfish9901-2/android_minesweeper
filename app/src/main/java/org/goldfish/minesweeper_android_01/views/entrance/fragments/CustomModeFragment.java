package org.goldfish.minesweeper_android_01.views.entrance.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import org.goldfish.minesweeper_android_01.R;
import org.goldfish.minesweeper_android_01.RefinedModeSelectListener;
import org.goldfish.minesweeper_android_01.persistance.entity.Result;
import org.goldfish.minesweeper_android_01.views.AbstractEntranceFragment;

/**
 * A simple {@link Fragment} subclass.
 * Basic logic:<br/>
 * 1. Set up the spinners for height and width{@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}<br/>
 * 2. Set up the spinner for mines<br/>
 * 3. Set up the start button<br/>
 */
public class CustomModeFragment extends AbstractEntranceFragment {
    View.OnClickListener giveUpListener;
    View.OnClickListener emptyChoiceListener;

    public CustomModeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        giveUpListener = v -> {
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.nav_host_fragment_container, ModeSelectFragment.class, savedInstanceState);
            transaction.addToBackStack(null);
            transaction.commit();
        };
        emptyChoiceListener = v -> Toast.makeText(
                requireContext(),
                "请先选择高度、宽度和雷数",
                Toast.LENGTH_SHORT
        ).show();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewToInflate = inflater.inflate(R.layout.fragment_custom_mode, container, false);

        Spinner heightSpinner = viewToInflate.findViewById(R.id.height_spinner);
        Spinner widthSpinner = viewToInflate.findViewById(R.id.width_spinner);

        heightSpinner.setAdapter(getHeightAdapter());
        widthSpinner.setAdapter(getWidthAdapter());

        AdapterView.OnItemSelectedListener listener = getAfterSizeSelectedListener(viewToInflate);

        heightSpinner.setOnItemSelectedListener(listener);
        widthSpinner.setOnItemSelectedListener(listener);
        viewToInflate.findViewById(R.id.custom_start_button)
                .setOnClickListener(emptyChoiceListener);
        viewToInflate.findViewById(R.id.custom_out_button)
                .setOnClickListener(giveUpListener);
        return viewToInflate;
    }

    /**
     * 获取选择高度和宽度后 自定义雷数的监听器
     *
     * @return {@link AdapterView.OnItemSelectedListener}
     */

    @NonNull
    private AdapterView.OnItemSelectedListener getAfterSizeSelectedListener(View inflatedView) {

        return new AdapterView.OnItemSelectedListener() {
            boolean heightSelected = false;
            boolean widthSelected = false;
            final Spinner minesSpinner = inflatedView.findViewById(R.id.count_spinner);

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getId() == R.id.height_spinner) {
                    heightSelected = true;
                } else if (parent.getId() == R.id.width_spinner) {
                    widthSelected = true;
                }
                if (!heightSelected || !widthSelected) {
                    return;
                }
                Spinner heightSpinner = inflatedView.findViewById(R.id.height_spinner);
                Spinner widthSpinner = inflatedView.findViewById(R.id.width_spinner);
                int height = (int) heightSpinner.getSelectedItem();
                int width = (int) widthSpinner.getSelectedItem();
                minesSpinner.setAdapter(getMinesAdapter(height, width));
                minesSpinner.setOnItemSelectedListener(getMineCountSelectedListener(inflatedView));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                heightSelected = false;
                widthSelected = false;
                minesSpinner.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item));
                Button startButton = inflatedView.findViewById(R.id.custom_start_button);
                startButton.setOnClickListener(giveUpListener);
            }
        };
    }

    @NonNull
    private AdapterView.OnItemSelectedListener getMineCountSelectedListener(View inflatedView) {
        Spinner minesSpinner = inflatedView.findViewById(R.id.count_spinner);
        Spinner heightSpinner = inflatedView.findViewById(R.id.height_spinner);
        Spinner widthSpinner = inflatedView.findViewById(R.id.width_spinner);
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Result result = getCustomResult(heightSpinner, widthSpinner, minesSpinner);
                View.OnClickListener listener = new RefinedModeSelectListener(result, requireActivity());
                inflatedView.findViewById(R.id.custom_start_button).setOnClickListener(listener);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Button startButton = inflatedView.findViewById(R.id.custom_start_button);
                startButton.setOnClickListener(giveUpListener);
            }
        };
    }


    private ArrayAdapter<Integer> getHeightAdapter() {
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        for (int i = 9; i <= 24; i++) {
            adapter.add(i);
        }
        return adapter;
    }

    @NonNull
    private ArrayAdapter<Integer> getWidthAdapter() {
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        for (int i = 9; i <= 30; i++) {
            adapter.add(i);
        }
        return adapter;
    }

    @NonNull
    private ArrayAdapter<Integer> getMinesAdapter(int height, int width) {
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        int count_min = (int) (Math.sqrt(height * width));
        int count_max = height * width / 4;
        for (int i = count_min; i <= count_max; i++) {
            adapter.add(i);
        }
        return adapter;
    }

    @NonNull
    private Result getCustomResult(
            @NonNull Spinner heightSpinner,
            @NonNull Spinner widthSpinner,
            @NonNull Spinner minesSpinner) {

        Result result = new Result(requireActivity());
        Integer height = (Integer) heightSpinner.getSelectedItem();
        Integer width = (Integer) widthSpinner.getSelectedItem();
        Integer mines = (Integer) minesSpinner.getSelectedItem();
        if (height == null) {
            Toast.makeText(requireContext(), "请选择高度", Toast.LENGTH_SHORT).show();
        } else if (width == null) {
            Toast.makeText(requireContext(), "请选择宽度", Toast.LENGTH_SHORT).show();
        } else if (mines == null) {
            Toast.makeText(requireContext(), "请选择雷数", Toast.LENGTH_SHORT).show();
        } else {
            result.setHeight(height);
            result.setWidth(width);
            result.setMineCount(mines);
            result.setDifficulty_description("自定义");
        }
        return result;
    }
}