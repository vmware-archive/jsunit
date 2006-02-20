package net.jsunit;

import java.io.IOException;
import java.net.URL;

import org.jdom.Document;

public interface RemoteRunnerHitter {

	public Document hitURL(URL url) throws IOException ;
	
}
