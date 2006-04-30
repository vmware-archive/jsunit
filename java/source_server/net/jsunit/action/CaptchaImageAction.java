package net.jsunit.action;

import com.opensymphony.xwork.Action;
import net.jsunit.captcha.AesCipher;
import net.jsunit.captcha.CaptchaGenerator;
import net.jsunit.JsUnitServer;

import java.io.*;

public class CaptchaImageAction implements Action, JsUnitServerAware {
    private String secretKey;
    private byte[] imageBytes;
    private String encryptedAnswer;

    public String execute() throws Exception {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        String plainTextAnswer = new AesCipher(secretKey).decrypt(encryptedAnswer);
        new CaptchaGenerator(secretKey).createImage(plainTextAnswer, stream);
        this.imageBytes = stream.toByteArray();
        return SUCCESS;
    }

    public void setAnswer(String encryptedAnswer) {
        this.encryptedAnswer = encryptedAnswer;
    }

    public InputStream getImageStream() throws IOException {
        return new ByteArrayInputStream(imageBytes);
    }

    public void setJsUnitServer(JsUnitServer server) {
        secretKey = server.getSecretKey();
    }
}
