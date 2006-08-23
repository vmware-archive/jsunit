package net.jsunit.model;

import org.w3c.dom.html.HTMLScriptElement;

import java.util.List;
import java.util.ArrayList;

public class TrackerReferencedJsFileResolver implements ReferencedJsFileResolver {
    public List<String> resolve(List<HTMLScriptElement> scriptElements) {
        return new ArrayList<String>();
    }
}
