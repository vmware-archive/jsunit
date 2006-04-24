package net.jsunit.action;

import net.jsunit.InvalidBrowserIdException;
import net.jsunit.SkinSource;
import net.jsunit.XmlRenderable;
import net.jsunit.model.BrowserResult;
import net.jsunit.results.Skin;

public class ResultDisplayerAction extends JsUnitBrowserTestRunnerAction implements SkinAware, RequestSourceAware {

    public static final String TRANSFORM = "transform";

    private String id;
    private BrowserResult result;
    private Integer browserId;
    private boolean browserIdInvalid;
    private Skin skin;
    private String ipAddress;

    public void setId(String id) {
        this.id = id;
    }

    public void setBrowserId(Integer browserId) {
        this.browserId = browserId;
    }

    public String execute() throws Exception {
        if (id == null || browserId == null)
            return ERROR;
        try {
            result = runner.findResultWithId(id, browserId);
        } catch (InvalidBrowserIdException e) {
            browserIdInvalid = true;
            return ERROR;
        }
        return skin != null ? TRANSFORM : SUCCESS;
    }

    public XmlRenderable getXmlRenderable() {
        if (result != null)
            return result;
        String message;
        if (browserIdInvalid)
            message = "Invalid Browser ID '" + browserId + "'";
        else if (id != null && browserId != null)
            message = "No Test Result has been submitted with ID '" + id + "' for browser ID '" + browserId + "'";
        else
            message = "A Test Result ID and a browser ID must both be given";
        return new ErrorXmlRenderable(message);
    }

    public void setSkin(Skin skin) {
        this.skin = skin;
    }

    public Skin getSkin() {
        return skin;
    }

    public SkinSource getSkinSource() {
        return runner;
    }

    public void setRequestIPAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setRequestHost(String host) {
    }

    public String getRequestIpAddress() {
        return ipAddress;
    }
}