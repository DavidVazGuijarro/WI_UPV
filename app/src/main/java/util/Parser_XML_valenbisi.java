package util;

import android.database.sqlite.SQLiteDatabase;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by joaquin on 26/04/14.
 */
public class Parser_XML_valenbisi {

    private String tipo_xml;

    /*En el constructor de la clase pasamos el nombre del fichero que vamos a parsear*/
    public Parser_XML_valenbisi() {
    }

    /*Como resultado de parsear el fichero se devuelve una lista de objetos: asignatura,schooñ,transporte o valencia
    dependiendo del fichero pasado como arcgumento*/
    public void parsear(InputStream fichero, SQLiteDatabase db) throws IOException, XmlPullParserException {

        /*La lista que vamos a devolver*/
        XmlPullParser parseado = XmlPullParserFactory.newInstance().newPullParser();
        parseado.setInput(fichero, null);
        int tipoevento = parseado.getEventType();
        MarcadorValenbisi mvb = null;

        while (tipoevento != XmlPullParser.END_DOCUMENT) {

            if ((tipoevento == XmlPullParser.START_TAG) && (parseado.getName().equals("marker"))) {

                String direccion = parseado.getAttributeValue(null, "address");
                int numero = Integer.parseInt(parseado.getAttributeValue(null, "number"));
                String latitud = parseado.getAttributeValue(null, "lat");
                String longitud = parseado.getAttributeValue(null, "lng");
                int numeroPlazas = 0;
                int plazasLibres = 0;
                mvb = new MarcadorValenbisi(numero, direccion, longitud, latitud, numeroPlazas, plazasLibres);
                db.execSQL("INSERT OR IGNORE INTO Valenbisi (num,direccion,longitud,latitud,numPlazas,plazasDisponibles) VALUES (" + mvb.getNumero() + ",\"" + mvb.getDireccion() + "\"," + mvb.getLongitud() + "," + mvb.getLatitud() + "," + mvb.getNumeroPlazas() + "," + mvb.getPlazasDisponibles() + ");");

                parseado.next();
                tipoevento = parseado.getEventType();
            } else {
                parseado.next();
                tipoevento = parseado.getEventType();
            }
        }
    }

    public void parsearPlazas(MarcadorValenbisi mvb, SQLiteDatabase db) {
        URL urlDatos = null;
        try {
            urlDatos = new URL("http://www.valenbisi.es/service/stationdetails/valence/" + mvb.getNumero());

            HttpURLConnection urlConnection1 = (HttpURLConnection) urlDatos.openConnection();
            InputStream in1 = new BufferedInputStream(urlConnection1.getInputStream());
            XmlPullParser parseado1 = XmlPullParserFactory.newInstance().newPullParser();
            parseado1.setInput(in1, null);
            int tipoevento1 = parseado1.getEventType();
            int plazasTotal = 0, plazasDisponibles = 0;
            while (tipoevento1 != XmlPullParser.END_DOCUMENT) {
                if ((tipoevento1 == XmlPullParser.START_TAG) && (parseado1.getName().equals("total"))) {
                    plazasTotal = Integer.parseInt(parseado1.nextText());
                } else if ((tipoevento1 == XmlPullParser.START_TAG) && (parseado1.getName().equals("available"))) {
                    plazasDisponibles = Integer.parseInt(parseado1.nextText());
                }
                parseado1.next();
                tipoevento1 = parseado1.getEventType();
            }
            db.execSQL("UPDATE Valenbisi SET numPlazas=" + plazasTotal + ", plazasDisponibles=" + plazasDisponibles + " WHERE num=" + mvb.getNumero() + ";");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

