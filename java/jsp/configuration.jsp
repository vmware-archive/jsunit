<%@ page import="net.jsunit.JsUnitAggregateServer" %>
<%@ page import="net.jsunit.ServerRegistry" %>
<%@ page import="net.jsunit.configuration.Configuration" %>
<%@ page import="net.jsunit.configuration.ConfigurationProperty" %>
<%@ page import="net.jsunit.configuration.ServerType" %>
<%
    JsUnitAggregateServer server = ServerRegistry.getAggregateServer();
    Configuration configuration = server.getConfiguration();
%>

<table border="0">
    <%
        for (ConfigurationProperty property : configuration.getRequiredAndOptionalConfigurationProperties(ServerType.AGGREGATE)) {
    %>
    <tr>
        <th valign="top" nowrap align="right"><%=property.getDisplayName()%>:</th>
        <td width="10">&nbsp;</td>
        <td valign="top">
            <%
                for (String valueString : property.getValueStrings(configuration)) {
            %><div><%
            if (valueString != null) {%>
            <%=valueString%><%
            }
        %></div><%
            }
        %>
        </td></tr>
    <%
        }
    %>
</table>
