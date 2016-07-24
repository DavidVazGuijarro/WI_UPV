package util;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;


/**
 * Created by joaquin on 26/04/14.
 */
public class Parser_XML_edificios {

    private String tipo_xml;

    /*En el constructor de la clase pasamos el nombre del fichero que vamos a parsear*/
    public Parser_XML_edificios() {
    }

    public void parsearEdificios(InputStream fichero, SQLiteDatabase db) throws XmlPullParserException, IOException {
        XmlPullParserFactory pullParserFactory;
        pullParserFactory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = pullParserFactory.newPullParser();
        parser.setInput(fichero, null);
        int eventType = parser.getEventType();
        MarcadorEdificio edificio = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {

            String name = null;
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    break;
                case XmlPullParser.START_TAG:

                    name = parser.getName();

                    Log.e("edificios nombre", parser.getName());
                    if (name.equals("edificio")) {
                        Log.e("edificios", "creo el edificio");
                        edificio = new MarcadorEdificio();
                    } else if (edificio != null) {
                        if (name.equals("id")) {
                            edificio.setNumero(parser.nextText());
                        } else if (name.equals("longitud")) {
                            edificio.setLongitud(parser.nextText());
                        } else if (name.equals("latitud")) {
                            edificio.setLatitud(parser.nextText());
                        } else if (name.equals("info")) {
                            edificio.setInformacion(parser.nextText());
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase("edificio") && edificio != null) {
                        try {
                            db.execSQL("INSERT OR IGNORE INTO Edificio (num,longitud,latitud,info) VALUES ('" + edificio.getNumero() + "','" + edificio.getLongitud() + "','" + edificio.getLatitud() + "','" + edificio.getInformacion() + "');");
                        } catch (SQLiteException e) {
                            Log.e("error", e.getMessage());
                        }
                    }
            }
            eventType = parser.next();
        }
    }
}

