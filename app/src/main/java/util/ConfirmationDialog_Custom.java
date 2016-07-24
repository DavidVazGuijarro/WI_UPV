package util;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import upv.welcomeincoming.com.R;

/**
 * Created by Cristian on 16/07/2014.
 */
public class ConfirmationDialog_Custom extends AlertDialog {
    private String texto;
    private Button btn_ok;
    private Button btn_cancel;
    View.OnClickListener ocOk;
    View.OnClickListener ocCancel;

    public ConfirmationDialog_Custom(Context context, String texto) {
        super(context);
        this.texto = texto;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirmation_dialog);

        TextView textView = (TextView) findViewById(R.id.textDialog);
        textView.setText(texto);
        btn_ok = (Button) findViewById(R.id.btnAceptar);
        btn_cancel = (Button) findViewById(R.id.btnCancelar);
        btn_ok.setOnClickListener(ocOk);
        btn_cancel.setOnClickListener(ocCancel);
    }

    protected void setMessage(String texto) {
        this.texto = texto;
    }

    public void setOkListener(View.OnClickListener oc) {
        this.ocOk = oc;
    }

    public void setCancelListener(View.OnClickListener oc) {
        this.ocCancel = oc;
    }

}
