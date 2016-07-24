package upv.welcomeincoming.com;

import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.util.CompatibilityHints;

import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import calendarupv.CalendarICAL;
import calendarupv.Calendario;
import calendarupv.ComparadorEventos;
import calendarupv.Evento;
import intranet.IntranetConnection;
import intranet.OutPutParamsIntranetConnection;
import util.DBHandler_Horarios;
import util.InternetConnectionChecker;
import util.Preferencias;
import util.ProgressDialog_Custom;

public class Fragment_Calendar extends ListFragment implements Observer {

    public interface CalendarListener {
        public void CalendarListenerError(String string);
    }

    private IntranetConnection intranetConnection;
    private InternetConnectionChecker icc;
    private ProgressDialog_Custom progressDialog;
    private Calendario calendario;
    private List<Evento> eventos;
    private TextView textViewName;
    private TextView textViewGroup;
    private TextView textViewInfo;
    private SQLiteDatabase db;
    private ArrayAdapterCalendarDiaryItemList adapter;
    private HashMap<Evento, Integer> eventosConAlerta = new HashMap<Evento, Integer>();

    public Fragment_Calendar() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            String string = data.getStringExtra("RESULT_STRING");
            this.adapter = new ArrayAdapterCalendarDiaryItemList(this.getActivity(), obtenerEventosPorAsignatura(string));
            this.setListAdapter(adapter);
            textViewName.setText(getHorario());
        }
        if (resultCode == 2) {
            String string = data.getStringExtra("RESULT_STRING");
            try {
                this.adapter = new ArrayAdapterCalendarDiaryItemList(this.getActivity(), obtenerEventosPorFecha(string));
                this.setListAdapter(adapter);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            textViewName.setText(getHorario());
        }

    }

    private List<Evento> obtenerEventosPorAsignatura(String asignatura) {
        String sql = "SELECT * FROM Evento WHERE nombre=\"" + asignatura + "\";";
        Cursor cursor = db.rawQuery(sql, null);
        List<Evento> eventos = new ArrayList<Evento>();
        while (cursor.moveToNext()) {
            Evento evento = new Evento(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getInt(5), cursor.getString(6));
            eventos.add(evento);
        }
        cursor.close();
        return eventos;
    }

    private List<Evento> obtenerEventosPorFecha(String date) throws ParseException {
        String sql = "SELECT * FROM Evento;";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Cursor cursor = db.rawQuery(sql, null);
        List<Evento> eventos = new ArrayList<Evento>();
        while (cursor.moveToNext()) {
            Evento evento = new Evento(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getInt(5), cursor.getString(6));
            String eDate = evento.getFecha().substring(0, 11).trim();
            if (date.equals(eDate))
                eventos.add(evento);
        }
        cursor.close();
        return eventos;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        icc = new InternetConnectionChecker();
        this.db = new DBHandler_Horarios(getActivity()).getWritableDatabase();
        textViewName = (TextView) this.getView().findViewById(R.id.textViewCalendarName);
        if (!estaVaciaDB()) {
            eventos = obtenerEventosDB(db);
            Collections.sort(eventos, new ComparadorEventos());
            this.adapter = new ArrayAdapterCalendarDiaryItemList(this.getActivity(), eventos);
            this.setListAdapter(adapter);
            textViewName.setText(getHorario());
        } else {
            progressDialog = new ProgressDialog_Custom(getActivity(), getString(R.string.downloading));
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            intranetConnection = new IntranetConnection(
                    Preferencias.getDNI(this.getActivity()),
                    Preferencias.getPINCalendar(this.getActivity()),
                    this
            );

            progressDialog.setMessage(getString(R.string.connecting));
            intranetConnection.connect();
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_filtro, menu);
    }

    @Override

    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {

            case R.id.actualizar_action:
                progressDialog = new ProgressDialog_Custom(getActivity(), getString(R.string.loading));
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                borrarEventosDB();
                //internet
                if (icc.checkInternetConnection(getActivity())) {
                    intranetConnection = new IntranetConnection(
                            Preferencias.getDNI(this.getActivity()),
                            Preferencias.getPIN(this.getActivity()),
                            this
                    );
                    progressDialog.setMessage(getString(R.string.connecting));
                    intranetConnection.connect();
                } else {
                    Toast.makeText(getActivity(), getString(R.string.errorConexion), Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.menuSortAsignaturas:
                Intent filtro = new Intent(getActivity(), Activity_Filtros.class);
                startActivityForResult(filtro, 1);
                break;
            case R.id.menuSortFecha:
                Intent filtro2 = new Intent(getActivity(), Activity_Filtros_Fecha.class);
                startActivityForResult(filtro2, 2);
                break;
            case R.id.menuSortAlarm:
                List<Evento> eventosListaAlerta = obtenerAlertados();
                this.adapter = new ArrayAdapterCalendarDiaryItemList(this.getActivity(), eventosListaAlerta);
                this.setListAdapter(adapter);
                break;
            case R.id.menuDeleteSo:
                this.adapter = new ArrayAdapterCalendarDiaryItemList(this.getActivity(), eventos);
                this.setListAdapter(adapter);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return false;
    }

    private void borrarEventosDB() {
        db.execSQL("DELETE FROM Evento");
    }

    private List<Calendario> obtenerCalendarios() {
        String sql = "SELECT * FROM Horario;";
        Cursor cursor = db.rawQuery(sql, null);
        List<Calendario> calendarios = new ArrayList<Calendario>();
        while (cursor.moveToNext()) {
            Calendario calendario = new Calendario(cursor.getString(cursor.getColumnIndex("id")), cursor.getString(cursor.getColumnIndex("Nombre")), cursor.getString(cursor.getColumnIndex("Url")));
            calendarios.add(calendario);
        }
        cursor.close();
        return calendarios;
    }

    private List<Evento> obtenerAlertados() {
        String sql = "SELECT * FROM Evento WHERE Alertado=1;";
        Cursor cursor = db.rawQuery(sql, null);
        List<Evento> eventos = new ArrayList<Evento>();
        while (cursor.moveToNext()) {
            Evento evento = new Evento(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getInt(5), cursor.getString(6));
            eventos.add(evento);
        }
        cursor.close();
        return eventos;
    }

    public List<Evento> obtenerEventosDB(SQLiteDatabase db) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        String fecha = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss").format(c.getTime());
        String sql = "SELECT * FROM Evento WHERE fecha>=\"" + fecha + "\";";
        Cursor cursor = db.rawQuery(sql, null);
        List<Evento> eventos = new ArrayList<Evento>();
        while (cursor.moveToNext()) {
            Evento evento = new Evento(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getInt(5), cursor.getString(6));
            eventos.add(evento);
        }
        cursor.close();
        return eventos;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calendar_show_calendar, container, false);
    }

    @Override
    public void update(Observable observable, Object data) {

        OutPutParamsIntranetConnection outPutParamsIntranetConnection = (OutPutParamsIntranetConnection) observable;

        //error?
        if (outPutParamsIntranetConnection.getException() != null) {


            if (outPutParamsIntranetConnection.isUserFail()) {
                Preferencias.setDNI(this.getActivity(), "");
                Preferencias.setPIN(this.getActivity(), "");
            }

            CalendarListener activity = (CalendarListener) getActivity();
            activity.CalendarListenerError(outPutParamsIntranetConnection.getException().getMessage());

            return;
        }
        if (data.equals("login")) {

            progressDialog.setMessage(getString(R.string.downloading));

            intranetConnection.getCalendars();

        } else if (data.equals("calendars")) {


            List<Calendario> diaryJSONList = outPutParamsIntranetConnection.getCalendars().getCalendarios();
            Iterator<Calendario> itCal = diaryJSONList.iterator();
            Calendario calendario = null;
            while (itCal.hasNext()) {
                calendario = itCal.next();
                calendario.insertarCalendariosDB(db);
                this.calendario = calendario;
            }

            intranetConnection.getICS(this.calendario.getUrl());
        } else if (data.equals("ics")) {
            try {
                actualizarEventos(outPutParamsIntranetConnection);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParserException e) {
                e.printStackTrace();
            }
            progressDialog.dismiss();
        }

    }

    private void actualizarEventos(OutPutParamsIntranetConnection outPutParamsIntranetConnection) throws IOException, ParserException {
        //parsear ical
        CompatibilityHints.setHintEnabled(CompatibilityHints.KEY_RELAXED_PARSING, true);
        StringReader sin = new StringReader(outPutParamsIntranetConnection.getIcsString());
        CalendarBuilder builder = new CalendarBuilder();
        Calendar calendar = builder.build(sin);
        CalendarICAL calendarICAL = new CalendarICAL(calendar, this.calendario.getUid());

        textViewName.setText(getHorario());

        if (calendarICAL.getEvents().size() > 0) {
            calendarICAL.insertarCalendariosDB(db);
            List<Evento> events = calendarICAL.getEvents();
            this.eventos = calendario.obtenerEventosDB(db);
            this.adapter = new ArrayAdapterCalendarDiaryItemList(this.getActivity(), eventos);
            this.setListAdapter(adapter);

        }
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    public boolean estaVaciaDB() {
        String sql = "SELECT * FROM Evento;";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    public int getUniqueID(Evento evento) {
        String sql = "SELECT * FROM Evento WHERE fecha='" + evento.getFecha() + "';";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            cursor.close();
            return id;
        }
        cursor.close();
        return -1;
    }

    public String getHorario() {
        String sql = "SELECT * FROM Horario;";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            String nombre = cursor.getString(cursor.getColumnIndex("Nombre"));
            cursor.close();
            return nombre;
        }
        cursor.close();
        return "";
    }

    public void borrarUniqueID(Evento evento) {
        db.execSQL("UPDATE Evento SET Alertado=0 WHERE fecha='" + evento.getFecha() + "';");
    }

    public void setUniqueID(Evento evento) {
        db.execSQL("UPDATE Evento SET Alertado=1 WHERE fecha='" + evento.getFecha() + "';");
    }

    private class ArrayAdapterCalendarDiaryItemList extends BaseAdapter {

        private HashMap<Evento, Integer> hashMap = new HashMap<Evento, Integer>();
        private final boolean[] mCheckedState;
        private Context context;
        private List<Evento> values;
        private int visibleValues = 20;

        public ArrayAdapterCalendarDiaryItemList(Context context, List<Evento> values) {
            if (values != null)
                mCheckedState = new boolean[values.size()];
            else
                mCheckedState = null;
            this.context = context;
            this.values = values;
        }

        public List<Evento> getValues() {
            return values;
        }

        public void setValues(List<Evento> values) {
            this.values = values;
        }

        public Context getContext() {
            return context;
        }

        public void setContext(Context context) {
            this.context = context;
        }

        public int getVisibleValues() {
            return visibleValues;
        }

        public void setVisibleValues(int visibleValues) {
            this.visibleValues = visibleValues;
        }

        private final SimpleDateFormat simpleDateFormatOriginal = new SimpleDateFormat("yyyyMMdd'T'HHmmss", Locale.getDefault());

        @Override
        public Object getItem(int pos) {
            return values.get(pos);
        }

        @Override
        public long getItemId(int pos) {
            return pos;
        }

        @Override
        public int getCount() {
            if (values == null)
                return 0;
            else return values.size();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View rowView = inflater.inflate(R.layout.fragment_calendar_show_calendar_item, parent, false);
            final Evento actual = values.get(position);
            LinearLayout layEdif = (LinearLayout) rowView.findViewById(R.id.layoutEdificio);
            LinearLayout layEdifL = (LinearLayout) rowView.findViewById(R.id.layoutEdificioL);
            LinearLayout layEdifE = (LinearLayout) rowView.findViewById(R.id.layoutExtEdifi);
            TextView firstLine = (TextView) rowView.findViewById(R.id.calendarFirstLine);
            TextView secondLineLeft = (TextView) rowView.findViewById(R.id.calendarSecondLineLeft);
            TextView thirdLineRight = (TextView) rowView.findViewById(R.id.calendarThirdLine);
            final Switch alarm = (Switch) rowView.findViewById(R.id.switch1);
            alarm.setChecked(actual.getAlertado() == 1);
            alarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    Intent intent = new Intent("alarmReceiver");
                    if (b) {
                        actual.setAlertado(1);
                        eventos.set(position, actual);
                        mCheckedState[position] = true;
                        setUniqueID(actual);
                        int id = getUniqueID(actual);
                        intent.putExtra("nombre", actual.getNombre());
                        intent.putExtra("edificio", actual.getUbicacion());
                        intent.putExtra("hora", actual.getFecha());
                        intent.putExtra("id", id);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), id, intent, 0);
                        Date date = null;
                        try {
                            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(actual.getFecha());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        long when = date.getTime();
                        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                        alarmManager.set(AlarmManager.RTC, when, pendingIntent);
                    }
                    if (!b) {
                        int id = getUniqueID(actual);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), id, intent, 0);
                        pendingIntent.cancel();
                        actual.setAlertado(0);
                        eventos.set(position, actual);
                        mCheckedState[position] = false;
                        eventosConAlerta.remove(actual);
                        borrarUniqueID(actual);
                    }
                }
            });
            if (values.get(position).getNombre() != null) {
                firstLine.setText(values.get(position).getNombre());
            }

            if (values.get(position).getUbicacion() != null)
                secondLineLeft.setText(values.get(position).getUbicacion());

            if (values.get(position).getFecha() != null)
                thirdLineRight.setText(values.get(position).getFecha());
            String edificio = values.get(position).getEdificio();
            if (edificio != null
                    && edificio.equals("1B") || edificio.equals("1C") || edificio.equals("1G")
                    || edificio.equals("1H") || edificio.equals("2F")
                    || edificio.equals("3H") || edificio.equals("3M")
                    || edificio.equals("3P") || edificio.equals("4D")
                    || edificio.equals("4H") || edificio.equals("5F")
                    || edificio.equals("7B") || edificio.equals("7I")
                    || edificio.equals("7J")) {
                final int positionV = position;
                Button fourthLineRight = new Button(getActivity());
                fourthLineRight.setText(values.get(position).getEdificio());
                fourthLineRight.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                fourthLineRight.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                fourthLineRight.setPadding(10, 0, 10, 0);
                fourthLineRight.setBackgroundResource(R.drawable.selector_button_back);
                fourthLineRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), Acvitity_VistaRealidadAumentada.class);
                        String edificio = values.get(positionV).getEdificio();
                        intent.putExtra("poi", edificio);
                        startActivity(intent);
                    }
                });
                layEdif.addView(fourthLineRight);
            } else layEdifE.removeView(layEdifL);

            return rowView;
        }
    }
}