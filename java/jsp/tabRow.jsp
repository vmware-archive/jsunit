<%String selectedPage = request.getParameter("selectedPage");%>
<tr>
    <td class="tabHeaderSeparator">&nbsp;</td>
    <%if (selectedPage.equals("fragmentRunner")) {%>
    <td class="selectedTab" nowrap>
        &nbsp;&nbsp;<img src="/jsunit/images/fragment.gif" alt="FragmentRunner" title="FragmentRunner">&nbsp;FragmentRunner&nbsp;&nbsp;
    </td>
    <%} else {%>
    <td class="unselectedTab" nowrap>
        &nbsp;&nbsp;<img src="/jsunit/images/fragment.gif" alt="FragmentRunner" title="FragmentRunner">&nbsp;<a href="fragmentRunnerPage">FragmentRunner</a>&nbsp;&nbsp;
    </td>
    <%}%>

    <td class="tabHeaderSeparator">&nbsp;</td>
    <%if (selectedPage.equals("uploadRunner")) {%>
    <td class="selectedTab" nowrap>
        &nbsp;&nbsp;<img src="/jsunit/images/upload.gif" alt="UploadRunner" title="UploadRunner">&nbsp;UploadRunner&nbsp;&nbsp;
    </td>
    <%} else {%>
    <td class="unselectedTab" nowrap>
        &nbsp;&nbsp;<img src="/jsunit/images/upload.gif" alt="UploadRunner" title="UploadRunner">&nbsp;<a href="uploadRunnerPage">UploadRunner</a>&nbsp;&nbsp;
    </td>
    <%}%>

    <td class="tabHeaderSeparator">&nbsp;</td>
    <%if (selectedPage.equals("urlRunner")) {%>
    <td class="selectedTab" nowrap>
        &nbsp;&nbsp;<img src="/jsunit/images/url.gif" alt="URLRunner" title="URLRunner">&nbsp;URLRunner&nbsp;&nbsp;
    </td>
    <%} else {%>
    <td class="unselectedTab" nowrap>
        &nbsp;&nbsp;<img src="/jsunit/images/url.gif" alt="URLRunner" title="URLRunner">&nbsp;<a href="urlRunnerPage">URLRunner</a>&nbsp;&nbsp;
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

    <%if (selectedPage.equals("help")) {%>
    <td class="selectedTab" nowrap>
        &nbsp;&nbsp;<img src="/jsunit/images/question_mark.gif" alt="Help/About" title="Help/About">&nbsp;Help/About&nbsp;&nbsp;
    </td>
    <%} else {%>
    <td class="unselectedTab" nowrap>
        &nbsp;&nbsp;<img border="0" src="/jsunit/images/question_mark.gif" alt="Help/About" title="Help/About">&nbsp;<a href="helpPage">Help/About</a>&nbsp;&nbsp;
    </td>
    <%}%>
    <td class="tabHeaderSeparator" width="80%">&nbsp;</td>
</tr>
