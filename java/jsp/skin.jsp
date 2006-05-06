<%@ page import="net.jsunit.JsUnitServer" %>
<%@ page import="net.jsunit.ServerRegistry" %>
<%@ page import="net.jsunit.results.Skin" %>
<%JsUnitServer server = ServerRegistry.getServer();%>
<select name="skinId">
    <%
        for (Skin skin : server.getSkins()) {
    %><option value="<%=skin.getId()%>"><%=skin.getDisplayName()%></option>
    <%}%>
    <option value="">None (raw XML)</option>
</select>