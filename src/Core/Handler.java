/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Core;

import Camera.Camera;
import Core.Input.Keyboard;
import Entity.EntityManager;
import Entity.Player;
import Graphics.Gui.GuiManager;
import Graphics.Gui.GuiWorldMap;
import World.World;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 *
 * @author Bailey
 */
public class Handler {
    
    public static Camera cam = new Camera(0, 0, 0);
    public static World world;
    private static GuiManager guiManager;
    
    private final int camSpeed = 4;
    int x = 0;
    int y = 0;
    
    
    public void init(){
        this.world = new World("toast");
        this.guiManager = new GuiManager();
        GuiManager.add("map", new GuiWorldMap());
        EntityManager.addEntity(new Player(Game.WIDTH/2, Game.HEIGHT/2));
    }
    
    public void tick(){
        this.guiManager.tick();
        EntityManager.tickAllEntities();
        
        if(Keyboard.Q){
            this.world = new World("toast"+Math.random());
        }
   
    }
    
    public void render(Graphics g){
        //Game rendering
        Graphics2D g2d = (Graphics2D)g;
        g.translate(Game.WIDTH/2, Game.HEIGHT/2);
            g.translate(-cam.x - Game.WIDTH/2, -cam.y - Game.HEIGHT/2);
                this.world.renderTiles(g);
            g.translate(cam.x + Game.WIDTH/2, cam.y + Game.HEIGHT/2);
        g.translate(-Game.WIDTH/2, -Game.HEIGHT/2);
        //Entity
        EntityManager.renderAllEntities(g);
//        g.setColor(Color.yellow);
//        g.drawRect(Game.getScreen().x, Game.getScreen().y, Game.getScreen().width, Game.getScreen().height);
        //Ui
        this.guiManager.render(g);
    }
    
    public static void onClick(Rectangle rect){
        if(guiManager.onClick(rect)){
            EntityManager.clickAllEntities(rect);
        }else{
            if(EntityManager.intersects(rect)){
                System.out.println("Intersection");
                EntityManager.clickAllEntities(rect);
            }else{

            }
        }
    }
}
