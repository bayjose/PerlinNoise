/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author Bailey
 */
public class Display {
    public int width;
    public int height;
    
    public int rotation = 0;
    
    public final int pWidth;
    public final int pHeight;
    public Pixel[] pixels;
    private BufferedImage image;
    
    public Display(int width, int height, int pWidth, int pHeight, Pixel[] pixels){
        this.width = width;
        this.height = height;
        
        this.pWidth = pWidth;
        this.pHeight = pHeight;
        
        this.rotation = 0;
        
        if(pixels == null){
            pixels = new Pixel[pWidth * pHeight];
            for(int i = 0; i<pixels.length; i++){
                pixels[i] = new ColorPixel(PixelUtils.makeColor(new int[]{0, 0, 0, 0}));
            }
        }
        this.pixels = pixels;
        
        image = new BufferedImage(pWidth, pHeight, BufferedImage.TYPE_INT_ARGB);
        image.setRGB(0, 0, pWidth, pHeight, writeToIntArray(), 0, pWidth);
    }
    
    public Display(int width, int height, int pWidth, int pHeight, Color color){
        this.width = width;
        this.height = height;
        
        this.pWidth = pWidth;
        this.pHeight = pHeight;
        
        this.rotation = 0;
        

        pixels = new Pixel[pWidth * pHeight];
        for(int i = 0; i<pixels.length; i++){
            pixels[i] = new ColorPixel(PixelUtils.makeColor(new int[]{color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()}));
        }
        
        image = new BufferedImage(pWidth, pHeight, BufferedImage.TYPE_INT_ARGB);
        image.setRGB(0, 0, pWidth, pHeight, writeToIntArray(), 0, pWidth);
    }
    
    private int[] writeToIntArray(){
        int[] imageData = new int[pWidth * pHeight];
        
        for(int j = 0; j< pHeight; j++){
            for(int i = 0; i< pWidth; i++){
                imageData[i+(j * pWidth)] = pixels[i+(j * pWidth)].getColor();
            }
        }
        
        return imageData;
    }
    
    public void overlay(Display display){
        if(display.pWidth == this.pWidth && display.pHeight == this.pHeight){
            for(int j = 0; j< pHeight; j++){
                for(int i = 0; i< pWidth; i++){
                    if(display.pixels[i+(j * pWidth)].getColor()<=16777215){
                        pixels[i+(j * pWidth)].setColor(display.pixels[i+(j * pWidth)].getColor());
                    }
                }
            }
            this.update();
        }
    }
    
    public int getPixelColor(int x, int y){
        if(x>=0 && x<this.pWidth){
            if(y>=0 && y<this.pHeight){
                return this.pixels[x+(y * this.pWidth)].getColor();
            } 
        }
        return 0;
    }
    
    public void update(){
        image = new BufferedImage(pWidth, pHeight, BufferedImage.TYPE_INT_ARGB);
        image.setRGB(0, 0, pWidth, pHeight, writeToIntArray(), 0, pWidth);
    }
    
    public void render(int x, int y, Graphics g){
        Graphics2D g2d = (Graphics2D)g;
        
        g.translate(x, y);
            g2d.rotate(Math.toRadians(rotation));
                g.drawImage(image, -width/2, -height/2, width, height, null);
            g2d.rotate(-Math.toRadians(rotation));
        g.translate(-x, -y);
    }
    
    
}
