package org.goldfish.minesweeper_android_01.views.record;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.goldfish.minesweeper_android_01.MainApplication;
import org.goldfish.minesweeper_android_01.R;
import org.goldfish.minesweeper_android_01.persistance.entity.GFTime;
import org.goldfish.minesweeper_android_01.persistance.entity.Result;

import java.util.List;

public class RecordActivity extends AppCompatActivity {
    final static float TEXT_SIZE = 30;
    TableLayout tableLayout;
    TableLayout detailLayout;

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
        List<Result> records = MainApplication.getInstance().getDao().getWinRecords();
        tableLayout = findViewById(R.id.record_table);
        detailLayout=findViewById(R.id.record_detail_table);
        for (Result record : records) {
            RecordRow row = new RecordRow(record);
            tableLayout.addView(row);
            DetailedRecordRow detailedRow=new DetailedRecordRow(record);
            detailLayout.addView(detailedRow);
        }

        Button backButton = findViewById(R.id.record_return_button);
        backButton.setOnClickListener(v -> finish());
    }



    class RecordRow extends TableRow {
        /**
         * 每行先难度 再高度宽度 再雷数 再时间
         *
         * @param record 数据
         */
        public RecordRow(Result record) {
            super(RecordActivity.this);
            setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            setBackgroundResource(R.drawable.table_row_bg); // Assuming you have a drawable for row background
            if (record.getStartTimeID() == -1)
                return;

            GFTime startTime = MainApplication.getInstance().getDao().getTimeById(record.getStartTimeID());
            GFTime endTime = MainApplication.getInstance().getDao().getTime(record.getEndTimeID());
            RecordCell minesCell = new RecordCell(record.getMineCount());
            RecordCell fieldSizeCell = new RecordCell(record.getHeight() + "*" + record.getWidth());
            RecordCell difficultyCell = new RecordCell(record.getDifficultyDescription());
            RecordCell timeCell = new RecordCell(record.getInterval());
            addView(difficultyCell);
            addView(fieldSizeCell);
            addView(minesCell);
            addView(timeCell);
//            addView(new DetailedRecordCell(startTime));
//            addView(new DetailedRecordCell(endTime));


        }
    }

    class DetailedRecordRow extends TableRow{
        public DetailedRecordRow(Result record) {
            super(RecordActivity.this);
            setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            setBackgroundResource(R.drawable.table_row_bg); // Assuming you have a drawable for row background
            if (record.getStartTimeID() == -1)
                return;

            GFTime startTime = MainApplication.getInstance().getDao().getTimeById(record.getStartTimeID());
            GFTime endTime = MainApplication.getInstance().getDao().getTime(record.getEndTimeID());
            RecordCell minesCell = new DetailedRecordCell(record.getMineCount());
            RecordCell fieldSizeCell = new DetailedRecordCell(record.getHeight() + "*" + record.getWidth());
            RecordCell difficultyCell = new DetailedRecordCell(record.getDifficultyDescription());
            RecordCell timeCell = new DetailedRecordCell(record.getInterval());
            addView(difficultyCell);
            addView(fieldSizeCell);
            addView(minesCell);
//            addView(timeCell);
            addView(new DetailedRecordCell(startTime));
            addView(new DetailedRecordCell(endTime));
        }
    }

    class RecordCell extends androidx.appcompat.widget.AppCompatTextView {
        public RecordCell(Object value) {
            super(RecordActivity.this);
            setText(value.toString());
            setTextSize(RecordActivity.TEXT_SIZE);

            setPadding(16, 16, 16, 16); // Padding for better readability
            setBackgroundResource(R.drawable.table_cell_bg); // Assuming you have a drawable for cell background
            setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        }
    }
    class DetailedRecordCell extends RecordCell{
        public DetailedRecordCell(Object value){
            super(value);
            setTextSize(20);
        }
    }

}


