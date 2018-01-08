package com.liashenko.app.controller.manager;

import com.liashenko.app.controller.utils.HttpParser;
import com.liashenko.app.utils.AppProperties;

import java.io.File;
import java.util.HashMap;
import java.util.ResourceBundle;

public class LocaleQueryConf {

    private static String SQL_QUERIES_PATH = AppProperties.getSqlQueryFileDir()
            + File.separator + AppProperties.getSqlQueryFileName();

    private static volatile LocaleQueryConf instance;
    private final HashMap<String, ResourceBundle> resourceBundles;

    private LocaleQueryConf() {
        resourceBundles = new HashMap<>();
    }

    public static LocaleQueryConf getInstance() {
        LocaleQueryConf localeInstance = instance;
        if (localeInstance == null){
            synchronized (LocaleQueryConf.class){
                localeInstance = instance;
                if (localeInstance == null) {
                    instance = localeInstance = new LocaleQueryConf();
                }
            }
        }
        return instance;
    }

    public synchronized ResourceBundle getLocalQueries(String locale) {
        if (resourceBundles.containsKey(locale)) {
            return resourceBundles.get(locale);
        } else {
            resourceBundles.put(locale, ResourceBundle.getBundle(SQL_QUERIES_PATH,
                    HttpParser.parseLocaleFromString(locale)));
            return resourceBundles.get(locale);
        }
    }
}
