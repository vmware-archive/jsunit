/*
  JsUnit
  Copyright (C) 2002 Edward Hieatt, edward@jsunit.net

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

var JSUNIT_UNDEFINED_VALUE;
var isTestPageLoaded=false;
var tracer=null;
function _displayStringForValue(aVar) {
	if (aVar===null) return "null";
	if (aVar===JSUNIT_UNDEFINED_VALUE) return "undefined";
	return aVar;
}
function fail(failureMessage) {
        throw new JsUnitException(null, failureMessage);
}
function error(errorMessage) {
        var errorObject=new Object();
        errorObject.description=errorMessage;
        throw errorObject;
}
function argumentsIncludeComments(expectedNumberOfNonCommentArgs, args) {
        return args.length==expectedNumberOfNonCommentArgs+1;
}
function commentArg(expectedNumberOfNonCommentArgs, args) {
        if (argumentsIncludeComments(expectedNumberOfNonCommentArgs, args))
                return args[0];
        else
                return null;
}
function nonCommentArg(desiredNonCommentArgIndex, expectedNumberOfNonCommentArgs, args) {
        return argumentsIncludeComments(expectedNumberOfNonCommentArgs, args) ?
               args[desiredNonCommentArgIndex] :
               args[desiredNonCommentArgIndex-1];
}
function _validateArguments(expectedNumberOfNonCommentArgs, args) {
        if (!(  args.length==expectedNumberOfNonCommentArgs ||
                (args.length==expectedNumberOfNonCommentArgs+1 && typeof(args[0])=="string") ))
                error("Incorrect arguments passed to assert function");
}
function _assert(comment, booleanValue, failureMessage) {
        if (!booleanValue)
                throw new JsUnitException(comment, failureMessage);
}
function assert() {
        _validateArguments(1, arguments);
        var booleanValue=nonCommentArg(1, 1, arguments);
        if (typeof(booleanValue)!="boolean")
                error("Bad argument to assert(boolean)");
        _assert(commentArg(1, arguments), booleanValue===true, "Call to assert(boolean) with false");
}
function assertTrue() {
        _validateArguments(1, arguments);
        var booleanValue=nonCommentArg(1, 1, arguments);
        if (typeof(booleanValue)!="boolean")
                error("Bad argument to assertTrue(boolean)");
        _assert(commentArg(1, arguments), booleanValue===true, "Call to assertTrue(boolean) with false");
}
function assertFalse() {
        _validateArguments(1, arguments);
        var booleanValue=nonCommentArg(1, 1, arguments);
        if (typeof(booleanValue)!="boolean")
                error("Bad argument to assertFalse(boolean)");
        _assert(commentArg(1, arguments), booleanValue===false, "Call to assertFalse(boolean) with true");
}
function assertEquals() {
        _validateArguments(2, arguments);
        var var1=nonCommentArg(1, 2, arguments);
        var var2=nonCommentArg(2, 2, arguments);
        _assert(commentArg(2, arguments), var1===var2, "Expected " + var1 + " (" + typeof(var1) + ") but was "+_displayStringForValue(var2) +" ("+typeof(var2)+")");
}
function assertNotEquals() {
        _validateArguments(2, arguments);
        var var1=nonCommentArg(1, 2, arguments);
        var var2=nonCommentArg(2, 2, arguments);
        _assert(commentArg(2, arguments), var1!==var2, "Expected not to be "+_displayStringForValue(var2));
}
function assertNull() {
        _validateArguments(1, arguments);
        var aVar=nonCommentArg(1, 1, arguments);
        _assert(commentArg(1, arguments), aVar===null, "Expected null but was "+_displayStringForValue(aVar));
}
function assertNotNull() {
        _validateArguments(1, arguments);
        var aVar=nonCommentArg(1, 1, arguments);
        _assert(commentArg(1, arguments), aVar!==null, "Expected not to be null");
}
function assertUndefined(aVar) {
        _validateArguments(1, arguments);
        var aVar=nonCommentArg(1, 1, arguments);
        _assert(commentArg(1, arguments), aVar===JSUNIT_UNDEFINED_VALUE, "Expected undefined but was "+_displayStringForValue(aVar));
}
function assertNotUndefined(aVar) {
        _validateArguments(1, arguments);
        var aVar=nonCommentArg(1, 1, arguments);
        _assert(commentArg(1, arguments), aVar!==JSUNIT_UNDEFINED_VALUE, "Expected not to be undefined");
}
function assertNaN(aVar) {
        _validateArguments(1, arguments);
        var aVar=nonCommentArg(1, 1, arguments);
        _assert(commentArg(1, arguments), isNaN(aVar), "Expected NaN");
}
function assertNotNaN(aVar) {
        _validateArguments(1, arguments);
        var aVar=nonCommentArg(1, 1, arguments);
        _assert(commentArg(1, arguments), !isNaN(aVar), "Expected not NaN");
}
function isLoaded() {
        return isTestPageLoaded;
}
function setUp() {
}
function tearDown() {
}
function getFunctionName(aFunction) {
        var name = aFunction.toString().match(/function (\w*)/)[1];
        if ((name == null) || (name.length==0))
                name = "anonymous";
        return name;
}
function getStackTrace() {
        var result = "";
        for (var a = arguments.caller; a !=null; a = a.caller) {
                result += "> "+getFunctionName(a.callee) + "\n";
                if (a.caller == a) {
                        result+="*";
                        break;
                }
        }
        return result;
}
function JsUnitException(comment, message) {
        this.isJsUnitException=true;
        this.comment=comment;
        this.jsUnitMessage=message;
        this.stackTrace=getStackTrace();
}
function JsUnitTestSuite() {
        this.isJsUnitTestSuite=true;
        this.testPages=Array();
        this.pageIndex=0;
}
function addTestPage(pageName) {
        with (this)
        testPages[testPages.length]=pageName;
}
function addTestSuite(suite) {
        with (this)
        for (var i=0; i<suite.testPages.length; i++)
                addTestPage(suite.testPages[i]);
}
function containsTestPages() {
        return this.testPages.length>0;
}
function nextPage() {
        return this.testPages[this.pageIndex++];
}
function hasMorePages() {
        return this.pageIndex<this.testPages.length;
}
JsUnitTestSuite.prototype.addTestPage=addTestPage;
JsUnitTestSuite.prototype.addTestSuite=addTestSuite;
JsUnitTestSuite.prototype.containsTestPages=containsTestPages;
JsUnitTestSuite.prototype.nextPage=nextPage;
JsUnitTestSuite.prototype.hasMorePages=hasMorePages;

function newOnLoadEvent() {
        isTestPageLoaded=true;
}

function warn() {
        if (tracer!=null) tracer.warn(arguments[0], arguments[1]);
}
function inform() {
        if (tracer!=null) tracer.inform(arguments[0], arguments[1]);
}
function debug() {
        if (tracer!=null) tracer.debug(arguments[0], arguments[1]);
}
function setTracer(aTracer) {
        tracer=aTracer;
}

window.onload=newOnLoadEvent;
