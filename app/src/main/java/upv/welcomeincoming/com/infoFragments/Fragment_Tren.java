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

import upv.welcomeincoming.com.R;
import util.DBHandler_Horarios;

import util.transporte;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Tren extends Fragment {
    transporte tren;
    SQLiteDatabase db;

    public Fragment_Tren() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tren, container, false);
        db = new DBHandler_Horarios(getActivity()).getReadableDatabase();
        tren = obtener();
        Button web = (Button) view.findViewById(R.id.btnWeb);
        Button telf = (Button) view.findViewById(R.id.btnTelf);
        TextView txtDescripcion = (TextView) view.findViewById(R.id.txtDescripcion);

        txtDescripcion.setText(tren.getDescripcion());
        telf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(android.content.Intent.ACTION_DIAL,
                        Uri.parse("tel:" + tren.getTelefono())); //
                startActivity(i);
            }
        });
        web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://" + tren.getUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        return view;
    }


    private transporte obtener() {
        String sql = "SELECT * FROM Transporte WHERE nombre = 'tren'";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            transporte transporte = new transporte(cursor.getString(cursor.getColumnIndex("nombre")), cursor.getString(cursor.getColumnIndex("descripcion")), cursor.getString(cursor.getColumnIndex("telefono")), cursor.getString(cursor.getColumnIndex("url")));
            return transporte;
        }
        cursor.close();
        return null;
    }
}
