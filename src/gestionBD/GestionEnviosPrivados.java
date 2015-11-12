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

import clases.EnvioPrivado;
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
public class GestionEnviosPrivados {
    /**
     * 
     * @param ep
     * @return 0    si se pudo insertar<br>
     *      -1  en caso de error
     */
    public static int insertarEnvioP(EnvioPrivado ep) {
        try
        {
            String consulta = "insert into envio_privado "
                    + "(remitente, destinatario, fechaHora, contenido, tipo, estado) VALUES (?,?,?,?,?,?)";
            PreparedStatement stmt = ConexionBD.getConexion().prepareStatement(consulta);
            stmt.setString(1, ep.getRemitente());
            stmt.setString(2, ep.getDestinatario());
            stmt.setTimestamp(3, ep.getFechaHora());
            stmt.setString(4, ep.getContenido());
            stmt.setString(5, ep.getTipo().toString());
            stmt.setString(6, EnvioPrivado.Enum_estado.EN_SERVIDOR.toString());
            
            System.out.println(stmt.toString());
            stmt.executeUpdate();
            return 0;
        }
        catch(SQLException e)
        {
            return -1;
        }
    }
    
    /**
     * 
     * @param remitente
     * @param destinatario
     * @return contenido de los envios de remitente a destinatario y viceversa
     */
    public static ArrayList<String> mostrarEnviosP(String remitente, String destinatario)
    {
        ArrayList<String> listaMensajes = new ArrayList<>();
        try {
            /*Statement stmt = ConexionBD.getConexion().createStatement();
            String consulta = "select contenido from mensaje where (aliasUsuario like '"+remitente+"' AND aliasContacto like '"+destinatario+"') "
                    + "OR (aliasUsuario like '"+destinatario+"' AND aliasContacto like '"+remitente+"')";
            ResultSet rs = stmt.executeQuery(consulta);*/
            String consulta = "select contenido from envio_privado where (remitente=? AND"
                    + " destinatario=?) OR (remitente=? AND destinatario=?)";
            PreparedStatement stmt = ConexionBD.getConexion().prepareStatement(consulta);
            stmt.setString(1, remitente);
            stmt.setString(2, destinatario);
            stmt.setString(3, destinatario);
            stmt.setString(4, remitente);
            ResultSet rs = stmt.executeQuery();
            while(rs.next())
            {
                listaMensajes.add(rs.getString(1));
            }
            return listaMensajes;
        } catch (SQLException ex) {
            Logger.getLogger(GestionEnviosPrivados.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Asigna un estado a un mensaje
     * @param mensajeP Mensaje a modificar
     * @param enum_estado Nuevo estado
     * @return 0 si todo va bien<br>
     *      -1 o -2 si no se pudo modificar
     */
    public static int modificarEstado(EnvioPrivado mensajeP, EnvioPrivado.Enum_estado enum_estado) {
        try {
            String consulta = "update envio_privado set estado=? where remitente=? and destinatario=? and fechaHora=?";
            PreparedStatement stmt = ConexionBD.getConexion().prepareStatement(consulta);
            stmt.setString(1, enum_estado.toString());
            stmt.setString(2, mensajeP.getRemitente());
            stmt.setString(3, mensajeP.getDestinatario());
            stmt.setTimestamp(4, mensajeP.getFechaHora());
            
            return (stmt.executeUpdate()!=0 ? 0 : -2);
        } catch (SQLException ex) {
            Logger.getLogger(GestionEnviosPrivados.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }
    
    /**
     * Busca los mensajes destinados a 'destinatario' que aun no hayan sido enviados
     * @param destinatario
     * @return 
     */
    public static ArrayList<EnvioPrivado> mostrarMensajesPendientes(String destinatario)
    {
        ArrayList<EnvioPrivado> enviosPendientes = new ArrayList<>();
        try {
            String consulta = "select * from envio_privado where destinatario=? and tipo=? and estado=?";
            PreparedStatement stmt = ConexionBD.getConexion().prepareStatement(consulta);
            stmt.setString(1, destinatario);
            stmt.setString(2, EnvioPrivado.Enum_tipo.MENSAJE.toString());
            stmt.setString(3, EnvioPrivado.Enum_estado.EN_SERVIDOR.toString());
            ResultSet rs = stmt.executeQuery();
            while(rs.next())
            {
                enviosPendientes.add(new EnvioPrivado(rs.getString(1), rs.getString(2), rs.getTimestamp(3), rs.getString(4), EnvioPrivado.Enum_tipo.valueOf(rs.getString(5))/*rs.getObject(5, EnvioPrivado.Enum_tipo.class)*/, EnvioPrivado.Enum_estado.valueOf(rs.getString(6))/*rs.getObject(6, EnvioPrivado.Enum_estado.class)*/));
            }
            return enviosPendientes;
        } catch (SQLException ex) {
            Logger.getLogger(GestionEnviosPrivados.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static ArrayList<EnvioPrivado> mostrarEnviosP(String remitente, String destinatario, EnvioPrivado.Enum_tipo tipo) {
        ArrayList<EnvioPrivado> listaEnvios = new ArrayList<>();
        try {
            String consulta = "select * from envio_privado where ((remitente=? AND"
                    + " destinatario=?) OR (remitente=? AND destinatario=?)) AND tipo=?";
            PreparedStatement stmt = ConexionBD.getConexion().prepareStatement(consulta);
            stmt.setString(1, remitente);
            stmt.setString(2, destinatario);
            stmt.setString(3, destinatario);
            stmt.setString(4, remitente);
            stmt.setString(5, tipo.toString());
            ResultSet rs = stmt.executeQuery();
            while(rs.next())
            {
                listaEnvios.add(new EnvioPrivado(rs.getString(1), rs.getString(2), rs.getTimestamp(3), rs.getString(4), EnvioPrivado.Enum_tipo.valueOf(rs.getString(5))));
            }
            return listaEnvios;
        } catch (SQLException ex) {
            Logger.getLogger(GestionEnviosPrivados.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
