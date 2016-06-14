package com.org.sfv.rlm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by nguyen.quang.tung on 6/9/2016.
 */
public class DemoRealmObject extends RealmObject {

    @PrimaryKey
    private int id;

    @Required
    private String title;

    @Required
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
