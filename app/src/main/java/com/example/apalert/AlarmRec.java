package com.example.apalert;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

public class AlarmRec extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ComponentName receiver = new ComponentName(context, AlarmRec.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,PackageManager.COMPONENT_ENABLED_STATE_ENABLED,PackageManager.DONT_KILL_APP);

        String get_data = intent.getExtras().getString("data");
        int s = intent.getExtras().getInt("data1");

        Intent intent_rec = new Intent(context,NotificationShow.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent_rec.putExtra("data",get_data);
        intent_rec.putExtra("data1",s);
        context.startService(intent_rec);

        Log.d("TAG", "This is AlarmRec...");
    }
}
