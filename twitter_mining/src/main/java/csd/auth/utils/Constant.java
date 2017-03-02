package csd.auth.utils;

/**
 * Created by ilias on 17/12/2016.
 */
public class Constant {

    //For twitter json
    public static final String TWEET_TEXT = "text";
    public static final String TWEET_QUOTED_STATUS = "quoted_status";
    public static final String TWEET_ID = "id";
    public static final String TWEET_TIMESTAMP = "timestamp_ms";
    public static final String TWEET_USER = "user";
    public static final String TWEET_SCREEN_NAME = "screen_name";
    public static final String TWEET_ENTITIES = "entities";
    public static final String TWEET_URL = "urls";
    public static final String TWEET_EXPANDED_URL = "expanded_url";
    public static final String TWEET_USER_MENTIONED = "user_mentions";

    //regular expressions
    public static final String MENTION_REGEX = "^@(\\w+)|\\s@(\\w+)";
    public static final String HASHTAG_REGEX = "^#\\w+|\\s#\\w+";
    public static final String URL_REGEX = "http+://[\\S]+|https+://[\\S]+";

    public static final String WAITING = "WAITING";
    public static final String RUNNING = "RUNNING";
}
