package net.jsunit.version;

import java.net.URL;
import java.io.BufferedInputStream;

public class JsUnitWebsiteVersionGrabber implements VersionGrabber {
    public double grabVersion() {
        try {
            URL url = new URL("http://www.jsunit.net/version.txt");
            BufferedInputStream stream = new BufferedInputStream(url.openStream());
            int length = stream.available();
            byte[] array = new byte[length];
            stream.read(array);
            String string = new String(array);

            return Double.parseDouble(string);
        } catch (Throwable t) {
            return 0;
        }
    }
}
