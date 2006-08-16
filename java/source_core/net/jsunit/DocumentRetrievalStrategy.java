package net.jsunit;

import org.jdom.Document;

import java.net.URL;

public interface DocumentRetrievalStrategy {
    Document get(URL url);
}
