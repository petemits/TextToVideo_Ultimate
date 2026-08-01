import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageProcessor {
    
    public BufferedImage loadImage(String path) throws IOException {
        return ImageIO.read(new File(path));
    }
    
    public void saveImage(BufferedImage image, String path, String format) throws IOException {
        ImageIO.write(image, format, new File(path));
    }
    
    public BufferedImage resize(BufferedImage image, int width, int height) {
        BufferedImage resized = new BufferedImage(width, height, image.getType());
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(image, 0, 0, width, height, null);
        g2d.dispose();
        return resized;
    }
    
    public BufferedImage applyGaussianBlur(BufferedImage image, int radius) {
        if (radius <= 0) return image;
        
        BufferedImage result = new BufferedImage(
            image.getWidth(), image.getHeight(), image.getType()
        );
        
        float[] kernel = new float[radius * 2 + 1];
        float sigma = radius / 3.0f;
        float total = 0.0f;
        
        for (int i = -radius; i <= radius; i++) {
            float value = (float) Math.exp(-(i * i) / (2 * sigma * sigma));
            kernel[i + radius] = value;
            total += value;
        }
        
        for (int i = 0; i < kernel.length; i++) {
            kernel[i] /= total;
        }
        
        for (int y = radius; y < image.getHeight() - radius; y++) {
            for (int x = radius; x < image.getWidth() - radius; x++) {
                float r = 0, g = 0, b = 0;
                
                for (int i = -radius; i <= radius; i++) {
                    int pixel = image.getRGB(x + i, y);
                    float weight = kernel[i + radius];
                    
                    r += ((pixel >> 16) & 0xFF) * weight;
                    g += ((pixel >> 8) & 0xFF) * weight;
                    b += (pixel & 0xFF) * weight;
                }
                
                int newPixel = ((int)r << 16) | ((int)g << 8) | (int)b;
                result.setRGB(x, y, newPixel);
            }
        }
        
        return result;
    }
    
    public BufferedImage applySepia(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;
                
                int tr = (int)(0.393 * r + 0.769 * g + 0.189 * b);
                int tg = (int)(0.349 * r + 0.686 * g + 0.168 * b);
                int tb = (int)(0.272 * r + 0.534 * g + 0.131 * b);
                
                tr = Math.min(255, tr);
                tg = Math.min(255, tg);
                tb = Math.min(255, tb);
                
                int newRgb = (tr << 16) | (tg << 8) | tb;
                result.setRGB(x, y, newRgb);
            }
        }
        
        return result;
    }
    
    public static void main(String[] args) {
        try {
            ImageProcessor processor = new ImageProcessor();
            System.out.println("ImageProcessor initialized successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}