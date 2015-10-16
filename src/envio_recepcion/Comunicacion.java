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

package envio_recepcion;

import clases.CodigoMetodo;
import clases.Contacto;
import clases.EnvioPrivado;
import clases.Grupo;
import clases.GrupoContacto;
import clases.Usuario;
import gestionBD.GestionUsuarios;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author infdaid2
 */
public class Comunicacion extends Subject implements Runnable{

    private final Socket socket;
    private int codigo;
    private Usuario user;
    private int resultado;
    private Contacto contacto;
    private ObjectOutputStream objFlujoS;
    private String admin;
    private Grupo grupo;
    private int idGrupo;
    private EnvioPrivado envioPrivado;
    private String nuevoNombre;
    private GrupoContacto grupoContacto;

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

    public GrupoContacto getGrupoContacto() {
        return grupoContacto;
    }

    public String getNuevoNombre() {
        return nuevoNombre;
    }

    public Usuario getUser() {
        return user;
    }

    public EnvioPrivado getEnvioPrivado() {
        return envioPrivado;
    }

    public int getIdGrupo() {
        return idGrupo;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public String getAdmin() {
        return admin;
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
            
            while(socket.isConnected())
            {
                switch(codigo = (Integer)objFlujoE.readObject())
                {
                    case CodigoMetodo.REGISTRARSE:
                        System.out.println("Comunicacion: Codigo recibido");
                        this.user = (Usuario)objFlujoE.readObject();
                        System.out.println("Comunicacion: Usuario recibido");
                    /*    IP = (String)objFlujoE.readObject();           
                        System.out.println("Comunicacion: IP leída de flujo");*/
                        this.resultado = GestionUsuarios.registrarse(this.user);
                    /*    if(this.resultado == 0)
                        {
                            GestionConexiones.insertarConexion(new clases.Conexion(this.user.getAlias(), IP));
                        }*/
                        this.notifyObservers();
                        break;
                    case CodigoMetodo.INICIAR_SESION:
                        System.out.println("Comunicacion: Codigo recibido");
                        this.user = (Usuario)objFlujoE.readObject();
                        System.out.println("Comunicacion: Usuario recibido");
                    /*    IP = (String)objFlujoE.readObject();           
                        System.out.println("Comunicacion: IP leída de flujo");*/
                        this.resultado = GestionUsuarios.iniciarSesion(this.user);
                        this.notifyObservers();
                        break;
                    case CodigoMetodo.ELIMINAR_CONTACTO:
                        this.contacto = (Contacto) objFlujoE.readObject();
                        this.notifyObservers();
                        break;
                    case CodigoMetodo.ELIMINAR_GRUPO:
                        this.grupo = (Grupo) objFlujoE.readObject();
                        this.notifyObservers();
                        break;
                    case CodigoMetodo.INSERTAR_CONTACTO:
                        this.contacto = (Contacto) objFlujoE.readObject();
                        this.notifyObservers();
                        break;
                    case CodigoMetodo.INSERTAR_GRUPO:
                        this.grupo = (Grupo) objFlujoE.readObject();
                        this.notifyObservers();
                        break;
                    case CodigoMetodo.LISTAR_CONTACTOS_USUARIO:
                        this.admin = (String) objFlujoE.readObject();
                        this.notifyObservers();
                        break;
                    case CodigoMetodo.LISTAR_CONTACTOS_GRUPO:
                        this.grupo = (Grupo) objFlujoE.readObject();
                        this.notifyObservers();
                        break;
                    case CodigoMetodo.LISTAR_GRUPOS:
                        this.admin = (String) objFlujoE.readObject();
                        this.notifyObservers();
                        break;
                    case CodigoMetodo.MODIFICAR_CONTACTO:
                        this.contacto = (Contacto) objFlujoE.readObject();
                        this.notifyObservers();
                        break;
                    case CodigoMetodo.RECUPERAR_CONTACTO:
                        /*System.out.println("Comunicacion: Código de método leído de flujo");
                        objeto = objFlujoE.readObject();
                        System.out.println("Comunicacion: Objeto leído de flujo");
                        IP = (String)objFlujoE.readObject();           
                        System.out.println("Comunicacion: IP leída de flujo");*/
                        break;
                    case CodigoMetodo.MODIFICAR_GRUPO:
                        this.grupo = (Grupo) objFlujoE.readObject();
                        this.nuevoNombre = (String) objFlujoE.readObject();
                        this.notifyObservers();
                        break;
                    case CodigoMetodo.ENVIAR_MENSAJE_P:
                        this.envioPrivado = (EnvioPrivado) objFlujoE.readObject();
                        this.notifyObservers();
                        break;
                    case CodigoMetodo.INSERTAR_GRUPO_CONTACTO:
                        this.grupo = (Grupo) objFlujoE.readObject();
                        this.contacto = (Contacto) objFlujoE.readObject();
                        this.notifyObservers();
                        break;
                    case CodigoMetodo.ELIMINAR_GRUPO_CONTACTO:
                        this.grupo = (Grupo) objFlujoE.readObject();
                        this.contacto = (Contacto) objFlujoE.readObject();
                        this.notifyObservers();
                        break;
                    case CodigoMetodo.LISTAR_GRUPOS_CONTACTO:
                        this.contacto = (Contacto) objFlujoE.readObject();
                        this.notifyObservers();
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
            ex.printStackTrace();
            this.codigo = CodigoMetodo.DESCONECTARSE;
            this.notifyObservers();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Comunicacion.class.getName()).log(Level.SEVERE, null, ex);
        }/* finally
        {
            
        }*/
    }
    
}
