function toggleVisibility(divId, linkDivId, linkText) {
    var theDiv = document.getElementById(divId);
    var theLinkDiv = document.getElementById(linkDivId);
    var isVisible = theDiv.style.visibility == "visible";
    theLinkDiv.innerHTML = isVisible ? linkText : "hide";
    theDiv.style.visibility = isVisible ? "hidden" : "visible";
}

function toggleBlockVisibility(divId, linkDivId, linkText) {
    var theDiv = document.getElementById(divId);
    var theLinkDiv = document.getElementById(linkDivId);
    var isVisible = theDiv.style.display == "block";
    theLinkDiv.innerHTML = isVisible ? linkText : "hide";
    theDiv.style.display = isVisible ? "none" : "block";
}

function checkedBrowserCount() {
    var result = 0;
    var browserCheckboxes = document.forms[0]["urlId_browserId"];
    for (var i = 0; i < browserCheckboxes.length; i++) {
        var browserCheckbox = browserCheckboxes[i];
        if (browserCheckbox.checked)
            result ++;
    }
    return result;
}

function atLeastOneBrowserIsChecked() {
    return checkedBrowserCount() > 0;
}

function tooManyBrowsersAreChecked() {
    return checkedBrowserCount() > 3;
}