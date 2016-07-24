package upv.welcomeincoming.com;

import android.app.ListActivity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class Activity_Info_UPV extends ListActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_info_edificio);
        final String edificio = getIntent().getStringExtra("edificio");
        String[] info = getIntent().getStringArrayExtra("info");
        boolean poi = false;
        TextView textNombre = (TextView) findViewById(R.id.txtDireccion);
        textNombre.setText(getString(R.string.edificio) + " " + edificio);
        ImageButton imageView = (ImageButton) findViewById(R.id.imgra);
        imageView.setVisibility(View.GONE);
        String[] edificios = getResources().getStringArray(R.array.edificios);
        for (int i = 0; i < edificios.length; i++)
            if (edificios[i].contains(edificio)) poi = true;
        if (poi) {
            imageView.setVisibility(View.VISIBLE);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (checkSensor()) {
                        Intent intent = new Intent(getApplicationContext(), Acvitity_VistaRealidadAumentada.class);
                        intent.putExtra("poi", edificio);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.nosensor), Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            imageView.setVisibility(View.GONE);
        }
        //final String[] info = UpvMarkersHashMap.get(marker).getInformacion().split(";");
        ListView listaInfo = getListView();
        listaInfo.setDivider(getResources().getDrawable(R.drawable.divisor));
        listaInfo.setTextAlignment(View.TEXT_ALIGNMENT_INHERIT);
        listaInfo.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.item_lista_info_upv, info));

        Button btnCerrar = (Button) findViewById(R.id.btnClose);

        btnCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public boolean checkSensor() {
        SensorManager mSensorManager;

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        List<Sensor> mSensorList = mSensorManager.getSensorList(Sensor.TYPE_ALL);

        for (int i = 0; i < mSensorList.size(); i++) {
            if (Sensor.TYPE_ORIENTATION == mSensorList.get(i).getType()) {
                return true;
            }
        }
        return false;
    }
}
