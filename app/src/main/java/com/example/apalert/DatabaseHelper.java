package com.example.apalert;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.widget.Toast;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "AppointmentApp.db";
    private static final String TB_NAME = "schedule_details";
    private static final String DROP_TB = "DROP TABLE IF EXISTS "+TB_NAME;
    private static final int VERSION_NUMBER = 1;

    private static final String SERIALNO = "serial_no";
    private static final String FULL_DATE = "full_date";
    private static final String FULL_TIME = "full_time";
    private static final String YEAR = "year";
    private static final String MONTH = "month";
    private static final String MONTH_OF_DAY = "month_of_day";
    private static final String HOUR = "hour";
    private static final String MINUTE = "minute";
    private Context context;

    private static final String CREATE_TABLE = "CREATE TABLE "+TB_NAME+"" +
            "("+SERIALNO+" INTEGER PRIMARY KEY AUTOINCREMENT," +
            "" + " "+FULL_DATE+" VARCHAR(255), "+FULL_TIME+" VARCHAR(255)," +
            " "+YEAR+" VARCHAR(255),"+MONTH+" VARCHAR(255), "+MONTH_OF_DAY+" VARCHAR(255)," +
            ""+HOUR+" VARCHAR(255), "+MINUTE+" VARCHAR(255))";

    private static final String DISPLAY_TABLE="SELECT * FROM "+TB_NAME;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, VERSION_NUMBER);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            db.execSQL(CREATE_TABLE);
        }catch (Exception e){
            Toast.makeText(context, "CREATE TABLE Exception: "+e, Toast.LENGTH_LONG);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try{
            db.execSQL(DROP_TB);
            onCreate(db);
        }catch (Exception e){
            Toast.makeText(context, "DROP TABLE Exception: "+e, Toast.LENGTH_LONG);
        }
    }

    //Insert remainder information
    public long insertRemInfo(String dateStr, String timeStr, String strYear, String strMonth, String strDay,
                              String hours, String minutes){

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FULL_DATE, dateStr);
        contentValues.put(FULL_TIME, timeStr);
        contentValues.put(YEAR, strYear);
        contentValues.put(MONTH, strMonth);
        contentValues.put(MONTH_OF_DAY, strDay);
        contentValues.put(HOUR, hours);
        contentValues.put(MINUTE, minutes);

        long rowID = sqLiteDatabase.insert(TB_NAME, null, contentValues);
        return rowID;
    }

    //get remainder information
    public Cursor displayRemInfo() {
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery(DISPLAY_TABLE,null);
        return  cursor;
    }

    //Delete ALL remainder information
    public void deleteRemInfo() {
        context.deleteDatabase(DB_NAME);
    }
}
