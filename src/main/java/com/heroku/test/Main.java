package com.heroku.test;

import java.util.Date;
import java.util.Random;

import org.eclipse.jetty.nosql.mongodb.MongoSessionIdManager;
import org.eclipse.jetty.nosql.mongodb.MongoSessionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.webapp.WebAppContext;

import com.mongodb.DB;
import com.mongodb.MongoURI;

public class Main {

    public static void main(String[] args) throws Exception{
        String webappDirLocation = "src/main/webapp/";

        String webPort = System.getenv("PORT");
        if(webPort == null || webPort.isEmpty()) {
            webPort = "8080";
        }

        Server server = new Server(Integer.valueOf(webPort));
        WebAppContext root = new WebAppContext();

        MongoURI mongoURI = new MongoURI(System.getenv("MONGOHQ_URL"));
        DB connectedDB = mongoURI.connectDB();

        if (mongoURI.getUsername() != null) {
            connectedDB.authenticate(mongoURI.getUsername(), mongoURI.getPassword());
        }

        MongoSessionIdManager idMgr = new MongoSessionIdManager(server, connectedDB.getCollection("sessions"));

        Random rand = new Random((new Date()).getTime());
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
        root.setParentLoaderPriority(true);

        server.setHandler(root);

        server.start();
        server.join();
    }

}
