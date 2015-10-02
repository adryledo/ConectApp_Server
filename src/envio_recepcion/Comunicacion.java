/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package envio_recepcion;

import clases.CodigoMetodo;
import clases.Usuario;
import gestionBD.GestionConexiones;
import gestionBD.GestionUsuarios;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import clases.Contacto;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 *
 * @author infdaid2
 */
public class Comunicacion extends Subject implements Runnable{

    private final Socket socket;
    private int codigo;
    private int resultado;
    private Contacto contacto;
    private ObjectOutputStream objFlujoS;

    public Comunicacion(Socket s)
    {
        OutputStream flujoS = null;
        this.socket = s;
        
        try {
            flujoS = this.socket.getOutputStream();
            this.objFlujoS = new ObjectOutputStream(flujoS);
        } catch (IOException ex) {
            Logger.getLogger(Comunicacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ObjectOutputStream getObjFlujoS() {
        return objFlujoS;
    }

    public int getCodigo() {
        return codigo;
    }
    
    public int getResultado() {
        return resultado;
    }

    public Contacto getContacto() {
        return contacto;
    }
    
    public Socket getSocket() {
        return socket;
    }
    
    @Override
    public void run() {
        
        InputStream flujoEntrada = null;
        ObjectInputStream objFlujoE = null;
        String IP;
        
        try {
            //http://codigoprogramacion.com/cursos/java/103-sockets-en-java-con-cliente-y-servidor.html#.VgEaod-qpBc
            flujoEntrada = socket.getInputStream();
            objFlujoE = new ObjectInputStream(flujoEntrada);
            Usuario user = null;
            while(socket.isConnected())
            {
                switch(codigo = (Integer)objFlujoE.readObject())
                {
                    case CodigoMetodo.REGISTRARSE:
                        System.out.println("Comunicacion: Codigo recibido");
                        user = (Usuario)objFlujoE.readObject();
                        System.out.println("Comunicacion: Usuario recibido");
                        IP = (String)objFlujoE.readObject();           
                        System.out.println("Comunicacion: IP leída de flujo");
                        this.resultado = GestionUsuarios.registrarse(user);
                        if(this.resultado == 0)
                        {
                            GestionConexiones.insertarConexion(new clases.Conexion(user.getAlias(), IP));
                        }
                        this.notifyObservers();
                        break;
                    case CodigoMetodo.INICIAR_SESION:
                        System.out.println("Comunicacion: Codigo recibido");
                        user = (Usuario)objFlujoE.readObject();
                        System.out.println("Comunicacion: Usuario recibido");
                        IP = (String)objFlujoE.readObject();           
                        System.out.println("Comunicacion: IP leída de flujo");
                        this.resultado = GestionUsuarios.iniciarSesion(user);
                        this.notifyObservers();
                        break;
                    case CodigoMetodo.ELIMINAR_CONTACTO:
                    case CodigoMetodo.ELIMINAR_GRUPO:
                    case CodigoMetodo.INSERTAR_CONTACTO:
                        this.contacto = (Contacto) objFlujoE.readObject();
                        this.notifyObservers();
                        break;
                    case CodigoMetodo.INSERTAR_GRUPO:
                    case CodigoMetodo.LISTAR_CONTACTOS:
                        this.notifyObservers();
                        break;
                    case CodigoMetodo.LISTAR_CONTACTOS_GRUPO:
                    case CodigoMetodo.LISTAR_GRUPOS:
                    case CodigoMetodo.MODIFICAR_CONTACTO:
                    case CodigoMetodo.MOSTRAR_MENSAJES:
                    case CodigoMetodo.RECUPERAR_CONTACTO:
                        /*System.out.println("Comunicacion: Código de método leído de flujo");
                        objeto = objFlujoE.readObject();
                        System.out.println("Comunicacion: Objeto leído de flujo");
                        IP = (String)objFlujoE.readObject();           
                        System.out.println("Comunicacion: IP leída de flujo");*/
                        break;
                    default:
                        break;
                }
            }
        //    this.notifyObservers();
        } catch (IOException ex) {
            //Logger.getLogger(Comunicacion.class.getName()).log(Level.SEVERE, null, ex);
            /*try {
                objFlujoE.close();
                flujoEntrada.close();
                //this.socket.close();
            } catch (IOException e) {
                Logger.getLogger(Comunicacion.class.getName()).log(Level.SEVERE, null, e);
            }*/
            this.codigo = CodigoMetodo.DESCONECTARSE;
            this.notifyObservers();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Comunicacion.class.getName()).log(Level.SEVERE, null, ex);
        }/* finally
        {
            
        }*/
    }
    
}
