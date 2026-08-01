import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeometryEffects {
    
    private Random random = new Random();
    
    public BufferedImage applyWaveEffect(BufferedImage image, double frequency, double amplitude) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double offsetX = amplitude * Math.sin(y * frequency);
                int sourceX = (int)(x + offsetX);
                
                if (sourceX >= 0 && sourceX < width) {
                    int rgb = image.getRGB(sourceX, y);
                    result.setRGB(x, y, rgb);
                }
            }
        }
        
        return result;
    }
    
    public BufferedImage applyRippleEffect(BufferedImage image, double centerX, double centerY, 
                                          double wavelength, double amplitude) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double dx = x - centerX;
                double dy = y - centerY;
                double distance = Math.sqrt(dx * dx + dy * dy);
                
                double offset = amplitude * Math.sin(distance * wavelength);
                int sourceX = (int)(x + dx * offset / distance);
                int sourceY = (int)(y + dy * offset / distance);
                
                if (sourceX >= 0 && sourceX < width && sourceY >= 0 && sourceY < height) {
                    int rgb = image.getRGB(sourceX, sourceY);
                    result.setRGB(x, y, rgb);
                }
            }
        }
        
        return result;
    }
    
    public BufferedImage applySwirlEffect(BufferedImage image, double centerX, double centerY, 
                                         double strength) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double dx = x - centerX;
                double dy = y - centerY;
                double distance = Math.sqrt(dx * dx + dy * dy);
                double angle = Math.atan2(dy, dx);
                
                double swirl = strength * distance / 100.0;
                double newAngle = angle + swirl;
                
                int sourceX = (int)(centerX + distance * Math.cos(newAngle));
                int sourceY = (int)(centerY + distance * Math.sin(newAngle));
                
                if (sourceX >= 0 && sourceX < width && sourceY >= 0 && sourceY < height) {
                    int rgb = image.getRGB(sourceX, sourceY);
                    result.setRGB(x, y, rgb);
                }
            }
        }
        
        return result;
    }
    
    public static void main(String[] args) {
        try {
            GeometryEffects effects = new GeometryEffects();
            System.out.println("GeometryEffects initialized successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}