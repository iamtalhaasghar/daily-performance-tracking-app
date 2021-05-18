package pk.asghar.talha.performance_tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class CreateTaskAcitivity extends AppCompatActivity {

    TextView textViewStartTime;
    TextView textViewEndTime;
    TextView textViewDate;
    EditText etTaskName;
    EditText etTaskDetails;
    Date startingTime;
    Date endingTime;
    boolean newRecord;
    Db db;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = new Db(CreateTaskAcitivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        etTaskName = findViewById(R.id.editTextTaskName);
        etTaskDetails = findViewById(R.id.editTextTaskDetails);
        textViewStartTime = findViewById(R.id.text_view_start_time);
        textViewEndTime = findViewById(R.id.text_view_end_time);
        textViewDate = findViewById(R.id.text_view_date);


        id = getIntent().getIntExtra(Db.KEY_ID, -1);
        if(id != -1){
            newRecord = false;
            Cursor cursor = db.read(id);
            String name = cursor.getString(1);
            String date = cursor.getString(2);
            String startTime = cursor.getString(3);
            String endTime = cursor.getString(4);
            String description = cursor.getString(5);

            etTaskName.setText(name);
            etTaskDetails.setText(description);
            textViewStartTime.setText(startTime);
            textViewEndTime.setText(endTime);
            textViewDate.setText(date);
        }
        else{
            newRecord = true;
        }

    }

    public void saveTaskButtonClicked(View view){

        String name = etTaskName.getText().toString();
        String details = etTaskDetails.getText().toString();
        String startTimeText = textViewStartTime.getText().toString();
        String endTimeText = textViewEndTime.getText().toString();
        String dateText = textViewDate.getText().toString();

        if(name.isEmpty() || details.isEmpty() || startTimeText.isEmpty() || endTimeText.isEmpty() || dateText.isEmpty()){
            Toast.makeText(CreateTaskAcitivity.this, "Please fill all fields.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(startingTime != null && endingTime != null && endingTime.before(startingTime)){
            Toast.makeText(CreateTaskAcitivity.this, "The Task Completion Time must be greater than the task`s starting time.", Toast.LENGTH_SHORT).show();
            return;
        }


        DailyTask dailyTask = new DailyTask(name, dateText, startTimeText, endTimeText, details);
        long rowId;
        if(newRecord) {
            rowId = db.insert(dailyTask);
        }
        else{
            rowId = db.update(id, dailyTask);
        }
        String message = "Unexpected Failure!!";
        if(rowId != -1){
            message = "Success!! Id of the Task added / updated is : "+rowId;
            Intent intent = new Intent(CreateTaskAcitivity.this, ViewAllActivity.class);
            startActivity(intent);
            finish();
        }
        Toast.makeText(CreateTaskAcitivity.this, message, Toast.LENGTH_SHORT).show();


    }


    public void startTimeButtonClicked(View view){
        StartTimeSetListener startTimeSetListener = new StartTimeSetListener();
        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(startTimeSetListener, false);
        timePickerDialog.show(getSupportFragmentManager(), "TimePickerDialog");
    }

    public void endTimeButtonClicked(View view){

        EndTimeSetListener timeSetListener = new EndTimeSetListener();
        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(timeSetListener, false);
        timePickerDialog.show(getSupportFragmentManager(), "TimePickerDialog");

    }

    public void dateButtonClicked(View view){
        DateSetListener dateSetListener = new DateSetListener();
        DatePickerDialog dialog = DatePickerDialog.newInstance(dateSetListener);
        dialog.show(getSupportFragmentManager(), "DatePickerDialog");
    }

    private class StartTimeSetListener implements TimePickerDialog.OnTimeSetListener{

        @Override
        public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
            try {
                String timeText = String.format("%d:%d",hourOfDay, minute);
                SimpleDateFormat format = new SimpleDateFormat("H:m");
                startingTime = format.parse(timeText);
                format.applyPattern("hh:mma");
                textViewStartTime.setText(format.format(startingTime));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private class EndTimeSetListener implements TimePickerDialog.OnTimeSetListener{

        @Override
        public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
            try {
                String timeText = String.format("%d:%d",hourOfDay, minute);
                SimpleDateFormat format = new SimpleDateFormat("H:m");
                endingTime = format.parse(timeText);
                format.applyPattern("hh:mma");
                textViewEndTime.setText(format.format(endingTime));

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private class DateSetListener implements DatePickerDialog.OnDateSetListener{

        @Override
        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
            textViewDate.setText(String.format("%02d-%02d-%04d",dayOfMonth, monthOfYear, year));

        }
    }


}