MockXmlHttpRequest = function() {
    this.requestHeaderNamesToValues = {};
}

MockXmlHttpRequest.prototype.open = function(method, url, isAsync, userName, password) {
    this.method = method;
    this.url = url;
    this.isAsync = isAsync;
    this.userName = userName;
    this.password = password;
}

MockXmlHttpRequest.prototype.send = function(data) {
    this.sendCalled = true;
    this.data = data;
}

MockXmlHttpRequest.prototype.setRequestHeader = function(label, value) {
    this.requestHeaderNamesToValues[label] = value;
}

function createXmlHttpRequest() {
    return new MockXmlHttpRequest();
}