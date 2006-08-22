package net.jsunit.model;

import net.jsunit.utility.StringUtility;
import org.w3c.dom.html.HTMLScriptElement;

import java.util.ArrayList;
import java.util.List;

public class DefaultReferencedJsFileResolver implements ReferencedJsFileResolver {

    public List<String> resolve(List<HTMLScriptElement> scriptElements) {
        List<String> result = new ArrayList<String>();
        for (HTMLScriptElement scriptElement : scriptElements) {
            String filePath = scriptElement.getSrc();
            if (isNonEmptyNonJsUnitReferencedJsFile(filePath))
                result.add(filePath);
        }
        return result;
    }

    private boolean isNonEmptyNonJsUnitReferencedJsFile(String filePath) {
        return !StringUtility.isEmpty(filePath) && !filePath.toLowerCase().endsWith("jsunitcore.js");
    }

}
