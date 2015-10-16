/*
 * Copyright (C) 2015 Adrian Ledo
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

/**
 *
 * @author Adri&aacute;n Ledo
 */
public class GrupoContacto implements Serializable
{
    private String admin, nombreGrupo, aliasContacto;

    public GrupoContacto(String admin, String nombreGrupo, String aliasContacto) {
        this.admin = admin;
        this.nombreGrupo = nombreGrupo;
        this.aliasContacto = aliasContacto;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getNombreGrupo() {
        return nombreGrupo;
    }

    public void setNombreGrupo(String nombreGrupo) {
        this.nombreGrupo = nombreGrupo;
    }

    public String getAliasContacto() {
        return aliasContacto;
    }

    public void setAliasContacto(String aliasContacto) {
        this.aliasContacto = aliasContacto;
    }
}
