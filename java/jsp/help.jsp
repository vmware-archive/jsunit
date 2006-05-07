<%@ page import="net.jsunit.JsUnitServer" %>
<%@ page import="net.jsunit.ServerRegistry" %>
<%JsUnitServer server = ServerRegistry.getServer();%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Help/About - JsUnit</title>
    <script type="text/javascript" src="app/jsUnitCore.js"></script>
    <script type="text/javascript" src="app/server/jsUnitVersionCheck.js"></script>
    <link rel="stylesheet" type="text/css" href="./css/jsUnitStyle.css">
</head>

<body bgcolor="#eeeeee">
<jsp:include page="header.jsp"/>
<table cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
    <jsp:include page="tabRow.jsp">
        <jsp:param name="selectedPage" value="help"/>
    </jsp:include>
    <tr>
        <td colspan="13" style="border-style: solid;border-bottom-width:1px;border-top-width:0px;border-left-width:1px;border-right-width:1px;border-color:#000000;">
            <table>
                <tr>
                    <td colspan="3">
                        This is a JsUnit <%=server.isAggregateServer() ? "aggregate" : ""%> server.  It provides services for running JsUnit tests.  To learn more about
                        JsUnit tests, see <a href="http://www.jsunit.net">www.jsunit.net</a>.<br>
                        <br>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>

</body>
</html>