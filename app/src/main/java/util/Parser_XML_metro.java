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
public class Parser_XML_metro {

    private String tipo_xml;

    /*En el constructor de la clase pasamos el nombre del fichero que vamos a parsear*/
    public Parser_XML_metro() {
    }

    public void parsear(InputStream fichero, SQLiteDatabase db) throws XmlPullParserException, IOException {
        XmlPullParserFactory pullParserFactory;
        pullParserFactory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = pullParserFactory.newPullParser();
        parser.setInput(fichero, null);
        int eventType = parser.getEventType();
        Metro metro = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {

            String name = null;
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    break;
                case XmlPullParser.START_TAG:

                    name = parser.getName();

                    if (name.equals("metro")) {
                        metro = new Metro();
                    } else if (metro != null) {
                        if (name.equals("nombre")) {
                            metro.setNombre(parser.nextText());
                        } else if (name.equals("id")) {
                            metro.setId(Integer.parseInt(parser.nextText()));
                        } else if (name.equals("latitud")) {
                            metro.setLatitud(parser.nextText());
                        } else if (name.equals("longitud")) {
                            metro.setLongitud(parser.nextText());
                        } else if (name.equals("lineas")) {
                            metro.setLineas(parser.nextText());
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase("metro") && metro != null) {
                        try {
                            db.execSQL("INSERT OR IGNORE INTO Metro (id,nombre,latitud,longitud,lineas) VALUES ('" + metro.getId() + "','" + metro.getNombre() + "','" + metro.getLatitud() + "','" + metro.getLongitud() + "','" + metro.getLineas() + "');");
                        } catch (SQLiteException e) {
                            Log.e("error", e.getMessage());
                        }
                    }
            }
            eventType = parser.next();
        }
    }
}

