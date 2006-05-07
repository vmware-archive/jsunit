<%@ page import="net.jsunit.JsUnitServer" %>
<%@ page import="net.jsunit.ServerRegistry" %>

<%
    JsUnitServer server = ServerRegistry.getServer();
    String url = request.getParameter("url") == null ? "" : request.getParameter("url");
%>
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
                        <td width="5%">
                            <b>URL:</b>
                        </td>
                        <td width="45%">
                            <input type="text" name="url" size="60" value="<%=url%>">&nbsp;
                            <input type="submit" class="button" value="Run">
                        </td>
                        <td width="1%" rowspan="50">&nbsp;</td>
                        <td width="48%" rowspan="50" valign="top">
                            <div class="rb1roundbox">
                                <div class="rb1top"><div></div></div>

                                <div class="rb1content">
                                    <table>
                                        <tr>
                                            <td align="center">
                                                <div class="rb3roundbox">
                                                    <div class="rb3top"><div></div></div>

                                                    <div class="rb3content">
                                                        <img src="/jsunit/images/question_mark.gif" alt="What is the URLRunner service?" title="What is the FragmentRunner service?" border="0">
                                                        <b>What is the URLRunner service?</b>
                                                    </div>

                                                    <div class="rb3bot"><div></div></div></div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                If you have JsUnit Test Pages hosted on an internet-visible web server
                                                (perhaps on a JsUnit server of your own), you can ask this server to run
                                                them using the <i>URLRunner service</i>.
                                                Enter the full JsUnit TestRunner URL for your Test Pages, choose which
                                                browsers you want to run your Test Page on and which skin you want your
                                                results displayed in, and press "Run".
                                                For example, you can run the JsUnit self-tests by entering
                                                <font size="-2">
                                                    http://www.jsunit.net/runner/testRunner.html? testPage=
                                                    http://www.jsunit.net/runner/tests/jsUnitTestSuite.html.
                                                </font>
                                                <br>
                                                <br>
                                                The URL runner service is useful for proving to a wide audience that
                                                your JavaScript code passes all of its tests. This is especially
                                                valuable if you produce a party library that other developers use and
                                                want confidence in. You can provide a link on your site to the URLRunner
                                                service, passing in your URL (using the query string parameter "url"),
                                                and users can try running your tests against all the browsers/OSs that
                                                they are interested in supporting.
                                            </td>
                                        </tr>
                                    </table>
                                </div>

                                <div class="rb1bot"><div></div></div></div>
                        </td>
                        <td width="1%" rowspan="50">&nbsp;</td>
                    </tr>
                    <tr>
                        <td width="5%" valign="top">
                            <b>Browsers:</b>
                        </td>
                        <td width="45%" valign="top">
                            <jsp:include page="browsers.jsp">
                                <jsp:param name="multipleBrowsersAllowed" value="true"/>
                            </jsp:include>
                        </td>
                    </tr>
                    <tr>
                        <td width="5%" valign="top">
                            <b>Skin:</b>
                        </td>
                        <td width="45%" valign="top">
                            <jsp:include page="skin.jsp"/>
                        </td>
                    </tr>
                    <%if (server.getConfiguration().useCaptcha()) {%>
                    <tr>
                        <td nowrap width="5%" valign="top"><b>Enter text:</b></td>
                        <td width="45%" valign="top">
                            <jsp:include page="captcha.jsp"/>
                        </td>
                    </tr>
                    <%}%>
                </table>
            </td>
        </tr>
    </table>
</form>
<b>Test results:</b>
<iframe name="resultsFrame" width="100%" height="250" src="/jsunit/app/emptyPage.html"></iframe>

</body>
</html>