/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package envio_recepcion;

/**
 *
 * @author ADRIANLC
 */
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class Subject
{
    private CopyOnWriteArrayList<Observer> registeredObservers;

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
        registeredObservers.stream().forEach((observer) -> {
            observer.update(this);
        });
    }
}