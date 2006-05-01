package net.jsunit.captcha;

import nl.captcha.servlet.CaptchaProducer;
import nl.captcha.servlet.Constants;
import nl.captcha.servlet.DefaultCaptchaIml;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

public class CaptchaGenerator {

    private AesCipher aes;
    private CaptchaProducer producer;

    public CaptchaGenerator(String secretKey) {
        aes = new AesCipher(secretKey);
        Properties properties = new Properties();
        properties.put(Constants.SIMPLE_CAPTCHA_BOX, "true");
        properties.put(Constants.SIMPLE_CAPTCHA_TEXTPRODUCER_FONTC, "129,0,0");
        properties.put(Constants.SIMPLE_CAPTCHA_BOX_C, "0,0,0");
        properties.put(
                Constants.SIMPLE_CAPTCHA_TEXTPRODUCER_CHARR,
                "a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z"
        );
        producer = new DefaultCaptchaIml(properties);
    }

    public void createImage(String answer, OutputStream stream) throws IOException {
        producer.createImage(stream, answer);
    }

    public String generateKey(long timeMillis, String answer) {
        return aes.encrypt(timeMillis + "_" + answer);
    }

    public String generateRandomAnswer() {
        return producer.createText();
    }

}
