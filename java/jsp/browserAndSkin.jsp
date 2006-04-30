<%@ page import="net.jsunit.JsUnitServer" %>
<%@ page import="net.jsunit.ServerRegistry" %>
<%@ page import="net.jsunit.model.Browser" %>
<%@ page import="net.jsunit.results.Skin" %>
<%JsUnitServer server = ServerRegistry.getServer();%>
<table width="50%">
    <%if (!server.isAggregateServer()) {%>
    <tr>
        <td width="10%" valign="top">
            <b>Browser:</b>
        </td>
        <td>
            <table width="100%">
                <%
                    boolean includeAllBrowsersOption = Boolean.parseBoolean(request.getParameter("includeAllBrowsersOption"));
                    if (includeAllBrowsersOption) {
                %>
                <tr>
                    <td nowrap>
                        <input type="radio" name="browserId" value="" checked>
                        All available browsers
                    </td>
                </tr>
                <%}%>
                <%for (Browser browser : server.getConfiguration().getBrowsers()) {%>
                <tr>
                    <td valign="top" nowrap>
                        <input type="radio" name="browserId" value="<%=browser.getId()%>">
                        <%if (browser.getType() != null) {%>
                        <img src="<%=browser.getLogoPath()%>" alt="<%=browser.getDisplayName()%>" title="<%=browser.getDisplayName()%>">
                        <%}%>
                        <%=browser.getDisplayName()%>
                    </td>
                </tr>
                <%}%>
            </table>
        </td>
    </tr>
    <%}%>
    <tr>
        <td width="1">
            <b>Skin:</b>
        </td>
        <td>
            <select name="skinId">
                <%
                    for (Skin skin : server.getSkins()) {
                %><option value="<%=skin.getId()%>"><%=skin.getDisplayName()%></option>
                <%}%>
                <option value="">None (raw XML)</option>
            </select><br>
        </td>
    </tr>
</table>