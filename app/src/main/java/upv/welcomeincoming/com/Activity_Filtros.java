package upv.welcomeincoming.com;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import util.DBHandler_Horarios;

public class Activity_Filtros extends Activity {
    SQLiteDatabase db;
    DBHandler_Horarios helper;
    Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helper = new DBHandler_Horarios(this);
        db = helper.getWritableDatabase();
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_filtros);

        spinner = (Spinner) findViewById(R.id.spinnerAsignaturas);
        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.item_lista_spinner_filtros, obtenerAsignaturas());
        spinner.setAdapter(adapter);

        Button btnAceptar = (Button) findViewById(R.id.btnAceptar);
        Button btnCancelar = (Button) findViewById(R.id.btnCancelar);

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                Log.e("asignatura", (String) spinner.getSelectedItem());
                intent.putExtra("RESULT_STRING", (String) spinner.getSelectedItem());
                setResult(1, intent);
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
