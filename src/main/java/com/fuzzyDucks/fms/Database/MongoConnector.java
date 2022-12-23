package com.fuzzyDucks.fms.Database;

import com.fuzzyDucks.fms.Database.enums.MongoConf;
import com.fuzzyDucks.fms.Logger.ILogger;
import com.fuzzyDucks.fms.Logger.LoggingHandler;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

public class MongoConnector {
    private static MongoConnector instance = null;
    private static final MongoClientURI uri = new MongoClientURI(MongoConf.URI.getValue());
    private static final MongoClient client = new MongoClient(uri);
    private static MongoDatabase db;

    private MongoConnector() {
        db = client.getDatabase(MongoConf.DB.getValue());
    }

    private static final ILogger logger= LoggingHandler.getInstance();
    public static MongoConnector getInstance() {
        if (instance == null) {
            instance = new MongoConnector();
        }
        logger.logInfo("MongoConnector instance created");
        return instance;
    }

    public MongoDatabase getDatabase() {
        return db;
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            client.close();
        } finally {
            super.finalize();
        }
    }

}
