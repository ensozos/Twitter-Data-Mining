package csd.auth.db.entities;

/**
 * Created by nikos on 18/12/2016.
 */
public class Relationship extends Entity {
    public static final String REL_HASHTAG_USED = "HASHTAG_USED";
    public static final String REL_URL_USED = "URL_USED";
    public static final String REL_MENTION_USER = "MENTION_USER";
    public static final String REL_RETWEET_TWEET = "RETWEED_TWEET";

    public Relationship() {
        super();
    }

    public Relationship setRelation(String relation) {
        attrs.put("relation", relation);
        return this;
    }

    public Relationship setTimestamp(String timestamp) {
        attrs.put("timestamp", timestamp);
        return this;
    }

    public String getRelation() {
        return attrs.get("relation");
    }

    public static Relationship make(String relation, String timestamp) {
       return (new Relationship()).setRelation(relation).setTimestamp(timestamp);
    }
}
