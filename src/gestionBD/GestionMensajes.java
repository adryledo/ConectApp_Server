/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gestionBD;

import clases.Mensaje;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import utilidadesBD.Conexion;

/**
 *
 * @author ADRIANLC
 */
public class GestionMensajes {

    public static int insertarMensaje(Mensaje m) {
        try
        {
            String consulta = "insert into mensaje "
                    + "(aliasUsuario, aliasContacto, fecha, contenido) VALUES (?,?,?,?)";
            PreparedStatement stmt = Conexion.getConexion().prepareStatement(consulta);
            stmt.setString(1, m.getAliasUsuario());
            stmt.setString(2, m.getAliasContacto());
            stmt.setString(3, m.getFecha());
            stmt.setString(4, m.getContenido());
            
            System.out.println(consulta);
            stmt.executeUpdate();
            return 0;
        }
        catch(SQLException e)
        {
            return -1;
        }
    }
    
    public static ArrayList<String> mostrarMensajes(String aliasUsuario1, String aliasUsuario2)
    {
        ArrayList<String> listaMensajes = new ArrayList<String>();
        try {
            Statement stmt = Conexion.getConexion().createStatement();
            String consulta = "select contenido from mensaje where (aliasUsuario like '"+aliasUsuario1+"' AND aliasContacto like '"+aliasUsuario2+"') "
                    + "OR (aliasUsuario like '"+aliasUsuario2+"' AND aliasContacto like '"+aliasUsuario1+"')";
            ResultSet rs = stmt.executeQuery(consulta);
            while(rs.next())
            {
                listaMensajes.add(rs.getString(1));
            }
            return listaMensajes;
        } catch (SQLException ex) {
            Logger.getLogger(GestionMensajes.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
