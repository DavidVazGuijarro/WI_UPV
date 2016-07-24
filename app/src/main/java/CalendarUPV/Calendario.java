package calendarupv;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Cristian on 15/07/2014.
 */
public class Calendario {
    private String uid;
    private String nombre;
    private String url;

    public Calendario(String uid, String nombre, String url) {
        this.uid = uid;
        this.nombre = nombre;
        this.url = url;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean existeEnDB(SQLiteDatabase db) {
        String sql = "SELECT * FROM Evento WHERE idHorario=\"" + uid + "\";";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public List<Evento> obtenerEventosDB(SQLiteDatabase db) {
        //poner fecha actual
        Calendar c = Calendar.getInstance();
        //String fecha = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss").format(c.getTime());
        String fecha = "2014-02-30 00:00:00";
        String sql = "SELECT * FROM Evento WHERE fecha>=\"" + fecha + "\" and idHorario = \"" + uid + "\";";
        Cursor cursor = db.rawQuery(sql, null);
        List<Evento> eventos = new ArrayList<Evento>();
        while (cursor.moveToNext()) {
            Evento evento = new Evento(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getInt(5), cursor.getString(6));
            eventos.add(evento);
        }
        cursor.close();
        return eventos;
    }

    public void insertarCalendariosDB(SQLiteDatabase db) {
        String sql = "INSERT OR IGNORE INTO \"main\".\"Horario\" (\"id\",\"Nombre\",\"Url\") VALUES (\"" + uid + "\",\"" + this.nombre + "\",\"" + this.url + "\");";
        db.execSQL(sql);
    }
}
