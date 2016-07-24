package upv.welcomeincoming.com.GCM;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import upv.welcomeincoming.com.Activity_Home;
import upv.welcomeincoming.com.R;
import upv.welcomeincoming.com.foro.Activity_Ver_Tema;
import upv.welcomeincoming.com.foro.DBHandler_NotificacionesForo;
import upv.welcomeincoming.com.foro.Tema;
import util.DBHandler_Horarios;
import util.Preferencias;

/**
 * Created by Cristian on 07/08/2014.
 */
public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 0;
    public static final int NOTIFICATION_ID_INFO = -1;
    private static final int NOTIFICATION_ID_DESLOGUEO = 3;
    final String TAG = "gcm";
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that upv.welcomeincoming.app.GCM
             * will be extended in the future with new message types, just ignore
             * any message types you're not interested in, or that you don't
             * recognize.
             */
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                //sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                String info = extras.getString("info");
                String desregistro = extras.getString("desregistro");
                if (info != null) {
                    sendNotificationInfo(info);
                    return;
                } else if (desregistro != null) {
                    sendNotificationDesregistro();
                    return;
                }
                sendNotification(extras.getString("tema"), extras.getString("nombreusuario"));
            } else if ("send_event".equals(messageType)) {
                sendNotificationRegistroOK(extras.getString("Mensaje"));
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }


    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a upv.welcomeincoming.app.GCM message.
    private void sendNotificationRegistroOK(String msg) {
        String texto = getString(R.string.msgNotificacionRegistro).replace("%%", "%");
        texto = String.format(texto, Preferencias.getNombre(this));//texto del mensaje
        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, Activity_Home.class), 0);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(getString(R.string.bienvenido))
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(texto))
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentText(texto);
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    private void sendNotification(String temajson, String nombreusuario) {
        Type tipoObjeto = new TypeToken<Tema>() {
        }.getType();
        Tema tema = new Gson().fromJson(temajson, tipoObjeto);

        if (!Preferencias.getForumAlerts(getApplicationContext()) || DBHandler_NotificacionesForo.notificacionDesactivada(getApplicationContext(), tema.getId())) {
            return;//salimos si la notificacion la tenemos desactivada
        }

        if (tema.getIdUsuario().equals(Preferencias.getDNI(getApplicationContext()))) {//es el usuario que ha creado el tema
            sendNotificationByTema(tema, nombreusuario);
        } else {
            sendNotificationByComent(tema, nombreusuario);
        }

    }

    private void sendNotificationByTema(Tema tema, String nombreusuario) {
        String msg = nombreusuario + " " + getString(R.string.NotificacionPublicadoEnTuTema) + "\n\"" + tema.getTitulo() + "\"";

        Intent intent = new Intent(this, Activity_Ver_Tema.class);
        intent.putExtra("idtema", tema.getId());
        intent.putExtra("titulo", tema.getTitulo());
        intent.putExtra("descripcion", tema.getDescripcion());
        intent.putExtra("fechaCreacion", tema.getFecha_creacion().getTime());
        intent.putExtra("lang", tema.getLanguage());
        intent.putExtra("idusuario", tema.getIdUsuario());
        intent.putExtra("nombreusuario", tema.getNombreUsuario());
        intent.putExtra("apellidos", tema.getApellidoUsuario());
        PendingIntent contentIntent = PendingIntent.getActivity(this, tema.getId(), intent, PendingIntent.FLAG_ONE_SHOT);

        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_forum_notification)
                .setContentTitle(getString(R.string.forum))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(tema.getId(), mBuilder.build());
    }

    private void sendNotificationByComent(Tema tema, String nombreusuario) {
        String msg = nombreusuario + " " + getString(R.string.NotificacionPublicadoEnComentario) + "\n\"" + tema.getTitulo() + "\"";

        Intent intent = new Intent(this, Activity_Ver_Tema.class);
        intent.putExtra("idtema", tema.getId());
        intent.putExtra("titulo", tema.getTitulo());
        intent.putExtra("descripcion", tema.getDescripcion());
        intent.putExtra("fechaCreacion", tema.getFecha_creacion().getTime());
        intent.putExtra("lang", tema.getLanguage());
        intent.putExtra("idusuario", tema.getIdUsuario());
        intent.putExtra("nombreusuario", tema.getNombreUsuario());
        intent.putExtra("apellidos", tema.getApellidoUsuario());
        intent.setFlags(PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent contentIntent = PendingIntent.getActivity(this, tema.getId(), intent, PendingIntent.FLAG_ONE_SHOT);

        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_forum_notification)
                .setContentTitle(getString(R.string.forum))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(tema.getId(), mBuilder.build());
    }

    private void sendNotificationInfo(String info) {
        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, Activity_Home.class), 0);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(getString(R.string.bienvenido))
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(info))
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentText(info);
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID_INFO, mBuilder.build());
    }

    private void sendNotificationDesregistro() {
        if (Preferencias.logeado(getApplicationContext())) {
            //nos deslogueamos y lo notificamos al usuario
            Preferencias.desloguearse(getApplicationContext());
            borrarDatosUsuario();
            String texto = getString(R.string.textoNotificacionDeslogueo);
            String titulo = getString(R.string.sesionCerradaNotificationtitle);
            mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, Activity_Home.class), 0);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setContentTitle(titulo)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(texto))
                    .setAutoCancel(true)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setContentText(texto);
            mBuilder.setContentIntent(contentIntent);
            mNotificationManager.notify(NOTIFICATION_ID_DESLOGUEO, mBuilder.build());
        }
    }


    private void borrarDatosUsuario() {
        SQLiteDatabase db = new DBHandler_Horarios(getApplicationContext()).getWritableDatabase();
        db.execSQL("DELETE FROM Evento");
        db.execSQL("DELETE FROM Horario");
    }


}