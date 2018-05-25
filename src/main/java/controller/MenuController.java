package main.java.controller;

import main.java.view.MenuView;
import main.java.view.View;
import main.java.SQLiteManager.SQLiteManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MenuController extends Controller {

    private MenuView view;

    private SQLiteManager sqLiteManager;

    public MenuController(SQLiteManager sqLiteManager) {
        view = new MenuView();
        this.sqLiteManager = sqLiteManager;
        addListeners();
    }

    @Override
    void addListeners() {
        view.setUpdateStateListener(new UpdateStateListener());
    }

    public ArrayList<String> getActiveChairNames() {
        // TODO: replace mockup list
        ArrayList<String> activeChairNames = new ArrayList<>();
        activeChairNames.add("CHAIR CS 1");
        activeChairNames.add("CHAIR CS 2");
        activeChairNames.add("CHAIR MGMT 1");
        activeChairNames.add("CHAIR MGMT 2");
        return activeChairNames;
    }

    public ArrayList<String> getActiveColNames() {
        // TODO: replace mockup list
        ArrayList<String> activeColNames = new ArrayList<>();
        activeColNames.add("ID");
        activeColNames.add("TITLE");
        activeColNames.add("CHAIR");
        return activeColNames;
    }

    public String getActiveStudentName() {
        // TODO: replace mockup value
        return "Stefan";
    }

    class UpdateStateListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            notifyAllObservers();
        }
    }

    @Override
    public View getView() {
        return view;
    }

    @Override
    public void update() {
        // nothing to update, will never subscribe to anything
    }

}
