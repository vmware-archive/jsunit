package net.jsunit;

import net.jsunit.model.TestRunResult;
import org.jdom.Document;

import java.net.URL;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import java.io.File;

public class SuccessfulRemoteServerHitter implements RemoteServerHitter {

    public Document hitURL(URL url) {
        return successfulTestRunResult();
    }

    public Document postToURL(URL url, Map<String, List<File>> fieldsToValues) throws IOException {
        return successfulTestRunResult();
    }

    private Document successfulTestRunResult() {
        return new Document(new TestRunResult().asXml());
    }

}
