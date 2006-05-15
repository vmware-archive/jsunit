<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Create account - JsUnit</title>
    <link rel="stylesheet" type="text/css" href="./css/jsUnitStyle.css">
</head>

<body bgcolor="#eeeeee">
<form action="/jsunit/processcreateaccount" method="post">
<jsp:include page="header.jsp"/>
<table cellpadding="0" cellspacing="0" width="100%" bgcolor="#FFFFFF">
<jsp:include page="tabRow.jsp">
    <jsp:param name="selectedPage" value="myAccount"/>
</jsp:include>
<tr>
<td colspan="13" style="border-style: solid;border-bottom-width:1px;border-top-width:0px;border-left-width:1px;border-right-width:1px;border-color:#000000;" align="left">
<table width="100%" align="left">
<tr>
    <td colspan="*"></td>
</tr>
<tr>
    <td width="24%" colspan="2" nowrap>
        <b>Create a JsUnit account:</b>
    </td>
    <td width="1%"></td>
    <td width="25%" colspan="2" nowrap>
        <b>Aleady have a JsUnit account? Sign in:</b>
    </td>
    <td width="1%" rowspan="20"></td>
    <td width="48%" rowspan="20" valign="top">
        <div class="rb1roundbox">
            <div class="rb1top"><div></div></div>

            <div class="rb1content">
                <table width="100%">
                    <tr>
                        <td colspan="5" align="center">
                            <div class="rb3roundbox">
                                <div class="rb3top"><div></div></div>

                                <div class="rb3content">
                                    <img src="/jsunit/images/question_mark.gif" alt="What is the FragmentRunner service?" title="What is the FragmentRunner service?" border="0">
                                    <b>Why should I create a JsUnit account?</b>
                                </div>

                                <div class="rb3bot"><div></div></div></div>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            A JsUnit account gives you the following benefits:
                            <ul>
                                <li>
                                    <b>access to the JsUnit services as web services over SOAP</b>. Using the
                                    TestRunService (<a href="/services/TestRunService?wsdl">see WSDL</a>), you can run
                                    your tests programmatically. A <b>Java client</b> is available that wraps your run
                                    in a
                                    JUnit test suite. A <b>Ruby client</b> is in development.
                                </li>
                                <li>
                                    <b>unlimited access to the JsUnit manual services</b> with any number of browsers
                                    and no need to enter the CAPTCHA text.
                                </li>
                                <li>
                                    An <b>increased upper limit on test run times</b> (various levels are available)
                                </li>
                                <li>
                                    The ability to run test <b>suites</b> on the manual upload runner
                                </li>
                            </ul>
                        </td>
                    </tr>
                </table>
            </div>

            <div class="rb1bot"><div></div></div></div>
    </td>
    <td width="1%" rowspan="20"></td>

</tr>
<tr>
    <td align="right" valign="middle" nowrap>
        <b>First name:</b>
    </td>
    <td>
        <input type="text" name="user.firstName" tabindex="1">
    </td>
    <td></td>
    <td nowrap align="right">
        <b>Email address:</b>
    </td>
    <td>
        <input type="text" name="username" tabindex="101">
    </td>
</tr>
<tr>
    <td align="right" valign="middle" nowrap>
        <b>Last name:</b>
    </td>
    <td>
        <input type="text" name="user.lastName" tabindex="2">
    </td>
    <td></td>
    <td align="right" nowrap>
        <b>Password:</b>
    </td>
    <td>
        <input type="password" name="username" tabindex="102">
    </td>
</tr>
<tr>
    <td align="right" valign="middle" nowrap>
        <b>Email address:</b>
    </td>
    <td>
        <input type="text" name="user.emailAddress" tabindex="3">
    </td>
    <td></td>
    <td></td>
    <td>
        <input type="submit" value="Sign in" class="button" tabindex="103">
    </td>
</tr>
<tr>
    <td align="right" valign="middle" nowrap>
        <b>Password:</b>
    </td>
    <td>
        <input type="password" name="password1" tabindex="4">
    </td>
    <td colspan="3"></td>
</tr>
<tr>
    <td align="right" valign="middle" nowrap>
        <b>Confirm password:</b>
    </td>
    <td>
        <input type="password" name="password2" tabindex="5">
    </td>
    <td colspan="3"></td>
</tr>
<tr>
    <td colspan="5"></td>
</tr>
<tr>
    <td></td>
    <td>
        <input type="submit" value="Create account" class="button" tabindex="6">
    </td>
    <td colspan="3"></td>
</tr>
<tr>
    <td colspan="6"></td>
</tr>
</table>
</td>
</tr>
</table>
</form>
</body>
</html>