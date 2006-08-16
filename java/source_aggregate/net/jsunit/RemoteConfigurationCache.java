package net.jsunit;

import net.jsunit.configuration.RemoteConfiguration;
import net.jsunit.model.RemoteServerConfigurationSource;

import java.util.List;

public interface RemoteConfigurationCache extends RemoteServerConfigurationSource {
    void setCachedRemoteConfigurations(List<RemoteConfiguration> list);

    List<RemoteConfiguration> getAllRemoteMachineConfigurations();

    RemoteConfiguration getRemoteMachineConfigurationById(int id);
}
