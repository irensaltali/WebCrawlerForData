
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 */

/**
 * @author Freedom
 *
 */
public class Crawler {

	static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
	static final String DB_URL = "jdbc:mariadb://127.0.0.1/webcrawlerDB";

	//  Database credentials
	static final String USER = "root";
	static final String PASS = "MariaRootIren";
	
	/**
	 * 
	 */
	public Crawler() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//MariDBConnectWithSQL();
		//UrlParsing();
		
		
		Document doc;
        try {
        	
        	
            // need http protocol
            doc = Jsoup.connect("https://notedu.com").get();

            // get page title
            String title = doc.title();
            System.out.println("title : " + title);

            // get all links
            Elements links = doc.select("a[href]");
            for (Element link : links) {

                // get the value from href attribute
                System.out.println("\nlink : " + link.attr("href"));
                System.out.println("text : " + link.text());

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public static String UrlRegexMatcher() {
		String line = "This order was placed for QT3000! OK?";
	      String pattern = "(.*)(\\d+)(.*)";

	      // Create a Pattern object
	      Pattern r = Pattern.compile(pattern);

	      // Now create matcher object.
	      Matcher m = r.matcher(line);
	      if (m.find( )) {
	         System.out.println("Found value: " + m.group(0) );
	         System.out.println("Found value: " + m.group(1) );
	         System.out.println("Found value: " + m.group(2) );
	      }else {
	         System.out.println("NO MATCH");
	      }
	      
	      return "1";
	}
	
	public static void UrlParsing() {
		URL aURL = null;
		
		try {
			aURL = new URL("http://www.idefix.com/Kitap/Kahramanin-Sonsuz-Yolculugu/Joseph-Campbell/Din-Mitoloji/Mitoloji-Efsane/urunno=0001711511001");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("protocol = " + aURL.getProtocol());
		System.out.println("authority = " + aURL.getAuthority());
		System.out.println("host = " + aURL.getHost());
		System.out.println("port = " + aURL.getPort());
		System.out.println("path = " + aURL.getPath());
		System.out.println("query = " + aURL.getQuery());
		System.out.println("filename = " + aURL.getFile());
		System.out.println("ref = " + aURL.getRef());
			}
	
	
	public static void MariDBConnectWithSQL() {
		Connection conn = null;
	    Statement stmt = null;
	    try {
	        //STEP 2: Register JDBC driver
	        Class.forName("org.mariadb.jdbc.Driver");

	        //STEP 3: Open a connection
	        System.out.println("Connecting to a selected database...");
	        conn = DriverManager.getConnection(
	                "jdbc:mariadb://127.0.0.1/webcrawlerDB", USER, PASS);
	        System.out.println("Connected database successfully...");

	        //STEP 4: Execute a query
	        System.out.println("Creating table in given database...");
	        stmt = conn.createStatement();

	        String sql = "CREATE TABLE REGISTRATION "
	                + "(id INTEGER not NULL, "
	                + " first VARCHAR(255), "
	                + " last VARCHAR(255), "
	                + " age INTEGER, "
	                + " PRIMARY KEY ( id ))";

	        stmt.executeUpdate(sql);
	        System.out.println("Created table in given database...");
	    } catch (SQLException se) {
	        //Handle errors for JDBC
	        se.printStackTrace();
	    } catch (Exception e) {
	        //Handle errors for Class.forName
	        e.printStackTrace();
	    } finally {
	        //finally block used to close resources
	        try {
	            if (stmt != null) {
	                conn.close();
	            }
	        } catch (SQLException se) {
	        }// do nothing
	        try {
	            if (conn != null) {
	                conn.close();
	            }
	        } catch (SQLException se) {
	            se.printStackTrace();
	        }//end finally try
	    }//end try
	    System.out.println("Goodbye!");
	}

}
