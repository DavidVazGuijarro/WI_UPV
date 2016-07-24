package upv.welcomeincoming.com.foro;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import upv.welcomeincoming.com.GCM.GCMConfig;
import upv.welcomeincoming.com.GCM.MessageSender;
import util.Preferencias;


/**
 * Created by Marcos on 21/07/14.
 */
public class Controlador {

    private static Controlador control;
    //private final String urlservidor = "marpeso.noip.me:8089/welcomeincoming";//PROVISIONAL
    private final String urlservidor = "marpeso.noip.me:8180/welcomeincoming";//FIJO

    private Controlador() throws Exception {
    }

    public static Controlador dameControlador() throws Exception {
        if (control == null) {
            control = new Controlador();
        }
        return control;
    }

    public ArrayList<Tema> getTemasTodos(int limit, Context context) {
        ArrayList<Tema> listaTemas = new ArrayList<Tema>();
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("idusuario", Preferencias.getDNI(context)));
        pairs.add(new BasicNameValuePair("pin", Preferencias.getPIN(context)));
        pairs.add(new BasicNameValuePair("tipo", "todos"));
        pairs.add(new BasicNameValuePair("limit", "" + limit));


        try {
            URL url = new URL("http://" + urlservidor + "/getTemas");
            HttpClient httpclient = new DefaultHttpClient();
            /*Creamos el objeto de HttpClient que nos permitira conectarnos mediante peticiones http*/
            HttpPost httppost = new HttpPost("http://" + urlservidor + "/getTemas");
            /*El objeto HttpPost permite que enviemos una peticion de tipo POST a una URL especificada*/
            //AÑADIR PARAMETROS
        /*Una vez añadidos los parametros actualizamos la entidad de httppost, esto quiere decir en pocas palabras anexamos los parametros al objeto para que al enviarse al servidor envien los datos que hemos añadido*/
            httppost.setEntity(new UrlEncodedFormEntity(pairs));
            /*Finalmente ejecutamos enviando la info al server*/
            HttpResponse resp = httpclient.execute(httppost);
            //Si el codigo de respuesta no es OK devolvemos null
            int statusCode = resp.getStatusLine().getStatusCode();
            if (!("" + statusCode).startsWith("2")) {
                Log.e("getTemasTodos error", "status code = " + statusCode);
                return null;
            }
            HttpEntity ent = resp.getEntity();/*y obtenemos una respuesta*/

            String response = EntityUtils.toString(ent);
            Gson gson = new Gson();
            Type tipoObjeto = new TypeToken<List<Tema>>() {
            }.getType();
            listaTemas = gson.fromJson(response, tipoObjeto);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return listaTemas;
    }

    public ArrayList<Tema> getTemasMe(int limit, Context context) {
        ArrayList<Tema> listaTemas = new ArrayList<Tema>();
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("idusuario", Preferencias.getDNI(context)));
        pairs.add(new BasicNameValuePair("pin", Preferencias.getPIN(context)));
        pairs.add(new BasicNameValuePair("tipo", "byMe"));
        pairs.add(new BasicNameValuePair("limit", "" + limit));
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://" + urlservidor + "/getTemas");
            httppost.setEntity(new UrlEncodedFormEntity(pairs));
            HttpResponse resp = httpclient.execute(httppost);
            int statusCode = resp.getStatusLine().getStatusCode();
            if (!("" + statusCode).startsWith("2")) {
                Log.e("getTemasMe error", "status code = " + statusCode);
                return null;
            }
            HttpEntity ent = resp.getEntity();/*y obtenemos una respuesta*/
            String response = EntityUtils.toString(ent);
            Gson gson = new Gson();
            Type tipoObjeto = new TypeToken<List<Tema>>() {
            }.getType();
            listaTemas = gson.fromJson(response, tipoObjeto);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return listaTemas;
    }

    public ArrayList<Tema> getTemasComentByMe(int limit, Context context) {
        ArrayList<Tema> listaTemas = new ArrayList<Tema>();
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("idusuario", Preferencias.getDNI(context)));
        pairs.add(new BasicNameValuePair("pin", Preferencias.getPIN(context)));
        pairs.add(new BasicNameValuePair("tipo", "comentByMe"));
        pairs.add(new BasicNameValuePair("limit", "" + limit));


        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://" + urlservidor + "/getTemas");
            httppost.setEntity(new UrlEncodedFormEntity(pairs));
            HttpResponse resp = httpclient.execute(httppost);
            int statusCode = resp.getStatusLine().getStatusCode();
            if (!("" + statusCode).startsWith("2")) {
                Log.e("getTemasComentMe error", "status code = " + statusCode);
                return null;
            }
            HttpEntity ent = resp.getEntity();/*y obtenemos una respuesta*/

            String response = EntityUtils.toString(ent);
            Gson gson = new Gson();
            Type tipoObjeto = new TypeToken<List<Tema>>() {
            }.getType();
            listaTemas = gson.fromJson(response, tipoObjeto);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return listaTemas;
    }

    public ArrayList<Tema> getTemasBuscar(int limit, Context context, String word) {
        ArrayList<Tema> listaTemas = new ArrayList<Tema>();
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        //obtener usuario y pass de las preferencias.
        pairs.add(new BasicNameValuePair("idusuario", Preferencias.getDNI(context)));
        pairs.add(new BasicNameValuePair("pin", Preferencias.getPIN(context)));
        pairs.add(new BasicNameValuePair("tipo", "contains"));
        pairs.add(new BasicNameValuePair("limit", "" + limit));
        pairs.add(new BasicNameValuePair("word", word));


        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://" + urlservidor + "/getTemas");
            httppost.setEntity(new UrlEncodedFormEntity(pairs));
            HttpResponse resp = httpclient.execute(httppost);
            int statusCode = resp.getStatusLine().getStatusCode();
            if (!("" + statusCode).startsWith("2")) {
                Log.e("getTemasBuscar error", "status code = " + statusCode);
                return null;
            }
            HttpEntity ent = resp.getEntity();/*y obtenemos una respuesta*/

            String response = EntityUtils.toString(ent);
            Gson gson = new Gson();
            Type tipoObjeto = new TypeToken<List<Tema>>() {
            }.getType();
            listaTemas = gson.fromJson(response, tipoObjeto);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return listaTemas;
    }

    public ArrayList<Tema> getTemasLanguage(int limit, Context context, String languageCode) {
        ArrayList<Tema> listaTemas = new ArrayList<Tema>();
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("idusuario", Preferencias.getDNI(context)));
        pairs.add(new BasicNameValuePair("pin", Preferencias.getPIN(context)));
        pairs.add(new BasicNameValuePair("tipo", "language"));
        pairs.add(new BasicNameValuePair("limit", "" + limit));
        pairs.add(new BasicNameValuePair("language", languageCode));


        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://" + urlservidor + "/getTemas");
            httppost.setEntity(new UrlEncodedFormEntity(pairs));
            HttpResponse resp = httpclient.execute(httppost);
            int statusCode = resp.getStatusLine().getStatusCode();
            if (!("" + statusCode).startsWith("2")) {
                Log.e("getTemasLanguage error", "status code = " + statusCode);
                return null;
            }
            HttpEntity ent = resp.getEntity();/*y obtenemos una respuesta*/

            String response = EntityUtils.toString(ent);
            Gson gson = new Gson();
            Type tipoObjeto = new TypeToken<List<Tema>>() {
            }.getType();
            listaTemas = gson.fromJson(response, tipoObjeto);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return listaTemas;
    }

    public ArrayList<Comentario> getComentarios(Context context, int idtema, int limit) {
        ArrayList<Comentario> listaComentarios = new ArrayList<Comentario>();
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("idusuario", Preferencias.getDNI(context)));
        pairs.add(new BasicNameValuePair("pin", Preferencias.getPIN(context)));
        pairs.add(new BasicNameValuePair("idtema", "" + idtema));
        pairs.add(new BasicNameValuePair("limit", "" + limit));


        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://" + urlservidor + "/getComentarios");
            httppost.setEntity(new UrlEncodedFormEntity(pairs));
            HttpResponse resp = httpclient.execute(httppost);
            int statusCode = resp.getStatusLine().getStatusCode();
            if (!("" + statusCode).startsWith("2")) {
                Log.e("getComentarios error", "status code = " + statusCode);
                return null;
            }
            HttpEntity ent = resp.getEntity();/*y obtenemos una respuesta*/

            String response = EntityUtils.toString(ent);
            Gson gson = new Gson();
            Type tipoObjeto = new TypeToken<List<Comentario>>() {
            }.getType();
            listaComentarios = gson.fromJson(response, tipoObjeto);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return listaComentarios;
    }


    public int insertarTema(Context context, String titulo, String descripcion, String languageCode) {
        int resultado = -1;
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("idusuario", Preferencias.getDNI(context)));
        pairs.add(new BasicNameValuePair("pin", Preferencias.getPIN(context)));
        pairs.add(new BasicNameValuePair("titulo", titulo));
        pairs.add(new BasicNameValuePair("descripcion", descripcion));
        pairs.add(new BasicNameValuePair("language", languageCode));


        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://" + urlservidor + "/insertTema");
            httppost.setEntity(new UrlEncodedFormEntity(pairs));
            HttpResponse resp = httpclient.execute(httppost);
            int statusCode = resp.getStatusLine().getStatusCode();
            if (!("" + statusCode).startsWith("2")) {
                Log.e("insertTema error", "status code = " + statusCode);
                return resultado;
            }
            HttpEntity ent = resp.getEntity();/*y obtenemos una respuesta*/

            String response = EntityUtils.toString(ent);
            resultado = Integer.parseInt(response);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resultado;

    }

    public int insertarComentario(Context context, String texto, String languageCode, int idtema) {
        int resultado = -1;
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("idusuario", Preferencias.getDNI(context)));
        pairs.add(new BasicNameValuePair("pin", Preferencias.getPIN(context)));
        pairs.add(new BasicNameValuePair("idtema", "" + idtema));
        pairs.add(new BasicNameValuePair("data", texto));
        pairs.add(new BasicNameValuePair("language", languageCode));

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://" + urlservidor + "/insertComentario");
            httppost.setEntity(new UrlEncodedFormEntity(pairs));
            HttpResponse resp = httpclient.execute(httppost);
            int statusCode = resp.getStatusLine().getStatusCode();
            if (!("" + statusCode).startsWith("2")) {
                Log.e("insertComentario error", "status code = " + statusCode);
                return resultado;
            }
            HttpEntity ent = resp.getEntity();/*y obtenemos una respuesta*/

            String response = EntityUtils.toString(ent);
            resultado = Integer.parseInt(response);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resultado;
    }

    public int insertarUsuario(Activity context, String nombre, String apellidos, String languagecode) {
        int resultado = -1;
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("idusuario", Preferencias.getDNI(context)));
        pairs.add(new BasicNameValuePair("pin", Preferencias.getPIN(context)));
        pairs.add(new BasicNameValuePair("nombre", nombre));
        pairs.add(new BasicNameValuePair("apellidos", apellidos));
        pairs.add(new BasicNameValuePair("language", languagecode));
        String regid = getRegID(context);
        Preferencias.setGCMID(context, regid);
        pairs.add(new BasicNameValuePair("regid", regid));

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://" + urlservidor + "/insertarUsuario");
            httppost.setEntity(new UrlEncodedFormEntity(pairs));
            HttpResponse resp = httpclient.execute(httppost);
            int statusCode = resp.getStatusLine().getStatusCode();
            if (!("" + statusCode).startsWith("2")) {
                Log.e("insertarUsuario error", "status code = " + statusCode);
                return resultado;
            }
            HttpEntity ent = resp.getEntity();/*y obtenemos una respuesta*/

            String response = EntityUtils.toString(ent);
            resultado = Integer.parseInt(response);

            if (resultado > 0 && Preferencias.isPrimerRegistro(context)) {
                sendRegistrationIdToBackend(context);
                Preferencias.registrado(context);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e("", "usuario insertado..");
        return resultado;
    }


    public String getPhotoUser(Context context, String iduser_photo) {
        String respuesta = "-1";
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("idusuario", Preferencias.getDNI(context)));
        pairs.add(new BasicNameValuePair("pin", Preferencias.getPIN(context)));
        pairs.add(new BasicNameValuePair("userphoto", iduser_photo));

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://" + urlservidor + "/getUserPhoto");
            httppost.setEntity(new UrlEncodedFormEntity(pairs));
            HttpResponse resp = httpclient.execute(httppost);
            int statusCode = resp.getStatusLine().getStatusCode();
            if (!("" + statusCode).startsWith("2")) {
                Log.e("getUserPhoto error", "status code = " + statusCode);
            }
            HttpEntity ent = resp.getEntity();/*y obtenemos una respuesta*/

            respuesta = EntityUtils.toString(ent);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (respuesta.equals("null") || respuesta.equals("")) respuesta = "-1";
        return respuesta;
    }

    public int setPhoto(Context context, String photo) {
        int resultado = -1;
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("idusuario", Preferencias.getDNI(context)));
        pairs.add(new BasicNameValuePair("pin", Preferencias.getPIN(context)));
        pairs.add(new BasicNameValuePair("photo", photo));

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://" + urlservidor + "/setPhotoUser");
            httppost.setEntity(new UrlEncodedFormEntity(pairs));
            HttpResponse resp = httpclient.execute(httppost);
            int statusCode = resp.getStatusLine().getStatusCode();
            if (!("" + statusCode).startsWith("2")) {
                Log.e("setPhoto error", "status code = " + statusCode);
                return resultado;
            }
            HttpEntity ent = resp.getEntity();/*y obtenemos una respuesta*/

            String response = EntityUtils.toString(ent);
            resultado = Integer.parseInt(response);//devuelve -1 o 0 si hay algun fallo y 1 si se ha insertado

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resultado;
    }

    public Estadisticas getEstadisticasUser(Context context) {
        Estadisticas estadisticas = null;
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("idusuario", Preferencias.getDNI(context)));
        pairs.add(new BasicNameValuePair("pin", Preferencias.getPIN(context)));

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://" + urlservidor + "/getEstadisticas");
            httppost.setEntity(new UrlEncodedFormEntity(pairs));
            HttpResponse resp = httpclient.execute(httppost);
            int statusCode = resp.getStatusLine().getStatusCode();
            if (!("" + statusCode).startsWith("2")) {
                Log.e("getEstadisticas error", "status code = " + statusCode);
            }
            HttpEntity ent = resp.getEntity();/*y obtenemos una respuesta*/

            String respuesta = EntityUtils.toString(ent);
            if (!respuesta.equals("")) {
                Gson gson = new Gson();
                Type tipoObjeto = new TypeToken<Estadisticas>() {
                }.getType();
                estadisticas = gson.fromJson(respuesta, tipoObjeto);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return estadisticas;
    }


    public String getRegID(Activity _context) {
        String regid = "";
        regid = Preferencias.getGCMID(_context);
        if (!regid.isEmpty()) return regid;
        if (GCMConfig.checkPlayServices(_context)) {
            try {
                GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(_context);
                regid = gcm.register(GCMConfig.SENDER_ID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.i("", "No valid Google Play Services APK found.");
        }
        return regid;
    }


    public static void sendRegistrationIdToBackend(Context context) {
        String regid = Preferencias.getGCMID(context);
        if (!regid.isEmpty()) {
            MessageSender messageSender = new MessageSender();
            Bundle dataBundle = new Bundle();
            dataBundle.putString("action", "ECHO");
            dataBundle.putString("Mensaje", "REGISTRO_OK");
            GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
            messageSender.sendMessage(dataBundle, gcm);
            Log.e("gcm", "Succeded!");
        } else {
            Log.e("gcm", "no registration id");
        }
    }

    public int unRegisterUsuario(Context context) {//desloguea al usuario de bd y elimina las preferencias e usuario
        int resultado = -1;
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("idusuario", Preferencias.getDNI(context)));
        pairs.add(new BasicNameValuePair("pin", Preferencias.getPIN(context)));

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://" + urlservidor + "/unRegisterUsuario");
            httppost.setEntity(new UrlEncodedFormEntity(pairs));
            HttpResponse resp = httpclient.execute(httppost);
            int statusCode = resp.getStatusLine().getStatusCode();
            if (!("" + statusCode).startsWith("2")) {
                Log.e("unRegisterUsuario error", "status code = " + statusCode);
                return resultado;
            }
            HttpEntity ent = resp.getEntity();/*y obtenemos una respuesta*/

            String response = EntityUtils.toString(ent);
            resultado = Integer.parseInt(response);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resultado;
    }


}
