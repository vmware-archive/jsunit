<%@ page import="net.jsunit.JsUnitServer" %>
<%@ page import="net.jsunit.ServerRegistry" %>
<%JsUnitServer server = ServerRegistry.getServer();%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>JsUnit Server - UploadRunner</title>
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

    </script>
    <link rel="stylesheet" type="text/css" href="./css/jsUnitStyle.css">
</head>

<body bgcolor="#e4ecec">
<form action="/jsunit/runner" method="post" target="resultsFrame" enctype="multipart/form-data">
    <jsp:include page="header.jsp"/>
    <table cellpadding="0" cellspacing="0" width="100%" bgcolor="#FFFFFF">
        <jsp:include page="tabRow.jsp">
            <jsp:param name="selectedPage" value="uploadRunner"/>
        </jsp:include>
        <tr>
            <td colspan="13" style="border-style: solid;border-bottom-width:1px;border-top-width:0px;border-left-width:1px;border-right-width:1px;border-color:#000000;">
                <table>
                    <tr>
                        <td colspan="3">
                            You can upload a Test Page and ask the server to run it using the <i>upload runner</i>
                            service.
                            Select your Test Page and any referenced .js files below; choose specific browsers and a
                            skin if desired.
                            <br/>
                            <br/>
                        </td>
                    </tr>
                    <tr>
                        <td nowrap width="10%" valign="top">
                            <b>Test Page:</b>
                        </td>
                        <td width="10%">
                            <input type="file" name="testPageFile" size="50">
                        </td>
                        <td width="80%">
                            <input type="submit" value="Run">
                        </td>
                    </tr>
                    <%

                        String countString = request.getParameter("referencedJsFileFieldCount");
                        if (countString != null) {
                            int count = Integer.parseInt(countString);
                            for (int i = 0; i < count; i++) {

                    %>
                    <tr id="defaultReferencedJsFileField<%=i%>">
                        <td>.js file</td>
                        <td align="left" width="1"><input type="file" name="referencedJsFiles"></td>
                        <td>
                            <a href="javascript:removeReferencedJsFile('defaultReferencedJsFileField<%=i%>')">[remove]</a>
                        </td>
                    </tr>
                    <%
                            }
                        }

                    %>
                    <tr id="addReferencedJsFileRow">
                        <td>&nbsp;</td>
                        <td colspan="2">
                            <a href="javascript:addReferencedJsFile()" id="addReferencedJsFile">
                                add referenced .js file
                            </a>
                        </td>
                    </tr>
                </table>
                <table>
                    <tr>
                        <td valign="top">
                            <jsp:include page="browserAndSkin.jsp">
                                <jsp:param name="multipleBrowsersAllowed" value="true"/>
                            </jsp:include>
                        </td>
                        <td width="100">&nbsp;</td>
                        <td valign="top">
                            <%if (server.getConfiguration().useCaptcha()) {%>
                            <jsp:include page="captcha.jsp"/>
                            <%}%>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
</form>

<b>Test results:</b>
<iframe name="resultsFrame" width="100%" height="250" src="/jsunit/app/emptyPage.html"></iframe>

</body>
</html>