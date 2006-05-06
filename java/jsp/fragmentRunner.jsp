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

<body bgcolor="#e4ecec">
<form action="/jsunit/runner" method="post" target="resultsFrame">
<jsp:include page="header.jsp"/>
<table cellpadding="0" cellspacing="0" width="100%" bgcolor="#FFFFFF">
<jsp:include page="tabRow.jsp">
    <jsp:param name="selectedPage" value="fragmentRunner"/>
</jsp:include>
<tr>
<td colspan="13" style="border-style: solid;border-bottom-width:1px;border-top-width:0px;border-left-width:1px;border-right-width:1px;border-color:#000000;">
<table width="100%">
<tr>
    <td width="5%" valign="top">
        <b>Fragment:</b>
    </td>
    <td width="60%" colspan="2" valign="top" height="160">
        <textarea id="fragment" name="fragment" style="width:97%;height:97%" rows="10"></textarea>
    </td>
    <td width="1%" rowspan="4">&nbsp;</td>
    <td width="33%" rowspan="4" valign="top">
        <div class="rb1roundbox">
            <div class="rb1top"><div></div></div>

            <div class="rb1content">
                <table width="100%">
                    <tr>
                        <td align="center">
                            <div class="rb3roundbox">
                                <div class="rb3top"><div></div></div>

                                <div class="rb3content">
                                    <img src="/jsunit/images/question_mark.gif" alt="What is the FragmentRunner?" title="What is the FragmentRunner?" border="0">
                                    <b>What is the FragmentRunner service?</b>
                                </div>

                                <div class="rb3bot"><div></div></div></div>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            You can ask this JsUnit Server to run JsUnit on fragments of code using
                            the <i>fragment runner</i> service.
                            You may enter any kind of JavaScript - standalone statements and
                            assertions or real Test Functions. For example, you could enter:
                            <br>
                            <table align="center">
                                <tr>
                                    <td nowrap>
                                        <div class="rb3roundbox">
                                            <div class="rb3top"><div></div></div>

                                            <div class="rb3content">
                                                <font size="-2">
                                                    var myVar = 3;<br>
                                                    assertEquals(3, myVar);<br>
                                                    assertNotNull(myVar);<br>
                                                    assertEquals(6, myVar * 2);
                                                </font>
                                            </div>

                                            <div class="rb3bot"><div></div></div></div>
                                    </td>
                                    <td>&nbsp;&nbsp;<b>or:</b>&nbsp;&nbsp;</td>
                                    <td nowrap>
                                        <div class="rb3roundbox">
                                            <div class="rb3top"><div></div></div>

                                            <div class="rb3content">
                                                <font size="-2">
                                                    function testSimple() {<br>
                                                    &nbsp;&nbsp;&nbsp;assertTrue(true);<br>
                                                    &nbsp;&nbsp;&nbsp;assertFalse(false);<br>
                                                    }
                                                </font>
                                            </div>

                                            <div class="rb3bot"><div></div></div></div>
                                    </td>
                                </tr>
                            </table>
                            <br>
                            Enter a fragment in the text area, choose which browsers you want to run your fragment on
                            and which skin you want your results displayed in, and press "Run test fragment".
                            <br>
                            The fragment runner service is useful for experimenting with bits and pieces of JavaScript,
                            but once you start creating real JsUnit Test Pages, you will probably find that using the
                            <a href="/jsunit/uploadRunnerPage">upload runner</a> service is more powerful.
                        </td>
                    </tr>
                </table>
            </div>

            <div class="rb1bot"><div></div></div></div>
    </td>
    <td width="1%" rowspan="4">&nbsp;</td>
</tr>
<tr>
    <td width="5%" height="1">&nbsp;</td>
    <td width="60%" valign="top" colspan="2" height="1">
        <input type="submit" class="button" value="Run test fragment" onclick="return verifyFragmentNotBlank()">
    </td>
</tr>
<tr>
    <td width="5% " valign="top">
        <b>Browsers:</b>
    </td>
    <td width="25%" valign="top">
        <jsp:include page="browsers.jsp">
            <jsp:param name="goOnClick" value="return verifyFragmentNotBlank()"/>
        </jsp:include>
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