package csd.auth.db.entities;

/**
 * Created by ilias on 18/12/2016.
 */
public class Tweet extends Entity {

    public static final String TYPE = "Tweet";

    public Tweet() {
        super();
        type = TYPE;
    }

    public Tweet setRetweet(String retweet) {
        attrs.put("tid", retweet);
        return this;
    }

    public static Tweet make(long retweet) {
        return (new Tweet()).setRetweet(String.valueOf(retweet));
    }

}
