package net.jsunit.captcha;

import nl.captcha.servlet.CaptchaProducer;
import nl.captcha.servlet.Constants;
import nl.captcha.servlet.DefaultCaptchaIml;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

public class CaptchaGenerator {

    private CaptchaProducer producer;

    public CaptchaGenerator() {
        Properties properties = new Properties();
        properties.put(Constants.SIMPLE_CAPTCHA_BOX, "true");
        properties.put(Constants.SIMPLE_CAPTCHA_TEXTPRODUCER_FONTC, "0,0,0");
        properties.put(Constants.SIMPLE_CAPTCHA_BOX_C, "255,255,255");
        properties.put("cap.background.c.from", "255,255,255");
        properties.put("cap.background.c.to", "255,255,255");
        properties.put(Constants.SIMPLE_CAPTCHA_TEXTPRODUCER_CHARR, "a,b,c,d,e,f,h,k,o,p,r,s,t,x,y,z");
        properties.put(Constants.SIMPLE_CAPTCHA_OBSCURIFICATOR, JsUnitCaptchaEngine.class.getName());
        properties.put(Constants.SIMPLE_CAPTCHA_TEXTPRODUCER_CHARRL, "6");
        producer = new DefaultCaptchaIml(properties);
    }

    public void createImage(String answer, OutputStream stream) throws IOException {
        producer.createImage(stream, answer);
    }

    public String generateRandomAnswer() {
        return producer.createText();
    }

}
