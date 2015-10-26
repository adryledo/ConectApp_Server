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

import clases.Archivo;
import clases.EnvioPrivado;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
//import gestionBD.GestionConexiones;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author ADRIANLC
 */

public class EnvioArchivo extends Subject implements Runnable {

    private ObjectOutputStream flujoObjetos;
    private DataOutputStream flujoDatos;
    private long tam;
    private boolean iniciado;
    private boolean enviado;
    private int numIter;
    private final String ruta;
    private final EnvioPrivado envPriv;
//    private ArrayList<String> IPs;
    private final Socket socketArchivo;


    public int getNumIter() {
        return numIter;
    }

    public boolean isIniciado() {
        return iniciado;
    }
    
    public boolean isEnviado() {
        return enviado;
    }

    public String getRutaFich() {
        return ruta;
    }

    public EnvioArchivo(Socket socket, String rutaFichero, EnvioPrivado envPriv) {
        this.socketArchivo = socket;
    /*    if((this.IPs = GestionConexiones.getIPs(archivo.getAliasContacto())) == null)
        {
            this.fallo = "El contacto no está conectado";
            notifyObservers();
        }*/
        this.ruta = rutaFichero;
        this.envPriv = envPriv;
    }

    void envioFich() {
        int tamBuf = 256;
        byte buffer[] = new byte[tamBuf];
        int numBytesLeidos = 0;
        FileInputStream ficheroOrigen;
        try {
            ficheroOrigen = new FileInputStream(this.ruta);
            this.numIter = (int) this.tam / tamBuf;
            if (this.tam % tamBuf != 0) {
                this.numIter++;
            }
            this.iniciado = false;
//            notifyObservers();
//---- NOTIFICAR A LA VENTANA EL VALOR DE numIter PARA INICIAR LA BARRA DE PROGRESO ---
            try {
                while (this.numIter > 0) {
                    numBytesLeidos = ficheroOrigen.read(buffer);
                    flujoDatos.write(buffer, 0, numBytesLeidos);
                    this.numIter--;
                    this.iniciado = true;
//                    notifyObservers();
//---- NOTIFICAR A LA VENTANA EL VALOR 1 PARA ACTUALIZAR LA BARRA DE PROGRESO ---
                }
                ficheroOrigen.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("-----***** ERROR RECIBIENDO ***----");
            }
            this.enviado = true;
            notifyObservers();
        } catch (FileNotFoundException e) {
        }
    }

    @Override
    public void run() {    
        OutputStream flujoSalida;
    /*    for(String IP : IPs)
        {*/
            this.iniciado = false;
            this.enviado = false;
            try {
            //    Socket socketEnviar = new Socket(IP, 62003);
                flujoSalida = this.socketArchivo.getOutputStream();
                flujoObjetos = new ObjectOutputStream(flujoSalida);
                flujoDatos = new DataOutputStream(flujoSalida);

                File miFich = new File(ruta);
                this.tam = miFich.length();
    //            String soloNombre = ruta.substring(ruta.lastIndexOf('\\') + 1);
                flujoObjetos.writeLong(this.tam);
                System.out.printf("Tamaño en cliente"+this.tam);
    //            flujo.writeInt(soloNombre.length());
    //            flujo.writeBytes(soloNombre);
    //            flujo.writeUTF(InetAddress.getLocalHost().getHostAddress());
                flujoObjetos.writeObject(this.envPriv);
                System.out.println("EnvioPrivado enviado");
                envioFich();
                System.out.println("Fichero enviado!!!");
            //    socketEnviar.close();
            } catch (UnknownHostException e) {
                System.out.println("Referencia a host no resuelta");
            } catch (IOException e) {
                System.out.println("Error en las comunicacines");
            } catch (SecurityException e) {
                System.out.println("Comunicación no permitida");
            }
    //    }
    }
}
