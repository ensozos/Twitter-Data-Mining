package csd.auth.db.entities;

/**
 * Created by nikos on 14/12/2016.
 */
public class User extends Entity {
    public User() {
        super();
        type = "User";
    }

    public User setName(String name) {
        attrs.put("name", name);
        return this;
    }

    public static User make(String name) {
        return (new User()).setName(name);
    }
}
