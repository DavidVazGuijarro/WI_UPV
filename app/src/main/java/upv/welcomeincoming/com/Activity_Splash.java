package upv.welcomeincoming.com;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.widget.LinearLayout;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import util.DBHandler_Horarios;
import util.Parser_XML;
import util.Parser_XML_edificios;
import util.Parser_XML_emt;
import util.Parser_XML_interes;
import util.Parser_XML_metro;
import util.Parser_XML_valenbisi;
import util.transporte;


public class Activity_Splash extends Activity {
    LinearLayout image;
    SQLiteDatabase db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Show the splash screen
        setContentView(R.layout.splash_screen);
        this.db = new DBHandler_Horarios(this).getReadableDatabase();
        image = (LinearLayout) findViewById(R.id.loadingPanel);
        // Find the progress bar
        final int[] imageArray = {R.drawable.screen_splash1, R.drawable.screen_splash1_5, R.drawable.screen_splash2, R.drawable.screen_splash3, R.drawable.screen_splash};

        SmoothProgressBar progressBar = (SmoothProgressBar) findViewById(R.id.progressbar);
        if (!interesestaVacia() && !metroestaVacia() && !valenbisiestaVacia() && !edificiosestaVacia() && !emtestaVacia()) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                int i = 0;

                public void run() {
                    image.setBackgroundResource(imageArray[i]);
                    i++;
                    if (i > imageArray.length - 1) {
                        Intent mainIntent = new Intent().setClass(
                                Activity_Splash.this, Activity_Home.class);
                        startActivity(mainIntent);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        finish();
                    }
                    if (i < imageArray.length)
                        handler.postDelayed(this, 200);  //for interval...

                }
            }, 500);
        } // Pass in whatever you need a url is just an example we don't use it in this tutorial
        else {
            new LoadingTask(progressBar, getResources(), getApplicationContext()).execute("NORMAL");
        }
    }

    public void onTaskFinished() {
        completeSplash();
    }


    private void completeSplash() {
        startApp();
        finish(); // Don't forget to finish this Splash Activity so the user can't return to it!
    }

    private void startApp() {
        Intent intent = new Intent(this, Activity_Home.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }


    private class LoadingTask extends AsyncTask<String, Integer, Integer> {

        private final Context context;

        // This is the progress bar you want to update while the task is in progress
        private final SmoothProgressBar progressBar;
        // This is the listener that will be told when this task is finished
        private final Resources resources;

        /**
         * A Loading task that will load some resources that are necessary for the app to start
         *
         * @param progressBar - the progress bar you want to update while the task is in progress
         */
        public LoadingTask(SmoothProgressBar progressBar, Resources resources, Context context) {
            this.progressBar = progressBar;
            this.resources = resources;
            this.context = context;
        }

        @Override
        protected Integer doInBackground(String... params) {
            downloadResources();
            // Perhaps you want to return something to your post execute
            return 1234;
        }

        private boolean resourcesDontAlreadyExist() {
            return !(!interesestaVacia() && !metroestaVacia() && !valenbisiestaVacia() && !edificiosestaVacia() && !emtestaVacia());
        }


        private void downloadResources() {
            // We are just imitating some process thats takes a bit of time (loading of resources / downloading)
            try {
                //Mapas
                if (interesestaVacia())
                    new Parser_XML_interes().parsear(resources.openRawResource(R.raw.sitiosinteres), db);
                if (metroestaVacia())
                    new Parser_XML_metro().parsear(resources.openRawResource(R.raw.metro), db);
                publishProgress(R.drawable.screen_splash1_5);
                if (valenbisiestaVacia())
                    new Parser_XML_valenbisi().parsear(resources.openRawResource(R.raw.valenbisi), db);
                publishProgress(R.drawable.screen_splash2);
                if (edificiosestaVacia())
                    new Parser_XML_edificios().parsearEdificios(resources.openRawResource(R.raw.edificios), db);
                publishProgress(R.drawable.screen_splash3);
                if (emtestaVacia())
                    new Parser_XML_emt().parsear(resources.openRawResource(R.raw.emt), db);
                //Transportes
                InputStream fichero = getResources().openRawResource(R.raw.transportes);
                Parser_XML parseador = new Parser_XML("transportes");
                List<transporte> listaTransportes = parseador.parseando(fichero);
                listaTransportes.remove(0);
                Iterator<transporte> transporteIterator = listaTransportes.iterator();
                while (transporteIterator.hasNext()) {
                    transporte transporte = transporteIterator.next();
                    db.execSQL("INSERT OR IGNORE INTO Transporte (nombre,descripcion,telefono,url) VALUES ('" + transporte.getNombre() + "','" + transporte.getDescripcion() + "','" + transporte.getTelefono() + "','" + transporte.getUrl() + "');");
                }

                publishProgress(R.drawable.screen_splash);
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            image.setBackgroundResource(values[0]);
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            onTaskFinished(); // Tell whoever was listening we have finished
        }
    }

    private boolean valenbisiestaVacia() {
        String sql = "SELECT * FROM Valenbisi";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    private boolean edificiosestaVacia() {
        String sql = "SELECT * FROM Edificio";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    private boolean emtestaVacia() {
        String sql = "SELECT * FROM EMT";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    private boolean metroestaVacia() {
        String sql = "SELECT * FROM Metro";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    private boolean interesestaVacia() {
        String sql = "SELECT * FROM Interes";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }
}
