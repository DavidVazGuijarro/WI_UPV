package upv.welcomeincoming.com;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.Locale;

public class Activity_Speaker extends ActionBarActivity implements TextToSpeech.OnInitListener {
    private static final String LOG_SPEAK = "LOG_SPEAK";
    private int RESULT_SPEAKER = 0;
    TextToSpeech ttobj;
    String toSpeak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speaker);

        if (savedInstanceState == null) {
        }
        //recogemos el parametro del texto que tenemos que reproducir
        Bundle bundle = getIntent().getExtras();

        toSpeak = bundle.getString("text");

        //preparamos el speaker
        Intent intentSpeak = new Intent();
        intentSpeak.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(intentSpeak, RESULT_SPEAKER);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.speaker, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onInit(int initStatus) {
        if (initStatus == TextToSpeech.SUCCESS) {
            ttobj.setLanguage(new Locale("es", "ES"));//español de españa
        } else if (initStatus == TextToSpeech.ERROR) {
            alertInfo(getResources().getString(R.string.err_no_TTS));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_SPEAKER) {
            // variable indicating whether or not the user has the TTS data installed.
            // If the data is present, the code goes ahead and creates an instance of the TTS class.
            // If the data is not present, the app will prompt the user to install it.
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                ttobj = new TextToSpeech(getApplicationContext(),
                        new TextToSpeech.OnInitListener() {
                            @Override
                            public void onInit(int status) {
                                if (status != TextToSpeech.ERROR) {
                                    ttobj.setLanguage(new Locale("es", "ES"));//español de españa
                                    ttobj.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                                }
                            }
                        }
                );


            } else {
                Intent installTTSIntent = new Intent();
                installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTSIntent);

            }
        }
    }

    public void alertInfo(String mns) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(mns)
                .setPositiveButton(getResources().getString(R.string.btn_OK), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                    }
                });
        // Create the AlertDialog object and return it
        builder.show();
    }

    @Override
    public void onPause() {

        if (ttobj != null) {
            ttobj.stop();
            ttobj.shutdown();
        }
        super.onPause();

    }

    public void gotoRepeat(View view) {
        ttobj.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
    }

    public void goBack(View view) {
        finish();
    }


}
