/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Core.Game;
import Core.Handler;
import Graphics.Display;
import Graphics.SpriteBinder;
import World.TileConstants;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 *
 * @author Bailey
 */
public abstract class Entity {
    public int x;
    public int y;
    public int width = TileConstants.size;
    public int height = TileConstants.size;
    private int id=0;
    public Display display;
    
    
    public Entity(int x, int y){
        this.x = x;
        this.y = y;
    }
    
    public int getID(){
        return this.id;
    }
    
    public void setID(int id){
        this.id = id;
    }
    
    
    public abstract void tick();
    public abstract void render(Graphics g);
    
    public void onAdd(){
        return;
    };
    
    public void onRemove(){
        return;
    };
    
    public void onClick(Rectangle rect){
        return;
    }
    
    public void allocateGraphics(String name){
        this.display = SpriteBinder.loadSprite(name);
        this.display.width = this.width;
        this.display.height = this.height;
    }
    
    public Entity setGraphics(String name){
        this.display = SpriteBinder.loadSprite(name);
        this.display.width = this.width;
        this.display.height = this.height;
        return this;
    }
    
    public Rectangle getBounds(){
        return new Rectangle(this.x-(this.display.width)-(Handler.cam.x), this.y-(this.display.height)-(Handler.cam.y), this.width, this.height);
    }
}
