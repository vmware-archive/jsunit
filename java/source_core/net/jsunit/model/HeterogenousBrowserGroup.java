package net.jsunit.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HeterogenousBrowserGroup implements Iterable<Browser> {

    private List<Browser> browsers = new ArrayList<Browser>();

    public static List<HeterogenousBrowserGroup> createFrom(List<Browser> allBrowsers) {
        List<Browser> browsersLeft = new ArrayList<Browser>(allBrowsers);
        List<HeterogenousBrowserGroup> result = new ArrayList<HeterogenousBrowserGroup>();
        while (!browsersLeft.isEmpty()) {
            HeterogenousBrowserGroup group = new HeterogenousBrowserGroup();
            for (Browser browser : browsersLeft) {
                if (!group.containsBrowserOfConflictingType(browser))
                    group.add(browser);
            }
            browsersLeft.removeAll(group.getBrowsers());
            result.add(group);
        }
        return result;
    }

    public List<Browser> getBrowsers() {
        return browsers;
    }

    private boolean containsBrowserOfConflictingType(Browser aBrowser) {
        for (Browser browser : browsers) {
            if (browser.conflictsWith(aBrowser))
                return true;
        }
        return false;
    }

    private void add(Browser browser) {
        browsers.add(browser);
    }

    public Iterator<Browser> iterator() {
        return browsers.iterator();
    }

    public int size() {
        return browsers.size();
    }
}
