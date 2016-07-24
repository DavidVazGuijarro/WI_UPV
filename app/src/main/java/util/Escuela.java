package util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joaquin on 26/04/14.
 */
public class Escuela {

    String escuelanombre;
    String urlprincipal;
    String urldocencia;
    String escuelamail;
    String escuelafax;
    String coordinadornombre;
    String coordinadormail;
    String coordinadortelefono;
    String coordinadorextension;
    String tecniconombre;
    String tecnicomail;
    String tecnicotelefono;
    String tecnicoextension;
    List asignaturas = new ArrayList<asignatura>();

    public Escuela() {

    }

    public String getEscuelanombre() {
        return escuelanombre;
    }

    public void setEscuelanombre(String escuelanombre) {
        this.escuelanombre = escuelanombre;
    }

    public String getUrlprincipal() {
        return urlprincipal;
    }

    public void setUrlprincipal(String urlprincipal) {
        this.urlprincipal = urlprincipal;
    }

    public String getUrldocencia() {
        return urldocencia;
    }

    public void setUrldocencia(String urldocencia) {
        this.urldocencia = urldocencia;
    }

    public String getEscuelamail() {
        return escuelamail;
    }

    public void setEscuelamail(String escuelamail) {
        this.escuelamail = escuelamail;
    }

    public String getEscuelafax() {
        return escuelafax;
    }

    public void setEscuelafax(String escuelafax) {
        this.escuelafax = escuelafax;
    }

    public String getCoordinadornombre() {
        return coordinadornombre;
    }

    public void setCoordinadornombre(String coordinadornombre) {
        this.coordinadornombre = coordinadornombre;
    }

    public String getCoordinadormail() {
        return coordinadormail;
    }

    public void setCoordinadormail(String coordinadormail) {
        this.coordinadormail = coordinadormail;
    }

    public String getCoordinadortelefono() {
        return coordinadortelefono;
    }

    public void setCoordinadortelefono(String coordinadortelefono) {
        this.coordinadortelefono = coordinadortelefono;
    }

    public String getCoordinadorextension() {
        return coordinadorextension;
    }

    public void setCoordinadorextension(String coordinadorextension) {
        this.coordinadorextension = coordinadorextension;
    }

    public String getTecniconombre() {
        return tecniconombre;
    }

    public void setTecniconombre(String tecniconombre) {
        this.tecniconombre = tecniconombre;
    }

    public String getTecnicomail() {
        return tecnicomail;
    }

    public void setTecnicomail(String tecnicomail) {
        this.tecnicomail = tecnicomail;
    }

    public String getTecnicotelefono() {
        return tecnicotelefono;
    }

    public void setTecnicotelefono(String tecnicotelefono) {
        this.tecnicotelefono = tecnicotelefono;
    }

    public String getTecnicoextension() {
        return tecnicoextension;
    }

    public void setTecnicoextension(String tecnicoextension) {
        this.tecnicoextension = tecnicoextension;
    }

    public List getAsignaturas() {
        return asignaturas;
    }

    public void setAsignaturas(List asignaturas) {
        this.asignaturas = asignaturas;
    }
}
