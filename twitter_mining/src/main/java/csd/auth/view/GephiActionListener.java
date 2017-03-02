package csd.auth.view;

import csd.auth.callbacks.OutputCallback;
import csd.auth.controller.SimilarityController;
import csd.auth.utils.Constant;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by ilias on 10/1/2017.
 */
public class GephiActionListener implements ActionListener, OutputCallback {

    private TwitterClientFrame frame;

    public GephiActionListener(TwitterClientFrame frame) {
        this.frame = frame;
    }

    public void actionPerformed(ActionEvent e) {
        frame.setStatusInfo(Constant.WAITING, Color.ORANGE);
        frame.setConsoleText("Please wait...");



        SwingWorker worker = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                SimilarityController similarityController = new SimilarityController(GephiActionListener.this);
                similarityController.execSimilarityQueries();
                return true;
            }

            @Override
            protected void done() {
                frame.setConsoleText("Done!!!");
                super.done();
            }
        };
        worker.execute();


    }

    @Override
    public void onOutputResponse(String response) {
        frame.setConsoleText(response);
    }

    @Override
    public void oStatusResponse(String label, Color color) {
        frame.setStatusInfo(label, color);
    }
}
