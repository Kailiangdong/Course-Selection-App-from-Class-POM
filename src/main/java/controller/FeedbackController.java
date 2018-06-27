package controller;

import net.hockeyapp.javafx.simple.FeedbackManager;
import view.FeedbackDialog;
import view.View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FeedbackController extends Controller {

    private final String HOCKEYAPP_ID = "fc83ec2bc6ce4698ab94bf783146cdca";

    private FeedbackDialog dialog;
    private FeedbackManager feedbackManager;

    public FeedbackController() {
        dialog = new FeedbackDialog();
        feedbackManager = new FeedbackManager(HOCKEYAPP_ID);
        addListeners();
        dialog.open();
    }

    private void sendFeedback() {
        feedbackManager.submitFeedback(System.getProperty("user.name"), null, "Feedback", dialog.getInput());
    }

    @Override
    public void addListeners() {
        dialog.addListenerOnCancel(new ListenerOnCancel());
        dialog.addListenerOnOK(new ListenerOnOK());
    }

    @Override
    public View getView() {
        return dialog;
    }

    @Override
    public void update() {
        // Nothing to update, nothing to subscribe
    }

    class ListenerOnOK implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            sendFeedback();
            dialog.close();
        }
    }

    class ListenerOnCancel implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            dialog.close();
        }
    }
}
