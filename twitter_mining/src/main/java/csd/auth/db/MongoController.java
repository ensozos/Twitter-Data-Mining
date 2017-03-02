package csd.auth.db;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import csd.auth.utils.Config;

/**
 * Created by ilias on 2/11/2016.
 */
public class MongoController {

    private DB mongoDatabase;
    private DBCollection collection;

    public MongoController() {
        mongoDatabase = Config.setMongo();
        collection = mongoDatabase.getCollection("twitter-collection");
    }

    public void saveCollection(String tweet) {
        DBObject dbObject = (DBObject) JSON.parse(tweet);
        collection.insert(dbObject);
    }


    public DBCursor getCursor() {
        return collection.find();
    }

}
