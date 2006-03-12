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

    private File logFileForId(String id) {
        return new File(logsDirectory + File.separator + LOG_PREFIX + id + ".xml");
    }

    public void deleteDirectory(String directoryName) {
        File file = new File(directoryName);
        file.delete();
    }

    public void store(BrowserResult result) {
        String xml = XmlUtility.asString(result.asXml());
        FileUtility.write(logFileForId(result.getId()), xml);
    }

    public void remove(String id) {
        File file = logFileForId(id);
        FileUtility.delete(file);
    }

    public BrowserResult retrieve(String id) {
        File logFile = logFileForId(id);
        if (logFile.exists())
            return new BrowserResultBuilder().build(logFile);
        return null;
    }

}
