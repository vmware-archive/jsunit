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
    <script type="text/javascript">
        function verifyFragmentNotBlank() {
            var isBlank = document.getElementById("fragment").value == "";
            if (isBlank)
                alert("Please enter a fragment.")
            return !isBlank;
        }
    </script>
    <link rel="stylesheet" type="text/css" href="./css/jsUnitStyle.css">
</head>

<body>
<jsp:include page="header.jsp"/>

<form action="/jsunit/runner" method="post" target="resultsFrame">
    <table cellpadding="0" cellspacing="0" width="100%">
        <jsp:include page="tabRow.jsp">
            <jsp:param name="selectedPage" value="fragmentRunner"/>
        </jsp:include>
        <tr>
            <td colspan="13" style="border-style: solid;border-bottom-width:1px;border-top-width:0px;border-left-width:1px;border-right-width:1px;">
                <table>
                    <tr>
                        <td colspan="3">
                            You can ask the server to run JsUnit tests of test code fragments using the <i>fragment
                            runner</i>
                            service. You may enter any kind of JavaScript - statements or Test Functions. For example,
                            you could enter "var myVar = 3; assertEquals(myVar, 3);" or "function testSimple()
                            {assertTrue(true);}".
                            Type in your test code fragments below; choose specific browsers and a skin if desired.
                            <br/>
                            <br/>
                        </td>
                    </tr>
                    <tr>
                        <td width="10%" valign="top">
                            <b>Fragment:</b>
                        </td>
                        <td width="10%">
                            <textarea id="fragment" name="fragment" width="100%" cols="75" rows="10"></textarea>
                        </td>
                        <td width="80%" valign="top">
                            <input type="submit" value="Run" onclick="return verifyFragmentNotBlank()">
                        </td>
                    </tr>
                </table>
                <table>
                    <tr>
                        <td valign="top">
                            <jsp:include page="browserAndSkin.jsp">
                                <jsp:param name="goOnClick" value="return verifyFragmentNotBlank()"/>
                                <jsp:param name="multipleBrowsersAllowed" value="true"/>
                            </jsp:include>
                        </td>
                        <td width="100">&nbsp;</td>
                        <td valign="top">
                            <%if (server.getConfiguration().useCaptcha()) {%>
                            <jsp:include page="captcha.jsp"/>
                            <%}%>
                        </td>
                    </tr>
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