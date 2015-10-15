/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import java.io.Serializable;

/**
 *
 * @author ADRIANLC
 */
public class Grupo implements Serializable
{
    private String admin;
    private String nombre;

    public Grupo(String nombre)
    {
        this.nombre = nombre;
    }
    
    public Grupo(String admin, String nombre)
    {
        this.admin = admin;
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    @Override
    public String toString() {
        return nombre;
    }
}