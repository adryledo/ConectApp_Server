/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

/**
 *
 * @author ADRIANLC
 */
public class Grupo
{
    private int id;
    private String nombre, aliasPropietario;

    public Grupo(int id, String aliasPropietario, String nombre) {
        this.id = id;
        this.aliasPropietario = aliasPropietario;
        this.nombre = nombre;
    }

    public String getAliasPropietario() {
        return aliasPropietario;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }
    
    @Override
    public String toString() {
        return nombre;
    }
}
