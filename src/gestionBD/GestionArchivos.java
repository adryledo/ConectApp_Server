/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gestionBD;

import clases.Archivo;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import utilidadesBD.ConexionBD;

/**
 *
 * @author ADRIANLC
 */
public class GestionArchivos
{
    public static int insertarArchivo(Archivo a) {
        try
        {
            String consulta = "insert into archivo (aliasUsuario, aliasContacto, fecha, nombre) VALUES (?,?,?,?)";
            PreparedStatement stmt = ConexionBD.getConexion().prepareStatement(consulta);
            stmt.setString(1, a.getAliasUsuario());
            stmt.setString(2, a.getAliasContacto());
            stmt.setString(3, a.getFecha());
            stmt.setString(4, a.getNombre());
            
            System.out.println(consulta);
            stmt.executeUpdate();
            return 0;
        }
        catch(SQLException e)
        {
            return -1;
        }
    }
}
