package mongo;

import static com.mongodb.client.model.Projections.excludeId;
import static com.mongodb.client.model.Projections.include;
import static com.mongodb.client.model.Projections.fields;

import org.bson.Document;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

/**
 * Program to create a collection, insert JSON objects, and perform simple queries on MongoDB.
 */
public class QueryMongo
{
	// TODO: Change this to be your login (first letter of first name + 7 letters of last name)
	/**
	 * MongoDB database name
	 */
	public static final String DATABASE_NAME = "rlawrenc";
	
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
		
		
		// TODO: Add 5 objects to collection of the form: key, name, num, values
		// 		- where key is an increasing integer starting at 1 (i.e. 1, 2, 3, ...)
		//		- name is "text"+key (e.g. "text1")
		//		- num is key  (e.g. 1)
		//		- values is an array of 3 objects of the form: {"val":1, "text":"text1"}, {"val":2, "text":"text2"}, {"val":3, "text":"text3"}
		//			- The example is above for key = 1, for key = 2 the values should be 2,3,4, etc.
		// See: http://mongodb.github.io/mongo-java-driver/3.4/driver/getting-started/quick-start/
		// See: http://docs.mongodb.org/manual/reference/method/db.collection.insert/
		// See: http://api.mongodb.org/java/current/com/mongodb/BasicDBList.html
				
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
    	return col.find().iterator();		// Note: Need to modify query as this is currently returning all documents
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
