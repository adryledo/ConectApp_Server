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
 * @author Adrian Ledo
 */
public class RecepcionArchivo extends Subject implements Runnable {

    private ObjectInputStream flujoObjetos;
    private DataInputStream flujoDatos;
    private long tam;
    private String rutaArchivo;
    private boolean iniciado, recibido, aceptacion;
    private int numIter;
    private EnvioPrivado envPriv;
    private final Socket socketArchivo;
    private Usuario usuario;

    public RecepcionArchivo(Socket socketArchivo) {
        this.iniciado = false;
        this.recibido = false;
        this.aceptacion = false;
        this.socketArchivo = socketArchivo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Socket getSocketArchivo() {
        return socketArchivo;
    }

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
            FileOutputStream ficheroDestino = new FileOutputStream(this.rutaArchivo);
            this.numIter = (int) this.tam / tamBuf;
            if (this.tam % tamBuf != 0) {
                this.numIter++;
            }
            this.iniciado = true;
            try {
                while (this.numIter > 0) {
                    numBytesLeidos = this.flujoDatos.read(buffer);
                    ficheroDestino.write(buffer, 0, numBytesLeidos);
                    this.numIter--;
                }
                System.out.println("Fichero recibido");
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
        InputStream flujoLectura;
        try {
            flujoLectura = this.socketArchivo.getInputStream();
            flujoObjetos = new ObjectInputStream(flujoLectura);
            flujoDatos = new DataInputStream(flujoLectura);

            this.usuario = (Usuario) flujoObjetos.readObject();
            while(this.socketArchivo.isConnected())
            {
                this.envPriv = (EnvioPrivado) this.flujoObjetos.readObject();
                this.tam = flujoDatos.readLong();
                recepcionFichero();
                System.out.println("Fichero recibido");
            }
        } catch (IOException | SecurityException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RecepcionArchivo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}