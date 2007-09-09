function JsUnitTestManager() {
    this.log = [];
    
    this._windowForAllProblemMessages = null;

    this.container = top.frames.testContainer;
    this.documentLoader = top.frames.documentLoader;
    this.mainFrame = top.frames.mainFrame;

    this.containerController = this.container.frames.testContainerController;
    this.containerTestFrame = this.container.frames.testFrame;

    var mainData = this.mainFrame.frames.mainData;

    // form elements on mainData frame
    this.testFileName = mainData.document.testRunnerForm.testFileName;
    this.runButton = mainData.document.testRunnerForm.runButton;
    this.stopButton = mainData.document.testRunnerForm.stopButton;
    this.traceLevel = mainData.document.testRunnerForm.traceLevel;
    this.closeTraceWindowOnNewRun = mainData.document.testRunnerForm.closeTraceWindowOnNewRun;
    this.timeout = mainData.document.testRunnerForm.timeout;
    this.setUpPageTimeout = mainData.document.testRunnerForm.setUpPageTimeout;

    // image output
    this.progressBar = this.mainFrame.frames.mainProgress.document.progress;

    this.problemsListField = this.mainFrame.frames.mainErrors.document.testRunnerForm.problemsList;
    this.testCaseResultsField = this.mainFrame.frames.mainResults.document.resultsForm.testCaseResults;
    this.resultsTimeField = this.mainFrame.frames.mainResults.document.resultsForm.time;

    // 'layer' output frames
    this.uiFrames = new Object();
    this.uiFrames.mainStatus = this.mainFrame.frames.mainStatus;

    var mainCounts = this.mainFrame.frames.mainCounts;

    this.uiFrames.mainCountsErrors = mainCounts.frames.mainCountsErrors;
    this.uiFrames.mainCountsFailures = mainCounts.frames.mainCountsFailures;
    this.uiFrames.mainCountsRuns = mainCounts.frames.mainCountsRuns;
    this._baseURL = "";

    this.setup();
}

// seconds to wait for each test page to load
JsUnitTestManager.TESTPAGE_WAIT_SEC = 10;

// milliseoncds between test runs
JsUnitTestManager.TIMEOUT_LENGTH = 20;

// seconds to wait for setUpPage to complete
JsUnitTestManager.SETUPPAGE_TIMEOUT = 10;

// milliseconds to wait between polls on setUpPages
JsUnitTestManager.SETUPPAGE_INTERVAL = 100;

JsUnitTestManager.RESTORED_HTML_DIV_ID = "jsUnitRestoredHTML";

JsUnitTestManager.prototype.setup = function () {
    this.totalCount = 0;
    this.errorCount = 0;
    this.failureCount = 0;
    this._suiteStack = Array();

    var initialSuite = new top.jsUnitTestSuite();
    Utilities.push(this._suiteStack, initialSuite);
}

JsUnitTestManager.prototype.start = function () {
    this._baseURL = this.resolveUserEnteredTestFileName();
    var firstQuery = this._baseURL.indexOf("?");
    if (firstQuery >= 0) {
        this._baseURL = this._baseURL.substring(0, firstQuery);
    }
    var lastSlash = this._baseURL.lastIndexOf("/");
    var lastRevSlash = this._baseURL.lastIndexOf("\\");
    if (lastRevSlash > lastSlash) {
        lastSlash = lastRevSlash;
    }
    if (lastSlash > 0) {
        this._baseURL = this._baseURL.substring(0, lastSlash + 1);
    }

    this._timeRunStarted = new Date();
    this.initialize();
    setTimeout('top.testManager._nextPage();', JsUnitTestManager.TIMEOUT_LENGTH);
}

JsUnitTestManager.prototype.getBaseURL = function () {
    return this._baseURL;
}

JsUnitTestManager.prototype.doneLoadingPage = function (pageName) {
    this._testFileName = pageName;
    if (this.isTestPageSuite())
        this._handleNewSuite();
    else
    {
        this._testIndex = 0;
        this._testsInPage = this.getTestFunctionNames();
        this._numberOfTestsInPage = this._testsInPage.length;
        this._runTest();
    }
}

JsUnitTestManager.prototype._handleNewSuite = function () {
    var allegedSuite = this.containerTestFrame.suite();
    if (allegedSuite.isJsUnitTestSuite) {
 		var newSuite = this._cloneTestSuite(allegedSuite);
        if (newSuite.containsTestPages())
            push(this._suiteStack, newSuite);
        this._nextPage();
    }
    else {
        this.fatalError('Invalid test suite in file ' + this._testFileName);
        this.abort();
    }
}

/**
* This function handles cloning of a jsUnitTestSuite object.  This was added to replace the clone method of the jsUnitTestSuite class due to an IE bug in cross frame scripting. (See also jsunit bug 1522271)
**/
JsUnitTestManager.prototype._cloneTestSuite = function(suite) {
	var clone = new jsUnitTestSuite();
	clone._testPages = suite._testPages.concat(new Array(0));
	return clone;
}

JsUnitTestManager.prototype._runTest = function () {
    if (this._testIndex + 1 > this._numberOfTestsInPage)
    {
        // execute tearDownPage *synchronously*
        // (unlike setUpPage which is asynchronous)
        if (typeof this.containerTestFrame.tearDownPage == 'function') {
            this.containerTestFrame.tearDownPage();
        }

        this._nextPage();
        return;
    }

    if (this._testIndex == 0) {
        this.storeRestoredHTML();
        if (typeof(this.containerTestFrame.setUpPage) == 'function') {
            // first test for this page and a setUpPage is defined
            if (typeof(this.containerTestFrame.setUpPageStatus) == 'undefined') {
                // setUpPage() not called yet, so call it
                this.containerTestFrame.setUpPageStatus = false;
                this.containerTestFrame.startTime = new Date();
                this.containerTestFrame.setUpPage();
                // try test again later
                setTimeout('top.testManager._runTest()', JsUnitTestManager.SETUPPAGE_INTERVAL);
                return;
            }

            if (this.containerTestFrame.setUpPageStatus != 'complete') {
                top.status = 'setUpPage not completed... ' + this.containerTestFrame.setUpPageStatus + ' ' + (new Date());
                if ((new Date() - this.containerTestFrame.startTime) / 1000 > this.getsetUpPageTimeout()) {
                    this.fatalError('setUpPage timed out without completing.');
                    if (!this.userConfirm('Retry Test Run?')) {
                        this.abort();
                        return;
                    }
                    this.containerTestFrame.startTime = (new Date());
                }
                // try test again later
                setTimeout('top.testManager._runTest()', JsUnitTestManager.SETUPPAGE_INTERVAL);
                return;
            }
        }
    }

    top.status = '';
    // either not first test, or no setUpPage defined, or setUpPage completed
    this.executeTestFunction(this._testsInPage[this._testIndex]);
    this.totalCount++;
    this.updateProgressIndicators();
    this._testIndex++;
    setTimeout('if (top.testManager) top.testManager._runTest()', JsUnitTestManager.TIMEOUT_LENGTH);
}

JsUnitTestManager.prototype._done = function () {
    var secondsSinceRunBegan = (new Date() - this._timeRunStarted) / 1000;
    this.setStatus('Done (' + secondsSinceRunBegan + ' seconds)');
    this._cleanUp();
    if (top.shouldSubmitResults()) {
        this.resultsTimeField.value = secondsSinceRunBegan;
        top.submitResults();
    }
}

JsUnitTestManager.prototype._nextPage = function () {
    this._restoredHTML = null;
    if (this._currentSuite().hasMorePages()) {
        this.loadPage(this._currentSuite().nextPage());
    }
    else {
        Utilities.pop(this._suiteStack);
        if (this._currentSuite() == null)
            this._done();
        else
            this._nextPage();
    }
}

JsUnitTestManager.prototype._currentSuite = function () {
    var suite = null;

    if (this._suiteStack && this._suiteStack.length > 0)
        suite = this._suiteStack[this._suiteStack.length - 1];

    return suite;
}

JsUnitTestManager.prototype.calculateProgressBarProportion = function () {
    if (this.totalCount == 0)
        return 0;
    var currentDivisor = 1;
    var result = 0;

    for (var i = 0; i < this._suiteStack.length; i++) {
        var aSuite = this._suiteStack[i];
        currentDivisor *= aSuite._testPages.length;
        result += (aSuite._pageIndex - 1) / currentDivisor;
    }
    result += (this._testIndex + 1) / (this._numberOfTestsInPage * currentDivisor);
    return result;
}

JsUnitTestManager.prototype._cleanUp = function () {
    this.containerController.setTestPage('./app/emptyPage.html');
    this.finalize();
    top.tracer.finalize();
}

JsUnitTestManager.prototype.abort = function () {
    this.setStatus('Aborted');
    this._cleanUp();
}

JsUnitTestManager.prototype.getTimeout = function () {
    var result = JsUnitTestManager.TESTPAGE_WAIT_SEC;
    try {
        result = eval(this.timeout.value);
    }
    catch (e) {
    }
    return result;
}

JsUnitTestManager.prototype.getsetUpPageTimeout = function () {
    var result = JsUnitTestManager.SETUPPAGE_TIMEOUT;
    try {
        result = eval(this.setUpPageTimeout.value);
    }
    catch (e) {
    }
    return result;
}

JsUnitTestManager.prototype.isTestPageSuite = function () {
    var result = false;
    if (typeof(this.containerTestFrame.suite) == 'function')
    {
        result = true;
    }
    return result;
}

JsUnitTestManager.prototype.isTestFunction = function(propertyName, obj) {
    return propertyName.substring(0, 4) == 'test' && typeof(obj[propertyName]) == 'function';
}

JsUnitTestManager.prototype.getTestFunctionNames = function () {
    return this.getTestFunctionNamesFromExposedTestFunctionNames(this.containerTestFrame) ||
        this.getTestFunctionNamesFromFrameProperties(this.containerTestFrame) ||
        this.getTestFunctionNamesFromRuntimeObject(this.containerTestFrame) ||
        this.getTestFunctionNamesUsingPlainTextSearch(this.containerTestFrame);
}

JsUnitTestManager.prototype.getTestFunctionNamesFromExposedTestFunctionNames = function (testFrame) {
    if (testFrame && typeof(testFrame.exposeTestFunctionNames) == 'function') {
        return testFrame.exposeTestFunctionNames();
    } else {
        return null;
    }
}

JsUnitTestManager.prototype.getTestFunctionNamesFromFrameProperties = function (testFrame) {
    var testFunctionNames = [];

    for (var i in testFrame) {
        if (this.isTestFunction(i, testFrame)) {
            Utilities.push(testFunctionNames, i);
        }
    }

    return testFunctionNames.length > 0 ? testFunctionNames : null;
}

JsUnitTestManager.prototype.getTestFunctionNamesFromRuntimeObject = function (testFrame) {
    var testFunctionNames = [];

    if (testFrame.RuntimeObject) {
        var runtimeObject = testFrame.RuntimeObject("test*");
        for (var i in runtimeObject) {
            if (this.isTestFunction(i, runtimeObject)) {
                Utilities.push(testFunctionNames, i);
            }
        }
    }

    return testFunctionNames.length > 0 ? testFunctionNames : null;
}

/**
 * Method of last resort. This will pick up functions that are commented-out and will not be able to pick up
 * tests in included JS files.
 */
JsUnitTestManager.prototype.getTestFunctionNamesUsingPlainTextSearch = function (testFrame) {
    var testFunctionNames = [];

    if (testFrame &&
        testFrame.document &&
        typeof(testFrame.document.scripts) != 'undefined' &&
        testFrame.document.scripts.length > 0) { // IE5 and up
        var scriptsInTestFrame = testFrame.document.scripts;

        for (i = 0; i < scriptsInTestFrame.length; i++) {
            var someNames = this._extractTestFunctionNamesFromScript(scriptsInTestFrame[i]);
            if (someNames) {
                testFunctionNames = testFunctionNames.concat(someNames);
            }
        }
    }

    return testFunctionNames.length > 0 ? testFunctionNames : null;
}

JsUnitTestManager.prototype._extractTestFunctionNamesFromScript = function (aScript) {
    var result;
    var remainingScriptToInspect = aScript.text;
    var currentIndex = this._indexOfTestFunctionIn(remainingScriptToInspect);
    while (currentIndex != -1) {
        if (!result)
            result = new Array();

        var fragment = remainingScriptToInspect.substring(currentIndex, remainingScriptToInspect.length);
        result = result.concat(fragment.substring('function '.length, fragment.indexOf('(')));
        remainingScriptToInspect = remainingScriptToInspect.substring(currentIndex + 12, remainingScriptToInspect.length);
        currentIndex = this._indexOfTestFunctionIn(remainingScriptToInspect);
    }
    return result;
}

JsUnitTestManager.prototype._indexOfTestFunctionIn = function (string) {
    return string.indexOf('function test');
}

JsUnitTestManager.prototype.loadPage = function (testFileName) {
    this._testFileName = testFileName;
    this._loadAttemptStartTime = new Date();
    this.setStatus('Opening Test Page "' + this._testFileName + '"');
    this.containerController.setTestPage(this._testFileName);
    this._callBackWhenPageIsLoaded();
}

JsUnitTestManager.prototype._callBackWhenPageIsLoaded = function () {
    if ((new Date() - this._loadAttemptStartTime) / 1000 > this.getTimeout()) {
        this.fatalError('Reading Test Page ' + this._testFileName + ' timed out.\nMake sure that the file exists and is a Test Page.');
        if (this.userConfirm('Retry Test Run?')) {
            this.loadPage(this._testFileName);
            return;
        } else {
            this.abort();
            return;
        }
    }
    if (!this._isTestFrameLoaded()) {
        setTimeout('if (top.testManager) top.testManager._callBackWhenPageIsLoaded();', JsUnitTestManager.TIMEOUT_LENGTH);
        return;
    }
    this.doneLoadingPage(this._testFileName);
}

JsUnitTestManager.prototype._isTestFrameLoaded = function () {
    try {
        return this.containerController.isPageLoaded();
    }
    catch (e) {
    }
    return false;
}

JsUnitTestManager.prototype.executeTestFunction = function (functionName) {
    this._testFunctionName = functionName;
    this.setStatus('Running test "' + this._testFunctionName + '"');
    var exception = null;
    var timeBefore = new Date();
    try {
        if (this._restoredHTML)
            top.testContainer.testFrame.document.getElementById(JsUnitTestManager.RESTORED_HTML_DIV_ID).innerHTML = this._restoredHTML;
        if (this.containerTestFrame.setUp !== JSUNIT_UNDEFINED_VALUE)
            this.containerTestFrame.setUp();
        this.containerTestFrame[this._testFunctionName]();
    }
    catch (e1) {
        exception = e1;
    }
    finally {
        try {
            if (this.containerTestFrame.tearDown !== JSUNIT_UNDEFINED_VALUE)
                this.containerTestFrame.tearDown();
        }
        catch (e2) {
            //Unlike JUnit, only assign a tearDown exception to excep if there is not already an exception from the test body
            if (exception == null)
                exception = e2;
        }
    }
    var timeTaken = (new Date() - timeBefore) / 1000;
    if (exception != null)
        this._handleTestException(exception);
    var serializedTestCaseString = this._currentTestFunctionNameWithTestPageName(true) + "|" + timeTaken + "|";
    if (exception == null)
        serializedTestCaseString += "S||";
    else {
        if (exception.isJsUnitFailure)
            serializedTestCaseString += "F|";
        else {
            serializedTestCaseString += "E|";
        }
        serializedTestCaseString += this._problemDetailMessageFor(exception);
    }
    this._addOption(this.testCaseResultsField,
            serializedTestCaseString,
            serializedTestCaseString);
}

JsUnitTestManager.prototype._currentTestFunctionNameWithTestPageName = function(useFullyQualifiedTestPageName) {
    var testURL = this.containerTestFrame.location.href;
    var testQuery = testURL.indexOf("?");
    if (testQuery >= 0) {
        testURL = testURL.substring(0, testQuery);
    }
    if (!useFullyQualifiedTestPageName) {
        if (testURL.substring(0, this._baseURL.length) == this._baseURL)
            testURL = testURL.substring(this._baseURL.length);
    }
    return testURL + ':' + this._testFunctionName;
}

JsUnitTestManager.prototype._addOption = function(listField, problemValue, problemMessage) {
    if (typeof(listField.ownerDocument) != 'undefined'
            && typeof(listField.ownerDocument.createElement) != 'undefined') {
        // DOM Level 2 HTML method.
        // this is required for Opera 7 since appending to the end of the
        // options array does not work, and adding an Option created by new Option()
        // and appended by listField.options.add() fails due to WRONG_DOCUMENT_ERR
        var problemDocument = listField.ownerDocument;
        var errOption = problemDocument.createElement('option');
        errOption.setAttribute('value', problemValue);
        errOption.appendChild(problemDocument.createTextNode(problemMessage));
        listField.appendChild(errOption);
    }
    else {
        // new Option() is DOM 0

        var errOption = new Option(problemMessage, problemValue);

        if (typeof(listField.add) != 'undefined') {
            // DOM 2 HTML
            try {
                listField.add(errOption, null);
            } catch(err) {
                listField.add(errOption); // IE 5.5
            }

        }
        else if (typeof(listField.options.add) != 'undefined') {
            // DOM 0
            listField.options.add(errOption, null);
        }
        else {
            // DOM 0
            listField.options[listField.length] = errOption;
        }
    }
}

JsUnitTestManager.prototype._handleTestException = function (excep) {
    var problemMessage = this._currentTestFunctionNameWithTestPageName(false) + ' ';
    var errOption;
    if (!excep.isJsUnitFailure) {
        problemMessage += 'had an error';
        this.errorCount++;
    }
    else {
        problemMessage += 'failed';
        this.failureCount++;
    }
    var listField = this.problemsListField;
    this._addOption(listField,
            this._problemDetailMessageFor(excep),
            problemMessage);
}

JsUnitTestManager.prototype._problemDetailMessageFor = function (excep) {
    var result = null;
    if (excep.isJsUnitFailure) {
        result = '';
        if (excep.comment != null)
            result += ('"' + excep.comment + '"\n');

        result += excep.jsUnitMessage;

        if (excep.stackTrace)
            result += '\n\nStack trace follows:\n' + excep.stackTrace;
    }
    else {
        result = 'Error message is:\n"';
        result +=
        (typeof(excep.description) == 'undefined') ?
        excep :
        excep.description;
        result += '"';
        if (typeof(excep.stack) != 'undefined') // Mozilla only
            result += '\n\nStack trace follows:\n' + excep.stack;
    }
    return result;
}

JsUnitTestManager.prototype._setTextOnLayer = function (layerName, str) {
    try {
        var content;
        if (content = this.uiFrames[layerName].document.getElementById('content'))
            content.innerHTML = str;
        else
            throw new Error("No content div found.");
    }
    catch (e) {
        var html = '';
        html += '<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">';
        html += '<html><head><link rel="stylesheet" type="text/css" href="css/jsUnitStyle.css"><\/head>';
        html += '<body><div id="content">';
        html += str;
        html += '<\/div><\/body>';
        html += '<\/html>';
        this.uiFrames[layerName].document.write(html);
        this.uiFrames[layerName].document.close();
    }
}

JsUnitTestManager.prototype.setStatus = function (str) {
    this._setTextOnLayer('mainStatus', '<b>Status:<\/b> ' + str);
    this.log.push(str);
}

JsUnitTestManager.prototype._setErrors = function (n) {
    this._setTextOnLayer('mainCountsErrors', '<b>Errors: <\/b>' + n);
}

JsUnitTestManager.prototype._setFailures = function (n) {
    this._setTextOnLayer('mainCountsFailures', '<b>Failures:<\/b> ' + n);
}

JsUnitTestManager.prototype._setTotal = function (n) {
    this._setTextOnLayer('mainCountsRuns', '<b>Runs:<\/b> ' + n);
}

JsUnitTestManager.prototype._setProgressBarImage = function (imgName) {
    this.progressBar.src = imgName;
}

JsUnitTestManager.prototype._setProgressBarWidth = function (w) {
    this.progressBar.width = w;
}

JsUnitTestManager.prototype.updateProgressIndicators = function () {
    this._setTotal(this.totalCount);
    this._setErrors(this.errorCount);
    this._setFailures(this.failureCount);
    this._setProgressBarWidth(300 * this.calculateProgressBarProportion());

    if (this.errorCount > 0 || this.failureCount > 0)
        this._setProgressBarImage('../images/red.gif');
    else
        this._setProgressBarImage('../images/green.gif');
}

JsUnitTestManager.prototype.showMessageForSelectedProblemTest = function () {
    var problemTestIndex = this.problemsListField.selectedIndex;
    if (problemTestIndex != -1)
        this.fatalError(this.problemsListField[problemTestIndex].value);
}

JsUnitTestManager.prototype.showMessagesForAllProblemTests = function () {
    if (this.problemsListField.length == 0)
        return;

    this.tryToCloseWindow(this._windowForAllProblemMessages);

    var body = '<p>Tests with problems (' + this.problemsListField.length + ' total) - JsUnit<\/p>'
        + '<p>Running on ' + navigator.userAgent + '</p>';

    for (var i = 0; i < this.problemsListField.length; i++) {
        body += '<p class="jsUnitDefault">';
        body += '<b>' + (i + 1) + '. ';
        body += this.problemsListField[i].text;
        body += '<\/b><\/p><p><pre>';
        body += this._makeHTMLSafe(this.problemsListField[i].value);
        body += '<\/pre><\/p>';
    }

    this._windowForAllProblemMessages = this.createWindow("Tests with problems", body);
}

JsUnitTestManager.prototype.showLog = function() {
    this.tryToCloseWindow(this.logWindow);

    var body = "<pre>";

    for (var i = 0; i < this.log.length; i++) {
        body += this.log[i];
        body += "\n";
    }

    body += "</pre>";

    this.logwindow = this.createWindow("Log", body);
}

JsUnitTestManager.prototype.tryToCloseWindow = function(w) {
    try {
        if (w && !w.closed) w.close();
    } catch(e) {
    }
}

JsUnitTestManager.prototype.createWindow = function(title, body) {
    var w = window.open('', '', 'width=600, height=350,status=no,resizable=yes,scrollbars=yes');
    var resDoc = w.document;
    resDoc.write('<html><head><link rel="stylesheet" href="../css/jsUnitStyle.css"><title>');
    resDoc.write(title);
    resDoc.write(' - JsUnit<\/title><head><body>');
    resDoc.write(body);
    resDoc.write('<\/body><\/html>');
    resDoc.close();
}

JsUnitTestManager.prototype._makeHTMLSafe = function (string) {
    string = string.replace(/&/g, '&amp;');
    string = string.replace(/</g, '&lt;');
    string = string.replace(/>/g, '&gt;');
    return string;
}

JsUnitTestManager.prototype._clearProblemsList = function () {
    var listField = this.problemsListField;
    var initialLength = listField.options.length;

    for (var i = 0; i < initialLength; i++)
        listField.remove(0);
}

JsUnitTestManager.prototype.initialize = function () {
    this.setStatus('Initializing...');
    this._setRunButtonEnabled(false);
    this._clearProblemsList();
    this.updateProgressIndicators();
    this.setStatus('Done initializing');
}

JsUnitTestManager.prototype.finalize = function () {
    this._setRunButtonEnabled(true);
}

JsUnitTestManager.prototype._setRunButtonEnabled = function (b) {
    this.runButton.disabled = !b;
    this.stopButton.disabled = b;
}

JsUnitTestManager.prototype.getTestFileName = function () {
    var rawEnteredFileName = this.testFileName.value;
    var result = rawEnteredFileName;

    while (result.indexOf('\\') != -1)
        result = result.replace('\\', '/');

    return result;
}

JsUnitTestManager.prototype.getTestFunctionName = function () {
    return this._testFunctionName;
}

JsUnitTestManager.prototype.resolveUserEnteredTestFileName = function (rawText) {
    var userEnteredTestFileName = top.testManager.getTestFileName();

    // only test for file:// since Opera uses a different format
    if (userEnteredTestFileName.indexOf('http://') == 0 || userEnteredTestFileName.indexOf('https://') == 0 || userEnteredTestFileName.indexOf('file://') == 0)
        return userEnteredTestFileName;

    return getTestFileProtocol() + this.getTestFileName();
}

JsUnitTestManager.prototype.storeRestoredHTML = function () {
    if (document.getElementById && top.testContainer.testFrame.document.getElementById(JsUnitTestManager.RESTORED_HTML_DIV_ID))
        this._restoredHTML = top.testContainer.testFrame.document.getElementById(JsUnitTestManager.RESTORED_HTML_DIV_ID).innerHTML;
}

JsUnitTestManager.prototype.fatalError = function(aMessage) {
    if (top.shouldSuppressDialogs())
        this.setStatus(aMessage);
    else
        alert(aMessage);
}

JsUnitTestManager.prototype.userConfirm = function(aMessage) {
    if (top.shouldSuppressDialogs())
        return false;
    else
        return confirm(aMessage);
}

function getTestFileProtocol() {
    return getDocumentProtocol();
}

function getDocumentProtocol() {
    var protocol = top.document.location.protocol;

    if (protocol == "file:")
        return "file:///";

    if (protocol == "http:")
        return "http://";

    if (protocol == 'https:')
        return 'https://';

    if (protocol == "chrome:")
        return "chrome://";

    return null;
}

function _browserSupportsReadingFullPathFromFileField() {
    return !isOpera() && !isIE7();
}

function isOpera() {
    return navigator.userAgent.toLowerCase().indexOf("opera") != -1;
}

function isIE7() {
    return navigator.userAgent.toLowerCase().indexOf("msie 7") != -1;
}

function isBeingRunOverHTTP() {
    return getDocumentProtocol() == "http://";
}

function getWebserver() {
    if (isBeingRunOverHTTP()) {
        var myUrl = location.href;
        var myUrlWithProtocolStripped = myUrl.substring(myUrl.indexOf("/") + 2);
        return myUrlWithProtocolStripped.substring(0, myUrlWithProtocolStripped.indexOf("/"));
    }
    return null;
}