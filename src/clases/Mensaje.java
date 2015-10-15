/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 *
 * @author ADRIANLC
 */
public class Mensaje implements Serializable
{
    private String aliasUsuario, aliasContacto, contenido;
    private Timestamp fecha;

    public Mensaje(String aliasUsuario, String aliasContacto, Timestamp fecha, String contenido)
    {
        this.aliasUsuario = aliasUsuario;
        this.aliasContacto = aliasContacto;
        this.fecha = fecha;
        this.contenido = contenido;
    }

    public String getAliasContacto() {
        return aliasContacto;
    }

    public String getAliasUsuario() {
        return aliasUsuario;
    }

    public String getContenido() {
        return contenido;
    }

    public Timestamp getFecha() {
        return fecha;
    }
}
