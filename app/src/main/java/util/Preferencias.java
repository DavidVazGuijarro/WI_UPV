package util;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;

public class Preferencias {
    private static final String APP_FILE = "APP_PREFERENCES";
    private static final String USER_FILE = "USER_PREFERENCES";
    private static final String USER_DNI = "DNI";
    private static final String USER_PIN = "PIN";
    private static final String USER_PIN_CALENDAR = "PIN_CALENDAR";
    private static final String APP_LENGUAGE = "LENGUAGE";
    private static final String USER_LANGUAGE = "LANGUAGE";
    private static final String APP_CALENDAR_ALERTS = "CALENDAR_ALERTS";
    private static final String APP_FORUM_ALERTS = "FORUM_ALERTS";
    private static final String USER_NAME = "NAME";
    private static final String USER_PHOTO = "PHOTO";
    private static final String USER_APELLIDOS = "APELLIDOS";
    private static final String PROPERTY_REG_ID = "PROPERTY_REG_ID";
    private static final String APP_FIRST_HOME = "FIRST_HOME";
    private static final String APP_FIRST_OPTIONS = "FIRST_OPTIONS";
    private static final String REGISTRADO = "REGISTRADO";
    private static final String USER_USERNAME = "USERNAME";


    public static void desloguearse(Context context) {
        context.getSharedPreferences(USER_FILE, Activity.MODE_PRIVATE).edit().clear().commit();

    }

    public static boolean logeado(Context context) {
        return !getDNI(context).isEmpty();
    }

    //USER PREFERENCES
    public static String getDNI(Context context) {
        try {
            return context.getSharedPreferences(USER_FILE, Activity.MODE_PRIVATE).getString(USER_DNI, "");
        } catch (Exception e) {
            Log.d(Preferencias.class.getSimpleName(), "Exception", e);
        }
        return null;
    }

    public static String getPIN(Context context) {
        try {
            return context.getSharedPreferences(USER_FILE, Activity.MODE_PRIVATE).getString(USER_PIN, "");
        } catch (Exception e) {
            Log.d(Preferencias.class.getSimpleName(), "Exception", e);
        }
        return null;
    }

    public static void setPIN(Context context, String pass) {
        try {
            SharedPreferences.Editor editor = context.getSharedPreferences(USER_FILE, Activity.MODE_PRIVATE).edit();
            editor.putString(USER_PIN, pass);
            editor.commit();
        } catch (Exception e) {
            Log.d(Preferencias.class.getSimpleName(), "Exception", e);
        }
    }

    public static String getPINCalendar(Context context) {
        try {
            return context.getSharedPreferences(USER_FILE, Activity.MODE_PRIVATE).getString(USER_PIN_CALENDAR, "");
        } catch (Exception e) {
            Log.d(Preferencias.class.getSimpleName(), "Exception", e);
        }
        return null;
    }

    public static void setPINCalendar(Context context, String pass) {
        try {
            SharedPreferences.Editor editor = context.getSharedPreferences(USER_FILE, Activity.MODE_PRIVATE).edit();
            editor.putString(USER_PIN_CALENDAR, pass);
            editor.commit();
        } catch (Exception e) {
            Log.d(Preferencias.class.getSimpleName(), "Exception", e);
        }
    }

    public static void setDNI(Context context, String user) {
        try {
            SharedPreferences.Editor editor = context.getSharedPreferences(USER_FILE, Activity.MODE_PRIVATE).edit();
            editor.putString(USER_DNI, user);
            editor.commit();
        } catch (Exception e) {
            Log.d(Preferencias.class.getSimpleName(), "Exception", e);
        }
    }

    public static String getNombre(Context context) {
        try {
            return context.getSharedPreferences(USER_FILE, Activity.MODE_PRIVATE).getString(USER_NAME, "");
        } catch (Exception e) {
            Log.d(Preferencias.class.getSimpleName(), "Exception", e);
        }
        return null;
    }

    public static void setNombre(Context context, String name) {
        try {
            SharedPreferences.Editor editor = context.getSharedPreferences(USER_FILE, Activity.MODE_PRIVATE).edit();
            editor.putString(USER_NAME, name);
            editor.commit();
        } catch (Exception e) {
            Log.d(Preferencias.class.getSimpleName(), "Exception", e);
        }
    }

    public static String getApellidos(Context context) {
        try {
            return context.getSharedPreferences(USER_FILE, Activity.MODE_PRIVATE).getString(USER_APELLIDOS, "");
        } catch (Exception e) {
            Log.d(Preferencias.class.getSimpleName(), "Exception", e);
        }
        return null;
    }

    public static void setApellidos(Context context, String apellido) {
        try {
            SharedPreferences.Editor editor = context.getSharedPreferences(USER_FILE, Activity.MODE_PRIVATE).edit();
            editor.putString(USER_APELLIDOS, apellido);
            editor.commit();
        } catch (Exception e) {
            Log.d(Preferencias.class.getSimpleName(), "Exception", e);
        }
    }

    public static void setGCMID(Context context, String id) {
        try {
            SharedPreferences.Editor editor = context.getSharedPreferences(APP_FILE, Activity.MODE_PRIVATE).edit();
            editor.putString(PROPERTY_REG_ID, id);
            editor.commit();
        } catch (Exception e) {
            Log.d(Preferencias.class.getSimpleName(), "Exception", e);
        }
    }

    public static String getGCMID(Context context) {
        try {
            return context.getSharedPreferences(APP_FILE, Activity.MODE_PRIVATE).getString(PROPERTY_REG_ID, "");
        } catch (Exception e) {
            Log.d(Preferencias.class.getSimpleName(), "Exception", e);
        }
        return null;
    }

    public static boolean getCalendarAlerts(Context context) {
        try {
            return context.getSharedPreferences(APP_FILE, Activity.MODE_PRIVATE).getBoolean(APP_CALENDAR_ALERTS, false);
        } catch (Exception e) {
            Log.d(Preferencias.class.getSimpleName(), "Exception", e);
        }
        return false;
    }

    public static boolean getForumAlerts(Context context) {
        try {
            return context.getSharedPreferences(APP_FILE, Activity.MODE_PRIVATE).getBoolean(APP_FORUM_ALERTS, true);
        } catch (Exception e) {
            Log.d(Preferencias.class.getSimpleName(), "Exception", e);
        }
        return false;
    }

    public static void setForumAlerts(Context context, boolean id) {
        try {
            SharedPreferences.Editor editor = context.getSharedPreferences(APP_FILE, Activity.MODE_PRIVATE).edit();
            editor.putBoolean(APP_FORUM_ALERTS, id);
            editor.commit();
        } catch (Exception e) {
            Log.d(Preferencias.class.getSimpleName(), "Exception", e);
        }
    }

    public static void setCalendarAlerts(Context context, boolean id) {
        try {
            SharedPreferences.Editor editor = context.getSharedPreferences(APP_FILE, Activity.MODE_PRIVATE).edit();
            editor.putBoolean(APP_CALENDAR_ALERTS, id);
            editor.commit();
        } catch (Exception e) {
            Log.d(Preferencias.class.getSimpleName(), "Exception", e);
        }
    }

    public static void setFirstHome(Context context, int id) {
        try {
            SharedPreferences.Editor editor = context.getSharedPreferences(APP_FILE, Activity.MODE_PRIVATE).edit();
            editor.putInt(APP_FIRST_HOME, id);
            editor.commit();
        } catch (Exception e) {
            Log.d(Preferencias.class.getSimpleName(), "Exception", e);
        }
    }

    public static int getFirstHome(Context context) {
        try {
            return context.getSharedPreferences(APP_FILE, Activity.MODE_PRIVATE).getInt(APP_FIRST_HOME, 1);
        } catch (Exception e) {
            Log.d(Preferencias.class.getSimpleName(), "Exception", e);
        }
        return -1;
    }

    public static void setFirstOptions(Context context, int id) {
        try {
            SharedPreferences.Editor editor = context.getSharedPreferences(APP_FILE, Activity.MODE_PRIVATE).edit();
            editor.putInt(APP_FIRST_OPTIONS, id);
            editor.commit();
        } catch (Exception e) {
            Log.d(Preferencias.class.getSimpleName(), "Exception", e);
        }
    }

    public static int getFirstOptions(Context context) {
        try {
            return context.getSharedPreferences(APP_FILE, Activity.MODE_PRIVATE).getInt(APP_FIRST_OPTIONS, 1);
        } catch (Exception e) {
            Log.d(Preferencias.class.getSimpleName(), "Exception", e);
        }
        return -1;
    }

    public static String getLanguage(Context context) {
        try {
            return context.getSharedPreferences(APP_FILE, Activity.MODE_PRIVATE).getString(USER_LANGUAGE, "es");
        } catch (Exception e) {
            Log.d(Preferencias.class.getSimpleName(), "Exception", e);
        }
        return null;
    }

    public static void setLanguage(Context context, String language_code) {
        try {
            SharedPreferences.Editor editor = context.getSharedPreferences(APP_FILE, Activity.MODE_PRIVATE).edit();
            editor.putString(USER_LANGUAGE, language_code);
            editor.commit();
        } catch (Exception e) {
            Log.d(Preferencias.class.getSimpleName(), "Exception", e);
        }
    }

    public static String getUsername(Context context) {
        return getNombre(context);
    }

    public static void setUsername(Context context, String username) {
        setNombre(context, username);
    }

    public static Bitmap getPhoto(Context context) {
        try {
            String photo64 = context.getSharedPreferences(USER_FILE, Activity.MODE_PRIVATE).getString(USER_PHOTO, "");
            if (photo64.equals("")) return null;
            return MyPhotoUtil.decodeBase64(photo64);
        } catch (Exception e) {
            Log.d(Preferencias.class.getSimpleName(), "Exception", e);
        }
        return null;
    }

    public static void setPhoto(Context context, Bitmap photo) {
        try {
            String photo64 = MyPhotoUtil.encodeTobase64(photo);
            SharedPreferences.Editor editor = context.getSharedPreferences(USER_FILE, Activity.MODE_PRIVATE).edit();
            editor.putString(USER_PHOTO, photo64);
            editor.commit();
        } catch (Exception e) {
            Log.d(Preferencias.class.getSimpleName(), "Exception", e);
        }
    }

    public static void registrado(Context context) {
        try {
            SharedPreferences.Editor editor = context.getSharedPreferences(APP_FILE, Activity.MODE_PRIVATE).edit();
            editor.putBoolean(REGISTRADO, true);
            editor.commit();
        } catch (Exception e) {
            Log.d(Preferencias.class.getSimpleName(), "Exception", e);
        }
    }

    public static boolean isPrimerRegistro(Context context) {
        boolean registrado = false;
        try {
            registrado = context.getSharedPreferences(APP_FILE, Activity.MODE_PRIVATE).getBoolean(REGISTRADO, false);
        } catch (Exception e) {
            Log.d(Preferencias.class.getSimpleName(), "Exception", e);
        }
        return !registrado;
    }
}



