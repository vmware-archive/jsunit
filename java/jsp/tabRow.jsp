<%@ page import="net.jsunit.JsUnitServer" %>
<%@ page import="net.jsunit.ServerRegistry" %>
<%JsUnitServer server = ServerRegistry.getServer();%>
<%String selectedPage = request.getParameter("selectedPage");%>
<tr>
    <td class="tabHeaderSeparator">&nbsp;</td>
    <%if (selectedPage.equals("fragmentRunner")) {%>
    <td class="selectedTab">
        &nbsp;&nbsp;FragmentRunner&nbsp;&nbsp;
    </td>
    <%} else {%>
    <td class="unselectedTab">
        &nbsp;&nbsp;<a href="fragmentRunnerPage">FragmentRunner</a>&nbsp;&nbsp;
    </td>
    <%}%>

    <td class="tabHeaderSeparator">&nbsp;</td>
    <%if (selectedPage.equals("uploadRunner")) {%>
    <td class="selectedTab">
        &nbsp;&nbsp;UploadRunner&nbsp;&nbsp;
    </td>
    <%} else {%>
    <td class="unselectedTab">
        &nbsp;&nbsp;<a href="uploadRunnerPage">UploadRunner</a>&nbsp;&nbsp;
    </td>
    <%}%>

    <td class="tabHeaderSeparator">&nbsp;</td>
    <%if (selectedPage.equals("urlRunner")) {%>
    <td class="selectedTab">
        &nbsp;&nbsp;URLRunner&nbsp;&nbsp;
    </td>
    <%} else {%>
    <td class="unselectedTab">
        &nbsp;&nbsp;<a href="urlRunnerPage">URLRunner</a>&nbsp;&nbsp;
    </td>
    <%}%>

    <td class="tabHeaderSeparator">&nbsp;</td>
    <%if (false) {%>
    <%if (selectedPage.equals("logDisplayer")) {%>
    <td class="selectedTab">
        &nbsp;&nbsp;LogDisplayer&nbsp;&nbsp;
    </td>
    <%} else {%>
    <td class="unselectedTab">
        &nbsp;&nbsp;<a href="logDisplayerPage">LogDisplayer</a>&nbsp;&nbsp;
    </td>
    <%}%>
    <td class="tabHeaderSeparator">&nbsp;</td>
    <%}%>

    <td class="tabHeaderSeparator" width="99%">&nbsp;</td>
</tr>
