package util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import upv.welcomeincoming.com.R;

public class MultiExpandableListAdapter_Escuelas extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<asignatura>> _listDataChild;
    private String[] nombres = {"Nombre", "Codigo", "URL", "Creditos", "Semestre"};

    public MultiExpandableListAdapter_Escuelas(Context context, List<String> listDataHeader,
                                               HashMap<String, List<asignatura>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {


        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_asignatura, null);
        }

        TextView tv_nombre = (TextView) convertView.findViewById(R.id.tv_nombre);
        TextView tv_desc = (TextView) convertView.findViewById(R.id.tv_description);
        ImageButton btn_url = (ImageButton) convertView.findViewById(R.id.im_url);
        asignatura asig = (asignatura) getChild(groupPosition, childPosition);
        String nCodigo = "Code";
        String idioma = "i";
        Locale locale1 = _context.getResources().getConfiguration().locale;
        if (locale1.getCountry().equals("ES")) idioma = "c";
        final String url = "http://www.upv.es/pls/oalu/sic_asi.Busca_Asi?P_VISTA=ANDP_IDIOMA%3DiANDp_codi%3D" + asig.getCodigo() + "ANDp_caca%3Dact&P_IDIOMA=" + idioma + "&p_codi=" + asig.getCodigo() + "&p_caca=act";

        String nSemestre = "Semestre";
        String nCredits = "Credits";
        String codigo = asig.getCodigo();
        String semestre = asig.getSemestre();
        String creditos = asig.getCreditos();

        String separator = "<br></br>"; //<br></br> para nueva linea
        String descripcion = "<font color=#444444>" + nCodigo + ": </font>" + "<font color=#808080>" + codigo + "</font>" + separator
                + "<font color=#444444>" + nSemestre + ": </font>" + "<font color=#808080>" + semestre + "</font>" + separator
                + "<font color=#444444>" + nCredits + ": </font>" + "<font color=#808080>" + creditos + "</font>";

        tv_desc.setText(Html.fromHtml(descripcion));
        tv_nombre.setText(asig.getNombre());
        btn_url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                _context.startActivity(i);
            }
        });

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_lista_escuelas_padre, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}