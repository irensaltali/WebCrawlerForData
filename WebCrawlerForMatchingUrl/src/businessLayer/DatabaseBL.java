/**
 * 
 */
package businessLayer;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;

import dataLayer.Url;
import mainprocesses.UrlParsing;

/**
 * @author Freedom
 *
 */
public class DatabaseBL {
	
	static DatabaseBL instance;
	UrlParsing urlParsing;
	private String protocol = "http";
	private String host = "www.idefix.com";
	
	//  Database settings
	static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
	static final String DB_URL = "jdbc:mariadb://127.0.0.1/webcrawlerDB";
	static Connection conn = null;

	//  Database credentials
	static final String USER = "webcrawler";
	static final String PASS = "Iren";
	/**
	 * 
	 */
	private DatabaseBL() {

		try {
    		System.out.println("Connecting to a selected database...");
			conn = DriverManager.getConnection(
			        "jdbc:mariadb://127.0.0.1/webcrawlerDB", USER, PASS);
			System.out.println("Connected database successfully...");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		urlParsing = UrlParsing.getInstance(host);
	}
	
	public static DatabaseBL getInstance() {
		if(instance==null)
			instance = new DatabaseBL();
		
		return instance;
	}
	
	
	/**
	 * Create Url tables if not exists
	 */
	public void CreateNewUrlTable() {
		Statement stmt = null;	
	    try {
	        stmt = conn.createStatement();

	        String sql = "CREATE TABLE `url` "
	        		+ "(`id` INT NOT NULL AUTO_INCREMENT,"
	        		+ "`protocol` VARCHAR(50) NOT NULL,"
	        		+ "`host` VARCHAR(50) NOT NULL,"
	        		+ "`path` VARCHAR(500) NOT NULL DEFAULT '',"
	        		+ "`query` VARCHAR(500) NULL DEFAULT NULL,"
	        		+ "`creationtime` DATETIME NULL DEFAULT NULL,"
	        		+ "`validity` BIT NULL DEFAULT b'0',"
	        		+ "`parsed` BIT NULL DEFAULT b'0',"
	        		+ "`crawled` BIT NULL DEFAULT b'0',"
	        		+ "`availability` BIT NULL DEFAULT b'0',"
	        		+ "`error` VARCHAR(500) NOT NULL DEFAULT '',"
	        		+ "PRIMARY KEY (`id`)"
	        		+ ")"
	        		+ "COMMENT='This table contains all url that extracted from related sites.'"
	        		+ "COLLATE='utf8_turkish_ci'"
	        		+ "ENGINE=InnoDB";


	        stmt.executeUpdate(sql);
	        AddInitialUrlToTable();
	    } catch (SQLException se) {
	        //Handle errors for JDBC
	        se.printStackTrace();
	    } catch (Exception e) {
	        //Handle errors for Class.forName
	        e.printStackTrace();
	    } 
	}
	
	public void CheckAndAddNewUrl(URL url) {
		if(url.getHost().equals(host)) {
			Statement stmt = null;	
		    try {
		        stmt = conn.createStatement();
	
		        String sql = "SELECT * FROM `webcrawlerDB`.`url` WHERE "
		        		+ "protocol='"+url.getProtocol()+"'"
		    	        + "and host='"+url.getHost()+"'"
		        		+ (url.getPath()==null?"":" and path='"+url.getPath()+"'")
		        		+ (url.getQuery()==null?"":" and query='"+url.getQuery()+"'");
	
		        
		        ResultSet result = stmt.executeQuery(sql);
		        if(!result.next())
		        	AddUrlToTable(url);
		    } catch (SQLException se) {
		        //Handle errors for JDBC
		        se.printStackTrace();
		    } catch (Exception e) {
		        e.printStackTrace();
		    } 
		}
	}
	
	public void AddInitialUrlToTable() {

		Statement stmt = null;	
	    try {
	        stmt = conn.createStatement();
	        

	        String sql = "INSERT INTO `webcrawlerDB`.`url` ("
	        		+ "`protocol`"
	        		+ ", `host`"
	        		+", `creationtime`"
	        		+", `validity`"
	        		+ ") VALUES ("
	        		+ "'"+protocol+"'"
	        		+ ", '"+host+"'"
	        		+", '"+new Timestamp(System.currentTimeMillis())+"'"
	        		+", 1"
	        		+")";

	        stmt.executeQuery(sql);
	        
	    } catch (SQLException se) {
	        //Handle errors for JDBC
	        se.printStackTrace();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public void AddUrlToTable(URL url) {

		boolean availability=urlParsing.AvaliabilityToParse(url);
		
		Statement stmt = null;	
	    try {
	        stmt = conn.createStatement();
	        

	        String sql = "INSERT INTO `webcrawlerDB`.`url` ("
	        		+ "`protocol`"
	        		+ ", `host`"
	        		+ ", `path`"
	        		+ (url.getQuery()==null?"":", `query`")
	        		+", `creationtime`"
	        		+", `validity`"
	        		+", `availability`"
	        		+ ") VALUES ("
	        		+ "'"+url.getProtocol()+"'"
	        		+ ", '"+url.getHost()+"'"
	        		+ ", '"+url.getPath()+"'"
	        		+ (url.getQuery()==null?"":", '"+url.getQuery()+"'")
	        		+", '"+new Timestamp(System.currentTimeMillis())+"'"
	        		+", 1"
	        		+", "+availability
	        		+")";

	        stmt.executeQuery(sql);
	        
	    } catch (SQLException se) {
	        //Handle errors for JDBC
	        se.printStackTrace();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	public Url GetNextUrlToCrawle() {
		Statement stmt = null;	
	    try {
	        stmt = conn.createStatement();

	        String sql = "SELECT * FROM `webcrawlerDB`.`url` WHERE "
	        		+ "crawled=0 and error=''";

	        
	        ResultSet result = stmt.executeQuery(sql);
	        if(result.next()) {
	        	Url url = new Url(result.getInt("id"),
	        			result.getString("protocol"),
	        			result.getString("host"),
	        			result.getString("path"),
	        			result.getString("query"),
	        			result.getDate("creationtime"),
	        			result.getBoolean("validity"),
	        			result.getBoolean("parsed"),
	        			result.getBoolean("crawled"),
	        			result.getBoolean("availability"),
	        			result.getString("error")      			
	        			);
	        	return url;
	        }
	        	
	    } catch (SQLException se) {
	        //Handle errors for JDBC
	        se.printStackTrace();
	    } catch (Exception e) {
	        e.printStackTrace();
	    } 
	    return null;
	}

	public void MarkAsCrawled(int id) {
		Statement stmt = null;	
	    try {
	        stmt = conn.createStatement();

	        String sql = "UPDATE `webcrawlerDB`.`url` SET `crawled`=b'1' WHERE  `id`="+id;

	        stmt.executeUpdate(sql);
	        	
	    } catch (SQLException se) {
	        //Handle errors for JDBC
	        se.printStackTrace();
	    } catch (Exception e) {
	        e.printStackTrace();
	    } 
	}
	
	public void MarkAsReturnedError(int id, String error) {
		Statement stmt = null;	
	    try {
	        stmt = conn.createStatement();

	        String sql = "UPDATE `webcrawlerDB`.`url` SET `error`='"+error+"' WHERE  `id`="+id;

	        stmt.executeUpdate(sql);
	        	
	    } catch (SQLException se) {
	        //Handle errors for JDBC
	        se.printStackTrace();
	    } catch (Exception e) {
	        e.printStackTrace();
	    } 
	}
}
