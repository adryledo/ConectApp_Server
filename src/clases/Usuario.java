/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

/**
 *
 * @author ADRIANLC
 */
public class Usuario implements java.io.Serializable
{
    private String alias, contrasenha;

    public Usuario(String alias, String contrasenha) {
        this.alias = alias;
        this.contrasenha = contrasenha;
    }

    public String getAlias() {
        return alias;
    }

    public String getContrasenha() {
        return contrasenha;
    }
}
