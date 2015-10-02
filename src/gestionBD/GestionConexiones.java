/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gestionBD;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ADRIANLC
 */
public class GestionConexiones
{
    public static int insertarConexion(clases.Conexion c)
    {
        if(isConectado(c.getAliasUsuario()))
        {
            return -2;
        }
        
        try
        {
            String consulta = "insert into conexion (aliasUsuario, ip) VALUES (?,?)";
            PreparedStatement stmt = utilidadesBD.Conexion.getConexion().prepareStatement(consulta);
            stmt.setString(1, c.getAliasUsuario());
            stmt.setString(2, c.getIp());
            
            System.out.println(consulta);
            stmt.executeUpdate();
            return 0;
        }
        catch(SQLException e)
        {
            return -1;
        }
    }
    
    public static int eliminarConexion(clases.Conexion c)
    {
        try {
            Statement stmt = utilidadesBD.Conexion.getConexion().createStatement();
            String sql = "delete from conexion where aliasUsuario like '"+c.getAliasUsuario()+"'";
            return stmt.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(GestionConexiones.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public static ArrayList<String> getIPs(String aliasUsuario) {
        ArrayList<String> ips = null;
        if(!isConectado(aliasUsuario))
        {
            return null;
        }
        
        try {
            ips = new ArrayList<String>();
            Statement stmt = utilidadesBD.Conexion.getConexion().createStatement();
            String consulta = "select ip from conexion where aliasUsuario like '"+aliasUsuario+"'";
            ResultSet rs = stmt.executeQuery(consulta);
            while(rs.next())
            {
                ips.add(rs.getString(0));
            }
        } catch (SQLException ex) {
            Logger.getLogger(GestionConexiones.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ips;
    }

    public static boolean isConectado(String aliasUsuario) {
        try {
            Statement stmt = utilidadesBD.Conexion.getConexion().createStatement();
            String consulta = "select * from conexion where aliasUsuario like '"+aliasUsuario+"'";
            ResultSet rs = stmt.executeQuery(consulta);
            return rs.next();
        } catch (SQLException ex) {
            Logger.getLogger(GestionConexiones.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static String getAlias(String ip) {
        try {
            Statement stmt = utilidadesBD.Conexion.getConexion().createStatement();
            String consulta = "select aliasUsuario from conexion where ip like '"+ip+"'";
            ResultSet rs = stmt.executeQuery(consulta);
            if(rs.next())
            {
                return rs.getString(1);
            }
            return null;
        } catch (SQLException ex) {
            Logger.getLogger(GestionConexiones.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
