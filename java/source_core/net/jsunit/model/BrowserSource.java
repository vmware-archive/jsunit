package net.jsunit.model;

import java.util.List;

public interface BrowserSource {

    Browser getBrowserById(int id);

    List<Browser> getAllBrowsers();
}
