<%String selectedPage = request.getParameter("selectedPage");%>
<tr>
    <td class="tabHeaderSeparator">&nbsp;</td>
    <%if (selectedPage.equals("fragmentRunner")) {%>
    <td class="selectedTab" nowrap>
        <table cellpadding="0" cellspacing="0">
            <tr>
                <td>&nbsp;&nbsp;</td>
                <td valign="middle"><img src="/jsunit/images/fragment.gif" alt="Fragment Runner" title="Fragment Runner"></td>
                <td nowrap valign="middle">&nbsp;Fragment Runner&nbsp;&nbsp;</td>
            </tr>
        </table>
    </td>
    <%} else {%>
    <td class="unselectedTab" nowrap>
        <table cellpadding="0" cellspacing="0">
            <tr>
                <td>&nbsp;&nbsp;</td>
                <td valign="middle"><img src="/jsunit/images/fragment.gif" alt="Fragment Runner" title="Fragment Runner"></td>
                <td nowrap valign="middle">&nbsp;<a href="fragmentrunnerpage">Fragment Runner</a>&nbsp;&nbsp;</td>
            </tr>
        </table>
    </td>
    <%}%>

    <td class="tabHeaderSeparator">&nbsp;</td>
    <%if (selectedPage.equals("uploadRunner")) {%>
    <td class="selectedTab" nowrap>
        <table cellpadding="0" cellspacing="0">
            <tr>
                <td>&nbsp;&nbsp;</td>
                <td valign="middle"><img src="/jsunit/images/upload.gif" alt="Upload Runner" title="Upload Runner"></td>
                <td nowrap valign="middle">&nbsp;Upload Runner&nbsp;&nbsp;</td>
            </tr>
        </table>
    </td>
    <%} else {%>
    <td class="unselectedTab" nowrap>
        <table cellpadding="0" cellspacing="0">
            <tr>
                <td>&nbsp;&nbsp;</td>
                <td valign="middle"><img src="/jsunit/images/upload.gif" alt="Upload Runner" title="Upload Runner"></td>
                <td nowrap valign="middle">&nbsp;<a href="uploadrunnerpage">Upload Runner</a>&nbsp;&nbsp;</td>
            </tr>
        </table>
    </td>
    <%}%>

    <td class="tabHeaderSeparator">&nbsp;</td>
    <%if (selectedPage.equals("urlRunner")) {%>
    <td class="selectedTab" nowrap>
        <table cellpadding="0" cellspacing="0">
            <tr>
                <td>&nbsp;&nbsp;</td>
                <td valign="middle"><img src="/jsunit/images/url.gif" alt="URL Runner" title="URL Runner"></td>
                <td nowrap valign="middle">&nbsp;URL Runner&nbsp;&nbsp;</td>
            </tr>
        </table>
    </td>
    <%} else {%>
    <td class="unselectedTab" nowrap>
        <table cellpadding="0" cellspacing="0">
            <tr>
                <td>&nbsp;&nbsp;</td>
                <td valign="middle"><img src="/jsunit/images/url.gif" alt="URL Runner" title="URL Runner"></td>
                <td nowrap valign="middle">&nbsp;<a href="urlrunnerpage">URL Runner</a>&nbsp;&nbsp;</td>
            </tr>
        </table>
    </td>
    <%}%>

    <td class="tabHeaderSeparator">&nbsp;</td>
    <%if (selectedPage.equals("myAccount")) {%>
    <td class="selectedTab" nowrap valign="middle">
        <table cellpadding="0" cellspacing="0">
            <tr>
                <td>&nbsp;&nbsp;</td>
                <td valign="middle"><img src="/jsunit/images/myaccount.gif" alt="My Account" title="My Account"></td>
                <td nowrap valign="middle">&nbsp;My Account&nbsp;&nbsp;</td>
            </tr>
        </table>
    </td>
    <%} else {%>
    <td class="unselectedTab" nowrap valign="middle">
        <table cellpadding="0" cellspacing="0">
            <tr>
                <td>&nbsp;&nbsp;</td>
                <td valign="middle"><img src="/jsunit/images/myaccount.gif" alt="My Account" title="My Account"></td>
                <td nowrap valign="middle">&nbsp;<a href="myaccountpage">My Account</a>&nbsp;&nbsp;</td>
            </tr>
        </table>
    </td>
    <%}%>

    <td class="tabHeaderSeparator">&nbsp;</td>
    <%if (selectedPage.equals("help")) {%>
    <td class="selectedTab" nowrap>
        <table cellpadding="0" cellspacing="0">
            <tr>
                <td>&nbsp;&nbsp;</td>
                <td valign="middle"><img src="/jsunit/images/question_mark.gif" alt="Help" title="Help"></td>
                <td nowrap valign="middle">&nbsp;Help&nbsp;&nbsp;</td>
            </tr>
        </table>
    </td>
    <%} else {%>
    <td class="unselectedTab" nowrap>
        <table cellpadding="0" cellspacing="0">
            <tr>
                <td>&nbsp;&nbsp;</td>
                <td valign="middle"><img src="/jsunit/images/question_mark.gif" alt="Help" title="Help"></td>
                <td nowrap valign="middle">&nbsp;<a href="helppage">Help</a>&nbsp;&nbsp;</td>
            </tr>
        </table>
    </td>
    <%}%>

    <td class="tabHeaderSeparator" width="70%">&nbsp;</td>
</tr>
