package util;

/**
 * Created by joaquin on 27/04/14.
 */
public class transporte {

    private String nombre;
    private String descripcion;
    private String telefono;
    private String url;

    public transporte(String nombre, String descripcion, String telefono, String url) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.telefono = telefono;
        this.url = url;
    }

    public transporte() {

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "transporte{" +
                "nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", telefono=" + telefono +
                ", url='" + url + '\'' +
                '}';
    }
}
