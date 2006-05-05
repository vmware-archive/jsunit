package net.jsunit.action;

import net.jsunit.RemoteRunSpecification;
import net.jsunit.model.RemoteMachineConfigurationSource;

import java.util.List;

public interface RemoteRunSpecificationAware extends RemoteMachineConfigurationSource {

    public void setRemoteRunSpecifications(List<RemoteRunSpecification> specs);

    public void setInvalidRemoteMachineUrlBrowserCombination(InvalidRemoteMachineUrlBrowserCombination combination);

}
