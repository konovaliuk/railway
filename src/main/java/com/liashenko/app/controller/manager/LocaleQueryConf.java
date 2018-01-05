package com.liashenko.app.controller.manager;

import com.liashenko.app.controller.utils.HttpParser;
import com.liashenko.app.utils.AppProperties;

import java.io.File;
import java.util.HashMap;
import java.util.ResourceBundle;


//is it thread safe?????
public class LocaleQueryConf {

    private static String SQL_QUERIES_PATH = AppProperties.getSqlQueryFileDir()
            + File.separator + AppProperties.getSqlQueryFileName();

    private static LocaleQueryConf instance = null;
    private final HashMap<String, ResourceBundle > resourceBundles;

    private LocaleQueryConf(){
        resourceBundles = new HashMap<>();
    }

    public static LocaleQueryConf getInstance(){
        if (instance == null) {
            instance = new LocaleQueryConf();
        }
        return instance;
    }

    public ResourceBundle getLocalQueries(String locale){
        if (resourceBundles.containsKey(locale)) {
            return resourceBundles.get(locale);
        } else {
            resourceBundles.put(locale, ResourceBundle.getBundle(SQL_QUERIES_PATH,
                    HttpParser.parseLocaleFromString(locale)));
            return resourceBundles.get(locale);
        }
    }
}
