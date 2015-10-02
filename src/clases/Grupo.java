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
    private int id;
    private String nombre;
    private String aliasPropietario;

    public Grupo(String nombre)
    {
        this.nombre = nombre;
    }
    
    public Grupo(int id, String nombre)
    {
        this.id = id;
        this.nombre = nombre;
    }
    
    public Grupo(int id, String nombre, String aliasPropietario) {
        this.id = id;
        this.aliasPropietario = aliasPropietario;
        this.nombre = nombre;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAliasPropietario() {
        return aliasPropietario;
    }

    public void setAliasPropietario(String aliasPropietario) {
        this.aliasPropietario = aliasPropietario;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
