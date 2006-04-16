package net.jsunit.interceptor;

import com.opensymphony.xwork.Action;
import net.jsunit.action.SkinAware;
import net.jsunit.results.Skin;
import net.jsunit.utility.StringUtility;

import javax.servlet.http.HttpServletRequest;

public class SkinInterceptor extends JsUnitInterceptor {
    protected void execute(Action targetAction) throws Exception {
        SkinAware skinAware = (SkinAware) targetAction;
        HttpServletRequest request = request();
        String skinIdString = request.getParameter("skinId");
        if (!StringUtility.isEmpty(skinIdString)) {
            int skinId = Integer.parseInt(skinIdString);
            Skin skin = skinAware.getSkinSource().getSkinById(skinId);
            skinAware.setSkin(skin);
        }
    }
}
