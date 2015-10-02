/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package principal;

import clases.CodigoMetodo;
import envio_recepcion.Comunicacion;
import envio_recepcion.Observer;
import envio_recepcion.Subject;
import gestionBD.GestionContactos;
import gestionBD.GestionGrupos;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import utilidadesBD.Conexion;

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
    private static ArrayList<Thread> arrayThreads;
    
    
    public Servidor()
    {
        arraySockets = new ArrayList<>();
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
                + "  PRIMARY KEY (`alias`))";
        stmt.executeUpdate(consulta);
        consulta = "CREATE TABLE "+nombreBD+".`grupo` ("
                + " `id` int(11) NOT NULL AUTO_INCREMENT,"
                + " `nombre` varchar(45) NOT NULL,"
                + " `aliasPropietario` varchar(45) NOT NULL,"
                + " PRIMARY KEY (`id`),"
                + " UNIQUE `nombre_alias` (`nombre`, `aliasPropietario`),"
                + " KEY `pertenece` (`aliasPropietario`),"
                + " CONSTRAINT `pertenece` FOREIGN KEY (`aliasPropietario`)"
                + " REFERENCES `usuario` (`alias`)"
                + " ON DELETE CASCADE ON UPDATE CASCADE)";
        stmt.executeUpdate(consulta);
        consulta = "CREATE TABLE "+nombreBD+".`contacto` ("
                + " `idGrupo` int(11) NOT NULL,"
                + " `aliasContacto` varchar(45) NOT NULL,"
                + " `nombre` varchar(45) DEFAULT NULL,"
                + " `telefono` varchar(15) DEFAULT NULL,"
                + " `direccion` varchar(45) DEFAULT NULL,"
                + " `email` varchar(45) DEFAULT NULL,"
                + " PRIMARY KEY (`idGrupo`,`aliasContacto`),"
                + " KEY `grupo` (`idGrupo`),"
                + " CONSTRAINT `grupo` FOREIGN KEY (`idGrupo`)"
                + " REFERENCES `grupo` (`id`)"
                + " ON DELETE CASCADE ON UPDATE CASCADE)";
        stmt.executeUpdate(consulta);
        consulta = "CREATE TABLE "+nombreBD+".`conexion` ("
                + " `aliasUsuario` varchar(45) NOT NULL,"
                + " `ip` varchar(45) NOT NULL,"
                + " PRIMARY KEY (`aliasUsuario`,`ip`))";
        stmt.executeUpdate(consulta);
        consulta = "CREATE TABLE "+nombreBD+".`mensaje` ("
                + " `aliasUsuario` varchar(45) NOT NULL,"
                + " `aliasContacto` varchar(45) NOT NULL,"
                + " `fecha` datetime NOT NULL,"
                + " `contenido` varchar(128) DEFAULT NULL,"
                + " PRIMARY KEY (`aliasUsuario`,`aliasContacto`,`fecha`))";
        stmt.executeUpdate(consulta);
        consulta = "CREATE TABLE "+nombreBD+".`archivo` ("
                + " `aliasUsuario` varchar(45) NOT NULL,"
                + " `aliasContacto` varchar(45) NOT NULL,"
                + " `fecha` datetime NOT NULL,"
                + " `nombre` varchar(45) NOT NULL,"
                + " PRIMARY KEY (`aliasUsuario`,`aliasContacto`,`fecha`))";
        stmt.executeUpdate(consulta);
        consulta = "insert into "+nombreBD+".usuario (alias, contrasenha) values ('admin','admin')";
        stmt.executeUpdate(consulta);
        consulta = "insert into "+nombreBD+".grupo (id, nombre, aliasPropietario) VALUES (0,'administrador','admin')";
        stmt.executeUpdate(consulta);  
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
            Conexion.conectar(url, puerto, usuario, "", clave);
            Statement stmt = Conexion.getConexion().createStatement();
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
     * @see utilidadesBD.Conexion
     * @exception IOException No lanza ninguna excepci&oacute;n, pero había que usar esta etiqueta...
     */
    private void conectarBD()
    {
        switch(Conexion.conectar(this.url, this.puerto, this.usuario, this.nombreBD, this.clave))
        {
            case -1: System.err.println("Error en cadena de conexion");
                System.exit(0);
                return;
            case -2: System.err.println("No se ha cargado el driver");
                System.exit(0);
                return;
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
                        posViaCom = arraySockets.indexOf(com.getSocket());
                        arrayThreads.get(posViaCom).interrupt();
                        arraySockets.remove(com.getSocket());
                        arrayThreads.remove(posViaCom);
                        break;
                    case CodigoMetodo.LISTAR_CONTACTOS:
                        objFlujoS.writeObject(com.getCodigo());
                        objFlujoS.writeObject(GestionContactos.listarContactos(com.getGroupOwnerNick()));
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
                        objFlujoS.writeObject(GestionGrupos.listarGrupos(com.getGroupOwnerNick()));
                        break;
                    case CodigoMetodo.INSERTAR_GRUPO:
                        objFlujoS.writeObject(com.getCodigo());
                        objFlujoS.writeObject(GestionGrupos.insertarGrupo(com.getGrupo()));
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
        
        try
        {
            ServerSocket socketServidor = new ServerSocket(62006);
            while(true)
            {
                if(arraySockets.add(socketServidor.accept()))
                {
                    com = new Comunicacion(arraySockets.get(arraySockets.size()-1));
                    com.registerObserver(server);
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
}
