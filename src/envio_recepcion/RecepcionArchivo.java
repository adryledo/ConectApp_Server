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

import clases.EnvioPrivado;
import clases.Usuario;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ADRIANLC
 */
public class RecepcionArchivo extends Subject implements Runnable {

    private ObjectInputStream flujoObjetos;
    private DataInputStream flujoDatos;
    private long tam;
    private String rutaArchivo;
    private boolean iniciado, recibido, aceptacion;
    private int numIter;
    //private String IP;
    private EnvioPrivado envPriv;
//    private Usuario usuario;
//    private String aliasContacto;
    private final Socket socketArchivo;
    private Usuario usuario;

    public RecepcionArchivo(Socket socketArchivo) {
        this.iniciado = false;
        this.recibido = false;
        this.aceptacion = false;
        this.socketArchivo = socketArchivo;
    }

    /*public String getIP() {
        return IP;
    }*/

    public Usuario getUsuario() {
        return usuario;
    }

    public Socket getSocketArchivo() {
        return socketArchivo;
    }

/*    public String getAliasContacto() {
        return aliasContacto;
    }*/

    public int getNumIter() {
        return numIter;
    }

    public String getRutaArchivo() {
        return rutaArchivo;
    }

    public boolean isIniciado() {
        return iniciado;
    }

    public boolean isRecibido() {
        return recibido;
    }
    
    public boolean isAceptacion() {
        return aceptacion;
    }

    public void setAceptacion(boolean aceptacion) {
        this.aceptacion = aceptacion;
    }
    
    public synchronized void detener() {
        try {
            wait();
        } catch (InterruptedException e) {
        }
    }
    
    public synchronized void despertar() {
        notifyAll();
    }

    public EnvioPrivado getEnvPriv() {
        return envPriv;
    }

    public void recepcionFichero() {
        int tamBuf = 256;
        byte buffer[] = new byte[tamBuf];
        int numBytesLeidos = 0;
        try {
            this.rutaArchivo = System.getProperty("java.io.tmpdir").concat("/"+this.envPriv.getContenido());
        //    this.rutaArchivo = System.getProperty("java.io.tmpdir").concat("/"+this.rutaArchivo);
//            String nomCom = directorio.concat("/"+rutaArchivo);
            FileOutputStream ficheroDestino = new FileOutputStream(this.rutaArchivo);
            this.numIter = (int) this.tam / tamBuf;
            if (this.tam % tamBuf != 0) {
                this.numIter++;
            }
            
        //    notifyObservers();
            this.iniciado = true;
//---- NOTIFICAR A LA VENTANA EL VALOR DE numIter PARA INICIAR LA BARRA DE PROGRESO ---
            try {
                while (this.numIter > 0) {
                    numBytesLeidos = this.flujoDatos.read(buffer);
                    ficheroDestino.write(buffer, 0, numBytesLeidos);
                //    this.flujo.readFully(buffer);
                //    ficheroDestino.write(buffer);
                    this.numIter--;
                    System.out.println("numIter="+this.numIter);
                //    notifyObservers();
//---- NOTIFICAR A LA VENTANA EL VALOR 1 PARA ACTUALIZAR LA BARRA DE PROGRESO ---
                }
                System.out.println("FIN");
                ficheroDestino.close();
                this.recibido = true;
            } catch (IOException e) {
            }
            
            notifyObservers();
        } catch (FileNotFoundException e) {
        }
    }

    @Override
    public void run() {
        //byte[] mensaje;
        InputStream flujoLectura;
    //    Socket comunicaCliente;
    //    ServerSocket socketServidor;
        //int i;
        try {
            flujoLectura = this.socketArchivo.getInputStream();
            flujoObjetos = new ObjectInputStream(flujoLectura);
            flujoDatos = new DataInputStream(flujoLectura);

            this.usuario = (Usuario) flujoObjetos.readObject();
            while(this.socketArchivo.isConnected())
            {
            //    socketServidor = new ServerSocket(62005);
            //    comunicaCliente = socketServidor.accept();
            //    this.usuario = (Usuario) flujo.readUnshared();
                
            /*    int tamNombre = flujoDatos.readInt();
                byte[] contenido = new byte[tamNombre];
                flujoDatos.readFully(contenido);
//                this.IP = flujo.readUTF();
                this.rutaArchivo = new String(contenido);*/
                
            /*    int tamAlias = flujo.readInt();
                byte alias[] = new byte[tamAlias];
                flujo.readFully(alias);
                this.aliasContacto = new String(alias);*/
            //    this.envPriv = (EnvioPrivado) flujo.readObject();
                this.envPriv = (EnvioPrivado) this.flujoObjetos.readObject();
                System.out.println("EnvioPrivado recibido");
            //    notifyObservers();
            /*    while (!this.isAceptacion()) {
                    this.detener();
                }*/
                
                this.tam = flujoDatos.readLong();
                System.out.println("Tamaño de archivo en servidor: "+this.tam);
                recepcionFichero();
                System.out.println("Fichero recibido");
            //    comunicaCliente.close();
            //    socketServidor.close();
            }
        } catch (IOException e) {
        } catch (SecurityException e) {
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RecepcionArchivo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

/*    public Usuario getUsuario() {
        return this.usuario;
    }*/
}