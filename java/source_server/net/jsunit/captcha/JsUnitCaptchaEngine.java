package net.jsunit.captcha;

import com.jhlabs.image.RippleFilter;
import com.jhlabs.image.WaterFilter;
import nl.captcha.obscurity.GimpyEngine;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.util.Properties;

public class JsUnitCaptchaEngine implements GimpyEngine {

    public BufferedImage getDistortedImage(BufferedImage bufferedimage) {
        BufferedImage bufferedimage1 = new BufferedImage(bufferedimage.getWidth(), bufferedimage.getHeight(), 2);
        Graphics2D graphics2d = (Graphics2D) bufferedimage1.getGraphics();
        RippleFilter ripplefilter = new RippleFilter();
        ripplefilter.setWaveType(16);
        ripplefilter.setXAmplitude(2.5999999046325684D);
        ripplefilter.setYAmplitude(1.7000000476837158D);
        ripplefilter.setXWavelength(15D);
        ripplefilter.setYWavelength(5D);
        ripplefilter.setEdgeAction(1);
        WaterFilter waterfilter = new WaterFilter();
        waterfilter.setAmplitude(4D);
        waterfilter.setAntialias(true);
        waterfilter.setPhase(15D);
        waterfilter.setWavelength(70D);
        FilteredImageSource filteredimagesource = new FilteredImageSource(bufferedimage.getSource(), waterfilter);
        Image image = Toolkit.getDefaultToolkit().createImage(filteredimagesource);
        filteredimagesource = new FilteredImageSource(image.getSource(), ripplefilter);
        image = Toolkit.getDefaultToolkit().createImage(filteredimagesource);
        graphics2d.drawImage(image, 0, 0, null, null);
        graphics2d.dispose();
        return bufferedimage1;
    }

    public void setProperties(Properties properties) {
    }
}

