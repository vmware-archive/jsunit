package net.jsunit.model;

import java.net.URL;
import java.util.List;

public interface RemoteMachineURLSource {
    public URL getRemoteMachineURLById(int id);

    public List<URL> getAllRemoteMachineURLs();
}
