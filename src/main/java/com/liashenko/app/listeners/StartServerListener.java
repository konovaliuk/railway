package com.liashenko.app.listeners;

import com.liashenko.app.utils.AppProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Properties;

/**
 * @author Admin
 */
@WebListener
public class StartServerListener implements ServletContextListener {
    //     private ServletContext context = null;
    private static final Logger rootLogger = LogManager.getRootLogger();
    private static final Logger classLogger = LogManager.getLogger(StartServerListener.class);

    /*This method is invoked when the Web Application has been removed
    and is no longer able to accept requests
    */
    public void contextDestroyed(ServletContextEvent event) {
        //Output a simple message to the server's console
        //System.out.println("The Simple Web App. Has Been Removed");
//    this.context = null;
        classLogger.info("Server stopped");
    }

    //This method is invoked when the Web Application
    //is ready to service requests
    public void contextInitialized(ServletContextEvent event) {
        classLogger.info("Server started");
        try {
            AppProperties.loadFilesProperties(decodePath("app_settings.properties"));
        } catch (RuntimeException e) {
            classLogger.error("file \'site_config.properties\' not found: ", e);
        }

//        LocalHostIPaddress.assignIpAddres();
    }

    private  String decodePath(String propertyFilePath) {
            URL url = getClass().getClassLoader().getResource(propertyFilePath);
            if (url != null) {
                try {
                    return URLDecoder.decode(url.getPath(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    classLogger.error(e.getMessage());
                }
            } else {
                classLogger.error("File " + propertyFilePath + " not found!");
            }
        return "";
    }
}
