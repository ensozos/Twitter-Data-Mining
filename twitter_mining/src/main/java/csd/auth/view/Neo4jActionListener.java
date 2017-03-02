package csd.auth.view;

import csd.auth.callbacks.OutputCallback;
import csd.auth.controller.UserActivityController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by ilias on 6/1/2017.
 */
public class Neo4jActionListener implements ActionListener, OutputCallback {

    private TwitterClientFrame frame;

    public Neo4jActionListener(TwitterClientFrame frame) {
        this.frame = frame;
    }

    public void onOutputResponse(String response) {
        frame.setConsoleText(response);
    }

    public void oStatusResponse(String label, Color color) {
        frame.setStatusInfo(label,color);
    }

    public void actionPerformed(ActionEvent e) {

        frame.setConsoleText("Please wait...");


        SwingWorker worker = new SwingWorker<Boolean,Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                UserActivityController userActivityController = new UserActivityController(Neo4jActionListener.this);
                userActivityController.initActivity();
                return true;
            }

            @Override
            protected void done() {
                frame.setConsoleText("Done!!!");
                super.done();
            }
        };
        worker.execute();


        //int tweets = 5418;
        //System.out.println("Tweets/sec   -> "+ tweets*1000/elapsedTime); // n tweets * 1000 for ms
    }
}
