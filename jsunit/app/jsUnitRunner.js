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
var testManager=null;
var pageLoader=new PageLoader();
var uiUpdater=new UiUpdater();
var loadedPageInvestigator=new LoadedPageInvestigator();
var utility=new Utility();

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
        uiUpdater.initialize();
        setTimeout("testManager._nextPage();", TIMEOUT_LENGTH);
}
function doneLoadingPage(pageName) {
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
                this._nextPage();
        else {
                this._testExecutor.executeTestFunction(this._testsInPage[this._testIndex]);
                this.totalCount++;
                uiUpdater.updateProgressIndicators();
                this._testIndex++;
                setTimeout("testManager._runTest()", TIMEOUT_LENGTH);
        }
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
        uiUpdater.finalize();
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
        if ((new Date() - this._loadAttemptStartTime) / 1000 > MAX_SECONDS_WAITING_FOR_TEST_PAGE_TO_OPEN) {
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

/**************************************************************/

function UiUpdater() {
        this._windowForAllProblemMessages=null;
}
function _setTextOnLayer(layerName, str) {
	document.getElementById(layerName).innerHTML=str;
}
function setStatus(str) {
        this._setTextOnLayer("statusDiv", str);
}
function _setErrors(n) {
        this._setTextOnLayer("errorsDiv", n);
}
function _setFailures(n) {
        this._setTextOnLayer("failuresDiv", n);
}
function _setTotal(n) {
        this._setTextOnLayer("totalDiv", n);
}
function _setProgressBarImage(imgName) {
	progressBar().src=imgName;
}
function _setProgressBarWidth(w) {
        progressBar().width=w;
}
function updateProgressIndicators() {
        this._setTotal(testManager.totalCount);
        this._setErrors(testManager.errorCount);
        this._setFailures(testManager.failureCount);
        this._setProgressBarWidth(300*testManager.calculateProgressBarProportion());
        if (testManager.errorCount>0 || testManager.failureCount>0)
                this._setProgressBarImage("../images/red.gif");
        else
                this._setProgressBarImage("../images/green.gif");
}
function showMessageForSelectedProblemTest() {
	var problemTestIndex=problemsListField().selectedIndex;
        if (problemTestIndex!=-1)
		alert(problemsListField()[problemTestIndex].value);
}
function showMessagesForAllProblemTests() {
        if (problemsListField().length==0) return;
        try {
                this._windowForAllProblemMessages.close();
        } catch(e) {
        }
        this._windowForAllProblemMessages = window.open("","","width=600, height=350,status=no,resizable=yes,scrollbars=yes");
        var resDoc = this._windowForAllProblemMessages.document;
        resDoc.write("<html><head><link rel=\"stylesheet\" href=\"jsUnitStyle.css\"><title>Tests with problems - JsUnit</title><head><body>");
        resDoc.write("<p class=\"jsUnitSubHeading\">Tests with problems ("+problemsListField().length+" total)</p>");
        for (var i=0; i<problemsListField().length; i++) {
                resDoc.write("<p class=\"jsUnitDefault\">");
                resDoc.write("<b>" + (i+1) + ". ");
                resDoc.write(problemsListField()[i].text);
                resDoc.write("</b></p><p><pre>");
                resDoc.write(problemsListField()[i].value);
                resDoc.write("</pre></p>");
        }
        resDoc.write("</body></html>");
        resDoc.close();
}
function _clearProblemsList() {
	var listField=problemsListField();
	var initialLength=listField.options.length;
        for (var i=0; i<initialLength; i++)
                listField.remove(0);
}
function initialize() {
        this.setStatus("Initializing...");
        this._setRunButtonEnabled(false);
        this._clearProblemsList();
        this.updateProgressIndicators();
        this.setStatus("Done initializing");
}
function finalize() {
        this._setRunButtonEnabled(true);
}
function _setRunButtonEnabled(b) {
        runButton().disabled=!b;
}
UiUpdater.prototype._setTextOnLayer=_setTextOnLayer;
UiUpdater.prototype.setStatus=setStatus;
UiUpdater.prototype._setErrors=_setErrors;
UiUpdater.prototype._setFailures=_setFailures;
UiUpdater.prototype._setTotal=_setTotal;
UiUpdater.prototype._setProgressBarImage=_setProgressBarImage;
UiUpdater.prototype._setProgressBarWidth=_setProgressBarWidth;
UiUpdater.prototype.updateProgressIndicators=updateProgressIndicators;
UiUpdater.prototype.showMessageForSelectedProblemTest=showMessageForSelectedProblemTest;
UiUpdater.prototype._clearProblemsList=_clearProblemsList;
UiUpdater.prototype.initialize=initialize;
UiUpdater.prototype.finalize=finalize;
UiUpdater.prototype._setRunButtonEnabled=_setRunButtonEnabled;

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
	var testFunctionNames=new Array();
        if (isIE5Plus()) {
		var scriptsInTestFrame=containerTestFrame().document.scripts;
		for (var i=0; i<scriptsInTestFrame.length; i++) {
                        var someNames=this._extractTestFunctionNamesFromScript(scriptsInTestFrame[i]);
			if (someNames)
				testFunctionNames=testFunctionNames.concat(someNames);
                }
	} else {
                for (var i in containerTestFrame()) {
                        if (i.substring(0, 4)=="test" && typeof(containerTestFrame()[i])=="function")
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

/***********************************************/

function  kickOffTests() {
        if (isBlank(getTestFileName())) {
                alert("Please enter a file name.");
                return;
        }
        testManager=new TestManager(utility.resolveUserEnteredTestFileName());
        testManager.start();
}

/********************************************************/
function container() {
	return parent.frames[0];
}
function containerController() {
	return container().frames[0];
}
function containerTestFrame() {
	return container().frames[1];
}
function getTestFileName() {
        var rawEnteredFileName=document.testRunnerForm.testFileName.value;
        var result=rawEnteredFileName;
        while (result.indexOf("\\")!=-1)
                result=result.replace("\\", "/");
        return result;
}
function getTestFileProtocol() {
        return getDocumentProtocol();
}
function problemsListField() {
	return document.testRunnerForm.problemsList;
}
function progressBar() {
	return document.progress;
}
function runButton() {
        return document.testRunnerForm.runButton;
}
function getDocumentProtocol() {
        var protocol=document.location.protocol;
        if (protocol=="file:") return "file:///";
        if (protocol=="http:") return "http://";
        return null;
}
