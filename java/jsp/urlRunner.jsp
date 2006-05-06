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

<body bgcolor="#e4ecec">
<form action="/jsunit/runner" method="get" target="resultsFrame">
    <jsp:include page="header.jsp"/>
    <table cellpadding="0" cellspacing="0" width="100%" bgcolor="#FFFFFF">
        <jsp:include page="tabRow.jsp">
            <jsp:param name="selectedPage" value="urlRunner"/>
        </jsp:include>
        <tr>
            <td colspan="13" style="border-style: solid;border-bottom-width:1px;border-top-width:0px;border-left-width:1px;border-right-width:1px;border-color:#000000;">
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
                            <input type="submit" class="button" value="Run">
                        </td>
                    </tr>
                    <tr>
                        <td>&nbsp;</td>
                        <td colspan="2">
                            <i>e.g.
                                jsunit.net/runner/testRunner.html?testPage=jsunit.net/runner/tests/jsUnitTestSuite.html</i>
                        </td>
                    </tr>
                </table>
                <table>
                    <tr>
                        <td width="5% " valign="top">
                            <b>Browsers:</b>
                        </td>
                        <td width="25%" valign="top">
                            <jsp:include page="browsers.jsp"/>
                        </td>
                        <td width="35%" valign="top" rowspan="2">
                            <%if (server.getConfiguration().useCaptcha()) {%>
                            <jsp:include page="captcha.jsp"/>
                            <%}%>
                        </td>
                    </tr>
                    <tr>
                        <td width="5%" valign="top">
                            <b>Skin:</b>
                        </td>
                        <td width="25%" colspan="1">
                            <jsp:include page="skin.jsp"/>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
</form>
<b>Test results:</b>
<iframe name="resultsFrame" width="100%" height="250" src="/jsunit/app/emptyPage.html"></iframe>

</body>
</html>