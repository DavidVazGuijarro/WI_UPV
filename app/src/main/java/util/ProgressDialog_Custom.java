package util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import upv.welcomeincoming.com.R;

/**
 * Created by Cristian on 16/07/2014.
 */
public class ProgressDialog_Custom extends ProgressDialog {
    private String texto;

    public ProgressDialog_Custom(Context context, String texto) {
        super(context);
        this.texto = texto;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_custom);

        TextView textView = (TextView) findViewById(R.id.textDialog);
        textView.setText(texto);
        this.setCanceledOnTouchOutside(false);
    }

    protected void setMessage(String texto) {
        this.texto = texto;
    }
}
