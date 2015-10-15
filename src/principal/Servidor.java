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

package principal;

import clases.CodigoMetodo;
import clases.EnvioPrivado;
import envio_recepcion.Comunicacion;
import envio_recepcion.Observer;
import envio_recepcion.Subject;
import gestionBD.GestionContactos;
import gestionBD.GestionEnviosPrivados;
import gestionBD.GestionGrupoContacto;
import gestionBD.GestionGrupos;
import gestionBD.GestionUsuarios;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import utilidadesBD.ConexionBD;

/**
 *
 * @author ADRIANLC
 */
public class Servidor implements Observer
{
    private File fileAplicacion;
    private String caminoAplicacion;
    private String caminoArchivosConfiguracionConEspacios;
    
    private String url, puerto, usuario, nombreBD, clave;
    
    private static ArrayList<Socket> arraySockets;
    private static ArrayList<Comunicacion> arrayComunicaciones;
    private static ArrayList<Thread> arrayThreads;
    
    
    public Servidor()
    {
        arraySockets = new ArrayList<>();
        arrayComunicaciones = new ArrayList<>();
        arrayThreads = new ArrayList<>();
    }
    
    private void configurarDirectoriosProperties()
    {
        fileAplicacion = new File(Servidor.class.getProtectionDomain()
                .getCodeSource().getLocation().getPath());
        caminoAplicacion = fileAplicacion.getAbsolutePath();
        caminoArchivosConfiguracionConEspacios = caminoAplicacion.replaceAll("%20", " ").replaceAll("\\\\", "/");
        caminoArchivosConfiguracionConEspacios = caminoArchivosConfiguracionConEspacios.concat("/../../build/classes");
    }

    private void crearBD(Statement stmt) throws SQLException {
        String consulta;
        System.out.println("La BD no existe");
        consulta = "CREATE SCHEMA "+nombreBD+"";
        stmt.executeUpdate(consulta);
        System.out.println("BD creada");
        consulta = "CREATE TABLE "+nombreBD+".`usuario` ("
                + "  `alias` varchar(45) NOT NULL,"
                + "  `contrasenha` varchar(45) NOT NULL,"
                + " `dispConectados` INT(2) UNSIGNED NOT NULL DEFAULT 0,"
                + "  PRIMARY KEY (`alias`))";
        stmt.executeUpdate(consulta);
        consulta = "CREATE TABLE "+nombreBD+".`contacto` ("
                + " `creador` varchar(45) NOT NULL,"
                + "  `alias` varchar(45) NOT NULL,"
                + "  `nombre` varchar(45) NOT NULL DEFAULT '',"
                + "  `telefono` varchar(15) DEFAULT NULL,"
                + "  `direccion` varchar(45) DEFAULT NULL,"
                + "  `email` varchar(45) DEFAULT NULL,"
                + "  PRIMARY KEY (`creador`,`alias`) USING BTREE,"
                + "  KEY `FK_usuario_contacto` (`alias`),"
                + "  CONSTRAINT `FK_creador_contacto` FOREIGN KEY (`creador`) REFERENCES `usuario` (`alias`) ON DELETE CASCADE ON UPDATE CASCADE,"
                + "  CONSTRAINT `FK_usuario_contacto` FOREIGN KEY (`alias`) REFERENCES `usuario` (`alias`) ON DELETE CASCADE ON UPDATE CASCADE)";
        stmt.executeUpdate(consulta);
        consulta = "CREATE TABLE "+nombreBD+".`grupo` ("
                + " `admin` varchar(45) NOT NULL,"
                + " `nombre` varchar(45) NOT NULL,"
                + " PRIMARY KEY (`admin`,`nombre`) USING BTREE,"
                + " KEY `pertenece` (`admin`),"
                + " CONSTRAINT `pertenece` FOREIGN KEY (`admin`) REFERENCES `usuario` (`alias`) ON DELETE CASCADE ON UPDATE CASCADE)";
        stmt.executeUpdate(consulta);
        consulta = "CREATE TABLE "+nombreBD+".`grupo_contacto` ("
                + " `admin` varchar(45) NOT NULL,"
                + " `nombreGrupo` varchar(45) NOT NULL,"
                + " `aliasContacto` varchar(45) NOT NULL,"
                + " PRIMARY KEY (`admin`,`nombreGrupo`,`aliasContacto`) USING BTREE,"
                + " KEY `FK_contacto` (`admin`,`aliasContacto`),"
                + " KEY `FK_grupo` (`admin`,`nombreGrupo`),"
                + " CONSTRAINT `FK_contacto` FOREIGN KEY (`admin`, `aliasContacto`) REFERENCES `contacto` (`creador`, `alias`) ON DELETE CASCADE ON UPDATE CASCADE,"
                + " CONSTRAINT `FK_grupo` FOREIGN KEY (`admin`, `nombreGrupo`) REFERENCES `grupo` (`admin`, `nombre`) ON DELETE CASCADE ON UPDATE CASCADE)";
        stmt.executeUpdate(consulta);
        consulta = "CREATE TABLE "+nombreBD+".`envio_privado` ("
                + " `remitente` varchar(45) NOT NULL,"
                + " `destinatario` varchar(45) NOT NULL,"
                + " `fechaHora` datetime NOT NULL,"
                + " `contenido` varchar(128) DEFAULT NULL,"
                + " `tipo` ENUM ('mensaje', 'archivo') NOT NULL,"
                + " `estado` ENUM ('EN_SERVIDOR', 'ENVIADO') DEFAULT 'EN_SERVIDOR',"
                + " PRIMARY KEY (`remitente`,`destinatario`,`fechaHora`) USING BTREE,"
                + " CONSTRAINT `FK_remitente` FOREIGN KEY (`remitente`) REFERENCES `usuario` (`alias`) ON DELETE CASCADE ON UPDATE CASCADE,"
                + " CONSTRAINT `FK_destinatario` FOREIGN KEY (`destinatario`) REFERENCES `usuario` (`alias`) ON DELETE CASCADE ON UPDATE CASCADE)";
        stmt.executeUpdate(consulta);
        consulta = "CREATE TABLE "+nombreBD+".`envio_grupal` ("
                + " `remitente` varchar(45) NOT NULL,"
                + " `adminGrupo` varchar(45) NOT NULL,"
                + " `nombreGrupo` varchar(45) NOT NULL,"
                + " `fechaHora` datetime NOT NULL,"
                + " `contenido` varchar(128) DEFAULT NULL,"
                + " `tipo` ENUM ('mensaje', 'archivo') NOT NULL,"
                + " PRIMARY KEY (`remitente`,`adminGrupo`,`nombreGrupo`,`fechaHora`) USING BTREE,"
                + " CONSTRAINT `FK_remitenteGr` FOREIGN KEY (`remitente`) REFERENCES `usuario` (`alias`) ON DELETE CASCADE ON UPDATE CASCADE,"
                + " CONSTRAINT `FK_destinatarioGr` FOREIGN KEY (`adminGrupo`,`nombreGrupo`) REFERENCES `grupo` (`admin`, `nombre`) ON DELETE CASCADE ON UPDATE CASCADE)";
        stmt.executeUpdate(consulta);
    /*    consulta = "insert into "+nombreBD+".usuario (alias, contrasenha) values ('admin','admin')";
        stmt.executeUpdate(consulta);
        consulta = "insert into "+nombreBD+".grupo (id, nombre, aliasPropietario) VALUES (0,'administrador','admin')";
        stmt.executeUpdate(consulta);*/
    }
    
    private void leerProperties()
    {
        Properties propiedades = new Properties();
        try
        {
            String fichConf = caminoArchivosConfiguracionConEspacios
                    .concat("/configuracion/config.properties");
//            propiedades.load(this.getClass().getResourceAsStream("/configuracion/config.properties")); // load para programar
            propiedades.load(new FileInputStream(fichConf)); // load para distribuír
            url = propiedades.getProperty("url");
            puerto = propiedades.getProperty("puerto");
            usuario = propiedades.getProperty("usuario");
            nombreBD = propiedades.getProperty("nombreBD");
            clave = propiedades.getProperty("clave");
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void comprobarBD()
    {
        leerProperties();
        
        try
        {
            int resultado = 0;
            ConexionBD.conectar(url, puerto, usuario, "", clave);
            Statement stmt = ConexionBD.getConexion().createStatement();
            String consulta = "SHOW DATABASES LIKE '"+nombreBD+"'";
            ResultSet rs = stmt.executeQuery(consulta);
            while(rs.next())
            {
                resultado = 1;
            }
            if(resultado == 0)
            {
                crearBD(stmt);
                
                System.out.println("Tablas creadas");
            } else
            {
                System.out.println("Ejecución normal");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Conecta con la base de datos
     * @see utilidadesBD.ConexionBD
     * @exception IOException No lanza ninguna excepci&oacute;n, pero había que usar esta etiqueta...
     */
    private void conectarBD()
    {
        switch(ConexionBD.conectar(this.url, this.puerto, this.usuario, this.nombreBD, this.clave))
        {
            case -1: System.err.println("Error en cadena de conexion");
                System.exit(0);
                return;
            case -2: System.err.println("No se ha cargado el driver");
                System.exit(0);
                return;
        }
    }
    
    private void inicializarValoresBD()
    {
        try {
            String consulta = "set SQL_SAFE_UPDATES=0";
            PreparedStatement stmt = ConexionBD.getConexion().prepareStatement(consulta);
            stmt.executeUpdate();
            consulta = "update conect_app.usuario set dispConectados=0";
            stmt = ConexionBD.getConexion().prepareStatement(consulta);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void update(Subject subject) {
        if(subject instanceof Comunicacion)
        {
            Comunicacion com = (Comunicacion) subject;
            int posViaCom;
            ObjectOutputStream objFlujoS = null;
            try {
                objFlujoS = com.getObjFlujoS();
                switch(com.getCodigo())
                {
                    case CodigoMetodo.REGISTRARSE:
                    case CodigoMetodo.INICIAR_SESION:
                    //    objFlujoS.writeInt(com.getResultado());
                        objFlujoS.writeObject(com.getCodigo());
                        System.out.println("Servidor: Codigo insertado en flujo");
                        objFlujoS.writeObject(com.getResultado());
                        System.out.println("Servidor: Resultado insertado en flujo");
                        break;
                    case CodigoMetodo.DESCONECTARSE:
                        GestionUsuarios.cerrarSesion(com.getUser());
                        posViaCom = arraySockets.indexOf(com.getSocket());
                        arrayThreads.get(posViaCom).interrupt();
                        arraySockets.remove(com.getSocket());
                        arrayThreads.remove(posViaCom);
                        break;
                    case CodigoMetodo.LISTAR_CONTACTOS_USUARIO:
                        objFlujoS.writeObject(com.getCodigo());
                        objFlujoS.writeObject(GestionContactos.listarContactos(com.getAdmin()));
                        break;
                    case CodigoMetodo.INSERTAR_CONTACTO:
                        objFlujoS.writeObject(com.getCodigo());
                        objFlujoS.writeObject(GestionContactos.insertarContacto(com.getContacto()));
                        break;
                    case CodigoMetodo.ELIMINAR_CONTACTO:
                        objFlujoS.writeObject(com.getCodigo());
                        objFlujoS.writeObject(GestionContactos.eliminarContacto(com.getContacto()));
                        break;
                    case CodigoMetodo.MODIFICAR_CONTACTO:
                        objFlujoS.writeObject(com.getCodigo());
                        objFlujoS.writeObject(GestionContactos.modificarContacto(com.getContacto()));
                        break;
                    case CodigoMetodo.LISTAR_GRUPOS:
                        objFlujoS.writeObject(com.getCodigo());
                        objFlujoS.writeObject(GestionGrupos.listarGrupos(com.getAdmin()));
                        break;
                    case CodigoMetodo.INSERTAR_GRUPO:
                        objFlujoS.writeObject(com.getCodigo());
                        objFlujoS.writeObject(GestionGrupos.insertarGrupo(com.getGrupo()));
                        break;
                    case CodigoMetodo.LISTAR_CONTACTOS_GRUPO:
                        objFlujoS.writeObject(com.getCodigo());
                        objFlujoS.writeObject(GestionGrupoContacto.listarContactosGrupo(com.getGrupo()));
                        break;
                /*    case CodigoMetodo.ELIMINAR_GRUPO:
                        objFlujoS.writeObject(com.getCodigo());
                        int resultado;
                        if((resultado=GestionContactos.eliminarContactosGrupo(com.getGrupo())) == 0)
                        {
                            resultado = GestionGrupos.eliminarGrupo(com.getGrupo());
                        }
                        objFlujoS.writeObject(resultado);
                        break;*/
                    case CodigoMetodo.MODIFICAR_GRUPO:
                        objFlujoS.writeObject(com.getCodigo());
                        objFlujoS.writeObject(GestionGrupos.modificarGrupo(com.getGrupo(), com.getNombreActual()));
                        break;
                    case CodigoMetodo.ENVIAR_MENSAJE_P:
                        objFlujoS.writeObject(com.getCodigo());
                        objFlujoS.writeObject(GestionEnviosPrivados.insertarEnvioP(com.getEnvioPrivado()));
                        this.enviarMensajeP(com.getEnvioPrivado());
                        break;
                    case CodigoMetodo.INSERTAR_GRUPO_CONTACTO:
                        objFlujoS.writeObject(com.getCodigo());
                        objFlujoS.writeObject(GestionGrupoContacto.insertarContactoAGrupo(com.getGrupo(), com.getContacto()));
                        objFlujoS.writeObject(com.getGrupo());
                        break;
                    default:
                        break;
                }
            } catch (IOException ex) {
                Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
    public static void main(String args[])
    {
        Servidor server = new Servidor();
        Comunicacion com;
        Thread thCom;
        
        server.configurarDirectoriosProperties();
        server.comprobarBD();
        server.conectarBD();
        server.inicializarValoresBD();
        
        try
        {
            ServerSocket socketServidor = new ServerSocket(62006);
            while(true)
            {
                if(arraySockets.add(socketServidor.accept()))
                {
                    com = new Comunicacion(arraySockets.get(arraySockets.size()-1));
                    com.registerObserver(server);
                    arrayComunicaciones.add(com);
                    thCom = new Thread(com);
                    arrayThreads.add(thCom);
                    thCom.start();
                }
            }
        } catch (BindException ex)
        {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void enviarMensajeP(EnvioPrivado mensajeP) {
        for(Comunicacion c : arrayComunicaciones)
        {
            if(c.getUser().getAlias().equals(mensajeP.getDestinatario()))
            {
                ObjectOutputStream objFlujoS = c.getObjFlujoS();
                try {
                    objFlujoS.writeObject(CodigoMetodo.RECIBIR_MENSAJE_P);
                    objFlujoS.writeObject(mensajeP);
                } catch (IOException ex) {
                    Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                }
                return;
            }
        }
    }
}
