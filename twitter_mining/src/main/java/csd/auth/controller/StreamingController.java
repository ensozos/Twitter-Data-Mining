package csd.auth.controller;

import csd.auth.callbacks.OutputCallback;
import csd.auth.callbacks.TrendCallback;
import csd.auth.db.MongoController;
import csd.auth.utils.Config;
import csd.auth.utils.Constant;
import twitter4j.*;

import java.awt.*;


/**
 * Created by ilias on 30/10/2016.
 * TODO allakse ton donald line 72!!!!
 */
public class StreamingController implements TrendCallback {

    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(StreamingController.class);

    private static int  SLEEP_TIMER = 3*60*1000;

    /**
     *  Flag for restart
     */
    private static boolean SLEEPING = false;
    /**
     * twitterStream for streaming tweets
     */
    private TwitterStream twitterStream;

    /**
     * Controller for mongoDB
     */
    private MongoController mongoController;

    /**
     * var for random subject( trend )
     */
    private String trend;

    /**
     * listener for twitter4j
     */
    private StatusListener listener;

    /**
     * callback for streaming
     */
    private OutputCallback outputCallback;

    public StreamingController(OutputCallback outputCallback) {

        this.outputCallback = outputCallback;
        TwitterStreamFactory factory = new TwitterStreamFactory(Config.getTwitterConfiguration());
        mongoController = new MongoController();
        twitterStream = factory.getInstance();

    }

    public void executeStream() {

        outputCallback.oStatusResponse(Constant.RUNNING, Color.GREEN);//set status label running

        listener = new StatusListener() {
            public void onStatus(Status status) {
                mongoController.saveCollection(TwitterObjectFactory.getRawJSON(status));
                outputCallback.onOutputResponse("status = [" + status.getText() + "]");
            }

            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {/* empty */}
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {/* empty */}
            public void onScrubGeo(long userId, long upToStatusId) {/* empty */}
            public void onStallWarning(StallWarning warning) {/* empty */}

            public void onException(Exception ex) {
                outputCallback.onOutputResponse(ex.getStackTrace().toString());
                outputCallback.oStatusResponse(Constant.RUNNING, Color.RED);//set status label running
                restart();
            }
        };


        twitterStream.addListener(listener);
        twitterStream.filter("Donald Trump");


    }

    private void restart() {
        try {
            SLEEPING = true;
            twitterStream.shutdown();
            twitterStream.clearListeners();
            listener = null;
            Thread.currentThread().sleep(SLEEP_TIMER);
            SLEEPING = false;
            executeStream();
        } catch (InterruptedException e) {
            outputCallback.onOutputResponse(e.getStackTrace().toString());
        }
    }




    /**
     * The callback from TrendsTask
     *
     * @param trend
     * @return
     */
    public void onTrendResponse(String trend) {
        this.trend = trend;
        logger.info("Current trend=[" + trend + "]");

        if (SLEEPING)
            return;

        if (listener == null)
            executeStream();
    }


}
