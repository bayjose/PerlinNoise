/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics;

import java.awt.Color;

/**
 *
 * @author Bailey
 */
public class PixelUtils {
    public static int[] getRGBA(int color){
        int[] out = new int[4];
        Color c = new Color(color);
        
        int b = c.getBlue();
        int g = c.getGreen();
        int r = c.getRed();
        int a = c.getAlpha();
        
        out[0] = r;
        out[1] = g;
        out[2] = b;
        out[3] = a; 
        
        return out; 
    }
    
    public static int makeColor(int[] color){        
        int b = color[2] * 1;
        int g = color[1] * 256;
        int r = color[0] * 65536;
        int a = color[3] * 16777216;
        
        return r+g+b+a; 
    }
    
    public static int[] normalize(int[] color){
        float largest = 0.0f;
        int[] thisColor = color;
        for(int i = 0; i<thisColor.length; i++){
            if(thisColor[i]>largest){
                largest = thisColor[i];
            }
        }
        for(int i = 0; i<thisColor.length; i++){
            thisColor[i] = (int)((thisColor[i]/largest)*255);
        }
        return thisColor;
    }
}
