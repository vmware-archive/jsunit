package net.jsunit;

import net.jsunit.configuration.Configuration;

import java.net.URL;
import java.util.List;
import java.util.Arrays;

public class JsUnitFarmServer extends AbstractJsUnitServer {

    private JsUnitFarmServer instance;

    public JsUnitFarmServer(Configuration configuration) {
        super(configuration);
        instance = this;
    }

    protected void ensureConfigurationIsValid() {
        configuration.ensureValidForFarm();
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

}
