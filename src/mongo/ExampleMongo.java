package mongo;

import org.bson.Document;

import static com.mongodb.client.model.Projections.excludeId;
import static com.mongodb.client.model.Projections.include;
import static com.mongodb.client.model.Projections.fields;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;

/**
 * Example on how to use the Mongo API directly.
 */
public class ExampleMongo
{
	/**
	 * Main method
	 * 
	 * @param args
	 * 			no arg required
	 */	
    public static void main(String [] args)
	{
		try (
			// Provide connection information to MongoDB server 
			MongoClient mongoClient = new MongoClient("cosc304.ok.ubc.ca");
		)
		{		   		    								
            // Provide database information to connect to
			MongoDatabase db = mongoClient.getDatabase("tpch"); 
						 
			// Get a list of the collections in this database and print them out
			System.out.println("List of collections: ");
	        MongoIterable<String> collectionNames = db.listCollectionNames();
	        for (String s : collectionNames) {
	            System.out.println("\nCollection: "+s);
	            
	            // Print out first 5 documents of the collection 
	            if (s.contains("system"))
	                continue;      // Do not query any system collections
	            System.out.println("Documents:\n");
	            MongoCollection<Document> col = db.getCollection(s);
	            MongoCursor<Document> cursor = col.find().iterator();	            
	            for (int i=0; i < 5 && cursor.hasNext(); i++)
	            {  
	                Document doc = cursor.next();
	                System.out.println(doc);
	            }
	        }
	        	     	     
	        // Sample query #2 equivalent SQL: SELECT r_regionkey, r_name FROM region where r_regionkey < 3
	        System.out.println("\n\nSample query with output:");
	        MongoCollection<Document> col = db.getCollection("region");
	        Document query = new Document("r_regionkey", new Document("$lt", 3));	     
	        MongoCursor<Document> cursor = col.find(query).projection(fields(include("r_regionkey", "r_name"), excludeId())).iterator();
	        while (cursor.hasNext())
            {  
                Document doc = cursor.next();
                System.out.println(doc);
            }
	        cursor.close();
	        
	        System.out.println("\nFINISHED!"); 	        
		}
		catch (Exception ex)
		{	System.out.println("Exception: " + ex);
			ex.printStackTrace();
		}		
	} 
} 
