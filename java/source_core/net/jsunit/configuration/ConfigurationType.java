package net.jsunit.configuration;

import net.jsunit.utility.StringUtility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;

public enum ConfigurationType {
    STANDARD(
        new ConfigurationProperty[] {
            ConfigurationProperty.CLOSE_BROWSERS_AFTER_TEST_RUNS,
            ConfigurationProperty.LOGS_DIRECTORY,
            ConfigurationProperty.LOG_STATUS,
            ConfigurationProperty.PORT,
            ConfigurationProperty.RESOURCE_BASE,
            ConfigurationProperty.TIMEOUT_SECONDS,
        },
        new ConfigurationProperty[]{
            ConfigurationProperty.BROWSER_FILE_NAMES,
            ConfigurationProperty.DESCRIPTION,
            ConfigurationProperty.URL,
        }
    ),
    FARM(
        new ConfigurationProperty[] {
            ConfigurationProperty.LOGS_DIRECTORY,
            ConfigurationProperty.LOG_STATUS,
            ConfigurationProperty.PORT,
            ConfigurationProperty.REMOTE_MACHINE_URLS,
            ConfigurationProperty.IGNORE_UNRESPONSIVE_REMOTE_MACHINES,
        },
        new ConfigurationProperty [] {
            ConfigurationProperty.DESCRIPTION,
            ConfigurationProperty.RESOURCE_BASE,
            ConfigurationProperty.URL,
        }
    );

    private List<ConfigurationProperty> requiredProperties;
    private List<ConfigurationProperty> optionalProperties;

    private ConfigurationType(ConfigurationProperty[] required, ConfigurationProperty[] optional) {
        this.requiredProperties = Arrays.asList(required);
        this.optionalProperties = Arrays.asList(optional);
    }

    public List<ConfigurationProperty> getRequiredConfigurationProperties() {
        return requiredProperties;
    }

    public List<ConfigurationProperty> getOptionalConfigurationProperties() {
        return optionalProperties;
    }

    public List<ConfigurationProperty> getPropertiesInvalidFor(Configuration configuration) {
        List<ConfigurationProperty> result = new ArrayList<ConfigurationProperty>();

        for (ConfigurationProperty property : getRequiredAndOptionalConfigurationProperties()) {
            try {
                String valueString = property.getValueString(configuration);
                if (isPropertyRequired(property) && StringUtility.isEmpty(valueString))
                    result.add(property);
            } catch (ConfigurationException e) {
                result.add(property);
            }
        }

        return result;

    }

    private boolean isPropertyRequired(ConfigurationProperty property) {
        return getRequiredConfigurationProperties().contains(property);
    }

    public List<ConfigurationProperty> getRequiredAndOptionalConfigurationProperties() {
        List<ConfigurationProperty> result = new ArrayList<ConfigurationProperty>();
        result.addAll(getRequiredConfigurationProperties());
        result.addAll(getOptionalConfigurationProperties());
        Collections.sort(result, ConfigurationProperty.comparator());
        return result;
    }
}
