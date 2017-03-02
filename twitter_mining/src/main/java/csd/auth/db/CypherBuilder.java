package csd.auth.db;

import csd.auth.db.entities.Entity;
import csd.auth.db.entities.Relationship;

import java.util.HashMap;

/**
 * Created by ria on 12/14/16.
 */
public class CypherBuilder {
    public static final String REL_HASHTAG_USED = "HASHTAG_USED";
    public static final String REL_URL_USED = "URL_USED";
    public static final String REL_MENTION_USER = "MENTION_USER";
    public static final String REL_RETWEET_TWEET = "RETWEED_TWEET";

    private Entity a;
    private Entity b;
    private Relationship relation;

    public static CypherBuilder make(Entity a, Entity b, Relationship relation) {
        return (new CypherBuilder()).setEntities(a, b).setRelation(relation);
    }

    private CypherBuilder setEntities(Entity a, Entity b) {
        this.a = a;
        this.b = b;

        return this;
    }

    public CypherBuilder setRelation(Relationship relation) {
        this.relation = relation;

        return this;
    }

    public Relationship getRelation() {
        return relation;
    }

    public String build() {
        return build(false);
    }

    public String build(boolean prof) {
        String attrsA = attrsToString(a.getAttrs());
        String attrsB = attrsToString(b.getAttrs());

        String query = prof ? "PROFILE ": "";
        query += mergeEntity("a", a.getType(), attrsA);
        query += mergeEntity("b", b.getType(), attrsB);
        query += mergeRelation();

        return query;
    }

    private String mergeRelation() {
        String relAttrs = attrsToString(relation.getAttrs());
        return String.format("CREATE (a)-[:%s %s]->(b)", relation.getRelation(), relAttrs);
    }

    private String attrsToString(HashMap<String, String> attrs) {
        String str = "{";
        for (String key : attrs.keySet()) {
            str += String.format("%s:'%s', ", key, attrs.get(key));
        }

        return str.substring(0, str.length() - 2) + "}";
    }

    private String mergeEntity(String var, String type, String attrs) {
        return String.format("MERGE (%s:%s %s) ", var, type, attrs);
    }
}
