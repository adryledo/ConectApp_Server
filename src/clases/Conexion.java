/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

/**
 *
 * @author ADRIANLC
 */
public class Conexion
{
    private String aliasUsuario, ip;

    public Conexion(String aliasUsuario, String ip) {
        this.aliasUsuario = aliasUsuario;
        this.ip = ip;
    }

    public String getAliasUsuario() {
        return aliasUsuario;
    }

    public String getIp() {
        return ip;
    }
}
