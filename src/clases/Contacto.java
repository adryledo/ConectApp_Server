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
public class Contacto implements Serializable
{
    private String creador, alias, nombre, telefono, direccion, email;

    public Contacto() {
    }

    public Contacto(String creador, String alias, String nombre, String telefono, String direccion, String email) {
        this.creador = creador;
        this.alias = alias;
        this.nombre = nombre;
        this.telefono = telefono;
        this.direccion = direccion;
        this.email = email;
    }

    public void setCreador(String creador) {
        this.creador = creador;
    }
    
    public void setAlias(String alias) {
        this.alias = alias;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreador() {
        return creador;
    }

    public String getAlias() {
        return alias;
    }
    
    public String getNombre() {
        return nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
