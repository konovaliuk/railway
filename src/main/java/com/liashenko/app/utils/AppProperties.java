package com.liashenko.app.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Properties;

public abstract class AppProperties {
    private static final Logger classLogger = LogManager.getLogger(AppProperties.class);

    private static Properties  properties = new Properties();

    public static String getSqlQueryFileName(){
        return properties.getProperty("sql_queries_file_name", "my_sql_queries");
    }

    public static String getSqlQueryFileDir(){
        return properties.getProperty("sql_queries_file_dir", "sql_queries");
    }

    public static String getSaltGenerationAlg(){
        return properties.getProperty("salt_generation_algorithm","SHA1PRNG");
    }

    public static String getPassGenerationAlg(){
        return properties.getProperty("password_generation_algorithm","PBKDF2WithHmacSHA1");
    }

    public static int getPassAlgIterationValue(){
        try {
            return Integer.parseInt(properties.getProperty("password_alg_iteration_val", "1000"));
        }catch (ClassCastException ignore) {
            return 1000;
        }
    }

    public static int getDefaultUsersOnPageCount(){
        try {
            return Integer.parseInt(properties.getProperty("default_users_on_page_count", "5"));
        } catch (ClassCastException ignore) {
            return 5;
        }
    }


    public static String getDefaultLocaleStr(){
        return properties.getProperty("default_locale", "uk_UA");
    }

    public static String getSessionInterval() {
        return properties.getProperty("session_interval", "20");
    }

    public static void loadFilesProperties(String filePath) {
        try (BufferedInputStream fis = new BufferedInputStream(new FileInputStream(filePath));) {
            properties.load(fis);
        } catch (IOException e) {
            classLogger.error("Couldn't read file 'app_settings.properties'",  e);
            throw new RuntimeException("Couldn't read file 'app_settings.properties'");
        }
    }



}
