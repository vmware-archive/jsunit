/**
 * 
 */
package net.jsunit;

import java.io.IOException;
import java.net.URL;

import org.jdom.Document;

public class BlowingUpRemoteRunnerHitter implements RemoteRunnerHitter {

    public Document hitURL(URL url) throws IOException {
        throw new IOException();
    }
}