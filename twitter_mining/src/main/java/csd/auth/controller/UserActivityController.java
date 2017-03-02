package csd.auth.controller;

import com.mongodb.DBCursor;
import com.mongodb.util.JSON;
import csd.auth.callbacks.OutputCallback;
import csd.auth.db.CypherBuilder;
import csd.auth.db.MongoController;
import csd.auth.db.Neo4jController;
import csd.auth.utils.Constant;
import twitter4j.JSONArray;
import twitter4j.JSONException;
import twitter4j.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ilias on 6/12/2016.
 */
public class UserActivityController {

    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(UserActivityController.class);

    private MongoController mongoController;
    private Neo4jController neo4jController;
    private OutputCallback outputCallback;

    public UserActivityController(OutputCallback outputCallback) {
        mongoController = new MongoController();
        neo4jController = new Neo4jController();
        this.outputCallback = outputCallback;
    }

    public void initActivity() {

        int count = 1;
        DBCursor cursor = mongoController.getCursor();

        while (cursor.hasNext()) {
            try {
                JSONObject json = new JSONObject(JSON.serialize(cursor.next()));
                parseJson(json);
                count++;

                if (count % 30 == 0) {
                    outputCallback.onOutputResponse("Count " + count);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        neo4jController.closeSession();
    }

    private void parseJson(JSONObject json) {
        try {
            //if you want to use user id for indexing in neo4j
            //Long twitter_id = getTwitterId(json);
            //Long user_id = getUserID(json);

            Long retweet_id = getRetweetId(json);
            String user_name = getUserName(json);
            String timestamp = getTimestamp(json);

            String tweet_text = json.get(Constant.TWEET_TEXT).toString();

            List<String> listHashtags = getHashTagWithRegex(tweet_text); //get hash tags with regular expressions
            List<String> listMentions = getMentionsWithRegex(tweet_text); // get mantions with regular expressions
            List<String> listUrl = getUrlFromEntities(json);


            // for hashtags
            for (int i = 0; i < listHashtags.size(); i++) {
                neo4jController.usedHashTag(user_name, listHashtags.get(i), timestamp);
            }
            // for urls
            for (int i = 0; i < listUrl.size(); i++) {
                neo4jController.usedUrl(user_name, listUrl.get(i), timestamp);
            }

            // for metnions
            for (int i = 0; i < listMentions.size(); i++) {
                neo4jController.mentionedUser(user_name, listMentions.get(i), timestamp);
            }

            // for retweet
            if (retweet_id != null) {
                neo4jController.retweetedTweet(user_name, retweet_id, timestamp);
            }

        } catch (JSONException e) {
            logger.info(e);
            e.printStackTrace();
        }
    }

    private String getRetweetText(JSONObject jsonObject) {
        JSONObject quote = null;
        try {
            quote = jsonObject.getJSONObject(Constant.TWEET_QUOTED_STATUS);
            if (quote != null)
                return quote.getString(Constant.TWEET_TEXT);
        } catch (JSONException e) {
            return null;
        }
        return null;
    }

    private Long getRetweetId(JSONObject jsonObject) {
        JSONObject quote = null;
        try {
            quote = jsonObject.getJSONObject(Constant.TWEET_QUOTED_STATUS);
            if (quote != null)
                return quote.getLong(Constant.TWEET_ID);
        } catch (JSONException e) {
            return null;
        }
        return null;
    }

    private List<Long> getUserMentionedId(JSONObject jsonObject) throws JSONException {

        JSONObject entities = jsonObject.getJSONObject(Constant.TWEET_ENTITIES);

        List<Long> mentionedId = new ArrayList<Long>();
        JSONArray arrayId = entities.getJSONArray(Constant.TWEET_USER_MENTIONED);

        for (int i = 0; i < arrayId.length(); i++) {
            JSONObject obj = arrayId.getJSONObject(i);
            Long expanded_url = obj.getLong(Constant.TWEET_ID);

            if (expanded_url != null)
                mentionedId.add(expanded_url);
        }

        return mentionedId;
    }

    private String getTimestamp(JSONObject jsonObject) throws JSONException {
        return jsonObject.getString(Constant.TWEET_TIMESTAMP);
    }

    private String getUserName(JSONObject jsonObject) throws JSONException {
        return jsonObject.getJSONObject(Constant.TWEET_USER).getString(Constant.TWEET_SCREEN_NAME);
    }

    private long getUserID(JSONObject jsonObject) throws JSONException {

        return jsonObject.getJSONObject(Constant.TWEET_USER).getLong(Constant.TWEET_ID);

    }

    private long getTwitterId(JSONObject jsonObject) throws JSONException {
        return jsonObject.getLong(Constant.TWEET_ID);
    }

    private List<String> getUrlFromEntities(JSONObject jsonObject) throws JSONException {
        // for urls
        JSONObject entities = jsonObject.getJSONObject(Constant.TWEET_ENTITIES);
        List<String> urls = new ArrayList<String>();
        JSONArray arrayUrl = entities.getJSONArray(Constant.TWEET_URL);
        for (int i = 0; i < arrayUrl.length(); i++) {
            JSONObject obj = arrayUrl.getJSONObject(i);
            String expanded_url = obj.getString(Constant.TWEET_EXPANDED_URL);
            if (expanded_url != null)
                urls.add(expanded_url);
        }

        return urls;
    }

    /**
     * Get hashtags from text with regular expressions
     *
     * @param text
     * @return
     */
    private List<String> getHashTagWithRegex(String text) {
        List<String> hashTagsList = new ArrayList<String>();

        Pattern patternHashtag = Pattern.compile(Constant.HASHTAG_REGEX);

        Matcher matcherHashTag = patternHashtag.matcher(text);
        while (matcherHashTag.find()) {
            hashTagsList.add(matcherHashTag.group().trim());
        }

        return hashTagsList;
    }

    /**
     * Get mentions from text with regular expressions
     *
     * @param text
     * @return
     */
    private List<String> getMentionsWithRegex(String text) {
        List<String> mentionsList = new ArrayList<String>();

        Pattern patternMention = Pattern.compile(Constant.MENTION_REGEX);

        Matcher matcherHashTag = patternMention.matcher(text);
        while (matcherHashTag.find()) {
            String username = matcherHashTag.group(1);
            if (username == null)
                username = matcherHashTag.group(2);

            mentionsList.add(username.trim());
        }
        return mentionsList;
    }

    /**
     * First implementation with regex
     * TODO remove that method
     *
     * @param text
     */
    private List<String> regexFromText(String text) {

        List<String> userTokens = new ArrayList<String>();

        String regexMention = "^@\\w+|\\s@\\w+";
        Pattern patternMention = Pattern.compile(regexMention);

        String regexHashTag = "^#\\w+|\\s#\\w+";
        Pattern patternHashtag = Pattern.compile(regexHashTag);

        String regexUrl = "http+://[\\S]+|https+://[\\S]+";
        Pattern patternUrl = Pattern.compile(regexUrl);

        Matcher matcherMention = patternMention.matcher(text);
        while (matcherMention.find()) {
            userTokens.add(matcherMention.group());
        }

        Matcher matcherHashTag = patternHashtag.matcher(text);
        while (matcherHashTag.find()) {
            userTokens.add(matcherHashTag.group());
        }

        Matcher matcherUrl = patternUrl.matcher(text);
        while (matcherUrl.find()) {
            userTokens.add(matcherUrl.group());
        }

        return userTokens;
    }


}