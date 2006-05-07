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

<table cellpadding="0" cellspacing="0">
    <tr>
        <td valign="top" colspan="3">
            <img width="200" src="/jsunit/captchaImage?captchaKey=<%=URLEncoder.encode(key, "UTF-8")%>" title="CAPTCHA image" alt="CAPTCHA image">
        </td>
        <td>&nbsp;</td>
        <td nowrap valign="top">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td colspan="3">
                        <input type="text" size="10" name="attemptedCaptchaAnswer" autocomplete="off">
                    </td>
                </tr>
                <tr>
                    <td align="right" valign="middle">
                        <a href="javascript:toggleVisibility('whatIsCaptchaDiv', 'whatIsCaptchaLinkDiv', 'what\'s this?')">
                            <img src="/jsunit/images/question_mark.gif" alt="what's this?" title="what's this?" border="0">
                        </a>
                    </td>
                    <td></td>
                    <td align="left" valign="middle">
                        <font size="-2">
                            <a href="javascript:toggleVisibility('whatIsCaptchaDiv', 'whatIsCaptchaLinkDiv', 'what\'s this?')">
                                <div id="whatIsCaptchaLinkDiv" style="text-decoration:underline;text-align:left">
                                    what's this?
                                </div>
                            </a>
                        </font>
                    </td>
                </tr>
            </table>
        </td>
        <td valign="top">
            <div class="rb1roundbox" id="whatIsCaptchaDiv" style="z-index:2;visibility:hidden;vertical-align:top;margin:1px;">
                <div class="rb1top"><div></div></div>

                <div class="rb1content">
                    JsUnit uses a device called a CAPTCHA to prohibit anonymous programmatic access to public services.
                    Enter the text you see in the box.<br>
                    <font size="-2">CAPTCHAs expire after &frac12; hour. Tired of the CAPTCHA?
                        <b><a href="#">Sign up</a></b> for a JsUnit account!</font>
                </div>

                <div class="rb1bot"><div></div></div>
            </div>
        </td>
    </tr>
</table>
