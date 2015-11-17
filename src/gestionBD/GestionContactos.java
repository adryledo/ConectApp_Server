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

import java.sql.ResultSet;
import java.sql.SQLException;
import utilidadesBD.ConexionBD;

import clases.Contacto;
import java.sql.PreparedStatement;
import java.util.ArrayList;
/**
 *
 * @author Adrian Ledo
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
        if(existeContacto(c))
        {
            return -2;
        }
        if(!GestionUsuarios.existeUsuario(c.getAlias()))
        {
            return -4;
        }
        
        try
        {
            String consulta = "insert into contacto "
                    + "(creador, alias, nombre, telefono, direccion, email) VALUES (?,?,?,?,?,?)";
            
            PreparedStatement stmt = ConexionBD.getConexion().prepareStatement(consulta);
            stmt.setString(1, c.getCreador());
            stmt.setString(2, c.getAlias());
            stmt.setString(3, c.getNombre());
            stmt.setString(4, c.getTelefono());
            stmt.setString(5, c.getDireccion());
            stmt.setString(6, c.getEmail());
            
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
            String consulta= "delete from contacto where creador=? AND alias=?";
            PreparedStatement stmt = ConexionBD.getConexion().prepareStatement(consulta);
            stmt.setString(1, c.getCreador());
            stmt.setString(2, c.getAlias());
            
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
            PreparedStatement stmt = ConexionBD.getConexion().prepareStatement(consulta);
            stmt.setString(1, c.getNombre());
            stmt.setString(2, c.getTelefono());
            stmt.setString(3, c.getDireccion());
            stmt.setString(4, c.getEmail());
            stmt.setString(5, c.getCreador());
            stmt.setString(7, c.getAlias());
            
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
            String consulta = "SELECT * FROM contacto where creador=?";
            PreparedStatement stmt = ConexionBD.getConexion().prepareStatement(consulta);
            stmt.setString(1, creador);
            stmt.execute();
            ResultSet rs = stmt.getResultSet();
            while(rs.next())
            {
                Contacto c = new Contacto(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6));
                resultado.add(c);
            }
            
            return resultado;
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    private static boolean existeContacto(Contacto c) {
        try
        {
            String consulta= "select * from contacto where creador=? AND alias=?";
            PreparedStatement stmt = ConexionBD.getConexion().prepareStatement(consulta);
            stmt.setString(1, c.getCreador());
            stmt.setString(2, c.getAlias());
            
            return stmt.executeQuery().next();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return true;
        }
    }
}


