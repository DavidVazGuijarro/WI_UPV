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
public class Parser_XML_interes {

    private String tipo_xml;

    /*En el constructor de la clase pasamos el nombre del fichero que vamos a parsear*/
    public Parser_XML_interes() {
    }

    public void parsear(InputStream fichero, SQLiteDatabase db) throws XmlPullParserException, IOException {
        XmlPullParserFactory pullParserFactory;
        pullParserFactory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = pullParserFactory.newPullParser();
        parser.setInput(fichero, null);
        int eventType = parser.getEventType();
        Interes interes = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {

            String name = null;
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    break;
                case XmlPullParser.START_TAG:

                    name = parser.getName();

                    if (name.equals("interes")) {
                        interes = new Interes();
                    } else if (interes != null) {
                        if (name.equals("nombre")) {
                            interes.setNombre(parser.nextText());
                        } else if (name.equals("latitud")) {
                            interes.setLatitud(parser.nextText());
                        } else if (name.equals("longitud")) {
                            interes.setLongitud(parser.nextText());
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase("interes") && interes != null) {
                        try {
                            db.execSQL("INSERT OR IGNORE INTO Interes (nombre,latitud,longitud) VALUES ('" + interes.getNombre() + "','" + interes.getLatitud() + "','" + interes.getLongitud() + "');");
                        } catch (SQLiteException e) {
                            Log.e("error", e.getMessage());
                        }
                    }
            }
            eventType = parser.next();
        }
    }
}

