package vn.edu.hanu.fitdictionary;

import android.app.Application;

import vn.edu.hanu.fitdictionary.data.FetchUser;

public class FitDictionaryApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FetchUser.initialize();
    }
}
