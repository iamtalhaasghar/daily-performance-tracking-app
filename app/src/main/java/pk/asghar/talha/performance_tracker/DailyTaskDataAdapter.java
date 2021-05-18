package pk.asghar.talha.performance_tracker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DailyTaskDataAdapter extends RecyclerView.Adapter<DailyTaskDataAdapter.DailyTaskViewHolder> {

    private Context context;
    private Db db;
    public DailyTaskDataAdapter(Context context){
        this.context = context;
        db = new Db(context);
    }
    @NonNull
    @Override
    public DailyTaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        View dailyTaskRowLayoutView = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.daily_task_row_layout, parent, false);
        DailyTaskViewHolder dailyTaskViewHolder = new DailyTaskViewHolder(dailyTaskRowLayoutView);
        return dailyTaskViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DailyTaskViewHolder holder, int position) {
        Cursor cursor = db.readAll();
        cursor.moveToPosition(position);
        int id = cursor.getInt(0);
        String name = cursor.getString(1);
        String date = cursor.getString(2);
        String startTime = cursor.getString(3);
        String endTime = cursor.getString(4);
        String description = cursor.getString(5);
        final DailyTask dailyTask = new DailyTask(id, name, date, startTime, endTime, description);
        holder.setDailyTask(dailyTask);
        holder.itemView.findViewById(R.id.button_view_task_details).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setMessage(dailyTask.toString());
                alert.create().show();
            }
        });

        holder.itemView.findViewById(R.id.button_del_task).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean success = db.delete(dailyTask.getId());
                notifyDataSetChanged();
                Toast.makeText(context, "Deleted task with id = "+dailyTask.getId()+". Success: "+success, Toast.LENGTH_SHORT).show();
            }
        });

        holder.itemView.findViewById(R.id.button_edit_task).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CreateTaskAcitivity.class);
                intent.putExtra(Db.KEY_ID, dailyTask.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return db.readAll().getCount();
    }

    public class DailyTaskViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewTaskId;
        private TextView textViewTaskName;
        public DailyTaskViewHolder(View itemView) {
            super(itemView);
            textViewTaskId = itemView.findViewById(R.id.text_view_task_id);
            textViewTaskName = itemView.findViewById(R.id.text_view_task_name);

        }

        public void setDailyTask(DailyTask dailyTask){
            textViewTaskId.setText(String.valueOf(dailyTask.getId()));
            textViewTaskName.setText(dailyTask.getName());
        }


    }

}
