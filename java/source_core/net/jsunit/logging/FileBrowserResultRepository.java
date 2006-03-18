package net.jsunit.logging;

import net.jsunit.model.BrowserResult;
import net.jsunit.model.BrowserResultBuilder;
import net.jsunit.utility.FileUtility;
import net.jsunit.utility.XmlUtility;

import java.io.File;

public class FileBrowserResultRepository implements BrowserResultRepository {

    private static final String LOG_PREFIX = "JSTEST-";

    private File logsDirectory;

    public FileBrowserResultRepository(File logsDirectory) {
        this.logsDirectory = logsDirectory;
        if (!logsDirectory.exists())
            logsDirectory.mkdir();
    }

    private File logFileForId(String id, int browserId) {
        return new File(logsDirectory + File.separator + LOG_PREFIX + id + "." + browserId + ".xml");
    }

    public void deleteDirectory(String directoryName) {
        File file = new File(directoryName);
        file.delete();
    }

    public void store(BrowserResult result) {
        String xml = XmlUtility.asString(result.asXml());
        FileUtility.write(logFileForId(result.getId(), result.getBrowserId()), xml);
    }

    public BrowserResult retrieve(String id, int browserId) {
        File logFile = logFileForId(id, browserId);
        if (logFile.exists())
            return new BrowserResultBuilder().build(logFile);
        return null;
    }

}
