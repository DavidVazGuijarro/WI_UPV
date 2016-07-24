package upv.welcomeincoming.com.GCM;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

import util.Preferencias;

/**
 * Created by Joe on 5/28/2014.
 */
public class GCMConfig {
    // Google Project Number
    static final String GOOGLE_PROJECT_ID = "741478944070";

    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static String SENDER_ID = "741478944070";
    private static final String TAG = "welcomeincomingapp";
    private static GoogleCloudMessaging gcm;
    private static String regid = "";


    public static String getGcmRegId(Activity _context) {
        if (checkPlayServices(_context)) {
            gcm = GoogleCloudMessaging.getInstance(_context);
            regid = getRegistrationId(_context.getApplicationContext());

            if (regid.isEmpty()) {
                Log.e("error", "Sin reg ID");
                registerInBackground(_context);
            } else {
                Log.e("GcmRegID", regid);
            }
        } else {
            Log.i(TAG, "No valid Google Play Services APK found.");
        }
        return regid;
    }


    /**
     * Registers the application with upv.welcomeincoming.app.GCM servers asynchronously.
     * <p/>
     * Stores the registration ID and app versionCode in the application's
     * shared preferences.
     */
    private static void registerInBackground(final Activity _context) {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(_context.getApplicationContext());
                    }
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;
                    Log.e("regID", regid);
                    // You should send the registration ID to your server over HTTP,
                    // so it can use upv.welcomeincoming.app.GCM/HTTP or CCS to send messages to your app.
                    // The request to your server should be authenticated if your app
                    // is using accounts.
                    sendRegistrationIdToBackend();

                    // For this demo: we don't need to send it because the device
                    // will send upstream messages to a server that echo back the
                    // message using the 'from' address in the message.

                    // Persist the regID - no need to register again.
                    storeRegistrationId(_context.getApplicationContext(), regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.

                    Log.e("error", msg);
                }
                return msg;
            }
        }.execute(null, null, null);
    }

    /**
     * Sends the registration ID to your server over HTTP, so it can use upv.welcomeincoming.app.GCM/HTTP
     * or CCS to send messages to your app. Not needed for this demo since the
     * device sends upstream messages to a server that echoes back the message
     * using the 'from' address in the message.
     */
    public static void sendRegistrationIdToBackend() {
        if (!regid.isEmpty()) {
            MessageSender messageSender = new MessageSender();
            Bundle dataBundle = new Bundle();
            dataBundle.putString("action", "ECHO");
            dataBundle.putString("Mensaje", "REGISTRO_OK");
            messageSender.sendMessage(dataBundle, gcm);
            Log.e("gcm", "Succeded!");
        } else {
            Log.e("gcm", "no registration id");
        }
    }

    /**
     * Stores the registration ID and app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId   registration ID
     */
    private static void storeRegistrationId(Context context, String regId) {
        Log.i(TAG, "Saving regId on app version ");
        Preferencias.setGCMID(context, regId);
    }


    /**
     * Gets the current registration ID for application on upv.welcomeincoming.app.GCM service.
     * <p/>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     * registration ID.
     */
    private static String getRegistrationId(Context _context) {
        String registrationId = Preferencias.getGCMID(_context);
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        /*int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }*/
        return registrationId;
    }


    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    public static boolean checkPlayServices(Activity _context) {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(_context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, _context,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
            }
            return false;
        }
        return true;
    }

}
