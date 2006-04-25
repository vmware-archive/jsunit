<%@ page import="net.jsunit.JsUnitServer" %>
<%@ page import="net.jsunit.ServerRegistry" %>

<%JsUnitServer server = ServerRegistry.getServer();%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>JsUnit Server - URLRunner</title>
    <script type="text/javascript" src="app/jsUnitCore.js"></script>
    <script type="text/javascript" src="app/server/jsUnitVersionCheck.js"></script>
    <link rel="stylesheet" type="text/css" href="./css/jsUnitStyle.css">
</head>

<body>

<jsp:include page="header.jsp"/>
<form action="/jsunit/runner" method="get" target="resultsFrame">
    <table cellpadding="0" cellspacing="0" width="100%">
        <jsp:include page="tabRow.jsp">
            <jsp:param name="selectedPage" value="urlRunner"/>
        </jsp:include>
        <tr>
            <td colspan="13" style="border-style: solid;border-bottom-width:1px;border-top-width:0px;border-left-width:1px;border-right-width:1px;">
                <table>
                    <tr>
                        <td colspan="3">
                            If you have JsUnit tests hosted on a JsUnit server (or any web server), you can ask this
                            server
                            to run them using the <i>URL runner</i> service.
                            You can specify a URL and/or browser ID and skin using this form:
                            <br/>
                            <br/>
                        </td>
                    </tr>
                    <tr>
                        <td width="10%">
                            <b>URL:</b>
                        </td>
                        <td width="10%">
                            <input type="text" name="url" size="60" value="">
                        </td>
                        <td width="80%">
                            <input type="submit" value="Run">
                        </td>
                    </tr>
                    <tr>
                        <td>&nbsp;</td>
                        <td colspan="2">
                            <font size="-2"><i>e.g.
                                http://www.jsunit.net/runner/testRunner.html?testPage=http://www.jsunit.net/runner/tests/jsUnitTestSuite.html</i>
                            </font>
                        </td>
                    </tr>
                    <jsp:include page="browserAndSkin.jsp">
                        <jsp:param name="includeAllBrowsersOption" value="true"/>
                    </jsp:include>
                </table>
            </td>
        </tr>
    </table>
</form>
<br>
<font size="-2">Server output:</font>
<br>
<iframe name="resultsFrame" width="100%" height="250" border="0"></iframe>

</body>
</html>