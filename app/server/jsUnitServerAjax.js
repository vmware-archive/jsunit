var jsUnitUpdater;

JsUnitServerAjaxUpdater = function() {
    this.statusMessages = Array();
    this.statusRequest = null;
    this.testRunCountRequest = null;
    jsUnitUpdater = this;
}

JsUnitServerAjaxUpdater.prototype.askServerForStatus = function() {
    this.statusRequest = createXmlHttpRequest();
    if (this.statusRequest) {
        this.statusRequest.onreadystatechange = serverStatusRequestStateChanged;
        this.statusRequest.open("GET", "serverstatus?cacheBuster=" + new Date().getTime(), true);
        this.statusRequest.send(null);
    }
}

JsUnitServerAjaxUpdater.prototype.askServerForTestRunCount = function() {
    this.testRunCountRequest = createXmlHttpRequest();
    if (this.testRunCountRequest) {
        this.testRunCountRequest.onreadystatechange = testRunCountRequestStateChanged;
        this.testRunCountRequest.open("GET", "testruncount?cacheBuster=" + new Date().getTime(), true);
        this.testRunCountRequest.send(null);
    }
}

function serverStatusRequestStateChanged() {
    jsUnitUpdater.serverStatusRequestStateChanged();
}

function testRunCountRequestStateChanged() {
    jsUnitUpdater.testRunCountRequestStateChanged();
}

JsUnitServerAjaxUpdater.prototype.serverStatusRequestStateChanged = function() {
    if (this.statusRequest && this.statusRequest.readyState == 4) {
        if (this.statusRequest.status == 200)
            updateServerStatusDiv(this.statusRequest.responseText);
        setTimeout("jsUnitUpdater.askServerForStatus()", 10000);
    }
}

JsUnitServerAjaxUpdater.prototype.testRunCountRequestStateChanged = function() {
    if (this.testRunCountRequest && this.testRunCountRequest.readyState == 4) {
        if (this.testRunCountRequest.status == 200)
            updateTestRunCountDiv(this.testRunCountRequest.responseText);
        setTimeout("jsUnitUpdater.askServerForTestRunCount()", 10000);
    }
}

function updateServerStatusDiv(messageString) {
}

function updateTestRunCountDiv(countString) {
}

function createXmlHttpRequest() {
    if (window.XMLHttpRequest)
        return new XMLHttpRequest();
    else if (window.ActiveXObject)
        return new ActiveXObject("Microsoft.XMLHTTP");
}
