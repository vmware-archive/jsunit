MockXmlHttpRequest = function() {
}

MockXmlHttpRequest.prototype.open = function (method, url, isAsync, userName, password) {
    this.method = method;
    this.url = url;
    this.isAsync = isAsync;
    this.userName = userName;
    this.password = password;
}

MockXmlHttpRequest.prototype.send = function (data) {
    this.sendCalled = true;
    this.data = data;
}

function createXmlHttpRequest() {
    return new MockXmlHttpRequest();
}