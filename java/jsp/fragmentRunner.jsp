<%@ page import="net.jsunit.JsUnitServer" %>
<%@ page import="net.jsunit.ServerRegistry" %>
<%JsUnitServer server = ServerRegistry.getServer();%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>JsUnit Server - FragmentRunner</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <script type="text/javascript" src="app/jsUnitCore.js"></script>
    <script type="text/javascript" src="app/server/jsUnitVersionCheck.js"></script>
    <link rel="stylesheet" type="text/css" href="./css/jsUnitStyle.css">
</head>

<body>
<jsp:include page="header.jsp"/>

<form action="/jsunit/runner" method="post" target="resultsFrame">
    <table cellpadding="0" cellspacing="0">
        <jsp:include page="tabRow.jsp">
            <jsp:param name="selectedPage" value="fragmentRunner"/>
        </jsp:include>
        <tr>
            <td colspan="13" style="border-style: solid;border-bottom-width:1px;border-top-width:0px;border-left-width:1px;border-right-width:1px;">
                <table>
                    <tr>
                        <td colspan="2">
                            You can ask the server to run JsUnit tests of test code fragments using the <i>fragment
                            runner</i>
                            service. You may enter any kind of JavaScript - statements or Test Functions.
                            Type in your test code fragments below; choose a specific browser and/or skin if desired.
                            <br/>
                        </td>
                    </tr>
                    <tr>
                        <td width="10%" valign="top">
                            <b>Fragment:</b>
                        </td>
                        <td width="90%">
                            <textarea name="fragment" width="100%" cols="75" rows="10"></textarea>
                        </td>
                    </tr>
                    <tr>
                        <td>&nbsp;</td>
                        <td>
                            <font size="-2">
                                <i>e.g. assertTrue(true);</i>
                            </font>
                        </td>
                    </tr>
                    <jsp:include page="browserSkinAndGo.jsp"/>
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