/*
 * Copyright (C) 2015 infdaid2
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

import clases.Contacto;
import clases.Grupo;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import utilidadesBD.ConexionBD;

/**
 *
 * @author Adri&aacute;n Ledo
 */
public class GestionGrupoContacto {
    /**
     * Nueva inserci&oacute;n en la tabla grupo_contacto
     * @param g
     * @param c
     * @return 0    si pudo insertar<br>
     *      -1  <br>
     *      -2
     */
    public static int insertarContactoAGrupo(Grupo g, Contacto c)
    {
        try {
            String consulta = "insert into grupo_contacto values (?,?,?)";
            PreparedStatement stmt = ConexionBD.getConexion().prepareStatement(consulta);
            stmt.setString(1, g.getAdmin());
            stmt.setString(2, g.getNombre());
            stmt.setString(3, c.getAlias());
            
            return (stmt.executeUpdate()==1 ? 0 : -2);
        } catch (SQLException ex) {
            Logger.getLogger(GestionGrupoContacto.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }
    
    /**
     * 
     * @param g
     * @return Contactos del grupo dado<br>
     *      null    si falla
     */
    public static ArrayList<Contacto> listarContactosGrupo(Grupo g)
    {
        try {
            ArrayList<Contacto> arrayContactos = new ArrayList<>();
            String consulta = "select * from contacto where alias in (select aliasContacto"
                    + " from grupo_contacto where admin=? and nombreGrupo=?)";
            PreparedStatement stmt = ConexionBD.getConexion().prepareStatement(consulta);
            stmt.setString(1, g.getAdmin());
            stmt.setString(2, g.getNombre());
            stmt.execute();
            ResultSet rs = stmt.getResultSet();
            while(rs.next())
            {
                Contacto c = new Contacto(rs.getString(1), rs.getString(2), rs.getString(3),
                        rs.getString(4), rs.getString(5), rs.getString(6));
                arrayContactos.add(c);
            }
            return arrayContactos;
        } catch (SQLException ex) {
            Logger.getLogger(GestionGrupoContacto.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    /**
     * 
     * @param contacto
     * @return Grupos a los que pertenece el contacto
     */
    public static ArrayList<Grupo> listarGruposContacto(Contacto contacto)
    {
        try {
            ArrayList<Grupo> arrayGrupos = new ArrayList<>();
            String consulta = "select nombreGrupo from grupo_contacto where"
                    + " admin=? and aliasContacto=?";
            PreparedStatement stmt = ConexionBD.getConexion().prepareStatement(consulta);
            stmt.setString(1, contacto.getCreador());
            stmt.setString(2, contacto.getAlias());
            stmt.execute();
            ResultSet rs = stmt.getResultSet();
            while(rs.next())
            {
                arrayGrupos.add(new Grupo(contacto.getCreador(), rs.getString(1)));
            }
            return arrayGrupos;
        } catch (SQLException ex) {
            Logger.getLogger(GestionGrupoContacto.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    /**
     * Elimina la fila correspondiente de la tabla grupo_contacto
     * @param g Grupo del que se elimina
     * @param c Contacto a eliminar
     * @return 0    si pudo eliminar<br>
     *      -1 o -2 si en caso de fallo
     */
    public static int eliminarContactoDeGrupo(Grupo g, Contacto c)
    {
        try {
            String consulta = "delete from grupo_contacto where admin=? and nombreGrupo=? and aliasContacto=?";
            PreparedStatement stmt = ConexionBD.getConexion().prepareStatement(consulta);
            stmt.setString(1, g.getAdmin());
            stmt.setString(2, g.getNombre());
            stmt.setString(3, c.getAlias());
            
            return (stmt.executeUpdate()==1 ? 0 : -2);
        } catch (SQLException ex) {
            Logger.getLogger(GestionGrupoContacto.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }
}
