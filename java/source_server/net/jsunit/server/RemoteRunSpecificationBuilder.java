package net.jsunit.server;

import net.jsunit.InvalidRemoteMachineBrowserCombinationException;
import net.jsunit.RemoteRunSpecification;
import net.jsunit.configuration.RemoteConfiguration;
import net.jsunit.model.Browser;

import java.net.URL;
import java.util.*;

public class RemoteRunSpecificationBuilder {

    public RemoteRunSpecification forSingleRemoteBrowser(URL url, Browser remoteBrowser) {
        RemoteRunSpecification result = new RemoteRunSpecification(url);
        result.addBrowser(remoteBrowser);
        return result;
    }

    public List<RemoteRunSpecification> forAllBrowsersFromRemoteConfigurations(List<RemoteConfiguration> remoteConfigurations) {
        List<RemoteRunSpecification> result = new ArrayList<RemoteRunSpecification>();
        for (RemoteConfiguration remoteConfiguration : remoteConfigurations)
            result.add(new RemoteRunSpecification(remoteConfiguration.getRemoteURL()));
        return result;
    }

    public List<RemoteRunSpecification> fromIdStringPairs(String[] pairs, List<RemoteConfiguration> allRemoteConfigurations)
            throws InvalidRemoteMachineBrowserCombinationException {
        RemoteRunSpecificationMerger merger = new RemoteRunSpecificationMerger();
        for (String pair : pairs) {
            String[] ids = pair.split("_");
            int urlId;
            int browserId;
            try {
                urlId = Integer.parseInt(ids[0]);
                browserId = Integer.parseInt(ids[1]);
            } catch (Exception e) {
                return throwInvalidRemoteMachineBrowserCombinationException(ids, pair);
            }
            RemoteConfiguration configuration;
            try {
                configuration = allRemoteConfigurations.get(urlId);
            } catch (Exception e) {
                throw new InvalidRemoteMachineBrowserCombinationException(ids[0], ids[1]);
            }
            Browser browser = configuration.getBrowserById(browserId);
            if (browser == null)
                throw new InvalidRemoteMachineBrowserCombinationException(ids[0], ids[1]);
            merger.add(configuration.getRemoteURL(), browser);
        }
        return merger.getResult();
    }

    private List<RemoteRunSpecification> throwInvalidRemoteMachineBrowserCombinationException(String[] ids, String pair) throws InvalidRemoteMachineBrowserCombinationException {
        if (ids.length == 2)
            throw new InvalidRemoteMachineBrowserCombinationException(ids[0], ids[1]);
        else
            throw new InvalidRemoteMachineBrowserCombinationException(pair);
    }

    public List<RemoteRunSpecification> forAllBrowsersFromRemoteURLs(List<URL> remoteMachineURLs) {
        List<RemoteRunSpecification> result = new ArrayList<RemoteRunSpecification>();
        for (URL url : remoteMachineURLs)
            result.add(new RemoteRunSpecification(url));
        return result;
    }

    public List<RemoteRunSpecification> forAllBrowsersFromRemoteURLs(URL... remoteMachineURLs) {
        return forAllBrowsersFromRemoteURLs(Arrays.asList(remoteMachineURLs));
    }

    static class RemoteRunSpecificationMerger {
        private Map<String, RemoteRunSpecification> urlToSpec = new HashMap<String, RemoteRunSpecification>();

        public void add(URL remoteURL, Browser browser) {
            RemoteRunSpecification specForURL = urlToSpec.get(remoteURL.toString());
            if (specForURL == null) {
                specForURL = new RemoteRunSpecification(remoteURL);
                urlToSpec.put(remoteURL.toString(), specForURL);
            }
            specForURL.addBrowser(browser);
        }

        public List<RemoteRunSpecification> getResult() {
            List<RemoteRunSpecification> result = new ArrayList<RemoteRunSpecification>(urlToSpec.values());
            Collections.sort(result, new Comparator<RemoteRunSpecification>() {
                public int compare(RemoteRunSpecification first, RemoteRunSpecification second) {
                    return first.getRemoteMachineBaseURL().toString().compareTo(second.getRemoteMachineBaseURL().toString());
                }
            });
            return result;
        }
    }

}
