package csd.auth.db.entities;

import java.util.HashMap;

/**
 * Created by nikos on 14/12/2016.
 */
public class Entity {
    protected String type;
    protected HashMap<String, String> attrs;

    public Entity() {
        attrs = new HashMap<String, String>();
    }

    public String getType() {
        return type;
    }

    public HashMap<String, String> getAttrs() {
        return attrs;
    }
}
