<%@ page import="net.jsunit.JsUnitAggregateServer" %>
<%@ page import="net.jsunit.ServerRegistry" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%JsUnitAggregateServer server = ServerRegistry.getAggregateServer();%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Server Administration - JsUnit</title>
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
            var newHTML = "Test Run Count: " + testRunCount;
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

<body onload="pageLoaded()" bgcolor="#eeeeee">
<jsp:include page="header.jsp"/>
<table width="100%">
    <tr>
        <td valign="top" width="100%" nowrap>
            <h4>
                Server Status
            </h4>
            Up since <%=new SimpleDateFormat().format(server.getStartDate())%> - <div id="testRunCountDiv"></div>
        </td>
    </tr>
    <tr>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <td>
            <div style="width:50;height:10;overflow:scroll;border-style:groove" id="serverStatusDiv">&nbsp;</div>
        </td>
    </tr>
</table>
<br>
<table cellpadding="1" cellspacing="1" border="0" width="100%">
    <tr>
        <td valign="top" width="100%">
            <h4>
                Server configuration
            </h4>
            <jsp:include page="configuration.jsp"/>
        </td>
    </tr>
</table>
<br>


</body>
</html>