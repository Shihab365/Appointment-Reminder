package com.example.apalert;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Calendar;

public class AfterBootSetup extends IntentService {

    DatabaseHelper databaseHelper;

    public AfterBootSetup() {
        super("AfterBootSetup");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        getAlarmInfo();
        Log.d("TAG", "This is onHandleIntent...");
    }

    private void getAlarmInfo(){
        Log.d("TAG", "This is getAlarmInfo...");

        databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        String strYear="", strMonth="", strDay="", strHour="", strMinute="";
        int intYear, intMonth, intDay, intHour, intMinute;

        Cursor cursor=databaseHelper.displayRemInfo();
        while (cursor.moveToNext()){
            strYear = cursor.getString(3);
            strMonth = cursor.getString(4);
            strDay = cursor.getString(5);
            strHour = cursor.getString(6);
            strMinute = cursor.getString(7);
        }
        intYear = Integer.parseInt(strYear);
        intMonth = Integer.parseInt(strMonth);
        intDay = Integer.parseInt(strDay);
        intHour = Integer.parseInt(strHour);
        intMinute = Integer.parseInt(strMinute);

        Intent intent = new Intent(AfterBootSetup.this, AlarmRec.class);
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

        Calendar ca = Calendar.getInstance();
        ca.setTimeInMillis(System.currentTimeMillis());
        ca.set(Calendar.YEAR, intYear);
        ca.set(Calendar.MONTH, intMonth);
        ca.set(Calendar.DAY_OF_MONTH, intDay);
        ca.set(Calendar.HOUR_OF_DAY, intHour);
        ca.set(Calendar.MINUTE, intMinute);

        intent.putExtra("data", "on");
        intent.putExtra("data1", 1);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(AfterBootSetup.this,1,intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, ca.getTimeInMillis(), pendingIntent);
    }
}
