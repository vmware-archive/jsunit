var versionRequest;

function isOutOfDate(newVersionNumber) {
    return JSUNIT_VERSION < newVersionNumber;
}

function sendRequestForLatestVersion() {
    versionRequest = createXmlHttpRequest();
    if (versionRequest) {
        versionRequest.onreadystatechange = requestStateChanged;
        try {
            netscape.security.PrivilegeManager.enablePrivilege("UniversalBrowserRead");
        } catch (e) {
        }
        versionRequest.open("GET", "http://www.jsunit.net/version.txt", true);
        versionRequest.send(null);
    }
}

function createXmlHttpRequest() {
    if (window.XMLHttpRequest)
        return new XMLHttpRequest();
    else if (window.ActiveXObject)
        return new ActiveXObject("Microsoft.XMLHTTP");
}

function requestStateChanged() {
    if (versionRequest && versionRequest.readyState == 4 && versionRequest.status == 200) {
        var latestVersion = versionRequest.responseText;
        if (isOutOfDate(latestVersion))
            versionNotLatest(latestVersion);
        else
            versionLatest();
    }
}

function checkForLatestVersion() {
    setLatestVersionDivHTML("Checking for newer version...");
    try {
        sendRequestForLatestVersion();
    } catch (e) {
        setLatestVersionDivHTML("An error occurred while checking for a newer version: " + e.message);
    }
}

function versionNotLatest(latestVersion) {
    setLatestVersionDivHTML('<font color="red">A newer version of JsUnit, version ' + latestVersion + ', is available.</font>');
}

function versionLatest() {
    setLatestVersionDivHTML("You are running the latest version of JsUnit.");
}

function setLatestVersionDivHTML(string) {
    document.getElementById("versionCheckDiv").innerHTML = string;
}
