package upv.welcomeincoming.app;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

import util.DBHandler_Horarios;
import util.Interes;
import util.ProgressDialog_Custom;


public class Activity_Localizacion_Interes extends FragmentActivity {
    private GoogleMap mMap;
    private ArrayList<Interes> MarkersArray = new ArrayList<Interes>();
    private HashMap<Marker, Interes> MarkersHashMap;
    private LinearLayout linear;
    private Marker current;
    ProgressDialog_Custom progress;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DBHandler_Horarios helper = new DBHandler_Horarios(this);
        db = helper.getWritableDatabase();
        setContentView(R.layout.activity_localizacion_noinfo);

        progress = new ProgressDialog_Custom(this, getString(R.string.loading));
        // Initialize the HashMap for Markers and MyMarker object

        MarkersHashMap = new HashMap<Marker, Interes>();
        MarkersArray = obtenerMarcadores(db);
        setUpMap();
        plotMarkers(MarkersArray);
    }

    private void plotMarkers(ArrayList<Interes> markers) {
        if (markers.size() > 0) {
            for (Interes myMarker : markers) {

                // Create user marker with custom icon and other options
                MarkerOptions markerOption = new MarkerOptions().position(new LatLng(Double.parseDouble(myMarker.getLatitud()), (Double.parseDouble(myMarker.getLongitud()))));
                markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.interes));

                Marker currentMarker = mMap.addMarker(markerOption);
                MarkersHashMap.put(currentMarker, myMarker);
            }
        }
    }

    private void setUpMap() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map_view)).getMap();
            LatLng posicionUPV = new LatLng(39.4810811, -0.3421079);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(posicionUPV, 15, 0, 20));
            mMap.animateCamera(cameraUpdate);

            // Check if we were successful in obtaining the map.

            if (mMap != null) {
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(final Marker marker) {
                        marker.showInfoWindow();
                        return true;
                    }
                });
            } else
                Toast.makeText(getApplicationContext(), "Unable to create Maps", Toast.LENGTH_SHORT).show();
        }
    }

    private ArrayList<Interes> obtenerMarcadores(SQLiteDatabase db) {
        String sql = "SELECT * FROM Interes;";
        Cursor cursor = db.rawQuery(sql, null);
        ArrayList<Interes> markers = new ArrayList<Interes>();
        while (cursor.moveToNext()) {
            Interes marker = new Interes(cursor.getString(cursor.getColumnIndex("nombre")), cursor.getString(cursor.getColumnIndex("latitud")), cursor.getString(cursor.getColumnIndex("longitud")));
            markers.add(marker);
        }
        cursor.close();
        return markers;
    }

    public class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
        public MarkerInfoWindowAdapter() {
        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            View v = getLayoutInflater().inflate(R.layout.infowindow_layout, null);

            Interes myMarker = MarkersHashMap.get(marker);

            ImageView markerIcon = (ImageView) v.findViewById(R.id.marker_icon);

            TextView markerLabel = (TextView) v.findViewById(R.id.marker_label);

            markerIcon.setImageResource(R.drawable.interes);

            markerLabel.setText(myMarker.getNombre());
            return v;
        }
    }
}