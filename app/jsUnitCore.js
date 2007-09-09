/**
 * @fileoverview
 * jsUnitCore.js contains the implementation of the core JsUnit functionality: assertions, JsUnitTestSuites, and JsUnitException.
 * An HTML page is considered to be a JsUnit Test Page if it "includes" jsUnitCore.js, i.e. the following line is present:
 * <code>
 * &lt;script type="text/javascript" src="/path/to/jsUnitCore.js"&gt;&lt;/script&gt;
 * </code>
 * @author Edward Hieatt, edward@jsunit.net, http://www.jsunit.net
 */

/**
 * The JsUnit version
 * @version
 */
var JSUNIT_VERSION = 2.2;

/**
 * For convenience, a variable that equals "undefined"
 */
var JSUNIT_UNDEFINED_VALUE;

/**
 * Whether or not the current test page has been (completely) loaded yet
 */
var isTestPageLoaded = false;

/**
 * Predicate used for testing JavaScript == (i.e. equality excluding type)
 */
var DOUBLE_EQUALITY_PREDICATE = function(var1, var2) {return var1 == var2;};

/**
 * Predicate used for testing JavaScript === (i.e. equality including type)
 */
var TRIPLE_EQUALITY_PREDICATE = function(var1, var2) {return var1 === var2;};

/**
 * Predicate used for testing whether two obects' toStrings are equal
 */
var TO_STRING_EQUALITY_PREDICATE = function(var1, var2) {return var1.toString() === var2.toString();};

/**
 * Hash of predicates for testing equality by primitive type
 */
var PRIMITIVE_EQUALITY_PREDICATES = {
    'String':   DOUBLE_EQUALITY_PREDICATE,
    'Number':   DOUBLE_EQUALITY_PREDICATE,
    'Boolean':  DOUBLE_EQUALITY_PREDICATE,
    'Date':     TRIPLE_EQUALITY_PREDICATE,
    'RegExp':   TO_STRING_EQUALITY_PREDICATE,
    'Function': TO_STRING_EQUALITY_PREDICATE
}

/**
 * Hack for NS62 bug
 * @private
 */
function _jsUnitFixTop() {
    var tempTop = top;
    if (!tempTop) {
        tempTop = window;
        while (tempTop.parent) {
            tempTop = tempTop.parent;
            if (tempTop.top && tempTop.top.jsUnitTestSuite) {
                tempTop = tempTop.top;
                break;
            }
        }
    }
    try {
        window.top = tempTop;
    } catch (e) {
    }
}

_jsUnitFixTop();

/**
 * @param Any object
 * @return String - the type of the given object
 * @private
 */
function _trueTypeOf(something) {
    var result = typeof something;
    try {
        switch (result) {
            case 'string':
                break;
            case 'boolean':
                break;
            case 'number':
                break;
            case 'object':
            case 'function':
                switch (something.constructor) {
                    case new String().constructor:
                        result = 'String';
                        break;
                    case new Boolean().constructor:
                        result = 'Boolean';
                        break;
                    case new Number().constructor:
                        result = 'Number';
                        break;
                    case new Array().constructor:
                        result = 'Array';
                        break;
                    case new RegExp().constructor:
                        result = 'RegExp';
                        break;
                    case new Date().constructor:
                        result = 'Date';
                        break;
                    case Function:
                        result = 'Function';
                        break;
                    default:
                        var m = something.constructor.toString().match(/function\s*([^( ]+)\(/);
                        if (m)
                            result = m[1];
                        else
                            break;
                }
                break;
        }
    }
    finally {
        result = result.substr(0, 1).toUpperCase() + result.substr(1);
        return result;
    }
}

/**
 * @private
 */
function _displayStringForValue(aVar) {
    var result = '<' + aVar + '>';
    if (!(aVar === null || aVar === JSUNIT_UNDEFINED_VALUE)) {
        result += ' (' + _trueTypeOf(aVar) + ')';
    }
    return result;
}

/**
 * @private
 */
function _argumentsIncludeComments(expectedNumberOfNonCommentArgs, args) {
    return args.length == expectedNumberOfNonCommentArgs + 1;
}
/**
 * @private
 */
function _commentArg(expectedNumberOfNonCommentArgs, args) {
    if (_argumentsIncludeComments(expectedNumberOfNonCommentArgs, args))
        return args[0];

    return null;
}
/**
 * @private
 */
function _nonCommentArg(desiredNonCommentArgIndex, expectedNumberOfNonCommentArgs, args) {
    return _argumentsIncludeComments(expectedNumberOfNonCommentArgs, args) ?
           args[desiredNonCommentArgIndex] :
           args[desiredNonCommentArgIndex - 1];
}

/**
 * @private
 */
function _validateArguments(expectedNumberOfNonCommentArgs, args) {
    if (!( args.length == expectedNumberOfNonCommentArgs ||
           (args.length == expectedNumberOfNonCommentArgs + 1 && (typeof(args[0]) == 'string') || args[0] == null)))
        throw new JsUnitAssertionArgumentError('Incorrect arguments passed to assert function');
}

/**
 * @private
 */
function _checkEquals(var1, var2) {
    return var1 === var2;
}

/**
 * @private
 */
function _checkNotUndefined(aVar) {
    return aVar !== JSUNIT_UNDEFINED_VALUE;
}

/**
 * @private
 */
function _checkNotNull(aVar) {
    return aVar !== null;
}

/**
 * All assertions ultimately go through this method.
 * @private
 */
function _assert(comment, booleanValue, failureMessage) {
    if (!booleanValue)
        throw new JsUnitFailure(comment, failureMessage);
}

/**
 * Checks that the given boolean value is true.
 * @param comment optional, displayed in the case of failure
 * @value value that is expected to be true
 * @throws JsUnitFailure if the given value is not true
 * @throws JsUnitInvalidAssertionArgument if the given value is not a boolean or if an incorrect number of arguments is passed
 */
function assert() {
    _validateArguments(1, arguments);
    var booleanValue = _nonCommentArg(1, 1, arguments);

    if (typeof(booleanValue) != 'boolean')
        throw new JsUnitAssertionArgumentError('Bad argument to assert(boolean)');

    _assert(_commentArg(1, arguments), booleanValue === true, 'Call to assert(boolean) with false');
}

/**
 * Synonym for assertTrue
 * @see #assert
 */
function assertTrue() {
    _validateArguments(1, arguments);
    assert(_commentArg(1, arguments), _nonCommentArg(1, 1, arguments));
}

/**
 * Checks that a boolean value is false.
 * @param comment optional, displayed in the case of failure
 * @value value that is expected to be false
 * @throws JsUnitFailure if value is not false
 * @throws JsUnitInvalidAssertionArgument if the given value is not a boolean or if an incorrect number of arguments is passed
 */
function assertFalse() {
    _validateArguments(1, arguments);
    var booleanValue = _nonCommentArg(1, 1, arguments);

    if (typeof(booleanValue) != 'boolean')
        throw new JsUnitAssertionArgumentError('Bad argument to assertFalse(boolean)');

    _assert(_commentArg(1, arguments), booleanValue === false, 'Call to assertFalse(boolean) with true');
}

/**
 * Checks that two values are equal (using ===)
 * @param comment optional, displayed in the case of failure
 * @param expected the expected value
 * @param actual the actual value
 * @throws JsUnitFailure if the values are not equal
 * @throws JsUnitInvalidAssertionArgument if an incorrect number of arguments is passed
 */
function assertEquals() {
    _validateArguments(2, arguments);
    var var1 = _nonCommentArg(1, 2, arguments);
    var var2 = _nonCommentArg(2, 2, arguments);
    _assert(_commentArg(2, arguments), _checkEquals(var1, var2), 'Expected ' + _displayStringForValue(var1) + ' but was ' + _displayStringForValue(var2));
}

/**
 * Checks that two values are not equal (using !==)
 * @param comment optional, displayed in the case of failure
 * @param value1 a value
 * @param value2 another value
 * @throws JsUnitFailure if the values are equal
 * @throws JsUnitInvalidAssertionArgument if an incorrect number of arguments is passed
 */
function assertNotEquals() {
    _validateArguments(2, arguments);
    var var1 = _nonCommentArg(1, 2, arguments);
    var var2 = _nonCommentArg(2, 2, arguments);
    _assert(_commentArg(2, arguments), var1 !== var2, 'Expected not to be ' + _displayStringForValue(var2));
}

/**
 * Checks that a value is null
 * @param comment optional, displayed in the case of failure
 * @param value the value
 * @throws JsUnitFailure if the value is not null
 * @throws JsUnitInvalidAssertionArgument if an incorrect number of arguments is passed
 */
function assertNull() {
    _validateArguments(1, arguments);
    var aVar = _nonCommentArg(1, 1, arguments);
    _assert(_commentArg(1, arguments), aVar === null, 'Expected ' + _displayStringForValue(null) + ' but was ' + _displayStringForValue(aVar));
}

/**
 * Checks that a value is not null
 * @param comment optional, displayed in the case of failure
 * @param value the value
 * @throws JsUnitFailure if the value is null
 * @throws JsUnitInvalidAssertionArgument if an incorrect number of arguments is passed
 */
function assertNotNull() {
    _validateArguments(1, arguments);
    var aVar = _nonCommentArg(1, 1, arguments);
    _assert(_commentArg(1, arguments), _checkNotNull(aVar), 'Expected not to be ' + _displayStringForValue(null));
}

/**
 * Checks that a value is undefined
 * @param comment optional, displayed in the case of failure
 * @param value the value
 * @throws JsUnitFailure if the value is not undefined
 * @throws JsUnitInvalidAssertionArgument if an incorrect number of arguments is passed
 */
function assertUndefined() {
    _validateArguments(1, arguments);
    var aVar = _nonCommentArg(1, 1, arguments);
    _assert(_commentArg(1, arguments), aVar === JSUNIT_UNDEFINED_VALUE, 'Expected ' + _displayStringForValue(JSUNIT_UNDEFINED_VALUE) + ' but was ' + _displayStringForValue(aVar));
}

/**
 * Checks that a value is not undefined
 * @param comment optional, displayed in the case of failure
 * @param value the value
 * @throws JsUnitFailure if the value is undefined
 * @throws JsUnitInvalidAssertionArgument if an incorrect number of arguments is passed
 */
function assertNotUndefined() {
    _validateArguments(1, arguments);
    var aVar = _nonCommentArg(1, 1, arguments);
    _assert(_commentArg(1, arguments), _checkNotUndefined(aVar), 'Expected not to be ' + _displayStringForValue(JSUNIT_UNDEFINED_VALUE));
}

/**
 * Checks that a value is NaN (Not a Number)
 * @param comment optional, displayed in the case of failure
 * @param value the value
 * @throws JsUnitFailure if the value is a number
 * @throws JsUnitInvalidAssertionArgument if an incorrect number of arguments is passed
 */
function assertNaN() {
    _validateArguments(1, arguments);
    var aVar = _nonCommentArg(1, 1, arguments);
    _assert(_commentArg(1, arguments), isNaN(aVar), 'Expected NaN');
}

/**
 * Checks that a value is not NaN (i.e. is a number)
 * @param comment optional, displayed in the case of failure
 * @param value the value
 * @throws JsUnitFailure if the value is not a number
 * @throws JsUnitInvalidAssertionArgument if an incorrect number of arguments is passed
 */
function assertNotNaN() {
    _validateArguments(1, arguments);
    var aVar = _nonCommentArg(1, 1, arguments);
    _assert(_commentArg(1, arguments), !isNaN(aVar), 'Expected not NaN');
}

/**
 * Checks that an object is equal to another using === for primitives and their object counterparts but also desceding
 * into collections and calling assertObjectEquals for each element
 * @param comment optional, displayed in the case of failure
 * @param value the expected value
 * @param value the actual value
 * @throws JsUnitFailure if the actual value does not equal the expected value
 * @throws JsUnitInvalidAssertionArgument if an incorrect number of arguments is passed
 */
function assertObjectEquals() {
    _validateArguments(2, arguments);
    var var1 = _nonCommentArg(1, 2, arguments);
    var var2 = _nonCommentArg(2, 2, arguments);
    var failureMessage = _commentArg(2, arguments) ? _commentArg(2, arguments) : '';
    if (var1 === var2)
        return;

    var isEqual = false;

    var typeOfVar1 = _trueTypeOf(var1);
    var typeOfVar2 = _trueTypeOf(var2);

    if (typeOfVar1 == typeOfVar2) {
        var primitiveEqualityPredicate = PRIMITIVE_EQUALITY_PREDICATES[typeOfVar1];
        if (primitiveEqualityPredicate) {
            isEqual = primitiveEqualityPredicate(var1, var2);
        } else if (var1.length === var2.length) {
            for (var i in var1)
                assertObjectEquals(failureMessage + ' found nested ' + typeOfVar1 + '@' + i + '\n', var1[i], var2[i]);
            isEqual = true;
        }
    }
    _assert(failureMessage, isEqual, 'Expected ' + _displayStringForValue(var1) + ' but was ' + _displayStringForValue(var2));
}

/**
 * Checks that an array is equal to another by checking that both are arrays and then comparing their elements using assertObjectEquals
 * @param comment optional, displayed in the case of failure
 * @param value the expected array
 * @param value the actual array
 * @throws JsUnitFailure if the actual value does not equal the expected value
 * @throws JsUnitInvalidAssertionArgument if an incorrect number of arguments is passed
 */
function assertArrayEquals() {
    _validateArguments(2, arguments);
    var array1 = _nonCommentArg(1, 2, arguments);
    var array2 = _nonCommentArg(2, 2, arguments);
    if (_trueTypeOf(array1) != 'Array' || _trueTypeOf(array2) != 'Array') {
        throw new JsUnitAssertionArgumentError('Non-array passed to assertArrayEquals');
    }
    assertObjectEquals(_commentArg(2, arguments), _nonCommentArg(1, 2, arguments), _nonCommentArg(2, 2, arguments));
}

/**
 * Checks that a value evaluates to true in the sense that value == true
 * @param comment optional, displayed in the case of failure
 * @param value the value
 * @throws JsUnitFailure if the actual value does not evaluate to true
 * @throws JsUnitInvalidAssertionArgument if an incorrect number of arguments is passed
 */
function assertEvaluatesToTrue() {
    _validateArguments(1, arguments);
    var value = _nonCommentArg(1, 1, arguments);
    if (!value)
        fail(_commentArg(1, arguments));
}

/**
 * Checks that a value evaluates to false in the sense that value == false
 * @param comment optional, displayed in the case of failure
 * @param value the value
 * @throws JsUnitFailure if the actual value does not evaluate to true
 * @throws JsUnitInvalidAssertionArgument if an incorrect number of arguments is passed
 */
function assertEvaluatesToFalse() {
    _validateArguments(1, arguments);
    var value = _nonCommentArg(1, 1, arguments);
    if (value)
        fail(_commentArg(1, arguments));
}

/**
 * Checks that a value is the same as an HTML string by "standardizing" both and comparing the result for equality.
 * Standardizing is done by temporarily creating a DIV, setting the innerHTML of the DIV to the string, and asking for
 * the innerHTML back.
 * @param comment optional, displayed in the case of failure
 * @param value1 the expected HTML string
 * @param value2 the actual HTML string
 * @throws JsUnitFailure if the standardized actual value does not equal the standardized expected value
 * @throws JsUnitInvalidAssertionArgument if an incorrect number of arguments is passed
 */
function assertHTMLEquals() {
    _validateArguments(2, arguments);
    var var1 = _nonCommentArg(1, 2, arguments);
    var var2 = _nonCommentArg(2, 2, arguments);
    var var1Standardized = Utilities.standardizeHTML(var1);
    var var2Standardized = Utilities.standardizeHTML(var2);

    _assert(_commentArg(2, arguments), var1Standardized === var2Standardized, 'Expected ' + _displayStringForValue(var1Standardized) + ' but was ' + _displayStringForValue(var2Standardized));
}

/**
 * Checks that a hash is has the same contents as another by iterating over the expected hash and checking that each
 * key's value is present in the actual hash and calling assertEquals on the two values, and then checking that there is
 * no key in the actual hash that isn't present in the expected hash.
 * @param comment optional, displayed in the case of failure
 * @param value the expected hash
 * @param value the actual hash
 * @throws JsUnitFailure if the actual hash does not evaluate to true
 * @throws JsUnitInvalidAssertionArgument if an incorrect number of arguments is passed
 */
function assertHashEquals() {
    _validateArguments(2, arguments);
    var var1 = _nonCommentArg(1, 2, arguments);
    var var2 = _nonCommentArg(2, 2, arguments);
    for (var key in var1) {
        assertNotUndefined("Expected hash had key " + key + " that was not found", var2[key]);
        assertEquals(
            "Value for key " + key + " mismatch - expected = " + var1[key] + ", actual = " + var2[key],
            var1[key], var2[key]
        );
    }
    for (var key in var2) {
        assertNotUndefined("Actual hash had key " + key + " that was not expected", var1[key]);
    }
}

/**
 * Checks that two value are within a tolerance of one another
 * @param comment optional, displayed in the case of failure
 * @param value1 a value
 * @param value1 another value
 * @param tolerance the tolerance
 * @throws JsUnitFailure if the two values are not within tolerance of each other
 * @throws JsUnitInvalidAssertionArgument if an incorrect number of arguments is passed
 */
function assertRoughlyEquals() {
    _validateArguments(3, arguments);
    var expected = _nonCommentArg(1, 3, arguments);
    var actual = _nonCommentArg(2, 3, arguments);
    var tolerance = _nonCommentArg(3, 3, arguments);
    assertTrue(
        "Expected " + expected + ", but got " + actual + " which was more than " + tolerance + " away",
        Math.abs(expected - actual) < tolerance
    );
}

/**
 * Checks that a collection contains a value by checking that collection.indexOf(value) is not -1
 * @param comment optional, displayed in the case of failure
 * @param collection the collection
 * @param value the value
 * @throws JsUnitFailure if the collection does not contain the value
 * @throws JsUnitInvalidAssertionArgument if an incorrect number of arguments are passed
 */
function assertContains() {
    _validateArguments(2, arguments);
    var value = _nonCommentArg(1, 2, arguments);
    var collection = _nonCommentArg(2, 2, arguments);
    assertTrue(
        "Expected '" + collection + "' to contain '" + value + "'",
        collection.indexOf(value) != -1
    );
}

/**
 * Checks that two arrays have the same contents, ignoring the order of the contents
 * @param comment optional, displayed in the case of failure
 * @param array1 first array
 * @param array2 second array
 * @throws JsUnitFailure if the two arrays contain different contents
 * @throws JsUnitInvalidAssertionArgument if an incorrect number of arguments are passed
 */
function assertArrayEqualsIgnoringOrder() {
    _validateArguments(2, arguments);
    var var1 = _nonCommentArg(1, 2, arguments);
    var var2 = _nonCommentArg(2, 2, arguments);

    var notEqualsMessage = "Expected arrays " + _displayStringForValue(var1) + " and " + _displayStringForValue(var2) + " to be equal (ignoring order)";
    var notArraysMessage = "Expected arguments " + _displayStringForValue(var1) + " and " + _displayStringForValue(var2) + " to be arrays";

    _assert(_commentArg(2, arguments), _checkNotNull(var1), notEqualsMessage);
    _assert(_commentArg(2, arguments), _checkNotNull(var2), notEqualsMessage);

    _assert(_commentArg(2, arguments), _checkNotUndefined(var1.length), notArraysMessage);
    _assert(_commentArg(2, arguments), _checkNotUndefined(var1.join), notArraysMessage);
    _assert(_commentArg(2, arguments), _checkNotUndefined(var2.length), notArraysMessage);
    _assert(_commentArg(2, arguments), _checkNotUndefined(var2.join), notArraysMessage);

    _assert(_commentArg(1, arguments), _checkEquals(var1.length, var2.length), notEqualsMessage);

    for (var i = 0; i < var1.length; i++) {
        var found = false;
        for (var j = 0; j < var2.length; j++) {
            try {
                assertObjectEquals(notEqualsMessage, var1[i], var2[j]);
                found = true;
            } catch (ignored) {
            }
        }
        _assert(_commentArg(2, arguments), found, notEqualsMessage);
    }
}

/**
 * Synonym for assertArrayEqualsIgnoringOrder
 * @see #assertArrayEqualsIgnoringOrder
 */
function assertEqualsIgnoringOrder() {
    _validateArguments(2, arguments);
    assertArrayEqualsIgnoringOrder(_commentArg(2, arguments), _nonCommentArg(1, 2, arguments), _nonCommentArg(2, 2, arguments));
}

/**
 * Causes a failure
 * @param failureMessage the message for the failure
 */
function fail(failureMessage) {
    throw new JsUnitFailure("Call to fail()", failureMessage);
}

/**
 * Causes an error
 * @param errorMessage the message for the error
 */
function error(errorMessage) {
    throw new JsUnitError(errorMessage);
}

/**
 * @class
 * A JsUnitFailure represents an assertion failure (or a call to fail()) during the execution of a Test Function
 * @param comment an optional comment about the failure
 * @param message the reason for the failure
 */
function JsUnitFailure(comment, message) {
    /**
     * Declaration that this is a JsUnitFailure
     * @ignore
     */
    this.isJsUnitFailure = true;
    /**
     * An optional comment about the failure
     */
    this.comment = comment;
    /**
     * The reason for the failure
     */
    this.jsUnitMessage = message;
    /**
     * The stack trace at the point at which the failure was encountered
     */
    this.stackTrace = Utilities.getStackTrace();
}

/**
 * @class
 * A JsUnitError represents an error (an exception or a call to error()) during the execution of a Test Function
 * @param description the reason for the failure
 */
function JsUnitError(description) {
    /**
     * The description of the error
     */
    this.description = description;
    /**
     * The stack trace at the point at which the error was encountered
     */
    this.stackTrace = Utilities.getStackTrace();
}

/**
 * @class
 * A JsUnitAssertionArgumentError represents an invalid call to an assertion function - either an invalid argument type
 * or an incorrect number of arguments
 * @param description a description of the argument error
 */
function JsUnitAssertionArgumentError(description) {
    /**
     * A description of the argument error
     */
    this.description = description;
}

function isLoaded() {
    return isTestPageLoaded;
}

/**
 * @private
 */
function setUp() {
}

/**
 * @private
 */
function tearDown() {
}

function warn() {
    if (top.tracer != null)
        top.tracer.warn(arguments[0], arguments[1]);
}

function inform() {
    if (top.tracer != null)
        top.tracer.inform(arguments[0], arguments[1]);
}

function info() {
    inform(arguments[0], arguments[1]);
}

function debug() {
    if (top.tracer != null)
        top.tracer.debug(arguments[0], arguments[1]);
}

/**
 * @class
 * A JsUnitTestSuite represents a suite of JsUnit Test Pages.  Test Pages and Test Suites can be added to a
 * JsUnitTestSuite
 * @constructor
 */
function JsUnitTestSuite() {
    /**
     * Declares that this object is a JsUnitTestSuite
     */
    this.isJsUnitTestSuite = true;
    /**
     * @private
     */
    this._testPages = Array();
    /**
     * @private
     */
    this._pageIndex = 0;

    for (var i = 0; i < arguments.length; i++) {
        if (arguments[i]._testPages) {
            this.addTestSuite(arguments[i]);
        } else {
            this.addTestPage(arguments[i]);
        }
    }
}

/**
 * Adds a Test Page to the suite
 * @param pageName the path to the Test Page
 */
JsUnitTestSuite.prototype.addTestPage = function (pageName) {
    this._testPages[this._testPages.length] = pageName;
}

/**
 * Adds a Test Suite to the suite
 * @param suite another JsUnitTestSuite object
 */

JsUnitTestSuite.prototype.addTestSuite = function (suite) {
    for (var i = 0; i < suite._testPages.length; i++)
        this.addTestPage(suite._testPages[i]);
}

/**
 * Whether the suite contains any Test Pages
 */
JsUnitTestSuite.prototype.containsTestPages = function () {
    return this._testPages.length > 0;
}

/**
 * Moves the suite on to its next Test Page
 */
JsUnitTestSuite.prototype.nextPage = function () {
    return this._testPages[this._pageIndex++];
}

/**
 * Whether the suite has more Test Pages
 */
JsUnitTestSuite.prototype.hasMorePages = function () {
    return this._pageIndex < this._testPages.length;
}

/**
 * Produces a copy of the suite
 */
JsUnitTestSuite.prototype.clone = function () {
    var clone = new JsUnitTestSuite();
    clone._testPages = this._testPages;
    return clone;
}

//For legacy support - JsUnitTestSuite used to be called jsUnitTestSuite
jsUnitTestSuite = JsUnitTestSuite;

function setJsUnitTracer(aJsUnitTracer) {
    top.tracer = aJsUnitTracer;
}

function jsUnitGetParm(name) {
    if (typeof(top.jsUnitParmHash[name]) != 'undefined')
    {
        return top.jsUnitParmHash[name];
    }
    return null;
}

function newOnLoadEvent() {
    isTestPageLoaded = true;
}

function jsUnitSetOnLoad(windowRef, onloadHandler) {
    var isKonqueror = navigator.userAgent.indexOf('Konqueror/') != -1;

    if (typeof(windowRef.attachEvent) != 'undefined') {
        // Internet Explorer, Opera
        windowRef.attachEvent("onload", onloadHandler);
    } else if (typeof(windowRef.addEventListener) != 'undefined' && !isKonqueror) {
        // Mozilla
        // exclude Konqueror due to load issues
        windowRef.addEventListener("load", onloadHandler, false);
    } else if (typeof(windowRef.document.addEventListener) != 'undefined' && !isKonqueror) {
        // DOM 2 Events
        // exclude Mozilla, Konqueror due to load issues
        windowRef.document.addEventListener("load", onloadHandler, false);
    } else if (typeof(windowRef.onload) != 'undefined' && windowRef.onload) {
        windowRef.jsunit_original_onload = windowRef.onload;
        windowRef.onload = function() {
            windowRef.jsunit_original_onload();
            onloadHandler();
        };
    } else {
        // browsers that do not support windowRef.attachEvent or
        // windowRef.addEventListener will override a page's own onload event
        windowRef.onload = onloadHandler;
    }
}

/**
 * @class
 * @constructor
 */
var Utilities = function() {
}

/**
 * Standardizes an HTML string by temporarily creating a DIV, setting its innerHTML to the string, and the asking for
 * the innerHTML back
 * @param html
 */
Utilities.standardizeHTML = function(html) {
    var translator = document.createElement("DIV");
    translator.innerHTML = html;
    return Utilities.trim(translator.innerHTML);
}

Utilities.isBlank = function(str) {
    return Utilities.trim(str) == '';
}

// the functions push(anArray, anObject) and pop(anArray)
// exist because the JavaScript Array.push(anObject) and Array.pop()
// functions are not available in IE 5.0

Utilities.push = function(anArray, anObject) {
    anArray[anArray.length] = anObject;
}

Utilities.pop = function pop(anArray) {
    if (anArray.length >= 1) {
        delete anArray[anArray.length - 1];
        anArray.length--;
    }
}

Utilities.getFunctionName = function(aFunction) {
    var regexpResult = aFunction.toString().match(/function(\s*)(\w*)/);
    if (regexpResult && regexpResult.length >= 2 && regexpResult[2]) {
            return regexpResult[2];
    }
    return 'anonymous';
}

Utilities.getStackTrace = function() {
    var result = '';

    if (typeof(arguments.caller) != 'undefined') { // IE, not ECMA
        for (var a = arguments.caller; a != null; a = a.caller) {
            result += '> ' + Utilities.getFunctionName(a.callee) + '\n';
            if (a.caller == a) {
                result += '*';
                break;
            }
        }
    }
    else { // Mozilla, not ECMA
        // fake an exception so we can get Mozilla's error stack
        try
        {
            foo.bar;
        }
        catch(exception)
        {
            var stack = Utilities.parseErrorStack(exception);
            for (var i = 1; i < stack.length; i++)
            {
                result += '> ' + stack[i] + '\n';
            }
        }
    }

    return result;
}

Utilities.parseErrorStack = function(exception) {
    var stack = [];
    var name;

    if (!exception || !exception.stack) {
        return stack;
    }

    var stacklist = exception.stack.split('\n');

    for (var i = 0; i < stacklist.length - 1; i++) {
        var framedata = stacklist[i];

        name = framedata.match(/^(\w*)/)[1];
        if (!name) {
            name = 'anonymous';
        }

        stack[stack.length] = name;
    }
    // remove top level anonymous functions to match IE

    while (stack.length && stack[stack.length - 1] == 'anonymous') {
        stack.length = stack.length - 1;
    }
    return stack;
}

Utilities.trim = function(str) {
    if (str == null)
        return null;

    var startingIndex = 0;
    var endingIndex = str.length - 1;

    var singleWhitespaceRegex = /\s/;
    while (str.substring(startingIndex, startingIndex + 1).match(singleWhitespaceRegex))
        startingIndex++;

    while (str.substring(endingIndex, endingIndex + 1).match(singleWhitespaceRegex))
        endingIndex--;

    if (endingIndex < startingIndex)
        return '';

    return str.substring(startingIndex, endingIndex + 1);
}

jsUnitSetOnLoad(window, newOnLoadEvent);
