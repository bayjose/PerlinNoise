/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Core;

import Core.Input.Keyboard;
import Core.Input.MouseInput;
import Core.Input.MousePositionLocator;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferStrategy;

/**
 *
 * @author Bayjose
 */
public class Game extends Canvas implements Runnable{
    
    boolean running = false;
    Thread thread;
    
    public static final String NAME = "Scripting Engine";
    public static int WIDTH = 1440;
    public static int HEIGHT = 900;
    
    private Handler handler;

    public synchronized void start(){
        running = true;
        thread = new Thread(this);
        thread.start();
    }
    
    public void run() {
        init();
        long last = System.nanoTime();
        final float ticksPerSecond = 60.0f;
        int frames = 0;
        int ticks = 0;
        long age = System.currentTimeMillis();
        long extra = 0;
        
        while(this.running == true){
            long now = System.nanoTime();
            while((now-last)+extra>=(1000000000.0/ticksPerSecond)){
                ticks++;
                tick();
                extra += (now-last);
                extra -=(1000000000.0/ticksPerSecond);
                last = now;
            }
            render();
            frames++;

            if(System.currentTimeMillis() - age > 1000){
                age = System.currentTimeMillis();
                System.out.println("Ticks:"+ticks+" Frames:"+frames);
                ticks = 0;
                frames = 0;
            }
        }
    }
    
    public void init(){
        this.createBufferStrategy(2);

        this.handler = new Handler();
        this.handler.init();
        this.addMouseListener(new MouseInput());
        this.addKeyListener(new Keyboard());
        this.addMouseMotionListener(new MousePositionLocator());
    }
    
    public void tick(){
        this.handler.tick();
    }
    
    public void render(){
        BufferStrategy buffer = this.getBufferStrategy();
        Graphics g = buffer.getDrawGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
        this.handler.render(g);
        g.dispose();
        buffer.show();
    }
    
    public static Rectangle getScreen(){
        return new Rectangle(0, 0, Game.WIDTH, Game.HEIGHT);
    }
    
    public static void main(String[] args) {
        Window window = new Window(new Game());
    }
}
