<%@ page import="net.jsunit.JsUnitServer" %>
<%@ page import="net.jsunit.ServerRegistry" %>
<%JsUnitServer server = ServerRegistry.getServer();%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>JsUnit Server - LogDisplayer</title>
    <script type="text/javascript" src="app/jsUnitCore.js"></script>
    <script type="text/javascript" src="app/server/jsUnitVersionCheck.js"></script>
    <link rel="stylesheet" type="text/css" href="./css/jsUnitStyle.css">
</head>

<body>
<jsp:include page="header.jsp"/>
<form action="/jsunit/displayer">

    <table cellpadding="0" cellspacing="0">
        <jsp:include page="tabRow.jsp">
            <jsp:param name="selectedPage" value="logDisplayer"/>
        </jsp:include>
        <tr>
            <td colspan="13" style="border-style: solid;border-bottom-width:1px;border-top-width:0px;border-left-width:1px;border-right-width:1px;">
                <table>
                    <tr>
                        <td colspan="3">
                            You can view the logs of past runs using the <i>displayer</i> service.
                            Use this form to specify the ID and browser of the run you want to see:
                            <br/>
                            <br/>
                        </td>
                    </tr>
                    <tr>
                        <td width="10%">
                            <b>ID:</b>
                        </td>
                        <td width="10%">
                            <input type="text" name="id" size="60"/>
                        </td>
                        <td width="80%">
                            <input type="submit" value="Show log">
                        </td>
                    </tr>
                    <jsp:include page="browserAndSkin.jsp">
                        <jsp:param name="includeAllBrowsersOption" value="false"/>
                    </jsp:include>
                </table>
            </td>
        </tr>
    </table>
</form>
<br>
Server output:
<br>
<iframe name="resultsFrame" width="100%" height="250"></iframe>
</body>
</html>