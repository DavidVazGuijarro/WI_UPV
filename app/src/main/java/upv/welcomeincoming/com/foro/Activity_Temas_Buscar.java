package upv.welcomeincoming.com.foro;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import upv.welcomeincoming.com.R;
import util.ProgressDialog_Custom;

public class Activity_Temas_Buscar extends ActionBarActivity {

    ListView listaTemas;
    Context _context;
    String _word;
    FrameLayout layout_buscar;
    EditText et_buscar;
    TextView emptyView;
    InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temas_buscar);
        _context = this;
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        listaTemas = (ListView) findViewById(R.id.listaTemas);
        this.setTitle(getString(R.string.temas_title));
        this.getSupportActionBar().setHomeButtonEnabled(true);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        layout_buscar = (FrameLayout) findViewById(R.id.layout_buscar);
        et_buscar = (EditText) findViewById(R.id.et_buscarTema);
        emptyView = (TextView) findViewById(R.id.empty);
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
        layout_buscar.setEnabled(false);
        layout_buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _word = et_buscar.getText().toString();
                imm.hideSoftInputFromWindow(et_buscar.getWindowToken(), 0);
                new cargarTemasTask(_context).execute();
            }
        });

        et_buscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (et_buscar.getText().toString().equals("")) {//Si cambia el texto a vacio
                    layout_buscar.setEnabled(false);
                } else {//Si se escribe texto
                    layout_buscar.setEnabled(true);
                }
            }
        });
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
            arrayTemas = control.getTemasBuscar(-1, _context, _word);
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            if (arrayTemas != null) {
                listaTemas.setAdapter(new TemasListAdapter(_context, 0, arrayTemas, _word));
                if (arrayTemas.isEmpty()) emptyView.setVisibility(View.VISIBLE);
                else emptyView.setVisibility(View.GONE);
                progress.dismiss();
            } else {
                Toast.makeText(_context, getString(R.string.errorConexion), Toast.LENGTH_LONG).show();
                progress.dismiss();
            }
            et_buscar.selectAll();

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
}
