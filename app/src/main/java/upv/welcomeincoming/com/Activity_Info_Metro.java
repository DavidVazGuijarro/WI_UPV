package upv.welcomeincoming.com;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Activity_Info_Metro extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_info_metro);
        final String nombre = getIntent().getStringExtra("nombre");
        String lineas = getIntent().getStringExtra("lineas");
        final int id = getIntent().getIntExtra("id", -1);

        TextView textNombre = (TextView) findViewById(R.id.txtNombre);
        textNombre.setText(nombre);
        TextView textWeb = (TextView) findViewById(R.id.txtDireccionWeb);
        textWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://www.metrovalencia.es/estacion_detalle.php?id=" + id);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLineas);
        Log.e("lineas metro", lineas);
        if (lineas.contains("1")) {
            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setImageResource(R.drawable.icon_linea1_big);
            linearLayout.addView(imageView);
        }
        if (lineas.contains("3")) {

            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setImageResource(R.drawable.icon_linea3_big);
            linearLayout.addView(imageView);
        }
        if (lineas.contains("4")) {

            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setImageResource(R.drawable.icon_linea4_big);
            linearLayout.addView(imageView);
        }
        if (lineas.contains("5")) {

            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setImageResource(R.drawable.icon_linea5_big);
            linearLayout.addView(imageView);
        }
        if (lineas.contains("6")) {

            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setImageResource(R.drawable.icon_linea6_big);
            linearLayout.addView(imageView);
        }
        Button btnCerrar = (Button) findViewById(R.id.btnClose);

        btnCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
