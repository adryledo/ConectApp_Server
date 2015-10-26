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

/**
 *
 * @author ADRIANLC
 */
public class Archivo
{
    private String aliasUsuario, aliasContacto, nombre, fecha;

    public Archivo(String aliasUsuario, String aliasContacto, String nombre, String fecha) {
        this.aliasUsuario = aliasUsuario;
        this.aliasContacto = aliasContacto;
        this.nombre = nombre;
        this.fecha = fecha;
    }

    public String getAliasContacto() {
        return aliasContacto;
    }

    public String getAliasUsuario() {
        return aliasUsuario;
    }

    public String getFecha() {
        return fecha;
    }

    public String getNombre() {
        return nombre;
    }
}
