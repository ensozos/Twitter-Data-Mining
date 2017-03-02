package csd.auth.db;

import com.sun.deploy.util.StringUtils;
import csd.auth.db.entities.*;
import csd.auth.utils.Config;
import org.neo4j.driver.v1.*;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by nikos on 6/12/2016.
 */
public class Neo4jController {

    private static Session session;

    public Neo4jController() {
        if (session == null) {

            session = Config.getNeo4jConfiguration().session();
            System.out.println("pass");
            initDatabase();
        }
    }

    private void initDatabase() {
        ArrayList<String> q = new ArrayList();

        // create indexes
        q.add("CREATE INDEX ON :User(name)");
        q.add("CREATE INDEX ON :Tweet(tid)");
        q.add("CREATE INDEX ON :Hashtag(name)");
        q.add("CREATE INDEX ON :Url(url)");

        for (String query : q) {
            session.run(query);
        }
    }

    public void closeSession() {
        session.close();
    }

    public void usedUrl(String username, String extendedUrl, String timestamp) {
        User user = User.make(username);
        Url url = Url.make(extendedUrl);
        Relationship rel = Relationship.make(Relationship.REL_URL_USED, timestamp);

        CypherBuilder cypher = CypherBuilder.make(user, url, rel);
        String query = cypher.build(false); // set true for profiling

        StatementResult result = session.run(query);
    }

    public void mentionedUser(String username, String mentionedUsername, String timestamp) {
        User user = User.make(username);
        User mentioned = User.make(mentionedUsername); // todo
        Relationship rel = Relationship.make(Relationship.REL_MENTION_USER, timestamp);

        CypherBuilder cypher = CypherBuilder.make(user, mentioned, rel);
        String query = cypher.build();

        session.run(query);
    }

    public void usedHashTag(String username, String htag, String timestamp) {
        User user = User.make(username);
        Hashtag hashtag = Hashtag.make(htag);
        Relationship rel = Relationship.make(Relationship.REL_HASHTAG_USED, timestamp);

        CypherBuilder cypher = CypherBuilder.make(user, hashtag, rel);
        String query = cypher.build();

        session.run(query);
    }


    public void retweetedTweet(String user_name, long retweet_id, String timestamp) {
        User user = User.make(user_name);
        Tweet tweet = Tweet.make(retweet_id);
        Relationship rel = Relationship.make(Relationship.REL_RETWEET_TWEET, timestamp);

        CypherBuilder cypher = CypherBuilder.make(user, tweet, rel);
        String query = cypher.build();

        session.run(query);
    }

    public void countRelationNumber(String nodeA, String nodeB, String rel) {
        // TODO move to cypherbuilder
        String query = String.format("MATCH (p:%1$s)-[r:%3$s]->(h:%2$s)\n" +
                "WITH p,h,count(r) as rels\n" +
                "CREATE (p)-[:%3$s_N{used:rels}]->(h)", nodeA, nodeB, rel);
        System.out.println(query);
        session.run(query);
    }

    public void calcCosineSimilarity(String nodeA, String nodeB, String rel, String simRel, double threshold, boolean alt) {
        String query = String.format(Locale.US, "match (u1:%1$s)-[:%3$s]->(h1:%2$s)\n" +
                "with u1, collect(h1) as hs1\n" +
                "\n" +
                "match (u2:%1$s)-[:%3$s]->(h2:%2$s)\n" +
                "where ID(u2) > ID(u1)\n" +
                "with u1, u2, hs1+collect(h2) as allhs\n" +
                "\n" +
                "match (h:%2$s)\n" +
                "where h in allhs\n" +
                "\n" +
                "optional match (u1)-[r1:%3$s]->(h)\n" +
                "optional match (u2)-[r2:%3$s]->(h)\n" +
                "\n" +
                "WITH SUM(r1.used * r2.used) AS xyDotProduct,\n" +
                "SQRT(REDUCE(xDot = 0.0, a IN COLLECT(r1.used) | xDot + a^2)) AS xLength,\n" +
                "SQRT(REDUCE(yDot = 0.0, b IN COLLECT(r2.used) | yDot + b^2)) AS yLength,\n" +
                "u1, u2\n" +
                "WHERE xyDotProduct > %5$.2f" +
                (alt ? " and u1.name <> u2.name \n" : "\n") +
                "MERGE (u1)-[s:%4$s]-(u2)\n" +
                "SET s.similarity = xyDotProduct / (xLength * yLength)", nodeA, nodeB, rel, simRel, threshold);

        try (Transaction tx = session.beginTransaction()) {
            tx.run(query);

            tx.success();
        }

    }


    public void calcTotalSimilarity () {
            String query = "match (u1:User)\n" +
                    "match (u2:User)\n" +
                    "with u1, u2\n" +
                    "where ID(u2) > ID(u1)\n" +
                    "optional match (u1)-[r]->(u2)\n" +
                    "with SUM(r.similarity) as sim_all, u1, u2\n" +
                    "where sim_all > 0\n" +
                    "MERGE (u1)-[s:TOTAL_SIMILARITY]-(u2)\n" +
                    "SET s.similarity = sim_all/4";

            session.run(query);
        }
}
