package net.jsunit.utility;

import java.util.ArrayList;
import java.util.List;
import java.net.URL;
import java.net.MalformedURLException;

public class JsUnitURL {

    private String baseURLString;
    private List<Parameter> parameters = new ArrayList<Parameter>();

    public JsUnitURL(String baseURLString) {
        this.baseURLString = baseURLString;
    }

    public void addParameter(String key, String value) {
        parameters.add(new Parameter(key, value));
    }

    public String asString() {
        StringBuffer result = new StringBuffer(baseURLString);
        boolean hasFirstParameter = baseURLString.indexOf("?") !=-1;
        for (Parameter parameter : parameters) {
            if (hasFirstParameter)
                result.append("&");
            else
                result.append("?");
            parameter.appendTo(result);
            hasFirstParameter = true;
        }
        return result.toString();
    }

    public URL asJavaURL() throws MalformedURLException {
        return new URL(asString());
    }

    static class Parameter {
        private String key;
        private String value;

        public Parameter(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public void appendTo(StringBuffer buffer) {
            buffer.append(key).append("=").append(value);
        }
    }
}
