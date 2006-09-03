package net.jsunit.model;

import org.w3c.dom.Node;

import java.util.List;

public interface ReferencedJsFileResolver {

    List<String> resolve(List<Node> scriptElements);

}
