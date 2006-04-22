package net.jsunit;

import com.opensymphony.xwork.config.entities.ActionConfig;
import com.opensymphony.xwork.config.entities.PackageConfig;
import com.opensymphony.xwork.config.providers.XmlConfigurationProvider;

import java.util.Map;

public class ConfigurationProviderWithRunner extends XmlConfigurationProvider {

    private String runnerActionName;

    public ConfigurationProviderWithRunner(String runnerActionName) {
        this.runnerActionName = runnerActionName;
    }

    public void init(com.opensymphony.xwork.config.Configuration configuration) {
        super.init(configuration);
        PackageConfig packageConfig = configuration.getPackageConfig("default");
        ActionConfig runnerConfig = findRunnerActionConfig(packageConfig.getActionConfigs());
        packageConfig.addActionConfig("runner", runnerConfig);
    }

    private ActionConfig findRunnerActionConfig(Map actionConfigs) {
        for (Object name : actionConfigs.keySet()) {
            if (name.equals(runnerActionName))
                return (ActionConfig) actionConfigs.get(name);
        }
        throw new RuntimeException("Should not happen");
    }

}
