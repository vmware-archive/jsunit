<%@ page import="net.jsunit.JsUnitServer" %>
<%@ page import="net.jsunit.ServerRegistry" %>
<%@ page import="net.jsunit.model.Browser" %>
<%@ page import="net.jsunit.results.Skin" %>
<%JsUnitServer server = ServerRegistry.getServer();%>
<%if (!server.isAggregateServer()) {%>
<tr>
    <td width="1">
        Browser:
    </td>
    <td>
        <select name="browserId">
            <option value="">All browsers</option>
            <%
                for (Browser browser : server.getConfiguration().getBrowsers()) {
            %><option value="<%=browser.getId()%>"><%=browser.getFileName()%></option>
            <%}%>
        </select><br>
    </td>
</tr>
<%}%>
<tr>
    <td width="1">
        Skin:
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
<tr>
    <td colspan="2">
        <input type="submit" value="Go"/>
    </td>
</tr>
