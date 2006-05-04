package net.jsunit.action;

import net.jsunit.model.RemoteMachineURLSource;

import java.net.URL;
import java.util.List;

public interface RemoteMachineURLSelectionAware extends RemoteMachineURLSource {

    public void setSelectedRemoteMachineURLs(List<URL> urls);

    public void setInvalidRemoteMachineURLId(String invalidId);

}
