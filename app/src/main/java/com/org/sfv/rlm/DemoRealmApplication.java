package com.org.sfv.rlm;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by nguyen.quang.tung on 6/9/2016.
 */
public class DemoRealmApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(getApplicationContext()).build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
