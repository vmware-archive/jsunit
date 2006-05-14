<%@ page import="net.jsunit.JsUnitAggregateServer" %>
<%@ page import="net.jsunit.ServerRegistry" %>
<%JsUnitAggregateServer server = ServerRegistry.getAggregateServer();%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>UploadRunner - JsUnit</title>
    <script type="text/javascript" src="app/jsUnitCore.js"></script>
    <script type="text/javascript" src="app/server/jsUnitVersionCheck.js"></script>
    <script type="text/javascript">

        function removeReferencedJsFile(anId) {
            var theRow = document.getElementById(anId);
            theRow.parentNode.removeChild(theRow);
        }

        function addReferencedJsFile() {
            var id = new Date().getTime();

            var rowNode = document.createElement("tr");
            rowNode.setAttribute("id", id);
            var leftCellNode = document.createElement("td");
            var boldElement = document.createElement("b");
            var textNode = document.createTextNode(".js file:");
            boldElement.appendChild(textNode);
            leftCellNode.appendChild(boldElement);

            rowNode.appendChild(leftCellNode);

            var middleCellNode = document.createElement("td");
            middleCellNode.setAttribute("width", "1");
            var fileUploadField = document.createElement("input");
            fileUploadField.setAttribute("type", "file");
            fileUploadField.setAttribute("size", "50");
            fileUploadField.setAttribute("name", "referencedJsFiles");
            middleCellNode.appendChild(fileUploadField);

            rowNode.appendChild(middleCellNode);

            var rightCellNode = document.createElement("td");
            rightCellNode.setAttribute("align", "left");
            var removeLink = document.createElement("a");
            removeLink.setAttribute("href", "javascript:removeReferencedJsFile(\"" + id + "\")");
            removeLink.appendChild(document.createTextNode("[remove]"));
            rightCellNode.appendChild(removeLink);

            rowNode.appendChild(rightCellNode);

            var addReferencedJsFileRowNode = document.getElementById("addReferencedJsFileRow");
            var rowParentNode = addReferencedJsFileRowNode.parentNode;
            rowParentNode.insertBefore(rowNode, addReferencedJsFileRowNode);
        }

        function verifyRequiredFieldsEntered() {
            if (document.getElementById("testPageFile").value == "") {
                alert("Please choose a Test Page.")
                document.getElementById("testPageFile").focus();
                return false;
            }
            if (!atLeastOneBrowserIsChecked()) {
                alert("Please choose 1 or more browsrs.")
                return false;
            }
        <%if (server.getConfiguration().useCaptcha()) {%>
            if (document.getElementById("attemptedCaptchaAnswer").value == "") {
                alert("Please enter the CAPTCHA text.");
                document.getElementById("attemptedCaptchaAnswer").focus();
                return false;
            }
        <%}%>
            return true;
        }

        function atLeastOneBrowserIsChecked() {
            var browserCheckboxes = document.forms[0]["urlId_browserId"];
            for (var i = 0; i < browserCheckboxes.length; i++) {
                var browserCheckbox = browserCheckboxes[i];
                if (browserCheckbox.checked)
                    return true;
            }
            return false;
        }
    </script>
    <link rel="stylesheet" type="text/css" href="./css/jsUnitStyle.css">
</head>

<body bgcolor="#eeeeee">
<form action="/jsunit/runner" method="post" target="resultsFrame" enctype="multipart/form-data">
<jsp:include page="header.jsp"/>
<table cellpadding="0" cellspacing="0" width="100%" bgcolor="#FFFFFF">
<jsp:include page="tabRow.jsp">
    <jsp:param name="selectedPage" value="uploadRunner"/>
</jsp:include>
<tr>
<td colspan="16" style="border-style: solid;border-bottom-width:1px;border-top-width:0px;border-left-width:1px;border-right-width:1px;border-color:#000000;">
<table>
<tr>
    <td nowrap width="5%" valign="top">
        <b>Test Page:</b>
    </td>
    <td width="22%" valign="top">
        <input type="file" name="testPageFile" id="testPageFile" size="50">
    </td>
    <td width="23%" valign="top">
        <input type="submit" value="Run" onclick="return verifyRequiredFieldsEntered()">
    </td>
    <td width="1%" rowspan="50">&nbsp;</td>
    <td width="48%" rowspan="50" valign="top">
        <div class="rb1roundbox">
            <div class="rb1top"><div></div></div>

            <div class="rb1content">
                <table width="100%">
                    <tr>
                        <td align="center">
                            <div class="rb3roundbox">
                                <div class="rb3top"><div></div></div>

                                <div class="rb3content">
                                    <img src="/jsunit/images/question_mark.gif" alt="What is the UploadRunner service?" title="What is the UploadRunner service?" border="0">
                                    <b>What is the UploadRunner service?</b>
                                </div>

                                <div class="rb3bot"><div></div></div></div>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            You can ask this JsUnit Server to run JsUnit on your Test Pages using the <i>upload
                            runner</i> service. Select your Test Page and associated .js files (if any), choose which
                            browsers you want to run your Test Page on and which skin you want your results displayed
                            in, and press "Run".
                            <br>
                            <br>
                            The upload runner service is useful for manually running your Test Pages, but it's a pain
                            to have to browse to your Test Page and its .js files. If you use the upload runner service
                            frequently,
                            you'll probably be interested in JsUnit's automated web services and the JsUnit client.
                            <a href="#">Learn more</a>.
                            <br>
                            <br>
                            <font size="-2">Note: uploaded Test Pages are not permanently stored on our servers.
                                <a href="#">Learn more</a>.</font>
                        </td>
                    </tr>
                </table>
            </div>

            <div class="rb1bot"><div></div></div></div>
    </td>
    <td width="1%" rowspan="50">&nbsp;</td>
</tr>
<%

    String countString = request.getParameter("referencedJsFileFieldCount");
    if (countString != null) {
        int count = Integer.parseInt(countString);
        for (int i = 0; i < count; i++) {

%>
<tr id="defaultReferencedJsFileField<%=i%>">
    <td width="5%">.js file</td>
    <td width="22%" align="left"><input type="file" name="referencedJsFiles"></td>
    <td width="23%">
        <a href="javascript:removeReferencedJsFile('defaultReferencedJsFileField<%=i%>')">[remove]</a>
    </td>
</tr>
<%
        }
    }

%>
<tr id="addReferencedJsFileRow">
    <td width="5%">&nbsp;</td>
    <td width="45%" colspan="2" valign="top">
        <a href="javascript:addReferencedJsFile()" id="addReferencedJsFile">
            add referenced .js file
        </a>
    </td>
</tr>

<tr>
    <td width="5%" valign="top">
        <b>Browsers:</b>
    </td>
    <td width="45%" valign="top" colspan="2">
        <jsp:include page="browsers.jsp">
            <jsp:param name="multipleBrowsersAllowed" value="true"/>
        </jsp:include>
    </td>
</tr>
<tr>
    <td width="5%" valign="top">
        <b>Skin:</b>
    </td>
    <td width="45%" valign="top" colspan="2">
        <jsp:include page="skin.jsp"/>
    </td>
</tr>
<%if (server.getConfiguration().useCaptcha()) {%>
<tr>
    <td width="5%" nowrap valign="top"><b>Enter text:</b></td>
    <td width="45%" valign="top" colspan="2">
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