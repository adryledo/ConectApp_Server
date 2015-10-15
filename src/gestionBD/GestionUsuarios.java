/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gestionBD;

import clases.Usuario;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import utilidadesBD.ConexionBD;

/**
 *
 * @author adry
 */
public class GestionUsuarios {
    /**
     * 
     * @param aliasPropietario 
     * @return true | false
     */
    static boolean existeUsuario(String aliasPropietario) {
        try {
            String consulta = "select * from usuario where alias=?";
            PreparedStatement stmt = ConexionBD.getConexion().prepareStatement(consulta);
            stmt.setString(1, aliasPropietario);
            System.out.println(stmt.toString());
            return stmt.executeQuery().next();
        } catch (SQLException ex) {
            Logger.getLogger(GestionUsuarios.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    /**
     * 
     * @param usuario
     * @return 0 si se pudo iniciar <br>
     * 1 en caso contrario
     */
    public static int iniciarSesion(Usuario usuario)
    {
        try {
            String consulta = "update usuario set dispConectados=dispConectados+1 where"
                    + " alias=? and contrasenha=?";
            PreparedStatement stmt = ConexionBD.getConexion().prepareStatement(consulta);
            stmt.setString(1, usuario.getAlias());
            stmt.setString(2, usuario.getContrasenha());
            System.out.println(stmt.toString());
            return stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(GestionUsuarios.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }
    /**
     * 
     * @param usuario
     * @return -2 si existe el usuario<br>
     * -1 si hay error<br>
     * 0 si se pudo registrar
     */
    public static int registrarse(Usuario usuario)
    {
        if(existeUsuario(usuario.getAlias()))
        {
            iniciarSesion(usuario);
            return -2;
        }
        
        try {
            String consulta = "insert into usuario (alias, contrasenha, dispConectados) values (?,?,1)";
            PreparedStatement stmt = ConexionBD.getConexion().prepareStatement(consulta);
            stmt.setString(1, usuario.getAlias());
            stmt.setString(2, usuario.getContrasenha());
            System.out.println(stmt.toString());
            stmt.executeUpdate();
            return 0;
        } catch (SQLException ex) {
            Logger.getLogger(GestionUsuarios.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }

    public static int cerrarSesion(Usuario user) {
        try {
            String consulta = "update usuario set dispConectados=dispConectados-1 where"
                    + " alias=?";// and contrasenha=?
            PreparedStatement stmt = ConexionBD.getConexion().prepareStatement(consulta);
            stmt.setString(1, user.getAlias());
        //    stmt.setString(2, user.getContrasenha());
            System.out.println(stmt.toString());
            return stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(GestionUsuarios.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }
}
