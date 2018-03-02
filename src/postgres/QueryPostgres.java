package postgres;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.postgresql.util.PGobject;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Program inserting and querying JSON objects using PostgreSQL.
 */
public class QueryPostgres
{
	/**
	 * Connection to database
	 */
	private Connection con;
			
	/**
	 * Table name
	 */
	public static final String TABLE_NAME = "data";
	
	/**
	 * Main method
	 * 
	 * @param args
	 * 			no arguments required
	 */	
    public static void main(String [] args) throws SQLException
	{
    	QueryPostgres qpostgres = new QueryPostgres();
    	qpostgres.connect();
    	qpostgres.load();
    	System.out.println(QueryPostgres.resultSetToString(qpostgres.query(), 100));
    	qpostgres.update(3);
    	System.out.println(QueryPostgres.resultSetToString(qpostgres.query(), 100));
    	System.out.println(QueryPostgres.resultSetToString(qpostgres.query1(), 100));
    	System.out.println(QueryPostgres.resultSetToString(qpostgres.query2(), 100));
	}
        
    /**
     * Connects to Postgres database and returns connection.
     *     
     * @return
     * 		connection
     */
    public Connection connect() throws SQLException
    {
    	// TODO: Modify this URL and user id and password for your own database
    	String url = "jdbc:postgresql://cosc304.ok.ubc.ca/db_kranslam";
		String uid = "rlawrenc";
		String pw = "test";
		
		System.out.println("Connecting to database.");
		con = DriverManager.getConnection(url, uid, pw);
		return con;		   
    }
    
    /**
     * Loads some sample JSON data into Postgres.
     */
    public void load() throws SQLException
    {							
		// Drop table if it exists
		Statement stmt = con.createStatement();
		try
		{
			stmt.executeUpdate("DROP TABLE IF EXISTS "+TABLE_NAME);
		}
		catch (SQLException e)
		{	// Ignore any exception with DROP			
		}
					
		// TODO: Create a table called "data"
		// Table should have field "id" as a serial primary key and field "text" as a json field		
		
		// TODO: Add 5 objects to table of the form: key, name, num, values
		// 		- where key is an increasing integer starting at 1 (i.e. 1, 2, 3, ...)
		//		- name is "text"+key (e.g. "text1")
		//		- num is key  (e.g. 1)
		//		- values is an array of 3 objects of the form: {"val":1, "text":"text1"}, {"val":2, "text":"text2"}, {"val":3, "text":"text3"}
		//			- The example is above for key = 1, for key = 2 the values should be 2,3,4, etc.
		// Note: Use PreparedStatements!
		
		// Note: This code is useful in PreparedStatements to set a JSON value
		// PGobject jsonObject = new PGobject();
		// jsonObject.setType("json");
		// jsonObject.setValue(buf.toString());
		// pstmt.setObject(1, jsonObject);
			
			
	}
    
    /**
     * Updates a record with given key so that the key is 10 times bigger.  The name field should also be updated with the new key value (e.g. text10).
     */
    public void update(int key) throws SQLException
    {
    	// Note: No support for UPDATE on json fields in Postgres 9.4 so will resort to downloading object to Java program, updating it, then saving it back.
    	// Alternative is to write a stored procedure which we will avoid.
    	
    	// TODO: Retrieve JSON object from database given key
    	
    	// Retrieve key and json text
    
    	// TODO: Use Google Gson library to convert string into a JsonObject 
    	// JsonObject jsonobj = new JsonParser().parse(jsontext).getAsJsonObject();
    	
    	// TODO: Modify JsonObject    	
    	    	    	
    	// TODO: Update JSON object in database
    	    		
    }
    
    /**
     * Performs a query that prints out all data.
     */
	public ResultSet query() throws SQLException
	{		
		Statement stmt = con.createStatement();
		return stmt.executeQuery("SELECT * FROM "+TABLE_NAME);
	}
    
    /**
     * Performs a query that returns all documents with key < 4.  Only show the key, name, and num fields.  Name the output field as "output".
     */
    public ResultSet query1() throws SQLException
    {
    	// TODO: Write a query that returns all documents with key < 4.  Only show the key, name, and num fields.
    	// See: http://www.postgresql.org/docs/9.4/static/functions-json.html
    	// See: http://www.postgresql.org/docs/9.4/static/datatype-json.html
    	// Note: You will need to use cast() to convert 'num' field to an int to do the comparison.
    	// Note: You may need to use json_build_object().
    	return null;    	
    }
    
    /**
     * Performs a query that returns all documents with key > 2 OR contains an element in the array with val = 4. 
     */
    public ResultSet query2() throws SQLException
    {    	
    	// TODO: Write a query that returns all documents with key > 2 OR contains an element in the array with val = 4. 
    	// See: http://www.postgresql.org/docs/9.4/static/functions-json.html
    	// See: http://www.postgresql.org/docs/9.4/static/datatype-json.html
    	// Note: You will need to use cast() to convert 'key' field to an int to do the comparison.
    	// Note: Getting the values out of the JSON array values may require an SQL subquery and the method json_array_elements().

    	return null;
    }          
    
        
    /*
	 * Do not change anything below here.
	 */
	/**
     * Converts a ResultSet to a string with a given number of rows displayed.
     * Total rows are determined but only the first few are put into a string.
     * 
     * @param rst
     * 		ResultSet
     * @param maxrows
     * 		maximum number of rows to display
     * @return
     * 		String form of results
     * @throws SQLException
     * 		if a database error occurs
     */    
    public static String resultSetToString(ResultSet rst, int maxrows) throws SQLException
    {                       
        StringBuffer buf = new StringBuffer(5000);
        int rowCount = 0;
        if (rst != null)
        {
	        ResultSetMetaData meta = rst.getMetaData();
	        buf.append("Total columns: " + meta.getColumnCount());
	        buf.append('\n');
	        if (meta.getColumnCount() > 0)
	            buf.append(meta.getColumnName(1));
	        for (int j = 2; j <= meta.getColumnCount(); j++)
	            buf.append(", " + meta.getColumnName(j));
	        buf.append('\n');
	                
	        while (rst.next()) 
	        {
	            if (rowCount < maxrows)
	            {
	                for (int j = 0; j < meta.getColumnCount(); j++) 
	                { 
	                	Object obj = rst.getObject(j + 1);                	 	                       	                                	
	                	buf.append(obj);                    
	                    if (j != meta.getColumnCount() - 1)
	                        buf.append(", ");                    
	                }
	                buf.append('\n');
	            }
	            rowCount++;
	        }
        }
        buf.append("Total results: " + rowCount);
        return buf.toString();
    }
} 
