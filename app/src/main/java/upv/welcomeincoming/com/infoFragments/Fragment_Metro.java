package upv.welcomeincoming.com.infoFragments;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import upv.welcomeincoming.com.Activity_Localizacion_EMT;
import upv.welcomeincoming.com.R;
import util.DBHandler_Horarios;
import util.InternetConnectionChecker;
import util.transporte;

public class Fragment_Metro extends Fragment {
    transporte metro;
    InternetConnectionChecker icc;
    private SQLiteDatabase db;

    public Fragment_Metro() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_metro, container, false);
        db = new DBHandler_Horarios(getActivity()).getReadableDatabase();
        metro = obtener();
        Button google_map = (Button) view.findViewById(R.id.btnGooglemap);
        Button web = (Button) view.findViewById(R.id.btnWeb);
        Button telf = (Button) view.findViewById(R.id.btnTelf);
        TextView txtDescripcion = (TextView) view.findViewById(R.id.txtDescripcion);

        txtDescripcion.setText(metro.getDescripcion());
        telf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(android.content.Intent.ACTION_DIAL,
                        Uri.parse("tel:" + metro.getTelefono())); //
                startActivity(i);
            }
        });
        google_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Activity_Localizacion_EMT.class));
            }
        });
        web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://" + metro.getUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        return view;
    }

    private transporte obtener() {
        String sql = "SELECT * FROM Transporte WHERE nombre = 'metro'";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            transporte transporte = new transporte(cursor.getString(cursor.getColumnIndex("nombre")), cursor.getString(cursor.getColumnIndex("descripcion")), cursor.getString(cursor.getColumnIndex("telefono")), cursor.getString(cursor.getColumnIndex("url")));
            return transporte;
        }
        cursor.close();
        return null;
    }
}
