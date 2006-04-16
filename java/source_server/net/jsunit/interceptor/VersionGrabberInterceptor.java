package net.jsunit.interceptor;

import com.opensymphony.xwork.Action;
import net.jsunit.action.VersionGrabberAware;
import net.jsunit.version.JsUnitWebsiteVersionGrabber;

public class VersionGrabberInterceptor extends JsUnitInterceptor {
    protected void execute(Action targetAction) throws Exception {
        VersionGrabberAware aware = ((VersionGrabberAware) targetAction);
        aware.setVersionGrabber(new JsUnitWebsiteVersionGrabber());
    }
}
