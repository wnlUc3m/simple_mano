package nfvm.dao;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.MongoWriteConcernException;
import com.mongodb.MongoWriteException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
import com.mongodb.util.JSONParseException;

import nfvm.model.OpenStackServer;

public class MongoServerDAO implements ServerDAO{

	private static MongoClient persistenceService = new MongoClient();
	private static MongoDatabase db;
	private static String serversCollection = "servers";
	private static final String serverIdAttribute = "serverId";
	private Gson gson = new Gson();
	
	public MongoServerDAO() {
		db = persistenceService.getDatabase(MongoFactoryDAO.getDbname());
	}
	
	@Override
	public boolean addServer(OpenStackServer s) {
		//TODO: User:storeUser - gestionar bien las excepciones
		try {
			String jsonServer = gson.toJson(s);
			BasicDBObject dbObject = (BasicDBObject) JSON.parse(jsonServer);
			db.getCollection(serversCollection, BasicDBObject.class).insertOne(dbObject);
			return true;
		} catch (JSONParseException parseExcept) {
			return false;
		} catch (MongoWriteException insertExcept){
			return false;
		} catch (MongoWriteConcernException dataInsertExcept) {
			return false;
		} catch (MongoException e) {
			return false;
		}
	}

	@Override
	public boolean deleteServer(OpenStackServer id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean existServer(String id) {
		FindIterable<Document> iterable = db.getCollection(serversCollection).find(
				new Document(serverIdAttribute, id));
	    if (iterable.first() != null)
	    	return true;
	    else
	    	return false;
	}

	@Override
	public OpenStackServer getServer(String id) {
		BasicDBObject query = new BasicDBObject(serverIdAttribute, new BasicDBObject("$eq", id));
	    FindIterable<Document> iterable = db.getCollection(serversCollection).find(query);
	    OpenStackServer server = null;
	    if (iterable.first() != null){
	    	server = gson.fromJson(gson.toJson(iterable.first()), OpenStackServer.class);
	    }
	    return server;
	}
	
	//TODO: actualizar conductor
	@Override
	public boolean updateServer(OpenStackServer s) {
	/*	try {
			String jsonServer = gson.toJson(s);
			BasicDBObject dbObject = (BasicDBObject) JSON.parse(jsonServer);
			Bson filter = new Document(serverIdAttribute, s.getServerId());
			db.getCollection(serversCollection,BasicDBObject.class).replaceOne(filter, dbObject);
			return true;
		} catch (Exception e) {
			// TODO: excepciones en la actualizacion
			return false;
		}*/
		return false;

	}
	/**
	 * 	com.mongodb.MongoWriteException - if the write failed due some other failure specific to the delete command
	 *	com.mongodb.MongoWriteConcernException - if the write failed due being unable to fulfil the write concern
	 *	com.mongodb.MongoException - if the write failed due some other failure
	 */
	/*
	@Override
	public boolean deleteDriver(String id) {
		if (existDriver(id)){
			BasicDBObject document = new BasicDBObject();
			document.put(userIdentifier, id);
			try {
				db.getCollection(driversCollection).deleteOne(document);
				
				if (existDriver(id)){
					return false;
				}
				else return true;
				
			}catch (com.mongodb.MongoWriteException deleteExcept) {
				System.out.println("Delete command problem " + deleteExcept);
				return false;
			}catch (com.mongodb.MongoWriteConcernException fulfilExcept) {
				System.out.println("Fulfil problem " + fulfilExcept);
				return false;
			}catch (com.mongodb.MongoException writeFailExcept){
				System.out.println("Write failed " + writeFailExcept);
				return false;
			}
		}
		else{
			return false;
		}
	}
	*/
		
}
