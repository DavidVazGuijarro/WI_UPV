package upv.welcomeincoming.com;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class Activity_Info_Valenbisi extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_info_valenbisi);
        String direccion = getIntent().getStringExtra("nombre");
        int total = getIntent().getIntExtra("total", 0);
        int disponibles = getIntent().getIntExtra("disponible", 0);

        Log.e("mvb", total + "," + disponibles);
        TextView textDireccion = (TextView) findViewById(R.id.txtDireccion);
        textDireccion.setText(direccion);
        TextView textTotal = (TextView) findViewById(R.id.txtNumplazas);
        textTotal.setText("" + total);
        TextView textDisponibles = (TextView) findViewById(R.id.txtPlazasdisponibles);
        textDisponibles.setText("" + disponibles);
        Button btnCerrar = (Button) findViewById(R.id.btnClose);

        btnCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
