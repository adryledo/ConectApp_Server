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
    private String aliasContacto, nombre, telefono, direccion, email;
    private int idGrupo;

    public Contacto() {
    }

    public Contacto(int idGrupo, String aliasContacto, String nombre, String telefono, String direccion, String email) {
        this.aliasContacto = aliasContacto;
        this.nombre = nombre;
        this.telefono = telefono;
        this.direccion = direccion;
        this.email = email;
        this.idGrupo = idGrupo;
    }

    public void setAliasContacto(String aliasContacto) {
        this.aliasContacto = aliasContacto;
    }
    
    /*public Contacto(int id, String ip, String nombre, String telefono, String direccion, String email, int idGrupo) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.direccion = direccion;
        this.email = email;
        this.idGrupo = idGrupo;
    }*/

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /*public void setId(int id) {
        this.id = id;
    }*/

    public void setIdGrupo(int idGrupo) {
        this.idGrupo = idGrupo;
    }

    /*public void setIp(String ip) {
        this.ip = ip;
    }*/

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /*public int getId() {
        return id;
    }*/

    public String getDireccion() {
        return direccion;
    }

    public String getEmail() {
        return email;
    }

    /*public String getIp() {
        return ip;
    }*/

    public int getIdGrupo() {
        return idGrupo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getAliasContacto() {
        return aliasContacto;
    }
    
    @Override
    public String toString() {
        return nombre;
    }
}
