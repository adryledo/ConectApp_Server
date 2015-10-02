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
import utilidadesBD.Conexion;

/**
 *
 * @author adry
 */
public class GestionUsuarios {

    static boolean existeUsuario(String aliasPropietario) {
        try {
            Statement stmt = Conexion.getConexion().createStatement();
            String consulta = "select * from usuario where alias like '"+aliasPropietario+"'";
            ResultSet rs = stmt.executeQuery(consulta);
            return rs.next();
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
            Statement stmt = Conexion.getConexion().createStatement();
            String consulta = "select * from usuario where alias like '"+usuario.getAlias()
                    +"' AND contrasenha like '"+usuario.getContrasenha()+"'";
            ResultSet rs = stmt.executeQuery(consulta);
            return ((rs.next())?0:1);
        } catch (SQLException ex) {
            Logger.getLogger(GestionUsuarios.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 1;
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
            return -2;
        }
        
        try {
            String consulta = "insert into usuario (alias, contrasenha) values (?,?)";
            PreparedStatement stmt = Conexion.getConexion().prepareStatement(consulta);
            stmt.setString(1, usuario.getAlias());
            stmt.setString(2, usuario.getContrasenha());
            System.out.println(consulta);
            stmt.executeUpdate();
            return 0;
        } catch (SQLException ex) {
            Logger.getLogger(GestionUsuarios.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }
}
