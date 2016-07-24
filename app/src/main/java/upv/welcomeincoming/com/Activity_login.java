package upv.welcomeincoming.com;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

import intranet.IntranetConnection;
import intranet.OutPutParamsIntranetConnection;
import upv.welcomeincoming.com.foro.Controlador;
import util.Preferencias;
import util.ProgressDialog_Custom;

public class Activity_login extends Activity implements Observer {

    EditText et_pin;
    EditText et_dni;
    TextView tv_datosIncorrectos;
    Button btn_login;
    ProgressDialog_Custom progressDialog;
    IntranetConnection intranetConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        comprobarCampos();
    }

    private void initViews() {
        et_pin = (EditText) findViewById(R.id.et_pin);
        et_dni = (EditText) findViewById(R.id.et_dni);
        tv_datosIncorrectos = (TextView) findViewById(R.id.tv_datosIncorrectos);
        tv_datosIncorrectos.setVisibility(View.GONE);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loguearse(et_dni.getText().toString(), et_pin.getText().toString());
            }
        });

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                comprobarCampos();
            }
        };
        et_pin.addTextChangedListener(watcher);
        et_dni.addTextChangedListener(watcher);
    }


    private void comprobarCampos() {
        if (et_dni.length() > 0 && et_pin.length() == 4) {
            btn_login.setEnabled(true);
        } else {
            btn_login.setEnabled(false);
        }
        tv_datosIncorrectos.setVisibility(View.GONE);
    }

    private void loguearse(String dni, String pin) {
        //codigo de la accion del boton de login

        progressDialog = new ProgressDialog_Custom(this, getString(R.string.connecting));
        progressDialog.show();
        intranetConnection = new IntranetConnection(dni, pin, this);
        intranetConnection.connect();
    }


    @Override
    public void update(Observable observable, Object data) {
        OutPutParamsIntranetConnection outPutParamsIntranetConnection = (OutPutParamsIntranetConnection) observable;

        //error?
        if (outPutParamsIntranetConnection.getException() != null) {

            if (outPutParamsIntranetConnection.isUserFail()) {
                Preferencias.setDNI(this.getParent(), "");
                Preferencias.setPIN(this.getParent(), "");
                tv_datosIncorrectos.setVisibility(View.VISIBLE);
                progressDialog.dismiss();
            }
            return;

        }
        if (data.equals("login")) {

            progressDialog.setMessage(getString(R.string.downloading));
            Preferencias.setDNI(getApplicationContext(), et_dni.getText().toString());
            int pin = generateRandomPin();
            Preferencias.setPIN(getApplicationContext(), "" + pin);
            Preferencias.setPINCalendar(getApplicationContext(), et_pin.getText().toString());
            intranetConnection.getCalendars();

        } else if (data.equals("calendars")) {
            String usuario = outPutParamsIntranetConnection.getCalendars().getUsername();
            Preferencias.setApellidos(this, usuario.substring(0, usuario.indexOf(",")));
            Preferencias.setNombre(this, usuario.substring(usuario.indexOf(",") + 1));
            final Activity contexto = this;
            new AsyncTask() {

                @Override
                protected Object doInBackground(Object[] objects) {
                    try {
                        int res = Controlador.dameControlador().insertarUsuario(contexto, Preferencias.getNombre(contexto), Preferencias.getApellidos(contexto), "es");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Object object) {
                    progressDialog.dismiss();
                    contexto.setResult(RESULT_OK);
                    contexto.finish();
                }
            }.execute(null, null, null);
        }

    }

    private static int generateRandomPin() {
        String cadena = "";
        for (int i = 0; i < 6; i++) {
            int aux = (int) Math.floor(Math.random() * 9 + 1);
            cadena += aux;
        }
        return Integer.parseInt(cadena);

    }
}
