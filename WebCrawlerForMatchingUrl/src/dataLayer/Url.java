package dataLayer;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;

public class Url {

	public Url() {
		// TODO Auto-generated constructor stub
	}
	
	private int id;
	private String protocol;
	private String host;
	private String path;
	private String query;
	private Date creationtime;
	private boolean validity;
	private boolean parsed;
	private boolean crawled;
	private boolean availability;
	
	public Url(int id, String protocol, String host, String path, String query, Date creationtime, boolean validity,
			boolean parsed, boolean crawled, boolean availability) {
		super();
		this.id = id;
		this.protocol = protocol;
		this.host = host;
		this.path = path;
		this.query = query;
		this.creationtime = creationtime;
		this.validity = validity;
		this.parsed = parsed;
		this.crawled = crawled;
		this.availability = availability;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public Date getCreationtime() {
		return creationtime;
	}
	public void setCreationtime(Date creationtime) {
		this.creationtime = creationtime;
	}
	public boolean isValidity() {
		return validity;
	}
	public void setValidity(boolean validity) {
		this.validity = validity;
	}
	public boolean isParsed() {
		return parsed;
	}
	public void setParsed(boolean parsed) {
		this.parsed = parsed;
	}
	public boolean isCrawled() {
		return crawled;
	}
	public void setCrawled(boolean crawled) {
		this.crawled = crawled;
	}
	public boolean isAvailability() {
		return availability;
	}
	public void setAvailability(boolean availability) {
		this.availability = availability;
	}
	

	public URL toURL() {
		try {
			return new URL(protocol +"://"+ host + (path==null?"":path) + (query==null?"":query));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public String toString() {
		return protocol +"://"+ host + (path==null?"":path) + (query==null?"":query);
	}
	

}
