package net.jsunit;

import net.jsunit.configuration.Configuration;
import net.jsunit.configuration.ConfigurationType;

import java.util.Arrays;
import java.util.List;

public class JsUnitFarmServer extends AbstractJsUnitServer {

    private static JsUnitFarmServer farmServerInstance;

    public JsUnitFarmServer(Configuration configuration) {
        super(configuration);
        farmServerInstance = this;
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
