/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package World;

import Core.Game;
import Core.Handler;
import Graphics.ColorPixel;
import Graphics.Display;
import Graphics.Pixel;
import Graphics.PixelUtils;
import Graphics.SpriteBinder;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

/**
 *
 * @author Bailey
 */
public class Tile {
    private Display display;
    private int x;
    private int y;
    private Tiles tile;
    
    public Tile(int x, int y, Tiles tile){
        this.x = x;
        this.y = y;
        this.tile = tile;
        this.display = SpriteBinder.loadSprite(tile.getTexture());
        this.display.width = TileConstants.size;
        this.display.height = TileConstants.size;
    }
    
    public Tile(int x, int y, int color){
        this.x = x;
        this.y = y;
        this.tile = Tiles.CUSTOM;
        
        final int noise = 15;
        
        Pixel[] pixels = new Pixel[ TileConstants.tileLOD *  TileConstants.tileLOD];
        for(int i = 0; i<pixels.length; i++){
            Random r = new Random(i*(x+(y*65536)));
            int[] rgb = PixelUtils.getRGBA(color);
            int offset = r.nextInt(noise) - (int)Math.floor(noise/2);
            rgb[0] = rgb[0] + offset;
            rgb[1] = rgb[1] + offset;
            rgb[2] = rgb[2] + offset;
            
            for(int j = 0; j<rgb.length; j++){
                if(rgb[j]>255){
                   rgb[j] = 255; 
                }
                if(rgb[j]<0){
                   rgb[j] = 0; 
                }
            }
            
            pixels[i] = new ColorPixel(PixelUtils.makeColor(rgb));            
        }
        
        this.display = new Display(0, 0, TileConstants.tileLOD, TileConstants.tileLOD, pixels);
        this.display.width = TileConstants.size;
        this.display.height = TileConstants.size;
    }
    
    public void update(){
        
    }
    
    public void tick(){
        
    }
    
    public boolean Collides(Rectangle rect){
        if(rect.intersects(new Rectangle(x-Handler.cam.x, y-Handler.cam.y, TileConstants.size, TileConstants.size))){
            return true;
        }
        return false;
    }
    
    public void render(Graphics g){
        display.render(x, y, g);
    }
}
