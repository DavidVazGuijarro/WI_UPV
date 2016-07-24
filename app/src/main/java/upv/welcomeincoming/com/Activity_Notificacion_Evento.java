package upv.welcomeincoming.com;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Activity_Notificacion_Evento extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificacion_evento);
        Intent intent = getIntent();
        String nombre = intent.getStringExtra("nombre");
        String edificio = intent.getStringExtra("edificio");
        String hora = intent.getStringExtra("hora");

        LinearLayout layEdif = (LinearLayout) findViewById(R.id.layoutEdificio);
        LinearLayout layEdifL = (LinearLayout) findViewById(R.id.layoutEdificioL);
        LinearLayout layEdifE = (LinearLayout) findViewById(R.id.layoutExtEdifi);
        TextView firstLine = (TextView) findViewById(R.id.calendarFirstLine);
        TextView secondLineLeft = (TextView) findViewById(R.id.calendarSecondLineLeft);
        TextView thirdLineRight = (TextView) findViewById(R.id.calendarThirdLine);
        firstLine.setText(nombre);
        secondLineLeft.setText(edificio);
        thirdLineRight.setText(hora);

        Button btn_ok = (Button) findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity__notificacion__evento, menu);
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
}
