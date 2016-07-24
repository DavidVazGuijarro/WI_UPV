package upv.welcomeincoming.com.foro;

/**
 * Created by Marcos on 31/07/14.
 */

import java.sql.Timestamp;

public class Tema {
    int id;
    String titulo;
    String descripcion;
    Timestamp fecha_creacion;
    Timestamp fecha_ultimo_comentario;
    int numcomentarios;
    String language;
    String idUsuario;
    String nombreUsuario;
    String apellidoUsuario;
    boolean notification;

    public Tema(int id, String titulo, String descripcion, Timestamp fecha_creacion, Timestamp fecha_ultimo_comentario, int numcomentarios, String language, String idUsuario, String nombreUsuario, String apellidoUsuario, boolean notification) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha_creacion = fecha_creacion;
        this.fecha_ultimo_comentario = fecha_ultimo_comentario;
        this.numcomentarios = numcomentarios;
        this.language = language;
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.apellidoUsuario = apellidoUsuario;
        this.notification = notification;
    }

    public Tema(int id, String titulo, String descripcion, Timestamp fecha_creacion, String language, String idUsuario, String nombreUsuario, String apellidoUsuario, boolean notification) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha_creacion = fecha_creacion;
        this.language = language;
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.apellidoUsuario = apellidoUsuario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Timestamp getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(Timestamp fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public Timestamp getFecha_ultimo_comentario() {
        return fecha_ultimo_comentario;
    }

    public void setFecha_ultimo_comentario(Timestamp fecha_ultimo_comentario) {
        this.fecha_ultimo_comentario = fecha_ultimo_comentario;
    }

    public int getNumcomentarios() {
        return numcomentarios;
    }

    public void setNumcomentarios(int numcomentarios) {
        this.numcomentarios = numcomentarios;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getApellidoUsuario() {
        return apellidoUsuario;
    }

    public void setApellidoUsuario(String apellidoUsuario) {
        this.apellidoUsuario = apellidoUsuario;
    }

    public boolean notificationOn() {
        return notification;
    }

    public void setNotification(boolean b) {
        this.notification = b;
    }

    public void changeNotificationStatus() {
        notification = !notification;
    }


}