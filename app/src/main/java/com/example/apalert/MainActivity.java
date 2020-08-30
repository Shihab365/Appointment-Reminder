package com.example.apalert;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    ImageButton btnAddAlarm, btnCancelAlarm, btnClearAlarm;
    TextView tvDate, tvTime;
    Calendar now = Calendar.getInstance();
    DatePickerDialog dpd;
    TimePickerDialog tpd;

    AlarmManager alarmManager;
    PendingIntent pendingIntent;

    Intent intent;
    int REQ_CODE=0;
    String ampm, dateStr, timeStr, strYear, strMonth, strDay, hours, minutes;
    String disFullDate, disFullTime;

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
        intent=new Intent(MainActivity.this, AlarmRec.class);

        btnAddAlarm = findViewById(R.id.btn_alarm_addID);
        btnCancelAlarm = findViewById(R.id.btn_alarm_cancelID);
        btnClearAlarm = findViewById(R.id.btn_alarm_clearID);
        tvDate = findViewById(R.id.tv_date_id);
        tvTime = findViewById(R.id.tv_time_id);

        Cursor cursor=databaseHelper.displayRemInfo();
        while (cursor.moveToNext()){
            disFullDate = cursor.getString(1);
            disFullTime = cursor.getString(2);

            tvDate.setText(disFullDate);
            tvTime.setText(disFullTime);
        }

        dpd=DatePickerDialog.newInstance(MainActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );

        tpd=TimePickerDialog.newInstance(MainActivity.this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                now.get(Calendar.SECOND),
                false
        );

        btnAddAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dpd.show(getFragmentManager(),"DatePickerDialog");
            }
        });

        btnCancelAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAlarm();
            }
        });

        btnClearAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper.deleteRemInfo();
                tvDate.setText("[Not yet entered]");
                tvTime.setText("[Not yet entered]");
                Toast.makeText(MainActivity.this, "All Record Delete!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //set date and time
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        now.set(Calendar.YEAR, year);
        now.set(Calendar.MONDAY, monthOfYear);
        now.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        tpd.show(getFragmentManager(),"TimePickerDialog");

        strYear = String.valueOf(year);
        strMonth = String.valueOf(monthOfYear);
        strDay = String.valueOf(dayOfMonth);

        dateStr=dayOfMonth+" - "+(monthOfYear+1)+" - "+year;
        tvDate.setText(dateStr);
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        now.set(Calendar.HOUR_OF_DAY, hourOfDay);
        now.set(Calendar.MINUTE, minute);
        now.set(Calendar.SECOND, second);

        hours = String.valueOf(hourOfDay);
        minutes = String.valueOf(minute);

        if(hourOfDay>=0 && hourOfDay<=11){
            if(hourOfDay==0){
                hours=String.valueOf(12);
            }
            ampm="am";
        }
        else{
            if(hourOfDay>=13 && hourOfDay<=23){
                hours=String.valueOf(hourOfDay-12);
            }
            ampm="pm";
        }
        if(minute<10){
            minutes=String.valueOf("0"+String.valueOf(minute));
        }

        timeStr = hours+" : "+minutes+ " "+ampm;
        tvTime.setText(timeStr);

        String strHour = String.valueOf(hourOfDay);
        String strMinute = String.valueOf(minute);

        //....Insert database....
        String db_date = dateStr;
        String db_time = timeStr;
        String db_year = strYear;
        String db_month = strMonth;
        String db_day = strDay;
        String db_hour = strHour;
        String db_minute = strMinute;
        long rowID = databaseHelper.insertRemInfo(db_date, db_time, db_year, db_month, db_day,
                db_hour, db_minute);


        intent.putExtra("data","on");
        REQ_CODE++;
        intent.putExtra("data1",REQ_CODE);

        pendingIntent=PendingIntent.getBroadcast(MainActivity.this,REQ_CODE,intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,now.getTimeInMillis(),pendingIntent);
        Toast.makeText(this, "Alarm Set Done!", Toast.LENGTH_SHORT).show();
    }

    public void cancelAlarm(){
        alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
        intent=new Intent(MainActivity.this,AlarmRec.class);
        intent.putExtra("data","off");
        REQ_CODE++;
        pendingIntent=PendingIntent.getBroadcast(MainActivity.this,REQ_CODE,intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,now.getTimeInMillis(),pendingIntent);
        Toast.makeText(this, "Alarm off!", Toast.LENGTH_SHORT).show();
    }
}
