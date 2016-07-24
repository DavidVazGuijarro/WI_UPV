package upv.welcomeincoming.com;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.webkit.WebView;

import com.wikitude.architect.ArchitectView;
import com.wikitude.architect.ArchitectView.ArchitectConfig;
import com.wikitude.architect.ArchitectView.SensorAccuracyChangeListener;

import java.io.IOException;
import java.util.Locale;

import util.ArchitectViewHolderInterface.ILocationProvider;
import util.LocationProvider;

public class Acvitity_VistaRealidadAumentada extends Activity {

    /**
     * soporte de Realidad Aumentada de Wikitude; architectView es donde se renderiza la camara, markers, etc.
     */
    protected ArchitectView architectView;

    protected SensorAccuracyChangeListener sensorAccuracyListener;

    /**
     * ultima localizacion conocida del usuario
     */
    protected Location lastKnownLocaton;

    /**
     * estrategia de localizacion
     */
    protected ILocationProvider locationProvider;

    /**
     * locationListener para obtener cambios de localizacion
     */
    protected LocationListener locationListener;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_architect_view);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        //Wikitude: Cambio en WebView para Android KitKat
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (0 != (getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE)) {
                WebView.setWebContentsDebuggingEnabled(true);
            }
        }

        // establecer AR-view
        this.architectView = (ArchitectView) this.findViewById(R.id.architectView);

        //Inicializar clave de Wikitude SDK (si se posee)
        // (en caso contrario, aparece una marca de agua en la architectView)
        final ArchitectConfig config = new ArchitectConfig("" /* license key */);

        this.architectView.onCreate(config);

        // listener del locationProvider, maneja los cambios de localizacion
        this.locationListener = new LocationListener() {
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }

            @Override
            public void onLocationChanged(final Location location) {
                if (location != null) {
                    // establecer ultima localizacion
                    Acvitity_VistaRealidadAumentada.this.lastKnownLocaton = location;
                    if (Acvitity_VistaRealidadAumentada.this.architectView != null) {
                        // chequeamos si la localizacion tiene altitud a un determinado nivel de exactitud (para invocar al metodo adecuado en la ARView)
                        if (location.hasAltitude() && location.hasAccuracy() && location.getAccuracy() < 7) {
                            Acvitity_VistaRealidadAumentada.this.architectView.setLocation(location.getLatitude(), location.getLongitude(), location.getAltitude(), location.getAccuracy());
                        } else {
                            Acvitity_VistaRealidadAumentada.this.architectView.setLocation(location.getLatitude(), location.getLongitude(), location.hasAccuracy() ? location.getAccuracy() : 1000);
                        }
                    }
                }
            }
        };

        // locationProvider
        this.locationProvider = new LocationProvider(this, this.locationListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        //Cargar datos del World a mostrar (edificio e idioma)

        super.onPostCreate(savedInstanceState);

        String building = getIntent().getStringExtra("poi");

        //Obtener idioma (Wikitude es codigo nativo)
        String auxlang = Locale.getDefault().getISO3Language().toLowerCase();
        String lang = "en";
        if (auxlang.equals("spa")) lang = "es";

        //Wikitude. Inicializar architectView. Seleccionar World a mostrar
        this.architectView.onPostCreate();
        try {
            this.architectView.load("upv/index_" + lang + "_" + building + ".html");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // reanudar architectView
        if (this.architectView != null) {
            this.architectView.onResume();
            // registrar accuracy listener en architectView
            if (this.sensorAccuracyListener != null) {
                this.architectView.registerSensorAccuracyChangeListener(this.sensorAccuracyListener);
            }
        }

        // reanudar locationProvider
        if (this.locationProvider != null) {
            this.locationProvider.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // pausar architectView (obligatorio)
        if (this.architectView != null) {
            this.architectView.onPause();

            // quitar registro de accuracy listener en la architectView
            if (this.sensorAccuracyListener != null) {
                this.architectView.unregisterSensorAccuracyChangeListener(this.sensorAccuracyListener);
            }
        }

        //Pausar locationProvider
        if (this.locationProvider != null) {
            this.locationProvider.onPause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // destruir architectView (obligatorio)
        if (this.architectView != null) {
            this.architectView.onDestroy();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (this.architectView != null) {
            this.architectView.onLowMemory();
        }
    }
}
