package org.goldfish.minesweeper_android_01.views.record;

import android.graphics.Color;
import android.os.Bundle;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.goldfish.minesweeper_android_01.MainApplication;
import org.goldfish.minesweeper_android_01.R;
import org.goldfish.minesweeper_android_01.persistance.entity.Result;
import org.goldfish.minesweeper_android_01.persistance.entity.ResultSummary;

import java.util.List;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.style.FontStyle;

public class RecordActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_record);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button returnButton = findViewById(R.id.record_return_button);
        returnButton.setOnClickListener(v -> finish());
        List<Result> records = MainApplication.getInstance().getDao().getWinRecords();
        if (records == null) {
            Toast.makeText(this, "无记录", Toast.LENGTH_SHORT).show();
            return;
        }
        if (records.isEmpty()) {
            Toast.makeText(this, "空记录", Toast.LENGTH_SHORT).show();
            return;
        }

        FontStyle style = new FontStyle(50, Color.parseColor("#000088"))
                , columnStyle = new FontStyle(40, Color.parseColor("#008800"))
//                ,dataStyle = new FontStyle(30, Color.parseColor("#888800"))
                ;

        SmartTable<ResultSummary> table = new SmartTable<>(this);
        table.getConfig().setTableTitleStyle(style);
        table.getConfig().setColumnTitleStyle(columnStyle);
        table.setData(ResultSummary.summaries(records));

        ScrollView summaryView = findViewById(R.id.rec_summary_wrapper);
        summaryView.addView(table);

    }
}
