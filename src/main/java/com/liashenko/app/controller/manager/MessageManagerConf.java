package com.liashenko.app.controller.manager;
import java.util.ResourceBundle;

public class MessageManagerConf {
     private static MessageManagerConf instance;
     private ResourceBundle resourceBundle;
     //класс извлекает информацию из файла messages.properties
     private static final String BUNDLE_NAME = "messages";

     public static final String LOGIN_ERROR_MESSAGE = "LOGIN_ERROR_MESSAGE";
     public static final String SERVLET_EXCEPTION_ERROR_MESSAGE = "SERVLET_EXCEPTION_ERROR_MESSAGE";
     public static final String IO_EXCEPTION_ERROR_MESSAGE="IO_EXCEPTION_ERROR_MESSAGE";

     public static MessageManagerConf getInstance(){
         if (instance == null){
             instance = new MessageManagerConf();
             instance.resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME);
         }
         return instance;
     }
     public String getProperty(String key){
         return (String)resourceBundle.getObject(key);
     }
}