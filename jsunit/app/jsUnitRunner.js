/*
  JsUnit
  Copyright (C) 2001 Edward Hieatt, edward@jsunit.net

  This program is free software; you can redistribute it and/or
  modify it under the terms of the GNU General Public License
  as published by the Free Software Foundation; either version 2
  of the License, or (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
*/

var MAX_SECONDS_WAITING_FOR_TEST_PAGE_TO_OPEN=5;
var TIMEOUT_LENGTH=1;
var PRETEST_INTERVAL = 100;
var testManager=null;
var pageLoader=new PageLoader();
var uiUpdater=new UiUpdater();
var loadedPageInvestigator=new LoadedPageInvestigator();
var utility=new Utility();
var tracer=new Tracer();

/*************************************************************/
function TestManager(initialTestFile) {
        this._testExecutor=new TestExecutor();
        this.totalCount=0;
        this.errorCount=0;
        this.failureCount=0;
        var initialSuite=new JsUnitTestSuite();
        initialSuite.addTestPage(initialTestFile);
        this._suiteStack=Array();
        push(this._suiteStack, initialSuite);
}
function start() {
        this._timeRunStarted=new Date();
        uiUpdater.initializeUiUpdater();
        tracer.initializeTracer();
        setTimeout("testManager._nextPage();", TIMEOUT_LENGTH);
}
function doneLoadingPage(pageName) {
        containerTestFrame().setTracer(tracer);
        this._testFileName=pageName;
        if (!loadedPageInvestigator.isLoadedTestPageATestSuitePage()) {
                this._testIndex=0;
                this._testsInPage=loadedPageInvestigator.getTestFunctionNamesInLoadedTestPage();
                this._numberOfTestsInPage=this._testsInPage.length;
                this._runTest();
        }
        else
                this._handleNewSuite();
}
function _handleNewSuite() {
        var allegedSuite=containerTestFrame().suite();
        if (allegedSuite.isJsUnitTestSuite) {
                var newSuite=utility.copySuite(allegedSuite);
                if (newSuite.containsTestPages())
                               push(this._suiteStack, newSuite);
                this._nextPage();
        }
        else {
                alert("Invalid test suite in file "+this._testFileName);
                this.abort();
        }
}
function _runTest() {
  if (this._testIndex+1>this._numberOfTestsInPage)
  {
    this._nextPage();
    return;
  }

  if (this._testIndex == 0 && typeof(top.uiFrames.testFrame.preTest) == 'function')
  {
    // first test for this page and a preTest is defined
    if (typeof(top.uiFrames.testFrame.preTestStatus) == 'undefined')
    {
      // preTest() not called yet, so call it
      top.uiFrames.testFrame.preTestStatus = false;
      top.uiFrames.testFrame.startTime = new Date();
      top.uiFrames.testFrame.preTest();
      // try test again later
      setTimeout("testManager._runTest()", PRETEST_INTERVAL);
      return;
    }

    if (top.uiFrames.testFrame.preTestStatus != 'complete')
    {
      // preTest called, but not complete yet
      top.status = 'preTest not completed... ' + top.uiFrames.testFrame.preTestStatus + ' ' + (new Date());
      if ((new Date() - top.uiFrames.testFrame.startTime) > PRETEST_TIMEOUT) {
        if (prompt('Test Pre-Condition timed out without completing., Retry or Cancel', 'Retry') != 'Retry')
        {
          testManager.abort();
          return;
        }
        top.uiFrames.testFrame.startTime = (new Date());
      }
      // try test again later
      setTimeout("testManager._runTest()", PRETEST_INTERVAL);
      return;
    }
  }

  top.status = '';
  // either not first test, or no preTest defined, or preTest completed
  this._testExecutor.executeTestFunction(this._testsInPage[this._testIndex]);
  this.totalCount++;
  uiUpdater.updateProgressIndicators();
  this._testIndex++;
  setTimeout("testManager._runTest()", TIMEOUT_LENGTH);
}
function _nextPage() {
        if (this._currentSuite().hasMorePages()) {
                pageLoader.loadPage(this._currentSuite().nextPage());
        }
        else {
                pop(this._suiteStack);
                if (this._currentSuite()==null)
                        this._done();
                else
                        this._nextPage();
        }

}
function calculateProgressBarProportion() {
        if (this.totalCount==0) return 0;
        var currentDivisor=1;
        var result=0;
        for (var i=0; i<this._suiteStack.length; i++) {
                var aSuite=this._suiteStack[i];
                currentDivisor*=aSuite.testPages.length;
                result+=(aSuite.pageIndex-1)/currentDivisor;
        }
        result+=(this._testIndex+1)/(this._numberOfTestsInPage*currentDivisor);
        return result;
}
function _done() {
        var secondsSinceRunBegan=(new Date()-this._timeRunStarted)/1000;
        uiUpdater.setStatus("Done (" + secondsSinceRunBegan + " seconds)");
        this._cleanUp();
}
function _currentSuite() {
        return this._suiteStack ? this._suiteStack[this._suiteStack.length-1] : null;
}
function abort() {
        uiUpdater.setStatus("Aborted");
        this._cleanUp();
}
function _cleanUp() {
        containerController().setTestPage("./emptyPage.html");
        uiUpdater.finalizeUiUpdater();
        tracer.finalizeTracer();
}
function timeout() {
        var result=MAX_SECONDS_WAITING_FOR_TEST_PAGE_TO_OPEN;
        try {
                result = eval(document.testRunnerForm.timeout.value);
        } catch (e) {
        }
        return result;
}
TestManager.prototype.start=start;
TestManager.prototype.doneLoadingPage=doneLoadingPage;
TestManager.prototype._handleNewSuite=_handleNewSuite;
TestManager.prototype._runTest=_runTest;
TestManager.prototype._done=_done;
TestManager.prototype._nextPage=_nextPage;
TestManager.prototype._currentSuite=_currentSuite;
TestManager.prototype.calculateProgressBarProportion=calculateProgressBarProportion;
TestManager.prototype._cleanUp=_cleanUp;
TestManager.prototype.abort=abort;
TestManager.prototype.timeout=timeout;

/**************************************************************/

function PageLoader() {
}
function loadPage(testFileName) {
        this._testFileName=testFileName;
        this._loadAttemptStartTime=new Date();
        setStatus("Opening Test Page \""+this._testFileName+"\"");
        containerController().setTestPage(this._testFileName);
        this._callBackWhenPageIsLoaded();
}
function _callBackWhenPageIsLoaded() {
        if ((new Date() - this._loadAttemptStartTime) / 1000 > testManager.timeout()) {
                alert("Reading Test Page "+this._testFileName+" timed out.\nMake sure that the file exists and is a Test Page.");
                testManager.abort();
                return;
        }
        if (!this._isTestFrameLoaded()) {
                setTimeout("pageLoader._callBackWhenPageIsLoaded();", TIMEOUT_LENGTH);
                return;
        }
        testManager.doneLoadingPage(this._testFileName);
}
function _isTestFrameLoaded() {
	try {
		return containerController().isPageLoaded();
	} catch (e) {
	}
	return false;
}
PageLoader.prototype.loadPage=loadPage;
PageLoader.prototype._callBackWhenPageIsLoaded=_callBackWhenPageIsLoaded;
PageLoader.prototype._isTestFrameLoaded=_isTestFrameLoaded;

/**************************************************************/
function TestExecutor() {
}
function executeTestFunction(functionName) {
        this._testFunctionName=functionName;
        setStatus("Running test \""+this._testFunctionName+"\"");
        var excep=null;
	try {
                containerTestFrame().setUp();
                eval("containerTestFrame()." + this._testFunctionName + "();");
        } catch (e1) {
                excep=e1;
	}
        finally {
                try {
                        containerTestFrame().tearDown();
                } catch (e2) {
                        excep=e2;
                }
        }
        if (excep!=null)
                this._handleTestException(excep);
}
function _handleTestException(excep) {
        var problemMessage=containerTestFrame().location.href+":"+this._testFunctionName + " ";
        if (!excep.isJsUnitException) {
                problemMessage += "had an error";
                testManager.errorCount++;
        } else {
                problemMessage += "failed";
                testManager.failureCount++;
        }
        var listField=problemsListField();
        listField.options[listField.length]=new Option(problemMessage, this._problemDetailMessageFor(excep));
}
function _problemDetailMessageFor(excep) {
        var result=null;
        if (excep.isJsUnitException) {
                result="";
                if (excep.comment!=null)
                        result+=("\""+excep.comment+"\"\n");
                result+=excep.jsUnitMessage;
                if (excep.stackTrace)
                        result+="\n\nStack trace follows:\n"+excep.stackTrace;
        }
        else {
                result="Error message is:\n\"";
                result +=
                        (excep.description==JSUNIT_UNDEFINED_VALUE) ?
                                excep :
                                excep.description;
                result += "\"";
        }
        return result;
}
TestExecutor.prototype.executeTestFunction=executeTestFunction;
TestExecutor.prototype._handleTestException=_handleTestException;
TestExecutor.prototype._problemDetailMessageFor=_problemDetailMessageFor;

/*******************************************/

function LoadedPageInvestigator() {
}
function isLoadedTestPageATestSuitePage() {
        var result=true;
        try {
                containerTestFrame().suite();
        }
        catch (e) {
                result=false;
        }
        return result;
}
function getTestFunctionNamesInLoadedTestPage() {
  var testFrame = containerTestFrame();
	var testFunctionNames=new Array();
  if (testFrame && testFrame.getTestFunctionNames)
        return testFrame.getTestFunctionNames();
  if (testFrame && testFrame.document && testFrame.document.scripts) { // IE5 and up
		var scriptsInTestFrame=testFrame.document.scripts;
		for (var i=0; i<scriptsInTestFrame.length; i++) {
      var someNames=this._extractTestFunctionNamesFromScript(scriptsInTestFrame[i]);
			if (someNames)
				testFunctionNames=testFunctionNames.concat(someNames);
    }
	} else {
    for (var i in testFrame) {
      if (i.substring(0, 4)=="test" && typeof(testFrame[i])=="function")
        push(testFunctionNames, i);
    }
  }
	return testFunctionNames;
}
function _extractTestFunctionNamesFromScript(aScript) {
	var result;
	var remainingScriptToInspect=aScript.text;
        var currentIndex=remainingScriptToInspect.indexOf("function test");
	while (currentIndex!=-1) {
		if (!result) result=new Array();
		var fragment=remainingScriptToInspect.substring(currentIndex, remainingScriptToInspect.length);
		result=result.concat(fragment.substring("function ".length, fragment.indexOf("(")));
                remainingScriptToInspect=remainingScriptToInspect.substring(currentIndex+12, remainingScriptToInspect.length);
                currentIndex=remainingScriptToInspect.indexOf("function test");
	}
	return result;
}
LoadedPageInvestigator.prototype.isLoadedTestPageATestSuitePage=isLoadedTestPageATestSuitePage;
LoadedPageInvestigator.prototype.getTestFunctionNamesInLoadedTestPage=getTestFunctionNamesInLoadedTestPage;
LoadedPageInvestigator.prototype._extractTestFunctionNamesFromScript=_extractTestFunctionNamesFromScript;

/********************************************/

function Utility() {
}
function copySuite(suite) {
        var result=new JsUnitTestSuite();
        result.testPages=suite.testPages;
        return result;
}
function resolveUserEnteredTestFileName(rawText) {
        var userEnteredTestFileName=getTestFileName();
        if (userEnteredTestFileName.indexOf("http://")==0 || userEnteredTestFileName.indexOf("file:///")==0)
                return userEnteredTestFileName;
        return getTestFileProtocol()+getTestFileName();
}
Utility.prototype.copySuite=copySuite;
Utility.prototype.resolveUserEnteredTestFileName=resolveUserEnteredTestFileName;

/********************************************/

function Tracer() {
        this._traceWindow=null;
        this.TRACE_LEVEL_WARNING=1;
        this.TRACE_LEVEL_INFO=2;
        this.TRACE_LEVEL_DEBUG=3;
}
function _trace(message, value, traceLevel) {
        if (_getChosenTraceLevel() >= traceLevel) {
                var traceString=message;
                if (value)
                        traceString+=": "+value;
                this._writeToTraceWindow(traceString, traceLevel);
        }
}
function _getChosenTraceLevel() {
        return eval(document.testRunnerForm.traceLevel.value);
}
function _writeToTraceWindow(traceString, traceLevel) {
        var htmlToAppend = "<p class=\"jsUnitDefault\">" + traceString + "</p>\n";
        this._getTraceWindow().document.write(htmlToAppend);
}
function _getTraceWindow() {
        if (this._traceWindow==null) {
                this._traceWindow = window.open("","","width=600, height=350,status=no,resizable=yes,scrollbars=yes");
                var resDoc = this._traceWindow.document;
                resDoc.write("<html>\n<head>\n<link rel=\"stylesheet\" href=\"jsUnitStyle.css\">\n<title>Tracing - JsUnit</title>\n<head>\n<body>");
                resDoc.write("<p class=\"jsUnitSubHeading\">Tracing - JsUnit</p>\n");
        }
        return this._traceWindow;
}
function initializeTracer() {
        if (this._traceWindow!=null && document.testRunnerForm.closeTraceWindowOnNewRun.checked)
                this._traceWindow.close();
        this._traceWindow=null;
}
function finalizeTracer() {
        if (this._traceWindow!=null) {
                this._traceWindow.document.write("</body>\n</html>");
                this._traceWindow.document.close();
        }
}
function warn() {
        this._trace(arguments[0], arguments[1], this.TRACE_LEVEL_WARNING);
}
function inform() {
        this._trace(arguments[0], arguments[1], this.TRACE_LEVEL_INFO);
}
function debug() {
        this._trace(arguments[0], arguments[1], this.TRACE_LEVEL_DEBUG);
}
Tracer.prototype.initializeTracer=initializeTracer;
Tracer.prototype.finalizeTracer=finalizeTracer;
Tracer.prototype.warn=warn;
Tracer.prototype.inform=inform;
Tracer.prototype.debug=debug;
Tracer.prototype._trace=_trace;
Tracer.prototype._getChosenTraceLevel=_getChosenTraceLevel;
Tracer.prototype._writeToTraceWindow=_writeToTraceWindow;
Tracer.prototype._getTraceWindow=_getTraceWindow;


/***********************************************/

function  kickOffTests() {
        if (isBlank(getTestFileName())) {
                alert("Please enter a file name.");
                return;
        }
        testManager=new TestManager(utility.resolveUserEnteredTestFileName());
        testManager.start();
}


