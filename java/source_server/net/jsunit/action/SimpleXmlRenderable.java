package net.jsunit.action;

import net.jsunit.XmlRenderable;
import org.jdom.Element;

public class SimpleXmlRenderable implements XmlRenderable {
    private Element element;

    public SimpleXmlRenderable(Element element) {
        this.element = element;
    }

    public Element asXml() {
        return element;
    }
}
