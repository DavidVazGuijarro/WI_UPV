package upv.welcomeincoming.com;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;

import eu.janmuller.android.simplecropimage.CropImage;
import upv.welcomeincoming.com.foro.Controlador;
import upv.welcomeincoming.com.foro.Estadisticas;
import util.MyPhotoUtil;
import util.Preferencias;

public class Activity_Profile extends ActionBarActivity {

    private static final int SELECT_PHOTO = 100;
    private static final int CONFIRM_PHOTO = 200;

    Context _context;
    Intent pictureActionIntent;
    ImageView iv_photo_user;
    ImageView iv_editar;
    LinearLayout layout_estadisticasForo;
    TextView tv_estadisticasForo;
    TextView tv_nombre;
    FrameLayout layout_photo;
    ProgressBar progreso_foto;
    ImageView iv_loading;
    int ratio = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        _context = this;
        iv_photo_user = (ImageView) findViewById(R.id.iv_user);
        iv_editar = (ImageView) findViewById(R.id.iv_editar_photo);
        layout_estadisticasForo = (LinearLayout) findViewById(R.id.layout_estadisticas_foro);
        tv_estadisticasForo = (TextView) findViewById(R.id.tv_stadisticasforo);
        tv_nombre = (TextView) findViewById(R.id.tv_nombre);
        layout_photo = (FrameLayout) findViewById(R.id.layout_foto);
        progreso_foto = (ProgressBar) findViewById(R.id.progressBar2);
        iv_loading = (ImageView) findViewById(R.id.iv_loading);
        this.getSupportActionBar().setHomeButtonEnabled(true);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        intiComponents();

    }

    private void intiComponents() {
        String nombreCompleto = Preferencias.getNombre(this) + " " + Preferencias.getApellidos(this);
        tv_nombre.setText(nombreCompleto);
        Bitmap imagen = Preferencias.getPhoto(this);
        if (imagen == null) {
            imagen = BitmapFactory.decodeResource(getResources(), R.drawable.photo_user_no);
            imagen = MyPhotoUtil.scaleBitmap(imagen);
        }
        imagen = Bitmap.createScaledBitmap(imagen, imagen.getWidth() * ratio, imagen.getHeight() * ratio, true);
        iv_photo_user.setImageBitmap(imagen);
        new SetPhotoTask(iv_photo_user).execute();
        new ForumStatisticsTask(tv_estadisticasForo, layout_estadisticasForo, iv_loading).execute();

        View.OnClickListener oc = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("onclik", "editar foto usuario pulsado");
                pictureActionIntent = new Intent(Intent.ACTION_PICK);
                pictureActionIntent.setType("image/*");
                //startActivityForResult(Intent.createChooser(photoPickerIntent,_context.getString(R.string.selectpicture)), SELECT_PHOTO);
                startDialog();
            }
        };
        layout_photo.setOnClickListener(oc);
        iv_editar.setOnClickListener(oc);


    }

    private void startDialog() {
        AlertDialog.Builder builder;

        LayoutInflater inflater = (LayoutInflater) _context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_chooser_photo, null);

        RelativeLayout itemGaleria = (RelativeLayout) layout.findViewById(R.id.layout_galeria);
        RelativeLayout itemCamara = (RelativeLayout) layout.findViewById(R.id.layout_camara);
        RelativeLayout itemClearPhoto = (RelativeLayout) layout.findViewById(R.id.layout_clear_photo);

        builder = new AlertDialog.Builder(_context);
        builder.setView(layout);
        final AlertDialog alertDialog = builder.create();

        itemGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pictureActionIntent = new Intent(Intent.ACTION_PICK, null);
                pictureActionIntent.setType("image/*");
                startActivityForResult(pictureActionIntent, SELECT_PHOTO);
                alertDialog.dismiss();
            }
        });
        itemCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pictureActionIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(pictureActionIntent, SELECT_PHOTO);
                alertDialog.dismiss();
            }
        });
        itemClearPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new UploadPhotoTask(null, iv_photo_user, progreso_foto).execute();
                alertDialog.dismiss();
            }
        });
        alertDialog.show();


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private class UploadPhotoTask extends AsyncTask<String, Void, Void> {

        Controlador control;
        ProgressBar progreso_photo;
        int resultado;
        Bitmap photo_bitmap;
        ImageView iv;
        Bitmap scaledBitmap;


        private UploadPhotoTask(Bitmap photo_bitmap, ImageView iv, ProgressBar progreso_foto) {
            try {
                control = Controlador.dameControlador();
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.photo_bitmap = photo_bitmap;
            this.iv = iv;
            this.progreso_photo = progreso_foto;
        }

        @Override
        protected void onPreExecute() {
            progreso_foto.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void v) {
            if (resultado > 0) {//ha ido bien
                Toast.makeText(_context, _context.getString(R.string.photoPerfilActualizada), Toast.LENGTH_SHORT).show();
                if (photo_bitmap != null) {
                    Bitmap imagen = Bitmap.createScaledBitmap(scaledBitmap, scaledBitmap.getWidth() * ratio, scaledBitmap.getHeight() * ratio, true);
                    iv_photo_user.setImageBitmap(imagen);
                    Preferencias.setPhoto(_context, scaledBitmap);
                } else {
                    Bitmap imagen = BitmapFactory.decodeResource(getResources(), R.drawable.photo_user_no);
                    imagen = MyPhotoUtil.scaleBitmap(imagen);
                    Bitmap imagenscale = Bitmap.createScaledBitmap(imagen, imagen.getWidth() * ratio, imagen.getHeight() * ratio, true);
                    iv_photo_user.setImageBitmap(imagenscale);
                    Preferencias.setPhoto(_context, imagen);
                }


            } else {
                Toast.makeText(_context, _context.getString(R.string.errorConexion), Toast.LENGTH_SHORT).show();
            }
            this.progreso_photo.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(String... strings) {
            String photo64 = "";
            if (photo_bitmap != null) {
                scaledBitmap = MyPhotoUtil.scaleBitmap(photo_bitmap);
                photo64 = MyPhotoUtil.encodeTobase64(scaledBitmap);
            }
            resultado = control.setPhoto(_context, photo64);
            return null;
        }
    }


    private class SetPhotoTask extends AsyncTask<String, Void, Void> {

        Controlador control;
        ImageView iv_photo;
        String photo64;
        Bitmap photo_bitmap;

        private SetPhotoTask(ImageView iv) {
            try {
                control = Controlador.dameControlador();
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.iv_photo = iv;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(Void v) {
            if (photo_bitmap != null) {
                this.iv_photo.setImageBitmap(photo_bitmap);
            }
        }

        @Override
        protected Void doInBackground(String... strings) {
            photo64 = control.getPhotoUser(_context, Preferencias.getDNI(_context));
            if (photo64 != null && !photo64.equals("-1")) {
                photo_bitmap = MyPhotoUtil.decodeBase64(photo64);
                Preferencias.setPhoto(_context, photo_bitmap);
                photo_bitmap = Bitmap.createScaledBitmap(photo_bitmap, photo_bitmap.getWidth() * ratio, photo_bitmap.getHeight() * ratio, true);
            }
            return null;
        }
    }


    private class ForumStatisticsTask extends AsyncTask<String, Void, Void> {

        Controlador control;
        TextView tv_estadisticas;
        LinearLayout layout_estadisticas;
        ImageView iv_loading;
        Estadisticas estadistica;
        AnimationDrawable animation;


        private ForumStatisticsTask(TextView tv, LinearLayout layout, ImageView iv_loading) {
            try {
                control = Controlador.dameControlador();
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.tv_estadisticas = tv;
            this.layout_estadisticas = layout;
            this.iv_loading = iv_loading;

            this.iv_loading.setBackgroundResource(R.drawable.anim_loading);//setBackgroundDrawable(animation);
            animation = (AnimationDrawable) iv_loading.getBackground();


        }

        @Override
        protected void onPreExecute() {
            iv_loading.setVisibility(View.VISIBLE);
            animation.start();
            layout_estadisticas.setVisibility(View.GONE);
        }

        @Override
        protected void onPostExecute(Void v) {
            if (estadistica != null) {
                tv_estadisticas.setText(getTextoEstadistica());
                layout_estadisticas.setVisibility(View.VISIBLE);
            }
            animation.stop();
            iv_loading.setVisibility(View.GONE);

        }

        @Override
        protected Void doInBackground(String... strings) {
            estadistica = control.getEstadisticasUser(_context);
            return null;
        }

        private String getTextoEstadistica() {
            String fecha = new SimpleDateFormat("d MMMM yyyy").format(estadistica.getFecha_ingreso());
            String patron = _context.getResources().getString(R.string.textoEstadisticasForo).replace("%%", "%"); //"Has publicado %s temas y %s comentarios desde %s.";
            return String.format(patron, "" + estadistica.getNumTemas(), "" + estadistica.getNumComentarios(), fecha);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    runCropImage(selectedImage);

                    //try {
                    //Bitmap yourSelectedImage = MyPhotoUtil.decodeScaleUri(this, selectedImage);
                    //Intent i = new Intent(_context, Activity_Edit_Photo_Profile.class);
                    //i.putExtra("BitmapImage", yourSelectedImage);
                    //startActivityForResult(i, CONFIRM_PHOTO);
                    //} catch (FileNotFoundException e) {
                    //    e.printStackTrace();
                    //}
                }
                break;
            case CONFIRM_PHOTO:
                if (resultCode == RESULT_OK) {
                    String path = imageReturnedIntent.getStringExtra(CropImage.IMAGE_PATH);
                    // if nothing received
                    if (path == null) {
                        return;
                    }
                    // cropped bitmap
                    Bitmap selectedImage = BitmapFactory.decodeFile(path);

                    //Bitmap selectedImage = (Bitmap)imageReturnedIntent.getParcelableExtra("Bitmap");
                    selectedImage = MyPhotoUtil.scaleBitmap(selectedImage);
                    new UploadPhotoTask(selectedImage, iv_photo_user, progreso_foto).execute();

                }

        }
    }


    private void runCropImage(Uri uri) {
        // create explicit intent
        Intent intent = new Intent(this, CropImage.class);
        // tell CropImage activity to look for image to crop
        String filePath = getRealPathFromURI(this, uri);
        intent.putExtra(CropImage.IMAGE_PATH, filePath);
        // allow CropImage activity to rescale image
        intent.putExtra(CropImage.SCALE, false);
        // if the aspect ratio is fixed to ratio 3/2
        intent.putExtra(CropImage.ASPECT_X, 157);
        intent.putExtra(CropImage.ASPECT_Y, 148);
        // start activity CropImage with certain request code and listen
        // for result
        startActivityForResult(intent, CONFIRM_PHOTO);
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


}



