/**************************************************************/

function UiUpdater()
{
        this._windowForAllProblemMessages=null;
}

function _setTextOnLayer(layerName, str)
{
  var html = '';
  html += '<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">';
  html += '<html><head><link rel="stylesheet" type="text/css" href="jsUnitStyle.css"><\/head>';
  html += '<body><div class="jsUnitDefault">';
  html += str;
  html += '<\/div><\/body>';
  html += '<\/html>';
  top.uiFrames[layerName].document.write(html);
  top.uiFrames[layerName].document.close();
}

function setStatus(str)
{
  this._setTextOnLayer("mainStatus", "<b>Status:</b> "+str);
}

function _setErrors(n)
{
  this._setTextOnLayer("mainCountsErrors", '<b>Errors: </b>' + n);
}

function _setFailures(n)
{
  this._setTextOnLayer("mainCountsFailures", '<b>Failures:</b> ' + n);
}

function _setTotal(n)
{
  this._setTextOnLayer("mainCountsRuns", '<b>Runs:</b> ' + n);
}

function _setProgressBarImage(imgName)
{
  progressBar().src=imgName;
}

function _setProgressBarWidth(w)
{
  progressBar().width=w;
}

function updateProgressIndicators()
{
  this._setTotal(testManager.totalCount);
  this._setErrors(testManager.errorCount);
  this._setFailures(testManager.failureCount);
  this._setProgressBarWidth(300*testManager.calculateProgressBarProportion());

  if (testManager.errorCount>0 || testManager.failureCount>0)
    this._setProgressBarImage("../images/red.gif");
  else
    this._setProgressBarImage("../images/green.gif");
}

function showMessageForSelectedProblemTest()
{
  var problemTestIndex=problemsListField().selectedIndex;
  if (problemTestIndex!=-1)
    alert(problemsListField()[problemTestIndex].value);
}

function showMessagesForAllProblemTests()
{
   if (problemsListField().length==0) return;

   try
   {
     this._windowForAllProblemMessages.close();
   }
   catch(e)
   {
   }

   this._windowForAllProblemMessages = window.open("","","width=600, height=350,status=no,resizable=yes,scrollbars=yes");
   var resDoc = this._windowForAllProblemMessages.document;
   resDoc.write("<html><head><link rel=\"stylesheet\" href=\"jsUnitStyle.css\"><title>Tests with problems - JsUnit</title><head><body>");
   resDoc.write("<p class=\"jsUnitSubHeading\">Tests with problems ("+problemsListField().length+" total) - JsUnit</p>");

   for (var i=0; i<problemsListField().length; i++)
   {
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

function _clearProblemsList()
{
  var listField=problemsListField();
  var initialLength=listField.options.length;

  for (var i=0; i<initialLength; i++)
    listField.remove(0);
}

function initializeUiUpdater()
{
  this.setStatus("Initializing...");
  this._setRunButtonEnabled(false);
  this._clearProblemsList();
  this.updateProgressIndicators();
  this.setStatus("Done initializing");
}

function finalizeUiUpdater()
{
  this._setRunButtonEnabled(true);
}

function _setRunButtonEnabled(b)
{
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
UiUpdater.prototype.initializeUiUpdater=initializeUiUpdater;
UiUpdater.prototype.finalizeUiUpdater=finalizeUiUpdater;
UiUpdater.prototype._setRunButtonEnabled=_setRunButtonEnabled;

/********************************************************/
function container() {
	return top.uiFrames.testContainer;
}
function containerController() {
	return top.uiFrames.testContainerController;
}
function containerTestFrame() {
	return top.uiFrames.testFrame;
}
function getTestFileName() {
        var rawEnteredFileName=document.testRunnerForm.testFileName.value;
        var result=rawEnteredFileName;
        while (result.indexOf("\\")!=-1)
                result=result.replace("\\", "/");
        return result;
}
function problemsListField() {
	return top.uiFrames.mainErrors.document.testRunnerForm.problemsList;
}
function progressBar() {
	return top.uiFrames.mainProgress.document.progress;
}
function runButton() {
        return top.uiFrames.mainData.document.testRunnerForm.runButton;
}

