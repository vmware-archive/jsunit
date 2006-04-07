<%@ page import="net.jsunit.JsUnitServer" %>
<%@ page import="net.jsunit.ServerRegistry" %>
<%@ page import="net.jsunit.configuration.Configuration" %>
<%@ page import="net.jsunit.configuration.ConfigurationProperty" %>
<%@ page import="net.jsunit.model.Browser" %>
<%@ page import="net.jsunit.results.Skin" %>
<%@ page import="net.jsunit.utility.SystemUtility" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%JsUnitServer server = ServerRegistry.getServer();%>
<%Configuration configuration = server.getConfiguration();%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>JsUnit <%if (server.isFarmServer()) {%> Farm<%}%> Server</title>
    <script type="text/javascript" src="app/jsUnitCore.js"></script>
    <script type="text/javascript" src="app/server/jsUnitVersionCheck.js"></script>
    <script type="text/javascript">

        function selectDiv(selectedDivName) {
            updateDiv("urlRunnerDiv", selectedDivName);
            updateDiv("fragmentRunnerDiv", selectedDivName);
        <%if (!server.isFarmServer()) {%>
            updateDiv("displayerDiv", selectedDivName);
        <%}%>
            updateDiv("testRunnerDiv", selectedDivName);
            updateDiv("configDiv", selectedDivName);
        }

        function updateDiv(divName, selectedDivName) {
            var isSelected = divName == selectedDivName;
            var theDiv = document.getElementById(divName);
            theDiv.style.visibility = isSelected ? "visible" : "hidden";
            theDiv.style.height = isSelected ? "" : "0";

            var theDivHeader = document.getElementById(divName + "Header");
            theDivHeader.className = isSelected ? "selectedTab" : "unselectedTab";
        }

        function pageLoaded() {
            selectDiv("fragmentRunnerDiv");
        }

    </script>
    <link rel="stylesheet" type="text/css" href="./css/jsUnitStyle.css">
</head>

<body onload="pageLoaded()">
<table height="90" width="100%" cellpadding="0" cellspacing="0" border="0" summary="jsUnit Information"
       bgcolor="#DDDDDD">
    <tr>
        <td width="1">
            <a href="http://www.jsunit.net" target="_blank"><img src="images/logo_jsunit.gif" alt="JsUnit" border="0"/></a>
        </td>
        <td width="50">&nbsp;</td>
        <th nowrap align="left">
            <h4>JsUnit <%=SystemUtility.jsUnitVersion()%><%if (server.isFarmServer()) {%> Farm<%}%> Server</h4>
            <font size="-2"><i>Running on <%=SystemUtility.displayString()%>
        </th>
        <td nowrap align="right" valign="middle">
            <font size="-2">
                <b><a href="http://www.jsunit.net/" target="_blank">www.jsunit.net</a></b><br>

                <div id="versionCheckDiv"><a href="javascript:checkForLatestVersion('latestversion')">Check for newer
                    version</a></div>
            </font>
            <br>
            <a href="http://www.pivotalsf.com/" target="top">
                <img border="0" src="images/pivotal.gif" alt="Powered By Pivotal">
            </a>
        </td>

    </tr>
</table>
<br>
<table cellpadding="1" cellspacing="1" border="0" width="100%">
    <tr>
        <td valign="top" width="50%">
            <h4>
                Server configuration
            </h4>
            <table border="0">
                <tr>
                    <th nowrap align="right">Server type:</th>
                    <td width="10">&nbsp;</td>
                    <td><%=server.serverType().getDisplayName()%></td>
                </tr>
                <%
                    List<ConfigurationProperty> propertiesToDisplay = new ArrayList<ConfigurationProperty>();
                    propertiesToDisplay.add(ConfigurationProperty.DESCRIPTION);
                    propertiesToDisplay.add(ConfigurationProperty.BROWSER_FILE_NAMES);
                    propertiesToDisplay.add(ConfigurationProperty.TIMEOUT_SECONDS);
                    for (ConfigurationProperty property : propertiesToDisplay) {
                %>
                <tr>
                    <th nowrap align="right" valign="top"><%=property.getDisplayName()%>:</th>
                    <td width="10">&nbsp;</td>
                    <td valign="top">
                        <%
                            for (String valueString : property.getValueStrings(configuration)) {
                        %><div><%
                        if (valueString != null) {
                            if (property.isURL()) {
                    %><a href="<%=valueString%>"><%=valueString%></a><%
                    } else {
                    %><%=valueString%><%
                            }
                        }
                    %></div><%
                        }
                    %>
                    </td></tr>
                <%
                    }
                %>
            </table>
        </td>
    </tr>
</table>
<br>
<h4>
    Available services
</h4>

<table cellpadding="0" cellspacing="0">
<tr>
    <td class="tabHeaderSeparator">&nbsp;</td>
    <td id="fragmentRunnerDivHeader" class="selectedTab">
        &nbsp;&nbsp;<a href="javascript:selectDiv('fragmentRunnerDiv')">fragmentRunner</a>&nbsp;&nbsp;
    </td>
    <td class="tabHeaderSeparator">&nbsp;</td>
    <td id="urlRunnerDivHeader" class="selectedTab">
        &nbsp;&nbsp;<a href="javascript:selectDiv('urlRunnerDiv')">urlRunner</a>&nbsp;&nbsp;
    </td>
    <td class="tabHeaderSeparator">&nbsp;</td>
    <%if (!server.isFarmServer()) {%>
    <td id="displayerDivHeader" class="unselectedTab">
        &nbsp;&nbsp;<a href="javascript:selectDiv('displayerDiv')">displayer</a>&nbsp;&nbsp;
    </td>
    <td class="tabHeaderSeparator">&nbsp;</td>
    <%}%>
    <td id="testRunnerDivHeader" class="unselectedTab">
        &nbsp;&nbsp;<a href="javascript:selectDiv('testRunnerDiv')">testRunner.html</a>&nbsp;&nbsp;
    </td>
    <td class="tabHeaderSeparator">&nbsp;</td>
    <td id="configDivHeader" class="unselectedTab">
        &nbsp;&nbsp;<a href="javascript:selectDiv('configDiv')">config</a>&nbsp;&nbsp;
    </td>
    <td class="tabHeaderSeparator" width="99%">&nbsp;</td>
</tr>
<tr>
<td colspan="11" style="border-style: solid;border-bottom-width:1px;border-top-width:0px;border-left-width:1px;border-right-width:1px;">

<%if (!server.isFarmServer()) {%>
<div id="fragmentRunnerDiv" style="width:100%;visibility:visible;background:#DDDDDD">
    <br>

    <form action="/jsunit/runner" method="post" name="fragmentRunnerForm">
        <table>
            <tr>
                <td colspan="2">
                    You can ask the server to run JsUnit tests of test code fragments using the <i>fragmentrunner</i>
                    service.
                    Type in your test code fragments below; choose a specific browser if desired.<br/>
                </td>
            </tr>
            <tr>
                <td width="1" valign="top">
                    Fragment:
                </td>
                <td>
                    <textarea name="fragment" cols="50" rows="10"></textarea>
                </td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td>
                    <font size="-2">
                        <i>e.g. assertTrue(true);</i>
                    </font>
                </td>
            </tr>
            <%if (!server.isFarmServer()) {%>
            <tr>
                <td width="1">
                    Browser:
                </td>
                <td>
                    <select name="browserId">
                        <option value="">All browsers</option>
                        <%
                            for (Browser browser : configuration.getBrowsers()) {
                        %><option value="<%=browser.getId()%>"><%=browser.getFileName()%></option>
                        <%}%>
                    </select><br>
                </td>
            </tr>
            <%}%>
            <tr>
                <td width="1">
                    Skin:
                </td>
                <td>
                    <select name="skinId">
                        <option value="">None - pure XML</option>
                        <%
                            for (Skin skin : server.getSkins()) {
                        %><option value="<%=skin.getId()%>"><%=skin.getDisplayName()%></option>
                        <%}%>
                    </select><br>
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <input type="submit" value="go"/>
                </td>
            </tr>
        </table>
    </form>
    <br>&nbsp;
</div>
<%}%>

<div id="urlRunnerDiv" style="width:100%;visibility:hidden;background:#DDDDDD">
    <br>

    <form action="/jsunit/runner" method="get" name="urlRunnerForm">
        <table>
            <tr>
                <td colspan="2">
                    You can ask the server to run JsUnit tests using the <i>runner</i> service.
                    You can run using the server's default URL for tests by going to <a href="/jsunit/runner">runner</a>,
                    or you can specify a custom URL and/or browser ID using this form:
                </td>
            </tr>
            <tr>
                <td width="1">
                    URL:
                </td>
                <td>
                    <input type="text" name="url" size="100" value=""/>
                </td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td>
                    <font size="-2"><i>e.g.
                        http://www.jsunit.net/runner/testRunner.html?testPage=http://www.jsunit.net/runner/tests/jsUnitTestSuite.html</i>
                    </font>
                </td>
            </tr>
            <%if (!server.isFarmServer()) {%>
            <tr>
                <td width="1">
                    Browser:
                </td>
                <td>
                    <select name="browserId">
                        <option value="">All browsers</option>
                        <%
                            for (Browser browser : configuration.getBrowsers()) {
                        %><option value="<%=browser.getId()%>"><%=browser.getFileName()%></option>
                        <%}%>
                    </select><br>
                </td>
            </tr>
            <%}%>
            <tr>
                <td width="1">
                    Skin:
                </td>
                <td>
                    <select name="skinId">
                        <option value="">None - pure XML</option>
                        <%
                            for (Skin skin : server.getSkins()) {
                        %><option value="<%=skin.getId()%>"><%=skin.getDisplayName()%></option>
                        <%}%>
                    </select><br>
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <input type="submit" value="go"/>
                </td>
            </tr>
        </table>
    </form>
    <br>&nbsp;
</div>

<%if (!server.isFarmServer()) {%>
<div id="displayerDiv" style="width:100%;visibility:hidden;background:#DDDDDD">
    <br>

    <form action="/jsunit/displayer" name="displayerForm">
        <table>
            <tr>
                <td colspan="2">
                    You can view the logs of past runs using the <i>displayer</i> service.
                    Use this form to specify the ID of the run you want to see:
                </td>
            </tr>
            <tr>
                <td width="1">
                    ID:
                </td>
                <td>
                    <input type="text" name="id" size="20"/>
                </td>
            </tr>
            <tr>
                <td width="1">
                    Browser:
                </td>
                <td>
                    <select name="browserId">
                        <%
                            for (Browser browser : configuration.getBrowsers()) {
                        %><option value="<%=browser.getId()%>"><%=browser.getFileName()%></option>
                        <%}%>
                    </select><br>
                </td>
            </tr>
            <tr>
                <td width="1">
                    Skin:
                </td>
                <td>
                    <select name="skinId">
                        <option value="">None - pure XML</option>
                        <%
                            for (Skin skin : server.getSkins()) {
                        %><option value="<%=skin.getId()%>"><%=skin.getDisplayName()%></option>
                        <%}%>
                    </select><br>
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <input type="submit" value="go"/>
                </td>
            </tr>
        </table>
    </form>
    <br>&nbsp;
</div>
<%}%>

<div id="testRunnerDiv" style="width:100%;visibility:hidden;background:#DDDDDD">
    <br>
    The manual Test Runner is at <a id="testRunnerHtml" href="./testRunner.html">testRunner.html</a>.
    <br>&nbsp;
</div>

<div id="configDiv" style="width:100%;visibility:hidden;background:#DDDDDD">
    <br>
    You can see the configuration of this server as XML by going to <a id="config"
                                                                       href="/jsunit/config">config</a>.
    The config service is usually only used programmatically.
    <br>&nbsp;
</div>

</td>
</tr>
</table>

</body>
</html>