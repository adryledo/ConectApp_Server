/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author ADRIANLC
 */
public class Mensaje implements Serializable
{
    private String aliasUsuario, aliasContacto, contenido, fecha;

    public Mensaje(String aliasUsuario, String aliasContacto, String fecha, String contenido)
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

    public String getFecha() {
        return fecha;
    }
}
