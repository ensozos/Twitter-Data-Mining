package csd.auth.utils;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by ilias on 30/10/2016.
 */
public class Config {

    //Twitter Api keys
    public static final String CONSUMER_KEY = "jUwsRl18RcARpfDl6DLnX2XYP";
    public static final String CONSUMER_SECRET = "oPCzXVDLJ6xdoVVmx5dKqWVAfReAkYitsqhJBPPJzj3w4jJdcx";
    public static final String ACCESS_TOKEN = "792368851296092160-dgxpRVl8xioS60KqYZGpyWtP1RBlfV3";
    public static final String ACCESS_TOKEN_SECRET = "ba4M0rXn2xcqwfDbdzOT4kfl3k3wDFIYCJ20f6rjpTaJE";

    //MongoDB
    private static final String DATABASE_HOST = "localhost";
    private static final int DATABASE_PORT = 27017;
    private static final String DATABASE_NAME = "twitter-client";

    //Neo4j
    private static final String DATABASE_HOST_NEO4J = "bolt://localhost";
    private static final String DATABASE_NAME_NEO4J = "neo4j";
    private static final String DATABASE_PSWD_NEO4J = "pass";

    /**
     * get configuration for twitter4j
     *
     * @return
     */
    public static twitter4j.conf.Configuration getTwitterConfiguration() {
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        return configurationBuilder.setDebugEnabled(false).setJSONStoreEnabled(true).
                setOAuthConsumerKey(CONSUMER_KEY).
                setOAuthConsumerSecret(CONSUMER_SECRET).
                setOAuthAccessToken(ACCESS_TOKEN).
                setOAuthAccessTokenSecret(ACCESS_TOKEN_SECRET).
                build();
    }

    public static Driver getNeo4jConfiguration() {

        return GraphDatabase.driver(DATABASE_HOST_NEO4J, AuthTokens.basic(DATABASE_NAME_NEO4J, DATABASE_PSWD_NEO4J));

    }

    public static DB setMongo() {
        return new MongoClient(DATABASE_HOST, DATABASE_PORT).getDB(DATABASE_NAME);
    }


}
