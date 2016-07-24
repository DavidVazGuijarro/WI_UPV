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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import upv.welcomeincoming.com.R;
import util.ProgressDialog_Custom;
import util.Traductor.language.Language;

public class Activity_Temas_Language extends ActionBarActivity {
    ListView listaTemas;
    ArrayList<Tema> arrayTemas;
    Context _context;
    TextView emptyView;
    Spinner spinner_language;
    ImageView iv_flag;
    String _languageCode;
    String initialSpinnerItem;
    ArrayAdapter<String> dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temas_language);
        _context = this;
        listaTemas = (ListView) findViewById(R.id.listaTemas);
        emptyView = (TextView) findViewById(R.id.empty);
        this.setTitle(getString(R.string.temas_title));
        this.getSupportActionBar().setHomeButtonEnabled(true);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        spinner_language = (Spinner) findViewById(R.id.spinner_language);
        iv_flag = (ImageView) findViewById(R.id.iv_flag);

        initComponents();
        //TextView empty = new TextView(this);
        //empty.setText(getString(R.string.noHayTemas));
        //empty.setLayoutParams(new ViewGroup.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        //listaTemas.setEmptyView(empty);

        //
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


    private void initComponents() {
        arrayTemas = new ArrayList<Tema>();
        initialSpinnerItem = getString(R.string.seleccione_idioma) + "...";
        List<String> list_spinner = Language.getArrayStringNameLanguages();
        list_spinner.add(0, initialSpinnerItem);
        dataAdapter = new ArrayAdapter<String>(_context,
                R.layout.spinner_item_custom, list_spinner);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_language.setAdapter(dataAdapter);
        spinner_language.setOnItemSelectedListener(new SpinnerHandler());
    }

    class cargarTemasTask extends AsyncTask<Void, Void, Void> {
        private ProgressDialog_Custom progress;
        private Exception exception;
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
            arrayTemas = control.getTemasLanguage(-1, _context, _languageCode);
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
            iv_flag.setImageDrawable(Language.getFlagResourcebyCode(_languageCode, _context));
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
                    }
                    return false;
                }
            });
            progress.show();
        }

    }

    private class SpinnerHandler implements AdapterView.OnItemSelectedListener {
        /**
         * This method will invoke when an entry is selected. Invoked once
         * when Spinner is first displayed, then again for each time the user selects something
         */
        @Override
        public void onItemSelected(AdapterView<?> spinner, View selectedView, int selectedIndex, long id) {
            String selection = spinner.getItemAtPosition(selectedIndex).toString();
            if (selection.equals(initialSpinnerItem)) {
                _languageCode = "";
                iv_flag.setImageDrawable(null);
                arrayTemas.clear();
                listaTemas.setAdapter(new TemasListAdapter(_context, 0, arrayTemas, null));
            } else {
                _languageCode = Language.getCodeByName(selection);
                new cargarTemasTask(_context).execute();
                //dataAdapter.remove(initialSpinnerItem);
                //dataAdapter.notifyDataSetChanged();
            }


        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub

        }
    }

}