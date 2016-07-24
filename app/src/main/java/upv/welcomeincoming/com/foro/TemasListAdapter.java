package upv.welcomeincoming.com.foro;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import upv.welcomeincoming.com.R;
import util.Traductor.language.Language;

/**
 * Created by Marcos on 2/08/14.
 */
public class TemasListAdapter extends ArrayAdapter<Tema> {
    private Context _context;
    private List<Tema> temas;
    private String _word;

    public TemasListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        _context = context;
    }

    public TemasListAdapter(Context context, int resource, List<Tema> items, String word) {
        super(context, resource, items);
        temas = items;
        _context = context;
        _word = word;
    }

    @Override
    public int getCount() {
        return temas.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {

            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            view = vi.inflate(R.layout.item_foro_tema, null);

        }

        final Tema tema = getItem(position);

        if (tema != null) {
            LinearLayout layout_item = (LinearLayout) view.findViewById(R.id.layout_item);
            TextView tv_titulo = (TextView) view.findViewById(R.id.tv_titulo);
            TextView tv_descripcion = (TextView) view.findViewById(R.id.tv_descripcion);
            TextView tv_numComents = (TextView) view.findViewById(R.id.tv_numcoments);
            TextView tv_nombreUsuario = (TextView) view.findViewById(R.id.tv_nombreUsuario);
            TextView tv_fecha = (TextView) view.findViewById(R.id.tv_fechaCreacion);
            ImageView iv_flag = (ImageView) view.findViewById(R.id.iv_flag);

            if (_word != null) {
                String newWord = "<font color='#E98C23'>" + _word + "</font>";
                String titulo = tema.getTitulo().replace(_word, newWord);
                tv_titulo.setText(Html.fromHtml(titulo));
            } else {
                tv_titulo.setText(tema.getTitulo());
            }
            tv_descripcion.setText(tema.getDescripcion());
            tv_numComents.setText("" + tema.getNumcomentarios());
            tv_nombreUsuario.setText(tema.getNombreUsuario() + " " + tema.getApellidoUsuario() + ", ");
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
            tv_fecha.setText(getFechaFormateada(tema.getFecha_creacion()));
            iv_flag.setImageDrawable(Language.getFlagResourcebyCode(tema.getLanguage(), _context));
            layout_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(_context, Activity_Ver_Tema.class);
                    i.putExtra("idtema", tema.getId());
                    i.putExtra("titulo", tema.getTitulo());
                    i.putExtra("descripcion", tema.getDescripcion());
                    i.putExtra("fechaCreacion", tema.getFecha_creacion().getTime());
                    i.putExtra("lang", tema.getLanguage());
                    i.putExtra("idusuario", tema.getIdUsuario());
                    i.putExtra("nombreusuario", tema.getNombreUsuario());
                    i.putExtra("apellidos", tema.getApellidoUsuario());
                    _context.startActivity(i);
                }
            });


        }

        return view;

    }

    private String getFechaFormateada(Timestamp date) {
        String daystring = new SimpleDateFormat("dd/MM/yyy, ").format(date);
        Calendar fecha = Calendar.getInstance();
        fecha.setTime(new Date(date.getTime()));
        Calendar fechaaux = Calendar.getInstance();

        if (fecha.get(Calendar.DAY_OF_YEAR) == fechaaux.get(Calendar.DAY_OF_YEAR) && fecha.get(Calendar.YEAR) == fechaaux.get(Calendar.YEAR)) {//es hoy
            daystring = _context.getString(R.string.hoy) + ", ";
            return daystring + new SimpleDateFormat("HH:mm").format(date);
        }

        fechaaux.add(Calendar.DAY_OF_YEAR, -1);
        if (fecha.get(Calendar.DAY_OF_YEAR) == fechaaux.get(Calendar.DAY_OF_YEAR) && fecha.get(Calendar.YEAR) == fechaaux.get(Calendar.YEAR)) {//es ayer
            daystring = _context.getString(R.string.ayer) + ", ";
            return daystring + new SimpleDateFormat("HH:mm").format(date);
        }

        if (fecha.get(Calendar.YEAR) == fechaaux.get(Calendar.YEAR)) {//es mismo año
            daystring = new SimpleDateFormat("dd MMM, ").format(date);
            return daystring + new SimpleDateFormat("HH:mm").format(date);
        }

        return daystring + new SimpleDateFormat("HH:mm").format(date);
    }

}
