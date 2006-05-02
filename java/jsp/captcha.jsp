<%@ page import="net.jsunit.JsUnitServer" %>
<%@ page import="net.jsunit.ServerRegistry" %>
<%@ page import="net.jsunit.captcha.CaptchaGenerator" %>
<%@ page import="net.jsunit.captcha.CaptchaSpec" %>
<%@ page import="java.net.URLEncoder" %>
<%
    JsUnitServer server = ServerRegistry.getServer();
    CaptchaSpec captchaSpec = CaptchaSpec.create(server.getConfiguration().getSecretKey());
    String key = captchaSpec.getEncryptedKey();%>

<script language="javascript" src="/jsunit/app/server/jsUnitServerUtilities.js"></script>

<input type="hidden" name="captchaKey" value="<%=key%>">

<table>
    <tr>
        <td colspan="3" width="200" height="50">
            <img src="/jsunit/captchaImage?captchaKey=<%=URLEncoder.encode(key, "UTF-8")%>">
        </td>
        <td rowspan="2" valign="top" width="400">
            <div class="rb1roundbox" id="whatIsCaptchaDiv" style="display:none">
                <div class="rb1top"><div></div></div>

                <div class="rb1content" align="center">
                    JsUnit uses a device called a CAPTCHA to disallow anonymous programmatic access.
                    Enter the text you see in the box before running your tests.
                    CAPTCHAs expire after &frac12; hour.<br>
                    Tired of the CAPTCHA? <b><a href="#">Sign up</a></b> for a JsUnit account!
                </div>

                <div class="rb1bot"><div></div></div>
            </div>
        </td>
    </tr>
    <tr>
        <td nowrap valign="middle" width="66" height="40">
            <b>Enter text:</b>
        </td>
        <td valign="middle" width="67">
            <input type="text" size="7" name="attemptedCaptchaAnswer" autocomplete="off">
        </td>
        <td nowrap align="left" valign="middle" width="66">
            <font size="-2">
                <a href="javascript:toggleVisibility('whatIsCaptchaDiv', 'whatIsCaptchaLinkDiv', 'what\'s this?')">
                    <div id="whatIsCaptchaLinkDiv" style="text-decoration:underline;">what's this?</div>
                </a>
            </font>
        </td>
    </tr>
</table>