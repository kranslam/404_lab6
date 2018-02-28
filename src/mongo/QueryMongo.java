package mongo;

import static com.mongodb.client.model.Projections.excludeId;
import static com.mongodb.client.model.Projections.include;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mongodb.client.model.Projections.fields;

import org.bson.Document;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

/**
 * Program to create a collection, insert JSON objects, and perform simple queries on MongoDB.
 */
public class QueryMongo
{
	// TODO: Change this to be your login (first letter of first name + 7 letters of last name)
	/**
	 * MongoDB database name
	 */
	public static final String DATABASE_NAME = "kranslam";
	
	/**
	 * MongoDB collection name
	 */
	public static final String COLLECTION_NAME = "data";
	
	/**
	 * MongoDB Server URL
	 */
	private static final String SERVER = "cosc304.ok.ubc.ca";
	
	/**
	 * Mongo client connection to server
	 */
	private MongoClient mongoClient;
	
	/**
	 * Mongo database
	 */
	private MongoDatabase db;
	
	
	/**
	 * Main method
	 * 
	 * @param args
	 * 			no arguments required
	 */	
    public static void main(String [] args)
	{
    	QueryMongo qmongo = new QueryMongo();
    	qmongo.connect();
    	qmongo.load();
    	System.out.println(QueryMongo.toString(qmongo.query()));
    	qmongo.update(3);
    	System.out.println(QueryMongo.toString(qmongo.query()));
    	System.out.println(QueryMongo.toString(qmongo.query1()));
    	System.out.println(QueryMongo.toString(qmongo.query2()));
	}
        
    /**
     * Connects to Mongo database and returns database object to manipulate for connection.
     *     
     * @return
     * 		Mongo database
     */
    public MongoDatabase connect()
    {
    	try
		{
		    // Provide connection information to MongoDB server 
		    mongoClient = new MongoClient(SERVER);
		}
	    catch (Exception ex)
		{	System.out.println("Exception: " + ex);
			ex.printStackTrace();
		}	
		
        // Provide database information to connect to		 
	    // Note: If the database does not already exist, it will be created automatically.
    	db = mongoClient.getDatabase(DATABASE_NAME);
		return db;
    }
    
    /**
     * Loads some sample data into MongoDB.
     */
    public void load()
    {					
		MongoCollection<Document> col;
		// Drop an existing collection (done to make sure you create an empty, new collection each time)
		col = db.getCollection(COLLECTION_NAME);
		if (col != null)
			col.drop();
		
		// TODO: Create a collection called "data". Note only need to get the collection as will be created if does not exist.
		// See: http://docs.mongodb.org/manual/reference/method/db.createCollection/
		// See: http://mongodb.github.io/mongo-java-driver/3.4/driver/tutorials/databases-collections/
		
		MongoCollection<Document> data = db.getCollection("data");
		// TODO: Add 5 objects to collection of the form: key, name, num, values
		// 		- where key is an increasing integer starting at 1 (i.e. 1, 2, 3, ...)
		//		- name is "text"+key (e.g. "text1")
		//		- num is key  (e.g. 1)
		//		- values is an array of 3 objects of the form: {"val":1, "text":"text1"}, {"val":2, "text":"text2"}, {"val":3, "text":"text3"}
		//			- The example is above for key = 1, for key = 2 the values should be 2,3,4, etc.
		// See: http://mongodb.github.io/mongo-java-driver/3.4/driver/getting-started/quick-start/
		// See: http://docs.mongodb.org/manual/reference/method/db.collection.insert/
		// See: http://api.mongodb.org/java/current/com/mongodb/BasicDBList.html
		
		
		Document d1 = new Document("key", 1).append("name", "text1").append("num", 1).append("values",Arrays.asList(new Document("val",1).append("text","text1"),new Document("val",2).append("text","text2"),new Document("val",3).append("text","text3")));
		Document d2 = new Document("key", 2).append("name", "text2").append("num", 2).append("values",Arrays.asList(new Document("val",2).append("text","text2"),new Document("val",3).append("text","text3"), new Document("val",4).append("text","text4")));
		Document d3 = new Document("key", 3).append("name", "text3").append("num", 3).append("values",Arrays.asList(new Document("val",3).append("text","text3"),new Document("val",4).append("text","text4"), new Document("val",5).append("text","text5")));
		Document d4 = new Document("key", 4).append("name", "text4").append("num", 4).append("values",Arrays.asList(new Document("val",4).append("text","text4"),new Document("val",5).append("text","text5"), new Document("val",6).append("text","text6")));
		Document d5 = new Document("key", 5).append("name", "text5").append("num", 5).append("values",Arrays.asList(new Document("val",5).append("text","text5"),new Document("val",6).append("text","text6"), new Document("val",7).append("text","text7")));
		
		List<Document> documents = new ArrayList<Document>();
		documents.add(d1);documents.add(d2);documents.add(d3);documents.add(d4);documents.add(d5);
		data.insertMany(documents);
				
	}
    
    /**
     * Updates a MongoDB record with given key so that the key is 10 times bigger.  The name field should also be updated with the new key value (e.g. text10).
     */
	public void update(int key) 
	{
		// TODO: Update document in collection with given key. Modify key to be 10 times bigger and update the name field to have the new key value.
		// See: http://docs.mongodb.org/manual/tutorial/modify-documents/
		// See: http://mongodb.github.io/mongo-java-driver/3.4/driver/getting-started/quick-start/#update-documents

		MongoCollection<Document> col = db.getCollection(COLLECTION_NAME);

		col.updateOne(Filters.eq("key", key), new Document("$set", new Document("key", key*10).append("name", "text"+key*10)));
	
	}
    
    /**
     * Performs a MongoDB query that prints out all data (except for the _id).
     */
	public MongoCursor<Document> query() 
	{		
		MongoCollection<Document> col = db.getCollection(COLLECTION_NAME);		
		
		// See: http://mongodb.github.io/mongo-java-driver/3.4/driver/getting-started/quick-start/#query-the-collection
		
		MongoCursor<Document> cursor = col.find().projection(fields(include("key", "name", "num", "values"), excludeId())).iterator();
		// OR:
		// MongoCursor<Document> cursor = col.find().projection(excludeId()).iterator();				
		return cursor;				
	}
    
    /**
     * Performs a MongoDB query that returns all documents with key < 4.  Only show the key, name, and num fields.
     */
    public MongoCursor<Document> query1()
    {
    	// TODO: Write a MongoDB query that returns all documents with key < 4.  Only show the key, name, and num fields.
    	// Use the code in query() method as a starter.  You must return a cursor to the results.
    	// See: http://docs.mongodb.org/manual/reference/method/db.collection.find/
    	// See: http://mongodb.github.io/mongo-java-driver/3.4/driver/getting-started/quick-start/#query-the-collection
    	
    	// Note: Make sure to return iterator() after do find. Example: col.find().iterator()
    	MongoCollection<Document> col = db.getCollection(COLLECTION_NAME);		
    	return col.find(Filters.lt("key", 4)).projection(fields(include("key", "name", "num"), excludeId())).iterator();		
   
    	
    }
    
    /**
     * Performs a MongoDB query that returns all documents with key > 2 OR contains an element in the array with val = 4. 
     */
    public MongoCursor<Document> query2()
    {    	
    	// TODO: Write a MongoDB query that returns all documents with key > 2 OR contains an element in the array with val = 4. 
    	// Use the code in query() method as a starter.  You must return a cursor to the results.
    	// See: http://docs.mongodb.org/manual/reference/operator/query/or/
    	// See: http://stackoverflow.com/questions/10620771/how-to-or-few-conditions-in-mongodb-using-java-driver
    	// See: http://docs.mongodb.org/manual/reference/operator/query/elemMatch/

    	MongoCollection<Document> col = db.getCollection(COLLECTION_NAME);		
	
    	// Note: Need to modify query as this is currently returning all documents
    	Document query = new Document();
		return col.find(query).iterator();		
    }          
    
    /**
     * Returns the Mongo database being used.
     * 
     * @return
     * 		Mongo database
     */
    public MongoDatabase getDb()
    {
    	return db;    
    }
    
    /**
     * Outputs a cursor of MongoDB results in string form.
     * 
     * @param cursor
     * 		Mongo cursor
     * @return
     * 		results as a string
     */
    public static String toString(MongoCursor<Document> cursor)
    {
    	StringBuilder buf = new StringBuilder();
    	int count = 0;
    	buf.append("Rows:\n");
    	if (cursor != null)
    	{	    	
			while (cursor.hasNext()) {
				Document obj = cursor.next();
				buf.append(obj.toJson());
				buf.append("\n");
				count++;
			}
    	}
		buf.append("Number of rows: "+count);
		cursor.close();
		return buf.toString();
    }
} 
