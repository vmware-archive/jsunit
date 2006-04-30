<%@ page import="net.jsunit.captcha.AesCipher" %>
<%@ page import="net.jsunit.captcha.CaptchaGenerator" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="net.jsunit.JsUnitServer"%>
<%@ page import="net.jsunit.ServerRegistry"%>
<%
    JsUnitServer server = ServerRegistry.getServer();
    CaptchaGenerator generator = new CaptchaGenerator(server.getSecretKey());
    String answer = generator.generateRandomAnswer();%>

<input type="hidden" name="captchaKey" value="<%=generator.generateKey(System.currentTimeMillis(), answer)%>">
<table>
    <tr>
        <td colspan="2">
            <img src="/jsunit/captchaImage?answer=<%=URLEncoder.encode(new AesCipher(server.getSecretKey()).encrypt(answer), "UTF-8")%>">
        </td>
    </tr>
    <td width="10%" nowrap>
        <b>Enter text:</b>
    </td>
    <td><input type="text" size="15" name="attemptedCaptchaAnswer"></td>
</table>