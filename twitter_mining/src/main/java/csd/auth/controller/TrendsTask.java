package csd.auth.controller;


import csd.auth.callbacks.TrendCallback;
import csd.auth.utils.Config;

import twitter4j.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TimerTask;


/**
 * Created by ilias on 29/10/2016.
 *
 */
public class TrendsTask extends TimerTask {

    private Twitter twitter;
    private TrendCallback trendCallback;


    public TrendsTask(TrendCallback trendCallback) {

        TwitterFactory twitterFactory = new TwitterFactory(Config.getTwitterConfiguration());
        twitter = twitterFactory.getInstance();
        this.trendCallback = trendCallback;

    }

    public void run() {
        executeTrends();
    }

    /**
     * execute request to get top ten
     * trends of twitter. Return with
     * callback a random trends from list
     */
    private void executeTrends() {
        ArrayList<String> listOfTrends = new ArrayList<String>();
        try {
//            Trends trends = twitter.getPlaceTrends(23424833);//greece
            Trends trends = twitter.getPlaceTrends(	23424977);//usa
            //String json = TwitterObjectFactory.getRawJSON(trends); //extract json from trends
            for (int i = 0; i < trends.getTrends().length; i++) {
                listOfTrends.add(trends.getTrends()[i].getName());
            }
            trendCallback.onTrendResponse(getTopTrend(listOfTrends));
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }

    /**
     * get the first trend from list
     * @param list
     * @return
     */
    private String getTopTrend(List<String> list){
        return list.get(0);
    }

    /**
     * Get a random trends from list
     *
     * @param list
     * @return string
     */
    private String getRandomTrend(List<String> list) {
        Random random = new Random();
        return list.get(random.nextInt(list.size()));
    }

}
