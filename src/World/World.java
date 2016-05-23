/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package World;

import Entity.EntityManager;
import Core.Game;
import Core.Handler;
import Graphics.ColorPixel;
import Graphics.Display;
import Graphics.Pixel;
import Graphics.PixelUtils;
import Graphics.SpriteBinder;
import Utils.DistanceCalculator;
import Utils.StringUtils;
import java.awt.Graphics;
import java.util.Random;

/**
 *
 * @author Bailey
 */
public class World {
    public String seed;
    public final int width;
    public final int height;
    
    private Display draw;
    private Display heightMap;
    private String[] tileIndex;
    
    //change the world
    int range = 100;
    int islandHeight = 100;
    int islandOffset = 15;
    int totalRange = (range+islandHeight);
    int subDivisions = 5;
    int waterHeight = (int)((0.5)*255);
    int[] heights;
    
    //tiles in the world
    Tile[][] tiles;
        
    public World(String seed){
        this.seed = seed;
        
        int scale = 1;
        int pw = Game.WIDTH/(6*scale);
        int ph = Game.HEIGHT/(6*scale);
        this.width = pw;
        this.height = ph;
        tiles = new Tile[this.width][this.height];
        
        islandHeight += ph/2;
        
        final int subDivisionsX = subDivisions*3;
        final int subDivisionsY = subDivisions*2;
        Pixel[] pixels = new Pixel[pw *ph];
        this.heights = new int[pw *ph];
        this.heightMap = SpriteBinder.loadSprite("heightMap.png");
//        this.tileIndex = StringUtils.loadData("tileMap.txt");
        
        //initialization
        for(int j = 0; j< ph; j++){
            for(int i = 0; i< pw; i++){
                pixels[i+(j * pw)] = new ColorPixel(0).setRGBA(0, 0, 0, 255);
                heights[i+(j+pw)] = 0;
            }
        }
        //gen nodes
        for(int j = 0; j<subDivisionsY; j++){
            for(int i = 0; i<subDivisionsX; i++){
                float value = this.randomPoint(range, i, j);
                int color = (int)((value / range) * 255);
                if(((i * (pw/subDivisionsX)) +((j * (ph/subDivisionsY)) * pw))<(pw*ph)){
                    pixels[((i * (pw/subDivisionsX)) +((j * (ph/subDivisionsY)) * pw))] = new ColorPixel(0).setRGBA(color, color, color, 255);
                }
            }
        }
        //linear interpolation
        //gen nodes
        float maxHeight = 0;
        for(int j = 0; j<subDivisionsY; j++){
            for(int i = 0; i<subDivisionsX; i++){
                if(((i * (pw/subDivisionsX)) +((j * (ph/subDivisionsY)) * pw))<(pw*ph)){
                    //Next step
                    int pt_00 = this.randomPoint(range, i, j);
                    int pt_01 = this.randomPoint(range, i, j+1);
                    int pt_10 = this.randomPoint(range, i+1, j);
                    int pt_11 = this.randomPoint(range, i+1, j+1);
                    
                    for(float y = 0; y<(ph/subDivisionsY); y++){
                        for(float x = 0; x<(pw/subDivisionsX); x++){
                            if((i+(int)x)<pw){
                                if((j+(int)y)<ph){
                                    if((((i * (pw/subDivisionsX))+(int)x) +(((j * (ph/subDivisionsY))+(int)y) * pw))<(pw*ph)){
                                        float stepX = (x/(pw/subDivisionsX)); 
                                        float stepY = (y/(ph/subDivisionsY));
                                        float litX = linearInterpolate(pt_00, pt_01, stepY);
                                        float litY = linearInterpolate(pt_10, pt_11, stepY);
                                        int color = (int)linearInterpolate((litX/totalRange)*255, (litY/totalRange)*255, stepX);
                                        pixels[(((i * (pw/subDivisionsX))+(int)x) +(((j * (ph/subDivisionsY))+(int)y) * pw))] = new ColorPixel(0).setRGBA(color, color, color, 255);
                                        this.heights[(((i * (pw/subDivisionsX))+(int)x) +(((j * (ph/subDivisionsY))+(int)y) * pw))] = color;
                                        if(color>maxHeight){
                                            maxHeight = color;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        //curve to gen island
        for(int y = 0; y<this.height; y++){
            for(int x = 0; x<this.width; x++){
                this.heights[x +(y * this.width)] += linearDecrease(x, y);
                if(this.heights[x +(y * this.width)]<0){
                    this.heights[x +(y * this.width)] = 0;
                }
                if(this.heights[x +(y * this.width)]>maxHeight){
                    maxHeight = this.heights[x +(y * this.width)];
                }
            }   
        }
        //put heightData into the island
        for(int y = 0; y<this.height; y++){
            for(int x = 0; x<this.width; x++){
                int color = (int)((this.heights[x +(y * this.width)] / maxHeight) * 255);
                pixels[x +(y * this.width)]= new ColorPixel(0).setRGBA(color, color, color, 255);
            }   
        }
        //Add The Water
        for(int j = 0; j< ph; j++){
            for(int i = 0; i< pw; i++){
                float progress = PixelUtils.getRGBA(pixels[i+(j * pw)].getColor())[0]/255.0f;
                int color = heightMap.getPixelColor((int)Math.ceil(this.linearInterpolate(heightMap.width, 0, progress)), 0);
                if(color == 0){
                    pixels[i +(j * this.width)].setColor(heightMap.getPixelColor(heightMap.width-1, 0));
                }else{
                    pixels[i +(j * this.width)].setColor(color);
                }
                tiles[i][j] = new Tile(i*TileConstants.size,j*TileConstants.size,  pixels[i +(j * this.width)].getColor());
            }
        }
        
        
        this.draw = new Display(Game.WIDTH, Game.HEIGHT, pw, ph, pixels);
    }
    
    public int randomPoint(int range, int x, int y){
        return new Random(("seed:"+this.seed+",x:"+x+",y:"+(y*15868000)).hashCode()).nextInt(range);
    }
    
    public float linearInterpolate(float start, float end, float progress){
        //the second - the first * .5 + the first
        return ((end-start)*progress)+start;
    }
    
    public float linearDecrease(int x, int y){
        return (1-(DistanceCalculator.CalculateDistanceF(x, y, this.width/2, this.height/2)/DistanceCalculator.CalculateDistanceF(islandOffset*3, islandOffset*2, this.width/2, this.height/2)))*this.islandHeight;
    }
    
    
    //Graphics
    public void renderMap(Graphics g){
        this.draw.render(Game.WIDTH/2, Game.HEIGHT/2, g);
    }
    
    public void renderTiles(Graphics g){
        for(int j = 0; j<(Game.HEIGHT/TileConstants.size)+2; j++){
            for(int i = 0; i<(Game.WIDTH/TileConstants.size)+2; i++){
                if((i+(int)Math.ceil(Handler.cam.x/TileConstants.size))<this.width&&(i+(int)Math.ceil(Handler.cam.x/TileConstants.size))>=0){
                    if((j+(int)Math.ceil(Handler.cam.y/TileConstants.size))<this.height&&(j+(int)Math.ceil(Handler.cam.y/TileConstants.size))>=0){
                        this.tiles[i+(int)Math.ceil(Handler.cam.x/TileConstants.size)][j+(int)Math.floor(Handler.cam.y/TileConstants.size)].render(g);
                    }
                }
            }
        }
    }
}
