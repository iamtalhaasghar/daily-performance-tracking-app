package pk.asghar.talha.performance_tracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Db {

    private Context context;
    private final static int DB_VER = 1;
    private final static String LOG_TAG = "performance_tracker";

    // schema of Database used in the app
    private final static String DB_NAME = "db_performances";
    private final static String TB_NAME = "tb_activities";

    public final static String KEY_ID = "col_id";
    private final static String KEY_START_TIME = "col_start_time";
    private final static String KEY_END_TIME = "col_end_time";
    private final static String KEY_DATE = "col_date";
    private final static String KEY_NAME = "col_activity";
    private final static String KEY_DESCRIPTION = "col_description";

    private final static String CREATE_DB_QUERY = "create table if not exists "+ TB_NAME + "(" +
            KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            KEY_START_TIME + " text," +
            KEY_END_TIME + " text," +
            KEY_DATE + " text," +
            KEY_NAME + " text," +
            KEY_DESCRIPTION + " text);";

    private DbHelper dbHelper;


    public Db(Context context){
        this.context = context;
        dbHelper = new DbHelper(context);
    }

    public long insert(DailyTask dailyTask){
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, dailyTask.getName());
        contentValues.put(KEY_DATE, dailyTask.getDate());
        contentValues.put(KEY_START_TIME, dailyTask.getStartTime());
        contentValues.put(KEY_END_TIME, dailyTask.getEndTime());
        contentValues.put(KEY_DESCRIPTION, dailyTask.getDescription());
        return dbHelper.getWritableDatabase().insert(TB_NAME, null, contentValues);
    }

    public long update(int id, DailyTask dailyTask){
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, dailyTask.getName());
        contentValues.put(KEY_DATE, dailyTask.getDate());
        contentValues.put(KEY_START_TIME, dailyTask.getStartTime());
        contentValues.put(KEY_END_TIME, dailyTask.getEndTime());
        contentValues.put(KEY_DESCRIPTION, dailyTask.getDescription());
        return dbHelper.getWritableDatabase().update(TB_NAME,  contentValues, KEY_ID+"="+id, null);
    }

    public Cursor readAll(){
        Cursor cursor = dbHelper.getReadableDatabase().query(TB_NAME,
                new String[]{KEY_ID, KEY_NAME, KEY_DATE, KEY_START_TIME, KEY_END_TIME, KEY_DESCRIPTION},
                null, null, null, null, null);
        cursor.moveToFirst();
        return cursor;
    }

    public Cursor read(int id){
        Cursor cursor = dbHelper.getReadableDatabase().query(TB_NAME,
                new String[]{KEY_ID, KEY_NAME, KEY_DATE, KEY_START_TIME, KEY_END_TIME, KEY_DESCRIPTION},
                KEY_ID+"="+id, null, null, null, null);
        cursor.moveToFirst();
        return cursor;
    }



    public boolean delete(int id){
        return dbHelper.getWritableDatabase().delete(TB_NAME, KEY_ID+"="+id, null) > 0;
    }
    private static class DbHelper extends SQLiteOpenHelper {

        DbHelper(Context context){
            super(context, DB_NAME, null, DB_VER);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_DB_QUERY);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            String sql = "Drop table if exists " + TB_NAME + ";";
            db.execSQL(sql);
            db.execSQL(CREATE_DB_QUERY);
            Log.i(LOG_TAG, "Upgraded databases from "+ oldVersion +" to "+newVersion);

        }
    }


}
