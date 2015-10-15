/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gestionBD;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import utilidadesBD.Conexion;

import clases.Contacto;
import clases.Grupo;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author ADRIANLC
 */
public class GestionContactos
{
    /**
     * Guarda el contacto en la tabla contactos<br>
     * @param c El contacto a insertar
     * @return 0 Si se pudo insertar<br>
     *      -1 Si hubo un error<br>
     *      -2 Si el contacto ya existe<br>
     *      -3 Si es el propietario del grupo en el que se quiere insertar<br>
     *      -4 Si el nick del contacto es un usuario que no existe
     */
    public static int insertarContacto(Contacto c)
    {
        //if(existeContacto(c.getCreador(), c.getAlias()))
        if(existeContacto(c))
        {
            return -2;
        }
        /*if(esPropietario(c.getCreador(), c.getAlias()))
        {
            return -3;
        }*/
        if(!GestionUsuarios.existeUsuario(c.getAlias()))
        {
            return -4;
        }
        
        try
        {
            String consulta = "insert into contacto "
                    + "(creador, alias, nombre, telefono, direccion, email) VALUES (?,?,?,?,?,?)";
            
            PreparedStatement stmt = Conexion.getConexion().prepareStatement(consulta);
            stmt.setString(1, c.getCreador());
            stmt.setString(2, c.getAlias());
            stmt.setString(3, c.getNombre());
            stmt.setString(4, c.getTelefono());
            stmt.setString(5, c.getDireccion());
            stmt.setString(6, c.getEmail());
            
            System.out.println(stmt.toString());
            stmt.executeUpdate();
            return 0;
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return -1;
        }
        
    }

    /**
     * Elimina un contacto de la tabla contactos
     * @param c El contacto a eliminar
     * @return 0 si se pudo eliminar
     */
    public static int eliminarContacto(Contacto c)
    {
        try
        {
        /*    Statement stmt=Conexion.getConexion().createStatement();
            String consulta= "delete from contacto where creador='"+c.getCreador()
                    +"' AND alias='"+c.getAlias()+"'";
            System.out.println(consulta);
            stmt.executeUpdate(consulta);            
            return 0;*/
            String consulta= "delete from contacto where creador=? AND alias=?";
            PreparedStatement stmt = Conexion.getConexion().prepareStatement(consulta);
            stmt.setString(1, c.getCreador());
            stmt.setString(2, c.getAlias());
            System.out.println(stmt.toString());
            return (stmt.executeUpdate()==1 ? 0 : -3);
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            if(e.getErrorCode()==1451)
            {
                return -2; //error por integridad referencial
            }
            return -1; //error generico
        }
    }
    
    /**
     * Modifica un contacto
     * @param c Contacto a modificar
     * @return 0 Si se pudo modificar
     */
    public static int modificarContacto(Contacto c)
    {
        try
        {
            String consulta = "update contacto set nombre=?, telefono=?, direccion=?, email=? where creador=? AND alias=?";
            PreparedStatement stmt = Conexion.getConexion().prepareStatement(consulta);
            stmt.setString(1, c.getNombre());
            stmt.setString(2, c.getTelefono());
            stmt.setString(3, c.getDireccion());
            stmt.setString(4, c.getEmail());
            stmt.setString(5, c.getCreador());
            stmt.setString(7, c.getAlias());
            
            System.out.println(stmt.toString());
            return (stmt.executeUpdate()==1 ? 0 : -2);
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return -1; //error generico
        }
        
    }
    
    /**
     * Busca los contactos del usuario dado
     * @param creador Usuario del que se requieren sus contactos
     * @return ArrayList de los contactos que creó creador<br>
     *      <i>null</i> si hubo un error
     */
    public static ArrayList<Contacto> listarContactos(String creador)
    {
        try
        {
            ArrayList<Contacto> resultado = new ArrayList();
        /*    Statement stmt = Conexion.getConexion().createStatement();
            //String consulta= "select * from contacto order by aliasContacto";
            String consulta = "SELECT * FROM contacto where creador like  '"+creador+"')";
            System.out.println(consulta);
            ResultSet rs = stmt.executeQuery(consulta);*/
            String consulta = "SELECT * FROM contacto where creador=?";
            PreparedStatement stmt = Conexion.getConexion().prepareStatement(consulta);
            stmt.setString(1, creador);
            stmt.execute();
            ResultSet rs = stmt.getResultSet();
            while(rs.next())
            {
                Contacto c = new Contacto(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6));
                resultado.add(c);
            }
            System.out.println(stmt.toString());
            return resultado;
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    /*public static ArrayList<Contacto> listarContactosGrupo(int idGrupo)
    {
        try
        {
            ArrayList<Contacto> resultado = new ArrayList();
            Statement stmt = Conexion.getConexion().createStatement();
            String consulta= "select * from contacto where idGrupo like "+idGrupo+"";
            System.out.println(consulta);
            ResultSet rs = stmt.executeQuery(consulta);
            while(rs.next())
            {
                Contacto c = new Contacto(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6));
                resultado.add(c);
            }
            return resultado;
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }*/
    
    /*public static Contacto recuperarContacto(String ip)
    {
        try
        {
            Contacto resultado = null;
            Statement stmt = Conexion.getConexion().createStatement();
            String consulta= "select * from contacto where aliasContacto in (select aliasUsuario from conexion where ip like '"+ip+"') ";
            System.out.println(consulta);
            ResultSet rs = stmt.executeQuery(consulta);
            if(rs.next())
            {
                resultado = new Contacto(rs.getInt(1), rs.getString(2), rs.getString(3));
            }
            return resultado;
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }*/

    private static boolean existeContacto(Contacto c) {
        try
        {
        /*    Statement stmt = Conexion.getConexion().createStatement();
            String consulta= "select * from contacto "
                    + "where creador='"+c.getCreador()+"' AND alias='"+c.getAlias()+"'";
            System.out.println(consulta);
            ResultSet rs = stmt.executeQuery(consulta);
            if(rs.next())
            {
                return true;
            }
            return false;*/
            String consulta= "select * from contacto where creador=? AND alias=?";
            PreparedStatement stmt = Conexion.getConexion().prepareStatement(consulta);
            stmt.setString(1, c.getCreador());
            stmt.setString(2, c.getAlias());
            System.out.println(stmt.toString());
            return stmt.executeQuery().next();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return true;
        }
    }
/*
    private static boolean esPropietario(int idGrupo, String aliasContacto) {
        try {
            Statement stmt = Conexion.getConexion().createStatement();
            String consulta = "select * from grupo where id like "+idGrupo+" and aliasPropietario like '"+aliasContacto+"'";
            ResultSet rs = stmt.executeQuery(consulta);
            return rs.next();
        } catch (SQLException ex) {
            Logger.getLogger(GestionContactos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }*/

/*    public static int eliminarContactosGrupo(Grupo grupo) {
        try
        {
            Statement stmt=Conexion.getConexion().createStatement();
            String consulta= "delete from contacto where idGrupo="+grupo.getId();
            System.out.println(consulta);
            stmt.executeUpdate(consulta);            
            return 0;
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            if(e.getErrorCode()==1451)
            {
                return -2; //error por integridad referencial
            }
            return -1; //error generico
        }
    }*/
}


