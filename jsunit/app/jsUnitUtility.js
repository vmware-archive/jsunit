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

var GECKO_FILE_TIMEOUT = 3000; // milliseconds
var PRETEST_TIMEOUT    = 60000; // milliseconds

function trim(str) {
        if (str==null) return null;
        var startingIndex=0, endingIndex=str.length-1;
        while(str.substring(startingIndex, startingIndex+1) == " ")
                startingIndex++;
        while(str.substring(endingIndex, endingIndex+1) == " ")
                endingIndex--;
        if (endingIndex<startingIndex) return "";
        return str.substring(startingIndex, endingIndex+1);
}
function isBlank(str) {
        return trim(str)=="";
}
//the functions push(anArray, anObject) and pop(anArray) exist because the JavaScript Array.push(anObject) and Array.pop() functions are not available in IE 5.0
function push(anArray, anObject) {
        anArray[anArray.length]=anObject;
}
function pop(anArray) {
        if (anArray.length>=1) {
                delete anArray[anArray.length - 1];
                anArray.length--;
        }
}
function getTestFileProtocol() {
        return getDocumentProtocol();
}
function getDocumentProtocol() {
        var protocol=top.document.location.protocol;
        if (protocol=="file:") return "file:///";
        if (protocol=="http:") return "http://";
        return null;
}
