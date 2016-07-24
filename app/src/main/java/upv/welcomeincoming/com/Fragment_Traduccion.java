package upv.welcomeincoming.com;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Locale;

import util.Traductor.translate.CommonPhrases;


/**
 * Created by Marcos on 3/05/14.
 */
public class Fragment_Traduccion extends Fragment {


    LinearLayout linear_action_traductor;
    LinearLayout lista_phrases;
    Resources res;
    String[] common_phrases_array = {};
    String[] common_phrases_es_array = {};
    LayoutInflater inflater;
    private TextToSpeech ttobj;
    Handler mHandlerStart = new Handler();
    Handler mHandlerDone = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        View view = inflater.inflate(R.layout.fragment_traduccion, container, false);
        initViews(view);
        llenarListaFrases();

        return view;
    }

    private void initViews(View view) {
        res = getActivity().getResources();
        common_phrases_array = CommonPhrases.getArrayPhrases(getActivity());
        common_phrases_es_array = CommonPhrases.getArrayPhrases_ES(getActivity());
        linear_action_traductor = (LinearLayout) view.findViewById(R.id.linear_action_traductor);
        lista_phrases = (LinearLayout) view.findViewById(R.id.lista_common_phrases);
        ttobj = new TextToSpeech(getActivity(),
                new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if (status != TextToSpeech.ERROR) {
                            ttobj.setLanguage(new Locale("es", "ES"));//español de españa
                        }
                    }
                }
        );

        linear_action_traductor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Activity_Traduccion.class);
                startActivity(i);
            }
        });


    }

    private void llenarListaFrases() {
        int length = CommonPhrases.length;
        for (int i = 0; i < length; i++) {
            lista_phrases.addView(getItemCommonPhrase(common_phrases_array[i], common_phrases_es_array[i]));
        }

    }

    private View getItemCommonPhrase(String phrase, final String phrase_es) {
        View viewItem = inflater.inflate(R.layout.item_common_phrase, null);
        TextView tv_phrase = (TextView) viewItem.findViewById(R.id.tv_phraseFrom);
        TextView tv_phrase_es = (TextView) viewItem.findViewById(R.id.tv_phraseEs);
        final ImageView iv_altavoz = (ImageView) viewItem.findViewById(R.id.iv_altavoz);
        ImageView iv_copy = (ImageView) viewItem.findViewById(R.id.iv_copy);
        ImageView iv_share = (ImageView) viewItem.findViewById(R.id.iv_share);
        tv_phrase.setText(phrase);
        tv_phrase_es.setText(phrase_es);
        iv_altavoz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHandlerStart = new Handler();
                mHandlerDone = new Handler();
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "messageID");
                ttobj.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                    @Override
                    public void onStart(String s) {
                        Runnable run = new Runnable() {
                            public void run() {
                                iv_altavoz.setImageDrawable((getResources().getDrawable(R.drawable.ic_action_altavoz_solido)));
                            }
                        };
                        mHandlerStart.post(run);
                    }

                    @Override
                    public void onDone(String s) {
                        Runnable run = new Runnable() {
                            public void run() {
                                iv_altavoz.setImageDrawable((getResources().getDrawable(R.drawable.ic_action_altavoz_hueco)));
                            }
                        };
                        mHandlerDone.post(run);

                    }

                    @Override
                    public void onError(String s) {

                    }
                });
                ttobj.speak(phrase_es, TextToSpeech.QUEUE_FLUSH, map);
            }
        });

        iv_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getActivity().getSystemService(getActivity().CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData.newPlainText("text label", phrase_es);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getActivity(), getString(R.string.textCopyToClipboard), Toast.LENGTH_SHORT).show();
            }
        });

        iv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String texto = phrase_es + "\n(Translated by upv welcomeincoming)";
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, texto);
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.compartirTraduccion)));
            }
        });


        return viewItem;
    }


}