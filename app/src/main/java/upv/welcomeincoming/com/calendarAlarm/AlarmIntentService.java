package upv.welcomeincoming.com.calendarAlarm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import upv.welcomeincoming.com.Activity_Notificacion_Evento;
import upv.welcomeincoming.com.R;
import util.Preferencias;

/**
 * Created by Cristian on 07/08/2014.
 */
public class AlarmIntentService extends Service {

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
    }

    @SuppressWarnings("static-access")
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        NotificationManager mManager = (NotificationManager) this.getApplicationContext().
                getSystemService(this.getApplicationContext().NOTIFICATION_SERVICE);
        Intent intent1 = new Intent(this.getApplicationContext(), Activity_Notificacion_Evento.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (intent != null) {
            int id = intent.getIntExtra("id", 0);
            String nombre = intent.getStringExtra("nombre");
            String edificio = intent.getStringExtra("edificio");
            String hora = intent.getStringExtra("hora");

            intent1.putExtra("nombre", intent.getStringExtra("nombre"));
            intent1.putExtra("edificio", intent.getStringExtra("edificio"));
            intent1.putExtra("hora", intent.getStringExtra("hora"));

            PendingIntent pendingNotificationIntent = PendingIntent.
                    getActivity(this.getApplicationContext(), id, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
            Log.e("nombre", nombre);
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.ic_launcher)
                            .setContentTitle(nombre)
                            .setAutoCancel(true)
                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                            .setSubText(edificio)
                            .setContentText(getString(R.string.clasecomienza));
            mBuilder.setContentIntent(pendingNotificationIntent);
            if (Preferencias.getCalendarAlerts(getApplicationContext()))
                mManager.notify(id, mBuilder.build());
        }
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
}