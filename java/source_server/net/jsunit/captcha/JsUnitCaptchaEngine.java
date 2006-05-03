package net.jsunit.captcha;

import com.jhlabs.image.RippleFilter;
import com.jhlabs.image.ShadowFilter;
import nl.captcha.obscurity.GimpyEngine;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.util.Properties;
import java.util.Random;

public class JsUnitCaptchaEngine implements GimpyEngine {

    public BufferedImage getDistortedImage(BufferedImage bufferedimage) {
        BufferedImage bufferedimage1 = new BufferedImage(bufferedimage.getWidth(), bufferedimage.getHeight(), 2);
        Graphics2D graphics2d = (Graphics2D) bufferedimage1.getGraphics();
        ShadowFilter shadowfilter = new ShadowFilter();
        shadowfilter.setRadius(10);
        Random random = new Random();
        RippleFilter ripplefilter = new RippleFilter();
        ripplefilter.setWaveType(16);
        ripplefilter.setXAmplitude(7.5999999046325684D);
        ripplefilter.setYAmplitude(random.nextFloat() + 1.0F);
        ripplefilter.setXWavelength(random.nextInt(7) + 8);
        ripplefilter.setYWavelength(random.nextInt(3) + 2);
        ripplefilter.setEdgeAction(1);
        ripplefilter.setEdgeAction(1);
        FilteredImageSource filteredimagesource = new FilteredImageSource(bufferedimage.getSource(), ripplefilter);
        Image image = Toolkit.getDefaultToolkit().createImage(filteredimagesource);
        FilteredImageSource filteredimagesource1 = new FilteredImageSource(image.getSource(), shadowfilter);
        image = Toolkit.getDefaultToolkit().createImage(filteredimagesource1);
        graphics2d.drawImage(image, 0, 0, null, null);
        graphics2d.dispose();
        return bufferedimage1;
    }

    public void setProperties(Properties properties) {
    }
}

