package net.jsunit.model;

import net.jsunit.configuration.RemoteConfiguration;

import java.util.List;

public interface RemoteMachineConfigurationSource {
    public RemoteConfiguration getRemoteMachineConfigurationById(int id);

    public List<RemoteConfiguration> getAllRemoteMachineConfigurations();
}
