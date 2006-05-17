<%@ page import="net.jsunit.JsUnitAggregateServer" %>
<%@ page import="net.jsunit.ServerRegistry" %>
<%
    JsUnitAggregateServer server = ServerRegistry.getAggregateServer();
%>
<script type="text/javascript">
    function showSignInDiv() {
        var marketingDiv = document.getElementById("marketingDiv");
        marketingDiv.style.visibility = "hidden";
        marketingDiv.style.position = "absolute";
        var signInDiv = document.getElementById("signInDiv");
        signInDiv.style.visibility = "visible";
        signInDiv.style.position = "";
    }
</script>
<table width="100%" cellpadding="0" cellspacing="0">
<tr>
<td width="1%"></td>
<td width="10%">
    <div class="rb0roundbox">
        <div class="rb0top"><div></div></div>

        <div class="rb0content" align="center">
            <a href="http://www.jsunit.net"><img src="images/logo_jsunit.gif" title="JsUnit" alt="JsUnit" border="0"/></a>
        </div>

        <div class="rb0bot"><div></div></div>
    </div>
</td>
<td width="1%"></td>
<td nowrap align="center" width="50%" valign="top">
    <table cellpadding="0" cellspacing="2">
        <tr>
            <td valign="top">
                <div class="rb0roundbox" width="34">
                    <div class="rb0top"><div></div></div>

                    <div class="rb0content" align="center">
                        <table cellpadding="0" cellspacing="0">
                            <tr>
                                <td align="center">
                                    <b>
                                        JsUnit Online Services
                                    </b>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    The JsUnit Online Services are tools that allow you to run your JsUnit tests on
                                    a variety<br>
                                    of browsers running on multiple operating systems. The services are available in a
                                    manual<br>
                                    form on these pages or in a programmatic form as web services. <a href="helppage">Learn
                                    more</a>.
                                </td>
                            </tr>
                        </table>
                    </div>

                    <div class="rb0bot"><div></div></div>
                </div>
            </td>
        </tr>
    </table>
</td>
<td width="1%"></td>
<td nowrap align="center" valign="top" width="30%">
    <table cellpadding="0" cellspacing="2" height="100%">
        <tr>
            <td>
                <div id="marketingDiv" style="visibility:visible;position:relative">
                    <div class="rb0roundbox">
                        <div class="rb0top"><div></div></div>

                        <div class="rb0content" align="center">
                            <table>
                                <tr>
                                    <td>
                                        You are not signed in.
                                        [<a href="/jsunit/myaccountpage">Sign in</a>]
                                    </td>
                                </tr>
                            </table>
                        </div>

                        <div class="rb0bot"><div></div></div>
                    </div>

                    <div class="rb0roundbox">
                        <div class="rb0top"><div></div></div>

                        <div class="rb0content" align="center">
                            <table>
                                <tr>
                                    <td>
                                        These are the <b>manual</b> services.
                                        <b><a href="myaccountpage">Sign up</a></b> for a JsUnit account<br>
                                        to get access to JsUnit web services using SOAP.
                                    </td>
                                </tr>
                            </table>
                        </div>

                        <div class="rb0bot"><div></div></div>
                    </div>
                </div>

<%--
                <div id="signInDiv" style="visibility:hidden;position:absolute">
                    <div class="rb0roundbox">
                        <div class="rb0top"><div></div></div>

                        <div class="rb0content">
                            <form action="/jsunit/processsignin" method="post">

                                <table>
                                    <tr>
                                        <td nowrap align="right">
                                            Email address:
                                        </td>
                                        <td>
                                            <input type="text" name="username">
                                        </td>
                                    </tr>
                                    <tr>
                                        <td align="right" nowrap>
                                            Password:
                                        </td>
                                        <td>
                                            <input type="password" name="username">
                                        </td>
                                    </tr>
                                    <tr>
                                        <td></td>
                                        <td>
                                            <input type="submit" name="signIn" value="Sign in" class="button">
                                        </td>
                                    </tr>
                                </table>
                            </form>

                        </div>

                        <div class="rb0bot"><div></div></div></div>
                </div>
--%>
            </td>
        </tr>
    </table>
</td>
<td width="1%"></td>
</tr>
</table>
</td>
<td>&nbsp;</td>
</tr>
</table>