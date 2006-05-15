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
<td colspan="16" style="border-style: solid;border-bottom-width:1px;border-top-width:0px;border-left-width:1px;border-right-width:1px;border-color:#000000;" align="left">
<table width="100%" align="left" border="0">
<tr>
    <td colspan="2">
        <h4>Create a JsUnit account</h4>
    </td>
    <td width="1%" rowspan="20">&nbsp;</td>
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
                            </ul>
                        </td>
                    </tr>
                </table>
            </div>

            <div class="rb1bot"><div></div></div></div>
    </td>
    <td width="1%" rowspan="20">&nbsp;</td>

</tr>
<tr>
    <td width="15%" align="right" valign="middle">
        <b>First name:</b>
    </td>
    <td>
        <input type="text" name="user.firstName">
    </td>
</tr>
<tr>
    <td align="right" valign="middle">
        <b>Last name:</b>
    </td>
    <td>
        <input type="text" name="user.lastName">
    </td>
</tr>
<tr>
    <td align="right" valign="middle">
        <b>Email address:</b>
    </td>
    <td>
        <input type="text" name="user.emailAddress">
    </td>
</tr>
<tr>
    <td align="right" valign="middle">
        <b>Password:</b>
    </td>
    <td>
        <input type="text" name="password1">
    </td>
</tr>
<tr>
    <td align="right" valign="middle">
        <b>Confirm password:</b>
    </td>
    <td>
        <input type="text" name="password2">
    </td>
</tr>
<tr>
    <td colspan="2"></td>
</tr>
<tr>
    <td>
        &nbsp;
    </td>
    <td>
        <input type="submit" value="Create account" class="button">
    </td>
</tr>
<tr>
    <td colspan="2"></td>
</tr>
</table>
</td>
</tr>
</table>
</form>
</body>
</html>