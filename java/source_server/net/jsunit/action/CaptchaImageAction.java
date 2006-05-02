package net.jsunit.action;

import com.opensymphony.xwork.Action;
import net.jsunit.JsUnitServer;
import net.jsunit.captcha.CaptchaGenerator;
import net.jsunit.captcha.CaptchaSpec;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class CaptchaImageAction implements Action, JsUnitServerAware {
    private String secretKey;
    private byte[] imageBytes;
    private String encryptedKey;

    public String execute() throws Exception {
        CaptchaSpec key = CaptchaSpec.fromEncryptedKey(secretKey, encryptedKey);
        if (key.isValid()) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            new CaptchaGenerator().createImage(key.getAnswer(), stream);
            this.imageBytes = stream.toByteArray();
            return SUCCESS;
        } else {
            return ERROR;
        }
    }

    public void setCaptchaKey(String encryptedAnswer) {
        this.encryptedKey = encryptedAnswer;
    }

    public InputStream getImageStream() throws IOException {
        return imageBytes != null ? new ByteArrayInputStream(imageBytes) : null;
    }

    public void setJsUnitServer(JsUnitServer server) {
        secretKey = server.getConfiguration().getSecretKey();
    }
}
