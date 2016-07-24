package upv.welcomeincoming.com.foro;

import java.sql.Timestamp;

/**
 * @author Marcos
 */
public class Estadisticas {
    int numTemas;
    int numComentarios;
    Timestamp fecha_ingreso;

    public Estadisticas(int numTemas, int numComentarios, Timestamp fecha_ingreso) {
        this.numTemas = numTemas;
        this.numComentarios = numComentarios;
        this.fecha_ingreso = fecha_ingreso;
    }

    public int getNumTemas() {
        return numTemas;
    }

    public int getNumComentarios() {
        return numComentarios;
    }

    public Timestamp getFecha_ingreso() {
        return fecha_ingreso;
    }

}
