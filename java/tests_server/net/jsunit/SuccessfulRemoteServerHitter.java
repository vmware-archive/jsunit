package net.jsunit;

import net.jsunit.model.TestRunResult;
import org.jdom.Document;

import java.net.URL;

public class SuccessfulRemoteServerHitter implements RemoteServerHitter {

    public Document hitURL(URL url) {
        return successfulTestRunResult();
    }

    private Document successfulTestRunResult() {
        return new Document(new TestRunResult().asXml());
    }

}
