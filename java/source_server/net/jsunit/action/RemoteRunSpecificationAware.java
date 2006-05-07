package net.jsunit.action;

import net.jsunit.RemoteRunSpecification;
import net.jsunit.model.RemoteServerConfigurationSource;

import java.util.List;

public interface RemoteRunSpecificationAware extends RemoteServerConfigurationSource {

    public void setRemoteRunSpecifications(List<RemoteRunSpecification> specs);

    public void setInvalidRemoteMachineUrlBrowserCombination(InvalidRemoteMachineUrlBrowserCombination combination);

}
