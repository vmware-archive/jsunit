<%@ page import="net.jsunit.JsUnitServer" %>
<%@ page import="net.jsunit.ServerRegistry" %>
<%@ page import="net.jsunit.configuration.Configuration" %>
<%@ page import="net.jsunit.configuration.ConfigurationProperty" %>
<%@ page import="net.jsunit.utility.SystemUtility" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%JsUnitServer server = ServerRegistry.getServer();%>
<%Configuration configuration = server.getConfiguration();%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>JsUnit <%if (server.isFarmServer()) {%> Farm<%}%> Server Administration</title>
    <script type="text/javascript" src="app/jsUnitCore.js"></script>
    <script type="text/javascript" src="app/server/jsUnitVersionCheck.js"></script>
    <script type="text/javascript" src="app/server/jsUnitServerAjax.js"></script>
    <script type="text/javascript">

        function updateServerStatusDiv(messageString) {
            var messageArray = messageString.split("|");
            var messageCount = messageArray.length;
            var newHTML = "<font size='-2'>";
            for (var i = messageCount - 1; i >= 0; i--) {
                newHTML += messageArray[i];
                newHTML += "<br>";
            }
            document.getElementById("serverStatusDiv").innerHTML = newHTML;
        }

        function updateTestRunCountDiv(testRunCount) {
            var newHTML = "<font size='-2'>Test Run Count: " + testRunCount + "</font>";
            document.getElementById("testRunCountDiv").innerHTML = newHTML;
        }

        function pageLoaded() {
            var updater = new JsUnitServerAjaxUpdater();
            updater.askServerForStatus();
            updater.askServerForTestRunCount();
        }

    </script>
    <link rel="stylesheet" type="text/css" href="./css/jsUnitStyle.css">
</head>

<body onload="pageLoaded()">
<table height="90" width="100%" cellpadding="0" cellspacing="0" border="0" summary="jsUnit Information"
       bgcolor="#DDDDDD">
    <tr>
        <td width="1">
            <a href="http://www.jsunit.net" target="_blank"><img src="images/logo_jsunit.gif" alt="JsUnit" border="0"/></a>
        </td>
        <td width="50">&nbsp;</td>
        <th nowrap align="left">
            <h4>JsUnit <%=SystemUtility.jsUnitVersion()%><%if (server.isFarmServer()) {%> Farm<%}%> Server
                Administration</h4>
            <font size="-2"><i>Running on <%=SystemUtility.displayString()%>
        </th>
        <td nowrap align="right" valign="middle">
            <font size="-2">
                <b><a href="http://www.jsunit.net/" target="_blank">www.jsunit.net</a></b><br>

                <div id="versionCheckDiv"><a href="javascript:checkForLatestVersion('latestversion')">Check for newer
                    version</a></div>
            </font>
            <br>
            <a href="http://www.pivotalsf.com/" target="top">
                <img border="0" src="images/pivotal.gif" alt="Powered By Pivotal">
            </a>
        </td>

    </tr>
</table>
<br>
<table cellpadding="1" cellspacing="1" border="0" width="100%">
    <tr>
        <td valign="top" width="50%">
            <h4>
                Server configuration (<a href="/jsunit/config">/config</a>)
            </h4>
            <table border="0">
                <tr>
                    <th valign="top" nowrap align="right">Server type:</th>
                    <td width="10">&nbsp;</td>
                    <td><%=server.serverType().getDisplayName()%></td>
                </tr>
                <%
                    for (ConfigurationProperty property : configuration.getRequiredAndOptionalConfigurationProperties(server.serverType())) {
                %>
                <tr>
                    <th valign="top" nowrap align="right"><%=property.getDisplayName()%>:</th>
                    <td width="10">&nbsp;</td>
                    <td valign="top">
                        <%
                            for (String valueString : property.getValueStrings(configuration)) {
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
<br>

<table>
    <tr>
        <td>
            <td valign="top" width="100%" height="200" nowrap>
                <h4>
                    Server Status
                </h4>
                <h4>
                    <font size="-2">Up since <%=new SimpleDateFormat().format(server.getStartDate())%></font><br>

                    <div id="testRunCountDiv"></div>
                    <font size="-2">Server log:</font>
                </h4>

                <div style="width:90%;height:90%;overflow:scroll" id="serverStatusDiv"></div>
            </td>

        </td>
    </tr>
</table>

</body>
</html>