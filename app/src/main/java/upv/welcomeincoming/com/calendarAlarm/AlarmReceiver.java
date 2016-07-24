package upv.welcomeincoming.com.calendarAlarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service1 = new Intent(context, AlarmIntentService.class);

        Log.e("nombre", intent.getStringExtra("nombre"));
        service1.putExtra("nombre", intent.getStringExtra("nombre"));
        service1.putExtra("id", intent.getIntExtra("id", 0));
        service1.putExtra("edificio", intent.getStringExtra("edificio"));
        service1.putExtra("hora", intent.getStringExtra("hora"));
        context.startService(service1);
    }
}