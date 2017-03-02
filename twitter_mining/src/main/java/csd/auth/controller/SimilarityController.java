package csd.auth.controller;

import csd.auth.callbacks.OutputCallback;
import csd.auth.db.MongoController;
import csd.auth.db.Neo4jController;
import csd.auth.db.entities.Relationship;
import csd.auth.utils.Constant;
import csd.auth.view.GephiActionListener;

import javax.management.relation.RelationNotification;
import java.awt.*;

/**
 * Created by nikos on 7/1/2017.
 */
public class SimilarityController {

    private Neo4jController neo4jController;
    private OutputCallback outputCallback;

    public SimilarityController(OutputCallback outputCallback) {
        this.outputCallback = outputCallback;
        neo4jController = new Neo4jController();
    }

    public void execSimilarityQueries() {
        outputCallback.oStatusResponse(Constant.RUNNING, Color.GREEN);

        execHashtagSimilarityQueries();
        execUrlSimilarityQueries();
        execRetweetSimilarityQueries();
        execMentionSimilarityQueries();

        execTotalSimilarityQuery();

        neo4jController.closeSession();
    }

    private void execHashtagSimilarityQueries() {
        neo4jController.countRelationNumber("User", "Hashtag", Relationship.REL_HASHTAG_USED);
        neo4jController.calcCosineSimilarity("User", "Hashtag", Relationship.REL_HASHTAG_USED + "_N", "HASHTAG_SIMILARITY", 0.2, false);

        outputCallback.onOutputResponse("HashTags similarity craeted!");
        // todo delete unused relationships...
    }

    private void execUrlSimilarityQueries() {
        neo4jController.countRelationNumber("User", "Url", Relationship.REL_URL_USED);
        neo4jController.calcCosineSimilarity("User", "Url", Relationship.REL_URL_USED + "_N", "URL_SIMILARITY", 0.2, false);

        outputCallback.onOutputResponse("Url similarity craeted!");

    }

    private void execRetweetSimilarityQueries() {
        neo4jController.countRelationNumber("User", "Tweet", Relationship.REL_RETWEET_TWEET);
        neo4jController.calcCosineSimilarity("User", "Tweet", Relationship.REL_RETWEET_TWEET + "_N", "RETWEET_SIMILARITY", 0.2, false);

        outputCallback.onOutputResponse("Retweet similarity craeted!");
    }

    private void execMentionSimilarityQueries() {
        neo4jController.countRelationNumber("User", "User", Relationship.REL_MENTION_USER);
        neo4jController.calcCosineSimilarity("User", "User", Relationship.REL_MENTION_USER + "_N", "MENTION_SIMILARITY", 0.2, true);

        outputCallback.onOutputResponse("Mention similarity craeted!");
    }

    private void execTotalSimilarityQuery() {
        neo4jController.calcTotalSimilarity();

        outputCallback.onOutputResponse("Total similarity craeted!");
    }
}
