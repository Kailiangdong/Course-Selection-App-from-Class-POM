package controller;

import view.View;

import java.util.LinkedList;
import java.util.List;

public abstract class Controller implements Observer {

    private List<Observer> observerList = new LinkedList<>();

    public void attach(Observer o){
        if(observerList.contains(o)) {
            return;
        }
        observerList.add(o);
    }

    public void detach(Observer o) {
        if(!observerList.contains(o)) {
            throw new RuntimeException(o.toString() + " is not observing " + this.toString());
        }
        observerList.remove(o);
    }

    public void notifyAllObservers() {
        for(Observer o : observerList) {
            o.update();
        }
    }

    public abstract void addListeners();

    public abstract View getView();

    @Override
    public abstract void update();

}
