package net.jsunit.model;

import net.jsunit.utility.StringUtility;
import net.jsunit.utility.XmlUtility;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

public class DefaultReferencedJsFileResolver implements ReferencedJsFileResolver {

    public List<String> resolve(List<Node> scriptElements) {
        List<String> result = new ArrayList<String>();
        for (Node scriptElement : scriptElements) {
            Node src = XmlUtility.srcAttribute(scriptElement);
            if (src != null && isNonEmptyNonJsUnitReferencedJsFile(src.getNodeValue()))
                result.add(src.getNodeValue());
        }
        return result;
    }

    private boolean isNonEmptyNonJsUnitReferencedJsFile(String filePath) {
        return !StringUtility.isEmpty(filePath) && !filePath.toLowerCase().endsWith("jsunitcore.js");
    }

}
