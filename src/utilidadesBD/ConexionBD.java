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

package utilidadesBD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author CURSO 1314
 */
public class ConexionBD {
    private static Connection conexion;

    public static int conectar(String url,String puerto,String usuario,String bd,String clave){
        
        try 
        {            
            Class.forName("com.mysql.jdbc.Driver");
            String cadenaConexion="jdbc:mysql://"+url+":"+puerto+"/"+bd+"?user="+usuario+"&password="+clave;
            System.out.println("Cadena de conexion:"+cadenaConexion);
            conexion=DriverManager.getConnection(cadenaConexion);
            return 0;
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
            return -1;
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
            return -2;
        }
    }    
    
    public static Connection getConexion()
    {
        return conexion;
    }
    
}


