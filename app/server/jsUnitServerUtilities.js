function toggleVisibility(divId, linkDivId, linkText) {
    var theDiv = document.getElementById(divId);
    var theLinkDiv = document.getElementById(linkDivId);
    var isVisible = theDiv.style.display == "block";
    theLinkDiv.innerHTML = isVisible ? linkText :"hide";
    theDiv.style.display = isVisible ? "none" : "block";
}
