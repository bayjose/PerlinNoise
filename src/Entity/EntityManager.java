/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Core.Game;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.LinkedList;

/**
 *
 * @author Bailey
 */
public class EntityManager {
    private static Entity[] entities = new Entity[]{};
    private static LinkedList<Entity> toAdd = new LinkedList<Entity>();
    
    public static void addEntity(Entity entity){
        entity.onAdd();
        entity.setID(entities.length+toAdd.size());
        toAdd.add(entity);
    }
    
    public static void removeEntity(int id){
        if(id<entities.length && id>=0){
            entities[id].onRemove();
            entities[id] = null;
        }
    }
    
    public static int size(){
        return entities.length;
    }
    
    private static void cleanUp(){
        //To see how many entities were removed this render call
        int size = 0;
        for(int i = 0 ; i < entities.length; i++){
            if(entities[i]!=null){
                size++;
            }
        }
        //synch entities / free up memory
        Entity[] newEntity = new Entity[size];
        int index = 0;
        for(int i = 0; i<entities.length; i++){
            if(entities[i] == null){
                
            }else{
                newEntity[index] = entities[i];
                index++;
            }
        }
        entities = newEntity;
    }
    
    private static void Synch(){
        if(toAdd.size() == 0){
            return;
        }
        Entity[] newEntity = new Entity[entities.length+toAdd.size()];
        for(int i = 0; i<entities.length; i++){
            newEntity[i] = entities[i];
        }
        for(int i = 0; i<toAdd.size(); i++){
            newEntity[i+entities.length] = toAdd.get(i);
        }
        entities = newEntity;
        toAdd.clear();
    }
    
    public static void tickAllEntities(){
        for(Entity entity: EntityManager.entities){
            if(entity.getBounds().intersects(Game.getScreen())){
                entity.tick();
            }
        }
    }
    
    public static void clickAllEntities(Rectangle rect){
        for(Entity entity: EntityManager.entities){
            if(entity.getBounds().intersects(Game.getScreen())){
                entity.onClick(rect);
            }
        }
    }
    
    public static void renderAllEntities(Graphics g){
        Synch();
        boolean flag = false;
        for(Entity entity: EntityManager.entities){
            if(entity!=null){
                if(entity.getBounds().intersects(Game.getScreen())){
                    entity.render(g);
                }
//                g.setColor(Color.ORANGE);
//                g.drawRect(entity.getBounds().x, entity.getBounds().y, entity.getBounds().width, entity.getBounds().height);
            }else{
                flag = true;
            }
        }
        if(flag){
            cleanUp();
        }
    }
    
    public static boolean intersects(Rectangle rect){
        for(Entity entity: EntityManager.entities){
            if(entity.getBounds().intersects(Game.getScreen())){
                if(entity.getBounds().intersects(rect)){
                    return true;
                }
            }
        }
        return false;
    }
    
}
