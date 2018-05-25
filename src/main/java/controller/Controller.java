package main.java.controller;

import main.java.view.View;

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
    }

    public void notifyAllObservers() {
        for(Observer o : observerList) {
            o.update();
        }
    }

    abstract void addListeners();

    abstract View getView();

    @Override
    abstract public void update();

}
