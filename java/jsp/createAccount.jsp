<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Create account - JsUnit</title>
    <link rel="stylesheet" type="text/css" href="./css/jsUnitStyle.css">
</head>

<body bgcolor="#eeeeee">
<jsp:include page="header.jsp"/>
<form action="processCreateAccount" method="post">
    <table>
        <tr>
            <td colspan="2">
                <p>
                    <h4>Create JsUnit account</h4>
                </p>
            </td>
        </tr>
        <tr>
            <td align="right" valign="middle">
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
    </table>
</form>
</body>
</html>