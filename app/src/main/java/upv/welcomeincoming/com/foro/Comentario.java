package upv.welcomeincoming.com.foro;

import java.sql.Timestamp;

/**
 * Created by Marcos on 21/07/14.
 */
public class Comentario {
    int idcomentario;
    int idtema;
    String idUsuario;
    Timestamp fecha;
    String language;
    String data;
    String nombreUsuario;
    String apellidoUsuario;

    public Comentario(int idcomentario, int idtema, String idUsuario, Timestamp fecha, String language, String data, String nombreUsuario, String apellidoUsuario) {
        this.idcomentario = idcomentario;
        this.idtema = idtema;
        this.idUsuario = idUsuario;
        this.fecha = fecha;
        this.language = language;
        this.data = data;
        this.nombreUsuario = nombreUsuario;
        this.apellidoUsuario = apellidoUsuario;
    }

    public int getIdcomentario() {
        return idcomentario;
    }

    public int getIdtema() {
        return idtema;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public String getLanguage() {
        return language;
    }

    public String getData() {
        return data;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getApellidoUsuario() {
        return apellidoUsuario;
    }


}

