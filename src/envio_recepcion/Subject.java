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

/**
 *
 * @author Adrian Ledo
 */
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class Subject
{
    private final CopyOnWriteArrayList<Observer> registeredObservers;

    public Subject() {
        this.registeredObservers = new CopyOnWriteArrayList<>();
    }

    public void registerObserver(Observer observer) {
        this.registeredObservers.add(observer);
    }

    public void unregisterObserver(Observer observer) {
        this.registeredObservers.remove(observer);
    }

    protected void notifyObservers() {
        for(Observer observer : registeredObservers)
        {
            observer.update(this);
        }
    }
}