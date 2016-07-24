package util;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by joaquin on 26/04/14.
 */
public class Parser_XML {

    private String tipo_xml;
    private String nombre_escuela;
    private boolean crear_lista;
    private List asignaturas;
    private Escuela escuela;
    private String auxiliar;

    /*En el constructor de la clase pasamos el nombre del fichero que vamos a parsear*/
    public Parser_XML(String opcion) {
        tipo_xml = opcion;
    }

    /*Como resultado de parsear el fichero se devuelve una lista de objetos: asignatura,schooñ,transporte o valencia
    dependiendo del fichero pasado como arcgumento*/
    public List parseando(InputStream fichero) {

        /*La lista que vamos a devolver*/
        List resultado = new ArrayList();

        try {
            XmlPullParser parseado = XmlPullParserFactory.newInstance().newPullParser();
            parseado.setInput(fichero, null);
            int tipoevento = parseado.getEventType();

        /*xml: telefonos.xml*/
            if (tipo_xml.equals("telefonos")) {

                /*Vamos a devolver un ArrayList de objetos school*/
                resultado = new ArrayList<Escuela>();

                while (tipoevento != XmlPullParser.END_DOCUMENT) {

                    if ((tipoevento == XmlPullParser.START_TAG) && (parseado.getName().equals("escuela"))) {
                        Escuela escuela = new Escuela();

                        escuela.setEscuelanombre(parseado.getAttributeValue(null, "escuelanombre"));
                        escuela.setUrlprincipal(parseado.getAttributeValue(null, "urlprincipal"));
                        escuela.setUrldocencia(parseado.getAttributeValue(null, "urldocencia"));
                        escuela.setEscuelamail(parseado.getAttributeValue(null, "escuelamail"));
                        escuela.setEscuelafax(parseado.getAttributeValue(null, "escuelafax"));
                        escuela.setCoordinadornombre(parseado.getAttributeValue(null, "coordinadornombre"));
                        escuela.setCoordinadormail(parseado.getAttributeValue(null, "coordinadormail"));
                        escuela.setCoordinadortelefono(parseado.getAttributeValue(null, "coordinadortelefono"));
                        escuela.setCoordinadorextension(parseado.getAttributeValue(null, "coordinadorextension"));
                        escuela.setTecniconombre(parseado.getAttributeValue(null, "tecniconombre"));
                        escuela.setTecnicomail(parseado.getAttributeValue(null, "tecnicomail"));
                        escuela.setTecnicotelefono(parseado.getAttributeValue(null, "tecnicotelefono"));
                        escuela.setTecnicoextension(parseado.getAttributeValue(null, "tecnicoextension"));

                        resultado.add(escuela);
                        parseado.next();
                        tipoevento = parseado.getEventType();
                    } else {
                        parseado.next();
                        tipoevento = parseado.getEventType();
                    }

                }
            }
        /*xml: asignaturas.xml*/
            else if (tipo_xml.equals("asignaturas")) {

            /*Aqui tambien devolvemos una lista de objetos school pero
            en este caso vamos rellenar en cada objeto su lista de asignatiras*/
                resultado = new ArrayList<Escuela>();

                while (tipoevento != XmlPullParser.END_DOCUMENT) {

                    if ((tipoevento == XmlPullParser.START_TAG) && (parseado.getName().equals("escuela"))) {
                        nombre_escuela = parseado.getAttributeValue(null, "nombre");
                    /*Cada escuela tendra una lista de objetos asignatura*/
                        asignaturas = new ArrayList<asignatura>();
                        crear_lista = true;
                        parseado.next();
                        tipoevento = parseado.getEventType();
                    } else if ((tipoevento == XmlPullParser.START_TAG) && (parseado.getName().equals("asignatura"))) {
                        while (crear_lista) {

                            if (tipoevento == XmlPullParser.START_TAG) {
                                asignatura nueva = new asignatura();
                                nueva.setCodigo(parseado.getAttributeValue(null, "codigo"));
                                nueva.setNombre(parseado.getAttributeValue(null, "nombre"));
                                nueva.setUrl(parseado.getAttributeValue(null, "url"));
                                nueva.setCreditos(parseado.getAttributeValue(null, "creditos"));
                                nueva.setSemestre(parseado.getAttributeValue(null, "semestre"));
                                asignaturas.add(nueva);
                            }
                            parseado.next();
                            tipoevento = parseado.getEventType();
                            if ((tipoevento == XmlPullParser.END_TAG) && (parseado.getName().equals("escuela"))) {
                                crear_lista = false;
                            }
                        }
                        Escuela escuela = new Escuela();
                        escuela.setEscuelanombre(nombre_escuela);
                        escuela.setAsignaturas(asignaturas);
                        resultado.add(escuela);
                        parseado.next();
                        tipoevento = parseado.getEventType();
                    } else {
                        parseado.next();
                        tipoevento = parseado.getEventType();
                    }


                }


            }
        /*xml: transportesl*/
            else if (tipo_xml.equals("transportes")) {

            /*Vamos a devolver un ArrayList de objetos transporte*/
                resultado = new ArrayList<transporte>();

                while (tipoevento != XmlPullParser.END_DOCUMENT) {

                    if ((tipoevento == XmlPullParser.START_TAG) && !(parseado.getName().equals("transportes"))) {

                        transporte nuevo = new transporte();
                        nuevo.setNombre(parseado.getName());
                        nuevo.setUrl(parseado.getAttributeValue(null, "url"));
                        nuevo.setDescripcion(parseado.getAttributeValue(null, "descripcion"));
                        nuevo.setTelefono(parseado.getAttributeValue(null, "telefono"));
                        resultado.add(nuevo);
                        parseado.next();
                        tipoevento = parseado.getEventType();
                    } else {
                        parseado.next();
                        tipoevento = parseado.getEventType();
                    }


                }

            }
        /*xml: valencia_intro.xml*/
            else if (tipo_xml.equals("valencia_intro")) {

            /*Vamos a devolver un ArrayList de objetos valencia*/
                resultado = new ArrayList<valencia>();

                while (tipoevento != XmlPullParser.END_DOCUMENT) {

                    if ((tipoevento == XmlPullParser.START_TAG) && (parseado.getName().equals("valencia"))) {

                        valencia nuevo = new valencia();
                        nuevo.setDescripcion(parseado.getAttributeValue(null, "descripcion"));
                        resultado.add(nuevo);
                        parseado.next();
                        tipoevento = parseado.getEventType();
                    } else {
                        parseado.next();
                        tipoevento = parseado.getEventType();
                    }
                }

            }
        } catch (Exception e) {
            Log.e("WelcomeIncoming", "Error leyendo XML desde fichero");
        }

        return resultado;
    }

}

