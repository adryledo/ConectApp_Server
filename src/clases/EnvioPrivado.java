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

package clases;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 *
 * @author Adri&aacute;n Ledo
 */
public class EnvioPrivado implements Serializable {
    
    private String remitente;
    private String destinatario;
    private Timestamp fechaHora;
    private String contenido;
    public enum Enum_tipo {MENSAJE, ARCHIVO};
    public enum Enum_estado {EN_SERVIDOR, ENVIADO};
    private Enum_tipo tipo;
    private Enum_estado estado;

    public EnvioPrivado() {
        //this.tipo = Enum_tipo.MENSAJE;
        this.estado = Enum_estado.EN_SERVIDOR;
    }

    public EnvioPrivado(String remitente, String destinatario, Timestamp fechaHora, String contenido, Enum_tipo tipo) {
        this.remitente = remitente;
        this.destinatario = destinatario;
        this.fechaHora = fechaHora;
        this.contenido = contenido;
        this.tipo = tipo;
        this.estado = Enum_estado.EN_SERVIDOR;
    }

    public EnvioPrivado(String remitente, String destinatario, Timestamp fechaHora, String contenido, Enum_tipo tipo, Enum_estado estado) {
        this.remitente = remitente;
        this.destinatario = destinatario;
        this.fechaHora = fechaHora;
        this.contenido = contenido;
        this.tipo = tipo;
        this.estado = estado;
    }

    public Enum_estado getEstado() {
        return estado;
    }

    public void setEstado(Enum_estado estado) {
        this.estado = estado;
    }

    public Enum_tipo getTipo() {
        return this.tipo;
    }

    public void setTipo(Enum_tipo tipo) {
        this.tipo = tipo;
    }


    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }


    public Timestamp getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Timestamp fechaHora) {
        this.fechaHora = fechaHora;
    }


    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }


    public String getRemitente() {
        return remitente;
    }

    public void setRemitente(String remitente) {
        this.remitente = remitente;
    }
}
