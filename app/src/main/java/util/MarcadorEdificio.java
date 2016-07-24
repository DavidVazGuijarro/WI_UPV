package util;


/**
 * Created by Cristian on 18/07/2014.
 */
public class MarcadorEdificio {
    private String numero;
    private String longitud;
    private String latitud;
    private String informacion;

    public MarcadorEdificio(String numero, String longitud, String latitud, String informacion) {
        this.numero = numero;
        this.longitud = longitud;
        this.latitud = latitud;
        this.informacion = informacion;
    }

    public MarcadorEdificio() {
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getInformacion() {
        return informacion;
    }

    public void setInformacion(String informacion) {
        this.informacion = informacion;
    }

    @Override
    public String toString() {
        return "MarcadorEdificio{" +
                "numero='" + numero + '\'' +
                ", longitud='" + longitud + '\'' +
                ", latitud='" + latitud + '\'' +
                ", informacion='" + informacion + '\'' +
                '}';
    }
}
