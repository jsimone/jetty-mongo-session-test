package com.heroku.test;

import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jetty.nosql.mongodb.MongoSessionIdManager;
import org.eclipse.jetty.nosql.mongodb.MongoSessionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.webapp.WebAppContext;

import com.mongodb.DB;
import com.mongodb.Mongo;

/**
 * 
 * This class launches the web application in an embedded Jetty container.
 * This is the entry point to your application. The Java command that is used for
 * launching should fire this main method.
 *
 * @author John Simone
 */
public class Main {
    
    /**
     * @param args
     */
    public static void main(String[] args) throws Exception{
        String webappDirLocation = "src/main/webapp/";
        
        //The port that we should run on can be set into an environment variable
        //Look for that variable and default to 8080 if it isn't there.
        String webPort = System.getenv("PORT");
        if(webPort == null || webPort.isEmpty()) {
            webPort = "8080";
        }

        Server server = new Server(Integer.valueOf(webPort));
        WebAppContext root = new WebAppContext();
        
        
        
        
        //Parse mango db URL
        String mongoDbURL = System.getenv("MONGOHQ_URL");
        
        System.out.println("mongo url: " + mongoDbURL);
        
        Matcher matcher = Pattern.compile("mongodb://(.*):(.*)@(.*):(.*)/(.*)").matcher(mongoDbURL);
        matcher.find();
        
        String mongoUser = matcher.group(1);
        String mongoPassword = matcher.group(2);
        String mongoHost = matcher.group(3);
        String mongoPort = matcher.group(4);
        String mongoDatabase = matcher.group(5);

        
        Mongo mongo = new Mongo(mongoHost, Integer.valueOf(mongoPort));
        DB db = mongo.getDB(mongoDatabase);
        db.authenticate(mongoUser, mongoPassword.toCharArray());
        
        //Set up session handling through MongoDb
        MongoSessionIdManager idMgr = new MongoSessionIdManager(server, db.getCollection("sessions"));
        
        //generate random worker name (should get this moved into Jetty)
        Random rand = new Random((new Date()).getTime());
        //generate a random number between 1000 and 9999
        int workerNum = 1000 + rand.nextInt(8999);
        
        idMgr.setWorkerName(String.valueOf(workerNum));
        server.setSessionIdManager(idMgr);

        SessionHandler sessionHandler = new SessionHandler();
        MongoSessionManager mongoMgr = new MongoSessionManager();
        mongoMgr.setSessionIdManager(server.getSessionIdManager());
        sessionHandler.setSessionManager(mongoMgr);
        root.setSessionHandler(sessionHandler);
        
        

        root.setContextPath("/");
        root.setDescriptor(webappDirLocation+"/WEB-INF/web.xml");
        root.setResourceBase(webappDirLocation);
        
        //Parent loader priority is a class loader setting that Jetty accepts.
        //By default Jetty will behave like most web containers in that it will
        //allow your application to replace non-server libraries that are part of the
        //container. Setting parent loader priority to true changes this behavior.
        //Read more here: http://wiki.eclipse.org/Jetty/Reference/Jetty_Classloading
        root.setParentLoaderPriority(true);
        
        server.setHandler(root);
        
        server.start();
        server.join();   
    }

}
