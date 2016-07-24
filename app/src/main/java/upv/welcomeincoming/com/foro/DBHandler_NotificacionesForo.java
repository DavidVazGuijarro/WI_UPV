package upv.welcomeincoming.com.foro;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import util.Preferencias;

public class DBHandler_NotificacionesForo extends SQLiteOpenHelper {

    public DBHandler_NotificacionesForo(Context contexto) {
        super(contexto, "dbForo.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //	Create	here	the	DB	by	code
        Log.e("bd", "Creamos la base de datos del foro");
        db.execSQL("CREATE  TABLE \"main\".\"notificaciones\" (\"idtema\" INTEGER NOT NULL , \"idusuario\" VARCHAR NOT NULL , PRIMARY KEY (\"idtema\", \"idusuario\"))");
    }

    @Override
    public void onUpgrade(
            SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public static void desactivarNotificacion(Context context, int idtema) {
        SQLiteDatabase db = new DBHandler_NotificacionesForo(context).getWritableDatabase();
        String idusuario = Preferencias.getDNI(context);
        if (idusuario != null && !idusuario.isEmpty()) {
            db.execSQL("insert or replace into notificaciones values(" + idtema + ", '" + idusuario + "');");
        }

    }

    public static void activarNotificacion(Context context, int idtema) {
        SQLiteDatabase db = new DBHandler_NotificacionesForo(context).getWritableDatabase();
        String idusuario = Preferencias.getDNI(context);
        if (idusuario != null && !idusuario.isEmpty()) {
            db.execSQL("delete from notificaciones where idtema = " + idtema + " and idusuario = '" + idusuario + "';");
        }
    }

    //guardan los temas que tienen notificaciones desactivadas.
    public static boolean notificacionDesactivada(Context context, int idtema) {
        boolean res = false;
        String idusuario = Preferencias.getDNI(context);
        SQLiteDatabase db = new DBHandler_NotificacionesForo(context).getWritableDatabase();
        if (idusuario != null && !idusuario.isEmpty()) {
            final Cursor cursor = db.rawQuery("SELECT * from notificaciones where idtema = " + idtema + " and idusuario = '" + idusuario + "'", null);
            if (cursor != null) {
                try {
                    res = cursor.moveToFirst();
                } finally {
                    cursor.close();
                }
            }
        }
        return res;
    }

    public static void borrarTodas(Context context) {
        SQLiteDatabase db = new DBHandler_NotificacionesForo(context).getWritableDatabase();
        String idusuario = Preferencias.getDNI(context);
        if (idusuario != null && !idusuario.isEmpty()) {
            db.execSQL("delete from notificaciones where idusuario = '" + idusuario + "';");
        }
    }
}
