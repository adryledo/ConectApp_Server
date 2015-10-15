/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gestionBD;

import clases.Grupo;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import utilidadesBD.Conexion;
/**
 *
 * @author Adri&aacute;n Ledo
 */
public class GestionGrupos
{
    /**
     * 
     * @param g Grupo a insertar
     * @return -1 si hay alg&uacute;n error<br>
     *      -2 si no existe ningún usuario con ese alias<br>
     *      0 si se pudo insertar
     */
    public static int insertarGrupo(Grupo g)
    {
        if(!GestionUsuarios.existeUsuario(g.getAdmin()))
        {
            return -2;
        }
        
        try
        {
            String consulta= "insert into grupo (admin, nombre) VALUES (?,?)";
            PreparedStatement stmt = Conexion.getConexion().prepareStatement(consulta);
            stmt.setString(1, g.getAdmin());
            stmt.setString(2, g.getNombre());
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
     * 
     * @param g Grupo a eliminar
     * @return 0    Si se pudo eliminar<br>
     *      -1  Si falla<br>
     *      -2  Si eliminó más de uno o ninguno
     */
    public static int eliminarGrupo(Grupo g)
    {
        try
        {
        /*    Statement stmt=Conexion.getConexion().createStatement();
            String consulta= "delete from grupo where admin='"+g.getAdmin()+"' and"
                    + " nombre='"+g.getNombre()+"'";
            System.out.println(consulta);
            stmt.executeUpdate(consulta);
            return 0;*/
            String consulta= "delete from grupo where admin=? and nombre=?";
            PreparedStatement stmt = Conexion.getConexion().prepareStatement(consulta);
            stmt.setString(1, g.getAdmin());
            stmt.setString(2, g.getNombre());
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
     * Modifica el nombre del grupo
     * @param g Grupo con el nombre nuevo
     * @param nombreActual nombre del grupo antes de modificarlo
     * @return 0    si todo fue bien<br>
     *      -1  si no se pudo modificar<br>
     *      -2  si modifica más de uno o ninguno
     */
    public static int modificarGrupo(Grupo g, String nombreActual)
    {
        try
        {
        /*    Statement stmt=Conexion.getConexion().createStatement();
            String consulta= "update grupo set nombre='"+g.getNombre()+"' where admin='"+g.getAdmin()
                    + "' and nombre='"+nombreActual+"'";
            stmt.executeUpdate(consulta);
            System.out.println(stmt.toString());
            return 0;*/
            String consulta= "update grupo set nombre=? where admin=? and nombre=?";
            PreparedStatement stmt = Conexion.getConexion().prepareStatement(consulta);
            stmt.setString(1, g.getNombre());
            stmt.setString(2, g.getAdmin());
            stmt.setString(3, nombreActual);
            System.out.println(stmt.toString());
            return (stmt.executeUpdate()==1 ? 0 : -2);
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return -1; //error generico
        }
        
    }
    
    public static ArrayList<Grupo> listarGrupos()
    {
        try
        {
            ArrayList<Grupo> resultado = new ArrayList<>();
        /*    Statement stmt = Conexion.getConexion().createStatement();
            String consulta= "select * from grupo order by nombre";
            System.out.println(consulta);
            ResultSet rs = stmt.executeQuery(consulta);
            while(rs.next())
            {
                Grupo g = new Grupo(rs.getString(1), rs.getString(2));
                resultado.add(g);
            }
            return resultado;*/
            String consulta= "select * from grupo order by nombre";
            PreparedStatement stmt = Conexion.getConexion().prepareStatement(consulta);
            stmt.execute();
            ResultSet rs = stmt.getResultSet();
            while(rs.next())
            {
                Grupo g = new Grupo(rs.getString(1), rs.getString(2));
                resultado.add(g);
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
    
    /**
     * 
     * @param admin Nick del propietario de los grupos
     * @return Array con los grupos que pertenecen al alias<br>
     *      <i>null</i> si falla
     */
    public static ArrayList<Grupo> listarGrupos(String admin)
    {
        try
        {
            ArrayList<Grupo> resultado = new ArrayList();
        /*    Statement stmt = Conexion.getConexion().createStatement();
            String consulta= "select * from grupo "
                    + "where admin='"+admin+"' order by nombre";
            System.out.println(consulta);
            ResultSet rs = stmt.executeQuery(consulta);*/
            String consulta = "select * from grupo where admin=? order by nombre";
            PreparedStatement stmt = Conexion.getConexion().prepareStatement(consulta);
            stmt.setString(1, admin);
            stmt.execute();
            ResultSet rs = stmt.getResultSet();
            while(rs.next())
            {
                Grupo g = new Grupo( rs.getString(1), rs.getString(2));
                resultado.add(g);
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
}