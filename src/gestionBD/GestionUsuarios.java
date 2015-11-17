/*
 * Copyright (C) 2015 Adrián Ledo
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package gestionBD;

import clases.Usuario;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import utilidadesBD.ConexionBD;

/**
 *
 * @author Adrian Ledo
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
                    + " alias=?";
            PreparedStatement stmt = ConexionBD.getConexion().prepareStatement(consulta);
            stmt.setString(1, user.getAlias());
            
            return stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(GestionUsuarios.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }
}
