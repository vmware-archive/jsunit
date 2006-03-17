var versionRequest;

function isOutOfDate(newVersionNumber) {
    return JSUNIT_VERSION < newVersionNumber;
}

function sendRequestForLatestVersion() {
    versionRequest = createXmlHttpRequest();
    if (versionRequest) {
        versionRequest.onreadystatechange = requestStateChanged;
        try {
            versionRequest.open("GET", "http://www.jsunit.net/version.txt", true);
            versionRequest.send(null);
        } catch (exception) {
            alert(exception);
        }
    }
}

function createXmlHttpRequest() {
    if (window.XMLHttpRequest) {
        var result = new XMLHttpRequest();
        if (netscape) {
            if (!netscape.security.PrivilegeManager.isPrivilegeEnabled("UniversalBrowserRead"))
                return null;
        }
        return result;
    }
    else if (window.ActiveXObject)
        return new ActiveXObject("Microsoft.XMLHTTP");
}

function requestStateChanged() {
    if (versionRequest && versionRequest.readyState == 4 && versionRequest.status == 200) {
        var latestVersion = versionRequest.responseText;
        if (isOutOfDate(latestVersion))
            showOutOfDateMessage(latestVersion);
    }
}

function showOutOfDateMessage(latestVersion) {
}