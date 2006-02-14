package net.jsunit;

import java.net.URL;
import java.util.List;

import net.jsunit.configuration.FarmConfiguration;

public class JsUnitFarmServer {

	private final FarmConfiguration configuration;

	public JsUnitFarmServer(FarmConfiguration configuration) {
		this.configuration = configuration;
	}

    public static void main(String[] args) {
        
    }

    public List<URL> getRemoteMachineURLs() {
		return configuration.getRemoteMachineURLs();
	}

	public void dispose() {
	}

	public void start() {
		
	}

}
