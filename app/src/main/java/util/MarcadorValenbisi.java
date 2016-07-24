package util;

/**
 * Created by Cristian on 18/07/2014.
 */
public class MarcadorValenbisi {
    private String direccion;
    private int numero;
    private String longitud;
    private String latitud;
    private int numeroPlazas;
    private int plazasDisponibles;

    public MarcadorValenbisi(int numero, String direccion, String longitud, String latitud, int numeroPlazas, int plazasDisponibles) {
        this.numero = numero;
        this.longitud = longitud;
        this.direccion = direccion;
        this.numeroPlazas = numeroPlazas;
        this.latitud = latitud;
        this.plazasDisponibles = plazasDisponibles;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getPlazasDisponibles() {
        return plazasDisponibles;
    }

    public void setPlazasDisponibles(int plazasDisponibles) {
        this.plazasDisponibles = plazasDisponibles;
    }

    public int getNumeroPlazas() {
        return numeroPlazas;
    }

    public void setNumeroPlazas(int numeroPlazas) {
        this.numeroPlazas = numeroPlazas;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    @Override
    public String toString() {
        return "MarcadorValenbisi{" +
                "direccion='" + direccion + '\'' +
                ", numero=" + numero +
                ", longitud='" + longitud + '\'' +
                ", latitud='" + latitud + '\'' +
                ", numeroPlazas=" + numeroPlazas +
                ", plazasDisponibles=" + plazasDisponibles +
                '}';
    }
}
