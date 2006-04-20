<%@ page import="net.jsunit.JsUnitServer" %>
<%@ page import="net.jsunit.ServerRegistry" %>
<%@ page import="net.jsunit.configuration.ConfigurationProperty" %>
<%JsUnitServer server = ServerRegistry.getServer();%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>JsUnit Server - Configuration</title>
    <script type="text/javascript" src="app/jsUnitCore.js"></script>
    <script type="text/javascript" src="app/server/jsUnitVersionCheck.js"></script>
    <link rel="stylesheet" type="text/css" href="./css/jsUnitStyle.css">
</head>

<body>
<jsp:include page="header.jsp"/>

<table cellpadding="0" cellspacing="0">
    <jsp:include page="tabRow.jsp">
        <jsp:param name="selectedPage" value="configuration"/>
    </jsp:include>
    <tr>
        <td colspan="13" style="border-style: solid;border-bottom-width:1px;border-top-width:0px;border-left-width:1px;border-right-width:1px;">

            <table cellpadding="1" cellspacing="1" border="0" width="100%">
                <tr>
                    <th nowrap align="right">Server type:</th>
                    <td width="10">&nbsp;</td>
                    <td><%=server.serverType().getDisplayName()%></td>
                </tr>
                <%
                    for (ConfigurationProperty property : server.getConfiguration().getRequiredAndOptionalConfigurationProperties(server.serverType())) {
                %>
                <tr>
                    <th nowrap align="right" valign="top"><%=property.getDisplayName()%>:</th>
                    <td width="10">&nbsp;</td>
                    <td valign="top">
                        <%
                            for (String valueString : property.getValueStrings(server.getConfiguration())) {
                        %><div><%
                        if (valueString != null) {
                            if (property.isURL()) {
                    %><a href="<%=valueString%>"><%=valueString%></a><%
                    } else {
                    %><%=valueString%><%
                            }
                        }
                    %></div><%
                        }
                    %>
                    </td></tr>
                <%
                    }
                %>
            </table>
        </td>
    </tr>
</table>

</body>
</html>