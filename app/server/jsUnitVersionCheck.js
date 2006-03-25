var jsUnitVersionRequest;

function isOutOfDate(newVersionNumber) {
    return JSUNIT_VERSION < newVersionNumber;
}

function sendRequestForLatestVersion(url) {
    jsUnitVersionRequest = createXmlHttpRequest();
    if (jsUnitVersionRequest) {
        jsUnitVersionRequest.onreadystatechange = requestStateChanged;
        jsUnitVersionRequest.open("GET", url, true);
        jsUnitVersionRequest.send(null);
    }
}

function createXmlHttpRequest() {
    if (window.XMLHttpRequest)
        return new XMLHttpRequest();
    else if (window.ActiveXObject)
        return new ActiveXObject("Microsoft.XMLHTTP");
}

function requestStateChanged() {
    if (jsUnitVersionRequest && jsUnitVersionRequest.readyState == 4) {
        if (jsUnitVersionRequest.status == 200) {
            var latestVersion = jsUnitVersionRequest.responseText;
            if (isOutOfDate(latestVersion))
                versionNotLatest(latestVersion);
            else
                versionLatest();
        } else
            versionCheckError();
    }
}

function checkForLatestVersion(url) {
    setLatestVersionDivHTML("Checking for newer version...");
    try {
        sendRequestForLatestVersion(url);
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

function versionCheckError() {
    setLatestVersionDivHTML("An error occurred while checking for a newer version.");
}