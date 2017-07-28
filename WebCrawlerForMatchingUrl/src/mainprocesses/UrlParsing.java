package mainprocesses;

import java.net.MalformedURLException;
import java.net.URL;

public class UrlParsing {
	
	private String host;
	private static UrlParsing instance;

	private UrlParsing(String host) {
		this.host = host;
	}
	
	public static UrlParsing getInstance(String host) {
		if(instance==null)
			instance = new UrlParsing(host);
		return instance;
	}
	
	public boolean AvaliabilityToParse(URL url) {
		
		if(!url.getHost().equals(host))
			return false;
		else if(url.getPath().indexOf("/not/")<0)
			return false;
		else 
			return true;
	}
	
	public URL StringToUrl(String protocol, String host,String url) {
		try {
			if(url.indexOf("http")>=0)
				return new URL(url);
			else if(url.length()<=1 || url.indexOf("#")==0 || url.indexOf("javascript:")>=0 || url.indexOf("mailto:")>=0)
				return null;
			else if(url.indexOf("/")==0 || url.indexOf("\\")==0)
				return new URL(protocol+"://"+host+url);
			else {
				System.out.println("Unkown url type> protocol: "+protocol+" host: "+host+" url: "+url);
				return null;
				}
		}
		catch(Exception e) {
			 e.printStackTrace();
			 return null;
		}
	}
	

	
	public static void UrlParsingM(String url) {
		URL aURL = null;
		
		try {
			aURL = new URL(url);
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
		System.out.println("UserInfo = " + aURL.getUserInfo());
			}

}
