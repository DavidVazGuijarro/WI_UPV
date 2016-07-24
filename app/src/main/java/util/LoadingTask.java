package util;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import upv.welcomeincoming.com.R;

public class LoadingTask extends AsyncTask<String, Integer, Integer> {

    public interface LoadingTaskFinishedListener {
        void onTaskFinished();
    }

    // This is the progress bar you want to update while the task is in progress
    private final SmoothProgressBar progressBar;
    // This is the listener that will be told when this task is finished
    private final LoadingTaskFinishedListener finishedListener;
    private final Resources resources;
    private SQLiteDatabase db;

    /**
     * A Loading task that will load some resources that are necessary for the app to start
     *
     * @param progressBar      - the progress bar you want to update while the task is in progress
     * @param finishedListener - the listener that will be told when this task is finished
     */
    public LoadingTask(SmoothProgressBar progressBar, LoadingTaskFinishedListener finishedListener, Resources resources, Context context) {
        this.progressBar = progressBar;
        this.finishedListener = finishedListener;
        this.resources = resources;
        Context context1 = context;
        this.db = new DBHandler_Horarios(context).getReadableDatabase();
    }

    @Override
    protected Integer doInBackground(String... params) {
        if (resourcesDontAlreadyExist()) {
            downloadResources();
        }
        // Perhaps you want to return something to your post execute
        return 1234;
    }

    private boolean resourcesDontAlreadyExist() {
        if (!interesestaVacia() && !metroestaVacia() && !valenbisiestaVacia() && !edificiosestaVacia() && !emtestaVacia())
            return false;
        else
            return true; // returning true so we show the splash every time
    }


    private void downloadResources() {
        // We are just imitating some process thats takes a bit of time (loading of resources / downloading)
        try {
            if (interesestaVacia())
                new Parser_XML_interes().parsear(resources.openRawResource(R.raw.sitiosinteres), db);
            publishProgress(20);
            if (metroestaVacia())
                new Parser_XML_metro().parsear(resources.openRawResource(R.raw.metro), db);
            publishProgress(40);
            if (valenbisiestaVacia())
                new Parser_XML_valenbisi().parsear(resources.openRawResource(R.raw.valenbisi), db);
            publishProgress(60);
            if (edificiosestaVacia())
                new Parser_XML_edificios().parsearEdificios(resources.openRawResource(R.raw.edificios), db);
            publishProgress(80);
            if (emtestaVacia())
                new Parser_XML_emt().parsear(resources.openRawResource(R.raw.emt), db);
            publishProgress(100);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        progressBar.setProgress(values[0]); // This is ran on the UI thread so it is ok to update our progress bar ( a UI view ) here
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
        finishedListener.onTaskFinished(); // Tell whoever was listening we have finished
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