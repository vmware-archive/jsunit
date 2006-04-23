package net.jsunit.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BrowserGroup {
    public static List<BrowserGroup> createFrom(List<Browser> browsers) {
        Map<BrowserType, Browser> typeToBrowser = new HashMap<BrowserType, Browser>();
        for (Browser browser : browsers) {

        }
        return new ArrayList<BrowserGroup>();
    }
}
