package upv.welcomeincoming.com.foro;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import upv.welcomeincoming.com.R;
import util.ProgressDialog_Custom;

public class Activity_Temas_comentMe extends ActionBarActivity {
    ListView listaTemas;
    Context _context;
    TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temas_todos);
        _context = this;
        listaTemas = (ListView) findViewById(R.id.listaTemas);
        emptyView = (TextView) findViewById(R.id.empty);
        this.setTitle(getString(R.string.temas_coment_title));
        this.getSupportActionBar().setHomeButtonEnabled(true);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //TextView empty = new TextView(this);
        //empty.setText(getString(R.string.noHayTemas));
        //empty.setLayoutParams(new ViewGroup.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        //listaTemas.setEmptyView(empty);

        new cargarTemasTask(this).execute();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    class cargarTemasTask extends AsyncTask<Void, Void, Void> {
        private ProgressDialog_Custom progress;
        private Exception exception;
        ArrayList<Tema> arrayTemas;
        Controlador control;
        Context _context;


        public cargarTemasTask(Context context) {
            try {
                control = Controlador.dameControlador();
            } catch (Exception e) {
                e.printStackTrace();
            }
            _context = context;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            arrayTemas = control.getTemasComentByMe(0, _context);
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            if (arrayTemas != null) {
                listaTemas.setAdapter(new TemasListAdapter(_context, 0, arrayTemas, null));
                if (arrayTemas.isEmpty()) emptyView.setVisibility(View.VISIBLE);
                else emptyView.setVisibility(View.GONE);
                progress.dismiss();
            } else {
                Toast.makeText(_context, getString(R.string.errorConexion), Toast.LENGTH_LONG).show();
                progress.dismiss();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog_Custom(_context, getString(R.string.loading_temas));
            WindowManager.LayoutParams lp = progress.getWindow().getAttributes();
            lp.dimAmount = 0.0f;
            progress.getWindow().setAttributes(lp);
            progress.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            progress.getWindow().setGravity(Gravity.CENTER);
            progress.setCanceledOnTouchOutside(false);
            progress.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                    if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                        progress.dismiss();
                        Activity_Temas_comentMe.this.onBackPressed();
                    }
                    return false;
                }
            });
            progress.show();
        }

    }


}
