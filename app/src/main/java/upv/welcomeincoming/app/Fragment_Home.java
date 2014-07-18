package upv.welcomeincoming.app;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import util.ProgressDialog_Custom;
import util.RSS.FeedParser;
import util.RSS.FeedParserFactory;
import util.RSS.Noticia;
import util.RSS.ParserType;
import util.RSS.RssListAdapter;

public class Fragment_Home extends ListFragment {
    private Activity local;
    private SQLiteDatabase db;
    private List<Noticia> noticias = new ArrayList<Noticia>();

    public Fragment_Home(SQLiteDatabase db) {
        this.db = db;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        if (estaVaciaNoticias()) {
            new RetrieveFeedTask().execute();
        } else {
            obtenerNoticias();
            setListAdapter(new RssListAdapter(getActivity(), 0, noticias));
        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem item = menu.add(Menu.NONE, R.id.actualizar_action, 10, R.string.actualizar);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        item.setIcon(R.drawable.ic_action_navigation_refresh);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override

    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {

            case R.id.actualizar_action:
                noticias.clear();
                borrarNoticias();
                new RetrieveFeedTask().execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent viewMessage = new Intent(Intent.ACTION_VIEW, Uri.parse(noticias.get(position).getLink()));
        this.startActivity(viewMessage);
    }

    private void borrarNoticias() {
        db.execSQL("DELETE FROM Noticia");
    }

    private void obtenerNoticias() {
        String sql = "SELECT titulo, url,fecha FROM Noticia";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            Noticia noticia = new Noticia(cursor.getString(cursor.getColumnIndex("titulo")), cursor.getString(cursor.getColumnIndex("url")), cursor.getString(cursor.getColumnIndex("fecha")));
            noticias.add(noticia);
        }
        cursor.close();
    }

    private boolean estaVaciaNoticias() {
        String sql = "SELECT * FROM Noticia";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }


    class RetrieveFeedTask extends AsyncTask<String, Void, Void> {
        private ProgressDialog_Custom progress;
        private Exception exception;

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            obtenerNoticias();
            setListAdapter(new RssListAdapter(getActivity(), 0, noticias));
            progress.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog_Custom(getActivity(), getString(R.string.downloading_news));
            WindowManager.LayoutParams lp = progress.getWindow().getAttributes();
            lp.dimAmount = 0.0f;
            progress.getWindow().setAttributes(lp);
            progress.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            progress.getWindow().setGravity(Gravity.BOTTOM);
            progress.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                FeedParser parser = FeedParserFactory.getParser(ParserType.ANDROID_SAX);
                noticias = parser.parse();
                Iterator<Noticia> itNoticia = noticias.iterator();
                Noticia noticia = null;
                while (itNoticia.hasNext()) {
                    noticia = itNoticia.next();
                    noticia.insertarNoticiaDB(db);
                }
            } catch (Exception e) {
            }
            return null;
        }
    }

}