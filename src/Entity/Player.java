/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Core.Game;
import Core.Handler;
import Core.Input.Keyboard;
import World.TileConstants;
import java.awt.Graphics;

/**
 *
 * @author Bailey
 */
public class Player extends Entity{
    
    final int walkSpeed = 4;
    final int sprintSpeed = (int)(walkSpeed*2.6);

    public Player(int x, int y) {
        super(x, y);
    }

    @Override
    public void onAdd() {
        this.allocateGraphics("shell.png");
        this.display.width = TileConstants.size;
        this.display.height = (int)(1.5*TileConstants.size);
    }

    @Override
    public void onRemove() {
        
    }

    @Override
    public void tick() {
        
        int speed = this.walkSpeed;
        if(Keyboard.SHIFT){
            speed = this.sprintSpeed;
        }
        
        if(Keyboard.W){
            Handler.cam.y-=speed;
        }
        if(Keyboard.A){
            Handler.cam.x-=speed;
        }
        if(Keyboard.S){
            Handler.cam.y+=speed;
        }
        if(Keyboard.D){
            Handler.cam.x+=speed;
        }
        
        this.x = Handler.cam.x+Game.WIDTH/2;
        this.y = Handler.cam.y+Game.HEIGHT/2;
    }

    @Override
    public void render(Graphics g) {
        this.display.render(x-Handler.cam.x, y-Handler.cam.y, g);
    }
    
}
