package net.jsunit.interceptor;

import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.xwork.Action;
import net.jsunit.action.SkinAware;
import net.jsunit.results.Skin;
import net.jsunit.utility.StringUtility;

import javax.servlet.http.HttpServletRequest;

public class SkinInterceptor extends JsUnitInterceptor {
    protected void execute(Action targetAction) {
        SkinAware skinAware = (SkinAware) targetAction;
        HttpServletRequest request = ServletActionContext.getRequest();
        String skinIdString = request.getParameter("skinId");
        if (!StringUtility.isEmpty(skinIdString)) {
            int skinId = Integer.parseInt(skinIdString);
            Skin skin = skinAware.getSkinSource().getSkinById(skinId);
            skinAware.setSkin(skin);
        }
    }
}
