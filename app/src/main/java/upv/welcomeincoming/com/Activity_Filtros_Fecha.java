package upv.welcomeincoming.com;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import util.DBHandler_Horarios;

public class Activity_Filtros_Fecha extends Activity {
    SQLiteDatabase db;
    DBHandler_Horarios helper;
    DatePicker picker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setLayout(300, 200);
        super.onCreate(savedInstanceState);
        helper = new DBHandler_Horarios(this);
        db = helper.getWritableDatabase();
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_filtros_fecha);

        picker = (DatePicker) findViewById(R.id.datePicker);

        Button btnAceptar = (Button) findViewById(R.id.btnAceptar);
        Button btnCancelar = (Button) findViewById(R.id.btnCancelar);

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                String day = picker.getDayOfMonth() + "";
                String month = picker.getMonth() + 1 + "";
                if (picker.getDayOfMonth() < 10) day = "0" + picker.getDayOfMonth();
                if (picker.getMonth() < 9) day = "0" + picker.getDayOfMonth();
                String date = picker.getYear() + "-" + month + "-" + day;
                intent.putExtra("RESULT_STRING", date);
                setResult(2, intent);
                finish();
            }
        });
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(0);
                finish();
            }
        });
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public List<String> obtenerAsignaturas() {
        String sql = "SELECT nombre FROM Evento;";
        Cursor cursor = db.rawQuery(sql, null);
        List<String> asignaturas = new ArrayList<String>();
        while (cursor.moveToNext()) {
            String asignatura = cursor.getString(cursor.getColumnIndex("nombre"));
            asignaturas.add(asignatura);
        }
        cursor.close();
        HashSet hs = new HashSet();
        hs.addAll(asignaturas);
        asignaturas.clear();
        asignaturas.addAll(hs);
        return asignaturas;
    }
}
