function toggleVisibility(divId, linkDivId, linkText) {
    var theDiv = document.getElementById(divId);
    var theLinkDiv = document.getElementById(linkDivId);
    var isVisible = theDiv.style.visibility == "visible";
    theLinkDiv.innerHTML = isVisible ? linkText :"hide";
    theDiv.style.visibility = isVisible ? "hidden" : "visible";
}
