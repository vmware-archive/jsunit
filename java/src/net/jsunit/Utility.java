package net.jsunit;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

/**
 * @author Edward Hieatt
 *         <p/>
 *         ***** BEGIN LICENSE BLOCK *****
 *         - Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *         -
 *         - The contents of this file are subject to the Mozilla Public License Version
 *         - 1.1 (the "License"); you may not use this file except in compliance with
 *         - the License. You may obtain a copy of the License at
 *         - http://www.mozilla.org/MPL/
 *         -
 *         - Software distributed under the License is distributed on an "AS IS" basis,
 *         - WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 *         - for the specific language governing rights and limitations under the
 *         - License.
 *         -
 *         - The Original Code is Edward Hieatt code.
 *         -
 *         - The Initial Developer of the Original Code is
 *         - Edward Hieatt, edward@jsunit.net.
 *         - Portions created by the Initial Developer are Copyright (C) 2003
 *         - the Initial Developer. All Rights Reserved.
 *         -
 *         - Author Edward Hieatt, edward@jsunit.net
 *         -
 *         - Alternatively, the contents of this file may be used under the terms of
 *         - either the GNU General Public License Version 2 or later (the "GPL"), or
 *         - the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 *         - in which case the provisions of the GPL or the LGPL are applicable instead
 *         - of those above. If you wish to allow use of your version of this file only
 *         - under the terms of either the GPL or the LGPL, and not to allow others to
 *         - use your version of this file under the terms of the MPL, indicate your
 *         - decision by deleting the provisions above and replace them with the notice
 *         - and other provisions required by the LGPL or the GPL. If you do not delete
 *         - the provisions above, a recipient may use your version of this file under
 *         - the terms of any one of the MPL, the GPL or the LGPL.
 *         -
 *         - ***** END LICENSE BLOCK *****
 * @author Edward Hieatt
 */
public class Utility {
    private static boolean logToStandardOut = true;

    public static boolean isEmpty(String s) {
        return s == null || s.trim().equals("");
    }

    public static void writeFile(String contents, String fileName) {
        writeFile(contents, new File(fileName));
    }

    public static void writeFile(String contents, File file) {
        try {
            if (file.exists())
                file.delete();
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
            out.write(contents.getBytes());
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List listFromCommaDelimitedString(String string) {
        List result = new ArrayList();
        if (isEmpty(string))
            return result;
        StringTokenizer toker = new StringTokenizer(string, ",");
        while (toker.hasMoreTokens())
            result.add(toker.nextToken());
        return result;
    }

    public static void log(String message, boolean includeDate) {
        if (logToStandardOut) {
            StringBuffer buffer = new StringBuffer();
            if (includeDate) {
                buffer.append(new Date());
                buffer.append(": ");
            }
            buffer.append(message);
            System.out.println(buffer.toString());
        }
    }

    public static void log(String message) {
        log(message, true);
    }

    public static void setShouldLogToStandardOut(boolean b) {
        logToStandardOut = b;
    }
}
