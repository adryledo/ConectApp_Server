/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utilidadesBD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author CURSO 1314
 */
public class Conexion {
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


