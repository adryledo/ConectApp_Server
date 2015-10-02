/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import java.util.Date;

/**
 *
 * @author ADRIANLC
 */
public class Archivo
{
    private String aliasUsuario, aliasContacto, nombre, fecha;

    public Archivo(String aliasUsuario, String aliasContacto, String nombre, String fecha) {
        this.aliasUsuario = aliasUsuario;
        this.aliasContacto = aliasContacto;
        this.nombre = nombre;
        this.fecha = fecha;
    }

    public String getAliasContacto() {
        return aliasContacto;
    }

    public String getAliasUsuario() {
        return aliasUsuario;
    }

    public String getFecha() {
        return fecha;
    }

    public String getNombre() {
        return nombre;
    }
}
