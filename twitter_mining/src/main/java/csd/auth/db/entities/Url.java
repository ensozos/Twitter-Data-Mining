package csd.auth.db.entities;

/**
 * Created by ilias on 16/12/2016.
 */
public class Url extends Entity {
    public static final String TYPE = "Url";

    public Url() {
        super();
        type = TYPE;
    }

    public Url setUrl(String url) {
        attrs.put("url", url);
        return this;
    }

    public static Url make(String url) {
        return (new Url()).setUrl(url);
    }

}
