package csd.auth.db.entities;

import java.util.HashMap;

/**
 * Created by nikos on 14/12/2016.
 */
public class Hashtag extends Entity {
    public static final String TYPE = "Hashtag";

    public Hashtag() {
        super();
        type = TYPE;
    }

    public Hashtag setName(String name) {
        attrs.put("name", name);
        return this;
    }

    public static Hashtag make(String name) {
        return (new Hashtag()).setName(name);
    }
}
