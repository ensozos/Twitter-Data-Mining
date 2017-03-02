package csd.auth.view;

import csd.auth.callbacks.OutputCallback;
import csd.auth.callbacks.TrendCallback;
import csd.auth.controller.StreamingController;
import csd.auth.controller.TrendsTask;
import csd.auth.utils.Constant;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;

/**
 * Created by ilias on 6/1/2017.
 */
public class MongoActionListener implements ActionListener, TrendCallback, OutputCallback {

    private TwitterClientFrame frame;

    /**
     * Threads period for every request
     */
    private static final long TRENDS_PERIOD = 2 * 3600 * 1000;

    /**
     * Timer for scheduling trendsTask
     */
    private static Timer timer;

    public MongoActionListener(TwitterClientFrame frame) {
        this.frame = frame;
    }

    /**
     * when the button pressed
     *
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
        frame.setStatusInfo(Constant.WAITING,Color.ORANGE);
        frame.setConsoleText("Please wait...");
        StreamingController streamingController = new StreamingController(this);
        initTrends(streamingController);
    }

    /**
     * Initialize treands tasks and schedule it
     * to run  TRENDS_PERIOD
     */
    private void initTrends(StreamingController streamingController) {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TrendsTask(streamingController), 0, TRENDS_PERIOD);
    }

    /**
     * Print to console response from trend task
     *
     * @param trend
     */
    public void onTrendResponse(String trend) {
        frame.setConsoleText(trend);
    }

    /**
     * Print to console response from streamming
     *
     * @param response
     */
    public void onOutputResponse(String response) {
        frame.setConsoleText(response);
    }

    public void oStatusResponse(String label,Color color) {
        frame.setStatusInfo(label,color);
    }
}
