package net.jsunit.model;

import org.w3c.dom.html.HTMLScriptElement;

import java.util.List;

public interface ReferencedJsFileResolver {

    List<String> resolve(List<HTMLScriptElement> scriptElements);

}
