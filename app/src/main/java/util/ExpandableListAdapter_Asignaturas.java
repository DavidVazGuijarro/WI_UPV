package util;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import upv.welcomeincoming.com.R;

public class ExpandableListAdapter_Asignaturas extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;
    private String[] _nombres;

    public ExpandableListAdapter_Asignaturas(Context context, List<String> listDataHeader,
                                             HashMap<String, List<String>> listChildData, String[] nombres) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this._nombres = nombres;
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

        // final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_asignatura, null);
        }

        TextView tv_nombre = (TextView) convertView.findViewById(R.id.tv_nombre);
        TextView tv_descripcion = (TextView) convertView.findViewById(R.id.tv_description);
        ImageButton btn_url = (ImageButton) convertView.findViewById(R.id.im_url);

        tv_nombre.setText(_nombres[childPosition]);
        tv_descripcion.setText((String) getChild(groupPosition, childPosition));
        btn_url.setVisibility(View.GONE);
        switch (childPosition) {
            default:
                break;
            case 0:
                tv_descripcion.setAutoLinkMask(Linkify.WEB_URLS);
                tv_descripcion.setMovementMethod(new ScrollingMovementMethod());
                break;
            case 1:
                tv_descripcion.setAutoLinkMask(Linkify.WEB_URLS);
                tv_descripcion.setMovementMethod(new ScrollingMovementMethod());
                break;
            case 2:
                tv_descripcion.setAutoLinkMask(Linkify.EMAIL_ADDRESSES);
                tv_descripcion.setMovementMethod(new ScrollingMovementMethod());
                break;
            case 3:
                tv_descripcion.setAutoLinkMask(Linkify.PHONE_NUMBERS);
                tv_descripcion.setMovementMethod(new ScrollingMovementMethod());
                break;
            case 5:
                tv_descripcion.setAutoLinkMask(Linkify.EMAIL_ADDRESSES);
                tv_descripcion.setMovementMethod(new ScrollingMovementMethod());
                break;
            case 6:
                tv_descripcion.setAutoLinkMask(Linkify.PHONE_NUMBERS);
                tv_descripcion.setMovementMethod(new ScrollingMovementMethod());
                break;
            case 8:
                tv_descripcion.setAutoLinkMask(Linkify.EMAIL_ADDRESSES);
                tv_descripcion.setMovementMethod(new ScrollingMovementMethod());
                break;
            case 9:
                tv_descripcion.setAutoLinkMask(Linkify.PHONE_NUMBERS);
                tv_descripcion.setMovementMethod(new ScrollingMovementMethod());
                break;

        }

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
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_lista_escuelas_padre, null);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
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