package net.jsunit;

import net.jsunit.configuration.Configuration;
import net.jsunit.configuration.ConfigurationType;

import java.net.URL;
import java.util.List;
import java.util.Arrays;

public class JsUnitFarmServer extends AbstractJsUnitServer {

    private static JsUnitFarmServer farmServerInstance;

    public JsUnitFarmServer(Configuration configuration) {
        super(configuration);
        farmServerInstance = this;
    }

    protected void ensureConfigurationIsValid() {
    }

    protected List<String> servletNames() {
        return Arrays.asList(new String[] {
            "config",
            "runner"
        });
    }

    public static void main(String args[]) {
          try {
              JsUnitFarmServer server = new JsUnitFarmServer(Configuration.resolve(args));
              server.start();
          } catch (Throwable t) {
              t.printStackTrace();
          }
    }

    public List<URL> getRemoteMachineURLs() {
        return configuration.getRemoteMachineURLs();
    }

    public String toString() {
        return "JsUnit Farm Server";
    }

	protected String xworkXmlName() {
		return "farm_xwork.xml";
	}

	public static JsUnitFarmServer getFarmServerInstance() {
		return farmServerInstance;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	protected ConfigurationType serverType() {
		return ConfigurationType.FARM;
	}

}
