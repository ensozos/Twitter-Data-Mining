package csd.auth;

import csd.auth.controller.SimilarityController;
import csd.auth.controller.StreamingController;
import csd.auth.controller.TrendsTask;
import csd.auth.controller.UserActivityController;
import csd.auth.db.Neo4jController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Timer;

import csd.auth.view.TwitterClientFrame;
import org.neo4j.driver.v1.*;

import javax.swing.*;


/**
 * Created by ilias on 29/10/2016.
 */
public class TwitterClient implements Runnable {

    public void run() {
        new TwitterClientFrame();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new TwitterClient());
    }
}

