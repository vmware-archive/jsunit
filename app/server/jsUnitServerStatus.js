var serverStatusUpdater;

ServerStatusUpdater = function() {
    this.statusMessages = Array();
    this.request = null;
    serverStatusUpdater = this;
}

ServerStatusUpdater.prototype.askServerForStatus = function() {
    this.request = createXmlHttpRequest();
    if (this.request) {
        this.request.onreadystatechange = serverStatusRequestStateChanged;
        this.request.open("GET", "serverstatus?cacheBuster=" + new Date().getTime(), true);
        this.request.send(null);
    }
}

function serverStatusRequestStateChanged() {
    serverStatusUpdater.requestStateChanged();
}

ServerStatusUpdater.prototype.requestStateChanged = function() {
    if (this.request && this.request.readyState == 4) {
        if (this.request.status == 200) {
            updateServerStatusDiv(this.request.responseText);
        }
        setTimeout("serverStatusUpdater.askServerForStatus()", 10000);
    }
}

function updateServerStatusDiv(messageString) {
}

function createXmlHttpRequest() {
    if (window.XMLHttpRequest)
        return new XMLHttpRequest();
    else if (window.ActiveXObject)
        return new ActiveXObject("Microsoft.XMLHTTP");
}
