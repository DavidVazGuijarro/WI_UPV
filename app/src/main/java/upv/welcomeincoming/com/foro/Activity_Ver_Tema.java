package upv.welcomeincoming.com.foro;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import upv.welcomeincoming.com.R;
import util.CircularImageView;
import util.MyPhotoUtil;
import util.Preferencias;
import util.ProgressDialog_Custom;
import util.ScrollViewExt;
import util.ScrollViewListener;
import util.Traductor.ApiKeys;
import util.Traductor.language.Language;
import util.Traductor.translate.Translate;

public class Activity_Ver_Tema extends ActionBarActivity implements ScrollViewListener {

    //ListView listaComentarios;
    Context _context;
    int idtema;
    Tema _tema;
    ScrollViewExt _scroll;
    TextView tv_titulo;
    TextView tv_desc;
    TextView tv_fechaCreacion;
    TextView tv_traducido;
    TextView tv_nombreUsu;
    TextView tv_apellidosUsu;
    ImageView iv_flag;
    ImageView iv_writecoment_deploy;
    CircularImageView imagenUsuarioTema;
    SmoothProgressBar progreso;
    LinearLayout listaLayoutComentarios;
    RelativeLayout layout_escribirComent;
    RelativeLayout container;
    LinearLayout layout_content_write_comentario;
    ImageView iv_flag_writecoment;
    ImageView iv_scrolldown;
    EditText et_writecoment;
    ProgressBar progreso_trad;
    ProgressBar progreso_send;
    ImageButton btn_send;
    ImageButton btn_send_ok;
    InputMethodManager imm;
    MenuItem itemnotification;

    boolean hecomentadoeltema;


    String comentLanguageCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_tema);
        _context = this;
        Translate.setKey(ApiKeys.YANDEX_API_KEY);
        idtema = getIntent().getIntExtra("idtema", -1);
        setTema();
        initComponents();


        this.setTitle(getString(R.string.ver_tema_title));
        this.getSupportActionBar().setHomeButtonEnabled(true);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        hecomentadoeltema = false;


        //la tarea de llenar la lista de comentarios se lanza ahora al crear el menu

    }


    private void initComponents() {
        _scroll = (ScrollViewExt) findViewById(R.id.scrollView);
        comentLanguageCode = _tema.getLanguage();
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        tv_titulo = (TextView) findViewById(R.id.titulo);
        tv_desc = (TextView) findViewById(R.id.tv_descripcion);
        tv_fechaCreacion = (TextView) findViewById(R.id.tv_fechaTema);
        tv_traducido = (TextView) findViewById(R.id.tv_verOriginal);
        tv_nombreUsu = (TextView) findViewById(R.id.tv_user_nombreTema);
        imagenUsuarioTema = (CircularImageView) findViewById(R.id.iv_photoUser_tema);
        tv_apellidosUsu = (TextView) findViewById(R.id.tv_user_apellidos_tema);
        iv_flag = (ImageView) findViewById(R.id.iv_flag_tema);
        iv_writecoment_deploy = (ImageView) findViewById(R.id.iv_writecoment_deploy);
        iv_scrolldown = (ImageView) findViewById(R.id.iv_scrolldown);
        progreso = (SmoothProgressBar) findViewById(R.id.progressbar);
        listaLayoutComentarios = (LinearLayout) findViewById(R.id.lista_comentarios);
        layout_escribirComent = (RelativeLayout) findViewById(R.id.layout_escribirComent);
        layout_content_write_comentario = (LinearLayout) findViewById(R.id.layout_write_coment);
        iv_flag_writecoment = (ImageView) findViewById(R.id.iv_flag_writecoment);
        et_writecoment = (EditText) findViewById(R.id.et_writecoment);
        progreso_send = (ProgressBar) findViewById(R.id.progres_send);
        progreso_trad = (ProgressBar) findViewById(R.id.progreso_trans);
        btn_send = (ImageButton) findViewById(R.id.btn_send);
        btn_send_ok = (ImageButton) findViewById(R.id.btn_send_ok);
        container = (RelativeLayout) findViewById(R.id.container);

        _scroll.setScrollViewListener(this);
        tv_titulo.setText(_tema.getTitulo());
        tv_desc.setText(_tema.getDescripcion());
        tv_fechaCreacion.setText(getFechaFormateada(_tema.getFecha_creacion()));
        tv_traducido.setVisibility(View.GONE);//tv_traducido.setText(_tema.getTitulo());
        tv_nombreUsu.setText(_tema.getNombreUsuario());
        tv_apellidosUsu.setText(_tema.getApellidoUsuario());
        layout_content_write_comentario.setVisibility(View.GONE);
        iv_flag.setImageDrawable(Language.getFlagResourcebyCode(_tema.getLanguage(), _context));
        iv_flag_writecoment.setImageDrawable(Language.getFlagResourcebyCode(comentLanguageCode, _context));
        iv_flag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(_context);
                dialog.setContentView(R.layout.forum_dialog_language);
                dialog.setTitle(_context.getString(R.string.traducir));
                LinearLayout listaItems = (LinearLayout) dialog.findViewById(R.id.layout_select_language);
                llenarItemsLanguageTema(listaItems, _tema, tv_traducido, tv_desc, tv_titulo, iv_flag, progreso_trad, dialog);
                dialog.show();
            }
        });
        layout_escribirComent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (layout_content_write_comentario.getVisibility() == View.VISIBLE) {//ocultar view escribir comentario
                    Animation anim = AnimationUtils.loadAnimation(_context, R.anim.rotate_off);
                    anim.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(et_writecoment.getWindowToken(), 0);
                            layout_content_write_comentario.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });
                    iv_writecoment_deploy.startAnimation(anim);


                } else {//mostrar view escribir comentario
                    Animation anim = AnimationUtils.loadAnimation(_context, R.anim.rotate_on);
                    anim.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            layout_content_write_comentario.setVisibility(View.VISIBLE);
                            bajarScroll();
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });
                    iv_writecoment_deploy.startAnimation(anim);
                    Animation slide = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_down);
                    layout_content_write_comentario.startAnimation(slide);

                }


            }
        });
        iv_flag_writecoment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //implementar codigo para seleccion language
                final Dialog dialog = new Dialog(_context);
                dialog.setContentView(R.layout.forum_dialog_language);
                dialog.setTitle(_context.getString(R.string.idiomacomentario));
                LinearLayout listaItems = (LinearLayout) dialog.findViewById(R.id.layout_select_language);
                llenarItemsLanguageWrite(listaItems, dialog);
                dialog.show();

            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //implmentar tarea enviar comentario
                String texto = et_writecoment.getText().toString();
                if (texto == null || texto.equals("")) return;
                new SendComentTask(texto).execute();
                //ocultar keyboard
                imm.hideSoftInputFromWindow(et_writecoment.getWindowToken(), 0);
            }
        });

        btn_send_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progreso_send.setVisibility(View.GONE);
                btn_send_ok.setVisibility(View.GONE);
                btn_send.setVisibility(View.VISIBLE);
                et_writecoment.setEnabled(true);
                iv_flag_writecoment.setEnabled(true);
                bajarScroll();
            }
        });

        iv_scrolldown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bajarScroll();
            }
        });
        new SetPhotoTask(_tema.getIdUsuario(), _tema.getNombreUsuario(), imagenUsuarioTema).execute();


    }


    private View getItemComentario(final Comentario comentario) {
        final View view = getLayoutInflater().inflate(R.layout.item_foro_comentario, null);
        final TextView tv_desc = (TextView) view.findViewById(R.id.tv_descripcion);
        final CircularImageView photo_user = (CircularImageView) view.findViewById(R.id.iv_photoUser_tema);
        TextView tv_fechaCreacion = (TextView) view.findViewById(R.id.tv_fechaTema);
        final TextView tv_traducido = (TextView) view.findViewById(R.id.tv_verOriginal);
        TextView tv_nombreUsu = (TextView) view.findViewById(R.id.tv_user_nombreTema);
        TextView tv_apellidosUsu = (TextView) view.findViewById(R.id.tv_user_apellidos_tema);
        final ImageView iv_flag = (ImageView) view.findViewById(R.id.iv_flag_tema);
        final ProgressBar barraprogreso = (ProgressBar) view.findViewById(R.id.progreso_trans);
        tv_desc.setText(comentario.getData());
        tv_fechaCreacion.setText(getFechaFormateada(comentario.getFecha()));
        tv_traducido.setVisibility(View.GONE);//tv_traducido.setText(_tema.getTitulo());
        tv_nombreUsu.setText(comentario.getNombreUsuario());
        tv_apellidosUsu.setText(comentario.getApellidoUsuario());
        iv_flag.setImageDrawable(Language.getFlagResourcebyCode(comentario.getLanguage(), _context));

        iv_flag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(_context);
                dialog.setContentView(R.layout.forum_dialog_language);
                dialog.setTitle(_context.getString(R.string.traducir));
                LinearLayout listaItems = (LinearLayout) dialog.findViewById(R.id.layout_select_language);
                llenarItemsLanguageComentario(listaItems, comentario, tv_traducido, tv_desc, iv_flag, barraprogreso, dialog);
                dialog.show();
            }
        });

        new SetPhotoTask(comentario.getIdUsuario(), comentario.getNombreUsuario(), photo_user).execute();
        if (Preferencias.getDNI(this).equals(comentario.getIdUsuario())) hecomentadoeltema = true;


        return view;
    }

    private void llenarItemsLanguageComentario(LinearLayout listaItems, final Comentario comentario, final TextView tv_traducido, final TextView tv_desc, final ImageView iv_flag_c, final ProgressBar barraprogreso, final Dialog dialog) {
        String[] nombres_lang = Language.ArrayNameLanguage;
        String[] code_lang = Language.ArrayLanguageCode;
        for (int i = 0; i < nombres_lang.length; i++) {
            if (code_lang[i].equals(comentario.getLanguage())) continue;
            final String codelang = code_lang[i];
            View v = getLayoutInflater().inflate(R.layout.forum_dialog_language_item, null);
            RelativeLayout item = (RelativeLayout) v.findViewById(R.id.item_language_select);
            TextView tv_name = (TextView) v.findViewById(R.id.tv_languageName);
            final ImageView iv_flag = (ImageView) v.findViewById(R.id.iv_flag);
            tv_name.setText(nombres_lang[i]);
            iv_flag.setImageDrawable(Language.getFlagResourcebyCode(code_lang[i], _context));
            tv_traducido.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    barraprogreso.setVisibility(View.GONE);
                    tv_desc.setText(comentario.getData());
                    iv_flag_c.setImageDrawable(Language.getFlagResourcebyCode(comentario.getLanguage(), _context));
                    tv_traducido.setVisibility(View.GONE);
                }
            });
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //traducir
                    dialog.dismiss();
                    new TraductorTask(comentario.getData(), null, comentario.getLanguage(), codelang, tv_desc, null, tv_traducido, iv_flag_c, barraprogreso, false).execute();//null son parametros cuando es un tema (titulo)
                }
            });
            listaItems.addView(v);
        }
    }

    private void llenarItemsLanguageTema(LinearLayout listaItems, final Tema tema, final TextView tv_traducido, final TextView tv_desc, final TextView tv_titulo, final ImageView iv_flag_c, final ProgressBar barraprogreso, final Dialog dialog) {
        String[] nombres_lang = Language.ArrayNameLanguage;
        String[] code_lang = Language.ArrayLanguageCode;
        for (int i = 0; i < nombres_lang.length; i++) {
            if (code_lang[i].equals(tema.getLanguage())) continue;
            final String codelang = code_lang[i];
            View v = getLayoutInflater().inflate(R.layout.forum_dialog_language_item, null);
            RelativeLayout item = (RelativeLayout) v.findViewById(R.id.item_language_select);
            TextView tv_name = (TextView) v.findViewById(R.id.tv_languageName);
            final ImageView iv_flag = (ImageView) v.findViewById(R.id.iv_flag);
            tv_name.setText(nombres_lang[i]);
            iv_flag.setImageDrawable(Language.getFlagResourcebyCode(code_lang[i], _context));
            tv_traducido.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    barraprogreso.setVisibility(View.GONE);
                    tv_desc.setText(tema.getDescripcion());
                    tv_titulo.setText(tema.getTitulo());
                    iv_flag_c.setImageDrawable(Language.getFlagResourcebyCode(tema.getLanguage(), _context));
                    tv_traducido.setVisibility(View.GONE);
                }
            });
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //traducir
                    dialog.dismiss();
                    new TraductorTask(tema.getDescripcion(), tema.getTitulo(), tema.getLanguage(), codelang, tv_desc, tv_titulo, tv_traducido, iv_flag_c, barraprogreso, true).execute();
                }
            });
            listaItems.addView(v);
        }
    }


    private void llenarItemsLanguageWrite(LinearLayout listaItems, final Dialog dialog) {
        String[] nombres_lang = Language.ArrayNameLanguage;
        String[] code_lang = Language.ArrayLanguageCode;
        for (int i = 0; i < nombres_lang.length; i++) {
            final String codelang = code_lang[i];
            View v = getLayoutInflater().inflate(R.layout.forum_dialog_language_item, null);
            RelativeLayout item = (RelativeLayout) v.findViewById(R.id.item_language_select);
            TextView tv_name = (TextView) v.findViewById(R.id.tv_languageName);
            final ImageView iv_flag_spinner = (ImageView) v.findViewById(R.id.iv_flag);
            tv_name.setText(nombres_lang[i]);
            iv_flag_spinner.setImageDrawable(Language.getFlagResourcebyCode(code_lang[i], _context));
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //traducir
                    dialog.dismiss();
                    iv_flag_writecoment.setImageDrawable(Language.getFlagResourcebyCode(codelang, _context));
                    comentLanguageCode = codelang;
                }
            });
            listaItems.addView(v);
        }
    }

    private void setTema() {
        String titulo = getIntent().getStringExtra("titulo");
        String desc = getIntent().getStringExtra("descripcion");
        Timestamp fechaCreacion = new Timestamp(getIntent().getLongExtra("fechaCreacion", System.currentTimeMillis()));
        String lang = getIntent().getStringExtra("lang");
        String idusu = getIntent().getStringExtra("idusuario");
        String nombreUsu = getIntent().getStringExtra("nombreusuario");
        String apellidos = getIntent().getStringExtra("apellidos");
        _tema = new Tema(idtema, titulo, desc, fechaCreacion, lang, idusu, nombreUsu, apellidos, false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.actualizar_action:
                listaLayoutComentarios.removeAllViews();
                new cargarComentariosTask(this).execute();
                return true;

            case R.id.tema_notificaction_action:
                _tema.changeNotificationStatus();
                if (_tema.notificationOn()) {
                    item.setIcon(R.drawable.ic_action_notification_bell_title_bar_active);
                } else item.setIcon(R.drawable.ic_action_notification_bell_title_bar_inactive);

                return true;

            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.activity__temas_todos, menu);
        MenuItem item = menu.add(Menu.NONE, R.id.actualizar_action, 10, R.string.actualizar);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        item.setIcon(R.drawable.ic_action_navigation_refresh);

        itemnotification = menu.add(Menu.NONE, R.id.tema_notificaction_action, 9, R.string.actualizar);
        itemnotification.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        itemnotification.setIcon(R.drawable.ic_action_notification_bell_title_bar_active);
        itemnotification.setVisible(false);
        _tema.setNotification(true);
        //mostramos o no el icono de notificacion segun corresponda
        if (_tema.getIdUsuario().equals(Preferencias.getPIN(_context))) {
            itemnotification.setVisible(true);
            if (DBHandler_NotificacionesForo.notificacionDesactivada(_context, _tema.getId())) {
                itemnotification.setIcon(R.drawable.ic_action_notification_bell_title_bar_inactive);
                _tema.setNotification(false);
            } else {
                itemnotification.setIcon(R.drawable.ic_action_notification_bell_title_bar_active);
                _tema.setNotification(true);
            }
        } else {
            if (DBHandler_NotificacionesForo.notificacionDesactivada(_context, _tema.getId())) {
                itemnotification.setIcon(R.drawable.ic_action_notification_bell_title_bar_inactive);
                _tema.setNotification(false);
            }
        }

        if (idtema > 0) new cargarComentariosTask(this).execute();

        return true;
    }


    public void bajarScroll() {
        _scroll.post(new Runnable() {

            @Override
            public void run() {
                _scroll.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    @Override
    public void onScrollChanged(ScrollViewExt scrollView, int x, int y, int oldx, int oldy) {
        // We take the last son in the scrollview
        View view = (View) scrollView.getChildAt(scrollView.getChildCount() - 1);
        int diff = ((view.getBottom() - layout_content_write_comentario.getHeight() - layout_escribirComent.getHeight()) - (scrollView.getHeight() + scrollView.getScrollY()));

        // diff hace referencia al punto en el que empieza a verse el cajetin de escibir comentario.
        if (diff > 0) {
            // sta arriba
            iv_scrolldown.setVisibility(View.VISIBLE);
        } else {// esta abajo
            iv_scrolldown.setVisibility(View.GONE);
        }
    }

    class cargarComentariosTask extends AsyncTask<Void, Void, Void> {
        private ProgressDialog_Custom progress;
        private Exception exception;
        ArrayList<Comentario> arrayComentarios;
        Controlador control;
        Context _context;


        public cargarComentariosTask(Context context) {
            try {
                control = Controlador.dameControlador();
            } catch (Exception e) {
                e.printStackTrace();
            }
            _context = context;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            arrayComentarios = control.getComentarios(_context, idtema, -1);
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            progreso.setVisibility(View.GONE);
            if (arrayComentarios != null) {
                llenarListacomentarios();
                //si hay un ocmentario del usuario, se pone visible el icono notificacion
                if (hecomentadoeltema) {
                    itemnotification.setVisible(true);
                    if (DBHandler_NotificacionesForo.notificacionDesactivada(_context, _tema.getId())) {//he comentado el tema y la notifiacion esta desactivada
                        itemnotification.setIcon(R.drawable.ic_action_notification_bell_title_bar_inactive);
                        _tema.setNotification(false);
                    } else {//he comentado el tema y la notifiacion no esta desactivada
                        itemnotification.setIcon(R.drawable.ic_action_notification_bell_title_bar_active);
                        _tema.setNotification(true);
                    }
                }

            } else {
                Toast.makeText(_context, getString(R.string.errorConexion), Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progreso.setVisibility(View.VISIBLE);
        }

        private void llenarListacomentarios() {
            Iterator<Comentario> it = arrayComentarios.iterator();
            while (it.hasNext()) {
                Comentario aux = it.next();
                listaLayoutComentarios.addView(getItemComentario(aux));
            }

        }

    }

    private class TraductorTask extends AsyncTask<String, Void, Void> {
        private String textoAtraducir;
        private String textoTitulo;
        private Language idiomaOrigen;
        private Language idiomaDestino;
        private String textoTraducido;
        private String textotituloTraducido;
        private TextView tv_data;
        private TextView tv_titulo;
        private TextView tv_traducido;
        private ProgressBar progreso;
        private ImageView iv_flag;
        private String idiomaDestinoCode;
        boolean isTema;

        private TraductorTask(String texto, String titulo, String idiomaOrigenCode, String idiomaDestinoCode, TextView tv_data, TextView tv_titulo, TextView tv_traducido, ImageView iv_flag, ProgressBar progreso, Boolean isTema) {
            this.textoAtraducir = texto;
            this.textoTitulo = titulo;
            this.idiomaDestino = Language.fromString(idiomaDestinoCode);
            this.idiomaDestinoCode = idiomaDestinoCode;
            this.idiomaOrigen = Language.fromString(idiomaOrigenCode);
            this.tv_data = tv_data;
            this.tv_titulo = tv_titulo;
            this.tv_traducido = tv_traducido;
            this.progreso = progreso;
            this.iv_flag = iv_flag;
            this.isTema = isTema;
        }

        @Override
        protected void onPreExecute() {
            tv_traducido.setVisibility(View.GONE);
            progreso.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void v) {

            if (textoTraducido != null && !textoTraducido.equals("")) {
                if (textotituloTraducido != null && !textotituloTraducido.equals("")) {
                    tv_titulo.setText(textotituloTraducido);
                }
                tv_data.setText(textoTraducido);
                iv_flag.setImageDrawable(Language.getFlagResourcebyCode(idiomaDestinoCode, _context));
                tv_traducido.setVisibility(View.VISIBLE);

            }
            progreso.setVisibility(View.GONE);

        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                textoTraducido = Translate.execute(textoAtraducir, idiomaOrigen, idiomaDestino);
                if (isTema)
                    textotituloTraducido = Translate.execute(textoTitulo, idiomaOrigen, idiomaDestino);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    private class SendComentTask extends AsyncTask<String, Void, Void> {

        Controlador control;
        String textoComentario;
        Comentario nuevoComentario;
        int res = -1;

        private SendComentTask(String texto) {
            try {
                control = Controlador.dameControlador();
            } catch (Exception e) {
                e.printStackTrace();
            }
            textoComentario = texto;
        }

        @Override
        protected void onPreExecute() {
            et_writecoment.setEnabled(false);
            iv_flag_writecoment.setEnabled(false);
            btn_send.setVisibility(View.GONE);
            btn_send_ok.setVisibility(View.GONE);
            progreso_send.setVisibility(View.VISIBLE);
            bajarScroll();
        }

        @Override
        protected void onPostExecute(Void v) {
            progreso.setVisibility(View.GONE);
            if (res < 0) {//fallo
                btn_send_ok.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_navigation_cancel_red));
            } else {//comentario publicado
                //añadir nuevo comentario al layout
                nuevoComentario = new Comentario(res, idtema, Preferencias.getDNI(_context), new Timestamp(new Date().getTime()),
                        comentLanguageCode, textoComentario, Preferencias.getNombre(_context), Preferencias.getApellidos(_context));
                listaLayoutComentarios.addView(getItemComentario(nuevoComentario));
                btn_send_ok.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_ok));
                et_writecoment.setText("");
                bajarScroll();
                itemnotification.setVisible(true);
            }
            btn_send.setVisibility(View.GONE);
            btn_send_ok.setVisibility(View.VISIBLE);
            progreso_send.setVisibility(View.GONE);

        }

        @Override
        protected Void doInBackground(String... strings) {
            res = control.insertarComentario(_context, textoComentario, comentLanguageCode, idtema);
            return null;
        }
    }

    private class SetPhotoTask extends AsyncTask<String, Void, Void> {

        Controlador control;
        String idusuario;
        ImageView iv_photo;
        String photo64;
        Bitmap photo_bitmap;
        String nombreusuario;

        private SetPhotoTask(String idusuario, String nombre, ImageView iv) {
            try {
                control = Controlador.dameControlador();
            } catch (Exception e) {
                e.printStackTrace();
            }
            nombreusuario = nombre;
            this.idusuario = idusuario;
            this.iv_photo = iv;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(Void v) {
            if (photo_bitmap != null) {
                this.iv_photo.setImageBitmap(photo_bitmap);
                this.iv_photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(_context, Activity_ver_foto.class);
                        i.putExtra("BitmapImage", photo_bitmap);
                        i.putExtra("nombreUsuario", nombreusuario);
                        startActivity(i);
                    }
                });
            }
        }

        @Override
        protected Void doInBackground(String... strings) {
            photo64 = control.getPhotoUser(_context, this.idusuario);
            if (photo64 != null && !photo64.equals("-1")) {
                photo_bitmap = MyPhotoUtil.decodeBase64(photo64);
            }
            return null;
        }
    }


    private String getFechaFormateada(Timestamp date) {
        String daystring = new SimpleDateFormat("dd/MM/yyy, ").format(date);
        Calendar fecha = Calendar.getInstance();
        fecha.setTime(new Date(date.getTime()));
        Calendar fechaaux = Calendar.getInstance();

        if (fecha.get(Calendar.DAY_OF_YEAR) == fechaaux.get(Calendar.DAY_OF_YEAR) && fecha.get(Calendar.YEAR) == fechaaux.get(Calendar.YEAR)) {//es hoy
            daystring = getString(R.string.hoy) + ", ";
            return daystring + new SimpleDateFormat("HH:mm").format(date);
        }

        fechaaux.add(Calendar.DAY_OF_YEAR, -1);
        if (fecha.get(Calendar.DAY_OF_YEAR) == fechaaux.get(Calendar.DAY_OF_YEAR) && fecha.get(Calendar.YEAR) == fechaaux.get(Calendar.YEAR)) {//es ayer
            daystring = getString(R.string.ayer) + ", ";
            return daystring + new SimpleDateFormat("HH:mm").format(date);
        }

        if (fecha.get(Calendar.YEAR) == fechaaux.get(Calendar.YEAR)) {//es mismo año
            daystring = new SimpleDateFormat("dd MMM, ").format(date);
            return daystring + new SimpleDateFormat("HH:mm").format(date);
        }

        return daystring + new SimpleDateFormat("HH:mm").format(date);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }


    @Override
    protected void onPause() {
        if (_tema.notificationOn()) {
            DBHandler_NotificacionesForo.activarNotificacion(_context, _tema.getId());
        } else {
            DBHandler_NotificacionesForo.desactivarNotificacion(_context, _tema.getId());
        }

        super.onPause();
    }


}

