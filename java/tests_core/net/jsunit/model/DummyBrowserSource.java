package net.jsunit.model;

import java.util.List;

public class DummyBrowserSource implements BrowserSource {

    private String fileName;
    private int id;

    public DummyBrowserSource(String fileName, int id) {
        this.fileName = fileName;
        this.id = id;
    }

    public Browser getBrowserById(int requestedId) {
        return requestedId == id ? new Browser(fileName, id) : null;
    }

    public List<Browser> getAllBrowsers() {
        return null;
    }
}
