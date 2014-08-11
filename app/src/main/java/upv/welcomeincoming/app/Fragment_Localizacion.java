package upv.welcomeincoming.app;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import util.ProgressDialog_Custom;

public class Fragment_Localizacion extends Fragment {
    private SQLiteDatabase db;
    private ProgressDialog_Custom pg;

    public Fragment_Localizacion(SQLiteDatabase db) {
        this.db = db;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_localizacion, container, false);
        final Spinner spinner = (Spinner) view.findViewById(R.id.spinnerEdificios);
        Button btn_enc = (Button) view.findViewById(R.id.btn_enc);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.edificios, R.layout.item_lista_spinner_filtros);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(adapterView.getContext(), "Has seleccionado el edificio " + adapterView.getItemAtPosition(i).toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btn_enc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Acvitity_VistaRealidadAumentada.class);
                String edificio = spinner.getSelectedItem().toString().split(" ")[1];
                intent.putExtra("poi", edificio);
                startActivity(intent);
            }
        });


        LinearLayout btn_upvmap = (LinearLayout) view.findViewById(R.id.linearUpvmap);
        btn_upvmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Activity_Localizacion_UPV.class);
                startActivity(intent);
            }
        });
        LinearLayout btn_valenbisi = (LinearLayout) view.findViewById(R.id.linearValenbisimap);
        btn_valenbisi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Activity_Localizacion_Valenbisi.class);
                startActivity(intent);
            }
        });
        LinearLayout btn_metro = (LinearLayout) view.findViewById(R.id.linearMetromap);
        btn_metro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Activity_Localizacion_Metro.class);
                startActivity(intent);
            }
        });
        LinearLayout btn_emt = (LinearLayout) view.findViewById(R.id.linearEmtmap);
        btn_emt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Activity_Localizacion_EMT.class);
                startActivity(intent);
            }
        });
        LinearLayout btn_interes = (LinearLayout) view.findViewById(R.id.linearInteresmap);
        btn_interes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Activity_Localizacion_Interes.class);
                startActivity(intent);
            }
        });
        return view;
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

    private boolean valenbisiestaVacia() {
        String sql = "SELECT * FROM Valenbisi";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    private boolean edificiosestaVacia() {
        String sql = "SELECT * FROM Edificio";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    private boolean emtestaVacia() {
        String sql = "SELECT * FROM EMT";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    private boolean metroestaVacia() {
        String sql = "SELECT * FROM Metro";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    private boolean interesestaVacia() {
        String sql = "SELECT * FROM Interes";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }
}
