package net.jsunit.logging;

import net.jsunit.model.BrowserResult;

public interface BrowserResultRepository {
    void store(BrowserResult result);

    void remove(String id);

    BrowserResult retrieve(String id);
}
