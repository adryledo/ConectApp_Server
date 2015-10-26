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

import clases.Grupo;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import utilidadesBD.ConexionBD;
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
            PreparedStatement stmt = ConexionBD.getConexion().prepareStatement(consulta);
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
            String consulta= "delete from grupo where admin=? and nombre=?";
            PreparedStatement stmt = ConexionBD.getConexion().prepareStatement(consulta);
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
     * @param nuevoNombre nombre del grupo antes de modificarlo
     * @return 0    si todo fue bien<br>
     *      -1  si no se pudo modificar<br>
     *      -2  si modifica más de uno o ninguno
     */
    public static int modificarGrupo(Grupo g, String nuevoNombre)
    {
        try
        {
            String consulta= "update grupo set nombre=? where admin=? and nombre=?";
            PreparedStatement stmt = ConexionBD.getConexion().prepareStatement(consulta);
            stmt.setString(1, nuevoNombre);
            stmt.setString(2, g.getAdmin());
            stmt.setString(3, g.getNombre());
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
        /*    Statement stmt = ConexionBD.getConexion().createStatement();
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
            PreparedStatement stmt = ConexionBD.getConexion().prepareStatement(consulta);
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
        /*    Statement stmt = ConexionBD.getConexion().createStatement();
            String consulta= "select * from grupo "
                    + "where admin='"+admin+"' order by nombre";
            System.out.println(consulta);
            ResultSet rs = stmt.executeQuery(consulta);*/
            String consulta = "select * from grupo where admin=? order by nombre";
            PreparedStatement stmt = ConexionBD.getConexion().prepareStatement(consulta);
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