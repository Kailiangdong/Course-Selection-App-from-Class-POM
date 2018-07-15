package controller;

import SQLiteManager.SQLiteManager;
import backend.DBConverter;
import backend.HTTPAnswer;
import backend.HTTPClient;
import controller.login.LoginController;
import org.json.JSONObject;
import view.InputDialog;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SyncController {

    private SQLiteManager sqLiteManager;
    private LoginController loginController;
    private InputDialog inputDialog;

    private Type type;

    enum Type {PUSH, PULL}

    public SyncController(SQLiteManager sqLiteManager, LoginController loginController, Type type) {
        this.sqLiteManager = sqLiteManager;
        this.loginController = loginController;
        this.type = type;
    }

    private void validateInput() {
        String input = inputDialog.getInput();
        if (input.matches("\\b((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)(\\.|$)){4}\\b|localhost")) {
            inputDialog.enableOKButton();
        } else {
            inputDialog.disableOKButton();
        }
    }

    private boolean syncDatabase() {
        String ipAddress = inputDialog.getInput();
        HTTPClient httpClient = new HTTPClient(ipAddress);
        if (type == Type.PULL) {
            HTTPAnswer answer = httpClient.httpRequest(HTTPClient.HTTPRequestType.GET, "");
            if (!answer.isHttpRequestSuccessful()) {
                return false;
            }
            try {
                JSONObject database = new JSONObject(answer.getAnswer());
                DBConverter dbConverter = new DBConverter(sqLiteManager);
                return dbConverter.deserializeDatabase(database);
            } catch (Exception e) {
                System.out.println("Pulled file could not be parsed");
                return false;
            }
        } else if (type == Type.PUSH) {
            DBConverter dbConverter = new DBConverter(sqLiteManager);
            JSONObject database = dbConverter.serializeDatabase();
            HTTPAnswer answer = httpClient.httpRequest(HTTPClient.HTTPRequestType.POST, database.toString());
            return answer.isHttpRequestSuccessful();
        } else {
            throw new RuntimeException("Unsupported sychronization type" + type.toString());
        }
    }

    public void setupConnection() {
        inputDialog = new InputDialog("Enter IP address", "Connect");
        inputDialog.addListenerOnCancel(new ListenerOnCancel());
        inputDialog.addListenerOnOK(new ListenerOnOK());
        inputDialog.addUserDocumentListener(new InputListener());
        inputDialog.disableOKButton();
        inputDialog.open();
    }

    class InputListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            validateInput();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            validateInput();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            validateInput();
        }
    }

    class ListenerOnOK implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (syncDatabase()) {
                loginController.logout();
                JOptionPane.showMessageDialog(null, "Successfully completed. Please login again.", "Sync: " + type.toString(), JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Errors occurred. Please try again.", "Sync: " + type.toString(), JOptionPane.INFORMATION_MESSAGE);
            }
            inputDialog.close();
        }
    }

    class ListenerOnCancel implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            inputDialog.close();
        }
    }
}
