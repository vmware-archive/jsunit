<%@ page import="net.jsunit.JsUnitServer" %>
<%@ page import="net.jsunit.ServerRegistry" %>
<%@ page import="net.jsunit.captcha.CaptchaSpec" %>
<%@ page import="java.net.URLEncoder" %>
<%
    JsUnitServer server = ServerRegistry.getServer();
    CaptchaSpec captchaSpec = CaptchaSpec.create(server.getConfiguration().getSecretKey());
    String key = captchaSpec.getEncryptedKey();
%>

<script type="text/javascript" src="/jsunit/app/server/jsUnitServerUtilities.js"></script>
<input type="hidden" name="captchaKey" value="<%=key%>">
<img width="200" src="/jsunit/captchaImage?captchaKey=<%=URLEncoder.encode(key, "UTF-8")%>" title="CAPTCHA image" alt="CAPTCHA image">
<table cellspacing="0" cellpadding="0" border="0">
    <tr>
        <td nowrap valign="top">
            <b>Enter text:</b>
        </td>
        <td valign="top">
            <input type="text" size="7" name="attemptedCaptchaAnswer" autocomplete="off">
        </td>
        <td nowrap align="left" valign="top" width="80%">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td>&nbsp;</td>
                    <td>
                        <a href="javascript:toggleVisibility('whatIsCaptchaDiv', 'whatIsCaptchaLinkDiv', 'what\'s this?')">
                            <img src="/jsunit/images/question_mark.gif" alt="what's this?" title="what's this?" border="0">
                        </a>
                    </td>
                    <td>
                        <font size="-2">
                            <a href="javascript:toggleVisibility('whatIsCaptchaDiv', 'whatIsCaptchaLinkDiv', 'what\'s this?')">
                                <div id="whatIsCaptchaLinkDiv" style="text-decoration:underline;">what's this?</div>
                            </a>
                        </font>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
<div class="rb1roundbox" id="whatIsCaptchaDiv" style="visibility:hidden;vertical-align:top;">
    <div class="rb1top"><div></div></div>

    <div class="rb1content" align="center">
        JsUnit uses a device called a CAPTCHA to prohibit anonymous programmatic access.
        Enter the text you see in the box.<br>
        <font size="-2">CAPTCHAs expire after &frac12; hour. Tired of the CAPTCHA? <b><a href="#">Sign up</a></b> for a JsUnit account!</font>
    </div>

    <div class="rb1bot"><div></div></div>
</div>
