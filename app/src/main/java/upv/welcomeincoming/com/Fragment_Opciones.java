package upv.welcomeincoming.com;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.amlcurran.showcaseview.targets.ViewTarget;

import java.util.Locale;

import upv.welcomeincoming.com.calendarAlarm.AlarmIntentService;
import upv.welcomeincoming.com.foro.Controlador;
import util.ConfirmationDialog_Custom;
import util.DBHandler_Horarios;
import util.InternetConnectionChecker;
import util.Preferencias;
import util.ProgressDialog_Custom;


/**
 * Created by Marcos on 3/05/14.
 */
public class Fragment_Opciones extends Fragment {

    private LinearLayout linearLista;
    private LayoutInflater inflater;
    InternetConnectionChecker icc;
    View itemPerfil;
    View divividierPerfil;
    boolean isSpinnerLanguageFirstSelection;
    int spinnerLastSelection;
    //ShowcaseView sv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        this.inflater = inflater;

        icc = new InternetConnectionChecker();
        linearLista = (LinearLayout) view.findViewById(R.id.linearOpciones);
        initComponents();
        return view;

    }

    public void initComponents() {
        isSpinnerLanguageFirstSelection = true;
        //Language
        View itemLanguage = generateItemLanguage();
        linearLista.addView(itemLanguage);
        addDividier();

        //Sesion
        final View itemSesion = generateItemSesion(getString(R.string.sesion));
        int idItemSesion = 213;
        itemSesion.setId(R.id.sesion_opciones);
        itemSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Preferencias.logeado(getActivity())) {
                    if (icc.checkInternetConnection(getActivity())) {
                        Intent i = new Intent(getActivity(), Activity_login.class);
                        startActivityForResult(i, 1);
                    } else {
                        Intent intent = new Intent(getActivity(), Activity_no_connection.class);
                        intent.putExtra("case",
                                getString(R.string.noInetBlock));
                        startActivity(intent);
                    }
                } else {
                    final ConfirmationDialog_Custom dialog = new ConfirmationDialog_Custom(getActivity(),
                            getString(R.string.confirmaciondeslogueo));
                    dialog.setOkListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new DesloguearTask(itemSesion).execute();
                            dialog.dismiss();
                        }
                    });
                    dialog.setCancelListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            }
        });


        linearLista.addView(itemSesion);
        addDividier();

        //Alertas
        final View itemAlertas = generateItemAlerta(getString(R.string.alertaCalendar), "Calendar");
        itemAlertas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Preferencias.setCalendarAlerts(getActivity(), false);
            }
        });
        linearLista.addView(itemAlertas);
        addDividier();

        View itemAlertaForum = generateItemAlerta(getString(R.string.alertaForum), "Forum");
        itemAlertaForum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Preferencias.setForumAlerts(getActivity(), false);
            }
        });
        linearLista.addView(itemAlertaForum);
        addDividier();

        //Perfil
        itemPerfil = inflater.inflate(R.layout.item_settings_perfil, null);
        LinearLayout linearPE = (LinearLayout) itemPerfil.findViewById(R.id.linearPerfil);
        linearPE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentPerfil = new Intent(getActivity(), Activity_Profile.class);
                startActivity(intentPerfil);
            }
        });
        linearLista.addView(itemPerfil);
        divividierPerfil = addDividier();
        //About
        View itemAbout = inflater.inflate(R.layout.item_settings_acerca, null);
        LinearLayout linearAC = (LinearLayout) itemAbout.findViewById(R.id.linearAcerca);
        linearAC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAbout = new Intent(getActivity(), Activity_Acercade.class);
                startActivity(intentAbout);
            }
        });
        linearLista.addView(itemAbout);
        addDividier();
        if (!Preferencias.logeado(getActivity())) {
            cambiarVisibilidadItemPerfil();
        }

    }

    private void cambiarVisibilidadItemPerfil() {
        if (itemPerfil.getVisibility() == View.VISIBLE) {
            itemPerfil.setVisibility(View.GONE);
            divividierPerfil.setVisibility(View.GONE);
        } else {
            itemPerfil.setVisibility(View.VISIBLE);
            divividierPerfil.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == getActivity().RESULT_OK) {
                linearLista.removeAllViews();
                initComponents();
            }
        }
    }

    private View generateItemSesion(String string) {
        View item = inflater.inflate(R.layout.item_settings_sesion, null);
        TextView tvi = (TextView) item.findViewById(R.id.textSesionInfo);
        TextView tvv = (TextView) item.findViewById(R.id.textSesionValue);
        if (Preferencias.getDNI(getActivity()) == "") {
            tvi.setText(getString(R.string.nosesion));
            tvv.setText(getString(R.string.login));
            tvv.setBackgroundResource(R.drawable.selector_button_back);
        } else {
            tvi.setText(getString(R.string.sisesion));
            tvv.setText(Preferencias.getNombre(getActivity()) + " " + Preferencias.getApellidos(getActivity()));
        }
        ViewTarget target = new ViewTarget(tvv);
        if (Preferencias.getFirstOptions(getActivity()) == 1) {
            /*sv = new ShowcaseView.Builder(getActivity())
                    .setTarget(target)
                    .setContentTitle(R.string.tutorial_options_title)
                    .setContentText(R.string.tutorial_options_message)
                    .setStyle(R.style.ShowCaseTheme)
                    .build();
            Preferencias.setFirstOptions(getActivity(), 0);*/
        }
        return item;
    }

    private View generateItemAlerta(String string, final String tipo) {
        View viewAlerta = inflater.inflate(R.layout.item_settings_alerta, null);
        TextView txtAlerta = (TextView) viewAlerta.findViewById(R.id.textAlerta);
        txtAlerta.setText(string);
        CheckBox check = (CheckBox) viewAlerta.findViewById(R.id.checkAlertCalendar);
        if (tipo.equals("Calendar")) {
            check.setChecked(Preferencias.getCalendarAlerts(getActivity()));
        } else if (tipo.equals("Forum")) {
            check.setChecked(Preferencias.getForumAlerts(getActivity()));
        }
        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (tipo.equals("Calendar")) {
                        Preferencias.setCalendarAlerts(getActivity(), true);
                    } else if (tipo.equals("Forum")) {
                        Preferencias.setForumAlerts(getActivity(), true);
                    }
                } else if (!b) {
                    if (tipo.equals("Calendar")) {
                        Preferencias.setCalendarAlerts(getActivity(), false);
                    } else if (tipo.equals("Forum")) {
                        Preferencias.setForumAlerts(getActivity(), false);
                    }
                }
            }
        });
        return viewAlerta;
    }


    private View generateItemLanguage() {
        final View viewSpinner = inflater.inflate(R.layout.item_settings_idioma, null);
        final Spinner spinner = (Spinner) viewSpinner.findViewById(R.id.options_lenguage_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.lenguages, R.layout.item_lista_spinner);
        spinner.setAdapter(adapter);
        Locale locale1 = getActivity().getApplicationContext().getResources().getConfiguration().locale;
        if (locale1.getCountry().equals("ES")) {
            spinner.setSelection(1);
        } else if (locale1.getCountry().equals("US")) spinner.setSelection(0);
        spinner.setOnItemSelectedListener(new SpinnerHandlerLanguage(spinner));

        ImageButton btn_accept = (ImageButton) viewSpinner.findViewById(R.id.btn_accept);
        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        return viewSpinner;
    }

    private View addDividier() {
        View v = inflater.inflate(R.layout.dividier_itemdrawer, null);
        linearLista.addView(v);
        return v;
    }


    private void borrarDatosUsuario() {
        SQLiteDatabase db = new DBHandler_Horarios(getActivity()).getWritableDatabase();
        db.execSQL("DELETE FROM Evento");
        db.execSQL("DELETE FROM Horario");
    }

    private class DesloguearTask extends AsyncTask<String, Void, Void> {

        private ProgressDialog_Custom progress;
        Controlador control;
        View itemSesion;
        int res = -1;

        private DesloguearTask(View view) {
            try {
                control = Controlador.dameControlador();
            } catch (Exception e) {
                e.printStackTrace();
            }
            itemSesion = view;
        }

        @Override
        protected void onPreExecute() {
            progress = new ProgressDialog_Custom(getActivity(), getString(R.string.cerrandoSesion));
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
                        //no hace nada (no se puede cancelar el cierre de sesion
                        // puesto que podria desregistrarse del srvidor y no de la app
                    }
                    return false;
                }
            });
            progress.show();
        }

        @Override
        protected void onPostExecute(Void v) {
            if (res > 0) {//nos hemos deslogueado con exito
                Preferencias.desloguearse(getActivity());
                borrarDatosUsuario();
                TextView tvi = (TextView) itemSesion.findViewById(R.id.textSesionInfo);
                TextView tvv = (TextView) itemSesion.findViewById(R.id.textSesionValue);
                tvi.setText(getString(R.string.nosesion));
                tvv.setText(getString(R.string.login));
                cambiarVisibilidadItemPerfil();
                Toast.makeText(getActivity(), getString(R.string.hasCerradoSesion), Toast.LENGTH_SHORT).show();
                Log.e("", "usuario desregistrado..");
            } else {
                Toast.makeText(getActivity(), getString(R.string.noHasCerradoSesion), Toast.LENGTH_SHORT).show();
            }
            progress.dismiss();
        }


        @Override
        protected Void doInBackground(String... strings) {
            try {
                res = Controlador.dameControlador().unRegisterUsuario(getActivity());

                AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

                Intent updateServiceIntent = new Intent(getActivity(), AlarmIntentService.class);
                PendingIntent pendingUpdateIntent = PendingIntent.getService(getActivity(), 0, updateServiceIntent, 0);

                // Cancel alarms
                try {
                    alarmManager.cancel(pendingUpdateIntent);
                } catch (Exception e) {
                    Log.e("alarm", "AlarmManager update was not canceled. " + e.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    private class SpinnerHandlerLanguage implements AdapterView.OnItemSelectedListener {

        Spinner _spinner;

        public SpinnerHandlerLanguage(Spinner spinner) {
            this._spinner = spinner;
        }

        /**
         * This method will invoke when an entry is selected. Invoked once
         * when Spinner is first displayed, then again for each time the user selects something
         */
        @Override
        public void onItemSelected(final AdapterView<?> spinner, View selectedView, final int selectedIndex, long id) {
            if (isSpinnerLanguageFirstSelection) {
                isSpinnerLanguageFirstSelection = false;
                spinnerLastSelection = selectedIndex;
                return;
            }
            final ConfirmationDialog_Custom dialog = new ConfirmationDialog_Custom(getActivity(),
                    getString(R.string.confirmacionCambioIdiaoma));
            dialog.setOkListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (spinner.getSelectedItemPosition() == 0) {
                        Locale locale = new Locale("en_US");
                        Locale.setDefault(locale);
                        Configuration config = new Configuration();
                        config.locale = locale;
                        getActivity().getApplicationContext().getResources().updateConfiguration(config, null);
                        getActivity().recreate();
                    } else if (spinner.getSelectedItemPosition() == 1) {
                        Locale locale = new Locale("es_ES");
                        Locale.setDefault(locale);
                        Configuration config = new Configuration();
                        config.locale = locale;
                        getActivity().getApplicationContext().getResources().updateConfiguration(config, null);
                        getActivity().recreate();
                    }
                    dialog.dismiss();
                }
            });
            dialog.setCancelListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isSpinnerLanguageFirstSelection = true;
                    _spinner.setSelection(spinnerLastSelection);
                    spinnerLastSelection = selectedIndex;
                    dialog.dismiss();
                }
            });
            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                    if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_BACK && keyEvent.getAction() == KeyEvent.ACTION_UP && !keyEvent.isCanceled()) {
                        isSpinnerLanguageFirstSelection = true;
                        _spinner.setSelection(spinnerLastSelection);
                        spinnerLastSelection = selectedIndex;
                        dialog.dismiss();
                    }
                    return false;
                }
            });
            dialog.show();


        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub

        }
    }


}