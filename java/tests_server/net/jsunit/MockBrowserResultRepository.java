package net.jsunit;

import net.jsunit.logging.BrowserResultRepository;
import net.jsunit.model.BrowserResult;

public class MockBrowserResultRepository implements BrowserResultRepository {
    public BrowserResult storedResult;
    public String requestedId;
    public int requestedBrowserId;

    public void store(BrowserResult result) {
        this.storedResult = result;
    }

    public BrowserResult retrieve(String id, int browserId) {
        this.requestedId = id;
        this.requestedBrowserId = browserId;
        return null;
    }
}
