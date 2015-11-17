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

import clases.Archivo;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import utilidadesBD.ConexionBD;

/**
 *
 * @author Adrian Ledo
 */
public class GestionArchivos
{
    public static int insertarArchivo(Archivo a) {
        try
        {
            String consulta = "insert into archivo (aliasUsuario, aliasContacto, fecha, nombre) VALUES (?,?,?,?)";
            PreparedStatement stmt = ConexionBD.getConexion().prepareStatement(consulta);
            stmt.setString(1, a.getAliasUsuario());
            stmt.setString(2, a.getAliasContacto());
            stmt.setString(3, a.getFecha());
            stmt.setString(4, a.getNombre());
            
            stmt.executeUpdate();
            return 0;
        }
        catch(SQLException e)
        {
            return -1;
        }
    }
}
