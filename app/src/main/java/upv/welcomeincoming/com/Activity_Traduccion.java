package upv.welcomeincoming.com;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import util.Traductor.ApiKeys;
import util.Traductor.detect.Detect;
import util.Traductor.language.Language;
import util.Traductor.translate.Translate;

public class Activity_Traduccion extends ActionBarActivity {

    protected static final int RESULT_SPEECH = 1;

    EditText etTrad;
    TextView tvTrad;
    Spinner spinner_from;
    Spinner spinner_to;
    ImageView iv_top_flag;
    ImageView iv_top_mic;
    ImageView iv_top_altavoz;
    ImageView iv_top_clear;
    ImageView iv_bottom_copy;
    ImageView iv_bottom_share;
    ImageView iv_bottom_altavoz;
    ImageView iv_bottom_flag;
    ImageButton button_change_language;
    ImageButton button_translate;

    String spinnerTo_selection, spinnerFrom_selection;
    private TextToSpeech ttobjTo;
    private TextToSpeech ttobjFrom;
    Handler mHandlerStart = new Handler();
    Handler mHandlerDone = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traduccion);
        Translate.setKey(ApiKeys.YANDEX_API_KEY);
        this.getSupportActionBar().setHomeButtonEnabled(true);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initViews();
        setListeners();
    }

    private void setListeners() {

        etTrad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (etTrad.getText().toString().equals("")) {//Si cambia el texto a vacio
                    button_translate.setEnabled(false);
                } else {//Si se escribe texto
                    button_translate.setEnabled(true);
                }
            }
        });
        List<String> list_spinner_from = Language.getArrayStringNameLanguages();
        list_spinner_from.add(0, getString(R.string.detectar_idioma));//anyadimos el item de detectar idioma
        ArrayAdapter<String> dataAdapter_from = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item, list_spinner_from);
        dataAdapter_from.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_from.setAdapter(dataAdapter_from);
        spinner_from.setOnItemSelectedListener(new SpinnerHandlerFrom());

        List<String> list_spinner_to = Language.getArrayStringNameLanguages();
        ArrayAdapter<String> dataAdapter_to = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item, list_spinner_to);
        dataAdapter_to.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_to.setAdapter(dataAdapter_to);
        spinner_to.setOnItemSelectedListener(new SpinnerHandlerTo());

        button_translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String texto = etTrad.getText().toString();
                if (spinnerFrom_selection.equals(getString(R.string.detectar_idioma))) {
                    new TraductorTask(texto, null, Language.fromString(Language.getCodeByName(spinnerTo_selection)), true, tvTrad).execute();
                } else {
                    new TraductorTask(texto, Language.fromString(Language.getCodeByName(spinnerFrom_selection)), Language.fromString(Language.getCodeByName(spinnerTo_selection)), false, tvTrad).execute();
                }
            }
        });
        //limpiar texto de ambos sitios
        iv_top_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etTrad.setText("");
                tvTrad.setText("");
            }
        });
        //copiar al portapapeles
        iv_bottom_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String texto = tvTrad.getText().toString();
                if (texto.equals("")) {
                    Toast.makeText(getApplicationContext(), getString(R.string.noText), Toast.LENGTH_SHORT).show();
                } else {
                    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getApplicationContext().getSystemService(getApplicationContext().CLIPBOARD_SERVICE);
                    android.content.ClipData clip = android.content.ClipData.newPlainText("text label", texto);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getApplicationContext(), getString(R.string.textCopyToClipboard), Toast.LENGTH_SHORT).show();
                }

            }
        });
        //compartir traduccion
        iv_bottom_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String texto = tvTrad.getText().toString();
                if (texto.equals("")) {
                    Toast.makeText(getApplicationContext(), getString(R.string.noText), Toast.LENGTH_SHORT).show();
                } else {
                    texto += "\n(Translated by upv welcomeincoming)";
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, texto);
                    sendIntent.setType("text/plain");
                    startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.compartirTraduccion)));
                }

            }
        });

        //reproducir texto
        iv_top_altavoz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String texto = etTrad.getText().toString();
                if (texto.equals("")) {
                    Toast.makeText(getApplicationContext(), getString(R.string.noText), Toast.LENGTH_SHORT).show();
                } else {
                    mHandlerStart = new Handler();
                    mHandlerDone = new Handler();
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "messageID");
                    ttobjFrom.setLanguage(Language.getLocaleByName(spinnerFrom_selection));
                    ttobjFrom.speak(texto, TextToSpeech.QUEUE_FLUSH, map);
                }

            }
        });

        //reproducir traduccion
        iv_bottom_altavoz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String texto = tvTrad.getText().toString();
                if (texto.equals("")) {
                    Toast.makeText(getApplicationContext(), getString(R.string.noText), Toast.LENGTH_SHORT).show();
                } else {
                    mHandlerStart = new Handler();
                    mHandlerDone = new Handler();
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "messageID");
                    ttobjTo.setLanguage(Language.getLocaleByName(spinnerTo_selection));
                    ttobjTo.speak(texto, TextToSpeech.QUEUE_FLUSH, map);
                }

            }
        });

        //intercambiar seleccion en los spinners
        button_change_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinnerFrom_selection.equals(getString(R.string.detectar_idioma))) {
                    Toast.makeText(getApplicationContext(), getString(R.string.seleccione_idioma), Toast.LENGTH_SHORT).show();
                } else {
                    int index_from = spinner_from.getSelectedItemPosition();
                    int index_to = spinner_to.getSelectedItemPosition();
                    spinner_from.setSelection(index_to + 1, true);//se suma uno por la posicion que ocupa DETECT LANGUAGE en el spinner
                    spinner_to.setSelection(index_from - 1, true);//se resta uno por la posicion que ocupa DETECT LANGUAGE en el otro spinner
                }
            }
        });


        //action speach to text
        iv_top_mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinnerFrom_selection.equals(getString(R.string.detectar_idioma))) {
                    Toast.makeText(getApplicationContext(), getString(R.string.seleccione_idioma), Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, Language.getLocaleByName(spinnerFrom_selection).toString());
                try {
                    startActivityForResult(intent, RESULT_SPEECH);
                    etTrad.setText("");
                } catch (ActivityNotFoundException a) {
                    Toast.makeText(getApplicationContext(), getString(R.string.noSoportaSpeechToText), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    @Override //metodo que se usa para la obtencion del texto traducido mediante el speech to text
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_SPEECH: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    etTrad.setText(text.get(0));
                    etTrad.selectAll();
                }
                break;
            }

        }
    }

    private void initViews() {
        etTrad = (EditText) findViewById(R.id.etTrad);
        tvTrad = (TextView) findViewById(R.id.tvTrad);
        spinner_from = (Spinner) findViewById(R.id.spinner_language_from);
        spinner_to = (Spinner) findViewById(R.id.spinner_language_to);
        iv_top_flag = (ImageView) findViewById(R.id.iv_top_flag);
        iv_top_mic = (ImageView) findViewById(R.id.iv_top_mic);
        iv_top_clear = (ImageView) findViewById(R.id.iv_top_clear);
        iv_top_altavoz = (ImageView) findViewById(R.id.iv_top_altavoz);
        iv_bottom_copy = (ImageView) findViewById(R.id.iv_bottom_copy);
        iv_bottom_share = (ImageView) findViewById(R.id.iv_bottom_share);
        iv_bottom_altavoz = (ImageView) findViewById(R.id.iv_bottom_altavoz);
        iv_bottom_flag = (ImageView) findViewById(R.id.iv_bottom_flag);
        button_change_language = (ImageButton) findViewById(R.id.button_change_language);
        button_translate = (ImageButton) findViewById(R.id.button_translate);
        button_translate.setEnabled(false);
        ttobjTo = new TextToSpeech(getApplicationContext(),
                new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if (status != TextToSpeech.ERROR) {
                            ttobjTo.setLanguage(new Locale("es", "ES"));//español de españa
                        }
                    }
                }
        );
        ttobjTo.setOnUtteranceProgressListener(new UtteranceProgressListener() {//no funciona de momento
            @Override
            public void onStart(String s) {
                Runnable run = new Runnable() {
                    public void run() {
                        iv_bottom_altavoz.setImageDrawable((getResources().getDrawable(R.drawable.ic_action_altavoz_solido)));

                    }
                };
                mHandlerStart.post(run);

            }

            @Override
            public void onDone(String s) {
                Runnable run = new Runnable() {
                    public void run() {
                        iv_bottom_altavoz.setImageDrawable((getResources().getDrawable(R.drawable.ic_action_altavoz_hueco)));
                    }
                };
                mHandlerDone.post(run);
            }

            @Override
            public void onError(String s) {

            }
        });
        ttobjFrom = new TextToSpeech(getApplicationContext(),
                new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if (status != TextToSpeech.ERROR) {
                            ttobjFrom.setLanguage(new Locale("es", "ES"));//español de españa
                        }
                    }
                }
        );
        ttobjFrom.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String s) {
                Runnable run = new Runnable() {
                    public void run() {
                        iv_top_altavoz.setImageDrawable((getResources().getDrawable(R.drawable.ic_action_altavoz_solido)));
                    }
                };
                mHandlerStart.post(run);
            }

            @Override
            public void onDone(String s) {
                Runnable run = new Runnable() {
                    public void run() {
                        iv_top_altavoz.setImageDrawable((getResources().getDrawable(R.drawable.ic_action_altavoz_hueco)));
                    }
                };
                mHandlerDone.post(run);

            }

            @Override
            public void onError(String s) {

            }
        });


    }


    private class TraductorTask extends AsyncTask<String, Void, Void> {
        private String textoAtraducir;
        private Language idiomaOrigen;
        private Language idiomaDestino;
        private boolean detectarIdioma;
        private String textoTraducido;
        private TextView tv;
        Language idiomaDetectado;

        private TraductorTask(String texto, Language idiomaOrigen, Language idiomaDestino, boolean detectarIdioma, TextView tv) {
            textoAtraducir = texto;
            this.idiomaDestino = idiomaDestino;
            this.idiomaOrigen = idiomaOrigen;
            this.detectarIdioma = detectarIdioma;
            this.tv = tv;

        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(Void v) {
            tv.setText(textoTraducido);
            if (detectarIdioma) {
                if (idiomaDetectado == null) {
                    Toast.makeText(getApplicationContext(), getString(R.string.idiomaNoDetectado), Toast.LENGTH_SHORT).show();
                } else {
                    iv_top_flag.setImageDrawable(Language.getFlagResourcebyName(Language.getNameByCode(idiomaDetectado.toString()), getApplicationContext()));
                    ttobjFrom.setLanguage(Language.getLocaleByCode(idiomaDetectado.toString()));
                }
            }
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                if (detectarIdioma) {
                    idiomaDetectado = Detect.execute(textoAtraducir);
                    textoTraducido = Translate.execute(textoAtraducir, idiomaDetectado, idiomaDestino);
                } else {
                    textoTraducido = Translate.execute(textoAtraducir, idiomaOrigen, idiomaDestino);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class SpinnerHandlerTo implements AdapterView.OnItemSelectedListener {
        /**
         * This method will invoke when an entry is selected. Invoked once
         * when Spinner is first displayed, then again for each time the user selects something
         */
        @Override
        public void onItemSelected(AdapterView<?> spinner, View selectedView, int selectedIndex, long id) {
            String selection = spinner.getItemAtPosition(selectedIndex).toString();
            spinnerTo_selection = selection;
            iv_bottom_flag.setImageDrawable(Language.getFlagResourcebyName(selection, getApplicationContext()));

            //traducimos tambien si hay texto
            if (spinnerFrom_selection == null) return;
            String texto = etTrad.getText().toString();
            if (texto.equals("")) return;
            if (spinnerFrom_selection.equals(getString(R.string.detectar_idioma))) {
                new TraductorTask(texto, null, Language.fromString(Language.getCodeByName(spinnerTo_selection)), true, tvTrad).execute();
            } else {
                new TraductorTask(texto, Language.fromString(Language.getCodeByName(spinnerFrom_selection)), Language.fromString(Language.getCodeByName(spinnerTo_selection)), false, tvTrad).execute();
            }


        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub

        }
    }

    private class SpinnerHandlerFrom implements AdapterView.OnItemSelectedListener {
        /**
         * This method will invoke when an entry is selected. Invoked once
         * when Spinner is first displayed, then again for each time the user selects something
         */
        @Override
        public void onItemSelected(AdapterView<?> spinner, View selectedView, int selectedIndex, long id) {
            String selection = spinner.getItemAtPosition(selectedIndex).toString();
            spinnerFrom_selection = selection;
            if (!spinnerFrom_selection.equals(getString(R.string.detectar_idioma))) {
                iv_top_flag.setImageDrawable(Language.getFlagResourcebyName(selection, getApplicationContext()));
            } else {
                iv_top_flag.setBackgroundResource(android.R.color.transparent);
                iv_top_flag.setImageDrawable(null);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub

        }
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


}
