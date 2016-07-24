package util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHandler_Horarios extends SQLiteOpenHelper {

    public DBHandler_Horarios(Context contexto) {
        super(contexto, "dbWelcomeIncoming.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //	Create	here	the	DB	by	code
        Log.e("bd", "Creamos la base de datos");
        db.execSQL("CREATE  TABLE main.Horario (id VARCHAR PRIMARY KEY  NOT NULL  UNIQUE , Nombre VARCHAR NOT NULL , Url VARCHAR NOT NULL );");
        db.execSQL("CREATE  TABLE \"Evento\" (\"id\" INTEGER PRIMARY KEY  NOT NULL  UNIQUE , \"nombre\" VARCHAR, \"profesor\" VARCHAR, \"ubicacion\" VARCHAR, \"fecha\" VARCHAR, \"alertado\" INTEGER, \"idHorario\" VARCHAR NOT NULL);");
        db.execSQL("CREATE  TABLE \"Noticia\" (\"id\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL  UNIQUE , \"titulo\" VARCHAR, \"url\" VARCHAR NOT NULL, \"fecha\" VARCHAR);");
        db.execSQL("CREATE  TABLE \"Valenbisi\" (\"num\" INTEGER PRIMARY KEY NOT NULL UNIQUE , \"direccion\" VARCHAR, \"longitud\" VARCHAR NOT NULL, \"latitud\" VARCHAR NOT NULL, \"numPlazas\" INTEGER NOT NULL, \"plazasDisponibles\" INTEGER NOT NULL);");
        db.execSQL("CREATE  TABLE \"Edificio\" (\"id\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE ,\"num\" VARCHAR NOT NULL, \"longitud\" VARCHAR NOT NULL, \"latitud\" VARCHAR NOT NULL, \"info\" VARCHAR);");
        db.execSQL("CREATE  TABLE \"Metro\" (\"id\" INTEGER PRIMARY KEY NOT NULL UNIQUE ,\"nombre\" VARCHAR NOT NULL, \"latitud\" VARCHAR NOT NULL, \"longitud\" VARCHAR NOT NULL, \"lineas\" VARCHAR NOT NULL);");
        db.execSQL("CREATE  TABLE \"Emt\" (\"id\" INTEGER PRIMARY KEY NOT NULL UNIQUE ,\"nombre\" VARCHAR NOT NULL, \"latitud\" VARCHAR NOT NULL, \"longitud\" VARCHAR NOT NULL);");
        db.execSQL("CREATE  TABLE \"Interes\" (\"id\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE ,\"nombre\" VARCHAR NOT NULL, \"latitud\" VARCHAR NOT NULL, \"longitud\" VARCHAR NOT NULL);");
        db.execSQL("CREATE  TABLE \"Transporte\" (\"id\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE ,\"nombre\" VARCHAR NOT NULL, \"descripcion\" VARCHAR NOT NULL, \"telefono\" VARCHAR NOT NULL, \"url\" VARCHAR NOT NULL);");
    }

    @Override
    public void onUpgrade(
            SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
