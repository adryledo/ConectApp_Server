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
 * @author CURSO 1314
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
        if(!GestionUsuarios.existeUsuario(g.getAliasPropietario()))
        {
            return -2;
        }
        
        try
        {
            String consulta= "insert into grupo (id, nombre, aliasPropietario) VALUES (?,?,?)";
            PreparedStatement stmt = Conexion.getConexion().prepareStatement(consulta);
            stmt.setInt(1, g.getId());
            stmt.setString(2, g.getNombre());
            stmt.setString(3, g.getAliasPropietario());
            System.out.println(consulta);
            stmt.executeUpdate();            
            return 0;
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return -1;
        }
        
    }

    public static int eliminarGrupo(Grupo g)
    {
        try
        {
            Statement stmt=Conexion.getConexion().createStatement();
            String consulta= "delete from grupo where id="+g.getId();
            System.out.println(consulta);
            stmt.executeUpdate(consulta);            
            return 0;
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return -1; //error generico
        }
    }
    
//    public static int modificarPersona(Persona p)
//    {
//        try
//        {
//            Statement stmt=Conexion.getConexion().createStatement();
//            String consulta= "update personas set nombre='"+p.getNombre()+"',edad="+p.getEdad()+",cod_ciudad="+p.getCodCiudad()+" where cod_persona="+p.getCodPersona();
//            System.out.println(consulta);
//            stmt.executeUpdate(consulta);            
//            return 0;
//        }
//        catch(SQLException e)
//        {
//            e.printStackTrace();
//            return -1; //error generico
//        }
//        
//    }
    
    public static ArrayList<Grupo> listarGrupos()
    {
        try
        {
            ArrayList<Grupo> resultado = new ArrayList<>();
            Statement stmt = Conexion.getConexion().createStatement();
            String consulta= "select * from grupo order by nombre";
            System.out.println(consulta);
            ResultSet rs = stmt.executeQuery(consulta);
            while(rs.next())
            {
                Grupo g = new Grupo(rs.getInt(1), rs.getString(2), rs.getString(3));
                resultado.add(g);
            }
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
     * @param aliasPropietario Nick del propietario de los grupos
     * @return Array con los grupos que pertenecen al alias<br>
     *      <i>null</i> si falla
     */
    public static ArrayList<Grupo> listarGrupos(String aliasPropietario)
    {
        try
        {
            ArrayList<Grupo> resultado = new ArrayList();
            Statement stmt = Conexion.getConexion().createStatement();
            String consulta= "select * from grupo "
                    + "where aliasPropietario='"+aliasPropietario+"' order by nombre";
            System.out.println(consulta);
            ResultSet rs = stmt.executeQuery(consulta);
            while(rs.next())
            {
                Grupo g = new Grupo(rs.getInt(1), rs.getString(2), rs.getString(3));
                resultado.add(g);
            }
            return resultado;
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }
}