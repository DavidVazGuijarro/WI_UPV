package upv.welcomeincoming.com.foro;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import upv.welcomeincoming.com.R;
import util.Preferencias;
import util.Traductor.language.Language;

public class Activity_Nuevo_Tema extends ActionBarActivity {
    Context _context;
    InputMethodManager imm;
    TextView tv_titulo_restantes;
    TextView tv_descripcion_restantes;
    EditText et_titulo;
    EditText et_descripcion;
    Spinner spinner_language;
    ImageView iv_flag;
    RelativeLayout layout_spinner;
    LinearLayout layout_publicar;
    LinearLayout layout_publicado;
    ProgressBar progreso;
    Button btn_nuevoTema;
    Button btn_verTema;
    TextView tv_temaPublicadoOk;
    TextView tv_publicarTema_error;


    String _languageCode;
    String _titulo;
    String _descripcion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_tema);
        _context = this;
        this.setTitle(getString(R.string.temas_title_new));
        this.getSupportActionBar().setHomeButtonEnabled(true);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        tv_titulo_restantes = (TextView) findViewById(R.id.tv_titulo_restantes);
        tv_descripcion_restantes = (TextView) findViewById(R.id.tv_descripcion_restantes);
        et_titulo = (EditText) findViewById(R.id.et_titulo);
        et_descripcion = (EditText) findViewById(R.id.et_descripcion);
        spinner_language = (Spinner) findViewById(R.id.spinner_language);
        iv_flag = (ImageView) findViewById(R.id.iv_flag);
        layout_spinner = (RelativeLayout) findViewById(R.id.layout_spinner);
        layout_publicar = (LinearLayout) findViewById(R.id.layout_publicarTema);
        layout_publicado = (LinearLayout) findViewById(R.id.layout_temaPublicadoOK);
        btn_nuevoTema = (Button) findViewById(R.id.btn_nuevoTema);
        btn_verTema = (Button) findViewById(R.id.btn_irTema);
        tv_temaPublicadoOk = (TextView) findViewById(R.id.tv_temaPublicadoOK);
        tv_publicarTema_error = (TextView) findViewById(R.id.tv_publicarTema_error);
        progreso = (ProgressBar) findViewById(R.id.progreso);


        initComponents();

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
        progreso.setVisibility(View.GONE);
        layout_publicado.setVisibility(View.GONE);
        tv_publicarTema_error.setVisibility(View.GONE);
        List<String> list_spinner = Language.getArrayStringNameLanguages();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(_context,
                android.R.layout.simple_spinner_item, list_spinner);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_language.setAdapter(dataAdapter);
        spinner_language.setOnItemSelectedListener(new SpinnerHandler());
        spinner_language.setFocusable(true);
        spinner_language.setFocusableInTouchMode(true);

        et_titulo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                tv_titulo_restantes.setText("(" + (60 - et_titulo.getText().length()) + ")");
            }
        });
        et_descripcion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                tv_descripcion_restantes.setText("(" + (300 - et_descripcion.getText().length()) + ")");
            }
        });
        spinner_language.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                Log.e("", "focus spinner changed!");
                if (hasFocus) {
                    imm.hideSoftInputFromWindow(et_descripcion.getWindowToken(), 0);
                    imm.hideSoftInputFromWindow(et_titulo.getWindowToken(), 0);
                    layout_spinner.setBackground(getResources().getDrawable(R.drawable.autocomplete_edit_selected));
                    spinner_language.performClick();

                } else
                    layout_spinner.setBackground(getResources().getDrawable(R.drawable.autocomplete_edit));
            }
        });

        btn_nuevoTema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imm.hideSoftInputFromWindow(et_descripcion.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(et_titulo.getWindowToken(), 0);
                _titulo = et_titulo.getText().toString();
                if (_titulo.trim().equals("")) {
                    tv_publicarTema_error.setText(getString(R.string.escribaTituloTema));
                    tv_publicarTema_error.setVisibility(View.VISIBLE);
                    et_titulo.requestFocus();
                    return;
                }
                _descripcion = et_descripcion.getText().toString();
                if (_descripcion.trim().equals("")) {
                    tv_publicarTema_error.setText(getString(R.string.escribaDescripcionTema));
                    tv_publicarTema_error.setVisibility(View.VISIBLE);
                    et_descripcion.requestFocus();
                    return;
                }
                new CrearTemaTask(_context).execute();
            }
        });


    }

    class CrearTemaTask extends AsyncTask<Void, Void, Void> {
        //private ProgressDialog_Custom progress;
        private Exception exception;
        Controlador control;
        Context _context;
        int res = -1;
        Tema nuevoTema;


        public CrearTemaTask(Context context) {
            try {
                control = Controlador.dameControlador();
            } catch (Exception e) {
                e.printStackTrace();
            }
            _context = context;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            res = control.insertarTema(_context, _titulo, _descripcion, _languageCode);
            nuevoTema = new Tema(res, _titulo, _descripcion, new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()), 0,
                    _languageCode, Preferencias.getNombre(_context), Preferencias.getNombre(_context), Preferencias.getApellidos(_context), true);//creamos el nuevo tema para poder acceder a el y visualizarlo
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            progreso.setVisibility(View.GONE);
            if (res < 0) {
                tv_publicarTema_error.setText(R.string.falloPublicarTema);
                tv_publicarTema_error.setVisibility(View.VISIBLE);
                layout_publicar.setVisibility(View.VISIBLE);
            } else {
                layout_publicado.setVisibility(View.VISIBLE);
                et_titulo.setKeyListener(null);
                et_descripcion.setKeyListener(null);
                spinner_language.setEnabled(false);
                layout_spinner.setBackground(getResources().getDrawable(R.drawable.autocomplete_edit));
                et_titulo.setBackground(getResources().getDrawable(R.drawable.autocomplete_edit));
                et_descripcion.setBackground(getResources().getDrawable(R.drawable.autocomplete_edit));
                btn_verTema.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(_context, Activity_Ver_Tema.class);
                        i.putExtra("idtema", nuevoTema.getId());
                        i.putExtra("titulo", nuevoTema.getTitulo());
                        i.putExtra("descripcion", nuevoTema.getDescripcion());
                        i.putExtra("fechaCreacion", nuevoTema.getFecha_creacion().getTime());
                        i.putExtra("lang", nuevoTema.getLanguage());
                        i.putExtra("idusuario", nuevoTema.getIdUsuario());
                        i.putExtra("nombreusuario", nuevoTema.getNombreUsuario());
                        i.putExtra("apellidos", nuevoTema.getApellidoUsuario());
                        i.putExtra("notification", nuevoTema.notificationOn());
                        _context.startActivity(i);
                        _context.startActivity(i);
                        Activity_Nuevo_Tema.this.finish();
                    }
                });
            }

            et_titulo.setEnabled(true);
            et_descripcion.setEnabled(true);
            layout_spinner.setEnabled(true);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progreso.setVisibility(View.VISIBLE);
            layout_publicar.setVisibility(View.GONE);
            et_titulo.setEnabled(false);
            et_descripcion.setEnabled(false);
            layout_spinner.setEnabled(false);
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
            _languageCode = Language.getCodeByName(selection);
            iv_flag.setImageDrawable(Language.getFlagResourcebyCode(_languageCode, _context));

        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub

        }
    }


}