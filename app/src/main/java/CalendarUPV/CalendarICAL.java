package calendarupv;


import android.database.sqlite.SQLiteDatabase;

import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class CalendarICAL {

    private long id;
    private String uid;
    private String name;
    private String timeZone;
    private String comment;
    private List<Evento> events;

    public CalendarICAL(net.fortuna.ical4j.model.Calendar calendar, String uid) {
        this.uid = uid;
        this.parseCalendar(calendar);
    }

    public CalendarICAL(String uid) {
        this.uid = uid;
    }

    private void parseCalendar(Calendar calendar) {

        //properties...
        this.name = calendar.getProperty("X-WR-CALNAME").getValue();
        this.timeZone = calendar.getProperty("X-WR-TIMEZONE").getValue();
        this.comment = calendar.getProperty("COMMENT").getValue();

        //events....
        events = this.getEventsAfterToday(calendar.getComponents());

    }

    private List<Evento> getEventsAfterToday(List<Component> componentList) {

        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        String today = simpleDateFormat.format(calendar.getTime());

        List<Evento> returnEventList = new ArrayList<Evento>();

        Iterator<Component> iterator = componentList.iterator();
        EventICAL eventICAL = null;
        Evento evento = null;

        while (iterator.hasNext()) {

            eventICAL = new EventICAL(iterator.next());
            String descripcion = eventICAL.getDescription();
            String nombreAsig = descripcion.substring(0, descripcion.indexOf(")") + 1);
            descripcion = descripcion.substring(descripcion.indexOf(":") + 1);
            String profesor = descripcion.substring(descripcion.indexOf(":") + 1, descripcion.indexOf("Grupo"));
            evento = new Evento(nombreAsig, profesor, eventICAL.getLocation(), eventICAL.getDtstartFormat(), 0, eventICAL.getBuilding());
            if (eventICAL.getDtstartOriginal().compareTo(today) > 0) {
                returnEventList.add(evento);
            }
        }
        Collections.sort(returnEventList, new ComparadorEventos());
        return returnEventList;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<Evento> getEvents() {
        return events;
    }

    public void setEvents(List<Evento> events) {
        this.events = events;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void insertarCalendariosDB(SQLiteDatabase db) {
        Iterator<Evento> itCal = getEvents().iterator();
        Evento evento = null;
        while (itCal.hasNext()) {
            evento = itCal.next();
            String sqlEvento = "INSERT OR IGNORE INTO \"main\".\"Evento\" (\"nombre\",\"profesor\",\"ubicacion\",\"fecha\",\"alertado\",\"idHorario\") VALUES (\"" + evento.getNombre() + "\",\"" + evento.getProfesor() + "\",\"" + evento.getUbicacion() + "\",\"" + evento.getFecha() + "\"," + 0 + ",\"" + uid + "\")";
            db.execSQL(sqlEvento);
        }
    }

    @Override
    public String toString() {

        String string = "";
        for (Evento item : this.events)
            string += item.toString();

        return "ICal{" +
                "id='" + id + '\'' +
                "uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", timeZone=" + timeZone +
                ", comment='" + comment + '\'' +
                ", events=" + string +
                '}';
    }
}
