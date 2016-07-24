package upv.welcomeincoming.com;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ActionViewTarget;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.util.concurrent.atomic.AtomicInteger;

import util.DBHandler_Horarios;
import util.InternetConnectionChecker;
import util.Preferencias;
import util.ProgressDialog_Custom;

public class Activity_Home extends ActionBarActivity implements Fragment_Calendar.CalendarListener, OnShowcaseEventListener {

    private static final int FRAGMENT_CALENDAR_CODE = 0;
    private static final int FRAGMENT_FORO_CODE = 1;
    public static Handler mUiHandler = null;
    private String[] opcionesMenu;
    private DrawerLayout drawerLayout;
    private LinearLayout drawerList;
    private LinearLayout panelDrawer;
    private int fragmentActual;
    private String asignatura;
    ActionBarDrawerToggle drawerToggle;
    FragmentTransaction tx;
    DBHandler_Horarios helper;
    ProgressDialog_Custom progress;
    SQLiteDatabase db;
    ShowcaseView sv;
    InternetConnectionChecker icc;
    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    String SENDER_ID = "1006259852133";
    static final String TAG = "welcomeincomingapp";
    TextView mDisplay;
    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    Menu menu;
    String regid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progress = new ProgressDialog_Custom(this, getString(R.string.loading));
        helper = new DBHandler_Horarios(this);
        icc = new InternetConnectionChecker();
        db = helper.getWritableDatabase();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        opcionesMenu = new String[]{getString(R.string.menu_option1), getString(R.string.menu_option2), getString(R.string.menu_option3), getString(R.string.menu_option4), getString(R.string.menu_option5), getString(R.string.menu_option6), getString(R.string.menu_option7)};

        setContentView(R.layout.activity_home);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (LinearLayout) findViewById(R.id.home_drawer);
        panelDrawer = (LinearLayout) findViewById(R.id.panel_drawer);
        getWindow().setUiOptions(ActivityInfo.UIOPTION_SPLIT_ACTION_BAR_WHEN_NARROW);
        tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.contenedor_fragment, new Fragment_Home());
        tx.commit();
        fragmentActual = 0;
        // Check device for Play Services APK. If check succeeds, proceed with
        //  upv.welcomeincoming.app.GCM registration.

        inicializarElementos();

        ActionViewTarget target = new ActionViewTarget(this, ActionViewTarget.Type.HOME);

        if (Preferencias.getFirstHome(this) == 1) {
            sv = new ShowcaseView.Builder(this)
                    .setTarget(target)
                    .setContentTitle(R.string.tutorial_drawer_title)
                    .setContentText(R.string.tutorial_drawer_message)
                    .setStyle(R.style.ShowCaseTheme)
                    .build();
            Preferencias.setFirstHome(this, 0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        this.menu = menu;
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FRAGMENT_CALENDAR_CODE) {
            if (resultCode == RESULT_OK) {
                mostrarFragment(1);
            }
        }
        if (requestCode == FRAGMENT_FORO_CODE) {
            if (resultCode == RESULT_OK) {
                mostrarFragment(6);
            }
        }


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
    }

    private void inicializarElementos() {
        String tituloApp = getTitle().toString();
        /*  ITEMS DRAWER */

        //Home
        View itemHome = generateItem(getString(R.string.menu_option1), R.drawable.icon_inicio);
        itemHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarFragment(0);
            }
        });
        drawerList.addView(itemHome);
        addDividier();

        //Calendar
        View itemFind = generateItem(getString(R.string.menu_option2), R.drawable.ic_action_collections_go_to_today);
        itemFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (icc.checkInternetConnection(getApplicationContext())) {
                    drawerLayout.closeDrawer(panelDrawer);
                    getActionBar().setTitle(opcionesMenu[1]);
                    if (Preferencias.getDNI(getApplicationContext()).equals("") || Preferencias.getPIN(getApplicationContext()).equals("")) {
                        Intent login = new Intent(getApplicationContext(), Activity_login.class);
                        startActivityForResult(login, FRAGMENT_CALENDAR_CODE);
                    } else {
                        mostrarFragment(1);
                    }
                } else {
                    Intent intent = new Intent(getApplicationContext(), Activity_no_connection.class);
                    intent.putExtra("case", getString(R.string.noInetCalendar));
                    startActivity(intent);
                    mostrarFragment(1);
                }
            }
        });
        drawerList.addView(itemFind);
        addDividier();

        //Info
        View itemInfo = generateItem(getString(R.string.menu_option3), R.drawable.ic_action_about);
        itemInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarFragment(2);
            }
        });
        drawerList.addView(itemInfo);
        addDividier();

        //Traduccion
        View itemTraduce = generateItem(getString(R.string.menu_option4), R.drawable.ic_action_device_access_mic);
        itemTraduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (icc.checkInternetConnection(getApplicationContext())) {
                    mostrarFragment(3);
                } else {
                    Intent intent = new Intent(getApplicationContext(), Activity_no_connection.class);
                    intent.putExtra("case", getString(R.string.noInetBlock));
                    startActivity(intent);
                }
            }
        });
        drawerList.addView(itemTraduce);
        addDividier();

        //Locate
        View itemLocate = generateItem(getString(R.string.menu_option5), R.drawable.ic_action_location_map);
        itemLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (icc.checkInternetConnection(getApplicationContext())) {
                    mostrarFragment(4);
                } else {
                    Intent intent = new Intent(getApplicationContext(), Activity_no_connection.class);
                    intent.putExtra("case", getString(R.string.noInetMaps));
                    startActivity(intent);
                    mostrarFragment(4);
                }
            }
        });
        drawerList.addView(itemLocate);
        addDividier();
        //Forum
        View itemForum = generateItem(getString(R.string.menu_option7), R.drawable.ic_action_social_group);
        itemForum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (icc.checkInternetConnection(getApplicationContext())) {
                    if (!Preferencias.logeado(getApplicationContext())) {
                        Intent login = new Intent(getApplicationContext(), Activity_login.class);
                        startActivityForResult(login, FRAGMENT_FORO_CODE);
                    } else {
                        mostrarFragment(6);
                    }
                } else {
                    Intent intent = new Intent(getApplicationContext(), Activity_no_connection.class);
                    intent.putExtra("case", getString(R.string.noInetBlock));
                    startActivity(intent);
                }
            }
        });
        drawerList.addView(itemForum);
        addDividier();

        //Option
        View itemOption = generateItem(getString(R.string.menu_option6), R.drawable.ic_action_action_settings);
        itemOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarFragment(5);
            }
        });
        drawerList.addView(itemOption);
        addDividier();



        /*   DrawerToggle  */

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            public void onDrawerClosed(View view) {
                //getSupportActionBar().setTitle(tituloSeccion);
                // ActivityCompat.invalidateOptionsMenu(MainActivity.this);
            }

            public void onDrawerOpened(View drawerView) {
                if (sv != null)
                    sv.hide();
                // ActivityCompat.invalidateOptionsMenu(MainActivity.this); //invoca a onPrepareOptionsMenu()
            }
        };

        drawerToggle.setDrawerIndicatorEnabled(true); //Permite mostrar el icono del drawer
        drawerLayout.setDrawerListener(drawerToggle);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

    }


    private View generateItem(String texto, int idIcon) {
        View item = this.getLayoutInflater().inflate(R.layout.item_drawer, null);
        TextView tv = (TextView) item.findViewById(R.id.tv_item_listdrawer);
        ImageView iv = (ImageView) item.findViewById(R.id.iv_item_listdrawer);
        tv.setText(texto);
        tv.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/futura_font.ttf"));
        iv.setBackgroundResource(idIcon);
        return item;
    }

    private void addDividier() {
        View v = this.getLayoutInflater().inflate(R.layout.dividier_itemdrawer, null);
        drawerList.addView(v);
    }

    private void mostrarFragment(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new Fragment_Home();
                break;
            case 1:
                fragment = new Fragment_Calendar();
                break;
            case 2:
                fragment = new Fragment_Info();
                break;
            case 3:
                fragment = new Fragment_Traduccion();
                break;
            case 4:
                fragment = new Fragment_Localizacion();
                break;
            case 5:
                fragment = new Fragment_Opciones();
                break;
            case 6:
                fragment = new Fragment_Foro();
                break;
        }


        if (fragment != null) {

            FragmentManager fgm = getSupportFragmentManager();
            fgm.beginTransaction().replace(R.id.contenedor_fragment, fragment).commit();
            getActionBar().setTitle(opcionesMenu[position]);
            drawerLayout.closeDrawer(panelDrawer);
            fragmentActual = position;
        }

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            //
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if (fragmentActual == 0) super.onBackPressed();
        else mostrarFragment(0);

    }

    @Override
    public void CalendarListenerError(String string) {

        Log.w(((Object) this).getClass().getName(), "Llamada a -> CalendarListenerError -> " + string);

        DialogFragment dialogFragment = this.getDialog(string);

        getSupportFragmentManager().popBackStack();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.contenedor_fragment, dialogFragment)
                .commit();
    }

    private DialogFragment getDialog(String string) {

        final String message = string;

        return new DialogFragment() {
            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setTitle("Atención");
                builder.setMessage(message);

                builder.setNeutralButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                return builder.create();
            }
        };
    }

    @Override
    public void onShowcaseViewHide(ShowcaseView showcaseView) {

    }

    @Override
    public void onShowcaseViewDidHide(ShowcaseView showcaseView) {

    }

    @Override
    public void onShowcaseViewShow(ShowcaseView showcaseView) {

    }

    // You need to do the Play Services APK check here too.
    @Override
    protected void onResume() {
        super.onResume();
    }
}