package calendarupv;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Cristian on 15/07/2014.
 */
public class Evento implements Comparable {
    private String nombre;
    private String profesor;
    private String ubicacion;
    private String fecha;
    private String edificio;
    private int alertado;

    public Evento(String nombre, String profesor, String ubicacion, String fecha, int alertado, String edificio) {
        this.nombre = nombre;
        this.profesor = profesor;
        this.ubicacion = ubicacion;
        this.fecha = fecha;
        this.alertado = alertado;
        this.edificio = edificio;
    }

    public String getEdificio() {
        return edificio;
    }

    public void setEdificio(String edificio) {
        this.edificio = edificio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getProfesor() {
        return profesor;
    }

    public void setProfesor(String profesor) {
        this.profesor = profesor;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getAlertado() {
        return alertado;
    }

    public void setAlertado(int alertado) {
        this.alertado = alertado;
    }


    @Override
    public String toString() {
        return "Evento{" +
                "nombre='" + nombre + '\'' +
                ", profesor='" + profesor + '\'' +
                ", ubicacion='" + ubicacion + '\'' +
                ", fecha='" + fecha + '\'' +
                ", edificio='" + edificio + '\'' +
                ", alertado=" + alertado +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        Date tfecha = null;
        Date ofecha = null;
        Evento evento = (Evento) o;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {

            tfecha = simpleDateFormat.parse(this.fecha);
            ofecha = simpleDateFormat.parse(((Evento) o).getFecha());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (tfecha != null || ofecha != null) {
            return tfecha.compareTo(ofecha);
        }
        return 0;
    }


}
