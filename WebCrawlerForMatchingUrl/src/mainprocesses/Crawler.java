package mainprocesses;

import org.jsoup.Connection.Response;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import businessLayer.DatabaseBL;
import dataLayer.Url;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.crypto.Data;

/**
 * 
 */

/**
 * @author Freedom
 *
 */
public class Crawler {

	private static String host="www.notedu.com";
		
	/**
	 * 
	 */
	public Crawler() {

	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) {

		
		DatabaseBL databaseBL = DatabaseBL.getInstance();
		UrlParsing urlParsing = UrlParsing.getInstance(host);
	
		//databaseBL.CreateNewUrlTable();
		Url url = databaseBL.GetNextUrlToCrawle();
		URL urlToAdd;
		org.jsoup.Connection connection = null; 

		while(url!=null) {
			System.out.println("Crawling Url: " + url.toString());
			Document doc=null;
	        try {
	        	connection = Jsoup.connect(url.toString());
	            doc = connection.get();


	            // get all links
	            Elements links = doc.select("a[href]");
	            for (Element link : links) {
	            	
	            	urlToAdd=urlParsing.StringToUrl(url.getProtocol(), url.getHost(), link.attr("href"));
	            	if(urlToAdd!=null) {
	            		databaseBL.CheckAndAddNewUrl(urlToAdd);
	            	}
	            }
	            
	            databaseBL.MarkAsCrawled(url.getId());

	        }catch (HttpStatusException e) {
	            e.printStackTrace();
	            databaseBL.MarkAsReturnedError(url.getId(), ""+e.getStatusCode());
	        } 
	        catch (IOException e) {
	            e.printStackTrace();
	            databaseBL.MarkAsReturnedError(url.getId(), ""+e.getMessage());
	        }
	        
	        
			url = databaseBL.GetNextUrlToCrawle();
		}
		
	}
	
	
	
	
}
