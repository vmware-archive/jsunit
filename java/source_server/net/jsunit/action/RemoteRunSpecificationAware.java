package net.jsunit.action;

import net.jsunit.RemoteRunSpecification;
import net.jsunit.model.RemoteServerConfigurationSource;

import java.util.List;

public interface RemoteRunSpecificationAware extends RemoteServerConfigurationSource, InvalidTestRunAttemptAware {

    public void setRemoteRunSpecifications(List<RemoteRunSpecification> specs);

}
