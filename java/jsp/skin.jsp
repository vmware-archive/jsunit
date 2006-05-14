<%@ page import="net.jsunit.JsUnitAggregateServer" %>
<%@ page import="net.jsunit.ServerRegistry" %>
<%@ page import="net.jsunit.results.Skin" %>
<%JsUnitAggregateServer server = ServerRegistry.getAggregateServer();%>
<select name="skinId">
    <%
        for (Skin skin : server.getSkins()) {
    %><option value="<%=skin.getId()%>"><%=skin.getDisplayName()%></option>
    <%}%>
    <option value="">None (raw XML)</option>
</select>