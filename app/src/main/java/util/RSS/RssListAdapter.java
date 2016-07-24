package util.RSS;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import upv.welcomeincoming.com.R;

public class RssListAdapter extends ArrayAdapter<Noticia> {
    private List<Noticia> noticias;

    public RssListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        Context _context = context;
    }

    public RssListAdapter(Context context, int resource, List<Noticia> items) {
        super(context, resource, items);
        noticias = items;
    }

    @Override
    public int getCount() {
        return noticias.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {

            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.item_lista_noticias, null);

        }

        Noticia p = getItem(position);

        if (p != null) {
            TextView title = (TextView) v.findViewById(R.id.textTitle);

            if (title != null) {
                title.setText(p.getTitle());
                // title.setTypeface(Typeface.createFromAsset(_context.getAssets(), "fonts/futura_font.ttf"));
            }
        }

        return v;

    }

}