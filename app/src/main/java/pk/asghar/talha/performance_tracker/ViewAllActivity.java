package pk.asghar.talha.performance_tracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Header;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ViewAllActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);

        RecyclerView rvMyActivities = findViewById(R.id.recycler_view_daily_tasks);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ViewAllActivity.this);
        rvMyActivities.setLayoutManager(layoutManager);

        RecyclerView.Adapter adapter = new DailyTaskDataAdapter(ViewAllActivity.this);
        rvMyActivities.setAdapter(adapter);

        findViewById(R.id.fab_new_task).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewAllActivity.this, CreateTaskAcitivity.class);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.fab_downlaod).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ViewAllActivity.this, "Here", Toast.LENGTH_SHORT).show();
                createPDF();
            }
        });

    }

    public void createPDF() {
        Document doc = new Document();
        Db db = new Db(ViewAllActivity.this);

        try {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/performance_tracker";

            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();

            File file = new File(dir, "result.pdf");
            FileOutputStream fOut = new FileOutputStream(file);

            PdfWriter.getInstance(doc, fOut);

            //open the document
            doc.open();

            Cursor cursor = db.readAll();
            while(!cursor.isAfterLast()){
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String date = cursor.getString(2);
                String startTime = cursor.getString(3);
                String endTime = cursor.getString(4);
                String description = cursor.getString(5);
                final DailyTask dailyTask = new DailyTask(id, name, date, startTime, endTime, description);
                Paragraph p1 = new Paragraph(dailyTask.toString());
                p1.setAlignment(Paragraph.ALIGN_CENTER);
                doc.add(p1);
                cursor.moveToNext();
            }


        } catch (DocumentException de) {
            Log.e("PDFCreate", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreate", "ioException:" + e);
        } finally {
            doc.close();
        }
    }

}